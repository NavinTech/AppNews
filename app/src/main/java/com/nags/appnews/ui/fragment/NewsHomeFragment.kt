package com.nags.appnews.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nags.appnews.R
import com.nags.appnews.adapter.NewsHomeAdapter
import com.nags.appnews.databinding.FragmentNewsHomeBinding
import com.nags.appnews.model.ArticleListResponseModel
import com.nags.appnews.network.ApiFailed
import com.nags.appnews.network.ApiSuccess
import com.nags.appnews.network.LoadingState
import com.nags.appnews.ui.dialog.ProgressDialog
import com.nags.appnews.ui.dialog.createDialog
import com.nags.appnews.utils.EventObserver
import com.nags.appnews.utils.SHARE_TYPE
import com.nags.appnews.viewmodel.NewsViewModel

/**
 * Fragment displaying the home screen with a list of news articles.
 */
class NewsHomeFragment : Fragment() {

    private lateinit var binding: FragmentNewsHomeBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsHomeAdapter: NewsHomeAdapter
    private lateinit var progressDialog: ProgressDialog

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state of the fragment.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsHomeBinding.inflate(inflater)
        // Instantiate the ViewModel using ViewModelProvider
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        progressDialog = ProgressDialog()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    private fun shareContent(item: ArticleListResponseModel) {
        val content = resources.getString(R.string.share_content) + item.url
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            content
        )
        sendIntent.type = SHARE_TYPE
        startActivity(sendIntent)
    }

    /**
     * Called when the fragment's activity has been created and the view hierarchy is ready.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState The saved instance state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.loadingState.observe(viewLifecycleOwner) {
            when (it) {
                LoadingState.LOADING -> {
                    progressDialog.showDialog(
                        requireContext(),
                        true, null
                    )
                }
                LoadingState.LOADED -> {
                    progressDialog.dismissDialog()
                    viewModel.refreshComplete()
                }
            }
        }

        viewModel.isRefreshing.observe(viewLifecycleOwner) {
            if (it) {
                lifecycleScope.launchWhenCreated {
                    viewModel.fetchData()
                }
            }
        }

        viewModel.statusCheck.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is ApiSuccess -> {
                    viewModel.refreshComplete()
                }

                is ApiFailed -> {
                    viewModel.refreshComplete()
                    createDialog(
                        requireContext(),
                        requireContext().getString(R.string.alert), it.message,
                        requireContext().getString(R.string.ok), null,
                        positiveListener = { dialog, _ ->
                            dialog.dismiss()
                        }
                    )
                }
            }
        })


        newsHomeAdapter = NewsHomeAdapter(
            NewsHomeAdapter.OnClickListener { item, _ ->
                findNavController().navigate(
                    NewsHomeFragmentDirections.actionNewsHomeFragmentToNewsDetailWebFragment(
                        item.url ?: "", item.title ?: ""
                    )
                )
            },
            NewsHomeAdapter.OnShareClickListener { item, _ ->
                shareContent(item)
            }
        )

        /**
         * Applying LinearLayoutManager to recyclerView
         */
        binding.rvNewsList.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = newsHomeAdapter
        }

        /**
         * Observing newsList and submitting list to newsHomeAdapter
         */
        viewModel.newsList.observe(viewLifecycleOwner) {
            newsHomeAdapter.submitList(it)
        }

    }

    /**
     * Called when the Fragment is no longer started.
     */
    override fun onPause() {
        viewModel.refreshComplete()
        super.onPause()
    }
}

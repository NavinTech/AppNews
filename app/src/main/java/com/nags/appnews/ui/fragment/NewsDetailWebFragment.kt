package com.nags.appnews.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nags.appnews.R
import com.nags.appnews.databinding.FragmentNewsDetailWebBinding
import com.nags.appnews.ui.dialog.ProgressDialog
import com.nags.appnews.ui.dialog.createDialog
import com.nags.appnews.utils.EventObserver
import com.nags.appnews.viewmodel.ToolBarViewModel

/**
 * Fragment displaying the web page for a news article.
 */
class NewsDetailWebFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailWebBinding
    private lateinit var toolBarViewModel: ToolBarViewModel
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailWebBinding.inflate(inflater)
        toolBarViewModel = ViewModelProvider(this)[ToolBarViewModel::class.java]
        progressDialog = ProgressDialog()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.toolBarViewModel = toolBarViewModel
        return binding.root
    }

    /**
     * Called when the fragment's activity has been created and the view hierarchy is ready.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState The saved instance state of the fragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputURL = arguments?.getString("url")
        val title = arguments?.getString("title")

        toolBarViewModel.isCloseButtonVisible.value = View.VISIBLE
        toolBarViewModel.title.value = title

        /**
         * Observing the back button click on toolbar
         */
        toolBarViewModel.closeButtonClicked.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                findNavController().navigateUp()
            }
        })

        if (inputURL != null) {
            loadPage(inputURL)
        }
    }

    /**
     * Loads the web page with the specified URL.
     *
     * @param url The URL of the web page to load.
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPage(url: String) {
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.domStorageEnabled = true

        binding.webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY
        webSettings.setSupportMultipleWindows(true) // This forces ChromeClient enabled.

        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                // Handle received title if needed
            }
        }

        progressDialog.showDialog(
            requireContext(),
            true,
            null
        )

        binding.webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                progressDialog.dismissDialog()
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                progressDialog.dismissDialog()
                createDialog(
                    requireContext(),
                    getString(R.string.alert),
                    description,
                    getString(R.string.ok),
                    null,
                    positiveListener = { dialog, _ ->
                        dialog.dismiss()
                    }
                )
            }
        }

        binding.webView.loadUrl(url)
    }
}

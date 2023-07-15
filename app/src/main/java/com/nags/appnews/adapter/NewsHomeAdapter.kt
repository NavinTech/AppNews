package com.nags.appnews.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nags.appnews.databinding.ItemNewsHomeBinding
import com.nags.appnews.model.ArticleListResponseModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

/**
 * RecyclerView adapter for displaying news articles in a home screen.
 *
 * @param onClickListener Listener to handle item click events.
 */
class NewsHomeAdapter(private val onClickListener: OnClickListener,
                      private val onShareClickListener: OnShareClickListener
) :
    ListAdapter<ArticleListResponseModel, NewsHomeAdapter.ViewHolder>(DiffCallback) {

    /**
     * DiffUtil callback for calculating the difference between old and new items.
     */
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ArticleListResponseModel>() {
            override fun areItemsTheSame(oldItem: ArticleListResponseModel, newItem: ArticleListResponseModel): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: ArticleListResponseModel, newItem: ArticleListResponseModel): Boolean {
                return oldItem.source.id == newItem.source.id
            }
        }
    }

    /**
     * Creates a ViewHolder for the item view.
     *
     * @param parent The parent ViewGroup.
     * @param viewType The view type of the new view.
     * @return The created ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Binds the data to the item view at the specified position.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.bindText(item.title, item.author)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item, position)
        }
        holder.binding.ivShare.setOnClickListener{
            onShareClickListener.onClick(item, position)
        }
        setImage(holder, item)

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun setImage(holder: ViewHolder, item: ArticleListResponseModel) {
        // Load image using Kotlin coroutines
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = item.urlToImage
                val inputStream = URL(imageUrl).openStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)

                // Display the image on the ImageView
                launch(Dispatchers.Main) {
                    holder.binding.imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle any errors that occur during image loading
            }
        }
    }

    /**
     * ViewHolder class for the item view.
     *
     * @param binding The ViewBinding for the item view.
     */
    inner class ViewHolder(val binding: ItemNewsHomeBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the article data to the item view.
         *
         * @param item The ArticleListResponseModel object representing the article data.
         */
        fun bind(item: ArticleListResponseModel) {
            binding.item = item
            binding.executePendingBindings()
        }

        /**
         * Binds the article title and author to the respective TextViews.
         *
         * @param title The article title.
         * @param author The article author.
         */
        fun bindText(title: String?, author: String?) {
            binding.headlineTextView.text = title
            binding.descriptionTextView.text = author
        }
    }

    /**
     * OnClickListener class to handle item click events.
     *
     * @param clickListener The listener function to handle the click event.
     */
    class OnClickListener(private val clickListener: (model: ArticleListResponseModel, position: Int) -> Unit) {
        /**
         * Handles the click event for an item.
         *
         * @param model The ArticleListResponseModel object representing the clicked item's data.
         * @param position The position of the clicked item in the list.
         */
        fun onClick(model: ArticleListResponseModel, position: Int) {
            clickListener.invoke(model, position)
        }
    }

    /**
     * OnClickListener class to handle item click events.
     *
     * @param clickListener The listener function to handle the click event.
     */
    class OnShareClickListener(private val clickListener: (model: ArticleListResponseModel, position: Int) -> Unit) {
        /**
         * Handles the click event for an item.
         *
         * @param model The ArticleListResponseModel object representing the clicked item's data.
         * @param position The position of the clicked item in the list.
         */
        fun onClick(model: ArticleListResponseModel, position: Int) {
            clickListener.invoke(model, position)
        }
    }
}

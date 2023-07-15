package com.nags.appnews.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nags.appnews.model.ArticleListResponseModel
import com.nags.appnews.model.SourceResponseModel
import com.nags.appnews.network.ApiState
import com.nags.appnews.network.LoadingState
import com.nags.appnews.network.fetchDataInBackground
import com.nags.appnews.utils.API_URL
import com.nags.appnews.utils.Event
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * ViewModel for the NewsFragment.
 */
class NewsViewModel : ViewModel() {

    private val _newsList = MutableLiveData<List<ArticleListResponseModel>>()
    val newsList: LiveData<List<ArticleListResponseModel>>
        get() = _newsList

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _statusCheck = MutableLiveData<Event<ApiState>>()
    val statusCheck: LiveData<Event<ApiState>>
        get() = _statusCheck

    init {
        refreshComplete()
        fetchData()
    }

    /**
     * Called when the user triggers a refresh action.
     */
    fun onRefresh() {
        _isRefreshing.value = true
    }

    /**
     * Marks the refresh action as complete.
     */
    fun refreshComplete() {
        _isRefreshing.postValue(false)
    }

    /**
     * Fetches the news data from the API.
     */
    fun fetchData() {
        viewModelScope.launch {
            _loadingState.value = LoadingState.LOADING
            val result =
                fetchDataInBackground(API_URL)
            result?.let {
                parseJSONData(result)
            }
            _loadingState.postValue(LoadingState.LOADED)
        }
    }

    /**
     * Parses the JSON response and updates the news list.
     *
     * @param jsonResponse The JSON response to parse.
     */
    private fun parseJSONData(jsonResponse: String) {
        try {
            val articles = mutableListOf<ArticleListResponseModel>()
            val jsonObject = JSONObject(jsonResponse)
            val articlesArray = jsonObject.getJSONArray("articles")

            // Loop through each article
            for (i in 0 until articlesArray.length()) {
                val articleObject = articlesArray.getJSONObject(i)
                val sourceObject = articleObject.getJSONObject("source")
                val source = SourceResponseModel(
                    sourceObject.optString("id"),
                    sourceObject.getString("name")
                )

                val article = ArticleListResponseModel(
                    source,
                    articleObject.getString("author"),
                    articleObject.getString("title"),
                    articleObject.getString("description"),
                    articleObject.getString("url"),
                    articleObject.getString("urlToImage"),
                    articleObject.getString("publishedAt"),
                    articleObject.getString("content")
                )

                articles.add(article)
            }
            _newsList.postValue(articles)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

package com.nags.appnews.model

/**
 * Model class representing the response for a list of articles.
 *
 * @param status The status of the response.
 * @param articles The list of articles in the response.
 */
data class ArticleResponseModel(
    val status: String?,
    val articles: List<ArticleListResponseModel?>
)

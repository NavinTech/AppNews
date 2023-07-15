package com.nags.appnews.model

/**
 * Model class representing an article in the list response.
 *
 * @param source The source of the article.
 * @param author The author of the article.
 * @param title The title of the article.
 * @param description The description of the article.
 * @param url The URL of the article.
 * @param urlToImage The URL of the article's image.
 * @param publishedAt The publishing date and time of the article.
 * @param content The content of the article.
 */
data class ArticleListResponseModel(
    val source: SourceResponseModel,
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)

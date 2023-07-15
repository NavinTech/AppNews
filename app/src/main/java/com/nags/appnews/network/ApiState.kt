package com.nags.appnews.network

/**
 * Sealed class representing the state of an API response.
 */
sealed class ApiState

/**
 * Represents a successful API response.
 *
 * @param message The success message.
 */
data class ApiSuccess(val message: String) : ApiState()

/**
 * Represents a failed API response.
 *
 * @param message The error message.
 */
data class ApiFailed(val message: String) : ApiState()

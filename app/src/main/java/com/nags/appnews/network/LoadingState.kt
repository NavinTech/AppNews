package com.nags.appnews.network

/**
 * Data class representing the loading state of an API request.
 *
 * @param status The current status of the request.
 * @param msg An optional message associated with the status.
 */
data class LoadingState(val status: Status, val msg: String? = null) {
    /**
     * Defines the possible status values for the loading state.
     */
    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED
    }

    companion object {
        /**
         * Represents a loaded state.
         */
        val LOADED = LoadingState(Status.SUCCESS)

        /**
         * Represents a loading state.
         */
        val LOADING = LoadingState(Status.RUNNING)

    }
}

package com.offline.continentalrecipesusingnavgraph.data.remote

import retrofit2.Response

data class SimpleResponse<T> (
    val status: Status,
    val data: Response<T>?,
    val exception: Exception?
) {

    companion object {
        fun <T> success(data: Response<T>): SimpleResponse<T> {
            return SimpleResponse(
                status = Status.Success,
                data = data,
                exception = null
            )
        }

        fun <T> failure(exception: Exception): SimpleResponse<T> {
            return SimpleResponse(
                status = Status.Failed,
                data = null,
                exception = exception
            )
        }
    }

    sealed class Status{
        object Success: Status()
        object Failed: Status()
    }

    val failed: Boolean
        get() = this.status == Status.Failed

    val isSuccessful: Boolean
        get() = !failed && this.status == Status.Success

    val body: T?
        get() = data?.body()
}

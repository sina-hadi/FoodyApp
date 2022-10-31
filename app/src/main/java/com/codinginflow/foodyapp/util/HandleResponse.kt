package com.codinginflow.foodyapp.util

import android.util.Log
import retrofit2.Response
import javax.inject.Inject

class HandleResponse<T> @Inject constructor(
    response: Response<T>
) {
    val handleResponse: NetworkResult<T> = when {
        response.message().toString().contains("timeout") -> {
            NetworkResult.Error("Timeout")
        }
        response.code() == 401 -> {
            NetworkResult.Error("API Key Limited.")
        }
        response.isSuccessful -> {
            val foodJoke = response.body()
            NetworkResult.Success(foodJoke!!)
        }
        else -> {
            NetworkResult.Error(response.message())
        }
    }
}
package ru.zemlyanaya.getonbus.mainactivity.model

import android.util.Log
import retrofit2.Response
import java.io.IOException

open class BaseRemoteRepository{

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {

        val result : Result<T> = safeApiResult(call, errorMessage)
        var data : T? = null

        when(result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Log.e("RemoteRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }

        return data
    }

    private suspend fun <T: Any> safeApiResult(call: suspend ()-> Response<T>, errorMessage: String) : Result<T>{
        val response = try {
            call.invoke()
        } catch (e: Exception){
            return Result.Error(e)
        }
        if(response.isSuccessful)
            return Result.Success(response.body()!!)

        return Result.Error(IOException("Error Occurred during getting safe Api result"))
    }
}
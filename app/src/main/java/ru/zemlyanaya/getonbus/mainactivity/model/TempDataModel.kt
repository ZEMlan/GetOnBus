package ru.zemlyanaya.getonbus.mainactivity.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("userId", "id", "title", "body")
data class PlaceholderPosts(

    @JsonProperty("userId")
    var userId: Int? = null,

    @JsonProperty("id")
    var id: Int? = null,

    @JsonProperty("title")
    var title: String? = null,

    @JsonProperty("body")
    var body: String? = null
    )


//A retrofit Network Interface for the Api
interface PlaceholderApi{
    @GET("/posts")
    fun getPostsAsync() : Deferred<Response<List<PlaceholderPosts>>>
}


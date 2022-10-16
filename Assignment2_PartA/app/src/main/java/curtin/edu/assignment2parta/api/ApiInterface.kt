package curtin.edu.assignment2parta.api

import curtin.edu.assignment2parta.data.User
import curtin.edu.assignment2parta.data.UserItem
import curtin.edu.assignment2parta.data.posts.Post
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("users")
    fun fetchUsers(): Call<User>

    @GET("posts")
    fun fetchPosts(): Call<Post>
}
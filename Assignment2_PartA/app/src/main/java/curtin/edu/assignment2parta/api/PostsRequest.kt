package curtin.edu.assignment2parta.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import curtin.edu.assignment2parta.data.posts.Post
import curtin.edu.assignment2parta.data.posts.PostItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
private const val TAG = "PostsRequest"
class PostsRequest : Callable<LiveData<List<PostItem>>> {
    private val myApi : ApiInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        myApi = retrofit.create(ApiInterface::class.java) //uses API interface here
        }

    override fun call(): LiveData<List<PostItem>> {
        val responseLiveData: MutableLiveData<List<PostItem>> = MutableLiveData()
        val apiRequest: Call<Post> = myApi.fetchPosts()

        apiRequest.enqueue(object : Callback<Post>{
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.e(TAG, "Failed to fetch posts", t)            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                Log.d(TAG, "Response received")
                val apiResponse: Post? = response.body()
                //val userResponse: UserResponse? = apiResponse?.users
                var posts: List<PostItem>? = apiResponse
                responseLiveData.value = posts
            }
        })
        return responseLiveData
    }

}
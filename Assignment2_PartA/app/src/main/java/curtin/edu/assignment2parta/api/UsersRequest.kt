package curtin.edu.assignment2parta.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import curtin.edu.assignment2parta.data.User
import curtin.edu.assignment2parta.data.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
private const val TAG = "ApiRequest"
class UsersRequest : Callable<LiveData<List<UserItem>>> {
    private val myApi : ApiInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        myApi = retrofit.create(ApiInterface::class.java) //uses API interface here
        }

//    fun fetchData(): LiveData<List<UserItem>>{
//        val responseLiveData: MutableLiveData<List<UserItem>> = MutableLiveData()
//        val flickrRequest: Call<User> = myApi.fetchUsers()
//
//        flickrRequest.enqueue(object : Callback<User>{
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                Log.e(TAG, "Failed to fetch users", t)
//            }
//
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                Log.d(TAG, "Response received")
//                val apiResponse: User? = response.body()
//                //val userResponse: UserResponse? = apiResponse?.users
//                var users: List<UserItem>? = apiResponse
//                responseLiveData.value = users
//            }
//        })
//        return responseLiveData
//    }

    override fun call(): LiveData<List<UserItem>> {
        val responseLiveData: MutableLiveData<List<UserItem>> = MutableLiveData()
        val apiRequest: Call<User> = myApi.fetchUsers()

        apiRequest.enqueue(object : Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, "Failed to fetch users", t)
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d(TAG, "Response received")
                val apiResponse: User? = response.body()
                //val userResponse: UserResponse? = apiResponse?.users
                var users: List<UserItem>? = apiResponse
                responseLiveData.value = users
            }
        })
        return responseLiveData
    }

}
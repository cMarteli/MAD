package curtin.edu.assignment2parta

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import curtin.edu.assignment2parta.api.PostsRequest
import curtin.edu.assignment2parta.api.UsersRequest
import curtin.edu.assignment2parta.data.UserItem
import curtin.edu.assignment2parta.data.posts.PostItem
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

private const val TAG = "ItemViewModel"
/**
 * [ItemViewModel] holds list of values
 */
class ItemViewModel : ViewModel(){

    lateinit var usersListLiveData: LiveData<List<UserItem>>
    lateinit var postsListLiveData: LiveData<List<PostItem>>

    init {
        //usersListLiveData = ApiRequest().fetchData()

        val service : ExecutorService = Executors.newFixedThreadPool(2)
        val future : Future<LiveData<List<UserItem>>> = service.submit(UsersRequest())

        try {
            usersListLiveData = future.get()
        } catch (ex : ExecutionException) {
            Log.d(TAG, "Error obtaining user data from thread")
        }

        val service2 : ExecutorService = Executors.newFixedThreadPool(2)
        val future2 : Future<LiveData<List<PostItem>>> = service2.submit(PostsRequest())
        try {
            postsListLiveData = future2.get()
        } catch (ex : ExecutionException) {
            Log.d(TAG, "Error obtaining post data from thread")
        }
    }
    //returns a list from posts livedata filtered by id, num = id
    fun getFilteredList(num : Int) : List<PostItem> {
        val filteredList = mutableListOf<PostItem>()
        if (postsListLiveData.value != null) {
            for(post in postsListLiveData.value!!) {
                if (post.userId == num) {
                    filteredList.add(post)
                }
            }
        }
        return filteredList
    }
}
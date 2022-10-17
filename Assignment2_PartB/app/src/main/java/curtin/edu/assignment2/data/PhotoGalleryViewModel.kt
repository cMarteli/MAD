package curtin.edu.assignment2.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import curtin.edu.assignment2.api.FlickrFetchr
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.ExecutionException

private const val TAG = "PhotoGalleryViewModel"
/**
 * [PhotoGalleryViewModel] holds information on images obtained from the API.
 * ensures API requests are only made at the creation of view model
 */
class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {
    var galleryItemLiveData : LiveData<List<GalleryItem>>

    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    //used to pre-populate the search text box with the last saved query
    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""

    init {
        galleryItemLiveData = MutableLiveData()
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)


        //rxjava implementation
        val searchTask = FlickrFetchr()
        var searchObservable = Single.fromCallable(searchTask)
        searchObservable = searchObservable.subscribeOn(Schedulers.io())
        searchObservable = searchObservable.observeOn(AndroidSchedulers.mainThread())
        searchObservable.subscribe()
        (object : SingleObserver<LiveData<List<GalleryItem>>> {
            override fun onSubscribe(d: Disposable) { }
            override fun onSuccess(s: LiveData<List<GalleryItem>>) {
                try {
                    setData(s)
                } catch (ex: ExecutionException) {
                    Log.d(TAG, "Error obtaining post data from thread")
                }
            }
            override fun onError(e: Throwable) {
                Log.d(TAG, "Error obtaining post data from thread")
            }
        })

        try {
            loadImages()
        } catch (ex: ExecutionException) {
            Log.d(TAG, "Error initializing")
        }

    }

    fun setData(s : LiveData<List<GalleryItem>>) {
        galleryItemLiveData = s
    }
    @Throws(ExecutionException::class)
    private fun loadImages() {
        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) { searchTerm ->
                if (searchTerm.isBlank()) { //if search string is empty show flickr.interestingness.getList
                    flickrFetchr.call()
                } else {
                    flickrFetchr.call(searchTerm)
                }
            }
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }
}
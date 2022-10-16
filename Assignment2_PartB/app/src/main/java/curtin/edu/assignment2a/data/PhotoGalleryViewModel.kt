package curtin.edu.assignment2a.data

import android.app.Application
import androidx.lifecycle.*
import curtin.edu.assignment2a.api.FlickrFetchr
import retrofit2.http.Query

/**
 * [PhotoGalleryViewModel] holds information on images obtained from the API.
 * ensures API requests are only made at the creation of view model
 */
class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {
    val galleryItemLiveData: LiveData<List<GalleryItem>>

    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    //used to pre-populate the search text box with the last saved query
    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""

    init {
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)

        galleryItemLiveData =
            Transformations.switchMap(mutableSearchTerm) { searchTerm ->
                if(searchTerm.isBlank()) { //if search string is empty show flickr.interestingness.getList
                    flickrFetchr.fetchPhotos()
                } else {
                    flickrFetchr.searchPhotos(searchTerm)
                }
            }
    }
    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }
}
package curtin.edu.assignment2a.api

import com.google.gson.annotations.SerializedName
import curtin.edu.assignment2a.data.GalleryItem

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems: List<GalleryItem>
}
package curtin.edu.assignment2.api

import com.google.gson.annotations.SerializedName
import curtin.edu.assignment2.data.GalleryItem

class PhotoResponse {
    @SerializedName("photo")
    lateinit var galleryItems: List<GalleryItem>
}
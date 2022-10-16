package curtin.edu.prac7

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import java.io.File

class SharedViewModel : ViewModel() {

    lateinit var photoFile: Bitmap
    lateinit var thumbnail: Bitmap
    var fileExists = false
    var thumbExists = false

    fun savePhoto(inFile : Bitmap){
        photoFile = inFile
        fileExists = true
    }

    fun saveThumbnail(inFile : Bitmap){
        thumbnail = inFile
        thumbExists = true
    }

}
package curtin.edu.assignment2.api

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.drawToBitmap
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.util.*


class ImageUploader {
    private var mStorageRef: StorageReference =
        FirebaseStorage.getInstance().reference //Firebase reference

    fun upload(inContext: Context?, image : ImageView, desc: String) {
        val bitmap: Bitmap = image.drawToBitmap()

        val uriFile = getImageUri(inContext, bitmap)

        val path = "images/" + UUID.randomUUID() + ".jpg"
        val metadata = StorageMetadata()
        metadata.getCustomMetadata(desc)
        val testRef: StorageReference = mStorageRef.child(path)

        val uploadTask : UploadTask = testRef.putFile(uriFile!!)

        uploadTask.addOnSuccessListener {
            Toast.makeText(inContext,"Saved File",Toast.LENGTH_SHORT).show()
        }

    }

    fun showLink()
    {

    }

    private fun getImageUri(inContext: Context?, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext?.contentResolver,
            inImage,
            "",
            null
        )
        return Uri.parse(path)
    }
}
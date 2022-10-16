package curtin.edu.prac7

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File


private const val REQUEST_PHOTO = 3

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class PhotoFragment : Fragment() {

    lateinit var photoFile: File
    lateinit var cameraButton: FloatingActionButton
    lateinit var photoView: ImageView
    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraButton = view.findViewById(R.id.camera_btn)
        photoView = view.findViewById(R.id.thumbnail_imgView)
        //photo button click
        cameraButton.setOnClickListener { photoButtonClicked(view.context) }

        if (sharedViewModel.fileExists){
            photoView.setImageBitmap(sharedViewModel.photoFile)
        }
    }

    private fun photoButtonClicked(c : Context) {
        photoFile = File(c.filesDir, "photo.jpg")
        val cameraUri: Uri = FileProvider.getUriForFile(
            c.applicationContext,BuildConfig.APPLICATION_ID + ".fileprovider",photoFile)
        val photoIntent = Intent()
        photoIntent.action = MediaStore.ACTION_IMAGE_CAPTURE
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        val pm = c.packageManager
        for (a in pm.queryIntentActivities(
            photoIntent, PackageManager.MATCH_DEFAULT_ONLY
        )) {
            c.grantUriPermission(
                a.activityInfo.packageName, cameraUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
        startActivityForResult(photoIntent, REQUEST_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PHOTO && resultCode == AppCompatActivity.RESULT_OK) {
            val photo = BitmapFactory.decodeFile(photoFile.toString())
            sharedViewModel.savePhoto(photo) //saves to viewmodel
            photoView.setImageBitmap(photo)
        }
    }

}
package curtin.edu.prac7

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

const val REQUEST_THUMBNAIL = 1
/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class ThumbnailFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: SharedViewModel by activityViewModels()

    lateinit var thumbnail: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thumbnail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val captureImage: Button = view.findViewById(R.id.captureImage)
        thumbnail = view.findViewById(R.id.imageThumbNile)

        captureImage.setOnClickListener {
            val intent = Intent()
            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            startActivityForResult(intent, REQUEST_THUMBNAIL)
        }
        if (sharedViewModel.thumbExists){
            thumbnail.setImageBitmap(sharedViewModel.thumbnail)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_THUMBNAIL) {
            val image = data?.extras!!["data"] as Bitmap?
            if (image != null) {
                sharedViewModel.saveThumbnail(image) //saves to viewmodel
                thumbnail.setImageBitmap(image)
            }
        }
    }

}
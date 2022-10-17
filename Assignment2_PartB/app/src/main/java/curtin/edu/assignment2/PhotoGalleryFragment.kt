package curtin.edu.assignment2

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import curtin.edu.assignment2.api.ImageUploader
import curtin.edu.assignment2.api.ThumbnailDownloader
import curtin.edu.assignment2.data.GalleryItem
import curtin.edu.assignment2.data.PhotoGalleryViewModel
import java.io.File


private const val TAG = "PhotoGalleryFragment"
private const val COL_COUNT = "param1"
/**
 * A simple [Fragment] subclass.
 * TODO: Continue from book page 553
 */
class PhotoGalleryFragment : Fragment() {

    private val photoGalleryViewModel: PhotoGalleryViewModel by activityViewModels()
    private lateinit var photoRecyclerView: RecyclerView
    private lateinit var thumbnailDownloader: ThumbnailDownloader<ViewHolder>
    private var colCount: Int = 0
    //private lateinit var mStorageRef: StorageReference //Firebase reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            colCount = it.getInt(COL_COUNT)
        }
        retainInstance = true
        setHasOptionsMenu(true)
        val responseHandler = Handler()
        thumbnailDownloader = //sets thumbnail
            ThumbnailDownloader(responseHandler) {photoHolder, bitmap ->
                val drawable = BitmapDrawable(resources, bitmap)
                photoHolder.image.setImageDrawable(drawable)
            }
        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)

        //mStorageRef = FirebaseStorage.getInstance().reference; //Initializes Firebase reference
    }

    /**
     * Draws options menu from XML file
     * takes search queries from user
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        searchView.apply {

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $queryText")
                    photoGalleryViewModel.fetchPhotos(queryText)
                    return true
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    Log.d(TAG, "QueryTextChange: $queryText")
                    return false
                }
            })
            setOnSearchClickListener {//pre-populates the search box
                searchView.setQuery(photoGalleryViewModel.searchTerm, false)
            }
        }
    }

    //clears the stored query (set it to "") when the user selects the Clear Search item
    // from the overflow menu.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                photoGalleryViewModel.fetchPhotos("")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewLifecycleOwner.lifecycle.addObserver(thumbnailDownloader.viewLifecycleObserver)
        val view = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        photoRecyclerView = view.findViewById(R.id.photo_recycler_view)
        photoRecyclerView.layoutManager = GridLayoutManager(context, colCount)
        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                photoRecyclerView.adapter = PhotoAdapter(galleryItems)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwner.lifecycle.removeObserver(
            thumbnailDownloader.viewLifecycleObserver
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(thumbnailDownloader.fragmentLifecycleObserver)
    }

    /**
     * [ViewHolder] Inner class - View Holder for recyclerView
     */
    private inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val image = itemView.findViewById(R.id.imageView_item) as ImageView
            val title = itemView.findViewById(R.id.textView_item) as TextView

            init {
                image.setOnClickListener { //displays title on click
                    val name = (bindingAdapter as PhotoAdapter).getGalleryItem(bindingAdapterPosition).title
                    Toast.makeText(context, name, Toast.LENGTH_SHORT).show()

                    val iup = ImageUploader()
                    iup.upload(context, image, name)

                }
            }
        }

    /**
     * [PhotoAdapter] Inner class - Adapter for recyclerView
     */
    private inner class PhotoAdapter(private val galleryItems: List<GalleryItem>)
        : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(R.layout.list_item_gallery,parent,false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val galleryItem = galleryItems[position]
            holder.title.text = galleryItem.id //TODO: Debug
            thumbnailDownloader.queueThumbnail(holder, galleryItem.url)

        }


        override fun getItemCount(): Int = galleryItems.size

        fun getGalleryItem(index : Int) : GalleryItem {
            return galleryItems[index]
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(colCount: Int) =
            PhotoGalleryFragment().apply {
                arguments = Bundle().apply {
                    putInt(COL_COUNT, colCount)
                }
            }
    }
}
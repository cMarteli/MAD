package curtin.edu.assignment2parta

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import curtin.edu.assignment2parta.data.posts.PostItem
import curtin.edu.assignment2parta.databinding.FragmentPostsBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_ID = "id"
/**
 * A fragment representing the details from each user.
 */
class PostsFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: ItemViewModel by activityViewModels()
    private var columnCount = 1
    private lateinit var postsRecyclerView: RecyclerView
    private lateinit var postsList: List<PostItem>
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(ARG_ID)
        }
        postsList = sharedViewModel.getFilteredList(id!!)
    }

    companion object {
        fun newInstance(id: Int) = PostsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_ID, id)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_posts_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = DetailsRecyclerViewAdapter(postsList)
                postsRecyclerView = view
            }
        }
        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        sharedViewModel.postsListLiveData.observe(
//            viewLifecycleOwner,
//        Observer { postsList ->
//            postsRecyclerView.adapter = DetailsRecyclerViewAdapter(postsList) //passes list here instead
//        })
//    }

    //sets filtered list by ID
//    fun setPostList(id : Int) {
//        postsList = sharedViewModel.getFilteredList(id)
//        postsRecyclerView.adapter?.notifyDataSetChanged()
//    }

    /**
     * [RecyclerView.Adapter] that can display a details from [PostItem].
     */
    inner class DetailsRecyclerViewAdapter(
        private val values: List<PostItem>
    ) : RecyclerView.Adapter<DetailsRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                    FragmentPostsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.title
            holder.contentView.text = item.body
        }

        override fun getItemCount(): Int = values.size

        /**
         * [ViewHolder] inner class, handles logic
         */
        inner class ViewHolder(binding: FragmentPostsBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val idView: TextView = binding.postTitle
            val contentView: TextView = binding.postBody

            override fun toString(): String {
                return super.toString() + " '" + contentView.text + "'"
            }
        }

    }
}
package curtin.edu.assignment2parta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import curtin.edu.assignment2parta.data.UserItem
import curtin.edu.assignment2parta.databinding.FragmentItemBinding


/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: ItemViewModel by activityViewModels()
    private lateinit var userRecyclerView: RecyclerView
    private var columnCount = 1

    companion object {
        fun newInstance() = ItemFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                userRecyclerView = view
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.usersListLiveData.observe(
            viewLifecycleOwner,
            Observer { usersList ->
                userRecyclerView.adapter = UserRecyclerViewAdapter(usersList) //passes list here instead
            })
    }

    /**
     * [RecyclerView.Adapter] inner class, can display a list of [UserItem].
     */
    inner class UserRecyclerViewAdapter(
        private var values: List<UserItem>
    ) : RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

            return ViewHolder(
                FragmentItemBinding.inflate(LayoutInflater.from(parent.context),
                parent,false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.usernameView.text = item.username
        }

        override fun getItemCount(): Int = values.size

        /**
         * [ViewHolder] inner class, Handles logic.
         */
        inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
            val usernameView: TextView = binding.username
            private val load: Button = binding.loadBtn
            init {
                //Load button click
                load.setOnClickListener { //TODO: STUB
                    //Toast.makeText(context, "Load Clicked", Toast.LENGTH_SHORT).show()
                    val selected = sharedViewModel.usersListLiveData.value!![bindingAdapterPosition]
                    fillMoreFrag(bindingAdapterPosition)
                    //calls list filter method using item from current item
                    //(parentFragmentManager.findFragmentByTag("postFrag") as PostsFragment).setPostList(selected.id)
                    (activity as MainActivity).startPostsFrag(selected.id)

                }
                //ItemView click
                itemView.setOnClickListener{
                    fillMoreFrag(bindingAdapterPosition)
                }
            }
            //fills the "more" fragment with data, pos = bindingAdapterPosition
            private fun fillMoreFrag(pos : Int) {
                val selected = sharedViewModel.usersListLiveData.value!![pos]
                (parentFragmentManager.findFragmentByTag("moreFrag") as MoreFragment).setMoreView(selected)

            }
        }
    }
}
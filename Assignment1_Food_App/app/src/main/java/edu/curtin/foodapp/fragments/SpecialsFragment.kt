package edu.curtin.foodapp.fragments

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
import edu.curtin.foodapp.R
import edu.curtin.foodapp.data.*
import edu.curtin.foodapp.databinding.FragmentSpecialsItemBinding

/**
 * A fragment representing a list of Items.
 */
class SpecialsFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var columnCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_specials_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                val values = sharedViewModel.getSpecials()//Gets cart from shared View model
                adapter = SpecialsRecyclerViewAdapter(values)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            SpecialsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    /**
     * [RecyclerView.Adapter] that can display a [Food].
     */
    inner class SpecialsRecyclerViewAdapter(private val values: List<Food>) : RecyclerView.Adapter<SpecialsRecyclerViewAdapter.ViewHolder>() {



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = FragmentSpecialsItemBinding.inflate(LayoutInflater.from(parent.context),
                parent,false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentItem = values[position]

            val displayStr =
                "${currentItem.foodName} from ${currentItem.restaurantName} only: ${currentItem.price}"
            holder.itemName.text = displayStr
        }

        override fun getItemCount(): Int = values.size

        /**
         * View Holder Inner class
         * Handles Logic
         * */
        inner class ViewHolder(binding: FragmentSpecialsItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val itemName: TextView = binding.specialsItemName

            override fun toString(): String {
                return super.toString() + " '" + itemName.text + "'"
            }
        }

    }
}
/**
 * @author C Victor Marteli 19598552
 */
package edu.curtin.foodapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.curtin.foodapp.data.Shop
import edu.curtin.foodapp.MenuActivity
import edu.curtin.foodapp.R
import edu.curtin.foodapp.data.SharedViewModel

/**
 *  [ShopsMenuFragment] class.
 */
class ShopsMenuFragment() : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_shops_menu, container, false)
        sharedViewModel.load(v.context)
        // Inflate the layout for this fragment
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        val recyclerView = view.findViewById(R.id.recyclerView01) as RecyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = FoodAdapter()
    }

    /**
     * FoodAdapter Inner class
     * */
    inner class FoodAdapter() : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {
        val foodList = sharedViewModel.shopList() //grabs restaurant list from viewModel

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_shops_item,parent,false)
            return FoodViewHolder(itemView)
        }

        //called at each scroll of recyclerView
        override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
            val currentItem = foodList[position]
            holder.menuImage.setImageResource(currentItem.drawID)
        }

        override fun getItemCount(): Int {
            return foodList.size
        }

        /**
         * View Holder Inner class
         * Handles Logic
         * */
        inner class FoodViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            val menuImage = itemView.findViewById(R.id.list_item_img) as ImageView

            init {//button click for item in recyclerView
                menuImage.setOnClickListener{
                    val selected = foodList[bindingAdapterPosition]
                    Toast.makeText(context, "Selected: " + selected.name, Toast.LENGTH_SHORT).show()
                    switchToRestaurant(selected)
                }
            }
            private fun switchToRestaurant(restaurant : Shop){
                sharedViewModel.rName = restaurant.name //sets restaurant name
                (activity as MenuActivity).goToRestFragment() //switches fragments
            }
        }
    }
}

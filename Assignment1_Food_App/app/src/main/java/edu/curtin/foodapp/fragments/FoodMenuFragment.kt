/**
 * @author C Victor Marteli 19598552
 */
package edu.curtin.foodapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.curtin.foodapp.R
import edu.curtin.foodapp.data.Food
import edu.curtin.foodapp.data.SharedViewModel
import java.text.NumberFormat

/**
 * A simple [Fragment] subclass.
 */
class FoodMenuFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_menu, container, false) as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val recyclerView = view.findViewById(R.id.recyclerView_restaurant) as RecyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = RestaurantRecyclerViewAdapter()
    }

    /**
     * ItemAdapter Inner class
     * */
    inner class RestaurantRecyclerViewAdapter() : RecyclerView.Adapter<RestaurantRecyclerViewAdapter.ViewHolder>() {

        val foodList  = sharedViewModel.getFoodOptions() //TODO: needs to change based on restaurant

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_food_item,parent,false)
            return ViewHolder(itemView)
        }

        //called at each scroll of recyclerView
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentItem = foodList[position]
            holder.foodName.text = currentItem.foodName
            holder.foodImage.setImageResource(currentItem.drawID)
            val qty = sharedViewModel.getItemQuantity(currentItem.id)
            holder.setItemQuantityToView(qty)
            holder.setPriceText(currentItem.price)
        }

        override fun getItemCount(): Int = foodList.size

        /**
         * View Holder Inner class
         * Handles Logic
         * */
        inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            val foodImage = itemView.findViewById(R.id.list_food_img) as ImageView
            val foodName = itemView.findViewById(R.id.list_food_name_txt) as TextView
            private val foodAmount = itemView.findViewById(R.id.list_food_amount_txt) as TextView
            private val foodPrice = itemView.findViewById(R.id.list_food_price_txt) as TextView
            private val addButton = itemView.findViewById(R.id.list_food_add_btn) as FloatingActionButton
            private val removeButton = itemView.findViewById(R.id.list_food_remove_btn) as FloatingActionButton

            init {
                addButton.setOnClickListener{//add button click for item in recyclerView
                    val selected = foodList[bindingAdapterPosition]
                    addToCart(Food(selected.foodName, selected.restaurantName))
                    val qty = sharedViewModel.getItemQuantity(selected.id)
                    setItemQuantityToView(qty)
                }

                removeButton.setOnClickListener{// remove button click for item in recyclerView
                    val selected = foodList[bindingAdapterPosition]
                    removeFromCart(Food(selected.foodName, selected.restaurantName))
                    val qty = sharedViewModel.getItemQuantity(selected.id)
                    setItemQuantityToView(qty)
                }
            }
            //Formats double to correct currency and displays it
            fun setPriceText(price : Double){
                val priceF = NumberFormat.getCurrencyInstance().format(price)
                val priceStr = "Price: $priceF"
                foodPrice.text = priceStr
            }
            fun setItemQuantityToView(quantity : Int){
                val qtyStr = "Quantity: $quantity"
                foodAmount.text = qtyStr
            }

            /**
             * Adds items to cart
             */
            private fun addToCart(newItem: Food) {
                //looks for for an existing item with same name and restaurant
                //if not null add one to cart
                val found = sharedViewModel.getItemInCart(newItem.id)
                if(found != null){
                    found.quantity++
                    sharedViewModel.setItemQuantity(found.quantity, found) //updates view model TODO
                }
                else{
                    sharedViewModel.addToCart(newItem)
                }
            }
            /**
             * Remove items from cart
             */
            private fun removeFromCart(newItem: Food) {
                val itemName = newItem.foodName
                //looks for for an existing item with same name and restaurant
                val found = sharedViewModel.getItemInCart(newItem.id)
                if(found != null){
                    if(found.quantity > 1){
                        found.quantity--
                    }
                    else if(found.quantity == 1){
                        sharedViewModel.removeFromCart(newItem)
                    }
                }
                Toast.makeText(context, "Removed: $itemName to Cart", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
/**
 * @author C Victor Marteli 19598552
 */
package edu.curtin.foodapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.curtin.foodapp.MenuActivity
import edu.curtin.foodapp.R
import edu.curtin.foodapp.data.SharedViewModel
import java.text.NumberFormat

/**
 * A fragment representing a list of Items.
 */
class CartFragment : Fragment() {

    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false) as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val recyclerView = view.findViewById(R.id.recyclerView_cart) as RecyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CartRecyclerViewAdapter()
        (activity as MenuActivity).replaceCheckOutBar() //switches bottomBar
    }

    /**
     * ItemAdapter Inner class
     * */
    inner class CartRecyclerViewAdapter() : RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder>() {

        val cart  = sharedViewModel.getCart() //Gets cart from shared View model

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_cart_item,parent,false)
            return ViewHolder(itemView)
        }

        //called at each scroll of recyclerView
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentItem = cart[position]

            holder.quantity.setText(currentItem.quantity.toString())
            holder.itemName.text = currentItem.foodName
            holder.restName.text = currentItem.restaurantName
            holder.setPriceText(currentItem.getTotalPrice())
        }


        override fun getItemCount(): Int = cart.size

        /**
         * View Holder Inner class
         * Handles Logic
         * */
        inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
            val itemName = itemView.findViewById(R.id.cart_item_name) as TextView
            val quantity = itemView.findViewById(R.id.cart_item_quantity) as EditText
            val restName = itemView.findViewById(R.id.cart_rest_name) as TextView
            private val totalPrice = itemView.findViewById(R.id.cart_item_price) as TextView
            private val updateBtn = itemView.findViewById(R.id.cart_item_update_btn) as FloatingActionButton

            init {
                quantity.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {
                        //hides update button if value is null
                        if (s.isBlank()){
                            updateBtn.hide()
                        }
                        if (s.isNotBlank()){
                            updateBtn.show()
                        }

                        //update button
                        updateBtn.setOnClickListener{//if user clicks update button
                            val sInt = Integer.parseInt(s.toString()) //converts editable to Int
                            val item = cart[bindingAdapterPosition]
                                if(sInt == 0){ //case: value set was 0 -> remove item
                                    sharedViewModel.removeFromCart(item)
                                    notifyItemRemoved(bindingAdapterPosition)
                                }
                                else{//case: value not 0 -> set new item quantity and price
                                    item.quantity = sInt
                                    sharedViewModel.setItemQuantity(sInt, item) //updates DB
                                    setPriceText(item.getTotalPrice())
                                }
                            sharedViewModel.setTotalPrice() //sets totalPrice live data to be read by checkout fragment
                        }
                    }
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
                })
            }

            //Formats double to correct currency and displays it
            fun setPriceText(price : Double){
                val priceF = NumberFormat.getCurrencyInstance().format(price)
                val priceStr = "Price: $priceF"
                totalPrice.text = priceStr
            }
        }
    }
}
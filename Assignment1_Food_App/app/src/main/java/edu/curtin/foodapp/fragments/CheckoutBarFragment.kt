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
import androidx.lifecycle.Observer
import edu.curtin.foodapp.MenuActivity
import edu.curtin.foodapp.R
import edu.curtin.foodapp.data.SharedViewModel
import java.text.NumberFormat

/**
 * A simple [Fragment] subclass.
 * replaces bottom-bar fragment in checkout state only
 * Contains back to restaurant menu button, total price live data, proceed to checkout button
 */
class CheckoutBarFragment : Fragment() {
    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_checkout_bar, container, false) as View
        sharedViewModel.setTotalPrice() //calculates price in view model and sets it

        //Observer for total price live data in case items are removed
        sharedViewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            updatePrice(v.findViewById(R.id.checkout_total_txt))
        })
        // Inflate the layout for this fragment
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backButton = view.findViewById<ImageView>(R.id.checkout_return_icon)
        val checkOutButton = view.findViewById<ImageView>(R.id.checkout_proceed_icon)
        val totalPrice = view.findViewById<TextView>(R.id.checkout_total_txt)
        updatePrice(totalPrice) //sets initial price
        backButton.setOnClickListener{
            (activity as MenuActivity).goToRestFragment() //switches fragments back to menu
        }
        checkOutButton.setOnClickListener{
            var message = "Your order is on its way!"
            if (sharedViewModel.getCart().isEmpty()){
                message = "Cart is empty"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            //TODO: save order time and details into DB, Ask for login ifno
        }
    }
    private fun updatePrice(inView: TextView){
        //formats and sets price
        val priceF = NumberFormat.getCurrencyInstance().format(sharedViewModel.getTotalPrice())
        var priceStr = "Total: $priceF"
        if (sharedViewModel.getCart().isEmpty()){ //if cart is empty display message
            priceStr = "Cart is empty"
        }
        inView.text = priceStr
    }
}
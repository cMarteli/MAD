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
import edu.curtin.foodapp.MenuActivity
import edu.curtin.foodapp.R

/**
 * A navigation bottom bar for the application.
 */
class BottomBarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_bottom_bar, container, false) as View
        val backButton = v.findViewById<ImageView>(R.id.back_arrow_icon)
        val cartButton = v.findViewById<ImageView>(R.id.cart_icon)
        backButton.setOnClickListener{
            (activity as MenuActivity).goToMenuFragment() //switches fragments back to menu
        }
        cartButton.setOnClickListener{
            (activity as MenuActivity).goToCartFragment() //switches fragments to cart
        }
        // Inflate the layout for this fragment
        return v
    }
}
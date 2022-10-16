/**
 * @author C Victor Marteli 19598552
 */
package edu.curtin.foodapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import edu.curtin.foodapp.fragments.*

val foodFrag : ShopsMenuFragment = ShopsMenuFragment()
val restaurantFrag: FoodMenuFragment = FoodMenuFragment()
val cartFrag: CartFragment = CartFragment()
val bottomFrag : Fragment = BottomBarFragment()
val checkoutFrag : Fragment = CheckoutBarFragment()

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar?.hide()
        //goToMenuFragment()
    }
    //replace navigation bar with bottom bar fragment
    private fun replaceBottomBar() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_menu_navigation_container, bottomFrag) //adds bottom bar frag
            .commit()
    }
    //replace navigation bar with bottom bar fragment
    fun replaceCheckOutBar() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_menu_navigation_container, checkoutFrag) //adds bottom bar frag
            .commit()
    }
    //menu of specific items
    fun goToRestFragment() {
        replaceBottomBar()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_menu, restaurantFrag)
            .commit()
    }
    //menu with all restaurants
    fun goToMenuFragment() {
        //assigns menu fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_menu, foodFrag)
            .commit()
    }
    //menu with all restaurants
    fun goToCartFragment() {
        //assigns menu fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_menu, cartFrag)
            .commit()
    }

}
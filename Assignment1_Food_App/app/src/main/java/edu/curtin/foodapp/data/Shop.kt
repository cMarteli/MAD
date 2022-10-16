/**
 * @author C Victor Marteli 19598552
 */
package edu.curtin.foodapp.data

import edu.curtin.foodapp.R

/**
 * Holds imageID and name of item to be displayed in RecyclerViews
 * either Restaurant or Food Item data
 */
data class Shop(var name : String){
    var drawID = setDrawable(name)

    private fun setDrawable(inName : String) : Int{
        var drawId = 0
        when (inName) {
            "MCDonald's" -> drawId = R.drawable.mcdonalds_logo_500x281
            "KFC" -> drawId = R.drawable.kfc_logo_500x281
            "Nando's" -> drawId = R.drawable.nandos_logo_500x337
            "Domino's" -> drawId = R.drawable.logo_dominos_pizza
            "Pizza Hut" -> drawId = R.drawable.pizza_hut_logo_500x345
        }
        return drawId
    }
}

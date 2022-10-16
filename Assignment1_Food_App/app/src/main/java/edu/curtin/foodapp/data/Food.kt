package edu.curtin.foodapp.data

import edu.curtin.foodapp.R

/**
 * Class to store restaurant data
 */
data class Food(val foodName : String, var restaurantName : String){
    var id = foodName+restaurantName as String
    var quantity = 1
    var price = setPrice(foodName)
    var drawID = setDrawable(foodName)


    private fun setDrawable(inFoodName : String) : Int{
        var drawId = 0
        when (inFoodName) {
            "Cheeseburger" ->  drawId = R.drawable.cheeseburger
            "Burger and Chips" ->  drawId = R.drawable.burger_chips
            "Chips" -> drawId = R.drawable.chips
            "Chicken Wings" -> drawId = R.drawable.chicken_wings
            "Fried Drumsticks" -> drawId = R.drawable.fried_drumstick
            "Chicken Pizza" -> drawId = R.drawable.chicken_pizza
            "Meat-lovers Pizza" -> drawId = R.drawable.meat_lovers_pizza
            "Coke" -> drawId = R.drawable.coca_cola
            "Sprite" -> drawId = R.drawable.sprite
            "Orange Juice" -> drawId = R.drawable.fresh_orange_juice
        }
        return drawId
    }

    private fun setPrice(inFoodName : String) : Double{
        var price = 0.0
        when (inFoodName) {
            "Burger and Chips" ->  price = 15.0
            "Chips", "Cheeseburger" -> price = 4.2
            "Chicken Wings" , "Fried Drumsticks" -> price = 13.25
            "Chicken Pizza" , "Meat-lovers Pizza" -> price = 25.50
            "Coke", "Sprite", "Orange Juice" -> price = 4.5
        }
        return price
    }

    //returns total of a single price of item
    fun getTotalPrice() : Double{
        return quantity*price
    }

}

/**
 * @author C Victor Marteli 19598552
 */
package edu.curtin.foodapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.CursorWrapper
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.curtin.foodapp.R
import edu.curtin.foodapp.data.OrderSchema.OrderTable

/**
 * [SharedViewModel] holds information on shopping cart and name of last selected restaurant.
 */
class SharedViewModel : ViewModel() {
    private var db : SQLiteDatabase? = null // The database connection.
    // Name of Selected Restaurant
    var rName: String = ""
    private var cart = arrayListOf<Food>()

    // Read database contents into cart.
    fun load(context: Context) {
        db = OrderDBHelper(context, null).writableDatabase
        cart = getAllOrders() // loads full list into database
    }

    //order total - live data
    val totalPrice : MutableLiveData<Double> by lazy{
        MutableLiveData<Double>()
    }

    //adds item to cart
    fun addToCart(inItems : Food){
        cart.add(inItems)
        //database add
        val cv = ContentValues()
        cv.put(OrderTable.Cols.ID, inItems.id)
        cv.put(OrderTable.Cols.ITEM_NAME, inItems.foodName)
        cv.put(OrderTable.Cols.RESTAURANT_NAME, inItems.restaurantName)
        cv.put(OrderTable.Cols.PRICE, inItems.price)
        cv.put(OrderTable.Cols.DRAW_ID, inItems.drawID)
        cv.put(OrderTable.Cols.QUANTITY, inItems.quantity)
        db!!.insert(OrderTable.TABLE_NAME, null, cv)
    }

    //removes item to cart
    fun removeFromCart(inItem : Food){
        cart.remove(inItem)
        //database remove
        val whereValue = arrayOf<String>(java.lang.String.valueOf(inItem.id))
        db!!.delete(OrderTable.TABLE_NAME, OrderTable.Cols.ID + " = ?",
            whereValue)
    }

    //Updates quantity of item in cart
    fun setItemQuantity(qty : Int, inFood : Food){
        inFood.quantity = qty
        //database edit
        val cv = ContentValues()
        cv.put(OrderTable.Cols.QUANTITY, inFood.quantity)
        val whereValue = arrayOf<String>(java.lang.String.valueOf(inFood.id))
        db!!.update(
            OrderTable.TABLE_NAME, cv, OrderTable.Cols.ID + " = ?", whereValue)
    }

    //returns null if item not found
    fun getItemInCart(id : String): Food?{
        for (Food in cart){
            if (Food.id == id){
                return Food
            }
        }
        return null
    }
    //returns 0 if item not found
    fun getItemQuantity(id : String): Int{
        var qty = 0
        for (Food in cart){
            if (Food.id == id){
                qty = Food.quantity
            }
        }
        return qty
    }

    fun getCart(): ArrayList<Food> {
        return cart
    }

    //returns liveData
    fun getTotalPrice(): Double? {
        return totalPrice.value
    }

    //adds and return the added price of all items in cart
    fun setTotalPrice(){
        var totalSum = 0.0
        for(Food in cart){
            totalSum += Food.getTotalPrice()
        }
        totalPrice.value = totalSum
    }

    /**
     * Database Related Methods
     */
    class OrderCursor(cursor: Cursor?) : CursorWrapper(cursor) {
        fun getOrder(): Food {
            val id = getString(getColumnIndex(OrderTable.Cols.ID))
            val itemName = getString(getColumnIndex(OrderTable.Cols.ITEM_NAME))
            val restName = getString(getColumnIndex(OrderTable.Cols.RESTAURANT_NAME))
            val price = getDouble(getColumnIndex(OrderTable.Cols.PRICE))
            val drawId = getInt(
                getColumnIndex(OrderTable.Cols.DRAW_ID)
            )
            val qty = getInt(
                getColumnIndex(OrderTable.Cols.QUANTITY)
            )
            val outFood = Food(itemName, restName)
            outFood.id = id
            outFood.price = price
            outFood.quantity = qty
            outFood.drawID = drawId

            return outFood
        }
    }

    //Retrieves entire database table
    private fun getAllOrders(): ArrayList<Food> {
        val factionsList: java.util.ArrayList<Food> = java.util.ArrayList<Food>()
        val cursor = OrderCursor(
            db!!.query(
                OrderTable.TABLE_NAME,  // FROM our table
                null,  // SELECT all columns
                null,  // WHERE clause (null = all rows)
                null,  // WHERE arguments
                null,  // GROUP BY clause
                null,  // HAVING clause
                null
            ) // ORDER BY clause
        )
        cursor.use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                factionsList.add(cursor.getOrder())
                cursor.moveToNext()
            }
        } // "leak" certain resources.
        return factionsList
    }

    /**
     * Returns list of items to display
     */
    fun getFoodOptions(): List<Food> {
            var options = ArrayList<Food>()
            when (rName) {
                "MCDonald's" -> options = addFastFood()
                "Domino's", "Pizza Hut" -> options = addPizza()
                "KFC", "Nando's" -> options = addChickenJoint()
            }
            addDrinks(options)
            return options
        }

    private fun addFastFood(): ArrayList<Food> {
        val options = ArrayList<Food>()
        options.add(Food("Cheeseburger", rName))
        options.add(Food("Burger and Chips", rName))
        options.add(Food("Chips", rName))
        return options
    }

    private fun addPizza(): ArrayList<Food> {
        val options = ArrayList<Food>()
        options.add(Food("Chicken Pizza", rName))
        options.add(Food("Meat-lovers Pizza", rName))
        options.add(Food("Chicken Wings", rName))
        return options
    }

    private fun addChickenJoint(): ArrayList<Food> {
        val options = ArrayList<Food>()
        options.add(Food("Fried Drumsticks", rName))
        options.add(Food("Chicken Wings", rName))
        options.add(Food("Chips", rName))
        return options
    }

    private fun addDrinks(options : ArrayList<Food>) {
        options.add(Food("Coke", rName))
        options.add(Food("Sprite", rName))
        options.add(Food("Orange Juice", rName))
    }

    //TODO: temporary method this should be done at random
    fun getSpecials(): List<Food> {
        val options = ArrayList<Food>()
        options.add(Food("Cheeseburger", "McDonald's"))
        options.add(Food("Chicken Wings", "KFC"))
        options.add(Food("Meat-lovers Pizza", "Domino's"))
        return options
    }


    fun shopList() : ArrayList<Shop> {
        return arrayListOf(
            Shop("MCDonald's"),
            Shop("KFC"),
            Shop("Nando's"),
            Shop("Domino's"),
            Shop("Pizza Hut")
        )
    }

}
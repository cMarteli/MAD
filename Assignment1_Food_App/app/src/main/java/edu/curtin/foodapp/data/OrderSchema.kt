package edu.curtin.foodapp.data

class OrderSchema {
    object OrderTable {
        // Table
        const val TABLE_NAME = "orders" // name
        object Cols {
            // Column names
            const val ID = "item_id"
            const val ITEM_NAME = "food_name"
            const val RESTAURANT_NAME = "restaurant_name"
            const val PRICE = "price"
            const val DRAW_ID = "draw_id"
            const val QUANTITY = "quantity"
        }
    }
}
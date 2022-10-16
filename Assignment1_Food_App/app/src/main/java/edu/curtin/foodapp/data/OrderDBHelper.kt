package edu.curtin.foodapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import edu.curtin.foodapp.data.OrderSchema.OrderTable

private const val VERSION = 1
private const val DATABASE_NAME = "orders.db"
class OrderDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, VERSION){


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + OrderTable.TABLE_NAME + "(" +
                    OrderTable.Cols.ID + " TEXT, " +
                    OrderTable.Cols.ITEM_NAME + " TEXT, " +
                    OrderTable.Cols.RESTAURANT_NAME + " TEXT, " +
                    OrderTable.Cols.PRICE + " DOUBLE, " +
                    OrderTable.Cols.DRAW_ID + " INTEGER, " +
                    OrderTable.Cols.QUANTITY + " INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, v1: Int, v2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + OrderTable.TABLE_NAME)
        onCreate(db)
    }
}
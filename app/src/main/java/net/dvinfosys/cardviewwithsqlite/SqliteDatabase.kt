package net.dvinfosys.cardviewwithsqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import java.util.*

class SqliteDatabase(context: Context?) : ManagedSQLiteOpenHelper(context!!, "mydb") {


    companion object {
        private var instance: SqliteDatabase? = null
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "product"
        private val TABLE_PRODUCTS = "products"
        private val COLUMN_ID = "_id"
        private val COLUMN_PRODUCTNAME = "productname"
        private val COLUMN_QUANTITY = "quantity"

        @Synchronized
        fun getInstance(context: Context?): SqliteDatabase {
            if (instance == null) {
                instance = SqliteDatabase(context?.applicationContext)
            }
            return instance!!
        }

    }

    val Context.database: SqliteDatabase get() = SqliteDatabase.getInstance(applicationContext)

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_PRODUCTS_TABLE = "CREATE TABLE $TABLE_PRODUCTS($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_PRODUCTNAME TEXT,$COLUMN_QUANTITY INTEGER)"
        db?.execSQL(CREATE_PRODUCTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS)
        onCreate(db)
    }

    fun listProducts(): List<Product> {
        val sql = "select * from " + TABLE_PRODUCTS
        val db = this.readableDatabase
        val storeProducts = ArrayList<Product>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {
                val id = Integer.parseInt(cursor.getString(0))
                val name = cursor.getString(1)
                val quantity = Integer.parseInt(cursor.getString(2))
                storeProducts.add(Product(id, name, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return storeProducts
    }

    fun addProduct(product: Product) {
        val values = ContentValues()
        values.put(COLUMN_PRODUCTNAME, product.name)
        values.put(COLUMN_QUANTITY, product.quantity)
        val db = this.writableDatabase
        db.insert(TABLE_PRODUCTS, null, values)
    }

    /*fun updateProduct(product: Product) {
        val values = ContentValues()
        values.put(COLUMN_PRODUCTNAME, product.name)
        values.put(COLUMN_QUANTITY, product.quantity)
        val db = this.writableDatabase
        db.update(TABLE_PRODUCTS, values, COLUMN_ID + "    = ?", arrayOf { product.id })
    }*/

    fun findProduct(name: String): Product? {
        val query = "Select * FROM $TABLE_PRODUCTS WHERE $COLUMN_PRODUCTNAME = name"
        val db = this.writableDatabase
        var mProduct: Product? = null
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            val productName = cursor.getString(1)
            val productQuantity = Integer.parseInt(cursor.getString(2))
            mProduct = Product(id, productName, productQuantity)
        }
        cursor.close()
        return mProduct
    }

    fun deleteProduct(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_PRODUCTS, COLUMN_ID + "    = ?", arrayOf(id.toString()))
    }


}
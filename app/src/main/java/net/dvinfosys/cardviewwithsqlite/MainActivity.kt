package net.dvinfosys.cardviewwithsqlite

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private var mDatabase: SqliteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fLayout = findViewById<RelativeLayout>(R.id.activity_to_do) as RelativeLayout
        val productView = findViewById<RecyclerView>(R.id.product_list) as RecyclerView
        val linearLayoutManager = LinearLayoutManager(this)
        productView.layoutManager = linearLayoutManager
        productView.setHasFixedSize(true)

        mDatabase = SqliteDatabase(this)
        val allProducts = mDatabase!!.listProducts()
        if (allProducts.size > 0) {
            productView.visibility = View.VISIBLE
            val mAdapter = ProductAdapter(this, allProducts)
            productView.adapter = mAdapter
        } else {
            productView.visibility = View.GONE
            Toast.makeText(this, "There is no product in the database. Start adding now", Toast.LENGTH_LONG).show()
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { addTaskDialog() }
    }

    private fun addTaskDialog() {
        val inflater = LayoutInflater.from(this)
        val subView = inflater.inflate(R.layout.add_product_layout, null)
        val idField = subView.findViewById<EditText>(R.id.enter_id)
        val nameField = subView.findViewById<EditText>(R.id.enter_name) as EditText
        val quantityField = subView.findViewById<EditText>(R.id.enter_quantity) as EditText
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add new product")
        builder.setView(subView)
        builder.create()
        builder.setPositiveButton("ADD PRODUCT") { dialog, which ->
            val id = Integer.parseInt(idField.text.toString())
            val name = nameField.text.toString()
            val quantity = Integer.parseInt(quantityField.text.toString())
            if (TextUtils.isEmpty(name) || quantity <= 0) {
                Toast.makeText(this@MainActivity, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show()
            } else {
                val newProduct = Product(id, name, quantity)
                mDatabase?.addProduct(newProduct)
                //refresh the activity
                finish();
                startActivity(getIntent());
            }
        }
        builder.setNegativeButton("CANCEL") { dialog, which -> Toast.makeText(this@MainActivity, "Task cancelled", Toast.LENGTH_LONG).show() }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDatabase != null) {
            mDatabase?.close()
        }
    }
}

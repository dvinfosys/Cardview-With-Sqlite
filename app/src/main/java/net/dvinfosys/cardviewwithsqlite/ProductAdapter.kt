package net.dvinfosys.cardviewwithsqlite

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast


class ProductAdapter(private val context: Context, private val listProducts: List<Product>) : RecyclerView.Adapter<ProductViewHolder>() {
    private val mDatabase: SqliteDatabase

    init {
        mDatabase = SqliteDatabase(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_list_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val singleProduct = listProducts[position]
        holder.name.setText(singleProduct.name)
        holder.quantity!!.text= singleProduct.quantity.toString()
        holder.editProduct.setOnClickListener(View.OnClickListener { editTaskDialog(singleProduct) })
        holder.deleteProduct.setOnClickListener(View.OnClickListener {
            //delete row from database
            mDatabase.deleteProduct(singleProduct.id)
            //refresh the activity page.
            (context as Activity).finish()
            context.startActivity(context.intent)
        })
    }

    override fun getItemCount(): Int {
        return listProducts.size
    }

    private fun editTaskDialog(product: Product?) {
        val inflater = LayoutInflater.from(context)
        val subView = inflater.inflate(R.layout.add_product_layout, null)
        val nameField = subView.findViewById<EditText>(R.id.enter_name) as EditText
        val quantityField = subView.findViewById<EditText>(R.id.enter_quantity) as EditText
        if (product != null) {
            nameField.setText(product.name)
            quantityField.setText(product.quantity)
        }
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Edit product")
        builder.setView(subView)
        builder.create()
        builder.setPositiveButton("EDIT PRODUCT") { dialog, which ->
            val name = nameField.text.toString()
            val quantity = Integer.parseInt(quantityField.text.toString())
            if (TextUtils.isEmpty(name) || quantity <= 0) {
                Toast.makeText(context, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show()
            } else {
                // mDatabase.updateProduct(Product(product!!.id, name, quantity))
                //refresh the activity
                (context as Activity).finish()
                context.startActivity(context.intent)
            }
        }
        builder.setNegativeButton("CANCEL") { dialog, which -> Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show() }
        builder.show()
    }


}
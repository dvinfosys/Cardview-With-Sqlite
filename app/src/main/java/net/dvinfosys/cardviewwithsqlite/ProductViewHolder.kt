package net.dvinfosys.cardviewwithsqlite

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var name: TextView
    var quantity: TextView? =null
    var deleteProduct: ImageView
    var editProduct: ImageView

    init {
        name = itemView.findViewById<TextView>(R.id.product_name) as TextView
        quantity=itemView.findViewById<TextView>(R.id.product_quantity)as TextView
        deleteProduct = itemView.findViewById<ImageView>(R.id.delete_product) as ImageView
        editProduct = itemView.findViewById<ImageView>(R.id.edit_product) as ImageView
    }
}
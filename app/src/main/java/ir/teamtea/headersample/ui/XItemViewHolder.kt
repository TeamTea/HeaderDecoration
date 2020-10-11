package ir.teamtea.headersample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.teamtea.headersample.R
import ir.teamtea.headersample.model.Item

class XItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val title : TextView = view.findViewById(R.id.item_value)

    private var xItem: Item? = null

    fun bind(item : Item?){
        if (item == null){
            val resources = itemView.resources
            title.text = resources.getString(R.string.loading)
        }else{
            showXItemData(item)
        }
    }

    private fun showXItemData(xItem: Item){
        this.xItem = xItem
        title.text = xItem.title
    }

    companion object {
        fun create(parent: ViewGroup): XItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.items, parent, false)
            return XItemViewHolder(view)
        }
    }
}
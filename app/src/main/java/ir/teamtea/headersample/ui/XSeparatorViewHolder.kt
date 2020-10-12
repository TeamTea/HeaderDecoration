package ir.teamtea.headersample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ir.teamtea.headerlibrary.recycler.HeaderViewHolder
import ir.teamtea.headersample.R

class XSeparatorViewHolder(itemView: View) : HeaderViewHolder(itemView) {
    private val text: TextView = itemView.findViewById(R.id.separator_item)


    fun bind(separatorText: String) {
        text.text = separatorText
    }

    companion object {
        fun create(parent: ViewGroup): XSeparatorViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.separator_items, parent, false)
            return XSeparatorViewHolder(view)
        }
    }
}
package ir.teamtea.headersample.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.teamtea.headersample.R
import ir.teamtea.headersample.model.UiItems

class ItemsAdapter : ListAdapter<UiItems,RecyclerView.ViewHolder>(UiItems_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.items) {
            XItemViewHolder.create(parent)
        } else {
            XSeparatorViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiItems.XSeparator -> R.layout.separator_items
            is UiItems.XItems -> R.layout.items
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiItem : UiItems = getItem(position)
        uiItem.let {
            when(it){
                is UiItems.XItems -> (holder as XItemViewHolder).bind(it.item)
                is UiItems.XSeparator -> (holder as XSeparatorViewHolder).bind(it.text)
            }
        }
    }


    companion object {
        private val UiItems_COMPARATOR = object : DiffUtil.ItemCallback<UiItems>() {
            override fun areItemsTheSame(oldItem: UiItems, newItem: UiItems): Boolean {
                return (oldItem is UiItems.XItems && newItem is UiItems.XItems &&
                        oldItem.item.title == newItem.item.title) ||
                        (oldItem is UiItems.XSeparator && newItem is UiItems.XSeparator &&
                                oldItem.text == newItem.text)
            }

            override fun areContentsTheSame(oldItem: UiItems, newItem: UiItems): Boolean =
                    oldItem == newItem
        }
    }

}
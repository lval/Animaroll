package com.lvalentin.animaroll

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lvalentin.animaroll.common.Enums
import com.lvalentin.animaroll.models.MediaVm


class MediaAdapter(private val dataList: List<MediaVm>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onItemCheck: ((MediaVm) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            Enums.RecyclerViewType.HEADER.id
        } else {
            Enums.RecyclerViewType.ITEM.id
        }
    }

    class HeaderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) { }
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val chkAlbum: CheckBox = itemView.findViewById(R.id.chk_media)
        val tvAlbumName: TextView = itemView.findViewById(R.id.tv_media_name)
        val tvAlbumItemCnt: TextView = itemView.findViewById(R.id.tv_media_item_cnt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Enums.RecyclerViewType.HEADER.id -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.media_header, parent, false)
                HeaderViewHolder(view)
            }
            Enums.RecyclerViewType.ITEM.id -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.media_item, parent, false)
                ItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            Enums.RecyclerViewType.HEADER.id -> {
                val hvHolder = holder as HeaderViewHolder
            }
            Enums.RecyclerViewType.ITEM.id -> {
                val iHolder = holder as ItemViewHolder
                val item = dataList[position - 1]
                iHolder.chkAlbum.setOnCheckedChangeListener(null)
                iHolder.tvAlbumName.text = item.name
                iHolder.tvAlbumItemCnt.text =
                    iHolder.tvAlbumItemCnt.context.resources.getQuantityString(
                        R.plurals.folder_item_cnt,
                        item.itemsCnt,
                        item.itemsCnt
                    )
                iHolder.chkAlbum.isChecked = item.isSelected
                iHolder.itemView.setOnClickListener { iHolder.chkAlbum.performClick() }
                iHolder.chkAlbum.setOnCheckedChangeListener { _, isChecked ->
                    item.isSelected = isChecked
                    onItemCheck?.invoke(item)
                }
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return dataList.size + 1
    }
}

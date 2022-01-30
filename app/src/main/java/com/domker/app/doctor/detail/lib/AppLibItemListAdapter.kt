package com.domker.app.doctor.detail.lib

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.widget.BaseItemListAdapter
import com.domker.base.file.FileUtils
import com.domker.base.file.ZipFileItem

/**
 * App里面Lib类库里面使用的CPU类型，以及每个类型下面lab的列表
 */
class AppLibItemListAdapter(
    context: Context,
    private val zipFileList: List<ZipFileItem>
) : BaseItemListAdapter<AppLibItemListAdapter.LibItemViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibItemViewHolder {
        val view = inflater.inflate(R.layout.item_lib_list_layout, parent, false)
        return LibItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibItemViewHolder, position: Int) {
        val c = zipFileList[position]
        holder.textViewSubject.text = c.fileName
        holder.textViewFile.text = FileUtils.formatFileSize(c.uncompressedSize)
        holder.textViewZip.text = FileUtils.formatFileSize(c.compressedSize)

        holder.icon.visibility = View.VISIBLE
        holder.view.setOnClickListener {
            invokeItemClick(holder, position)
        }
    }

    override fun getItemCount(): Int = zipFileList.size

    class LibItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textViewSubject: TextView = view.findViewById(R.id.textViewSubject)
        val textViewFile: TextView = view.findViewById(R.id.textViewContent)
        val textViewZip: TextView = view.findViewById(R.id.textViewZip)
        var icon: ImageView = view.findViewById(R.id.imageViewType)
    }
}
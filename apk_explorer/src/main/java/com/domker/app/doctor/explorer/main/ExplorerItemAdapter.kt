package com.domker.app.doctor.explorer.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.domker.app.doctor.explorer.ApkExplorer
import com.domker.app.doctor.explorer.R
import com.domker.app.doctor.explorer.menu.ExplorerMenu
import com.domker.app.doctor.explorer.utils.IconUtil
import com.domker.app.doctor.explorer.utils.RouterUtil
import com.domker.base.FileIntent
import com.domker.base.file.ZipFileItem
import com.domker.base.unzip
import java.io.File

/**
 * 文件浏览的详情
 * Created by wanlipeng on 2020/9/29 5:39 PM
 */
class ExplorerItemAdapter(
    private val context: Context,
    private val apkSourcePath: String
) : RecyclerView.Adapter<ExplorerItemAdapter.ItemViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val items: MutableList<ExplorerItem> = mutableListOf()
    private val apkExplorer = ApkExplorer(context)

    private var currentPath: String = File.separator
    private var folderChangeListener: ((List<String>) -> Unit)? = null

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewType: ImageView? = itemView.findViewById(R.id.imageViewType)
        val textViewName: TextView? = itemView.findViewById(R.id.textViewName)
        val imageViewIcon: ImageView? = itemView.findViewById(R.id.imageViewIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = inflater.inflate(R.layout.layout_apk_explorer_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = 0

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        // 子目录首行展示上一层目录
        if (currentPath != File.separator && position == 0) {
            holder.textViewName?.text = ".."
        } else {
            holder.textViewName?.text = item.file.name
        }
        holder.imageViewType?.apply {
            justFileType(this, item)
        }

        holder.itemView.setOnClickListener {
            if (item.isFile) {
                if (!onClickShow(item.file)) {
                    Toast.makeText(context, "不支持该文件类型: ${item.file.name}", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                updateExplorerData(item.file.absolutePath)
                notifyDataSetChanged()
            }
        }

        holder.itemView.setOnLongClickListener {
            if (!item.isFile) {
                return@setOnLongClickListener false
            }
            val menu = ExplorerMenu(context, it)
            menu.setOnMenuItemClickListener { m ->
                onItemLongPress(item, m.itemId)
            }
            menu.show()
            true
        }
    }

    /**
     * 长按一项的时候执行的操作
     */
    private fun onItemLongPress(item: ExplorerItem, itemId: Int): Boolean {
        val tempFile = File(apkSourcePath).unzip(context, item.file)
        tempFile?.apply {
            val intent = when (itemId) {
                R.id.open_with_text -> FileIntent.createHtmlFileIntent(context, this)
                R.id.open_with_image -> FileIntent.createImageFileIntent(context, this)
                else -> null
            }
            if (intent != null) {
                context.startActivity(intent)
                return true
            }
        }
        return false
    }

    /**
     * 点击并且展示
     *
     * @return 是否可以是别，不能打开的返回false
     */
    private fun onClickShow(file: File): Boolean {
        // 解压到临时文件夹
        val tempFile = File(apkSourcePath).unzip(context, file)
        tempFile?.apply {
            println(tempFile)
            return when {
                ApkExplorer.isResourceFile(tempFile) -> {
                    // 资源文件，名称固定
                    RouterUtil.openResourceExplorerActivity(tempFile.absolutePath)
                    true
                }
                ApkExplorer.isJsonFile(tempFile) -> {
                    RouterUtil.openJsonViewerActivity(tempFile)
                    true
                }
                else -> {
                    val intent = FileIntent.createFileIntent(context, tempFile)?.apply {
                        context.startActivity(this)
                    }
                    intent != null
                }
            }
        }
        return false
    }

    /**
     * 按返回按键的时候，需要返回上一层
     */
    fun onBackPressed(): Boolean {
        // 判断不在顶层的时候，返回按键需要返回上一层
        return if (items.size > 0 && currentPath != File.separator) {
            updateExplorerData(items[0].file.absolutePath)
            notifyDataSetChanged()
            true
        } else {
            false
        }
    }

    /**
     * 首次启动的试试，加载文件信息
     */
    fun loadItems(list: List<ZipFileItem>) {
        updateExplorerData(File.separator, list)
    }

    /**
     * 更新数据
     */
    private fun updateExplorerData(folderPath: String, list: List<ZipFileItem>) {
        currentPath = folderPath
        apkExplorer.updateData(list)
        items.clear()
        items.addAll(apkExplorer.filterItems(folderPath))
        items.sortBy { it.isFile }
        // 非顶层目录，增加返回上一层目录的入口
        if (folderPath != File.separator) {
            items.add(0, ExplorerItem(File(folderPath).parentFile!!, false))
        }
        folderChangeListener?.let {
            it(
                folderPath.split(File.separator).filter { it.isNotBlank() })
        }
    }

    /**
     * 路径发生变化的时候的回调
     */
    fun setOnFolderChanged(listener: (paths: List<String>) -> Unit) {
        folderChangeListener = listener
    }

    /**
     * 使用路径更新数据
     */
    private fun updateExplorerData(folderPath: String) {
        updateExplorerData(folderPath, apkExplorer.getData())
    }


    /**
     * 判断文件类型并展示相关的图标
     */
    private fun justFileType(imageViewType: ImageView, item: ExplorerItem) {
        val resourceId = if (!item.isFile) {
            R.drawable.format_folder_smartlock
        } else {
            IconUtil.justFileIcon(item.file)
        }
        Glide.with(context).load(resourceId).into(imageViewType)
    }
}
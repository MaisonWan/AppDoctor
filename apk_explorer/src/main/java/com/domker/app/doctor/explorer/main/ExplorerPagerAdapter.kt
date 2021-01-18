package com.domker.app.doctor.explorer.main

import android.content.Context
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domker.base.addItemDecoration
import com.domker.base.readZipFileList
import com.domker.base.thread.AppExecutors
import com.domker.app.doctor.explorer.R
import java.io.File

/**
 * 安装包浏览器，分页适配器
 */
class ExplorerPagerAdapter(private val context: Context,
                           private val owner: FragmentActivity,
                           private val apkSourcePath: String,
                           private val appPackageName: String,
                           private val pageTitleRes: IntArray) :
        RecyclerView.Adapter<ExplorerPagerAdapter.ExplorerViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val viewModel = ViewModelProvider(owner).get(ApkExplorerViewModel::class.java)
    private var explorerAdapter: ExplorerItemAdapter? = null
    private var currentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExplorerViewHolder {
        val view = inflater.inflate(R.layout.layout_explorer_pager, parent, false)
        return ExplorerViewHolder(view)
    }

    override fun getItemCount(): Int = pageTitleRes.size

    override fun onBindViewHolder(holder: ExplorerViewHolder, position: Int) {
        when (pageTitleRes[position]) {
            R.string.tab_text_apk -> {
                bindApk(holder)
            }
            R.string.tab_text_sec -> {
                bindFavorite(holder)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: ExplorerViewHolder) {
        super.onViewAttachedToWindow(holder)
        currentPosition = holder.layoutPosition
    }

    fun onBackPressed(): Boolean {
        if (pageTitleRes[currentPosition] == R.string.tab_text_apk) {
            return explorerAdapter?.onBackPressed() ?: false
        }
        return false
    }

    /**
     * 绑定apk的内容到列表中
     */
    private fun bindApk(holder: ExplorerViewHolder) {
        val adapter = ExplorerItemAdapter(context, apkSourcePath)
        explorerAdapter = adapter
        // 异步获取apk内部目录之后的结果
        viewModel.appInfo.observe(owner, {
            adapter.loadItems(it)
            adapter.notifyDataSetChanged()
        })
        // 异步获取apk内部结构完全的数据
        AppExecutors.executor.execute {
            val apkFile = File(apkSourcePath)
            viewModel.appInfo.postValue(apkFile.readZipFileList())
        }
        // 点击进入目录之后的回调
        adapter.setOnFolderChanged {
            holder.titleContent?.removeAllViews()
            addViewToTitle(appPackageName, holder.titleContent)
            it.forEach { path ->
                addViewToTitle(path, holder.titleContent)
            }
            val offset: Int = holder.titleContent!!.measuredHeight - holder.titleScrollView!!.measuredHeight
            holder.titleScrollView.scrollTo(offset, 0)
        }
        // 初始化RecyclerView
        holder.recyclerView?.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
            this.addItemDecoration(context, R.drawable.inset_recyclerview_divider)
            this.setItemViewCacheSize(100)
        }
    }

    private fun addViewToTitle(content: String, viewGroup: ViewGroup?) {
        val t = TextView(context)
        t.linksClickable = true
        t.text = content
        t.autoLinkMask = Linkify.ALL
        t.setOnClickListener {

        }
        viewGroup?.addView(t)

        val s = TextView(context)
        s.text = File.separator
        viewGroup?.addView(s)
    }

    private fun bindFavorite(holder: ExplorerViewHolder) {
        val text = TextView(context)
        text.text = "Favorite"
        holder.titleContent?.addView(text)
        val a = ExplorerItemAdapter(context, apkSourcePath)
        holder.recyclerView?.apply {
            this.adapter = a
            this.layoutManager = LinearLayoutManager(context)
            this.addItemDecoration(context, R.drawable.inset_recyclerview_divider)
            this.setItemViewCacheSize(100)
            a.notifyDataSetChanged()
        }
    }

    class ExplorerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView? = itemView.findViewById(R.id.recyclerViewFileList)
        val titleScrollView: HorizontalScrollView? = itemView.findViewById(R.id.pathTitle)
        val titleContent: LinearLayout? = itemView.findViewById(R.id.pathContent)
    }
}
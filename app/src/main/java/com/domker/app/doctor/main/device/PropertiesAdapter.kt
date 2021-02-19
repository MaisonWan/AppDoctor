package com.domker.app.doctor.main.device

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R

/**
 * 属性适配器
 * Created by wanlipeng on 2/15/21 1:59 AM
 */
class PropertiesAdapter(private val context: Context,
                        private val data: List<String>) :
        RecyclerView.Adapter<PropertiesAdapter.PropertiesViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private var showData = data
//    private val pattern: Pattern = Pattern.compile("\\[.+\\]")

    class PropertiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewKey: TextView = itemView.findViewById(R.id.textViewKey)
        val textViewValue: TextView = itemView.findViewById(R.id.textViewValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertiesViewHolder {
        val root = inflater.inflate(R.layout.item_key_value, parent, false)
        return PropertiesViewHolder(root)
    }

    override fun onBindViewHolder(holder: PropertiesViewHolder, position: Int) {
        val line = showData[position]
        val kv = unpackKeyValue(line)

        holder.textViewKey.text = kv[0]
        holder.textViewValue.text = kv[1]
    }

    override fun getItemCount(): Int = showData.size

    /**
     * 根据关键词搜索
     */
    fun search(keyWords: String?) {
        showData = if (keyWords.isNullOrBlank()) {
            data
        } else {
            data.filter { it.contains(keyWords) }
        }
    }

    companion object {

        /**
         * 解码每行的数据
         */
        @JvmStatic
        fun unpackKeyValue(line: String): Array<String> {
            var left = line.indexOfFirst { it == '[' } + 1
            var right = line.indexOfFirst { it == ']' }
            val key = line.substring(left, right)

            var value = line.substring(right + 1)
            left = value.indexOfFirst { it == '[' } + 1
            right = value.indexOfFirst { it == ']' }
            value = value.substring(left, right)
            return arrayOf(key, value)
        }
    }
}
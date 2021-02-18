package com.domker.app.doctor.main.device

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView


/**
 * 自定义过滤器
 * Created by wanlipeng on 2/18/21 3:29 PM
 */
class PropertyAutoCompleteAdapter<T>() : BaseAdapter(), Filterable {

    private var mOriginalValues: MutableList<T>? = null
    private var mObjects: MutableList<T>? = null
    private val mLock = Any()
    private var mResource = 0
    private var mDropDownResource = 0
    private var mContext: Context? = null
    private var mFilter: AutoCompleteFilter? = null
    private var mInflater: LayoutInflater? = null

    constructor(context: Context, textViewResourceId: Int, objects: List<T>) : this() {
        init(context, textViewResourceId, objects)
    }

    private fun init(context: Context, resource: Int, objects: List<T>) {
        mContext = context
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mDropDownResource = resource
        mResource = mDropDownResource
        mObjects = ArrayList(objects)
        mFilter = AutoCompleteFilter()
    }

    fun add(obj: T) {
        if (mOriginalValues != null) {
            synchronized(mLock) { mOriginalValues?.add(obj) }
        } else {
            mObjects!!.add(obj)
        }
    }

    fun insert(obj: T, index: Int) {
        if (mOriginalValues != null) {
            synchronized(mLock) { mOriginalValues?.add(index, obj) }
        } else {
            mObjects!!.add(index, obj)
        }
    }

    fun remove(obj: T) {
        if (mOriginalValues != null) {
            synchronized(mLock) { mOriginalValues?.remove(obj) }
        } else {
            mObjects!!.remove(obj)
        }
    }

    fun clear() {
        if (mOriginalValues != null) {
            synchronized(mLock) { mOriginalValues?.clear() }
        } else {
            mObjects!!.clear()
        }
    }

    fun getContext(): Context? {
        return mContext
    }

    override fun getCount(): Int {
        return mObjects!!.size
    }

    override fun getItem(position: Int): T {
        return mObjects!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, mResource)
    }

    private fun createViewFromResource(position: Int, convertView: View?,
                                       parent: ViewGroup, resource: Int): View {
        val view = convertView ?: mInflater!!.inflate(resource, parent, false)
        val text = try {
            view as TextView
        } catch (e: ClassCastException) {
            throw IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e)
        }
        val item: T = getItem(position)
        if (item is CharSequence) {
            text.text = item
        } else {
            text.text = item.toString()
        }
        return view
    }

    override fun getFilter(): Filter? {
        return mFilter
    }

    fun setDropDownViewResource(resource: Int) {
        mDropDownResource = resource
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent, mDropDownResource)
    }

    private inner class AutoCompleteFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            if (mOriginalValues == null) {
                synchronized(mLock) {
                    mOriginalValues = ArrayList(mObjects)
                }
            }
            val count: Int = mOriginalValues?.size ?: 0
            val values: ArrayList<T> = ArrayList()
            for (i in 0 until count) {
                val value: T = mOriginalValues!![i]
                val valueText: String = value.toString()
                if (valueText.contains(constraint)) {
                    values.add(value)
                }
            }
            results.values = values
            results.count = values.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null && results.count > 0) {
                mObjects = results.values as MutableList<T>
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}
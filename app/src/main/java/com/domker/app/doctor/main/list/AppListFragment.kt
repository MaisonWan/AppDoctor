package com.domker.app.doctor.main.list

import android.os.Bundle
import android.view.*
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.R
import com.domker.app.doctor.data.SORT_NAME
import com.domker.app.doctor.data.SORT_SIZE
import com.domker.app.doctor.data.SORT_TIME
import com.domker.app.doctor.databinding.FragmentAppListBinding
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.widget.BaseAppFragment

/**
 * 展示App列表的页面，多种不同风格的展示
 */
class AppListFragment : BaseAppFragment() {
    private lateinit var binding: FragmentAppListBinding
    private lateinit var adapter: AppListPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentAppListBinding.inflate(inflater, container, false)
        initViews(binding)
        return binding.root
    }

    private fun initViews(binding: FragmentAppListBinding) {
        adapter = AppListPageAdapter(requireContext(), this, this)
        adapter.includeSystemApp = appIncludeAll
        binding.viewpager.also { v ->
            v.offscreenPageLimit = 1
            v.adapter = adapter
            v.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.circlePageIndicator.setViewPager(v)
        }
    }

    override fun onAppIncludeChanged(includeAll: Boolean) {
        super.onAppIncludeChanged(includeAll)
        adapter.fetchAppList(binding.viewpager.currentItem, includeAll)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // 清除Activity的菜单
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val p = adapter.getDataProcessor()
        return when (item.itemId) {
            R.id.menu_sort_time -> {
                p.sortBy(SORT_TIME)
                true
            }
            R.id.menu_sort_name -> {
                p.sortBy(SORT_NAME)
                true
            }
            R.id.menu_sort_size -> {
                p.sortBy(SORT_SIZE)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
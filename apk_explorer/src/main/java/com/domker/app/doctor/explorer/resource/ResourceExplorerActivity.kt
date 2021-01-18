package com.domker.app.doctor.explorer.resource

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.domker.base.thread.AppExecutors
import com.domker.app.doctor.explorer.R
import com.domker.app.doctor.explorer.databinding.ActivityResourceExplorerBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.thereisnospon.util.parse.ArsrcParserJsonUtils
import com.thereisnospon.util.parse.ResourceArscFileParser
import com.thereisnospon.util.parse.type.ResFile


@Route(path = "/resource_explorer/activity")
class ResourceExplorerActivity : AppCompatActivity() {
    private val tabTitleRes = intArrayOf(R.string.tab_text_1, R.string.tab_text_2)
    private lateinit var binding: ActivityResourceExplorerBinding
    private lateinit var pageAdapter: ResourcePagerAdapter
    private lateinit var resourceViewModel: ResourceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResourceExplorerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resourceViewModel = ViewModelProvider(this).get(ResourceViewModel::class.java)
        resourceViewModel.resFile.observe(this, {
            bindTitles(it)
            val json = ArsrcParserJsonUtils.toJsObj(it)
            println(json)
        })

        // 获取文件路径
        val path = intent.getStringExtra("resource_file_path")

        AppExecutors.executor.execute {
            val resFile = ResourceArscFileParser.parseFile(path)
            resourceViewModel.resFile.postValue(resFile)
            println(resFile.header.packageCount)
        }
    }

    private fun bindTitles(resFile: ResFile) {
        val titles = arrayOf("11", "22", "33")
        pageAdapter = ResourcePagerAdapter(this, titles)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewPager.adapter = pageAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}
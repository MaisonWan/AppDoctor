package com.domker.app.doctor.explorer

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.domker.app.doctor.explorer.databinding.ActivityApkExplorerBinding
import com.domker.app.doctor.explorer.main.ExplorerPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

@Route(path = "/apk_explorer/activity")
class ApkExplorerActivity : AppCompatActivity() {
    private val tabTitleRes = intArrayOf(R.string.tab_text_apk, R.string.tab_text_sec)
    private var apkSourcePath: String = ""
    private var appPackageName: String = ""
    private var pageAdapter: ExplorerPagerAdapter? = null
    private lateinit var binding: ActivityApkExplorerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApkExplorerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.apply {
            apkSourcePath = getStringExtra("apk_source_path") ?: ""
            appPackageName = getStringExtra("package_name") ?: ""
        }

        initViews()

        onBackKeyPressed()
    }

    private fun initViews() {
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        pageAdapter = ExplorerPagerAdapter(this, this, apkSourcePath, appPackageName, tabTitleRes) {
            binding.loading.hide()
        }
        binding.viewPager.adapter = pageAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.setText(tabTitleRes[position])
        }.attach()
    }

    /**
     * 当年文件夹层级浏览的时候，如果按返回按键，则需要返回上一层
     */
    private fun onBackKeyPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val r = pageAdapter?.onBackPressed() ?: false
                // false的时候，说明没有拦截
                if (!r) {
                    this.isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}
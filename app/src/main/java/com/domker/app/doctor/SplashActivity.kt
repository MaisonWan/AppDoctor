package com.domker.app.doctor

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.databinding.SplashLayoutBinding
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.util.log

private const val ACTION_ANIMATION_BEGIN = 0
private const val ACTION_ANIMATION_END = 1

/**
 * Created by wanlipeng on 2018/3/5.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var mHandler: Handler
    private lateinit var handlerThread: HandlerThread
    private lateinit var binding: SplashLayoutBinding
    private var startTimeStamp = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationView.speed = 1f
        binding.animationView.playAnimation()

        handlerThread = HandlerThread("initThread")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper) { msg ->
            when (msg.what) {
                ACTION_ANIMATION_BEGIN -> {
                    animationBegin()
                    true
                }
                ACTION_ANIMATION_END -> {
                    animationEnd()
                    true
                }
                else -> {
                    log("SplashActivityTag", "when else")
                    false
                }
            }
        }
    }

    private fun animationEnd() {
        runOnUiThread {
            binding.animationView.cancelAnimation()
        }
        openMainActivity()
    }

    private fun animationBegin() {
        updateDatabase()
        mHandler.removeMessages(ACTION_ANIMATION_END)
        if (System.currentTimeMillis() - startTimeStamp > 500) {
            mHandler.sendEmptyMessage(ACTION_ANIMATION_END)
        } else {
            mHandler.sendEmptyMessageDelayed(ACTION_ANIMATION_END, 500)
        }
    }

    private fun updateDatabase() {
        log("before updateAppList")
        val checker = AppCheckFactory.instance
        if (checker.fetchAppListFromDatabase().isEmpty()) {
            checker.updateAppListFromSystem()
        }
        log("after updateAppList")
    }

    override fun onResume() {
        super.onResume()
        startTimeStamp = System.currentTimeMillis()
        mHandler.sendEmptyMessage(ACTION_ANIMATION_BEGIN)
        mHandler.sendEmptyMessageDelayed(ACTION_ANIMATION_END, 3000)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    private fun openMainActivity() {
        ARouter.getInstance()
            .build(Router.MAIN_ACTIVITY)
            .navigation()
        this.overridePendingTransition(0, 0)
        finish()
    }
}
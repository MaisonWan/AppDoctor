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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SplashLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handlerThread = HandlerThread("initThread")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper) { msg ->
            when (msg.what) {
                ACTION_ANIMATION_BEGIN -> {
                    log("SplashActivityTag", "before updateAppList")
                    val checker = AppCheckFactory.getInstance(applicationContext)
                    if (checker.updateAppListFromDatabase().isEmpty()) {
                        checker.updateAppListFromSystem()
                    }
                    log("SplashActivityTag", "after updateAppList")
                    mHandler.removeMessages(ACTION_ANIMATION_END)
                    mHandler.sendEmptyMessage(ACTION_ANIMATION_END)
                    true
                }
                ACTION_ANIMATION_END -> {
                    runOnUiThread {
                        binding.animationView.cancelAnimation()
                    }
                    openMainActivity()
                    true
                }
                else -> {
                    log("SplashActivityTag", "when else")
                    false
                }
            }
        }
        binding.animationView.speed = 1f
    }

    override fun onResume() {
        super.onResume()
        binding.animationView.playAnimation()
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
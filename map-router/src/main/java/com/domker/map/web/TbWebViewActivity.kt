package com.domker.map.web

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.TextView
import com.domker.map.R
import com.domker.map.utils.CustomProgressDialog
import com.domker.map.utils.CustomProgressDialog.Companion.createDialog
import com.domker.map.web.TbUrlBridge.overrideUrl
import com.domker.map.web.TbWebViewActivity
import java.util.*

/**
 * 贴吧通用WebView，支持设置cookie、自定义javascript interface
 *
 *
 * 文档： 工程根目录/doc/index.html
 *
 * @author zhaoxianlie
 */
@SuppressLint("SetJavaScriptEnabled")
class TbWebViewActivity : Activity() {
    private var tvTitle: TextView? = null
    private var tvBack: TextView? = null
    private val title: String? = null
    private var mTitle: String? = null
    private var mUrl: String? = null
    private var mWebView: WebView? = null
    private val mHandler: Handler? = Handler()
    private val mRunnable = Runnable { refresh() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tb_webview_activity)
        initTitleView()

        // 数据初始化
        val intent = this.intent
        mUrl = intent.getStringExtra(TAG_URL)
        mTitle = intent.getStringExtra(TAG_TITLE)
        if (!TextUtils.isEmpty(mTitle)) tvTitle!!.text = mTitle
        if (TextUtils.isEmpty(mUrl)) {
            return
        }
        // 同步cookie
        initCookie()
        initWebView()
        mHandler!!.postDelayed(mRunnable, 500)
    }

    private fun initTitleView() {
        tvBack = findViewById<View>(R.id.tv_back) as TextView
        tvTitle = findViewById<View>(R.id.tv_title_info) as TextView
        tvBack!!.setOnClickListener { finish() }
    }

    /**
     * 同步cookie
     */
    private fun initCookie() {
        CookieSyncManager.createInstance(this)
        val cookieManager = CookieManager.getInstance()
        if (mCookieMap != null && !mCookieMap!!.isEmpty()) {
            cookieManager.setAcceptCookie(true)
            val it: Iterator<String> = mCookieMap!!.keys.iterator()
            while (it.hasNext()) {
                val domain = it.next()
                cookieManager.setCookie(domain, mCookieMap!![domain])
            }
        } else {
            cookieManager.removeAllCookie()
        }
        CookieSyncManager.getInstance().sync()
    }

    /**
     * 给WebView增加js interface，供FE调用
     */
    @SuppressLint("JavascriptInterface")
    private fun addJavascriptInterface() {
        if (!mEnableJsInterface) {
            return
        }
        if (mJsInterfaces == null) {
            mJsInterfaces = HashMap()
        }
        // 添加一个通用的js interface接口：TbJsBridge
        if (!mJsInterfaces!!.containsKey("TbJsBridge")) {
            mJsInterfaces!!["TbJsBridge"] = object : JavascriptInterface {
                override fun createJsInterface(activity: Activity?): Any {
                    return TbJsBridge(activity!!)
                }
            }
        }

        // 增加javascript接口的支持
        val it: Iterator<String> = mJsInterfaces!!.keys.iterator()
        while (it.hasNext()) {
            val key = it.next()
            val jsInterface = mJsInterfaces!![key]!!.createJsInterface(this)
            mWebView?.addJavascriptInterface(jsInterface, key)
        }
    }

    /**
     * 初始化webview的相关参数
     *
     * @return
     */
    private fun initWebView() {
        try {
            mWebView = findViewById<View>(R.id.webview_entity) as WebView
            // 启用js功能
            mWebView!!.settings.javaScriptEnabled = true
            // 增加javascript接口的支持
            addJavascriptInterface()
            // 滚动条设置
            mWebView!!.settings.domStorageEnabled = true
            mWebView!!.settings.databaseEnabled = true
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                mWebView!!.settings.databasePath =
                    "/data/data/" + mWebView!!.context.packageName + "/databases/"
            }
            mWebView!!.isHorizontalScrollBarEnabled = false
            mWebView!!.setHorizontalScrollbarOverlay(false)
            mWebView!!.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY
            // 必须要设置这个，要不然，webview加载页面以后，会被放大，这里的100表示页面按照原来尺寸的100%显示，不缩放
//			mWebView.setInitialScale(100);
            // 处理webview中的各种通知、请求事件等
            // 处理webview中的js对话框、网站图标、网站title、加载进度等
            mWebView!!.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress == 100) {
                        stopDialog()
                    }
                }
            }
            mWebView!!.webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (TextUtils.isEmpty(url)) {
                        return false
                    }
                    // 通用url跳转规则
                    if (overrideUrl(this@TbWebViewActivity, url)) {
                        return true
                    } else {
                        // 非通用url规则，则用当前webview直接打开
                        try {
                            if (url.startsWith("weixin://") //微信
                                || url.startsWith("alipays://") //支付宝
                                || url.startsWith("mailto://") //邮件
                                || url.startsWith("tel://") //电话
                                || url.startsWith("baidumap://") //大众点评
                            //其他自定义的scheme
                            ) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                                startActivity(intent);
                                return true
                            }
                        } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                            return true //没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
                        }
                        mUrl = url
                        refresh()
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    if (TextUtils.isEmpty(mTitle)) {
                        tvTitle!!.text = view.title.toString()
                        mTitle = view.title.toString()
                    }
                }
            }

            // 使webview支持后退
            mWebView!!.setOnKeyListener(View.OnKeyListener { view, keyCode, keyEvent ->
                if (mWebView!!.canGoBack() // webview当前可以返回
                    && keyEvent.action == KeyEvent.ACTION_DOWN // 有按键行为
                    && keyCode == KeyEvent.KEYCODE_BACK
                ) { // 按下了后退键
                    mWebView!!.goBack() // 后退
                    return@OnKeyListener true
                }
                false
            })
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mWebView == null) {
            return
        }
        // 控制视频音频的，获取焦点播放，失去焦点停止
        mWebView!!.resumeTimers()
        callHiddenWebViewMethod("onResume")
    }

    override fun onPause() {
        super.onPause()
        if (mWebView == null) {
            return
        }
        mWebView!!.pauseTimers()
        callHiddenWebViewMethod("onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler?.removeCallbacks(mRunnable)
    }

    /**
     * 调用WebView本身的一些方法，有视频音频播放的情况下，必须加这个
     *
     * @param name
     */
    private fun callHiddenWebViewMethod(name: String) {
        if (mWebView != null) {
            try {
                val method = WebView::class.java.getMethod(name)
                method.invoke(mWebView)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    /**
     * 刷新WebView，重新加载数据
     */
    fun refresh() {
        if (mWebView != null) {
            showDialog("正在加载...")
            mWebView!!.loadUrl(mUrl!!)
        }
    }

    /**
     * 给WebView增加Javascript Interface的时候，在HashMap中加这个就行了，例子：
     *
     *
     * <pre>
     * HashMap<String></String>, JavascriptInterface> jsInterface = new HashMap<String></String>, JavascriptInterface>();
     * jsInterface.put("TbJsBridge", new JavascriptInterface() {
     *
     * @author zhaoxianlie
     * @Override public TbJsBridge createJsInterface(Activity activity) {
     * return new TbJsBridge(activity);
     * }
     * });
     * TbWebviewActivity.startActivity(AboutActivity.this,
     * "http://www.baidu.com", null, jsInterface);
    </pre> *
     */
    interface JavascriptInterface {
        fun createJsInterface(activity: Activity?): Any
    }

    private var dialog: CustomProgressDialog? = null

    /**
     * 显示加载对话框
     *
     * @param msg
     */
    fun showDialog(msg: String?) {
        if (dialog == null) {
            dialog = createDialog(this)
        }
        if (!msg.isNullOrBlank()) {
            dialog!!.setMessage(msg)
        }
        dialog!!.show()
    }

    /**
     * 关闭对话框
     */
    fun stopDialog() {
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    companion object {
        const val TAG_URL = "tag_url"
        const val TAG_TITLE = "tag_title"

        /**
         * 接受从外界动态绑定javascriptInterface，可以绑定多个
         */
        private var mJsInterfaces: HashMap<String, JavascriptInterface>? = null
        private var mEnableJsInterface = true

        /**
         * 接受从外界动态设置cookie，可以设置多个域的cookie
         */
        private var mCookieMap: HashMap<String, String>? = null

        /**
         * 启动WebView，支持设置：cookie、js interface
         *
         * @param context
         * @param url               网址
         * @param cookieMap         自定义cookie，格式为HashMap<Domain></Domain>,Cookie>
         * @param enableJsInterface 是否需要支持自定义的javascript interface
         * @param jsInterface       自定义的javascript interface
         */
        private fun startActivity(
            context: Context, title: String?, url: String, cookieMap: HashMap<String, String>?,
            enableJsInterface: Boolean, jsInterface: HashMap<String, JavascriptInterface>?
        ) {
            val intent = Intent(context, TbWebViewActivity::class.java)
            intent.putExtra(TAG_URL, url)
            intent.putExtra(TAG_TITLE, title)
            mCookieMap = cookieMap
            mJsInterfaces = jsInterface
            mEnableJsInterface = enableJsInterface
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        /**
         * 启动WebView，支持设置：cookie、js interface
         *
         * @param context
         * @param url         网址
         * @param cookieMap   自定义cookie，格式为HashMap<Domain></Domain>,Cookie>
         * @param jsInterface 自定义的javascript interface
         */
        fun startActivity(
            context: Context, title: String?, url: String, cookieMap: HashMap<String, String>?,
            jsInterface: HashMap<String, JavascriptInterface>?
        ) {
            startActivity(context, title, url, cookieMap, true, jsInterface)
        }

        /**
         * 启动一个不携带cookie、不支持javascript interface的WebView
         *
         * @param context
         * @param url
         */
        fun startActivity(context: Context, title: String?, url: String) {
            startActivity(context, title, url, null, false, null)
        }

        /**
         * 启动一个不携带cookie、不支持javascript interface的WebView
         *
         * @param context
         * @param url
         */
        fun startActivity(context: Context, url: String) {
            startActivity(context, null, url, null, false, null)
        }
    }
}
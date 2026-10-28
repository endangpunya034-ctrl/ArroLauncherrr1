
package com.arro.launcher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.*
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)

        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.mediaPlaybackRequiresUserGesture = false

        webView.isFocusable = true
        webView.isFocusableInTouchMode = true
        webView.addJavascriptInterface(AndroidBridge(), "Android")

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                if (url.startsWith("arro://launch")) {
                    val pkg = Uri.parse(url).getQueryParameter("package")
                    if (pkg != null) launchApp(pkg)
                    return true
                }
                return false
            }
        }
        webView.webChromeClient = WebChromeClient()
        webView.loadUrl("file:///android_asset/arro-launcher.html")
    }

    inner class AndroidBridge {
        @JavascriptInterface
        fun launchApp(packageName: String) {
            runOnUiThread { this@MainActivity.launchApp(packageName) }
        }
        @JavascriptInterface
        fun toast(msg: String) {
            runOnUiThread { Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun launchApp(packageName: String) {
        try {
            val mapped = when(packageName) {
                "youtube" -> "com.google.android.youtube.tv"
                "netflix" -> "com.netflix.ninja"
                "vidio" -> "com.vidio.android.tv"
                "spotify" -> "com.spotify.tv.android"
                "chrome" -> "com.android.chrome"
                "kodi" -> "org.xbmc.kodi"
                else -> packageName
            }
            val pm = packageManager
            val intent = pm.getLaunchIntentForPackage(mapped)
            if (intent != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Membuka " + mapped + "...", Toast.LENGTH_SHORT).show()
                if (mapped == "settings") {
                    startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "App tidak ditemukan: " + packageName, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {}
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }
}

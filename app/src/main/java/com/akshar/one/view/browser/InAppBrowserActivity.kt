package com.akshar.one.view.browser

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.akshar.one.R
import com.akshar.one.view.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_in_app_browser.*
import kotlinx.android.synthetic.main.activity_in_app_browser.linProgressIndicator
import kotlinx.android.synthetic.main.fragment_login.*

class InAppBrowserActivity : BaseActivity() {

    companion object{
        val TITLE = "title"
        val URL = "url"
    }

    private var title: String? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_browser)

        val bundle = intent.extras
        title = bundle?.getString(TITLE)
        url = bundle?.getString(URL)

        txtTitle.text = title

        showProgressIndicator(true)

        initialWebViewSetUp()

        setListeners()
    }

    private fun setListeners() {
        imgBack.setOnClickListener { onBackPressed() }
    }

    private fun initialWebViewSetUp() {
        webView.setInitialScale(1)
        val settings = webView.settings
        with(settings) {
            javaScriptEnabled = true
            setSupportZoom(true)
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = true
            displayZoomControls = false
            domStorageEnabled = true
            allowFileAccessFromFileURLs = true
            allowUniversalAccessFromFileURLs = true
        }

        val webViewClient = WebViewClientImpl(this)
        webView.webViewClient = webViewClient
        url?.let {
            webView.loadUrl(it)
        }
    }

    private fun showProgressIndicator(isLoading: Boolean?) {
        linProgressIndicator.visibility = if (isLoading == true) View.VISIBLE else View.GONE
    }

    inner class WebViewClientImpl(val activity: Activity) : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            showProgressIndicator(false)
        }
    }
}
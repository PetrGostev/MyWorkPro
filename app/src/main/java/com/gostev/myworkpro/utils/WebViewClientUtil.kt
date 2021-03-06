package com.gostev.myworkpro.utils

import android.annotation.TargetApi
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewClientUtil : WebViewClient() {
	@TargetApi(Build.VERSION_CODES.N)
	override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
		view.loadUrl(request.url.toString())
		return true
	}

	override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
		view.loadUrl(url)
		return true
	}
}
package com.gostev.myworkpro.web

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.gostev.myworkpro.R
import com.gostev.myworkpro.utils.WebViewClientUtil

class WebActivity : AppCompatActivity() {
	lateinit var webView:WebView

	@SuppressLint("SetJavaScriptEnabled")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_web)

		webView = this.findViewById(R.id.webView);
		webView.getSettings().javaScriptEnabled = true
		webView.webViewClient = WebViewClientUtil()
		webView.loadUrl("https://rifme.net/");

		webView.evaluateJavascript("(function(){return window.getSelection().toString()})()") { }
	}

	override fun onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack()
		} else {
			super.onBackPressed()
		}
	}
}

package com.gostev.myworkpro.web

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gostev.myworkpro.R
import com.gostev.myworkpro.utils.WebViewClientUtil
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

	@SuppressLint("SetJavaScriptEnabled")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_web)

		web_view.getSettings().javaScriptEnabled = true
		web_view.webViewClient = WebViewClientUtil()
		web_view.loadUrl("https://rifme.net/");

		web_view.evaluateJavascript("(function(){return window.getSelection().toString()})()") { }
	}

	override fun onBackPressed() {
		if (web_view.canGoBack()) {
			web_view.goBack()
		} else {
			super.onBackPressed()
		}
	}
}

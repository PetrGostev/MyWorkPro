package com.gostev.myworkpro.info

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.gostev.myworkpro.R
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

	private lateinit var clearlyButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_info)
		setSupportActionBar(toolbar)
		clearlyButton = findViewById(R.id.clearly)

		clearlyButton.setOnClickListener { finish() }
	}
}

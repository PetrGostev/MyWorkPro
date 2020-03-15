package com.gostev.myworkpro.models

class WriteModel {
    private lateinit var mTitle:String

    fun setTitle(name: String) {
        val stringParse: List<String> = name.split(".txt")
        mTitle = stringParse[0]
    }

    fun getTitle(): String {
        return mTitle
    }
}
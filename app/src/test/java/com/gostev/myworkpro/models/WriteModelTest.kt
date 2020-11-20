package com.gostev.myworkpro.models

import org.junit.Test

import org.junit.Assert.*

class WriteModelTest {

	@Test
	fun setTitle() {
		val stringParse: List<String> = "Проверка.txt".split(".txt")
		assertEquals("Проверка", stringParse[0])
	}
}
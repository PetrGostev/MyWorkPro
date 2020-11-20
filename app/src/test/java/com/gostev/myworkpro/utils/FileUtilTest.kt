package com.gostev.myworkpro.utils

import android.os.Environment
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import java.io.File

class FileUtilTest {
	//	@Mock
	lateinit var file: File

	@Test
	fun testCreateDir() {
//		file = File("", "MyWork")
//		val baseDir: File = file
//		//		var folder = File(baseDir, "MyFile")
//		var folder = createDir("MyFile")

		//		if (folder.isFile) {
		//			folder.delete()
		//		}
		//
		//		if (!folder.mkdirs()) {
		//			folder = baseDir
		//		}
//		assertEquals("MyFile", folder.name)
//		folder.delete()
	}

	@Test
	fun parseFile() {
		var mTitleWrite: String = ""
		var mTextWrite: String = ""
		var mDateWrite: String = ""
		var mPath: String =
			"Ночь" + "\n" + "Уходят в прошлое печали дня" + "\n" + "И лишь хорошее здесь ждет меня" + "\n" + "20.02.20" + "\n"
		if (mPath.isNotEmpty()) {
			val stringParse: List<String> = mPath.split("\n")
			mTitleWrite = stringParse[0]
			mDateWrite = stringParse[stringParse.size - 2]

			for (i in 1..stringParse.size - 3) {
				if (stringParse[i].isNotEmpty()) {
					mTextWrite = mTextWrite + stringParse[i] + "\n"
				}
			}
		}
		assertEquals("Ночь", mTitleWrite)
		assertEquals("20.02.20", mDateWrite)
		assertEquals("Уходят в прошлое печали дня" + "\n" + "И лишь хорошее здесь ждет меня" + "\n",
			mTextWrite)
	}

	fun createDir(name: String): File? {
		file = File("", "MyWork")
		val baseDir: File = file
		//		var folder = File(baseDir, name"MyFile")
		//		val baseDir: File = Environment.getExternalStorageDirectory()
		val folder = File(baseDir, name)
		if (folder.exists()) {
			return folder
		}
		if (folder.isFile) {
			folder.delete()
		}
		return if (folder.mkdirs()) {
			folder
		} else Environment.getExternalStorageDirectory()
	}
}

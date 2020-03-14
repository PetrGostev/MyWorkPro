package com.gostev.myworkpro.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import android.widget.Toast
import com.gostev.myworkpro.R
import java.io.*

class FileUtil {
	companion object {
		private const val DIRECTORY_NAME = "MyWork"
		private const val FILE_FORMAT = ".txt"
	}

	private lateinit var mTitleWrite: String
	private lateinit var mTextWrite: String
	private lateinit var mDateWrite: String
	private lateinit var  myFile : File

	interface FileUtilCallback {
		fun done(ok: Boolean)
	}

	fun createDir(name: String): File? {
		val myFile = File(DIRECTORY_NAME)
		val baseDir: File = Environment.getExternalStorageDirectory()
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

	fun writeFile(name: String, write: String, dateWrite: String, context: Context): Unit {
		try {
			val myFile = File("""${createDir(DIRECTORY_NAME).toString()}/$name$FILE_FORMAT""")
			myFile.createNewFile()
			val outputStream = FileOutputStream(myFile)
			outputStream.write(name.toByteArray())
			outputStream.write("\n".toByteArray())
			outputStream.write("\n".toByteArray())
			outputStream.write(write.toByteArray())
			outputStream.write("\n".toByteArray())
			outputStream.write("\n".toByteArray())
			outputStream.write(dateWrite.toByteArray())
			outputStream.close()
			Toast.makeText(context, context.getString(R.string.write_saved), Toast.LENGTH_SHORT).show()

			//для обнаружения на компе
			val path = arrayOfNulls<String>(1)
			path[0] = "$DIRECTORY_NAME/$name$FILE_FORMAT"
			MediaScannerConnection.scanFile(context, path, null, null);
		} catch (e: Exception) {
			Toast.makeText(context, context.getString(R.string.enabled_permissions), Toast.LENGTH_SHORT)
				.show()
		}
	}

	fun readFile(name: String, callback: FileUtilCallback) {
		var write: String = ""
		myFile = File("""${createDir(DIRECTORY_NAME).toString()}/$name$FILE_FORMAT""")
		try {
			val inputStream = FileInputStream(myFile)
			val bufferedReader = BufferedReader(InputStreamReader(inputStream))
			var line: String?
			try {
				while (bufferedReader.readLine().also { line = it } != null) {
					write = write + line + "\n"
				}
			} catch (e: IOException) {
				e.printStackTrace()
				callback.done(false)
			}
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
			callback.done(false)
		}

		parseFile(write)
		callback.done(true)
	}

	fun removeFile(name: String) {
		val file: File = File("""${createDir(DIRECTORY_NAME).toString()}/$name$FILE_FORMAT""")
		if (file.exists()) {
			file.delete()
		}
	}

	private fun parseFile(path: String) {
		mTitleWrite = ""
		mDateWrite = ""
		mTextWrite = ""
		if (path.isNotEmpty()) {
			val stringParse: List<String> = path.split("\n")
			mTitleWrite = stringParse[0]
			mDateWrite = stringParse[stringParse.size - 2]

			for (i in 1..stringParse.size - 3) {
				if (stringParse[i].isNotEmpty()) {
					mTextWrite = mTextWrite + stringParse[i] + "\n"
				}
			}
		}

	}

	fun getFile(): File {
		return myFile
	}

	fun getTitleWrite(): String {
		return mTitleWrite
	}

	fun getTextWrite(): String {
		return mTextWrite
	}

	fun getDateWrite(): String {
		return mDateWrite
	}
}
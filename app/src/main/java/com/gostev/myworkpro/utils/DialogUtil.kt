package com.gostev.myworkpro.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.AlertDialog.Builder

class DialogUtil {
	fun showEditDialog(getActivity: Activity?, title: String?, callback: EditCallback) {
		val alertDialog: AlertDialog = Builder(getActivity).create()
		alertDialog.setTitle(title)
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена") { dialog, _ ->
			dialog.dismiss()
			callback.done(false)
		}
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Да") { dialog, _ ->
			dialog.dismiss()
			callback.done(true)
		}
		alertDialog.show()
	}

	fun showEditFileDialog(getActivity: Activity?, callback: EditCallback) {
		val alertDialog: AlertDialog = Builder(getActivity).create()

		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Переименовать файл") { dialog, _ ->
			dialog.dismiss()
			callback.done(true)
		}
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Создать новый") { dialog, _ ->
			dialog.dismiss()
			callback.done(false)
		}
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Отмена") { dialog, _ ->
			dialog.dismiss()
		}


		alertDialog.show()
	}

	fun showInfoDialog(getActivity: Activity?, title: String?) {
		val builder = Builder(getActivity)
		builder.setTitle(title).setCancelable(false)
			.setNegativeButton("ОК") { dialog, id -> dialog.cancel() }
		val dialog = builder.create()
		dialog.show()
	}

	interface EditCallback {
		fun done(ok: Boolean)
	}
}
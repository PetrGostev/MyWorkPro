package com.gostev.myworkpro.write

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gostev.myworkpro.R
import com.gostev.myworkpro.utils.DialogUtil
import com.gostev.myworkpro.utils.FileUtil
import com.gostev.myworkpro.utils.FileUtil.FileUtilCallback
import com.gostev.myworkpro.utils.ModeType
import com.gostev.myworkpro.web.WebActivity
import com.gostev.myworkpro.writes.WritesActivity
import com.gostev.myworkpro.writes.WritesPresenter
import com.jakewharton.rxbinding.widget.RxTextView
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlinx.android.synthetic.main.fragment_write.*

open class WriteFragment : Fragment(R.layout.fragment_write) {
	companion object {
		private const val NAME_FILE = "NAME_FILE"
	}

	private var editItem: MenuItem? = null
	private var saveItem: MenuItem? = null
	private var sendItem: MenuItem? = null
	private var searchItem: MenuItem? = null

	private lateinit var writesActivity: WritesActivity
	private lateinit var mNameFile: String
	private var mIsForEdit: Boolean = false
	private lateinit var textWriteSub: Subscription
	private lateinit var dialogUtil: DialogUtil
	private lateinit var fileUtil: FileUtil
	private lateinit var writesPresenter: WritesPresenter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val args = this.arguments
		if (args != null) {
			mNameFile = args.getString(NAME_FILE).toString()
		}
		setHasOptionsMenu(true)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		writesActivity = activity as WritesActivity

		dialogUtil = writesActivity.getDialogUtil()
		fileUtil = writesActivity.getFileUtil()
		writesPresenter = writesActivity.getPresenter()

		setupViews()
	}

	override fun onResume() {
		super.onResume()
		textWriteSub = Subscriptions.unsubscribed()

		val text: String
		if (write.text.isNotEmpty()) {
			text = write.text.toString()
		} else {
			text = " "
		}
		subscribeEditTextPrint(text)
	}

	override fun onStop() {
		super.onStop()
		textWriteSub.unsubscribe()
	}

	private fun setIsForEdit(isEdit: Boolean) {
		mIsForEdit = isEdit
		title.isEnabled = isEdit
		write.isEnabled = isEdit
		editItem?.isVisible = !isEdit
		sendItem?.isVisible = !isEdit
		searchItem?.isVisible = isEdit

		saveItem?.isVisible = writesPresenter.isVisibleSaveItem()
	}

	private fun setupViews() {
		writesActivity.fab.visibility = View.GONE

		if (editItem != null) {
			mIsForEdit =
				writesPresenter.getModeType() == ModeType.EDIT || writesPresenter.getModeType() == ModeType.CREATE
		}

		fileUtil.readFile(mNameFile, object : FileUtilCallback {
			override fun done(ok: Boolean) {
				if (ok) {
					title.setText(fileUtil.getTitleWrite())
					write.setText(fileUtil.getTextWrite())
					date_write.setText(fileUtil.getDateWrite())
				}
			}
		})
	}

	private fun subscribeEditTextNoPrint() {
		textWriteSub = RxTextView.textChanges(write).debounce(3, TimeUnit.SECONDS)
			.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
			.subscribe {
				write.append("\n");
				textWriteSub.unsubscribe()
				subscribeEditTextPrint(write.text.toString())
			}
	}

	private fun subscribeEditTextPrint(text: String) {

		if (textWriteSub.isUnsubscribed) {
			textWriteSub = RxTextView.beforeTextChangeEvents(write).skip(1)
				.observeOn(AndroidSchedulers.mainThread()).map { it.text() }.map { it.toString() }
				.subscribe {
					if (saveItem?.isVisible == false) {
						saveItem?.isVisible = true
						writesPresenter.setVisibleSaveItem(true)
					}
					if (it[it.lastIndex].toString() != " ") {
						if (it[it.lastIndex] != text[text.lastIndex]) {
							textWriteSub.unsubscribe()
							subscribeEditTextNoPrint()
						}
					}
				}
		}
		RxTextView.beforeTextChangeEvents(title).skip(1).observeOn(AndroidSchedulers.mainThread())
			.map { it.text() }.map { it.toString() }.subscribe {
					saveItem?.isVisible = true
					writesPresenter.setVisibleSaveItem(true)
			}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		menu.clear()
		inflater.inflate(R.menu.menu_fragment, menu)
		editItem = menu.findItem(R.id.edit_fragment)
		saveItem = menu.findItem(R.id.save_fragment)
		sendItem = menu.findItem(R.id.send_fragment)
		searchItem = menu.findItem(R.id.search_fragment)

		setIsForEdit(writesPresenter.getModeType() == ModeType.EDIT || writesPresenter.getModeType() == ModeType.CREATE)
	}

	@SuppressLint("SetTextI18n")
	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val id = item.itemId
		if (id == R.id.save_fragment) {
			hideKeyboard()
			if (title.text.isEmpty()) {
				dialogUtil.showInfoDialog(activity, getString(R.string.title_required))
			} else {
				val simpleDateFormat =
					SimpleDateFormat(getString(R.string.date_format), Locale.getDefault())
				val date: String = simpleDateFormat.format(Date())

				if (writesPresenter.getModeType() == ModeType.EDIT) {
					if (date_write.text.isEmpty()) {
						date_write.text = getString(R.string.date_write, date)
					}

					if (title.text.split(" ") != fileUtil.getTitleWrite().split(" ")) {
						dialogUtil.showEditFileDialog(writesActivity,
							object : DialogUtil.EditCallback {
								override fun done(ok: Boolean) {
									if (ok) {
										fileUtil.removeFile(fileUtil.getTitleWrite())
									}
									writeFile()
									closeFragment()
								}
							})
					} else {
						showEditDialog()
					}

				} else {
					date_write.text = getString(R.string.date_write, date)
					showEditDialog()
				}
			}
		}
		if (id == R.id.edit_fragment) {
			setIsForEdit(true)
			writesPresenter.setModeType(ModeType.EDIT)
		}
		if (id == R.id.send_fragment) {
			setIsForEdit(false)
			writesPresenter.setModeType(ModeType.READ)

			val sendIntent: Intent = Intent(Intent.ACTION_SEND);
			sendIntent.type = "text/plain";
			sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileUtil.getFile()))
			writesActivity.startActivity(Intent.createChooser(sendIntent, title.text))
		}
		if (id == R.id.search_fragment) {
			val intent = Intent(writesActivity, WebActivity::class.java)
			startActivity(intent)
		}
		return super.onOptionsItemSelected(item)
	}

	private fun showEditDialog() {
		dialogUtil.showEditDialog(activity,
			getString(R.string.save_text),
			object : DialogUtil.EditCallback {
				override fun done(ok: Boolean) {
					if (ok) {
						writeFile()
						closeFragment()
					}
				}
			})
	}

	private fun writeFile() {
		fileUtil.writeFile(title.text.toString(),
			write.text.toString(),
			date_write.text.toString(),
			writesActivity)

		setIsForEdit(false)
		writesPresenter.setVisibleSaveItem(false)
		writesPresenter.setModeType(ModeType.READ)
	}

	private fun hideKeyboard() {
		val imm =
			writesActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		imm.hideSoftInputFromWindow(this.view?.windowToken, 0)
	}

	fun closeFragment() {
		writesActivity.supportFragmentManager.popBackStack()
		hideKeyboard()
		writesPresenter.obtainData()
		writesActivity.fab.visibility = View.VISIBLE
	}
}

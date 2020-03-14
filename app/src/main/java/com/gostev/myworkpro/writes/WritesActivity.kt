package com.gostev.myworkpro.writes

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gostev.myworkpro.R
import com.gostev.myworkpro.adapters.WritesAdapter
import com.gostev.myworkpro.info.InfoActivity
import com.gostev.myworkpro.models.WriteModel
import com.gostev.myworkpro.utils.DialogUtil
import com.gostev.myworkpro.utils.FileUtil
import com.gostev.myworkpro.utils.ModeType
import com.gostev.myworkpro.utils.PermissionsUtil
import com.gostev.myworkpro.write.WriteFragment

class WritesActivity : MvpAppCompatActivity(), WritesActivityMvpView, WritesAdapter.OnViewClickListener {

	companion object {
		private const val NAME_FILE = "NAME_FILE"
	}

	private lateinit var mRecyclerView: RecyclerView
	private lateinit var mWelcomeText: TextView
	private lateinit var mFab: FloatingActionButton

	private lateinit var mAdapter: WritesAdapter
	private var writeFragment: WriteFragment? = null

	private var mDialogUtil: DialogUtil =
		DialogUtil()
	private lateinit var mFileUtil: FileUtil
	private lateinit var mPermissionsUtil: PermissionsUtil

	private var mIsForEdt: Boolean = false

	@InjectPresenter
	lateinit var mPresenter: WritesPresenter

	@ProvidePresenter
	fun provideWritesPresenter(): WritesPresenter? {
		return WritesPresenter()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		mPermissionsUtil = PermissionsUtil()

		val toolbar: Toolbar = findViewById(R.id.toolbar)
		setSupportActionBar(toolbar)
		supportActionBar!!.setDisplayShowTitleEnabled(false)

		val title: TextView = findViewById(R.id.title_view)
		title.text = getString(R.string.app_name)

		mRecyclerView = findViewById(R.id.recycler)
		mWelcomeText = findViewById(R.id.welcome_text)
		mFab = findViewById(R.id.fab)

		mFileUtil = mPresenter.getFileUtil()

		checkPermissions()
	}

	private fun checkPermissions() {
		mPermissionsUtil.checkLocationPermissions(this,
			object : PermissionsUtil.PermissionsOkListener {
				override fun permissionsEnabled(enabled: Boolean) {
					if (enabled) {
						setupViews()
					} else {
						mDialogUtil.showInfoDialog(this@WritesActivity,
							getString(R.string.permissions_required))
					}
				}
			})
	}

	override fun onBackPressed() {
		val fragment = mPresenter.getWriteFragment()

		if (fragment != null && (mPresenter.getModeType() == ModeType.EDIT || mPresenter.getModeType() == ModeType.CREATE)) {
			mDialogUtil.showEditDialog(this,
				getString(R.string.no_save),
				object : DialogUtil.EditCallback {
					override fun done(ok: Boolean) {
						if (ok) {
							mPresenter.setWriteFragment(null)
							mFab.visibility = View.VISIBLE
							this@WritesActivity.onBackPressed()
						}
					}
				})
		} else {
			mFab.visibility = View.VISIBLE
			super.onBackPressed()
		}
		setupViews()
	}

	private fun setupViews() {
		mPresenter.obtainData()
		mFab.setOnClickListener {
			mPresenter.setModeType(ModeType.CREATE)
			mPresenter.setVisibleSaveItem(false)
			presentWriteFragment("")
		}
	}

	private fun visibleWelcomeText(visible: Boolean) {
		if (visible) mWelcomeText.visibility = View.VISIBLE
		else mWelcomeText.visibility = View.GONE
	}

	override fun showWrites(result: ArrayList<WriteModel>) {
		visibleWelcomeText(result.isEmpty())
		mAdapter = WritesAdapter(result, this)

		mRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
		mRecyclerView.itemAnimator = DefaultItemAnimator()
		mRecyclerView.adapter = mAdapter
	}

	override fun onClickWrite(view: View, writeModel: WriteModel) {
		mPresenter.setModeType(ModeType.READ)
		mPresenter.setVisibleSaveItem(false)
		presentWriteFragment(writeModel.getTitle())
	}

	override fun onRemoveWrite(writeModel: WriteModel) {
		mDialogUtil.showEditDialog(this,
			getString(R.string.write_be_deleted),
			object : DialogUtil.EditCallback {
				override fun done(ok: Boolean) {
					if (ok) {
						mFileUtil.removeFile(writeModel.getTitle())
						mPresenter.obtainData()
					} else {
						mPresenter.obtainData()
					}
				}
			})
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.menu_activity, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		val id = item.itemId
		if (id == R.id.vector_info) {
			val intent = Intent(this, InfoActivity::class.java)
			startActivity(intent)
		}
		return super.onOptionsItemSelected(item)
	}

	private fun presentWriteFragment(title: String) {
		val fragmentManager: FragmentManager = supportFragmentManager
		val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
		writeFragment = WriteFragment()

		val args = Bundle()
		args.putString(NAME_FILE, title)
		writeFragment!!.arguments = args
		mPresenter.setWriteFragment(writeFragment!!)

		fragmentTransaction.replace(R.id.frame, writeFragment!!)
		fragmentTransaction.addToBackStack(writeFragment!!.tag)
		fragmentTransaction.commit()
	}

	fun getDialogUtil(): DialogUtil {
		return mDialogUtil
	}

	fun getFileUtil(): FileUtil {
		return mFileUtil
	}

	fun getPresenter(): WritesPresenter {
		return mPresenter
	}
}
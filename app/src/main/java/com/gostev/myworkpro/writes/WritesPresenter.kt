@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.gostev.myworkpro.writes

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.gostev.myworkpro.models.WriteModel
import com.gostev.myworkpro.utils.FileUtil
import com.gostev.myworkpro.utils.ModeType
import com.gostev.myworkpro.write.WriteFragment
import java.io.File

@InjectViewState
class WritesPresenter: MvpPresenter<WritesActivityMvpView>() {
    private var fileUtil: FileUtil = FileUtil()
    private lateinit var modeType: ModeType
    private var isVisibleSaveItem: Boolean = false
    private var writeFragment: WriteFragment? = null

    fun obtainData() {
        val dir = File(
            (fileUtil.createDir("MyWork").toString().intern())
        )

        val f = File(dir.toString())
        val list: ArrayList<WriteModel> = ArrayList()

        if (f.listFiles() != null) {
            f.listFiles().forEach { listFile ->
                val writeModel: WriteModel =
                    WriteModel()
                writeModel.setTitle(listFile.name)
                list.add(writeModel)
            }
        }
        viewState.showWrites(list)
    }

    fun getFileUtil(): FileUtil {
        return fileUtil
    }

    fun setModeType(modeType: ModeType) {
        this.modeType = modeType
    }

    fun getModeType(): ModeType {
        return modeType
    }

    fun setVisibleSaveItem(visible: Boolean) {
        this.isVisibleSaveItem = visible
    }

    fun isVisibleSaveItem(): Boolean {
        return isVisibleSaveItem
    }

    fun setWriteFragment(writeFragment: WriteFragment?) {
    this.writeFragment = writeFragment
    }

    fun getWriteFragment(): WriteFragment? {
        return writeFragment
    }
}
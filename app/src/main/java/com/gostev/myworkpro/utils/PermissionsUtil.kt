package com.gostev.myworkpro.utils

import android.Manifest
import android.app.Activity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class PermissionsUtil {
	fun checkLocationPermissions(activity: Activity?, listener: PermissionsOkListener) {
		val permissionsListener: MultiplePermissionsListener =
			object : MultiplePermissionsListener {
				override fun onPermissionsChecked(report: MultiplePermissionsReport) {
					listener.permissionsEnabled(report.areAllPermissionsGranted())
				}

				override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>,
					token: PermissionToken) {
					token.continuePermissionRequest()
				}
			}
		Dexter.withActivity(activity).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(permissionsListener).check()
	}

	interface PermissionsOkListener {
		fun permissionsEnabled(enabled: Boolean)
	}
}
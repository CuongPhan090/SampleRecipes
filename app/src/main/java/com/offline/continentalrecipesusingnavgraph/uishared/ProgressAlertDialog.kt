package com.offline.continentalrecipesusingnavgraph.uishared

import android.app.Activity
import android.app.AlertDialog
import com.offline.continentalrecipesusingnavgraph.R

class ProgressAlertDialog(private val activity: Activity) {

    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {
        dialog = AlertDialog.Builder(activity)
            .setView(activity.layoutInflater.inflate(R.layout.custom_progress_bar, null))
            .setCancelable(false)
            .create()
        dialog.show()
    }

    fun dismissLoadingDialog() {
        dialog.dismiss()
    }
}

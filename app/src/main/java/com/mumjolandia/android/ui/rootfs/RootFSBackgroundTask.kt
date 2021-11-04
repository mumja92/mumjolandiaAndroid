package com.mumjolandia.android.ui.rootfs

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.mumjolandia.android.utils.MumjolandiaCommunicator
import kotlin.collections.ArrayList

class RootFSBackgroundTask(
    ip: String,
    port: Int,
    private val adapter: RootFSRecyclerViewAdapter?,
    private val textViewCwd: TextView?,
    private val rootView: View,
) : MumjolandiaCommunicator(ip, port) {
    public override fun onPostExecute(result: String) {
        if (result != ""){
            val separatedList: List<String> = result.split("\n")
            when (separatedList[0]) {
                "MumjolandiaReturnValue.rootfs_pwd_ok" -> {
                    textViewCwd?.text = separatedList[1]
                }
                "MumjolandiaReturnValue.rootfs_ls_ok" -> {
                    var filesString = separatedList[1]
                    // remove "['" from the beginning and "]'" from the end
                    filesString = filesString.removeRange(0,2)
                    filesString = filesString.removeRange(filesString.length-2,filesString.length)
                    adapter?.reset(getNewFileArray(filesString.split("\\n")))
                }
                "MumjolandiaReturnValue.rootfs_cd_ok" -> {
                    textViewCwd?.text = separatedList[1]
                }
                else -> {
                    Snackbar.make(rootView, separatedList[1], Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
                    // Indicating that path is broken
                    textViewCwd!!.setTextColor(Color.rgb(200,0,0))
                }
            }
        }
    }

    private fun getNewFileArray(separatedList: List<String>): ArrayList<String>{
        val receivedFiles = ArrayList<String>()
        for (file in separatedList){
            if (file != ""){
                receivedFiles.add(file)
            }
        }
        receivedFiles.add(0, "&..")
        return receivedFiles
    }
}
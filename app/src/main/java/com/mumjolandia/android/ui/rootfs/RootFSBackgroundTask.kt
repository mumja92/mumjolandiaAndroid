package com.mumjolandia.android.ui.rootfs

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.mumjolandia.android.utils.MumjolandiaCommunicator
import com.mumjolandia.android.utils.MumjolandiaResponse
import kotlin.collections.ArrayList

class RootFSBackgroundTask(
    ip: String,
    port: Int,
    private val adapter: RootFSRecyclerViewAdapter?,
    private val textViewCwd: TextView?,
    private val rootView: View,
) : MumjolandiaCommunicator(ip, port) {
    public override fun onPostExecute(result: MumjolandiaResponse) {
        if (result.string != ""){
            val separatedList: List<String> = result.string.split("\n")
            when (separatedList[0]) {
                "MumjolandiaReturnValue.rootfs_pwd_ok" -> {
                    executeCwd(separatedList[1])
                }
                "MumjolandiaReturnValue.rootfs_ls_ok" -> {
                    var filesString = separatedList[1]
                    // remove "['" from the beginning and "]'" from the end
                    filesString = filesString.removeRange(0,2)
                    filesString = filesString.removeRange(filesString.length-2,filesString.length)
                    executeLs(filesString)
                }
                "MumjolandiaReturnValue.rootfs_cd_ok" -> {
                    executeCd(separatedList[1])
                }
                "MumjolandiaReturnValue.connection_client_send_ok" -> {
                    var returnParametersString = separatedList[1]
                    returnParametersString = returnParametersString.removeRange(0,2)
                    returnParametersString = returnParametersString.removeRange(returnParametersString.length-2,returnParametersString.length)
                    val returnParameters = returnParametersString.split("\\n")
                    when (returnParameters[0]){
                        "pwd" -> {
                            executeCwd(returnParameters[1])
                        }
                        "cd" -> {
                            executeCd(returnParameters[1])
                        }
                        "ls" -> {
                            executeLs(returnParametersString.removeRange(0,2))
                        }
                        "closing server" ->{
                            Snackbar.make(rootView, "Server closed successfully", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show()
                        }
                    }
                }
                else -> {
                    Snackbar.make(rootView, separatedList[1], Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show()
                    // Indicating that path is broken
                    textViewCwd!!.setTextColor(Color.rgb(200,0,0))
                    adapter?.reset(ArrayList(emptyList()))
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

    private fun executeCwd(arg: String){
        textViewCwd?.text = arg
    }

    private fun executeCd(arg: String){
        textViewCwd?.text = arg
    }

    private fun executeLs(arg: String){
        adapter?.reset(getNewFileArray(arg.split("\\n")))
    }
}
package com.example.mumjolandiaandroid.ui.planner.task

import android.content.Context
import android.view.View
import com.example.mumjolandiaandroid.utils.MumjolandiaCommunicator
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class TaskBackgroundTask(
    ip: String,
    port: Int,
    private val adapter: TaskRecyclerViewAdapter?,
    private val context: Context?,
    private val rootView: View,
    private val invertList: Boolean = false
) : MumjolandiaCommunicator(ip, port) {
    public override fun onPostExecute(result: String) {
        if (result != ""){
            val separatedList: List<String> = result.split("\n")
            val mumjolandiaReturnValue = separatedList[0]
            if (mumjolandiaReturnValue != "MumjolandiaReturnValue.task_get"){
                var returnStatus = mumjolandiaReturnValue.subSequence(23, mumjolandiaReturnValue.length).toString()
                returnStatus += " " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString()
                returnStatus += ":" + Calendar.getInstance().get(Calendar.MINUTE).toString()
                returnStatus += ":" + Calendar.getInstance().get(Calendar.SECOND).toString()
                Snackbar.make(rootView, returnStatus, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            when (mumjolandiaReturnValue) {
                "MumjolandiaReturnValue.task_get" -> {
                    adapter?.reset(getNewTaskArray(separatedList.drop(1)))
                }
                else -> {
                }
            }
        }
    }

    private fun getNewTaskArray(separatedList: List<String>): ArrayList<String>{
        val receivedTasks = ArrayList<String>()
        for (task in separatedList){
            if (task != ""){
                receivedTasks.add(task)
            }
        }
        return if (invertList) {
            ArrayList(receivedTasks.reversed())
        }
        else{
            receivedTasks
        }
    }
}
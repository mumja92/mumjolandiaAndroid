package com.example.mumjolandiaandroid.ui.planner.task

import android.content.Context
import com.example.mumjolandiaandroid.utils.MumjolandiaCommunicator

class TaskBackgroundTask(
    ip: String,
    port: Int,
    private val adapter: TaskRecyclerViewAdapter?,
    private val context: Context?,
) : MumjolandiaCommunicator(ip, port) {
    public override fun onPostExecute(result: String) {
        if (result != ""){
            val separatedList: List<String> = result.split("\n")
            val mumjolandiaReturnValue = separatedList[0]
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
            receivedTasks.add(task)
        }
        return receivedTasks
    }
}
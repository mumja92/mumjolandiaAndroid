package com.mumjolandia.android.ui.planner.plan

import android.content.Context
import android.view.View
import android.widget.Toast
import com.mumjolandia.android.utils.MumjolandiaCommunicator
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class PlannerBackgroundTask(
    ip: String,
    port: Int,
    private val adapter: PlannerRecyclerViewAdapter?,
    private val context: Context?,
    private val rootView: View,
) : MumjolandiaCommunicator(ip, port) {
    public override fun onPostExecute(result: String) {
        if (result == ""){
            Toast.makeText(context, "Can't connect to mumjolandia", Toast.LENGTH_SHORT).show()
        }
        else{
            val separatedList: List<String> = result.split("\n")
            val mumjolandiaReturnValue = separatedList[0]
            if (mumjolandiaReturnValue != "MumjolandiaReturnValue.planner_get_ok"){
                var returnStatus = mumjolandiaReturnValue.subSequence(23, mumjolandiaReturnValue.length).toString()
                returnStatus += " " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toString()
                returnStatus += ":" + Calendar.getInstance().get(Calendar.MINUTE).toString()
                returnStatus += ":" + Calendar.getInstance().get(Calendar.SECOND).toString()
                Snackbar.make(rootView, returnStatus, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            when (mumjolandiaReturnValue) {
                "MumjolandiaReturnValue.planner_get_ok" -> {
                    adapter?.reset(getNewPlannerTaskArray(separatedList))
                }
                else -> {
                }
            }
        }
    }

    private fun getNewPlannerTaskArray(separatedList: List<String>): ArrayList<PlannerTask>{
        val receivedTasks = ArrayList<PlannerTask>()
        var loopIndex = 5
        while (loopIndex < separatedList.size){
            receivedTasks.add(
                PlannerTask(
                    separatedList[loopIndex - 2],
                    separatedList[loopIndex - 1].toInt(),
                    separatedList[loopIndex])
            )
            loopIndex += 3
        }
        return PlanGenerator().generate(receivedTasks)
    }
}
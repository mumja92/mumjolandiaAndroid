package com.example.mumjolandiaandroid.ui.planner.plan

import android.app.Activity
import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.example.mumjolandiaandroid.R
import com.example.mumjolandiaandroid.utils.MumjolandiaCommunicator
import java.util.*
import kotlin.collections.ArrayList

class PlannerBackgroundTask(
    ip: String,
    port: Int,
    private val adapter: PlannerRecyclerViewAdapter?,
    private val context: Context?,
) : MumjolandiaCommunicator(ip, port) {
    public override fun onPostExecute(result: String) {
        val textView2: TextView = (context as Activity).findViewById(R.id.textViewPlannerStatus)
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
                textView2.text = returnStatus
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
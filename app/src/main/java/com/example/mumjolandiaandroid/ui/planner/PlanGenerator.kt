package com.example.mumjolandiaandroid.ui.planner

import java.util.*
import kotlin.collections.ArrayList

class PlanGenerator {
    fun generate(tasksToAdd: ArrayList<PlannerTask>): ArrayList<PlannerTask>{
        val returnArray = ArrayList<PlannerTask>()
        for (task in tasksToAdd){
            returnArray.add(task)
        }
        val minHour = 0
        val maxHour = 23
        for (time in minHour..maxHour){
            var timeString = "$time:00"
            if (timeString.length < 5){
                timeString = "0$timeString"
            }

            var okToAdd = true
            for (task in tasksToAdd){
                if (task.time.substring(0,2) == timeString.substring(0,2)){
                    okToAdd = false
                    break
                }
            }
            if (okToAdd){
                val emptyTask = PlannerTask(
                        timeString,
                        60,
                        "",
                )
                returnArray.add(emptyTask)
            }
        }
        Collections.sort(returnArray, PlannerTaskComparator())
        return  returnArray
    }

}
package com.example.mumjolandiaandroid.ui.planner

class PlanGenerator {
    fun generate(tasksToAdd: ArrayList<PlannerTask>): ArrayList<PlannerTask>{
        val returnArray = ArrayList<PlannerTask>()
        val minHour = 0
        val maxHour = 23
        for (time in minHour..maxHour){
            var timeString = "$time:00"
            if (timeString.length < 5){
                timeString = "0$timeString"
            }
            returnArray.add(PlannerTask(
                    timeString,
                    60,
                    "",
            ))
        }
        for (task in tasksToAdd){
            val timePrefix = (task.time.substring(0,2)).toInt()
            returnArray[timePrefix] = task
        }
        return  returnArray
    }

}
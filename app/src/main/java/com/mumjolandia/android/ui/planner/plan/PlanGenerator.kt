package com.mumjolandia.android.ui.planner.plan

import com.mumjolandia.android.utils.DateHelper
import java.util.*
import kotlin.collections.ArrayList

class PlanGenerator {
    fun generate(tasksToAdd: ArrayList<PlannerTask>): ArrayList<PlannerTask>{
        val returnArray = ArrayList<PlannerTask>()
        val minHour = 7
        val maxHour = 20
        for (time in minHour..maxHour){
            var timeString = "$time:00"
            if (timeString.length < 5){
                timeString = "0$timeString"
            }
            val emptyTask = PlannerTask(
                    timeString,
                    60,
                    "",
            )
            returnArray.add(emptyTask)
        }
        for (new_task in tasksToAdd){
            val newTaskEnd = getTaskEnd(new_task)
            // Add task begin
            for (existing_task in returnArray){
                if (existing_task.time == new_task.time){
                    returnArray.remove(existing_task)
                    break
                }
            }
            returnArray.add(new_task)
            // Add task end
            var taskEndAlreadyExist = false
            for (existing_task in returnArray){
                if (existing_task.time == newTaskEnd.time){
                    taskEndAlreadyExist = true
                    break
                }
            }
            if (!taskEndAlreadyExist){
                returnArray.add(newTaskEnd)
            }
            // Remove tasks between start and end
            val taskToRemove = ArrayList<PlannerTask>()
            for (existingTask in returnArray){
                if (taskInRange(existingTask, new_task)){
                    taskToRemove.add(existingTask)
                }
            }
            returnArray.removeAll(taskToRemove)

        }
        Collections.sort(returnArray, PlannerTaskComparator())
        return  returnArray
    }

    private fun getTaskEnd(task: PlannerTask): PlannerTask {
        return PlannerTask(DateHelper().getStringFromDate(DateHelper().getDateFromString(task.time, task.duration)!!), 0, "")
    }

    private fun taskInRange(task1: PlannerTask, task2: PlannerTask): Boolean{
        val date1 = DateHelper().getDateFromString(task1.time)
        val date2Begin = DateHelper().getDateFromString(task2.time)
        val date2End = DateHelper().getDateFromString(task2.time, task2.duration)
        if (date1?.after(date2Begin)!! and date2End?.after(date1)!!){
            return true
        }
        return false
    }
}
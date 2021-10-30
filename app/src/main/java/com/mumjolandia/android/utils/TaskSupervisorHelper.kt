package com.mumjolandia.android.utils

class TaskSupervisorHelper {
    companion object{
        // example string:
        // -541\t[--](08 Jul) example

        fun getTaskDoneFromTaskString(taskString: String) : Boolean{
            // possible statuses are: {-- ++ p+ p- e+ e-} -> checking char right before ']'
            if (taskString[taskString.indexOf(']') - 1] == '-'){
                return false
            }
            return true
        }

        fun getTaskIdFromTaskString(taskString: String) : Int{
            return taskString.subSequence(0, taskString.indexOf("\t")).toString().toInt()
        }

        fun getTaskNameFromTaskString(taskString: String) : String{
            return taskString.subSequence(taskString.indexOf(")") + 2, taskString.length).toString()
        }

        fun getTaskDateStringFromTaskString(taskString: String): String?{
            if (taskString[taskString.indexOf("(") + 1] == '-'){
                return null
            }
            return taskString.subSequence( taskString.indexOf("(") + 1, taskString.indexOf(")")).toString()
        }

        fun getTaskTypeFromTaskString(taskString: String): TaskType{
            return when (taskString[taskString.indexOf('[') + 1]){
                'p' -> TaskType.PERIODIC
                'e' -> TaskType.EVENT
                '+' -> TaskType.TASK
                '-' -> TaskType.TASK
                else -> TaskType.INVALID
            }
        }
    }
}
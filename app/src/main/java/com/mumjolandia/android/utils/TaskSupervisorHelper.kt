package com.mumjolandia.android.utils

class TaskSupervisorHelper {
    companion object{
        fun getTaskDoneFromTaskString(taskString: String) : Boolean{
            // example string:
            // 541\t[--](08 Jul) example
            // possible statuses are: {-- ++ p+ p- e+ e-} -> checking char right before ']'
            if (taskString[taskString.indexOf(']') - 1] == '-'){
                return false
            }
            return true
        }

        fun getTaskIdFromTaskString(taskString: String) : Int{
            // example string:
            // -541\t[--](08 Jul) example
            return taskString.subSequence(0, taskString.indexOf("\t")).toString().toInt()
        }

        fun getTaskNameFromTaskString(taskString: String) : String{
            // example string:
            // -541\t[--](08 Jul) example
            return taskString.subSequence(taskString.indexOf(")") + 1, taskString.length).toString()
        }

        fun getTaskDateStringFromTaskString(taskString: String): String?{
            if (taskString[taskString.indexOf("(") + 1] == '-'){
                return null
            }
            return taskString.subSequence( taskString.indexOf("(") + 1, taskString.indexOf(")") -1).toString()
        }
    }
}
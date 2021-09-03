package com.mumjolandia.android

import com.mumjolandia.android.utils.TaskSupervisorHelper.Companion.getTaskDoneFromTaskString
import com.mumjolandia.android.utils.TaskSupervisorHelper.Companion.getTaskIdFromTaskString
import org.junit.Assert
import org.junit.Test

class TaskSupervisorHelperTest {
    @Test
    fun taskDoneFromStringUndone(){
        val testString = "541\t[--](08 Jul) example"
        Assert.assertFalse(getTaskDoneFromTaskString(testString))
    }

    @Test
    fun taskDoneFromStringDone(){
        val testString = "5413\t[++](08 Jul) example"
        Assert.assertTrue(getTaskDoneFromTaskString(testString))
    }

    @Test
    fun taskDoneFromStringEventDone(){
        val testString = "1\t[e+](08 Jul) example"
        Assert.assertTrue(getTaskDoneFromTaskString(testString))
    }

    @Test
    fun taskDoneFromStringPeriodicUndone(): Unit{
        val testString = "10\t[p-](08 Jul) example"
        Assert.assertFalse(getTaskDoneFromTaskString(testString))
    }

    @Test
    fun taskIdFromString10(){
        val testString = "10\t[p-](08 Jul) example"
        Assert.assertEquals(10, getTaskIdFromTaskString(testString).toLong())
    }

    @Test
    fun taskIdFromString0(){
        val testString = "0\t[p-](08 Jul) example"
        Assert.assertEquals(0, getTaskIdFromTaskString(testString).toLong())
    }

    @Test
    fun taskIdFromStringMinus15(){
        val testString = "-15\t[p-](08 Jul) example"
        Assert.assertEquals(-15, getTaskIdFromTaskString(testString).toLong())
    }

    @Test
    fun taskIdFromString351(){
        val testString = "351\t[p-](08 Jul) example"
        Assert.assertEquals(351, getTaskIdFromTaskString(testString).toLong())
    }
}
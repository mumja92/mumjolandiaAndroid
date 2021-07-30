package com.example.mumjolandiaandroid;

import com.example.mumjolandiaandroid.utils.TaskSupervisorHelper;

import org.junit.Test;

import static org.junit.Assert.*;


public class TaskSupervisorHelperTest {
    @Test
    public void getTaskDoneFromStringUndone() {
        String testString = "541\t[--](08 Jul) example";
        assertFalse(TaskSupervisorHelper.Companion.getTaskDoneFromTaskString(testString));
    }

    @Test
    public void getTaskDoneFromStringDone() {
        String testString = "5413\t[++](08 Jul) example";
        assertTrue(TaskSupervisorHelper.Companion.getTaskDoneFromTaskString(testString));
    }

    @Test
    public void getTaskDoneFromStringEventDone() {
        String testString = "1\t[e+](08 Jul) example";
        assertTrue(TaskSupervisorHelper.Companion.getTaskDoneFromTaskString(testString));
    }

    @Test
    public void getTaskDoneFromStringPeriodicUndone() {
        String testString = "10\t[p-](08 Jul) example";
        assertFalse(TaskSupervisorHelper.Companion.getTaskDoneFromTaskString(testString));
    }

    @Test
    public void getTaskIdFromString10() {
        String testString = "10\t[p-](08 Jul) example";
        assertEquals(10, TaskSupervisorHelper.Companion.getTaskIdFromTaskString(testString));
    }

    @Test
    public void getTaskIdFromString0() {
        String testString = "0\t[p-](08 Jul) example";
        assertEquals(0, TaskSupervisorHelper.Companion.getTaskIdFromTaskString(testString));
    }

    @Test
    public void getTaskIdFromStringMinus15() {
        String testString = "-15\t[p-](08 Jul) example";
        assertEquals(-15, TaskSupervisorHelper.Companion.getTaskIdFromTaskString(testString));
    }

    @Test
    public void getTaskIdFromString351() {
        String testString = "351\t[p-](08 Jul) example";
        assertEquals(351, TaskSupervisorHelper.Companion.getTaskIdFromTaskString(testString));
    }
}
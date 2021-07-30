package com.example.mumjolandiaandroid.ui.planner

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mumjolandiaandroid.R
import com.example.mumjolandiaandroid.ui.planner.plan.PlannerBackgroundTask
import com.example.mumjolandiaandroid.ui.planner.plan.PlannerRecyclerViewAdapter
import com.example.mumjolandiaandroid.ui.planner.task.TaskBackgroundTask
import com.example.mumjolandiaandroid.ui.planner.task.TaskRecyclerViewAdapter
import com.example.mumjolandiaandroid.utils.Helpers
import com.example.mumjolandiaandroid.utils.TaskSupervisorHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_planner.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PlannerFragment : Fragment(),
    PlannerRecyclerViewAdapter.ItemClickListener,
    TaskRecyclerViewAdapter.ItemClickListener
{
    private var viewChosenDay: TextView? = null
    private var plannerRecyclerView: RecyclerView? = null
    private var plannerRecyclerViewAdapter: PlannerRecyclerViewAdapter? = null
    private var taskRecyclerView: RecyclerView? = null
    private var taskRecyclerViewAdapter: TaskRecyclerViewAdapter? = null
    private var buttonPreviousDay: Button? = null
    private var buttonNextDay: Button? = null
    private var chosenDay: Int = 0
    private var dialogPlanner: AlertDialog.Builder? = null
    private var mumjolandiaIp = "127.0.0.1"
    private var mumjolandiaPort = 3335
    private var swapTaskAndPlanner = false
    private var root: View? = null
    private var fabAddTask: FloatingActionButton? = null
    private var fabSwapTasksMode: FloatingActionButton? = null
    private var flagTaskAllTaskMode = false

    init {
        if (Helpers.isEmulator()) {
            mumjolandiaIp = "10.0.2.2"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_planner, container, false)

        viewChosenDay = root?.findViewById(R.id.textViewPlannerChosenDay)
        viewChosenDay?.text=chosenDay.toString()
        viewChosenDay?.setOnClickListener {
            changeChosenDay(null)
        }

        buttonPreviousDay = root?.findViewById(R.id.buttonPlannerPreviousDay)
        buttonPreviousDay?.setOnClickListener {
            changeChosenDay(-1)
        }
        buttonNextDay = root?.findViewById(R.id.buttonPlannerNextDay)
        buttonNextDay?.setOnClickListener {
            changeChosenDay(1)
        }

        plannerRecyclerView = root?.findViewById(R.id.recyclerViewPlannerTasks)
        plannerRecyclerView?.layoutManager = LinearLayoutManager(activity)
        plannerRecyclerViewAdapter = PlannerRecyclerViewAdapter(activity, ArrayList())
        plannerRecyclerViewAdapter?.setClickListener(this)
        plannerRecyclerView?.adapter = plannerRecyclerViewAdapter

        taskRecyclerView = root?.findViewById(R.id.recyclerViewNormalTasks)
        taskRecyclerView?.layoutManager = LinearLayoutManager(activity)
        taskRecyclerViewAdapter = TaskRecyclerViewAdapter(activity, ArrayList())
        taskRecyclerViewAdapter?.setClickListener(this)
        taskRecyclerView?.adapter = taskRecyclerViewAdapter

        swapTaskAndPlanner()
        changeChosenDay(0)

        val fabSwapMode: FloatingActionButton = root!!.findViewById(R.id.floating_button_planner_swap_mode)
        fabSwapMode.setOnClickListener {
            swapTaskAndPlanner()
            changeChosenDay(chosenDay)
        }

        fabAddTask = root!!.findViewById(R.id.floating_button_planner_task_add)
        fabAddTask?.setOnClickListener {
            showDialogTaskAdd()
        }
        fabAddTask?.visibility=View.INVISIBLE

        fabSwapTasksMode = root!!.findViewById(R.id.floating_button_planner_task_mode)
        fabSwapTasksMode?.setOnClickListener {
            swapTaskAllTaskMode()
        }
        fabSwapTasksMode?.visibility=View.INVISIBLE

        return root
    }

    override fun onResume() {
        super.onResume()
        changeChosenDay(0)
    }

    override fun onItemClick(view: View?, position: Int) {
        if (swapTaskAndPlanner){
            showDialogPlanGetTaskData(position)
        }
        else{
            val taskString = taskRecyclerViewAdapter?.getItem(position)
            val taskDone = TaskSupervisorHelper.getTaskDoneFromTaskString(taskString.toString())
            val taskId = TaskSupervisorHelper.getTaskIdFromTaskString(taskString.toString())
            val command = if (taskDone){
                "task undone $taskId"
            }
            else{
                "task done $taskId"
            }
            sendTaskCommand(command, refreshTasks = true)
        }
    }

    override fun onItemLongClick(view: View?, position: Int) {
        if (swapTaskAndPlanner){
            // plan adapter does not have onLongClickListener implemented, so this won't show
            Snackbar.make(requireView(), "Plan hold", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }
        else {
            showDialogTaskSet(position)
        }
    }

    private fun sendTaskCommand(command: String?, refreshTasks: Boolean = true){
        if (command != null){
            TaskBackgroundTask(mumjolandiaIp, mumjolandiaPort, taskRecyclerViewAdapter, context, root!!).execute(command)
        }
        if (refreshTasks){
            if (flagTaskAllTaskMode){
                TaskBackgroundTask(mumjolandiaIp, mumjolandiaPort, taskRecyclerViewAdapter, context, root!!, true).execute("task ls x")
            }
            else{
                TaskBackgroundTask(mumjolandiaIp, mumjolandiaPort, taskRecyclerViewAdapter, context, root!!).execute("task ls")
            }
        }
    }

    private fun sendPlanCommand(command: String, refreshTasks: Boolean = true){
        PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, plannerRecyclerViewAdapter, context, root!!).execute(command)
        if (refreshTasks){
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, plannerRecyclerViewAdapter, context, root!!).execute("planner get $chosenDay")
        }
    }

    private fun swapTaskAndPlanner(){
        if (swapTaskAndPlanner){
            taskRecyclerView?.visibility = View.VISIBLE
            plannerRecyclerView?.visibility = View.INVISIBLE
            fabAddTask?.visibility=View.VISIBLE
            fabSwapTasksMode?.visibility=View.VISIBLE
        }
        else{
            taskRecyclerView?.visibility = View.INVISIBLE
            plannerRecyclerView?.visibility = View.VISIBLE
            fabAddTask?.visibility=View.INVISIBLE
            fabSwapTasksMode?.visibility=View.INVISIBLE
        }
        swapTaskAndPlanner = !swapTaskAndPlanner

    }

    private fun swapTaskAllTaskMode(){
        flagTaskAllTaskMode = !flagTaskAllTaskMode
        sendTaskCommand(null, true)
    }
    private fun changeChosenDay(dayShift: Int?){
        if (dayShift != null) {
            chosenDay += dayShift
        }
        else {
            chosenDay = 0
        }
        try{
            textViewPlannerChosenDay.text = translateShiftDayToText(chosenDay)
        }
        catch (ex: NullPointerException){
        }
        if (swapTaskAndPlanner){
            changeChosenDayPlanner()
        }
        else{
            changeChosenDayTask()
        }
    }

    private fun changeChosenDayPlanner(){
        val command = "planner get $chosenDay"
        sendPlanCommand(command, true)
    }

    private fun changeChosenDayTask(){
        var command = "task ls $chosenDay"
        if (chosenDay == 0){
            command = "task ls"
        }
        sendTaskCommand(command, true)
    }

    private fun initAlertPlanGetTaskData(index: Int){
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        dialogPlanner = AlertDialog.Builder(context)
        dialogPlanner?.setTitle("Enter task name:")
        val description = EditText(context)
        description.hint = "Description"
        val duration = EditText(context)
        duration.hint = "Duration (minutes)"
        duration.setText("60")
        val time = EditText(context)
        time.hint = "HH:MM"
        time.setText(plannerRecyclerViewAdapter?.getItem(index)?.time)


        layout.addView(description)
        layout.addView(duration)
        layout.addView(time)

        dialogPlanner?.setView(layout)
        dialogPlanner?.setPositiveButton("Add") { dialog, _ ->
            val command = "planner add " + chosenDay.toString() + " " + time.text.toString() + " " + duration.text.toString() + " '" + description.text.toString() + "'"
            sendPlanCommand(command, true)
            dialog.dismiss()
        }
        dialogPlanner?.setNegativeButton("Delete") { dialog, _ ->
            val command = "planner remove " + chosenDay.toString() + " " + plannerRecyclerViewAdapter?.getItem(index)?.time
            sendPlanCommand(command, true)
            dialog.dismiss()
        }
    }

    private fun initAlertTaskSet(index: Int, enableForToday: Boolean){
        dialogPlanner = AlertDialog.Builder(context)
        if (enableForToday){
            dialogPlanner?.setTitle("Enable task?")
        }
        else{
            dialogPlanner?.setTitle("Hide task?")
        }

        dialogPlanner?.setPositiveButton("Yes") { dialog, _ ->
            val taskString = taskRecyclerViewAdapter?.getItem(index)
            val taskId = TaskSupervisorHelper.getTaskIdFromTaskString(taskString.toString())
            val command = if (enableForToday){
                "task set $taskId 0"
            }
            else{
                "task set $taskId none"
            }
            sendTaskCommand(command, true)
            dialog.dismiss()
        }
        dialogPlanner?.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
    }

    private fun initAlertTaskAdd(){
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val taskName = EditText(context)
        taskName.hint = "name"

        layout.addView(taskName)

        dialogPlanner = AlertDialog.Builder(context)
        dialogPlanner?.setTitle("Enter new task name:")

        dialogPlanner?.setView(layout)
        dialogPlanner?.setPositiveButton("Submit") { dialog, _ ->
            val newTaskName = taskName.text.toString()
            val command = "task add $newTaskName"
            sendTaskCommand(command, true)
            dialog.dismiss()
        }
        dialogPlanner?.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
    }

    private fun showDialogPlanGetTaskData(index: Int){
        initAlertPlanGetTaskData(index)
        dialogPlanner?.show()
    }

    private fun showDialogTaskSet(index: Int){
        if (flagTaskAllTaskMode){
            initAlertTaskSet(index, true)
        }
        else{
            initAlertTaskSet(index, false)
        }
        dialogPlanner?.show()
    }

    private fun showDialogTaskAdd(){
        initAlertTaskAdd()
        dialogPlanner?.show()
    }

    private fun translateShiftDayToText(shift_day: Int): String{
        return when (shift_day) {
            -1 -> {
                "Yesterday"
            }
            0 -> {
                "Today"
            }
            1 -> {
                "Tomorrow"
            }
            else -> {
                val c = Calendar.getInstance()
                c.time = Date()
                c.add(Calendar.DATE, shift_day)
                return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(c.time) + " (" + SimpleDateFormat("EEEE", Locale.getDefault()).format(c.time) + ")"
            }
        }
    }
}
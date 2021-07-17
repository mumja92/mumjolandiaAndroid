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
    private var dialogAddPlannerTask: AlertDialog.Builder? = null
    private var mumjolandiaIp = "127.0.0.1"
    private var mumjolandiaPort = 3335
    private var swapTaskAndPlanner = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_planner, container, false)

        if (Helpers.isEmulator()) {
            mumjolandiaIp = "10.0.2.2"
        }

        viewChosenDay = root.findViewById(R.id.textViewPlannerChosenDay)
        viewChosenDay?.text=chosenDay.toString()
        viewChosenDay?.setOnClickListener {
            changeChosenDay(null)
        }

        buttonPreviousDay = root.findViewById(R.id.buttonPlannerPreviousDay)
        buttonPreviousDay?.setOnClickListener {
            changeChosenDay(-1)
        }
        buttonNextDay = root.findViewById(R.id.buttonPlannerNextDay)
        buttonNextDay?.setOnClickListener {
            changeChosenDay(1)
        }

        plannerRecyclerView = root.findViewById(R.id.recyclerViewPlannerTasks)
        plannerRecyclerView?.layoutManager = LinearLayoutManager(activity)
        plannerRecyclerViewAdapter = PlannerRecyclerViewAdapter(activity, ArrayList())
        plannerRecyclerViewAdapter?.setClickListener(this)
        plannerRecyclerView?.adapter = plannerRecyclerViewAdapter

        taskRecyclerView = root.findViewById(R.id.recyclerViewNormalTasks)
        taskRecyclerView?.layoutManager = LinearLayoutManager(activity)
        taskRecyclerViewAdapter = TaskRecyclerViewAdapter(activity, ArrayList())
        taskRecyclerViewAdapter?.setClickListener(this)
        taskRecyclerView?.adapter = taskRecyclerViewAdapter

        swapTaskAndPlanner()
        changeChosenDay(0)

        val fab: FloatingActionButton = root.findViewById(R.id.planner_floating_button)
        fab.setOnClickListener { view ->
            swapTaskAndPlanner()
            changeChosenDay(chosenDay)
            Snackbar.make(view, "Planner fragment", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        changeChosenDay(0)
    }

    override fun onItemClick(view: View?, position: Int) {
        showDialogGetTaskName(position)
    }

    private fun swapTaskAndPlanner(){
        if (swapTaskAndPlanner){
            taskRecyclerView?.visibility = View.VISIBLE
            plannerRecyclerView?.visibility = View.INVISIBLE
        }
        else{
            taskRecyclerView?.visibility = View.INVISIBLE
            plannerRecyclerView?.visibility = View.VISIBLE
        }
        swapTaskAndPlanner = !swapTaskAndPlanner

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
        PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, plannerRecyclerViewAdapter, context).execute(command)
    }

    private fun changeChosenDayTask(){
        var command = "task ls $chosenDay"
        if (chosenDay == 0){
            command = "task ls"
        }
        TaskBackgroundTask(mumjolandiaIp, mumjolandiaPort, taskRecyclerViewAdapter, context).execute(command)
    }

    private fun initAlert(index: Int){
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        dialogAddPlannerTask = AlertDialog.Builder(context)
        dialogAddPlannerTask?.setTitle("Enter task name:")
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

        dialogAddPlannerTask?.setView(layout)
        dialogAddPlannerTask?.setPositiveButton("Add") { dialog, _ ->
            val command = "planner add " + chosenDay.toString() + " " + time.text.toString() + " " + duration.text.toString() + " '" + description.text.toString() + "'"
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, plannerRecyclerViewAdapter, context).execute(command)
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, plannerRecyclerViewAdapter, context).execute("planner get $chosenDay")
            dialog.dismiss()
        }
        dialogAddPlannerTask?.setNegativeButton("Delete") { dialog, _ ->
            val command = "planner remove " + chosenDay.toString() + " " + plannerRecyclerViewAdapter?.getItem(index)?.time
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, plannerRecyclerViewAdapter, context).execute(command)
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, plannerRecyclerViewAdapter, context).execute("planner get $chosenDay")
            dialog.dismiss()
        }
    }

    private fun showDialogGetTaskName(index: Int){
        initAlert(index)
        dialogAddPlannerTask?.show()
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
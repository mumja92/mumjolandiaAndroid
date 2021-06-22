package com.example.mumjolandiaandroid.ui.planner

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mumjolandiaandroid.R
import com.example.mumjolandiaandroid.utils.Helpers
import com.example.mumjolandiaandroid.utils.MumjolandiaCommunicator
import kotlinx.android.synthetic.main.fragment_planner.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PlannerFragment : Fragment(), PlannerRecyclerViewAdapter.ItemClickListener {
    private var viewChosenDay: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerViewAdapter: PlannerRecyclerViewAdapter? = null
    private var buttonPreviousDay: Button? = null
    private var buttonNextDay: Button? = null
    private var chosenDay: Int = 0
    private var dialogAddPlannerTask: AlertDialog.Builder? = null
    private var mumjolandiaIp = "127.0.0.1"
    private var mumjolandiaPort = 3335

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

        recyclerView = root.findViewById(R.id.recyclerViewPlannerTasks)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerViewAdapter = PlannerRecyclerViewAdapter(activity, ArrayList())
        recyclerViewAdapter?.setClickListener(this)
        recyclerView?.adapter = recyclerViewAdapter

        changeChosenDay(0)
        return root
    }

    override fun onResume() {
        super.onResume()
        changeChosenDay(0)
    }

    override fun onItemClick(view: View?, position: Int) {
        showDialogGetTaskName(position)
    }

    private fun changeChosenDay(dayShift: Int?){
        if (dayShift != null) {
            chosenDay += dayShift
        }
        else {
            chosenDay = 0
        }
        val command = "planner get $chosenDay"
        try{
            textViewPlannerChosenDay.text = translateShiftDayToText(chosenDay)
        }
        catch (ex: NullPointerException){
        }
        PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, recyclerViewAdapter, context).execute(command)
    }

    private class PlannerBackgroundTask(
            ip: String,
            port: Int,
            private val adapter: PlannerRecyclerViewAdapter?,
            private val context: Context?,
    ) : MumjolandiaCommunicator(ip, port) {
        public override fun onPostExecute(result: String) {
            val textView2: TextView = (context as Activity).findViewById(R.id.textViewPlannerStatus)
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

        private fun getNewPlannerTaskArray(separatedList: List<String>): ArrayList<PlannerTask>{
            val receivedTasks = ArrayList<PlannerTask>()
            var loopIndex = 5
            while (loopIndex < separatedList.size){
                receivedTasks.add(
                        PlannerTask(
                                separatedList[loopIndex - 2],
                                separatedList[loopIndex - 1].toInt(),
                                separatedList[loopIndex]))
                loopIndex += 3
            }
            return PlanGenerator().generate(receivedTasks)
        }
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
        time.setText(recyclerViewAdapter?.getItem(index)?.time)


        layout.addView(description)
        layout.addView(duration)
        layout.addView(time)

        dialogAddPlannerTask?.setView(layout)
        dialogAddPlannerTask?.setPositiveButton("Add") { dialog, _ ->
            val command = "planner add " + chosenDay.toString() + " " + time.text.toString() + " " + duration.text.toString() + " '" + description.text.toString() + "'"
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, recyclerViewAdapter, context).execute(command)
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, recyclerViewAdapter, context).execute("planner get $chosenDay")
            dialog.dismiss()
        }
        dialogAddPlannerTask?.setNegativeButton("Delete") { dialog, _ ->
            val command = "planner remove " + chosenDay.toString() + " " + recyclerViewAdapter?.getItem(index)?.time
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, recyclerViewAdapter, context).execute(command)
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, recyclerViewAdapter, context).execute("planner get $chosenDay")
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
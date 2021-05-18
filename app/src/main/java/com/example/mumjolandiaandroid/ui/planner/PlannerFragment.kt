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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mumjolandiaandroid.R
import com.example.mumjolandiaandroid.utils.Helpers
import com.example.mumjolandiaandroid.utils.MumjolandiaCommunicator
import kotlinx.android.synthetic.main.fragment_planner.*


class PlannerFragment : Fragment(), PlannerRecyclerViewAdapter.ItemClickListener {
    private var viewChosenDay: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var recyclerViewAdapter: PlannerRecyclerViewAdapter? = null
    private var buttonPreviousDay: Button? = null
    private var buttonNextDay: Button? = null
    private var chosenDay: Int = 0
    private var alertChooseTaskName: AlertDialog.Builder? = null
    private var userChosenItemPosition = 0
    private var mumjolandiaIp = "127.0.0.1"
    private var mumjolandiaPort = 3335

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_planner, container, false)

        initAlert()

        initStuff()

        viewChosenDay = root.findViewById(R.id.textViewPlannerChosenDay)
        viewChosenDay?.text=chosenDay.toString()

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

    override fun onItemClick(view: View?, position: Int) {
        userChosenItemPosition = position
        showDialogGetTaskName()
    }

    private fun changeChosenDay(dayShift: Int){
        chosenDay += dayShift
        val command = "planner get $chosenDay"
        try{
            textViewPlannerChosenDay.text = chosenDay.toString()
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
            textView2.text = mumjolandiaReturnValue
            when (mumjolandiaReturnValue) {
                "MumjolandiaReturnValue.planner_get_ok" -> {
                    adapter?.reset(getNewPlannerTaskArray(separatedList))
                }
                "MumjolandiaReturnValue.planner_add_ok" -> {

                }
                else -> {
                    Toast.makeText(
                            context,
                            "Unhandled mumjolandia return value",
                            Toast.LENGTH_SHORT).show()
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
                                60,
                                separatedList[loopIndex]))
                loopIndex += 3
            }
            return PlanGenerator().generate(receivedTasks)
        }
    }

    private fun initAlert(){
        alertChooseTaskName = AlertDialog.Builder(context)
        alertChooseTaskName?.setTitle("Enter task name:")
        val input = EditText(context)
        alertChooseTaskName?.setView(input)
        alertChooseTaskName?.setPositiveButton("Accept") { dialog, _ ->
            val command = "planner add " + chosenDay.toString() + " " + recyclerViewAdapter?.getItem(userChosenItemPosition)?.time + " " + recyclerViewAdapter?.getItem(userChosenItemPosition)?.duration + " '" + input.text.toString() + "'"
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, recyclerViewAdapter, context).execute(command)
            PlannerBackgroundTask(mumjolandiaIp, mumjolandiaPort, recyclerViewAdapter, context).execute("planner get $chosenDay")
            dialog.dismiss()
        }
        alertChooseTaskName?.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
    }

    private fun showDialogGetTaskName(){
        initAlert()
        alertChooseTaskName?.show()
    }
    private fun initStuff(){
        if (Helpers.isEmulator()) {
            mumjolandiaIp = "10.0.2.2"
        }
    }
}
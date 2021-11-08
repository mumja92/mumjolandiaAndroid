package com.mumjolandia.android.ui.rootfs

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_rootfs.*

class RootFSFragment : Fragment(),
    RootFSRecyclerViewAdapter.ItemClickListener
{
    private var root: View? = null

    private var localMumjolandiaIp = "127.0.0.1"
    private var localMumjolandiaPort = 3335

    private var rootFSMode = RootFSMode.LOCAL

    private var textViewCwd: TextView? = null
    private var textViewMode: TextView? = null
    private var rootFSRecyclerView: RecyclerView? = null
    private var rootFSRecyclerViewAdapter: RootFSRecyclerViewAdapter? = null
    private var switchRootFSMode: SwitchCompat? = null
    private var buttonExitRemoteServer: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_rootfs, container, false)

        textViewMode = root?.findViewById(R.id.textViewRootFSMode)

        textViewCwd = root?.findViewById(R.id.textViewRootFSCwd)
        textViewCwd!!.setTextColor(Color.rgb(80,80,80))
        textViewCwd!!.setOnClickListener {
            when (rootFSMode){
                RootFSMode.LOCAL -> {
                    executeCommand("rootfs cd")
                    executeCommand("rootfs ls")
                }
                RootFSMode.REMOTE -> {
                    executeCommand("ssh send cd")
                    executeCommand("ssh send ls")
                }
            }
            // If path was broken (red) now it is fixed
            textViewCwd!!.setTextColor(Color.rgb(80,80,80))
        }

        rootFSRecyclerView = root?.findViewById(R.id.recyclerViewRootFS)
        rootFSRecyclerView?.layoutManager = LinearLayoutManager(activity)
        rootFSRecyclerViewAdapter = RootFSRecyclerViewAdapter(activity, ArrayList())
        rootFSRecyclerViewAdapter?.setClickListener(this)
        rootFSRecyclerView?.adapter = rootFSRecyclerViewAdapter

        switchRootFSMode = root?.findViewById(R.id.switchCompatRootFSChangeMode)
        switchRootFSMode?.setOnCheckedChangeListener { _, b ->
            if (b){
                swapRootFSMode(RootFSMode.REMOTE)
            }
            else{
                swapRootFSMode(RootFSMode.LOCAL)
            }
        }

        buttonExitRemoteServer = root?.findViewById(R.id.buttonRootFSExitServer)
        buttonExitRemoteServer?.setOnClickListener{
            executeCommand("ssh send exit")
        }
        swapRootFSMode(RootFSMode.LOCAL)

        return root
    }

    override fun onItemClick(view: View?, position: Int) {
        val file = rootFSRecyclerViewAdapter?.getItem(position)
        when (rootFSMode){
            RootFSMode.LOCAL -> {
                executeCommand("rootfs cd " + file?.subSequence(1, file.length))
                executeCommand("rootfs ls")
            }
            RootFSMode.REMOTE -> {
                executeCommand("ssh send cd " + file?.subSequence(1, file.length))
                executeCommand("ssh send ls")
            }
        }
    }

    override fun onItemLongClick(view: View?, position: Int) {
    }

    private fun executeCommand(command: String){
        RootFSBackgroundTask(
            localMumjolandiaIp,
            localMumjolandiaPort,
            rootFSRecyclerViewAdapter,
            textViewCwd,
            root!!,
        ).execute(command)
    }

    private fun swapRootFSMode(mode: RootFSMode){
        when (mode){
            RootFSMode.LOCAL -> {
                rootFSMode = RootFSMode.LOCAL
                textViewMode?.text = "Local"
                executeCommand("rootfs pwd")
                executeCommand("rootfs ls")
            }
            RootFSMode.REMOTE -> {
                rootFSMode = RootFSMode.REMOTE
                textViewMode?.text = "Remote"
                executeCommand("ssh send pwd")
                executeCommand("ssh send ls")
            }
        }
    }
}
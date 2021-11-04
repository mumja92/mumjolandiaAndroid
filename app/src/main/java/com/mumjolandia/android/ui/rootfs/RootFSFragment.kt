package com.mumjolandia.android.ui.rootfs

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.R
import com.mumjolandia.android.utils.Helpers

class RootFSFragment : Fragment(),
    RootFSRecyclerViewAdapter.ItemClickListener
{
    private var root: View? = null

    private var mumjolandiaIp = "127.0.0.1"
    private var mumjolandiaPort = 3335

    private var textViewCwd: TextView? = null
    private var rootFSRecyclerView: RecyclerView? = null
    private var rootFSRecyclerViewAdapter: RootFSRecyclerViewAdapter? = null

    init {
        if (Helpers.isEmulator()) {
            mumjolandiaIp = "10.0.2.2"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_rootfs, container, false)

        textViewCwd = root?.findViewById(R.id.textViewRootFSCwd)
        textViewCwd!!.setTextColor(Color.rgb(80,80,80))
        textViewCwd!!.setOnClickListener {
            executeCommand("rootfs cd")
            executeCommand("rootfs ls")
            // If path was broken (red) now it is fixed
            textViewCwd!!.setTextColor(Color.rgb(80,80,80))
        }

        rootFSRecyclerView = root?.findViewById(R.id.recyclerViewRootFS)
        rootFSRecyclerView?.layoutManager = LinearLayoutManager(activity)
        rootFSRecyclerViewAdapter = RootFSRecyclerViewAdapter(activity, ArrayList())
        rootFSRecyclerViewAdapter?.setClickListener(this)
        rootFSRecyclerView?.adapter = rootFSRecyclerViewAdapter

        executeCommand("rootfs pwd")
        executeCommand("rootfs ls")

        return root
    }

    private fun executeCommand(command: String){
        RootFSBackgroundTask(
            mumjolandiaIp,
            mumjolandiaPort,
            rootFSRecyclerViewAdapter,
            textViewCwd,
            root!!,
        ).execute(command)
    }

    override fun onItemClick(view: View?, position: Int) {
        val file = rootFSRecyclerViewAdapter?.getItem(position)
        executeCommand("rootfs cd " + file?.subSequence(1, file.length))
        executeCommand("rootfs ls")
    }

    override fun onItemLongClick(view: View?, position: Int) {
    }
}
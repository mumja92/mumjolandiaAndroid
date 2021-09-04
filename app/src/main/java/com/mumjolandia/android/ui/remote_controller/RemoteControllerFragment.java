package com.mumjolandia.android.ui.remote_controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.R;
import com.mumjolandia.android.utils.MumjolandiaCommunicator;

public class RemoteControllerFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_remote_controller, container, false);

        TextView textViewServerIp = root.findViewById(R.id.textViewRemoteControllerServerIp);
        Button buttonPlayPause = root.findViewById(R.id.buttonPlayPause);
        Button buttonNext = root.findViewById(R.id.buttonNext);
        Button buttonPrevious = root.findViewById(R.id.buttonPrevious);
        Button buttonMute = root.findViewById(R.id.buttonMute);
        Button buttonVolUp = root.findViewById(R.id.buttonVolUp);
        Button buttonVolDown = root.findViewById(R.id.buttonVolDown);
        Button buttonExit = root.findViewById(R.id.buttonRemoteControllerExit);

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        String ip = prefs.getString(getString(R.string.mumjolandiaIpKey), "127.0.0.1");
        int port = prefs.getInt(getString(R.string.mumjolandiaPortKey), 3333);
        String serverText = ip + ":" + port;
        textViewServerIp.setText(serverText);
        buttonPlayPause.setOnClickListener(v -> sendCommand("play"));
        buttonNext.setOnClickListener(v -> sendCommand("next"));
        buttonPrevious.setOnClickListener(v -> sendCommand("prev"));
        buttonMute.setOnClickListener(v -> sendCommand("mute"));
        buttonVolUp.setOnClickListener(v -> sendCommand("vol+"));
        buttonVolDown.setOnClickListener(v -> sendCommand("vol-"));
        buttonExit.setOnClickListener(v -> sendCommand("exit"));
        return root;
    }

    private void sendCommand(String command){
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        String ip = prefs.getString(getString(R.string.mumjolandiaIpKey), "127.0.0.1");
        int port = prefs.getInt(getString(R.string.mumjolandiaPortKey), 3333);
        new MumjolandiaCommunicator(ip, port).execute(command);
    }
}

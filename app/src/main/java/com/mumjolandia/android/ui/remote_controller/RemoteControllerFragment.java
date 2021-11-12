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
    String ip = "127.0.0.1";
    int port = 3335;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_remote_controller, container, false);

        Button buttonPlayPause = root.findViewById(R.id.buttonPlayPause);
        Button buttonNext = root.findViewById(R.id.buttonNext);
        Button buttonPrevious = root.findViewById(R.id.buttonPrevious);
        Button buttonMute = root.findViewById(R.id.buttonMute);
        Button buttonVolUp = root.findViewById(R.id.buttonVolUp);
        Button buttonVolDown = root.findViewById(R.id.buttonVolDown);
        Button buttonExit = root.findViewById(R.id.buttonRemoteControllerExit);

        buttonPlayPause.setOnClickListener(v -> sendCommand("ssh send play"));
        buttonNext.setOnClickListener(v -> sendCommand("ssh send next"));
        buttonPrevious.setOnClickListener(v -> sendCommand("ssh send prev"));
        buttonMute.setOnClickListener(v -> sendCommand("ssh send mute"));
        buttonVolUp.setOnClickListener(v -> sendCommand("ssh send vol+"));
        buttonVolDown.setOnClickListener(v -> sendCommand("ssh send vol-"));
        buttonExit.setOnClickListener(v -> sendCommand("ssh send exit"));
        return root;
    }

    private void sendCommand(String command){
        new MumjolandiaCommunicator(ip, port).execute(command);
    }
}

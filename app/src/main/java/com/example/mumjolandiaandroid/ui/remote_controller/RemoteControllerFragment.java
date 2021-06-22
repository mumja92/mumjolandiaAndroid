package com.example.mumjolandiaandroid.ui.remote_controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mumjolandiaandroid.R;
import com.example.mumjolandiaandroid.utils.MumjolandiaCommunicator;

public class RemoteControllerFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_remote_controller, container, false);

        Button buttonPlayPause = root.findViewById(R.id.buttonPlayPause);
        Button buttonNext = root.findViewById(R.id.buttonNext);
        Button buttonPrevious = root.findViewById(R.id.buttonPrevious);
        Button buttonMute = root.findViewById(R.id.buttonMute);
        Button buttonVolUp = root.findViewById(R.id.buttonVolUp);
        Button buttonVolDown = root.findViewById(R.id.buttonVolDown);

        buttonPlayPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendCommand("play");
            }
        });
        buttonNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendCommand("next");
            }
        });
        buttonPrevious.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendCommand("prev");
            }
        });
        buttonMute.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendCommand("mute");
            }
        });
        buttonVolUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendCommand("vol+");
            }
        });
        buttonVolDown.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sendCommand("vol-");
            }
        });
        return root;
    }

    private void sendCommand(String command){
        new MumjolandiaCommunicator("10.0.2.2", 3333).execute(command);
    }
}

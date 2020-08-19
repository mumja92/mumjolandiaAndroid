package com.example.mumjolandiaandroid.ui.remote_controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mumjolandiaandroid.R;

public class RemoteControllerFragment extends Fragment {

    private RemoteControllerViewModel remoteControllerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        remoteControllerViewModel =
                ViewModelProviders.of(this).get(RemoteControllerViewModel.class);
        View root = inflater.inflate(R.layout.fragment_remote_controller, container, false);
        final TextView textView = root.findViewById(R.id.text_remote_controller);
        remoteControllerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}

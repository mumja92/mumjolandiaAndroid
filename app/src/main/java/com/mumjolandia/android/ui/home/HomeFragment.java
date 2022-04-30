package com.mumjolandia.android.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.R;
import com.mumjolandia.android.utils.Helpers;
import com.mumjolandia.android.utils.MumjolandiaCommunicator;
import com.mumjolandia.android.utils.MumjolandiaResponse;

public class HomeFragment extends Fragment {

    TextView viewResponse;
    EditText userCommand;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final Button buttonHomeSendCommand = root.findViewById(R.id.buttonHomeSendCommand);
        buttonHomeSendCommand.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String command = userCommand.getText().toString();
                String local_ip = "127.0.0.1";
                if (Helpers.isEmulator()){
                    local_ip = "10.0.2.2";
                }
                new HomeBackgroundTask(local_ip, 3335, HomeFragment.this.getActivity()).execute(command);
            }
        });

        viewResponse = root.findViewById(R.id.textViewHomeResponse);
        userCommand = root.findViewById(R.id.editTextHomeCommand);
        userCommand.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buttonHomeSendCommand.performClick();
                    return true;
                }
                return false;
            }
        });
        return root;
    }

    private class HomeBackgroundTask extends MumjolandiaCommunicator {
        private Context context;

        public HomeBackgroundTask(String ip, int port, Context context) {
            super(ip, port);
            this.context = context;
        }

        public void onPostExecute(MumjolandiaResponse result) {
            TextView textView = (TextView) ((Activity) context).findViewById(R.id.textViewHomeResponse);
            textView.setText(result.getString());
        }
    }
}

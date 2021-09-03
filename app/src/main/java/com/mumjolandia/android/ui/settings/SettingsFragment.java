package com.mumjolandia.android.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.R;

public class SettingsFragment extends Fragment {

    private String currentIpPrefix = "    current ip: ";
    private String currentPortPrefix = "current port: ";


    TextView currentIp;
    TextView currentPort;
    EditText newIp;
    EditText newPort;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        Button buttonSaveIp = root.findViewById(R.id.buttonSettingsSaveIp);
        currentIp = root.findViewById(R.id.textViewSettingsCurrentIp);
        currentPort = root.findViewById(R.id.textViewSettingsCurrentPort);
        newIp = root.findViewById(R.id.editTextSettingsIp);
        newPort = root.findViewById(R.id.editTextSettingsPort);

        InputFilter[] filters = new InputFilter[1]; // force ip format on editbox
        filters[0] = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
                    if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (int i=0; i<splits.length; i++) {
                            if (Integer.valueOf(splits[i]) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
        newIp.setFilters(filters);

        buttonSaveIp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ip = newIp.getText().toString();
                int port;
                try {
                    port = Integer.parseInt(newPort.getText().toString());
                }
                catch (NumberFormatException e) {
                    port = -1;
                }
                savePortAndIp(ip, port);
                loadPortAndIp();
            }
        });

        loadPortAndIp();

        return root;
    }

    private void savePortAndIp(String ip, int port){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (!ip.matches(".*\\..*\\..*\\..*")){
            Toast.makeText(getActivity(), "Incorrect ip", Toast.LENGTH_SHORT).show();
        }
        else {
            editor.putString(getString(R.string.mumjolandiaIpKey), ip);
        }
        if (port < 1 || port > 9999){
            Toast.makeText(getActivity(), "Incorrect port", Toast.LENGTH_SHORT).show();
        }
        else {
            editor.putInt(getString(R.string.mumjolandiaPortKey), port);
        }
        editor.commit();
    }
    private void loadPortAndIp(){
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        String ip = prefs.getString(getString(R.string.mumjolandiaIpKey), "None");
        int port = prefs.getInt(getString(R.string.mumjolandiaPortKey), -1);
        if (! ip.equals("None")){
            currentIp.setText(getString(R.string.settingsCurrentIp, currentIpPrefix, ip));
        }
        if (! (port == -1)){
            currentPort.setText(getString(R.string.settingsCurrentPort, currentPortPrefix, port));
        }
    }
}

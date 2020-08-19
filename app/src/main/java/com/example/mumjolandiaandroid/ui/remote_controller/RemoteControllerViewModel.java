package com.example.mumjolandiaandroid.ui.remote_controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RemoteControllerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RemoteControllerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is remote controller fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
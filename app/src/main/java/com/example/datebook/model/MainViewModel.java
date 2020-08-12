package com.example.datebook.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public MainViewModel() { }

    // check for connection
    private MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    public MutableLiveData<Boolean> getIsConnected() { return isConnected; }
    public void setIsConnected(Boolean isConnected) { this.isConnected.postValue(isConnected); }
}

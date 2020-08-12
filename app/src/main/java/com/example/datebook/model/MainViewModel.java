package com.example.datebook.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    public MainViewModel() { }

    // check for connection
    private MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    public MutableLiveData<Boolean> getIsConnected() { return isConnected; }
    public void setIsConnected(Boolean isConnected) { this.isConnected.postValue(isConnected); }

    // check registration flow start status
    private MutableLiveData<Boolean> isFirstTimeUser = new MutableLiveData<>();
    public MutableLiveData<Boolean> getIsFirstTimeUser() { return isFirstTimeUser; }
    public void setIsFirstTimeUser(Boolean isFirstTimeUser) { this.isFirstTimeUser.postValue(isFirstTimeUser); }

}

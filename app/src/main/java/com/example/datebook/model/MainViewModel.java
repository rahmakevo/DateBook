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

    // pass Public Name
    private MutableLiveData<String> publicName = new MutableLiveData<>();
    public MutableLiveData<String> getPublicName() { return publicName; }
    public void setPublicName(String publicName) { this.publicName.postValue(publicName); }

    // pass gender
    private MutableLiveData<String> selectedGender = new MutableLiveData<>();
    public MutableLiveData<String> getSelectedGender() { return selectedGender; }
    public void setSelectedGender(String selectedGender) { this.selectedGender.postValue(selectedGender); }

    // pass dob
    private MutableLiveData<String> dobSelected = new MutableLiveData<>();
    public MutableLiveData<String> getDobSelected() { return dobSelected; }
    public void setDobSelected(String dobSelected) { this.dobSelected.postValue(dobSelected); }
}

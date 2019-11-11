package com.example.android3dprint;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;

public class User extends  BaseObservable {

    @Bindable
    public String getFirstName() {
        return firstName+ ", nihao";
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName ;
        notifyPropertyChanged(BR.firstName);
    }


    private  String firstName="Michael";

    private String lastName;

    @Bindable
    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
}

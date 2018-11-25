package com.example.daniel.meetkai_test.Interfaces;

public interface OnChangeFragmentListener {
    // Authentication Enumerator
    enum AuthFragmentType{
        LOGIN,
        CREATE_ACCOUNT
    }

    void onChangeFragment(AuthFragmentType fragmentType);
}
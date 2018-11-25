package com.example.daniel.meetkai_test.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.daniel.meetkai_test.MainActivityContainer;
import com.example.daniel.meetkai_test.R;

public class SettingsFragment extends Fragment implements View.OnClickListener{

    // Views for this Fragment

    /***
     * Creates the view object for the login page
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return a View object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initalize the Fragment's view object
        Button logoutButton = view.findViewById(R.id.log_out_button);
        logoutButton.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.log_out_button: {
                ((MainActivityContainer)getActivity()).logUserOut();
            }
            break;
        }
    }
}

package com.example.daniel.meetkai_test.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daniel.meetkai_test.Interfaces.OnChangeFragmentListener;
import com.example.daniel.meetkai_test.R;

public class CreateAccountFragment extends Fragment {
    // Fragment Views
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private TextView weakPasswordTextView;
    private TextView confirmPasswordErrorTextView;
    private TextView errorMessageTextView;
    private TextView usernameTakenTextView;
    // Fragment Variables
    private boolean isWeakPasswordEnable;
    private boolean isPasswordMatch;
    private boolean usernameFilled, passwordFilled, confirmedPasswordFilled;
    // Interfaces for Authentication Container to implement
    private OnChangeFragmentListener onChangeFragmentListener;

    /***
     * instantiates CreateAccountFragment object
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        //Initialize variable
        isWeakPasswordEnable = false;
        isPasswordMatch = false;
        usernameFilled = false;
        passwordFilled = false;
        confirmedPasswordFilled = false;

        //Initialize all of the views
        usernameEditText = (EditText) view.findViewById(R.id.user_name_edit_text);
        passwordEditText = (EditText) view.findViewById(R.id.password_edit_text);
        confirmPasswordEditText = (EditText) view.findViewById(R.id.confirm_password_edit_text);
        weakPasswordTextView = (TextView) view.findViewById(R.id.weak_password_error);
        confirmPasswordErrorTextView = (TextView) view.findViewById(R.id.confirm_password_error);
        usernameTakenTextView = (TextView) view.findViewById(R.id.user_name_error_text_view);

        errorMessageTextView = view.findViewById(R.id.error_text_view);

        // Inflate the layout for this fragment
        return view;
    }
    /**
     * Sets the OnChangeFragmentListener to communicate from this fragment to the activity
     * @param onChangeFragmentListener The listener for communication
     */
    public void setOnChangeFragmentListener(OnChangeFragmentListener onChangeFragmentListener) {
        this.onChangeFragmentListener = onChangeFragmentListener;
    }
}

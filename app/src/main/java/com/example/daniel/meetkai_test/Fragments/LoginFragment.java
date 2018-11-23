package com.example.daniel.meetkai_test.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daniel.meetkai_test.AuthenticationContainer;
import com.example.daniel.meetkai_test.Interfaces.OnChangeFragmentListener;
import com.example.daniel.meetkai_test.R;

public class LoginFragment extends Fragment implements View.OnClickListener{

    // The views associated with this Fragment
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView errorMessageTextView;
    // Interfaces for Authentication Container to implement
    private OnChangeFragmentListener onChangeFragmentListener;


    /***
     * creates the view object for the login page
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return a View object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Initialize all of the views
        usernameEditText = view.findViewById(R.id.user_name_edit_text);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        errorMessageTextView = view.findViewById(R.id.error_text_view);
        Button signInButton = view.findViewById(R.id.sign_in_button);
        Button signUpButton = view.findViewById(R.id.sign_up_button);

        //Set listeners for buttons
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Gets called upon the user clicking an interactive item on screen
     * @param view The current view
     */
    public void onClick(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        switch (view.getId()) {
            case R.id.sign_in_button: //user selects sign in
                //Username or password edit field is empty
                if(username.trim().isEmpty() || password.trim().isEmpty()) {
                    errorMessageTextView.setText(R.string.empty_login_entry);
                }
                else{

                }
                break;
            case R.id.sign_up_button: //user selects sign up
                onChangeFragmentListener.onChangeFragment(AuthenticationContainer.AuthFragmentType.CREATE_ACCOUNT);
                break;
        }
    }

    /**
     * Sets the OnChangeFragmentListener to communicate from this fragment to the activity
     * @param onChangeFragmentListener The listener for communication
     */
    public void setOnChangeFragmentListener(OnChangeFragmentListener onChangeFragmentListener) {
        this.onChangeFragmentListener = onChangeFragmentListener;
    }
}

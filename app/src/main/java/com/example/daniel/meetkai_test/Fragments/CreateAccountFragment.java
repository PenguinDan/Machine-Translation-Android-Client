package com.example.daniel.meetkai_test.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daniel.meetkai_test.Interfaces.OnChangeActivityListener;
import com.example.daniel.meetkai_test.Interfaces.OnChangeFragmentListener;
import com.example.daniel.meetkai_test.MeetKai.AuthenticationResponse;
import com.example.daniel.meetkai_test.MeetKai.Request;
import com.example.daniel.meetkai_test.MeetKai.User;
import com.example.daniel.meetkai_test.R;
import com.example.daniel.meetkai_test.Utilities.ApplicationUtilities;
import com.example.daniel.meetkai_test.Utilities.UserUtilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAccountFragment extends Fragment {
    // Fragment Views
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText adminSecretEditText;
    private TextView weakPasswordTextView;
    private TextView confirmPasswordErrorTextView;
    // Fragment Variables
    private boolean isWeakPasswordEnable;
    private boolean isPasswordMatch;
    private boolean usernameFilled, passwordFilled, confirmedPasswordFilled;
    private final String TAG = CreateAccountFragment.class.getSimpleName();
    // Interfaces for Authentication Container to implement
    private OnChangeActivityListener onChangeActivityListener;
    public interface CreateAccountFragmentListener {
        void onEnableCreateAccountButton(boolean enable);
    }
    private CreateAccountFragmentListener createAccountFragmentListener;


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
        adminSecretEditText = (EditText) view.findViewById(R.id.admin_secret_edit_text);
        confirmPasswordEditText = (EditText) view.findViewById(R.id.confirm_password_edit_text);
        weakPasswordTextView = (TextView) view.findViewById(R.id.weak_password_error);
        confirmPasswordErrorTextView = (TextView) view.findViewById(R.id.confirm_password_error);

        // Debug
//        usernameEditText.setText("DanDan");
//        passwordEditText.setText("118Ma7ur3!!");
//        confirmPasswordEditText.setText("118Ma7ur3!!");

        // Method that sets all of the OnFocusChanged listeners for the edit texts
        setTextEditFocusListeners();
        setTextChangeListeners();

        // Inflate the layout for this fragment
        return view;
    }


    /**
     * Listener that tells the activity to switch to the main activity
     * @param onChangeActivityListener The listener that was implemented in the main activity
     */
    public void setOnChangeActivityListener(OnChangeActivityListener onChangeActivityListener) {
        this.onChangeActivityListener = onChangeActivityListener;
    }

    /**
     * Allows the Fragment and the main activity to communicate for any necessary methods
     *
     * @param createAccountFragmentListener The listener from the AuthenticationContainer for communication
     * */
    public void setCreateAccountFragmentListener(CreateAccountFragmentListener createAccountFragmentListener) {
        this.createAccountFragmentListener = createAccountFragmentListener;
    }

    /**
     * Create focus listener for edit text for username and password
     */
    private void setTextEditFocusListeners() {

        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * check if text field for username is empty
             * @param v
             * @param hasFocus
             */
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!usernameEditText.getText().toString().isEmpty()) {
                        usernameFilled = true;
                    }
                }
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * check if password is weak or has no text in text field
             * @param v
             * @param hasFocus
             */
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (passwordEditText.getText().toString().isEmpty()) {
                        weakPasswordTextView.setVisibility(View.INVISIBLE);
                        passwordFilled = false;
                    } else if (passwordGuidelineCheck(passwordEditText.getText().toString())) {
                        weakPasswordTextView.setVisibility(View.INVISIBLE);
                        passwordFilled = true;
                        isWeakPasswordEnable = false;
                    } else {
                        weakPasswordTextView.setVisibility(View.VISIBLE);
                        isWeakPasswordEnable = true;
                    }
                }
            }
        });

        confirmPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /**
             * check if text fields for password confirmation and password are equal
             * @param v - view Object for CreateAccountFragment
             * @param hasFocus - verifies if cursor is focused on second text field
             */
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                        confirmPasswordErrorTextView.setVisibility(View.INVISIBLE);
                        isPasswordMatch = true;
                        confirmedPasswordFilled = true;
                    } else {
                        confirmPasswordErrorTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * Create text change listener for all of the edit texts in this Fragment
     */
    private void setTextChangeListeners() {
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            /**
             * when user changes their input
             * @param s - user input sequence
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().toString().trim().isEmpty()) {
                    usernameFilled = true;
                } else {
                    usernameFilled = false;
                }
                enableCreateAccountButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                isPasswordMatch = false;

                //Check if the password and confirm password match
                if (confirmedPasswordFilled) {
                    if (passwordEditText.getText().toString().equals(s.toString())) {
                        isPasswordMatch = true;
                    }
                }

                //Check if the user entered a password that follow the guide line
                if (passwordGuidelineCheck(s.toString())) {
                    passwordFilled = true;
                    isWeakPasswordEnable = false;
                } else {
                    passwordFilled = false;
                    isWeakPasswordEnable = true;
                }
                enableCreateAccountButton();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                isPasswordMatch = false;

                //Check if password and confirm password match
                if (passwordEditText.getText().toString().equals(s.toString())) {
                    confirmedPasswordFilled = true;
                    isPasswordMatch = true;
                    confirmPasswordErrorTextView.setVisibility(View.INVISIBLE);

                } else if (s.toString().trim().isEmpty()) {
                    confirmedPasswordFilled = false;
                }
                enableCreateAccountButton();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Checks the user's password to make sure it is strong enough to prevent dictionary attacks
     *
     * @param password The user's desired password
     * @return True if the password is strong enough and false otherwise
     */
    private boolean passwordGuidelineCheck(String password) {
        /**
         * Passwords must contain at least one a-z character, one A-Z character
         * one 0-9 character,and be 8 characters long minimum
         */
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }

    /**
     * Enable create account button to change button color to black
     */
    private void enableCreateAccountButton() {
        if (usernameFilled && passwordFilled && confirmedPasswordFilled && !isWeakPasswordEnable &&
                isPasswordMatch) {
            createAccountFragmentListener.onEnableCreateAccountButton(true);
        } else {
            createAccountFragmentListener.onEnableCreateAccountButton(false);
        }
    }


    /**
     * Retrieves the user's information and creates and account
     */
    public void createAccount() {
        // Retrieve the information in the edit texts
        final String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String adminSecret = adminSecretEditText.getText().toString();

        // Begin the request to the server
        Request request = new Request(getActivity());
        Call<AuthenticationResponse> createAccountRequest = request.createUser(username, password, adminSecret);
        Log.d(TAG, "Creating user account");
        createAccountRequest.enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                // Get the response code from the server
                switch(response.code()) {
                    case Request.CREATED:{
                        // Successful user account creation, cache their information
                        UserUtilities.cacheUser(getActivity(), response.body());
                    }
                    break;
                    case Request.UNAUTHORIZED: {
                        // The user attempted to create an admin account with an incorrect admin secret
                        ApplicationUtilities.displayToast(getActivity(), "Incorrect admin secret.");
                    }
                    break;
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                ApplicationUtilities.displayToast(getActivity(), "Server Error");
                t.printStackTrace();
            }
        });
    }


}

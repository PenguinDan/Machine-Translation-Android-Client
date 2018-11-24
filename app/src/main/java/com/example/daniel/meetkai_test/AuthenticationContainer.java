package com.example.daniel.meetkai_test;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daniel.meetkai_test.Fragments.CreateAccountFragment;
import com.example.daniel.meetkai_test.Fragments.LoginFragment;
import com.example.daniel.meetkai_test.Interfaces.OnChangeFragmentListener;

import org.w3c.dom.Text;

public class AuthenticationContainer extends AppCompatActivity implements OnChangeFragmentListener,
    View.OnClickListener{

    // Fragments associated with this container
    private LoginFragment loginFragment;
    private CreateAccountFragment createAccountFragment;
    // Views associated with this container
    private RelativeLayout toolbar;
    private ImageButton backButton;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        // Initialize all of the fragments
        loginFragment = new LoginFragment();
        loginFragment.setOnChangeFragmentListener(this);
        createAccountFragment = new CreateAccountFragment();
        createAccountFragment.setOnChangeFragmentListener(this);
        createAccountFragment.setCreateAccountFragmentListener(new CreateAccountFragment.CreateAccountFragmentListener() {
            /**
             * check if create button is enabled
             * @param enable - boolean variable to verify button is enabled
             */
            @Override
            public void onEnableCreateAccountButton(boolean enable) {
                //Enable create account button and change it color
                if(enable){
                    createAccountButton.setEnabled(true);
                    createAccountButton.setTextColor(getColor(R.color.black));
                }
                //Disable create account button and change its color
                else{
                    createAccountButton.setEnabled(false);
                    createAccountButton.setTextColor(getColor(R.color.createAccount));
                }
            }
        });

        // Initialize the views
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        backButton = (ImageButton) findViewById(R.id.back_button);
        backButton.setOnClickListener(this);
        createAccountButton = (Button) findViewById(R.id.create_account_button);

        // Automatically start the Login Fragment
        beginFragment(AuthFragmentType.LOGIN, true, false);

    }

    /**
     * Allows for Fragments to tell the Authentication Container to change fragments
     * @param fragmentType The fragment type to change to
     */
    @Override
    public void onChangeFragment(AuthFragmentType fragmentType) {
        beginFragment(fragmentType, true, true);
    }

    /**
     * Helper method that replaces the current fragment with one that is specified
     * @param fragmentType   The fragment that should now appear
     * @param setTransition  If the fragment should be transitioned in to the viewer
     * @param addToBackStack If the fragment should be added to the activity's back-stack
     */
    private void beginFragment(AuthFragmentType fragmentType, boolean setTransition, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        switch (fragmentType) {
            case LOGIN:
                fragmentTransaction.replace(R.id.main_display_container, loginFragment);
                break;
            case CREATE_ACCOUNT:
                toolbar.setVisibility(View.VISIBLE);
                createAccountButton.setVisibility(View.VISIBLE);
                fragmentTransaction.replace(R.id.main_display_container, createAccountFragment);
                break;
        }
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    /**
     * Event handler for the buttons in the StartUpContainer object
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button: {
                //Remove the current view of back stack
                getFragmentManager().popBackStackImmediate();
                toolbar.setVisibility(View.GONE);
            }
            break;
            case R.id.create_account_button: {
                createAccountFragment.createAccount();
            }
            break;
        }
    }
}

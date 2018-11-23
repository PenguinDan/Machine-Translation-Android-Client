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

public class AuthenticationContainer extends AppCompatActivity implements OnChangeFragmentListener {

    // Fragments associated with this container
    private LoginFragment loginFragment;
    private CreateAccountFragment createAccountFragment;
    // Views associated with this container
    private RelativeLayout toolbar;
    private ImageButton backButton;
    private Button createAccountButton;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize all of the fragments
        loginFragment = new LoginFragment();
        loginFragment.setOnChangeFragmentListener(this);
        createAccountFragment = new CreateAccountFragment();
        createAccountFragment.setOnChangeFragmentListener(this);

        // Initialize the views
        toolbar = (RelativeLayout) findViewById(R.id.toolbar);
        backButton = (ImageButton) findViewById(R.id.back_button);
        createAccountButton = (Button) findViewById(R.id.create_account_button);
        titleTextView = (TextView) findViewById(R.id.start_up_title_text_view);

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
                titleTextView.setText(R.string.create_account);
                fragmentTransaction.replace(R.id.main_display_container, createAccountFragment);
                break;
        }
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

}

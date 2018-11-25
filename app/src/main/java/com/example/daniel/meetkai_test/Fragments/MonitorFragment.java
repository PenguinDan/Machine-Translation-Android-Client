package com.example.daniel.meetkai_test.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.daniel.meetkai_test.Interfaces.OnRetrieveAccessToken;
import com.example.daniel.meetkai_test.MainActivityContainer;
import com.example.daniel.meetkai_test.MeetKai.Request;
import com.example.daniel.meetkai_test.MeetKai.SourceHashResponse;
import com.example.daniel.meetkai_test.MeetKai.User;
import com.example.daniel.meetkai_test.R;
import com.example.daniel.meetkai_test.Utilities.ApplicationUtilities;
import com.example.daniel.meetkai_test.Utilities.UserUtilities;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitorFragment extends Fragment implements View.OnClickListener{
    // Fragment Views
    private TextView monitorTextView;
    private Button getUserAnnotationsButton;
    private Button activeMonitorButton;
    private Button getSourceAnnotationsButton;
    // Fragment variables
    private User user;
    private Request request;
    private AlertDialog userAnnotationDialog;
    private AlertDialog sourceAnnotationDialog;

    /***
     * Creates the view object for the login page
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return a View object
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);

        // Initialize Fragment variables
        user = UserUtilities.getUserObject(getActivity());
        request = new Request(getActivity());

        // Initialize Fragment Views
        monitorTextView = (TextView) view.findViewById(R.id.monitor_fragment_text_view);
        getUserAnnotationsButton = (Button) view.findViewById(R.id.get_user_annotations);
        getUserAnnotationsButton.setOnClickListener(this);
        activeMonitorButton = (Button) view.findViewById(R.id.active_monitor_button);
        getSourceAnnotationsButton = (Button) view.findViewById(R.id.get_source_annotations);
        getSourceAnnotationsButton.setOnClickListener(this);
        if(!user.isAdmin()){
            activeMonitorButton.setVisibility(View.GONE);
            getSourceAnnotationsButton.setVisibility(View.GONE);
        } else {
            activeMonitorButton.setVisibility(View.VISIBLE);
            getSourceAnnotationsButton.setVisibility(View.VISIBLE);
        }


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(final View v) {
        // Check whether the user's access tokens have expired and call the methods
        UserUtilities.getUserAccessToken(getActivity(), new OnRetrieveAccessToken() {
            @Override
            public void onAccessTokenRenewed(int responseCode, String token) {
                if(responseCode == Request.ACCEPTED) {
                    // Begin the request with the new access token
                    requestFromServer(v, token);
                } else {
                    // Log the user out because their refresh token has expired
                    ((MainActivityContainer)getActivity()).logUserOut();
                    ApplicationUtilities.displayToast(getActivity(),"User must log in again");
                }
            }

            @Override
            public void onGetAccessToken(String token) {
                if (token != null) {
                    requestFromServer(v, token);
                } else {
                    // Log the user out because their is a null value for the refresh token
                    ((MainActivityContainer)getActivity()).logUserOut();
                    ApplicationUtilities.displayToast(getActivity(),"User must log in again");
                }
            }
        });
    }

    /**
     * Sends a request to the server based on what button was clicked
     *
     * @param view The button that was clicked
     * @param accessToken The user's access token
     */
    private void requestFromServer(View view, String accessToken) {
        switch(view.getId()) {
            case R.id.get_user_annotations: {
                if(user.isAdmin()) {
                    getAdminAnnotations(accessToken);
                } else {
                    getAnnotations(user.getUsername(), null,accessToken);
                }
            }
            break;
            case R.id.active_monitor_button: {

            }
            break;
            case R.id.get_source_annotations:{
                getSourceAnnotations(accessToken);
            }
            break;
        }
    }

    /**
     * Retrieves the source annotations
     *
     * @param accessToken The user's access token
     */
    private void getSourceAnnotations(final String accessToken) {
        // Create an instance of the Alert Dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Set the view of the Alert Dialog
        final View alertDialogView = getActivity().getLayoutInflater().inflate(R.layout.alert_dialog_source_annotations, null);
        alertDialogBuilder.setView(alertDialogView);

        // Initialize the views
        final Spinner hashSpinner = (Spinner) alertDialogView.findViewById(R.id.hash_spinner);
        final Button confirmButton = (Button) alertDialogView.findViewById(R.id.source_annotation_confirm_button);
        final Button cancelButton = (Button) alertDialogView.findViewById(R.id.source_annotations_cancel_button);

        // Begin a request to retrieve the hash list from the server
        Call<SourceHashResponse> getSourceHashesRequest = request.retrieveSourceHashes(
                user.getUsername(),
                accessToken
        );
        getSourceHashesRequest.enqueue(new Callback<SourceHashResponse>() {
            @Override
            public void onResponse(Call<SourceHashResponse> call, Response<SourceHashResponse> response) {
                if (response.code() == Request.OK) {
                    SourceHashResponse sourceHashResponse = response.body();
                    ArrayList<String> hashList = new ArrayList<>();
                    for (Map<String, String> sourceMap : sourceHashResponse.getList()) {
                        hashList.add(sourceMap.get("hash"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getActivity(), android.R.layout.simple_spinner_item, hashList
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    hashSpinner.setAdapter(adapter);
                } else {
                    ApplicationUtilities.displayToast(getActivity(), "Server error");
                }
            }

            @Override
            public void onFailure(Call<SourceHashResponse> call, Throwable t) {
                t.printStackTrace();
                ApplicationUtilities.displayToast(getActivity(), "Server Error");
            }
        });

        // Set the on click listeners for each button
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hash = hashSpinner.getSelectedItem().toString();
                Call<ResponseBody> sourceAnnotationsRequest =
                        request.retrieveSourceAnnotations(user.getUsername(), accessToken, hash, null);
                sourceAnnotationsRequest.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == Request.OK) {
                            try{
                                String sResponse = ApplicationUtilities.prettyPrintJson(response.body().string());
                                setMonitorText(sResponse, false);
                                sourceAnnotationDialog.dismiss();
                            }catch(IOException ex) {
                                ex.printStackTrace();
                            }
                        } else if (response.code() == Request.UNAUTHORIZED){
                            ApplicationUtilities.displayToast(getActivity(), "Must be user admin");
                        } else if (response.code() == Request.BAD_REQUEST) {
                            ApplicationUtilities.displayToast(getActivity(), "Must choose a choice");
                        } else if (response.code() == Request.NOT_FOUND) {
                            ApplicationUtilities.displayToast(getActivity(), "Invalid hash");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        ApplicationUtilities.displayToast(getActivity(), "Server Error");
                    }
                });
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceAnnotationDialog.dismiss();
            }
        });

        sourceAnnotationDialog = alertDialogBuilder.create();
        sourceAnnotationDialog.show();

    }

    /**
     * Admin version of the user annotations method
     *
     * @param accessToken The admin's access token
     */
    private void getAdminAnnotations(final String accessToken) {
        // Create an instance of the Alert Dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Set the view of the Alert Dialog
        final View alertDialogView = getActivity().getLayoutInflater().inflate(R.layout.alert_dialog_user_annotation, null);
        alertDialogBuilder.setView(alertDialogView);

        // Initialize the views for this Fragment
        final EditText adminUserChoiceEditText = (EditText) alertDialogView.findViewById(R.id.user_annotation_edit_text);
        final Button confirmUserChoiceButton = (Button) alertDialogView.findViewById(R.id.user_annotation_confirm_button);
        final Button selfUserChoiceButton = (Button) alertDialogView.findViewById(R.id.user_annotation_self_button);
        final Button cancelUserChoiceButton = (Button) alertDialogView.findViewById(R.id.user_annotation_cancel_button);

        // Add listeners for each button
        confirmUserChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminChoice = adminUserChoiceEditText.getText().toString();
                getAnnotations(user.getUsername(), adminChoice, accessToken);
                userAnnotationDialog.dismiss();
            }
        });
        selfUserChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnnotations(user.getUsername(), null, accessToken);
                userAnnotationDialog.dismiss();
            }
        });
        cancelUserChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAnnotationDialog.dismiss();
            }
        });

        userAnnotationDialog = alertDialogBuilder.create();
        userAnnotationDialog.show();
    }

    /**
     * Retrieves all of the user's annotations
     *
     * @param accessToken The user's access token
     */
    private void getAnnotations(String username, String targetUser, String accessToken) {
        // Send the request to the server
        Call<ResponseBody> userAnnotationRequest = request.retrieveUserAnnotations(username, accessToken, targetUser);
        userAnnotationRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == Request.OK) {
                    try{
                        String sResponse = ApplicationUtilities.prettyPrintJson(response.body().string());
                        setMonitorText(sResponse, false);
                    } catch(IOException ex) {
                        ex.printStackTrace();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                ApplicationUtilities.displayToast(getActivity(), "Server error");
            }
        });
    }

    /**
     * Sets the text for the Monitor Text View
     *
     * @param text The text to add
     * @param append Whether the new text should be appended to the current text
     */
    private void setMonitorText(String text, boolean append) {
        if(append) {
            String currText = monitorTextView.getText().toString();
            text = currText + "\n" + text;
        }
        int occurence = StringUtils.countOccurrencesOf(text, "\n") *2;
        monitorTextView.setLines(occurence);
        monitorTextView.setText(text);
    }
}

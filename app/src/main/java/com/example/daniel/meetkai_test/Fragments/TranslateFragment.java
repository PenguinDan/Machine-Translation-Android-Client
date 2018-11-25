package com.example.daniel.meetkai_test.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.daniel.meetkai_test.Interfaces.OnRetrieveAccessToken;
import com.example.daniel.meetkai_test.MainActivityContainer;
import com.example.daniel.meetkai_test.MeetKai.GetPhraseResponse;
import com.example.daniel.meetkai_test.MeetKai.Request;
import com.example.daniel.meetkai_test.MeetKai.User;
import com.example.daniel.meetkai_test.R;
import com.example.daniel.meetkai_test.Utilities.ApplicationUtilities;
import com.example.daniel.meetkai_test.Utilities.CountryCodes;
import com.example.daniel.meetkai_test.Utilities.UserUtilities;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslateFragment extends Fragment implements View.OnClickListener{
    // The views for this Fragment
    private EditText originalPhraseEditText;
    private EditText azureTranslationEditText;
    private EditText googleTranslationEditText;
    private EditText yandexTranslationEditText;
    private Spinner languageSpinner;
    private Button sendResponseButton;
    private Button getPhraseButton;
    private RadioGroup azureRadioGroup;
    private RadioGroup googleRadioGroup;
    private RadioGroup yandexRadioGroup;
    private ProgressBar progressBar;
    // Fragment variables
    private Request request;
    private User user;
    private String phraseHash;
    private String phraseLanguage;
    private CountryCodes countryCodes;
    private AlertDialog addSourceDialog;

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
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        // Initialize the Views
        originalPhraseEditText = (EditText) view.findViewById(R.id.original_phrase_edit_text);
        azureTranslationEditText = (EditText) view.findViewById(R.id.azure_translation_edit_text);
        googleTranslationEditText = (EditText) view.findViewById(R.id.google_translation_edit_text);
        yandexTranslationEditText = (EditText) view.findViewById(R.id.yandex_translation_edit_text);
        azureRadioGroup = (RadioGroup) view.findViewById(R.id.azure_radio_group);
        googleRadioGroup = (RadioGroup) view.findViewById(R.id.google_radio_group);
        yandexRadioGroup = (RadioGroup) view.findViewById(R.id.yandex_radio_group);
        languageSpinner = (Spinner) view.findViewById(R.id.language_spinner);
        getPhraseButton = (Button) view.findViewById(R.id.get_phrase_button);
        getPhraseButton.setOnClickListener(this);
        sendResponseButton = (Button) view.findViewById(R.id.send_response_button);
        sendResponseButton.setOnClickListener(this);
        enableSendResponseButton(false);
        progressBar = (ProgressBar) view.findViewById(R.id.translate_progress_bar);

        Button addSourceButton = (Button) view.findViewById(R.id.add_source_button);
        addSourceButton.setOnClickListener(this);

        // Initialize the Fragment variables
        request = new Request(getActivity());
        user = UserUtilities.getUserObject(getActivity());
        countryCodes = new CountryCodes();

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * The on click listener for the various buttons
     * @param v The view that was clicked
     */
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
     * Calls the method depending on what button was clicked
     * @param view The button that was clicked
     * @param accessToken The user's unexpired access token
     */
    private void requestFromServer(View view, String accessToken) {
        switch(view.getId()) {
            case R.id.get_phrase_button:{
                getPhraseFromServer(accessToken);
            }
            break;
            case R.id.send_response_button: {
                sendPhraseAnnotations(accessToken);
            }
            case R.id.add_source_button: {
                createAddSourceDialog(accessToken);
            }
            break;
        }
    }

    /**
     * Sends the server the annotations that the has made
     *
     * @param accessToken The user's access token
     */
    private void sendPhraseAnnotations(String accessToken) {
        // Retrieve the annotations for each one
        boolean azureIsCorrect = false;
        boolean googleIsCorrect = false;
        boolean yandexIsCorrect = false;

        if (azureRadioGroup.getCheckedRadioButtonId() == R.id.azure_correct_radio_button) {
            azureIsCorrect = true;
        }
        if (googleRadioGroup.getCheckedRadioButtonId() == R.id.google_correct_radio_button) {
            googleIsCorrect = true;
        }
        if(yandexRadioGroup.getCheckedRadioButtonId() == R.id.yandex_correct_radio_button) {
            yandexIsCorrect = true;
        }
        Call<ResponseBody> annotatePhraseRequest = request.annotatePhrase(
                user.getUsername(),
                accessToken,
                phraseHash,
                phraseLanguage,
                azureIsCorrect,
                googleIsCorrect,
                yandexIsCorrect
        );
        annotatePhraseRequest.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == Request.ACCEPTED) {
                    getPhraseButton.callOnClick();
                } else {
                    ApplicationUtilities.displayToast(getActivity(), "Error saving response");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                ApplicationUtilities.displayToast(getActivity(), "Server error.");
            }
        });
    }

    /**
     * Retrieves a phrase from the server
     *
     * @param accessToken The user's access token
     */
    private void getPhraseFromServer(String accessToken) {
        // Make the progress bar visible
        progressBar.setVisibility(View.VISIBLE);
        // Retrieve the chosen language value from the spinner
        String chosenLanguage = languageSpinner.getSelectedItem().toString();
        final String countryCode = countryCodes.getCode(chosenLanguage);

        // Retrieve the server request
        Call<GetPhraseResponse> getPhraseRequest = request.getPhrase(
                countryCode,
                user.getUsername(),
                accessToken
        );
        getPhraseRequest.enqueue(new Callback<GetPhraseResponse>() {
            @Override
            public void onResponse(Call<GetPhraseResponse> call, Response<GetPhraseResponse> response) {
                // Retrieved response, make progress bar disappear
                progressBar.setVisibility(View.GONE);
                if(response.code() == Request.OK) {
                    GetPhraseResponse phraseResp = response.body();
                    // Put the translations in the edit texts
                    originalPhraseEditText.setText(phraseResp.getPhrase());
                    azureTranslationEditText.setText(phraseResp.getAzureTranslation());
                    googleTranslationEditText.setText(phraseResp.getGoogleTranslation());
                    yandexTranslationEditText.setText(phraseResp.getYandexTranslation());
                    phraseHash = phraseResp.getHash();
                    phraseLanguage = countryCode;
                    enableSendResponseButton(true);

                } else if(response.code() == Request.UNAUTHORIZED) {
                    // User's access token is incorrect

                } else if(response.code() == Request.NOT_ACCEPTABLE) {
                    // User's access token has expired

                }
            }

            @Override
            public void onFailure(Call<GetPhraseResponse> call, Throwable t) {
                t.printStackTrace();
                ApplicationUtilities.displayToast(getActivity(), "Server Error");
            }
        });
    }

    /**
     * Enables/ Disables the sendResponseButton
     *
     * @param enable Whether the enable the button
     */
    private void enableSendResponseButton(boolean enable) {
        if (!enable) {
            sendResponseButton.setEnabled(false);
            sendResponseButton.setTextColor(getActivity().getColor(R.color.lightGray));
        } else {
            sendResponseButton.setEnabled(true);
            sendResponseButton.setTextColor(getActivity().getColor(R.color.black));
        }
    }


    private void createAddSourceDialog(final String accessToken) {
        // Create an instance of the Alert Dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // Set the view of the Alert Dialog
        final View alertDialogView = getActivity().getLayoutInflater().inflate(R.layout.alert_dialog_add_source, null);
        alertDialogBuilder.setView(alertDialogView);

        // Initialize the views for this Fragment
        final Button confirmAddSourceButton = (Button) alertDialogView.findViewById(R.id.confirm_add_source_button);
        final Button exitAddSourceButton = (Button) alertDialogView.findViewById(R.id.exit_add_source_button);
        final EditText addSourceEditText = (EditText) alertDialogView.findViewById(R.id.add_source_edit_text);

        // Set the listeners for each button and what they should do
        confirmAddSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the text from the add source edit text
                String sourceText = addSourceEditText.getText().toString();
                if(sourceText.isEmpty()) {
                    ApplicationUtilities.displayToast(getActivity(), "Source text cannot be empty");
                    return;
                }
                // Make the request to the server
                Call<ResponseBody> addSourceRequest = request.addPhrase(sourceText, user.getUsername(), accessToken);
                addSourceRequest.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == Request.OK) {
                            ApplicationUtilities.displayToast(getActivity(), "Source text added successfully");
                        } else if (response.code() == Request.NOT_ACCEPTABLE) {
                            ApplicationUtilities.displayToast(getActivity(), "Source text already exists");
                        } else {
                            ApplicationUtilities.displayToast(getActivity(), "Server error");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                        ApplicationUtilities.displayToast(getActivity(), "Server error");
                    }
                });
            }
        });
        exitAddSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSourceDialog.dismiss();
            }
        });

        addSourceDialog = alertDialogBuilder.create();
        addSourceDialog.show();
    }

}

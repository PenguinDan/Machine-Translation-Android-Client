package com.example.daniel.meetkai_test.MeetKai;

import android.content.Context;

import com.example.daniel.meetkai_test.R;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Request {
    // Top level variables
    private Context context;
    // Variables to interface with the server
    private HTTPInterface httpInterface;
    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient.Builder okHttpClientBuilder;
    private String clientId;
    // Constants
    public final static int OK = 200;
    public final static int CREATED = 201;
    public final static int ACCEPTED = 202;
    public final static int NON_AUTHORUTATUVE_INFO = 203;
    public final static int NO_CONTENT = 204;
    public final static int RESET_CONTENT = 205;
    public final static int PARTIAL_CONTENT = 206;
    public final static int BAD_REQUEST = 400;
    public final static int UNAUTHORIZED = 401;
    public final static int FORBIDDEN = 403;
    public final static int NOT_FOUND = 404;
    public final static int METHOD_NOT_ALLOWED = 405;
    public final static int NOT_ACCEPTABLE = 406;
    public final static int INTERNAL_SERVER_ERROR = 500;

    /**
     * Constructor
     * @param context Application context
     */
    public Request(Context context) {
        this.context = context;

        // Retrieve the client Id value
        clientId = context.getString(R.string.client_id);

        // Create a standard retrofit object to convert all of the responses to their proper forms
        retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(context.getString(R.string.base_url)).addConverterFactory(
                GsonConverterFactory.create()
        );

        // Instantiate an OkHttpClient.Builder object for logging
        okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        okHttpClientBuilder.addInterceptor(loggingInterceptor);
        retrofitBuilder.client(okHttpClientBuilder.build());

        // Create the Retrofit object to send requests to the server
        Retrofit retrofit = retrofitBuilder.build();
        httpInterface = retrofit.create(HTTPInterface.class);
    }

    /**
     * Creates a retrofit login request
     *
     * @param username The user's username
     * @param password The user's password
     *
     * @return A login request object
     */
    public Call<AuthenticationResponse> loginUser(final String username, final String password) {
        // Create the GET request to login the user
        return httpInterface.loginUser(clientId, username, password);
    }

    /**
     * Creates a retrofit create user request
     *
     * @param username The user's username
     * @param password The user's password
     * @param appSecret The application secret if the user has it for an admin account
     *
     * @return A create user account request object
     */
    public Call<AuthenticationResponse> createUser(final String username, final String password, final String appSecret){
        // Create the POST request to create a user
        return httpInterface.createUser(clientId, username, password, appSecret);
    }

    /**
     * Creates a retrofit get phrase request
     *
     * @param language The language in which the user wants to translate in
     * @param username The user's username
     * @param accessToken The user's access token
     *
     * @return A get phrase request object
     */
    public Call<GetPhraseResponse> getPhrase(final String language, final String username, final String accessToken){
        // Create the GET request to retrieve a phrase from the server
        return httpInterface.getPhrase(clientId, language, username, accessToken);
    }

    /**
     * Creates a retrofit add phrase request
     *
     * @param phrase The phrase that the user wants to add
     * @param username The user's username
     * @param accessToken The user's access token
     *
     * @return An add phrase request object
     */
    public Call<ResponseBody> addPhrase(final String phrase, final String username, final String accessToken) {
        // Create the POST request to save a phrase in the server
        return httpInterface.addPhrase(clientId, phrase, username, accessToken);
    }

    /**
     * Allows the user to refresh their access token
     *
     * @param username The user's username
     * @param refreshToken The user's refresh token
     *
     * @return A renew token request object
     */
    public Call<AuthenticationResponse> renewToken(final String username, final String refreshToken) {
        // Create the PUT request to get a new access token
        return httpInterface.renewToken(clientId, username, refreshToken);
    }

    /**
     * Allows the user to annotate a phrase
     * @param username The user's username
     * @param accessToken The user's access token
     * @param phraseHash The hash of the phrase they are annotating
     * @param languageAbr The language abbreviation of the language they abbreviated
     * @param isAzureCorrect If the azure translation was correct
     * @param isGoogleCorrect If the google translation was correct
     * @param isYandexCorrect If the yandex translation was correct
     *
     * @return A annotate phrase request object
     */
    public Call<ResponseBody> annotatePhrase(final String username, final String accessToken,
                                             final String phraseHash, final String languageAbr,
                                             final boolean isAzureCorrect, final boolean isGoogleCorrect,
                                             final boolean isYandexCorrect) {
        // Create the PUT request to annotate a phrase
        return httpInterface.annotatePhrase(clientId, username, accessToken, phraseHash, languageAbr,
                isAzureCorrect, isGoogleCorrect, isYandexCorrect);
    }

    /**
     * Allows the user to retrieve their annotations
     * Allows an admin to retrieve their or another user's annotations
     *
     * @param username The user's username
     * @param accessToken The user's access token
     * @param targetUser The target user if the user is an admin
     *
     * @return A retrieve user annotations request object
     */
    public Call<ResponseBody> retrieveUserAnnotations(final String username, final String accessToken,
                                                      final String targetUser) {
        // Create the GET request for the user's annotations
        return httpInterface.retrieveUserAnnotations(clientId, username, accessToken, targetUser);
    }

    /**
     * Allows an admin to retrieve source annotations
     *
     * @param username The user's username
     * @param accessToken The user's access token
     * @param hash The hash of the source item to retrieve the annotations for
     * @param phrase The phrase of the source item to retrieve the annotations for
     *
     * @return A request object to retrieve source annotations
     */
    public Call<ResponseBody> retrieveSourceAnnotations(final String username, final String accessToken,
                                                                     final String hash, final String phrase) {
        // Create the GET request to get a source's annotations
        return httpInterface.retrieveSourceAnnotations(clientId, username, accessToken, hash, phrase);
    }

    /**
     * Retrieves the source hashes
     *
     * @param username The user's username
     * @param accessToken The user's access tokens
     *
     * @return A request object to retrieve source hashes
     */
    public Call<SourceHashResponse> retrieveSourceHashes(final String username, final String accessToken) {
        // Create the GET request to get source hashes
        return httpInterface.retrieveSourceHashes(clientId, username, accessToken);
    }

    /**
     * Allows an admin to ask for monitoring access privilege
     *
     * @param username The admin's username
     * @param accessToken The admin's access token
     *
     * @return A request object for the admin
     */
    public Call<ResponseBody> getMonitoringAccess(final String username, final String accessToken) {
        // Create the GET request for getting monitoring access
        return httpInterface.getMonitoringAccess(clientId, username, accessToken);
    }

}

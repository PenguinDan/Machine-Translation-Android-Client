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
     *
     * @param username
     * @param password
     * @return
     */
    public Call<AuthenticationResponse> loginUser(final String username, final String password) {
        // Create the GET request to login the user
        return httpInterface.loginUser(clientId, username, password);
    }

    /**
     *
     * @param username
     * @param password
     * @param appSecret
     * @return
     */
    public Call<AuthenticationResponse> createUser(final String username, final String password, final String appSecret){
        // Create the POST request to create a user
        return httpInterface.createUser( clientId, username, password, appSecret);
    }


}

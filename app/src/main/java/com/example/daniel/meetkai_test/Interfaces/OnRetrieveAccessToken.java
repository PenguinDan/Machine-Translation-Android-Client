package com.example.daniel.meetkai_test.Interfaces;

public interface OnRetrieveAccessToken {
    void onAccessTokenRenewed(int responseCode, String token);
    void onGetAccessToken(String token);
}

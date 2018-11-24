package com.example.daniel.meetkai_test.MeetKai

class AuthenticationResponse(val message : String?,
                             val accessToken : Map<String, String>?,
                             val refreshToken: Map<String, String>?,
                             val isAdmin : Boolean?){

    /**
     * Returns the access token value or expiration depending on the argument
     */
    fun getAccessTokenValue(key: String) : String? {
        if (accessToken != null ) {
            return accessToken[key]
        }
        return null
    }

    /**
     * Returns the refresh token value or expiration depending on the argument
     */
    fun getRefreshTokenValue(key: String) : String? {
        if(refreshToken != null) {
            return refreshToken[key]
        }
        return null
    }
}

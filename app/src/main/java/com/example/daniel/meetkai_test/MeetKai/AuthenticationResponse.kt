package com.example.daniel.meetkai_test.MeetKai

class AuthenticationResponse(val message : String,
                             val username : String?,
                             val accessToken : Map<String, String>?,
                             val refreshToken: Map<String, String>?,
                             val isAdmin : Boolean?){
    // Class constants
    val TOKEN = "token"
    val EXPIRATION = "expiration"

    /**
     * Returns the access token value
     */
    fun getAccessTokenValue() : String? {
        return accessToken?.get(TOKEN)
    }

    /**
     * Returns the access token expiration
     */
    fun getAccessTokenExpiration() : String?{
        return accessToken?.get(EXPIRATION)
    }

    /**
     * Returns the refresh token value
     */
    fun getRefreshTokenValue() : String? {
        return refreshToken?.get(TOKEN)
    }

    fun getRefreshTokenExpiration() : String? {
        return refreshToken?.get(EXPIRATION)
    }
}

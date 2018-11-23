package com.example.daniel.meetkai_test.MeetKai

import java.io.Serializable

data class User(val username: String,
                val password: String?,
                val accessToken: String?,
                val refreshToken: String) : Serializable
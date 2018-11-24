package com.example.daniel.meetkai_test.MeetKai

import java.io.Serializable

data class User(val username: String,
                val accessToken: String,
                val refreshToken: Map<String, String>) : Serializable
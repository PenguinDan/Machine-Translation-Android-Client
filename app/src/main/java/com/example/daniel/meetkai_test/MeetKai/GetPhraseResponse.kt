package com.example.daniel.meetkai_test.MeetKai

class GetPhraseResponse(val phrase : String,
                        val hash: String,
                        val targetLanguage : String,
                        val azureTranslation: String,
                        val googleTranslation : String,
                        val yandexTranslation : String)
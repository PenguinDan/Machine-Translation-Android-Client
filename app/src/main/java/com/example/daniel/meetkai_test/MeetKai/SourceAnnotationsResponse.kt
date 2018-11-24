package com.example.daniel.meetkai_test.MeetKai

import org.json.JSONObject

class SourceAnnotationsResponse(val phrase : String,
                                val annotations: Map<String, Map<String, JSONObject>>?,
                                val languages : Array<String>?){

}
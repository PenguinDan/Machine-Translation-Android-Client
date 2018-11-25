package com.example.daniel.meetkai_test.Utilities;
import java.util.Map;
import java.util.TreeMap;


public class CountryCodes {
    final Map<String, String> map = new TreeMap<String, String>   (String.CASE_INSENSITIVE_ORDER);

    public CountryCodes() {
        map.put("Afrikaans", "af");
        map.put("Arabic", "ar");
        map.put("Bosnian", "bs");
        map.put("Bulgarian", "bg");
        map.put("Catalan", "ca");
        map.put("Croatian", "hr");
        map.put("Czech", "cs");
        map.put("Danish", "da");
        map.put("Dutch", "nl");
        map.put("Estonian", "et");
        map.put("Finnish", "fi");
        map.put("French", "fr");
        map.put("German", "de");
        map.put("Greek", "el");
        map.put("Haitian Creole", "ht");
        map.put("Hindi", "hi");
        map.put("Hungarian", "hu");
        map.put("Icelandian", "is");
        map.put("Indonesian", "id");
        map.put("Italian", "it");
        map.put("Japanese", "ja");
        map.put("Korean", "ko");
        map.put("Latvian", "lv");
        map.put("Lithuanian", "lt");
        map.put("Malagasy", "mg");
        map.put("Malay", "ms");
        map.put("Maltese", "mt");
        map.put("Norwegian", "no");
        map.put("Persian", "fa");
        map.put("Polish", "pl");
        map.put("Portuguese", "pt");
        map.put("Romanian", "ro");
        map.put("Slovak", "sk");
        map.put("Slovenian", "sl");
        map.put("Spanish", "es");
        map.put("Swedish", "sv");
        map.put("Telugu", "te");
        map.put("Thai", "th");
        map.put("Turkish", "tr");
        map.put("Ukrainian", "uk");
        map.put("Urdu", "ur");
        map.put("Vietnamese", "vi");
        map.put("Welsh", "cy");

    }

    public String getCode(String country){
        String countryFound = map.get(country);
        if(countryFound==null){
            countryFound="UK";
        }
        return countryFound;
    }
}
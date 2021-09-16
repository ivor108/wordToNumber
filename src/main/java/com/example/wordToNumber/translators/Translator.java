package com.example.wordToNumber.translators;

import java.util.HashMap;

public interface Translator {
    HashMap<String, String> dictionary = new HashMap<>();
    String translate(String textFrom);
    HashMap<String, String> getDictionary();

}

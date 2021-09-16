package com.example.wordToNumber.translators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class WordTranslator implements Translator {

    public WordTranslator(Languages language, String path) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(path + language.toString() + ".csv"))){
            String line;
            while ((line = br.readLine()) != null){
                String[] keyVal = line.split(", ");
                dictionary.put(keyVal[1], keyVal[0]);
            }
        }
    }

    public static String[] rankSplit(String string){
        String[] ranks = new String[4];
        String[] ranksName = new String[3];
        ranksName[0] = "миллиард";
        ranksName[1] = "миллион";
        ranksName[2] = "тысяч";
        Arrays.fill(ranks, "");

        for (int i = 0; i < 3; i++) {
            if(string.matches(".*" + ranksName[i] + ".*")){
                String[] tmp = string.split(" " + ranksName[i] + ".*?\\s");
                ranks[i] = tmp[0];
                if(tmp.length > 1)
                    string = tmp[1];
                else
                    string = "";
            }
        }

        ranks[3] = string;
        return ranks;
    }

    public Integer simpleTranslate(String textFrom) {
        int result = 0;
        if(textFrom.isEmpty())
            return result;
        String[] ranks = textFrom.split(" ");
        for (String rank:ranks){
            if(rank.equals("одна")){
                result += Integer.parseInt(dictionary.get("один"));
                continue;
            }
            result += Integer.parseInt(dictionary.get(rank));
        }


        return result;
    }

    @Override
    public String translate(String textFrom) {
        int result = 0;
        String[] ranks = rankSplit(textFrom);

        for (int i = 0; i < ranks.length; i++) {
            int index = ranks.length - i - 1;
            switch (index) {
                case 0:
                    result += simpleTranslate(ranks[i]);
                    break;
                case 1:
                    result += simpleTranslate(ranks[i]) * 1000;
                    break;
                case 2:
                    result += simpleTranslate(ranks[i]) * 1000000;
                    break;
                case 3:
                    result += simpleTranslate(ranks[i]) * 1000000000;
                    break;
            }
        }

        return Integer.toString(result);
    }

    @Override
    public HashMap<String, String> getDictionary() {
        return dictionary;
    }
}
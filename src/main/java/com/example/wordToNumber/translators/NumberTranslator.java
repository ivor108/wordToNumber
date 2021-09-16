package com.example.wordToNumber.translators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NumberTranslator implements Translator {

    public NumberTranslator(Languages language, String path) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(path + language.toString() + ".csv"))){
            String line;
            while ((line = br.readLine()) != null){
                String[] keyVal = line.split(", ");
                dictionary.put(keyVal[0], keyVal[1]);
            }
        }
    }

    public String[] rankSplit(String string){
        int targetLength = string.length();
        if (string.length()%3 != 0)
            targetLength = (string.length()/3 + 1)*3;

        String formatString = String.format("%0" + targetLength + "d", Long.parseLong(string));
        return formatString.split("(?<=\\G...)");
    }

    public String simpleTranslate(String textFrom) {
        StringBuilder result = new StringBuilder();
        ArrayList<String> ranks = new ArrayList<String>(Arrays.asList(textFrom.split("")));

        if(ranks.get(1).equals("1")){
            ranks.set(2, ranks.get(1) + ranks.get(2));
            ranks.set(1, "0");
        }

        for (int i = 0; i < ranks.size(); i++) {
            int degree = (int) Math.pow(10, i);
            int number = Integer.parseInt(ranks.get(ranks.size() - i - 1)) * degree;
            ranks.set(ranks.size()- i - 1, Integer.toString(number));
        }

        if(Integer.parseInt(textFrom) == 0){
            return dictionary.get("0");
        }
        else {
            for (String rank : ranks) {
                if ((rank.equals("0")))
                    continue;
                result.append(dictionary.get(rank)).append(" ");
            }
        }

        return result.toString().substring(0, result.length() - 1);
    }


    @Override
    public String translate(String textFrom) {
        StringBuilder result = new StringBuilder();
        String[] ranks = rankSplit(textFrom);

        for (int i = 0; i < ranks.length; i++) {
            int index = ranks.length - i - 1;
            int endNumber;
            switch (index) {
                case 0:
                    if (Integer.parseInt(ranks[i]) == 0 && ranks.length > 1)
                        break;
                    result.append(simpleTranslate(ranks[i]));
                    break;
                case 1:
                    if (Integer.parseInt(ranks[i]) == 0)
                        break;
                    endNumber = Integer.parseInt(ranks[i].substring(ranks[i].length() - 2));
                    if (endNumber == 1)
                        result.append("одна тысяча ");
                    else if (endNumber % 10 > 1 && endNumber % 10 < 5 && endNumber / 10 != 1)
                        result.append(simpleTranslate(ranks[i])).append(" тысячи ");
                    else
                        result.append(simpleTranslate(ranks[i])).append(" тысяч ");
                    break;
                case 2:
                    if (Integer.parseInt(ranks[i]) == 0)
                        break;
                    endNumber = Integer.parseInt(ranks[i].substring(ranks[i].length() - 2));
                    if (endNumber == 1)
                        result.append("один миллион ");
                    else if (endNumber % 10 > 1 && endNumber % 10 < 5 && endNumber / 10 != 1)
                        result.append(simpleTranslate(ranks[i])).append(" миллиона ");
                    else
                        result.append(simpleTranslate(ranks[i])).append(" миллионов ");
                    break;
                case 3:
                    if (Integer.parseInt(ranks[i]) == 0)
                        break;
                    endNumber = Integer.parseInt(ranks[i].substring(ranks[i].length() - 2));
                    if (endNumber == 1)
                        result.append("один миллиард ");
                    else if (endNumber % 10 > 1 && endNumber % 10 < 5 && endNumber / 10 != 1)
                        result.append(simpleTranslate(ranks[i])).append(" миллиарда ");
                    else
                        result.append(simpleTranslate(ranks[i])).append(" миллиардов ");
                    break;
            }
        }
        return result.toString();
    }

    @Override
    public HashMap<String, String> getDictionary() {
        return dictionary;
    }
}

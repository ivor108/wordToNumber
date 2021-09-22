package com.example.wordToNumber.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Service
public class RuTranslator implements Translator{


    private final HashMap<String, String> dictionaryNumber = new HashMap<>();
    private final HashMap<String, String> dictionaryWord = new HashMap<>();

    public RuTranslator() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("data/RU.csv");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8))){
            String line;
            while ((line = br.readLine()) != null){
                String[] keyVal = line.split(", ");
                dictionaryNumber.put(keyVal[0], keyVal[1]);
                dictionaryWord.put(keyVal[1], keyVal[0]);
            }
        }
    }

    private String[] rankSplitNumber(String string){
        if(!string.matches("\\d+"))
            return null;
        int targetLength = string.length();
        if (string.length()%3 != 0)
            targetLength = (string.length()/3 + 1)*3;

        String formatString = String.format("%0" + targetLength + "d", Long.parseLong(string));
        return formatString.split("(?<=\\G...)");
    }

    private String simpleTranslateNumber(String textFrom) {
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
            return dictionaryNumber.get("0");
        }
        else {
            for (String rank : ranks) {
                if ((rank.equals("0")))
                    continue;
                result.append(dictionaryNumber.get(rank)).append(" ");
            }
        }

        return result.toString().substring(0, result.length() - 1);
    }

    @Override
    public String translateNumber(String textFrom) {
        StringBuilder result = new StringBuilder();
        String[] ranks = rankSplitNumber(textFrom);

        if(ranks == null)
            return "";

        for (int i = 0; i < ranks.length; i++) {
            int index = ranks.length - i - 1;
            int endNumber;
            switch (index) {
                case 0:
                    if (Integer.parseInt(ranks[i]) == 0 && ranks.length > 1)
                        break;
                    result.append(simpleTranslateNumber(ranks[i]));
                    break;
                case 1:
                    if (Integer.parseInt(ranks[i]) == 0)
                        break;
                    endNumber = Integer.parseInt(ranks[i].substring(ranks[i].length() - 2));
                    if (endNumber % 10 == 1 && endNumber / 10 != 1) {
                        String prefix = ranks[i].substring(0, ranks[i].length() - 1);
                        if (Integer.parseInt(prefix) != 0)
                            result.append(simpleTranslateNumber(prefix + "0")).append(" ");
                        result.append("одна тысяча ");
                    } else if (endNumber % 10 == 2 && endNumber / 10 != 1) {
                        String prefix = ranks[i].substring(0, ranks[i].length() - 1);
                        if (Integer.parseInt(prefix) != 0)
                            result.append(simpleTranslateNumber(prefix + "0")).append(" ");
                        result.append("две тысячи ");
                    } else if (endNumber % 10 > 2 && endNumber % 10 < 5 && endNumber / 10 != 1)
                        result.append(simpleTranslateNumber(ranks[i])).append(" тысячи ");
                    else
                        result.append(simpleTranslateNumber(ranks[i])).append(" тысяч ");
                    break;
                case 2:
                    if (Integer.parseInt(ranks[i]) == 0)
                        break;
                    endNumber = Integer.parseInt(ranks[i].substring(ranks[i].length() - 2));
                    if (endNumber % 10 == 1 && endNumber / 10 != 1) {
                        String prefix = ranks[i].substring(0, ranks[i].length() - 1);
                        if (Integer.parseInt(prefix) != 0)
                            result.append(simpleTranslateNumber(prefix + "0")).append(" ");
                        result.append("один миллион ");
                    } else if (endNumber % 10 > 1 && endNumber % 10 < 5 && endNumber / 10 != 1)
                        result.append(simpleTranslateNumber(ranks[i])).append(" миллиона ");
                    else
                        result.append(simpleTranslateNumber(ranks[i])).append(" миллионов ");
                    break;
                case 3:
                    if (Integer.parseInt(ranks[i]) == 0)
                        break;
                    endNumber = Integer.parseInt(ranks[i].substring(ranks[i].length() - 2));
                    if (endNumber % 10 == 1 && endNumber / 10 != 1) {
                        String prefix = ranks[i].substring(0, ranks[i].length() - 1);
                        if (Integer.parseInt(prefix) != 0)
                            result.append(simpleTranslateNumber(prefix + "0")).append(" ");
                        result.append("один миддиард ");
                    } else if (endNumber % 10 > 1 && endNumber % 10 < 5 && endNumber / 10 != 1)
                        result.append(simpleTranslateNumber(ranks[i])).append(" миллиарда ");
                    else
                        result.append(simpleTranslateNumber(ranks[i])).append(" миллиардов ");
                    break;
            }
        }
        return result.toString();
    }

    private static String[] rankSplitWord(String string){
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

    private BigInteger simpleTranslateWord(String textFrom) {
        int result = 0;
        if(textFrom.isEmpty())
            return BigInteger.valueOf(result);
        String[] ranks = textFrom.split(" ");
        for (String rank:ranks){
            if(rank.equals("одна")){
                result += Integer.parseInt(dictionaryWord.get("один"));
                continue;
            }
            if(rank.equals("две")){
                result += Integer.parseInt(dictionaryWord.get("два"));
                continue;
            }
            if(dictionaryWord.containsKey(rank))
                result += Integer.parseInt(dictionaryWord.get(rank));
        }
        return BigInteger.valueOf(result);
    }

    @Override
    public String translateWord(String textFrom) {
        BigInteger result = new BigInteger("0");
        String[] ranks = rankSplitWord(textFrom);

        for (int i = 0; i < ranks.length; i++) {
            int index = ranks.length - i - 1;
            switch (index) {
                case 0:
                    result = result.add(simpleTranslateWord(ranks[i]));
                    break;
                case 1:
                    result = result.add(simpleTranslateWord(ranks[i]).multiply(new BigInteger("1000")));
                    break;
                case 2:
                    result = result.add(simpleTranslateWord(ranks[i]).multiply(new BigInteger("1000000")));
                    break;
                case 3:
                    result = result.add(simpleTranslateWord(ranks[i]).multiply(new BigInteger("1000000000")));
                    break;
            }
        }
        if(result.equals(new BigInteger("0")) && !textFrom.equals("ноль"))
            return "";

        return result.toString();
    }
}

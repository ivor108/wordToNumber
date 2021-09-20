package com.example.wordToNumber.translators;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class EnTranslator extends Language implements Translator{



    public EnTranslator(String path) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader(path + "EN.csv"))){
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

        int secondRank = Integer.parseInt(ranks.get(1)) * 10;
        ranks.set(1, Integer.toString(secondRank));

        if(Integer.parseInt(textFrom) == 0){
            return dictionaryNumber.get("0");
        }
        else {
            for (int i = 0; i < ranks.size(); i++) {
                if ((ranks.get(i).equals("0")))
                    continue;
                result.append(dictionaryNumber.get(ranks.get(i))).append(" ");
                if(i == 0)
                    result.append("hundred ");
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
                    result.append(simpleTranslateNumber(ranks[i])).append(" thousand ");
                    break;
                case 2:
                    if (Integer.parseInt(ranks[i]) == 0)
                        break;
                    result.append(simpleTranslateNumber(ranks[i])).append(" million ");
                    break;
                case 3:
                    if (Integer.parseInt(ranks[i]) == 0)
                        break;
                    result.append(simpleTranslateNumber(ranks[i])).append(" billion ");
                    break;
            }
        }
        return result.toString();
    }

    private static String[] rankSplitWord(String string){
        String[] ranks = new String[4];
        String[] ranksName = new String[3];
        ranksName[0] = "billion";
        ranksName[1] = "million";
        ranksName[2] = "thousand";
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

        String[] hundreds = textFrom.split(" hundred ");

        if(hundreds.length > 1){
            result += Integer.parseInt(dictionaryWord.get(hundreds[0])) * 100;
            textFrom = hundreds[1];
        }

        String[] ranks = textFrom.split(" ");
        for (String rank : ranks) {
            if (dictionaryWord.containsKey(rank)) {
                result += Integer.parseInt(dictionaryWord.get(rank));
            }
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
        if(result.equals(new BigInteger("0")) && !textFrom.equals("zero"))
            return "";

        return result.toString();
    }
}

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TextCount {
    ArrayList<String> allWordList = new ArrayList<>();

    ArrayList<String> unigram = new ArrayList<>();
    int[] charRepeatFreq = new int[26];
    int repeat = 10;
    String [] uniRepeatString = new String[repeat];
    int[] uniRepeatCount = new int[repeat];
    ArrayList<String> bigram = new ArrayList<>();

    String [] biRepeatString = new String[repeat];
    int[] biRepeatCount = new int[repeat];
    ArrayList<String> trigram = new ArrayList<>();

    String [] triRepeatString = new String[repeat];
    int[] triRepeatCount = new int[repeat];
    long totalCharCount = 0;
    private String fileName;

    public TextCount(String s){
       fileName = s;
       loadFromText(s);
    }
    public void loadFromText(String s){
        try{
            String filePath = "./" + s;
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            BufferedReader readFromFile = new BufferedReader(new FileReader(filePath));
            String lineString = readFromFile.readLine();
            while(lineString != null){
                String[] wordString = lineString.split(" ");
                for (String word : wordString ) {
                    totalCharCount = totalCharCount + word.length();
                    checkShortWord(word);
                    allWordList.add(word.toLowerCase());
                }
                lineString = readFromFile.readLine();
            }
            System.out.println("File Loaded");
            wordRepeats(unigram, uniRepeatString, uniRepeatCount);
            wordRepeats(bigram, biRepeatString, biRepeatCount);
            wordRepeats(trigram, triRepeatString, triRepeatCount);
            findCharFreq();
            printStats();

            readFromFile.close();
        } catch(IOException e){
            System.out.println("File read error.");
        }
    }

    public void checkShortWord(String word) {
        if (word.length() == 1) {
            unigram.add(word.toLowerCase());
        }
        if (word.length() == 2) {
            bigram.add(word.toLowerCase());
        }
        if (word.length() == 3) {
            trigram.add(word.toLowerCase());

        }
    }

    public void wordRepeats(ArrayList<String> wordlist, String[] stringArr, int[] intArr){
        ArrayList<String> tempList = wordlist;
        HashMap<String, Integer> wordMap = new HashMap<>();
        int j = 0;
        for (String word : wordlist){
            wordMap.put(word, wordMap.getOrDefault(word,0) + 1);
        }
        for (int i = 0; i < repeat && wordMap.size() > 0; i++) {
            int max;
            for(int k = 0; k < tempList.size(); k++){
                String testWord = tempList.get(k);
                max = Collections.max(wordMap.values());
                if(wordMap.get(testWord) == max){
                    stringArr[j] = testWord;
                    intArr[j] = max;
                    j++;
                    while(tempList.remove(testWord));
                    wordMap.remove(testWord);
                }
            }
        }
    }

    public void findCharFreq(){
        for(String word : allWordList){
            for (int i = 0; i < word.length(); i++) {
                int charIndex = word.charAt(i) - 97;
                if(charIndex >= 0 && charIndex <= 25){
                    charRepeatFreq[charIndex] = charRepeatFreq[charIndex] + 1;
                }

            }
        }
    }

    public void printCharFreq(){
        for (int i = 0; i < 26; i++) {
            System.out.println((char) ('a' + i) + ": " + charRepeatFreq[i]);
        }
    }

    public void printGrams(){
        System.out.println("Most common unigrams");
        for (int i = 0; i < repeat; i++) {
            System.out.println(uniRepeatString[i] + " : " + uniRepeatCount[i]);
        }
        System.out.println("Most common bigrams");
        for (int i = 0; i < repeat; i++) {
            System.out.println(biRepeatString[i] + " : " + biRepeatCount[i]);
        }
        System.out.println("Most common trigrams");
        for (int i = 0; i < repeat; i++) {
            System.out.println(triRepeatString[i] + " : " + triRepeatCount[i]);
        }
    }

    public void printStats(){
        System.out.println("Total Words : " + allWordList.size());
        System.out.println("One Letter Words : " + unigram.size());
        System.out.println("Two Letter Words : " + bigram.size());
        System.out.println("Three Letter Words : " + trigram.size());
        System.out.println("Total chars : " + totalCharCount);
        printGrams();
        printCharFreq();
    }

}

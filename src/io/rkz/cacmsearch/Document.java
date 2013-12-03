package io.rkz.cacmsearch;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Document : représente un document de la base CACM
 */
public class Document
{
    private int id;
    private ArrayList<String> words;

    public Document(int id, ArrayList<String> words)
    {
        this.id = id;
        if (words != null) this.words = words;
        else this.words = new ArrayList<String>();
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void addWord(String word) {
        words.add(word);
    }

    public void addWords(ArrayList<String> newWords) {
        words.addAll(newWords);
    }

    public HashMap<String, Integer> getWordFrequencies()
    {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (String word : words) {
            if (map.containsKey(word)) map.put(word, map.get(word) + 1);
            else map.put(word, 1);
        }
        return map;
    }
}

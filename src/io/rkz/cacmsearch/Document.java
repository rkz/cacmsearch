package io.rkz.cacmsearch;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a CACM document.
 */
public class Document
{
    private int id;
    private ArrayList<String> words;

    /**
     * Construct a document.
     * @param id
     * @param words
     */
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

    /**
     * Add a single word to the document
     * @param word
     */
    public void addWord(String word) {
        words.add(word);
    }

    /**
     * Add multiple words to the document
     * @param newWords ArrayList of words to add
     */
    public void addWords(ArrayList<String> newWords) {
        words.addAll(newWords);
    }

    /**
     * Get the word frequencies in the document.
     * @return a HashMap of (word -> number of occurences)
     */
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

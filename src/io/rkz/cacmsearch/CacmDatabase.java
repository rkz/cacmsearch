package io.rkz.cacmsearch;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Represents a complete CACM data file.
 */
public class CacmDatabase
{
    private ArrayList<String> stopList;
    private ArrayList<Document> documents;
    private HashMap<Integer, HashMap<String, Integer>> documentIndex;
    private HashMap<String, HashMap<Integer, Integer>> termIndex;

    /**
     * Construct a CacmDatabase by parsing a CACM data file, and build the indexes.
     *
     * @param cacmFileName path to the CACM file
     * @param stopListFileName path to a file containing stopwords (one word per line), or null to use no stop-list
     *
     * @throws IOException if the CACM data file could not be opened. No exception is thrown for the stop-list.
     */
    public CacmDatabase(String cacmFileName, String stopListFileName) throws IOException
    {
        // Load stop list (if provided)
        stopList = new ArrayList<String>();
        if (stopListFileName != null) {
            try {
                loadStopList(stopListFileName);
            }
            catch (IOException e) {}
        }

        // Load documents
        parseFile(cacmFileName);

        // Build indexes
        buildDocumentIndex();
        buildTermIndex();
    }

    /**
     * Return the stop-list words used when parsing the data file.
     * @return an ArrayList with the stop-list's words
     */
    public ArrayList<String> getStopList() {
        return stopList;
    }

    /**
     * Return the documents of the database.
     * @return an ArrayList with the documents
     */
    public ArrayList<Document> getDocuments() {
        return documents;
    }

    /**
     * Returns the document index.
     *
     * @return a <code>HashMap</code> where each document IDs is mapped to a <code>HashMap</code> of the term frequencies
     *      within the document.
     */
    public HashMap<Integer, HashMap<String, Integer>> getDocumentIndex() {
        return documentIndex;
    }

    /**
     * Returns the term index.
     *
     * @return a <code>HashMap</code> where each term is mapped to a <code>HashMap</code> of the term frequencies in
     *      the documents (each document being identified by its document ID in the inner HashMap).
     */
    public HashMap<String, HashMap<Integer, Integer>> getTermIndex() {
        return termIndex;
    }

    /**
     * Returns the {@link WordVector} corresponding to a document. Each coordinate is the number of occurences of the
     * word in the document.
     */
    public WordVector getDocumentWordVector(int docID)
    {
        if (!documentIndex.containsKey(docID)) return null;

        HashMap<String, Integer> docMap = documentIndex.get(docID);
        WordVector vector = new WordVector();
        for (String term : docMap.keySet()) {
            vector.put(term, docMap.get(term) + 0.0);
        }

        return vector;
    }

    /**
     * Loads the stop-list words from a file. Each line should contain exactly one stop-word.
     *
     * @param stopListFile path to the stop-list file
     * @throws IOException if the stop-list file could not be opened
     */
    private void loadStopList(String stopListFile) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(stopListFile));
        String currentLine = "";
        while ((currentLine = br.readLine()) != null) {
            stopList.add(currentLine.trim().toLowerCase());
        }
    }

    /**
     * Loads a CACM data file into the database.
     *
     * @param cacmFileName path to the data file
     * @throws IOException if the data file could not be opened
     */
    private void parseFile(String cacmFileName) throws IOException
    {
        documents = new ArrayList<Document>();

        BufferedReader br = new BufferedReader(new FileReader(cacmFileName));
        String currentLine = "";
        Document currentDoc = null;
        boolean reading = false;

        while ((currentLine = br.readLine()) != null) {

            // Document start (.I <id>)
            if (currentLine.startsWith(".I")) {
                if (currentDoc != null) documents.add(currentDoc);
                int id = Integer.parseInt(currentLine.substring(3).trim());
                currentDoc = new Document(id, null);
            }

            // T, W ou K field: turn reading on, from next line
            else if (currentLine.startsWith(".T") || currentLine.startsWith(".W") || currentLine.startsWith(".K")) {
                reading = true;
            }

            // Other field: do not read until the next field
            else if (currentLine.startsWith(".")) {
                reading = false;
            }

            // Not a field mark: buffer content if reading is turned on
            else if (reading) {
                currentDoc.addWords(tokenizeAndFilter(currentLine));
            }
        }

        if (currentDoc != null) documents.add(currentDoc);
    }

    /**
     * Extracts tokens (words) from a string, suppressing ponctuation and lowering case. Ignores words of the database's
     * stop-list.
     *
     * @param input string to tokenize and filter
     * @return an array of the string's tokens, lower-cased, without stop-words
     */
    private ArrayList<String> tokenizeAndFilter(String input)
    {
        input = input.trim().toLowerCase()
                .replaceAll("[^a-z]", " ")  // replace all non-letters by a space
                .replaceAll("( )+", " ");   // shrink consecutive spaces to 1 space

        // Ignore stop-words
        String[] rawWords = input.split(" ");
        ArrayList<String> words = new ArrayList<String>();
        for (String word : rawWords) {
            if (!stopList.contains(word)) words.add(word);
        }

        return words;
    }

    /**
     * Builds the document index.
     */
    private void buildDocumentIndex()
    {
        documentIndex = new HashMap<Integer, HashMap<String, Integer>>();

        for (Document d : documents)
        {
            HashMap<String, Integer> freq = d.getWordFrequencies();
            documentIndex.put(d.getId(), freq);
        }
    }

    /**
     * Builds the term index.
     */
    private void buildTermIndex()
    {
        termIndex = new HashMap<String, HashMap<Integer, Integer>>();

        for (Document d : documents)
        {
            int docID = d.getId();
            HashMap<String, Integer> freq = d.getWordFrequencies();

            // Add the words to the reverse index
            for (String term : freq.keySet()) {
                Integer frequency = freq.get(term);

                // If necessary, add the word to the global index
                if (!termIndex.containsKey(term)) {
                        termIndex.put(term, new HashMap<Integer, Integer>());
                }

                // Update or insert the document's word frequency
                HashMap<Integer, Integer> termFrequencies = termIndex.get(term);
                if (termFrequencies.containsKey(docID)) {
                        termFrequencies.put(docID, termFrequencies.get(docID) + frequency);
                }
                else {
                    termFrequencies.put(docID, frequency);
                }
            }
        }
    }
}

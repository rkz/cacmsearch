package io.rkz.cacmsearch;

import java.util.HashMap;


/**
 * Pair of index lists of a CACM database.
 *
 * A CacmIndex contains:
 *  - an index: docID -> map(word, frequency) in the document
 *  - a reverse index: word -> map(docID, frequency) in the database
 */
public class CacmIndex
{
    private HashMap<Integer, HashMap<String, Integer>> index;
    private HashMap<String, HashMap<Integer, Integer>> reverseIndex;

    /**
     * Initialize a CacmIndex from given index lists.
     *
     * @param index
     * @param reverseIndex
     */
    public CacmIndex(HashMap<Integer, HashMap<String, Integer>> index,
                          HashMap<String, HashMap<Integer, Integer>> reverseIndex)
    {
        this.index = index;
        this.reverseIndex = reverseIndex;
    }

    /**
     * Initialize a CacmIndex from index lists stored in files.
     *
     * @param indexFile path to the file containing the index list
     * @param reverseIndexFile path to the file containing the reverse index list
     */
    public CacmIndex(String indexFile, String reverseIndexFile)
    {
        // ...
    }

    public HashMap<Integer, HashMap<String, Integer>> getIndex() {
        return index;
    }

    public HashMap<String, HashMap<Integer, Integer>> getReverseIndex() {
        return reverseIndex;
    }
}

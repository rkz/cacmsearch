package io.rkz.cacmsearch;

import java.util.HashMap;


/**
 * Index of a CACM database.
 *
 * <p>A <code>CacmIndex</code> is built either from a <code>CacmDatabase</code> or from index files. Index files are
 * generated from a <code>CacmIndex</code>, allowing to save and load an index without opening the whole database.</p>
 *
 * <p>A <code>CacmIndex</code> is composed of two lists:</p>
 * <ul>
 *     <li>a <strong>document index</strong> which maps each docID to a map of (term, frequency) in the document;</li>
 *     <li>a <strong>term index</strong> which maps each term to a map of (docID, frequency) in the whole database.</li>
 * </ul>
 */
public class CacmIndex
{
    private HashMap<Integer, HashMap<String, Integer>> documentIndex;
    private HashMap<String, HashMap<Integer, Integer>> termIndex;

    /**
     * Initialize a <code>CacmIndex</code> from given index maps. This constructor is usually called by
     * {@link CacmDatabase#getIndex}, to create new indexes.
     *
     * @param documentIndex
     * @param termIndex
     */
    public CacmIndex(HashMap<Integer, HashMap<String, Integer>> documentIndex,
                          HashMap<String, HashMap<Integer, Integer>> termIndex)
    {
        this.documentIndex = documentIndex;
        this.termIndex = termIndex;
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

    public HashMap<Integer, HashMap<String, Integer>> getDocumentIndex() {
        return documentIndex;
    }

    public HashMap<String, HashMap<Integer, Integer>> getTermIndex() {
        return termIndex;
    }

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
}

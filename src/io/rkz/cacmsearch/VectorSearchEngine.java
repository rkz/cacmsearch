package io.rkz.cacmsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Performs vectorial searches over a CacmIndex.
 */
public class VectorSearchEngine
{
    private CacmIndex index;

    /**
     * Construct a VectorSearchEngine over a given index.
     * @param index
     */
    public VectorSearchEngine(CacmIndex index)
    {
        this.index = index;
    }

    /**
     * Perform a search over the index.
     *
     * <p>The search is performed as follows:</p>
     * <ol>
     *     <li>Compute cosines between the query vector and each document (using the document index).</li>
     *     <li>Ignore documents which are orthogonal to the query, i.e. irrelevant.</li>
     *     <li>Sort the remaining documents by descending cosine and return the corresponding {@link SearchMatch} set.</li>
     * </ol>
     *
     * @param query the query as a WordVector (each coordinate represents the number of occurences of the word in the
     *              query string)
     * @return an <code>ArrayList</code> of relevant matches
     */
    public ArrayList<SearchMatch> search(WordVector query)
    {
        ArrayList<SearchMatch> matches = new ArrayList<SearchMatch>();

        // Compute and keep matches with a non-null relevance
        Set<Integer> docIDs = index.getDocumentIndex().keySet();
        for (int docID : docIDs) {
            SearchMatch match = new SearchMatch(docID, WordVector.cos(index.getDocumentWordVector(docID), query));
            if (match.getScore() != 0) matches.add(match);
        }

        // Sort by relevance (most relevant match first)
        Collections.sort(matches);
        Collections.reverse(matches);

        return matches;
    }
}

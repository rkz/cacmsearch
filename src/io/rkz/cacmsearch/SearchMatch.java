package io.rkz.cacmsearch;

/**
 * Reference to a document matching a query with relevance information.
 */
public class SearchMatch implements Comparable<SearchMatch>
{
    private int documentID;
    private double score;

    SearchMatch(int documentID, double score)
    {
        this.documentID = documentID;
        this.score = score;
    }

    public int getDocumentID()
    {
        return documentID;
    }

    public double getScore()
    {
        return score;
    }

    public int compareTo(SearchMatch other)
    {
        if (score < other.score) return -1;
        else if (score > other.score) return 1;
        else return 0;
    }
}

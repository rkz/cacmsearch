package io.rkz.cacmsearch;

/**
 * Boolean expression checking the presence of a term in the document.
 */
public class BooleanTerm implements BooleanExpression
{
    private String term;

    public BooleanTerm(String term)
    {
        this.term = term;
    }

    public boolean eval(WordVector vector)
    {
        return vector.get(term) > 0.0;
    }
}

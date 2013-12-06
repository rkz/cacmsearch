package io.rkz.cacmsearch;

import java.util.HashMap;


/**
 * Vector of a document's weights. Each coordinate is the weight of a particular word in the document.
 */
public class WordVector extends HashMap<String, Double>
{
    /**
     * Get a vector's coordinate. Coordinates on axis not present in the vector's definition are zero.
     */
    public Double get(String axis)
    {
        if (containsKey(axis)) return super.get(axis);
        else return 0.0;
    }

    /**
     * Normalize a WordVector
     * @return a copy of the vector where each coordinate is divided by the vector's norm.
     */
    public WordVector normalize()
    {
        double norm = getNorm();

        WordVector normalized = new WordVector();
        for (String word : keySet())
            normalized.put(word, get(word) / norm);

        return normalized;
    }

    public double getNorm()
    {
        double squareNorm = 0.0;
        for (double x : values())
            squareNorm += x*x;

        return Math.sqrt(squareNorm);
    }

    /**
     * Compute the scalar product of two WordVector's.
     * @param v1 first vector
     * @param v2 second vector
     * @return the value of v1.v2
     */
    static public double scalarProduct(WordVector v1, WordVector v2)
    {
        double product = 0.0;

        // No need to iterate over the two vector's axis, as the axis which are not present in both vectors will not
        // contribute to the scalar product
        for (String word : v1.keySet()) {
            product += v1.get(word) * v2.get(word);
        }

        return product;
    }

    /**
     * Compute the cosine between two WordVector's.
     * @param v1 first vector
     * @param v2 second vector
     * @return cos(v1, v2)
     */
    static public double cos(WordVector v1, WordVector v2)
    {
        return scalarProduct(v1.normalize(), v2.normalize());
    }
}

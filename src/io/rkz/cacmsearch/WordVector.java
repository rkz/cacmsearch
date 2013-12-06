package io.rkz.cacmsearch;

import java.util.HashMap;


/**
 * Vector of a document's weights. Each coordinate is the weight of a particular word in the document.
 */
public class WordVector
{
    private HashMap<String, Double> coordinates;

    /**
     * Initialize a WordVector with the given coordinates.
     * @param coordinates coordinates of the vector (values) on the dimensions provided as keys. The coordinates on
     *                    other dimensions are null.
     */
    public WordVector(HashMap<String, Double> coordinates)
    {
        this.coordinates = coordinates;
    }

    /**
     * Get one coordinate
     * @param axis the word to use as axis
     * @return the vector's coordinate over the given axis, or zero if it has no such coordinate.
     */
    public double getCoordinate(String axis)
    {
        if (coordinates.containsKey(axis)) return coordinates.get(axis);
        else return 0.0;
    }

    /**
     * Normalize a WordVector
     * @return a copy of the vector where each coordinate is divided by the vector's norm.
     */
    public WordVector normalize()
    {
        double norm = getNorm();

        HashMap<String, Double> normalizedCoordinates = new HashMap<String, Double>();
        for (String word : coordinates.keySet())
            normalizedCoordinates.put(word, coordinates.get(word) / norm);

        return new WordVector(normalizedCoordinates);
    }

    public double getNorm()
    {
        double squareNorm = 0.0;
        for (double x : coordinates.values())
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
        for (String word : v1.coordinates.keySet()) {
            product += v1.getCoordinate(word) * v2.getCoordinate(word);
        }

        return product;
    }
}

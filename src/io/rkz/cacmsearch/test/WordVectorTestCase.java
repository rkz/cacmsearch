package io.rkz.cacmsearch.test;

import io.rkz.cacmsearch.WordVector;
import junit.framework.TestCase;

/**
 * Test case for WordVector
 */
public class WordVectorTestCase extends TestCase
{
    public void testCoordinates()
    {
        WordVector c1 = new WordVector();
        c1.put("foo", 3.0);
        c1.put("car", 2.0);
        c1.put("wee", 1.5);

        assertEquals(c1.get("foo"), 3.0);
        assertEquals(c1.get("bar"), 0.0);
    }

    public void testScalarProduct()
    {
        WordVector c1 = new WordVector();
        c1.put("foo", 3.0);
        c1.put("car", 2.0);
        c1.put("wee", 1.5);

        WordVector c2 = new WordVector();
        c2.put("bar", 4.0);
        c2.put("wee", 2.0);
        c2.put("zak", 1.1);

        assertEquals(WordVector.scalarProduct(c1, c2), 3.0);
    }

    public void testNormalize()
    {
        WordVector c1 = new WordVector();
        c1.put("foo", 3.0);
        c1.put("car", 2.0);
        c1.put("wee", 1.5);

        WordVector n1 = c1.normalize();
        assertEquals(Math.floor(n1.getNorm()*1000), 1000.0);
        assertEquals(n1.get("bar"), 0.0);
    }

    public void testCos()
    {
        WordVector c1 = new WordVector();
        c1.put("x", 1.0);

        WordVector c2 = new WordVector();
        c2.put("y", 2.0);
        c2.put("z", 1.0);

        WordVector c3 = new WordVector();
        c3.put("x", 1.0);
        c3.put("y", 1.0);

        assertEquals(WordVector.cos(c1, c2), 0.0);
        assertEquals(Math.floor(WordVector.cos(c1, c3)*1000), Math.floor(Math.sqrt(2)/2*1000));
    }
}

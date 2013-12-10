package io.rkz.cacmsearch.test;

import io.rkz.cacmsearch.BooleanSearchEngine;
import junit.framework.TestCase;
import java.util.ArrayList;

public class BooleanParsingTestCase extends TestCase
{
    public void testTokenize()
    {
        ArrayList<String> tokens = BooleanSearchEngine.tokenize("hello & (world | !people)");

        assertEquals(tokens.get(0), "hello");
        assertEquals(tokens.get(1), "&");
        assertEquals(tokens.get(2), "(");
        assertEquals(tokens.get(3), "world");
        assertEquals(tokens.get(4), "|");
        assertEquals(tokens.get(5), "!");
        assertEquals(tokens.get(6), "people");
        assertEquals(tokens.get(7), ")");
    }
}

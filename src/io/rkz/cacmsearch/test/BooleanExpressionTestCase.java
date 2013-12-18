package io.rkz.cacmsearch.test;

import io.rkz.cacmsearch.*;
import junit.framework.TestCase;

/**
 * Test case for boolean expressions.
 */
public class BooleanExpressionTestCase extends TestCase
{
    WordVector foo;
    WordVector bar;
    WordVector foo_bar;
    WordVector foo_wee;

    public void setUp()
    {
        foo = new WordVector();
        foo.put("foo", 1.0);

        bar = new WordVector();
        bar.put("bar", 1.0);

        foo_bar = new WordVector();
        foo_bar.put("foo", 1.0);
        foo_bar.put("bar", 1.0);

        foo_wee = new WordVector();
        foo_wee.put("foo", 1.0);
        foo_wee.put("wee", 1.0);
    }

    public void testTerm()
    {
        BooleanExpression e = new BooleanTerm("foo");

        assertTrue(e.eval(foo));
        assertTrue(e.eval(foo_bar));
        assertTrue(e.eval(foo_wee));
        assertFalse(e.eval(bar));
    }

    public void testNot()
    {
        BooleanExpression e = new BooleanNot(new BooleanTerm("foo"));

        assertFalse(e.eval(foo));
        assertFalse(e.eval(foo_bar));
        assertTrue(e.eval(bar));
    }

    public void testAnd()
    {
        BooleanExpression e = new BooleanAnd(new BooleanTerm("foo"), new BooleanTerm("bar"));

        assertTrue(e.eval(foo_bar));
        assertFalse(e.eval(foo));
        assertFalse(e.eval(foo_wee));
    }

    public void testOr()
    {
        BooleanExpression e = new BooleanOr(new BooleanTerm("foo"), new BooleanTerm("wee"));

        assertTrue(e.eval(foo_wee));
        assertTrue(e.eval(foo_bar));
        assertTrue(e.eval(foo));
        assertFalse(e.eval(bar));
    }
}

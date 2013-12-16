package io.rkz.cacmsearch.test;

import io.rkz.cacmsearch.*;
import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Arrays;

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

    public void testParse()
    {
        ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(
                "hello", "&", "(", "(", "!", "world", ")", "|", "people", ")"
        ));

        try {
            BooleanExpression expr = BooleanSearchEngine.parseTokenList(tokens);

            BooleanAnd andExpr = (BooleanAnd)expr;

            // check that the first operand is Term("hello")
            BooleanTerm helloTerm = (BooleanTerm)andExpr.getFirst();
            assert(helloTerm.getTerm().equals("hello"));

            // check that the second operand is Or(Not(Term("world")), Term("people"))
            BooleanOr orExpr = (BooleanOr)andExpr.getSecond();

            // or's first operand
            BooleanNot notTerm = (BooleanNot)orExpr.getFirst();
            BooleanTerm notInnerTerm = (BooleanTerm)notTerm.getArgument();
            assert(notInnerTerm.getTerm().equals("world"));

            // or's second operand
            BooleanTerm peopleTerm = (BooleanTerm)orExpr.getSecond();
            assert(peopleTerm.getTerm().equals("people"));
        }
        catch (BooleanSyntaxError e) {
            //noinspection ConstantConditions
            assert(false);
        }
        catch (ClassCastException e) {
            //noinspection ConstantConditions
            assert(false);
        }
    }
}

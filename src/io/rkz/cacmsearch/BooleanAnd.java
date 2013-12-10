package io.rkz.cacmsearch;

/**
 * "And" operator between two boolean expressions.
 */
public class BooleanAnd implements BooleanExpression
{
    BooleanExpression first;
    BooleanExpression second;

    public BooleanAnd (BooleanExpression first, BooleanExpression second)
    {
        this.first = first;
        this.second = second;
    }

    public boolean eval(WordVector vector)
    {
        return first.eval(vector) && second.eval(vector);
    }
}

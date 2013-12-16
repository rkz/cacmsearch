package io.rkz.cacmsearch;

/**
 * "Or" operator between two boolean expressions.
 */
public class BooleanOr implements BooleanExpression
{
    BooleanExpression first;
    BooleanExpression second;

    public BooleanOr (BooleanExpression first, BooleanExpression second)
    {
        this.first = first;
        this.second = second;
    }

    public BooleanExpression getFirst() {
        return first;
    }

    public BooleanExpression getSecond() {
        return second;
    }

    public boolean eval(WordVector vector)
    {
        return first.eval(vector) || second.eval(vector);
    }
}

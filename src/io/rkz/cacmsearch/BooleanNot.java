package io.rkz.cacmsearch;

/**
 * Negation of a boolean expression.
 */
public class BooleanNot implements BooleanExpression
{
    BooleanExpression argument;

    public BooleanNot (BooleanExpression argument)
    {
        this.argument = argument;
    }

    public boolean eval(WordVector vector)
    {
        return !argument.eval(vector);
    }
}

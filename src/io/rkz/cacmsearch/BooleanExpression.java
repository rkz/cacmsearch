package io.rkz.cacmsearch;

/**
 * Interface for boolean expressions or sub-expressions.
 */
public interface BooleanExpression
{
    boolean eval(WordVector query);
}

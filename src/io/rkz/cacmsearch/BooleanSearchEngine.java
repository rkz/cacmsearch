package io.rkz.cacmsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Performs boolean searches over a {@link CacmDatabase}.
 */
public class BooleanSearchEngine
{
    private CacmDatabase database;

    public BooleanSearchEngine(CacmDatabase database)
    {
        this.database = database;
    }

    /**
     * Perform a search using a {@link BooleanExpression} as query.
     * @param query expression against which to check the database's documents.
     * @return
     */
    public ArrayList<SearchMatch> search(BooleanExpression query)
    {
        return new ArrayList<SearchMatch>();
    }

    /**
     * Natural language search.
     */
    public ArrayList<SearchMatch> search(String query)
    {
        // Clean query
        query = query.toLowerCase()
                .replaceAll("^[a-z\\(\\)&\\|! ]", "");  // remove illegal characters

        // Tokenize


        return new ArrayList<SearchMatch>();
    }

    /**
     * Transform a string into an {@link ArrayList} of tokens.
     */
    public static ArrayList<String> tokenize(String input)
    {
        ArrayList<String> result = new ArrayList<String>();

        int currentCategory = -1;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            // Category change: initiate a new token
            if (getTokenCategory(ch) != currentCategory) {
                currentCategory = getTokenCategory(ch);
                result.add("");
            }

            // Append the character to the current (i.e. last) token
            result.set(result.size()-1, result.get(result.size() - 1) + ch);
        }

        // Ignore tokens containing only spaces
        ArrayList<String> resultNoSpaces = new ArrayList<String>();
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).replaceAll(" ", "").length() > 0) resultNoSpaces.add(result.get(i));
        }

        return resultNoSpaces;
    }

    public static int getTokenCategory(char ch)
    {
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("abcdefghijklmnopqrstuvwxyz");
        categories.add("(");
        categories.add(")");
        categories.add("&");
        categories.add("|");
        categories.add("!");
        categories.add(" ");

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).contains("" + ch)) return i;
        }

        return -1;
    }
}

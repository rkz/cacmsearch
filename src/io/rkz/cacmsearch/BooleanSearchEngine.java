package io.rkz.cacmsearch;

import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     * Helper for {@link BooleanSearchEngine#tokenize}. Determines to which category a token belongs.
     * @param ch
     * @return an integer corresponding to the character's token category. Category 0 is for words.
     */
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

    /**
     * Transform a token list into a BooleanExpression.
     */
    public static BooleanExpression parseTokenList(ArrayList<String> tokens) throws BooleanSyntaxError
    {
        /**
         * Isolate first-level groups
         * e.g. <"hello", "&", "(", "world", "|", "people", ")"> -> <<"hello">, <"&">, <"world", "|", "people">>
         */

        int level = 0;  // recursion level
        ArrayList<ArrayList<String>> firstLevelGroups = new ArrayList<ArrayList<String>>();
        ArrayList<String> currentGroup = new ArrayList<String>();

        for (int i = 0; i < tokens.size(); i++)
        {
            String token = tokens.get(i);
            if (token.equals("(")) {
                level++;
                if (level == 1) {  // opening a new first-level group
                    currentGroup.clear();
                }
                if (level > 1) {  // inner bracket
                    currentGroup.add("(");
                }
            }
            else if (token.equals(")")) {
                level--;
                if (level == 0) {  // closing a first-level group
                    firstLevelGroups.add(currentGroup);
                }
                if (level > 0) {  // inner bracket
                    currentGroup.add(")");
                }
                if (level < 0) {  // unbalanced brackets
                    throw new BooleanSyntaxError();
                }
            }
            else {
                if (level == 0) {  // token is a first-level group
                    firstLevelGroups.add(new ArrayList<String>(Arrays.asList(token)));
                }
                else {  // append the token to the current first-level group
                    currentGroup.add(token);
                }
            }
        }

        // Fail if unbalanced brackets
        if (level > 0) {
            throw new BooleanSyntaxError();
        }

        /**
         * Decode the expression according to the number of first-level groups
         */

        // 1 first-level group: single term or wholly parenthesed expression
        if (firstLevelGroups.size() == 1) {
            ArrayList<String> group = firstLevelGroups.get(0);
            if (group.size() == 1) {
                // Single token: must be a term
                String token = group.get(0);
                if (getTokenCategory(token.charAt(0)) == 0) return new BooleanTerm(token);
                else throw new BooleanSyntaxError();
            }
            else {
                // Multiple tokens: recurse
                return parseTokenList(group);
            }
        }

        // 2 first-level groups: "!X"
        else if (firstLevelGroups.size() == 2) {
            // First group must be a "!"
            if (firstLevelGroups.get(0).size() > 1 || !firstLevelGroups.get(0).get(0).equals("!")) {
                throw new BooleanSyntaxError();
            }
            return new BooleanNot(parseTokenList(firstLevelGroups.get(1)));
        }

        // 3 first-leve groups: "X & Y" or "X | Y"
        else if (firstLevelGroups.size() == 3) {
            // Central group must be a single token
            if (firstLevelGroups.get(1).size() > 1) {
                throw new BooleanSyntaxError();
            }

            String centralToken = firstLevelGroups.get(1).get(0);

            if (centralToken.equals("&")) {
                return new BooleanAnd(parseTokenList(firstLevelGroups.get(0)), parseTokenList(firstLevelGroups.get(2)));
            }
            else if (centralToken.equals("|")) {
                return new BooleanOr(parseTokenList(firstLevelGroups.get(0)), parseTokenList(firstLevelGroups.get(2)));
            }
            else {
                throw new BooleanSyntaxError();
            }
        }

        // More than 3 first-level groups
        else {
            throw new BooleanSyntaxError();
        }
    }
}

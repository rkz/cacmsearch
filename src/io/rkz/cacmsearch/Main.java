package io.rkz.cacmsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        CacmReader reader = new CacmReader();

        // Load stop list
        try {
            reader.loadStopList("cacm-2/common_words");
            System.out.format("Loaded stop list (%s words).\n", reader.getStopList().size());
        }
        catch (IOException e) {
            System.err.println("Unable to load stop list.");
        }

        // Parse cacm.all
        try {
            ArrayList<Document> docs = reader.parseFile("cacm-2/cacm.all");
            System.out.format("Parsed CACM database, loaded %d documents.\n", docs.size());
        }
        catch (IOException e) {
            System.err.println("Unable to load CACM database, aborting.");
            return;
        }
    }
}

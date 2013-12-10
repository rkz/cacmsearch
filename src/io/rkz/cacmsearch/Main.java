package io.rkz.cacmsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Main
{
    public static void main(String[] args)
    {
        try {
            // Load database
            Date begin = new Date();
            CacmDatabase db = new CacmDatabase("cacm-2/cacm.all", "cacm-2/common_words");
            Date end = new Date();

            System.out.println(String.format("CACM database loaded: %d documents, %d stop-words in %.2f s.",
                    db.getDocuments().size(),
                    db.getStopList().size(),
                    (end.getTime() - begin.getTime() + 0.0) / 1000));

            // Run a query
            VectorSearchEngine engine = new VectorSearchEngine(db);
            ArrayList<SearchMatch> results = engine.search("binary algorithm simulation");

            // Print the results
            System.out.println(String.format("%d results", results.size()));
            for (int i = 0; i < Math.min(results.size(), 10); i++) {
                int docID = results.get(i).getDocumentID();
                System.out.println(String.format("    %d (relevance=%.2f)",
                        docID,
                        results.get(i).getScore()));
            }

        }
        catch (IOException e) {
            System.err.println(String.format("I/O error: %s", e.getMessage()));
        }
    }
}

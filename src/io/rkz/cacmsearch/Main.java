package io.rkz.cacmsearch;

import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try {
            // Load database
            CacmDatabase db = new CacmDatabase("cacm-2/cacm.all", "cacm-2/common_words");
            System.out.println(String.format("CACM database loaded: %d documents, %d stop-words.",
                    db.getDocuments().size(),
                    db.getStopList().size()));

            // Generate indexes
            CacmIndex idx = db.getIndex();
            System.out.println(String.format("Generated indexes (reverse index size: %d words).",
                    idx.getReverseIndex().size()));

        }
        catch (IOException e) {
            System.err.println(String.format("I/O error: %s", e.getMessage()));
        }
    }
}

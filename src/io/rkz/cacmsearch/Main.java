package io.rkz.cacmsearch;

import java.util.Date;
import java.io.IOException;
import java.util.HashMap;

public class Main
{
    public static void main(String[] args)
    {
        try {
            Date begin = new Date();

            // Load database
            CacmDatabase db = new CacmDatabase("cacm-2/cacm.all", "cacm-2/common_words");
            System.out.println(String.format("CACM database loaded: %d documents, %d stop-words.",
                    db.getDocuments().size(),
                    db.getStopList().size()));

            // Generate indexes
            CacmIndex idx = db.getIndex();
            System.out.println(String.format("Generated indexes (reverse index size: %d words).",
                    idx.getReverseIndex().size()));

            Date end = new Date();

            System.out.println(String.format("Initialization time: %.2f s",
                    (end.getTime() - begin.getTime() + 0.0) / 1000));
        }
        catch (IOException e) {
            System.err.println("Error while loading the CACM database, aborting.");
        }
    }
}

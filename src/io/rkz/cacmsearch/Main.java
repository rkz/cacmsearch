package io.rkz.cacmsearch;

import java.util.Date;
import java.io.IOException;
import java.util.HashMap;

public class Main
{
    public static void main(String[] args)
    {
        try {
            // Test scalar vectors
            HashMap<String, Double> c1 = new HashMap<String, Double>();
            c1.put("zak", 3.0);
            c1.put("bar", 2.0);
            c1.put("wee", 1.5);
            WordVector v1 = new WordVector(c1);

            HashMap<String, Double> c2 = new HashMap<String, Double>();
            c2.put("bar", 4.0);
            c2.put("wee", 2.0);
            c2.put("zak", 1.1);
            WordVector v2 = new WordVector(c2);

            System.out.println(String.format("%f", WordVector.scalarProduct(v1, v2)));
            System.exit(0);

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

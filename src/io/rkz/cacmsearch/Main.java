package io.rkz.cacmsearch;

import java.util.Date;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try {
            Date start = new Date();
            CacmDatabase db = new CacmDatabase("cacm-2/cacm.all", "cacm-2/common_words");
            Date end = new Date();

            System.out.println(String.format("CACM database loaded (%.2f s): %d documents, %d stop-words.",
                    (end.getTime() - start.getTime() + 0.0) / 1000,
                    db.getDocuments().size(),
                    db.getStopList().size()));
        }
        catch (IOException e) {
            System.err.println("Error while loading the CACM database, aborting.");
        }
    }
}

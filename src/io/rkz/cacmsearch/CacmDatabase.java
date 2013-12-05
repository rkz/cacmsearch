package io.rkz.cacmsearch;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * CacmDatabase : représente une base de données CACM complète. Une CacmDatabase se construit à partir d'un fichier
 * CACM qui est parsé.
 */
public class CacmDatabase
{
    private ArrayList<String> stopList;
    private ArrayList<Document> documents;

    /**
     * Construct a CacmDatabase from a CACM text file.
     *
     * @param cacmFileName Path to the CACM file.
     * @param stopListFileName (optional) Path to a file containing stopwords, one per line.
     *
     * @throws IOException if the CACM database could not be opened. No exception is thrown for the stop-list.
     */
    public CacmDatabase(String cacmFileName, String stopListFileName) throws IOException
    {
        // Load stop list (if provided)
        stopList = new ArrayList<String>();
        if (stopListFileName != null) {
            try {
                loadStopList(stopListFileName);
            }
            catch (IOException e) {}
        }

        // Load documents
        parseFile(cacmFileName);
    }

    /**
     * Return the stop-list used for this database
     * @return an ArrayList of the stop-list's words
     */
    public ArrayList<String> getStopList() {
        return stopList;
    }

    /**
     * Return the documents of the database
     * @return an ArrayList of the documents
     */
    public ArrayList<Document> getDocuments() {
        return documents;
    }

    /**
     * Charge la stopList depuis un fichier dont le nom est passé en paramètre.
     */
    private void loadStopList(String stopListFile) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(stopListFile));
        String currentLine = "";
        while ((currentLine = br.readLine()) != null) {
            stopList.add(currentLine.trim().toLowerCase());
        }
    }

    /**
     * Parse un fichier CACM et renvoie la liste de documents correspondante
     */
    private void parseFile(String cacmFileName) throws IOException
    {
        documents = new ArrayList<Document>();

        BufferedReader br = new BufferedReader(new FileReader(cacmFileName));
        String currentLine = "";
        Document currentDoc = null;
        boolean reading = false;

        while ((currentLine = br.readLine()) != null) {

            // Début de document (.I <id>)
            if (currentLine.startsWith(".I")) {
                if (currentDoc != null) documents.add(currentDoc);
                int id = Integer.parseInt(currentLine.substring(3).trim());
                currentDoc = new Document(id, null);
            }

            // Début de champ T, W ou K : activer la lecture à partir de la prochaine ligne
            else if (currentLine.startsWith(".T") || currentLine.startsWith(".W") || currentLine.startsWith(".K")) {
                reading = true;
            }

            // Autre champ : pas de lecture jusqu'au prochain champ
            else if (currentLine.startsWith(".")) {
                reading = false;
            }

            // Pas de champ : lire si demandé
            else if (reading) {
                currentDoc.addWords(tokenizeAndFilter(currentLine));
            }
        }

        if (currentDoc != null) documents.add(currentDoc);
    }

    /**
     * Extrait les tokens ("mots") d'une ligne en supprimant la ponctuation et les majuscules. Supprime aussi les mots
     * de la stop list.
     */
    private ArrayList<String> tokenizeAndFilter(String input)
    {
        input = input.trim().toLowerCase()
                .replaceAll("[^a-z]", " ")  // remplacer tout ce qui n'est pas une lettre par un espace
                .replaceAll("( )+", " ");   // réduire les espaces consécutifs à 1 espace

        // Supprimer les mots de la stoplist
        String[] rawWords = input.split(" ");
        ArrayList<String> words = new ArrayList<String>();
        for (String word : rawWords) {
            if (!stopList.contains(word)) words.add(word);
        }

        return words;
    }
}

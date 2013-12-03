package io.rkz.cacmsearch;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * CacmReader : lit un fichier cacm et renvoie une liste de documents
 */
public class CacmReader
{
    private ArrayList<String> stopList;

    public CacmReader()
    {
        stopList = new ArrayList<String>();
    }

    /**
     * Charge la stopList depuis un fichier dont le nom est passé en paramètre.
     */
    public void loadStopList(String stopListFile) throws IOException
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
    public ArrayList<Document> parseFile(String cacmFileName) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(cacmFileName));

        String currentLine = "";
        Document currentDoc = null;
        ArrayList<Document> documents = new ArrayList<Document>();
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

        return documents;
    }

    /**
     * Extrait les tokens ("mots") d'une ligne en supprimant la ponctuation et les majuscules. Supprime aussi les mots
     * de la stop list.
     */
    public ArrayList<String> tokenizeAndFilter(String input)
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

    public ArrayList<String> getStopList() {
        return stopList;
    }
}

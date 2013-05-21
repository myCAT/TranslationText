/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This file is part of myCAT.
 *
 * myCAT is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * myCAT is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with myCAT. If not, see <http://www.gnu.org/licenses/>.
 *
 *********
 */
package org.olanto.TranslationText.client;

import com.google.gwt.user.client.Window;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Encore des utilitaires ...
 */
public class Utility {

    public Utility() {
    }

    public static int getInd(int curpos, int[][] lines) {
        boolean notfound = true;
        int pos = 0;
        while ((notfound) && (pos < lines.length - 1)) {
            if ((curpos >= lines[pos][1]) && (curpos < lines[pos + 1][1])) {
                notfound = false;
            } else {
                pos++;
            }
        }
        return pos;
    }

    public static int getln(int pos, int lastpos, int[][] lines) {
        int ln = 0;
        for (int i = pos; i < lastpos; i++) {
            ln += lines[i][0];
        }
        return ln;
    }

    public static ArrayList<String> getQueryWords(String query, ArrayList<String> stopWords) {
        String Query = query;
        ArrayList<String> hits = new ArrayList<String>();
        Query = Query.replace("\"", "");
        Query = Query.replace("(", "");
        Query = Query.replace(")", "");
        Query = Query.replace(",", "");
        Query = Query.replace("'", " ");
        Query = Query.replace("`", " ");
        Query = Query.replace("’", " ");
        Query = Query.replace("‘", " ");
        Query = Query.replace("#", " ");
        Query = Query.replace("“", " ");
        Query = Query.replace("”", " ");

        if (Query.startsWith("QL(")) { // complex query
            hits.add("xxx$$$xxx"); // pas de recherche
            return hits;
        }
        //Sinon requête avec des mots, donc enlever les mots clés et les mots vides
        String[] words = Query.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if ((!stopWords.contains(words[i].toLowerCase()))
                    && !(words[i].equalsIgnoreCase("NEAR"))
                    && !(words[i].equalsIgnoreCase("AND"))
                    && !(words[i].equalsIgnoreCase("OR"))
                    && !(words[i].equalsIgnoreCase("QUOTATION"))) {
                hits.add(words[i].toLowerCase());
            }
        }
        return hits;
    }

    public static String browseRequest(String Query) {
        String query = "";
        if (Query.length() <= 1) {
            query = "*";
        } else if (Query.length() > 1) {
            query = Query.substring(1);
        }
        return query;
    }

    public static int getIndex(String[] source, String cookie) {
        int i = 0;
        for (int j = 0; j < source.length; j++) {
            if (source[j].equalsIgnoreCase(cookie)) {
                i = j;
                break;
            }
        }
        return i;
    }

    public static ArrayList<String> getPatternWords(String Query, ArrayList<String> stopWords) {
        ArrayList<String> hits = new ArrayList<String>();

        //Sinon requête avec des mots, donc enlever les mots clés et les mots vides
        String[] words = Query.split("\\s+");
        hits.addAll(Arrays.asList(words));
        return hits;
    }

    public static ArrayList<String> getWildCharQueryWords(String[] words, ArrayList<String> stopWords) {
        ArrayList<String> hits = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
            if ((!stopWords.contains(words[i].toLowerCase()))
                    && !(words[i].equalsIgnoreCase("NEAR"))
                    && !(words[i].equalsIgnoreCase("AND"))
                    && !(words[i].equalsIgnoreCase("OR"))
                    && !(words[i].equalsIgnoreCase("QUOTATION"))) {
                hits.add(words[i]);
            }
        }
        return hits;
    }

    public static ArrayList<String> getRefWords(String refText) {
        refText = refText.toLowerCase();// may be origin of a problem in the MyRef
        ArrayList<String> hits = new ArrayList<String>();
        String[] words = refText.split("\\s+");
        hits.addAll(Arrays.asList(words));
//        Window.alert("Hits : "+hits.size());
        return hits;
    }

    public static ArrayList<String> getexactWords(String Query) {
        Query = Query.replace("\"", "");
        Query = Query.replace("(", "");
        Query = Query.replace(")", "");
        Query = Query.replace(",", "");
        Query = Query.replace("'", " ");
        Query = Query.replace("`", " ");
        Query = Query.replace("’", " ");
        Query = Query.replace("‘", " ");
        Query = Query.replace("#", " ");
        Query = Query.replace("“", " ");
        Query = Query.replace("”", " ");
        Query = Query.toLowerCase();
        ArrayList<String> hits = new ArrayList<String>();
        String[] words = Query.split("\\s+");
        hits.addAll(Arrays.asList(words));
//        Window.alert("Hits : "+hits.size());
        return hits;
    }

    public static String queryParser(String queryo, String langS, String langT, ArrayList<String> stopWords, ArrayList<String> collections) {
        String Query = queryo.trim();
        String query;
        String qt = Query;
        if (qt.startsWith("\"")) {
            if (qt.endsWith("\"")) {
                query = "QUOTATION(" + qt + ")";
            } else {
                query = "QUOTATION(" + qt + "\")";
            }
        } else if (qt.startsWith("#\"")) {
            if (qt.endsWith("\"")) {
                query = "QUOTATION(" + qt.substring(1) + ")";
            } else {
                query = "QUOTATION(" + qt.substring(1) + "\")";
            }
        } else if ((qt.contains("QL(")) || (qt.contains("QL ("))) {
            int l = Query.lastIndexOf(")");
            int f = Query.indexOf("(") + 1;
            query = Query.substring(f, l);
        } else if ((qt.contains(" AND ")) || (qt.contains(" OR ")) || (qt.startsWith("\""))) {
            String[] words = Query.split("\\s+");
            String q = "";
            for (int i = 0; i < words.length; i++) {
                if ((words[i].equals("AND")) || (words[i].equals("OR"))
                        || (!stopWords.contains(words[i].toLowerCase())) || (words[i].endsWith("\""))) {
                    q += " " + words[i] + " ";
                }
            }
            int k = q.length() - 1;
            query = q.substring(0, k);
        } else if (qt.contains(" NEAR ")) { // normalement deux arguments
            String q = "NEAR (";
            String[] words = Query.split("\\s+");
            int i = 0, j = 0;
            while ((i < words.length) && (j < 2)) {
                if (words[i].equalsIgnoreCase("NEAR")) {
                } else {
                    if (!stopWords.contains(words[i].toLowerCase())) {
                        q += "\"" + words[i] + "\",";
                        j++;
                    }
                }
                i++;
            }
            int k = q.length() - 1;
            query = q.substring(0, k) + ")";
        } else {
            String q = "QUOTATION(\"";
            String[] words = Query.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                if (!stopWords.contains(words[i].toLowerCase())) {
                    q += words[i] + " ";
                }
            }
            int k = q.length() - 1;
            query = q.substring(0, k) + "\")";
        }
        String seleColl = "";
        if (!collections.isEmpty()) {
            seleColl = "\"COLLECTION." + collections.get(0) + "\"";
            for (int k = 1; k < collections.size(); k++) {
                seleColl += " ORL \"COLLECTION." + collections.get(k) + "\"";
            }
            seleColl += " ANDL ";
        }
        String IN_Bitext = " IN[" + seleColl + "\"SOURCE." + langS + "\""
                + " ANDL \"TARGET." + langT + "\"]";

        String IN_Monotext = " IN[" + seleColl + "\"SOURCE." + langS + "\"" + "]";

        if (GuiConstant.BITEXT_ONLY) {
            return query + IN_Bitext;
        }
        return query + IN_Monotext;
    }

// unused replaced by the general method above
    public static String queryQuoteParser(String Quote, String langS, String langT, ArrayList<String> stopWords, ArrayList<String> collections) {
        String query;
        String q = "QUOTATION(\"";
        String[] words = Quote.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (!stopWords.contains(words[i].toLowerCase())) {
                q += words[i] + " ";
            }
        }
        int i = q.length() - 1;
        query = q.substring(0, i) + "\")";

        String seleColl = "";
        if (!collections.isEmpty()) {
            seleColl = "\"COLLECTION." + collections.get(0) + "\"";
            for (int k = 1; k < collections.size(); k++) {
                seleColl += " ORL \"COLLECTION." + collections.get(k) + "\"";
            }
            seleColl += " ANDL ";
        }
        String IN_Bitext = " IN[" + seleColl + "\"SOURCE." + langS + "\""
                + " ANDL \"TARGET." + langT + "\"]";

        String IN_Monotext = " IN[" + seleColl + "\"SOURCE." + langS + "\"" + "]";

        if (GuiConstant.BITEXT_ONLY) {
            return query + IN_Bitext;
        }
        return query + IN_Monotext;
    }

    public static String[] getCollections(ArrayList<String> collections) {
        String[] selectedColls = new String[collections.size()];
        for (int k = 0; k < collections.size(); k++) {
            selectedColls[k] = "COLLECTION." + collections.get(k);
        }
        return selectedColls;
    }

    public static String getDocListElement(String docListItem) {
        String documentName;
        String collectionPath;

        int i = docListItem.lastIndexOf("¦") + 1;
        int j = docListItem.length();
        documentName = docListItem.substring(i, j - 4);

        docListItem = docListItem.substring(0, i);

        i = docListItem.indexOf("¦");
        j = docListItem.length();
        collectionPath = docListItem.substring(0, i) + "/";

        ++i;
        if ((i <= j) && (i >= 1)) {
            docListItem = docListItem.substring(i);
        }

        i = docListItem.indexOf("¦");

        if (i >= 1) {
            collectionPath += docListItem.substring(0, i);
        }
//        System.out.println(collectionPath);

        return documentName + "-" + collectionPath;
    }

    public static ArrayList<String> getDocumentlist(String docs, String separator) {
        if (docs.length() > 2) {
            ArrayList<String> hits = new ArrayList<String>();
            String longName, docName, listElem;
            String[] words = docs.split("\\" + separator);
            for (int i = 0; i < words.length; i++) {
                int lastslash = words[i].lastIndexOf("/") - 2;
                longName = words[i].substring(lastslash);
                docName = getDocListElement(longName.substring(3));
                listElem = docName + "¦]" + "[¦" + longName;
                hits.add(listElem);
            }
//        Window.alert("Hits : " + hits.size() + " " + hits.toString());
            return hits;
        } else {
            return null;
        }
    }

    public static String wildCharQueryParser(String[] words, String langS, String langT, ArrayList<String> stopWords, ArrayList<String> collections) {
        String query, q = "";
        for (int i = 0; i < words.length; i++) {
            if (!stopWords.contains(words[i].toLowerCase())) {
                q += "\"" + words[i] + "\" OR ";
            }
        }
        int k = q.length() - 4;
        query = q.substring(0, k);
        String seleColl = "";
        if (!collections.isEmpty()) {
            seleColl = "\"COLLECTION." + collections.get(0) + "\"";
            for (int c = 1; c < collections.size(); c++) {
                seleColl += " ORL \"COLLECTION." + collections.get(c) + "\"";
            }
            seleColl += " ANDL ";
        }
        String IN = " IN[" + seleColl + "\"SOURCE." + langS + "\""
                + " ANDL \"TARGET." + langT + "\"]";

        return query + IN;
    }
}

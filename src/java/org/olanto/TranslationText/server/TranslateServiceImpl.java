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
package org.olanto.TranslationText.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.olanto.TranslationText.client.*;
import org.olanto.idxvli.ref.UploadedFile;
import org.olanto.idxvli.server.IndexService_MyCat;
import org.olanto.idxvli.server.QLResultNice;
import org.olanto.idxvli.server.REFResultNice;
import org.olanto.mapman.server.AlignBiText;
import org.olanto.senseos.SenseOS;

/**
 * implémentation des services du GUI
 */
@SuppressWarnings("serial")
public class TranslateServiceImpl extends RemoteServiceServlet implements TranslateService {

    // add usage of the implementation of the new mycatServer methods: order by and originalpath
    public static IndexService_MyCat is;
    private AlignBiText Align;
    public static String home = SenseOS.getMYCAT_HOME();
    public static Properties prop;
    public static ConstStringManager stringMan;
    public static String REGEX_BEFORE_TOKEN;
    public static String REGEX_AFTER_TOKEN;
    public static GwtProp CONST = null;
    public static boolean RELOAD_PARAM_ON = true;

    @Override
    public String myMethod(String s) {
        // Do something interesting with 's' here on the server.

        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
            s += " / " + is.getInformation();
        } catch (Exception ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "Server says: Alive";
    }

    GwtAlignBiText SetGwtAlignBiText(GwtSegDoc src, GwtSegDoc tgt, GwtIntMap map, String Q) {
        GwtAlignBiText result = new GwtAlignBiText();
        result.source = src;
        result.target = tgt;
        result.map = map;
        result.query = Q;
        return result;

    }

    GwtIntMap SetGwtIntMap(int[] from, int[] to) {
        GwtIntMap result = new GwtIntMap();
        result.from = from;
//        System.out.println("-----------------FROM------------------ ");
//        for (int i = 0; i < from.length; i++) {
//            System.out.println("SRC phrase: " + i + " --> " + from[i]);
//        }
        result.to = to;
//        System.out.println("-----------------TO------------------ ");
//        for (int i = 0; i < to.length; i++) {
//            System.out.println("TGT phrase: " + i + " --> " + to[i]);
//        }
        return result;

    }

    GwtSegDoc SetGwtSegDoc(int[][] lines, int nblines, String content, String uri, String lang, int ncal) {
        GwtSegDoc result = new GwtSegDoc();
        result.positions = lines;
//        for (int i = 0; i < lines.length; i++) {
//            System.out.println("--------------------------");
//            System.out.println("nombre de lignes dans la textarea de la phrase: " + i + " = " + lines[i][0]);
//            System.out.println("position du curseur pour la phrase: " + i + " = " + lines[i][1]);
//            System.out.println("correction pour la position du curseur pour IE de la phrase: " + i + " = " + lines[i][2]);
//            System.out.println("nombre de lignes avant la phrase: " + i + " = " + lines[i][3]);
//            System.out.println("nombre de phrases comportant une seule ligne jusqu'à la phrase: " + i + " = " + lines[i][4]);
//        }
//        System.out.println("original lines: "+lines[2109][2]);
//        System.out.println("Copie result: "+result.positions[2109][2]);
        result.nblines = nblines;
        result.content = content;
        result.uri = uri;
        result.lang = lang;
        result.Ncal = ncal;
        return result;
    }

    @Override
    public GwtAlignBiText getContent(String file, String langS, String langT, String Query, int w, int h) {
//        System.out.println("calling the server getContent. File = " + file);
        Align = new AlignBiText(file, langS, langT, Query, w, h);
        GwtSegDoc src = SetGwtSegDoc(Align.source.positions, Align.source.nblines, Align.source.content, Align.source.uri, Align.source.lang, Align.source.Ncal);
        GwtSegDoc tgt = SetGwtSegDoc(Align.target.positions, Align.target.nblines, Align.target.content, Align.target.uri, Align.target.lang, Align.target.Ncal);
        GwtIntMap map = SetGwtIntMap(Align.map.from, Align.map.to);
        return SetGwtAlignBiText(src, tgt, map, Align.query);
    }

    @Override
    public ArrayList<String> getDocumentList(String query, ArrayList<String> collections, boolean PATH_ON, int maxSize, String order) {
        ArrayList<String> documents = new ArrayList<>();
        String longName, docName, listElem;
//        System.out.println("Before calling the server for documents with the query: " + query);
        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
//            Timer t1 = new Timer("------------- " + query);
            QLResultNice res = is.evalQLNice(query, 0, maxSize, order);
            if (res.docname != null) {
//                System.out.println("List of documents retrieved");
                if (!collections.isEmpty()) {
//                    System.out.println("____________________Triés par collections & "+order+"____________________");
                    for (int s = 0; s < collections.size(); s++) {
//                        System.out.println("Collection: " + collections.get(s));
                        for (int i = 0; i < res.docname.length; i++) {//res.result or res.docname
                            int lastslash = res.docname[i].lastIndexOf("/") - 2;
                            longName = res.docname[i].substring(lastslash);
                            if (longName.contains(collections.get(s))) {
//                                System.out.println("Docname: " + res.docname[i]);
                                docName = getDocListElement(longName.substring(3), PATH_ON);
                                listElem = docName + "¦]" + "[¦" + longName;
                                if (!documents.contains(listElem)) {
                                    documents.add(listElem);
                                }
                            }
                        }
                    }
                } else {
//                    System.out.println("____________________ Sorted by " + order + "____________________");
                    for (int i = 0; i < res.docname.length; i++) {//res.result or res.docname
                        int lastslash = res.docname[i].lastIndexOf("/") - 2;
                        longName = res.docname[i].substring(lastslash);
                        docName = getDocListElement(longName.substring(3), PATH_ON);
                        listElem = docName + "¦]" + "[¦" + longName;
                        documents.add(listElem);
//                        System.out.println("Docname: " + res.docname[i]);
                    }
                }
            }
//            t1.stop();
        } catch (RemoteException ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documents;
    }

    private CollectionTree CollToGWTColl(CollTree s) {
        if (s != null) {
            CollectionTree s1 = new CollectionTree();
//            System.out.println("adding collectionTree");
            s1.id = s.id;
            s1.idUp = s.idUp;
            s1.currFolder = s.currFolder;
            s1.upperFolder = s.upperFolder;
            s1.level = s.level;
            s1.isEndOfCollection = s.isEndOfCollection;
            s1.childNumber = s.debug();
            s1.ChildFolders = new ArrayList<>();

            if (!s.ChildFolders.isEmpty()) {
                for (CollTree a : s.ChildFolders) {
//                    System.out.println("adding new map for collectionTree: " + a.id);
                    s1.ChildFolders.add(CollToGWTColl(a));
                }
            }
            return s1;
        } else {
            return null;
        }
    }

    @Override
    public CollectionTree SetCollection() {

        String[] list = getCollectionList();
        CollTree s = new CollTree("", 0, "", "1", false);
        s.ChildFolders = new ArrayList<>();
        for (String word : list) {
            int start = word.indexOf(".") + 1;
            word = word.substring(start);
            boolean end = false;
            String[] folders = word.split("\\¦");
            String Upper;
            String Current;
            CollTree a;
            if (folders.length == 1) {
                end = true;
            }
            if (!s.SubcontainsFolder(folders[0], 0, "")) {
                a = new CollTree(folders[0], 0, "", "", end);
                s.ChildFolders.add(a);
            } else {
                a = s.Subcontains(folders[0], 0, "¦" + folders[0]);
            }
            String pa = "¦" + folders[0];
            Upper = folders[0];
            for (int i = 1; i < folders.length; i++) {
                end = false;
//                System.out.println("\n\nadding new entry \n\n");
                Current = folders[i];
                a = a.getCollectionSubTree(Upper, i - 1, pa);
                if (i == (folders.length - 1)) {
                    end = true;
                }
                addFolder(Upper, Current, i, a, pa, end);
                Upper = Current;
                pa += "¦" + Current;
            }
        }

        CollectionTree s1 = CollToGWTColl(s);
//        System.out.println("*** Debug tree contents ***");
//        System.out.println(s.currFolder + " (level " + s.level + ")");
//        int size = s.debug();
//        System.out.println("*** End debug: size: " + size);

        return s1;
    }

    //add folders to the tree
    static void addFolder(String Upper, String current, int lev, CollTree temp, String upperId, boolean end) {
//        System.out.println("*** addFolder ***");
//        System.out.println("Adding folder " + Upper + "/" + current + " ...");

        if ((!temp.SubcontainsFolder(current, lev, upperId)) && (temp.level == (lev - 1))) {
//            System.out.println("adding '" + current + "' at level " + (temp.level + 1));
            CollTree a = new CollTree(current, temp.level + 1, temp.id, temp.currFolder, end);
            temp.ChildFolders.add(a);
//            System.out.println("Added Successfully '" + a.currFolder + "' at level " + a.level);
//            System.out.println("*** Debug tree contents");
//            System.out.println(temp.currFolder + " (level " + temp.level + ")");
//            temp.debug();
//            System.out.println("*** End debug");

        }

    }

    private String[] getCollectionList() {
        String[] Coll = null;

        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
            Coll = is.getDictionnary("COLLECTION.").result;
//            System.out.println("succeded getting collections");
        } catch (RemoteException ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

//        for (int i = 0; i < Coll.length; i++) {
//            System.out.println(Coll[i]);
//        }
        return Coll;
    }

    private String getDocListElement(String docListItem, boolean PATH_ON) {
        String documentName;
        String collectionPath;

        int i = docListItem.lastIndexOf("¦") + 1;
        int j = docListItem.length();

        // Strict name of the document without any path indication
        documentName = docListItem.substring(i, j - 4);
        docListItem = docListItem.substring(0, i);
        // Replace the highens by the slash in the rest of the path: DocName-Path
        if ((PATH_ON) && (docListItem.contains("¦"))) {
            collectionPath = docListItem.replace("¦", "/");
//            System.out.println(collectionPath);
            return documentName + "-" + collectionPath;
        } else {
            return documentName;
        }
    }

    @Override
    public ArrayList<String> getStopWords() {
        ArrayList<String> stopWords = new ArrayList<>();

        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
            String[] stpwd = is.getStopWords();
            stopWords.addAll(Arrays.asList(stpwd));
//            System.out.println("succeded getting stop words ");
        } catch (RemoteException ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return stopWords;
    }

    @Override
    public String[] getCorpusLanguages() {
        String[] languages = null;

        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
            languages = is.getCorpusLanguages();
//            System.out.println("succeded getting langues");
        } catch (RemoteException ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return languages;
    }

    @Override
    public int[][] getQueryWordsPos(int[][] positions, String content, ArrayList<String> Query, int queryLn) {
        ArrayList<String> Pos = new ArrayList<>();
        ArrayList<Integer> startPos = new ArrayList<>();
        ArrayList<Integer> lastPos = new ArrayList<>();
        int begin, end;
        String sentence, res, curHit, regex;
        Pattern p;
        Matcher m;
        boolean allfound;
//        System.out.println("Query: " + Query.size());
        for (int i = 0; i < positions.length; i++) {
            allfound = true;
            begin = positions[i][1];
            if (i == (positions.length - 1)) {
                end = content.length();
            } else {
                end = positions[i + 1][1] + 1;
            }
            sentence = content.substring(begin, end);

//            System.out.println("looking into sentence # " + i);
            int j = 0, start, len;
            startPos.clear();
            lastPos.clear();
            while ((allfound) && (j < Query.size())) {

                curHit = Query.get(j);
                len = curHit.length();
                regex = REGEX_BEFORE_TOKEN + curHit + REGEX_AFTER_TOKEN;
                p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                m = p.matcher(sentence);
                if (m.find()) {
                    start = m.start();
                    if (Query.size() > 1) {
                        if (j == 0) {
//                            System.out.println("start found at : " + start);
                            startPos.add(start);
                            while (m.find()) {
                                start = m.start();
                                startPos.add(start);
//                                System.out.println("Start found at : " + start);
                            }
                        }
                        if (j == Query.size() - 1) {
//                            System.out.println("last found at : " + start);
                            lastPos.add(start + len);
                            while (m.find()) {
                                start = m.start();
                                lastPos.add(start + len);
//                                System.out.println("last found at : " + start);
                            }
                        }
                    } else {
                        startPos.add(start);
//                        System.out.println("Start found at : " + start);
                        lastPos.add(start + len);
//                        System.out.println("Last found at : " + start);
                    }
                } else {
                    allfound = false;
                }
                j++;
            }
            if (allfound) {
                boolean done = false;
                int s = 0, k = 0, r = 0;
                while ((!done) && (s < lastPos.size())) {
                    k = lastPos.get(s);
                    for (int n = 0; n < startPos.size(); n++) {
                        r = startPos.get(n);
                        if (((k - r) <= 2 * queryLn) && ((k - r) >= 0)) {
                            done = true;
                            break;
                        }
                    }
                    s++;
                }
                if (done) {
                    res = i + "¦" + r + "¦" + k;
                    Pos.add(res);
                }
            }
        }
//        for (int s = 0; s < Pos.size(); s++) {
//            System.out.println("Line: " + Pos.get(s));
//        }
//        System.out.println(" All Found Occurrences# 1: " + Pos.size());
        return getPositions(Pos);
    }

    private int[][] getPositions(ArrayList<String> Pos) {
        int[][] posit;
        int i, k, pos, ln, j, len;
        String curr;
        if (!Pos.isEmpty()) {
            posit = new int[Pos.size()][3];
            for (int s = 0; s < Pos.size(); s++) {
                curr = Pos.get(s);
                i = curr.indexOf("¦");
                j = curr.lastIndexOf("¦");
                k = curr.length();
                pos = Integer.parseInt(curr.substring(0, i));
                ln = Integer.parseInt(curr.substring(i + 1, j));
                ln = (ln > 0) ? ln + 1 : ln;
                len = Integer.parseInt(curr.substring(j + 1, k));

                posit[s][0] = pos; // index de la ligne qui contient le mot
                posit[s][1] = ln; // index du mot dans la phrase
                posit[s][2] = len - ln + 1; // longueur à highlighter
            }
//            System.out.println(" All Found Occurrences# 2: " + posit.length);
//            for (int ls = 0; ls < posit.length; ls++) {
//                System.out.println("Line: " + posit[ls][0] + " Start: " + posit[ls][1]);
//            }
        } else {
            posit = new int[1][1];
            posit[0][0] = -1;
        }
        return posit;
    }

    @Override
    public String getOriginalUrl(String docName) {
        int lastslash = docName.lastIndexOf("/") - 2;
        docName = docName.substring(lastslash);
        String url = "";
        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
            url = is.getOriginalUrl(docName);
            System.out.println("URL ORiginal: " + url);
        } catch (Exception ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return url;
    }

    public GwtRef ReftoGwtRef(REFResultNice ref) {
        GwtRef refL = new GwtRef();
        refL.DOC_REF_SEPARATOR = REFResultNice.DOC_REF_SEPARATOR;
        refL.htmlref = ref.htmlref;
        refL.listofref = ref.listofref;
        refL.nbref = ref.nbref;
        refL.reftext = ref.reftext;
        return refL;
    }

    @Override
    public GwtRef getHtmlRef(String Content, String fileName, int minCons, String langS, String LangT, ArrayList<String> collections, String QDFileExtension) {
        String ref;
        GwtRef gref = null;
        String[] co;
//        System.out.println("uploaded file:" + fileName);
//        System.out.println("Content:" + Content);
        if (fileName.contains(QDFileExtension)) {
            gref = html2GwtRef(Content);
        } else {
            if (is == null) {
                is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
            }
            UploadedFile up = new UploadedFile(Content, fileName);
//        System.out.println(up.getFileName() + "\n" + up.getContentString());
            try {
//                Timer t1 = new Timer("-------------  ref ");

                co = getCollections(collections);

//            System.out.println("calling references service: " + is.getInformation());
                ref = is.getHtmlReferences(up, minCons, langS, LangT, co);
//                t1.stop();
                if (ref != null) {
//                    System.out.println(ref);
                    gref = html2GwtRef(ref);
                }
            } catch (RemoteException ex) {
                Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return gref;
    }

    private String[] getCollections(ArrayList<String> collections) {
        if ((collections != null) && (!collections.isEmpty())) {
            String[] selectedColls = new String[collections.size()];
            for (int k = 0; k < collections.size(); k++) {
                selectedColls[k] = "COLLECTION." + collections.get(k);
            }
            return selectedColls;
        } else {
            return null;
        }
    }

    private ArrayList<String> GetSubList(ArrayList<String> list, int index) {
        ArrayList<String> res = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            res.add(list.get(i));
        }
        return res;
    }

    @Override
    public int[][] getRefWordsPos(String content, ArrayList<String> query, int queryLn, float reFactor, int minRefLn) {
        ArrayList<String> Query;
        String regex;
        int refLength = (int) (((reFactor * queryLn) > minRefLn) ? (reFactor * queryLn) : minRefLn);
        if (query.size() > 1000) {
            System.out.println("word list bigger than 1000, looking or the first 1000 words");
            Query = GetSubList(query, 1000);
        } else {
            Query = query;
        }
        ArrayList<String> Pos = new ArrayList<>();
        ArrayList<Integer> startPos = new ArrayList<>();
        ArrayList<Integer> lastPos = new ArrayList<>();
        String first, res, last;
        Pattern p;
        Matcher m;
        startPos.clear();
        lastPos.clear();

        first = Query.get(0);
        last = Query.get(Query.size() - 1);
        regex = REGEX_BEFORE_TOKEN + first + REGEX_AFTER_TOKEN;
        p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        m = p.matcher(content);
        if (m.find()) {
//            System.out.println("start found at : " + m.start());
            startPos.add(m.start());
            while (m.find()) {
                startPos.add(m.start());
//                System.out.println("Start found at : " + m.start());
            }
        }
        regex = REGEX_BEFORE_TOKEN + last + REGEX_AFTER_TOKEN;
        p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        m = p.matcher(content);
        if (m.find()) {
//            System.out.println("last found at : " + m.start());
            lastPos.add(m.start() + last.length());
            while (m.find()) {
                lastPos.add(m.start() + last.length());
//                System.out.println("last found at : " + m.start());
            }
        }
        int startp, lastp;
        for (int s = 0; s < startPos.size(); s++) {
            startp = startPos.get(s);
            for (int l = 0; l < lastPos.size(); l++) {
                lastp = lastPos.get(l);
//                System.out.println("refLength: " + refLength);
                if (((lastp - startp) >= queryLn) && ((lastp - startp) <= refLength)) {
                    if (getAllWords(content.substring(startp, lastp + 1), Query)) {
                        res = startp + "¦" + (lastp - startp);
                        Pos.add(res);
                    }
                }
            }
        }

//        for (int i = 0; i < Pos.size(); i++) {
//            System.out.println("Positions found in Line: " + Pos.get(i));
//        }
        return getPositionsRef(Pos);
    }

    public boolean getAllWords(String content, ArrayList<String> Query) {
        String curHit;
        boolean allfound = true;
        int j = 1;
        while ((allfound) && (j < Query.size() - 1)) {
            curHit = Query.get(j);
//            System.out.println("test if: " + curHit + " is in :" + content);
            if (content.contains(curHit)) {
                j++;
            } else {
                allfound = false;
            }
        }
//        System.out.println("All found : " + allfound);
        return allfound;
    }

    private int[][] getPositionsRef(ArrayList<String> Pos) {
        int[][] posit;
        int i, k, l, r;
        String curr;
        if (!Pos.isEmpty()) {
            posit = new int[Pos.size()][2];
            for (int s = 0; s < Pos.size(); s++) {
                curr = Pos.get(s);
                i = curr.indexOf("¦");
                k = curr.length();
                l = Integer.parseInt(curr.substring(0, i));
                l = (l > 0) ? l + 1 : l;
                r = Integer.parseInt(curr.substring(i + 1, k));
                posit[s][0] = l;
                if (l == 0) {
                    posit[s][1] = r + 2;
                } else {
                    posit[s][1] = r + 1;
                }
            }
//            System.out.println(" All Found Occurrences# 2: " + posit.length);
//            for (int ls = 0; ls < posit.length; ls++) {
//                System.out.println("Line: " + posit[ls][0] + " Start: " + posit[ls][1]);
//            }
        } else {
            posit = new int[1][1];
            posit[0][0] = -1;
        }
        return posit;
    }

    @Override
    public int[][] getQueryWordsPosAO(int[][] positions, String content, ArrayList<String> Query, int queryLn) {
        ArrayList<String> Pos = new ArrayList<>();
        int begin, end, j;
        String sentence, hit, regex;
        Pattern p;
        Matcher m;
        for (int i = 0; i < positions.length; i++) {
            begin = positions[i][1];
            if (i == (positions.length - 1)) {
                end = content.length();
            } else {
                end = positions[i + 1][1] + 1;
            }
            sentence = content.substring(begin, end);
            j = 0;
            while (j < Query.size()) {
                hit = Query.get(j);
//                System.out.println("looking for: " + hit);
                regex = REGEX_BEFORE_TOKEN + hit + REGEX_AFTER_TOKEN;
                p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                m = p.matcher(sentence);
                while (m.find()) {
                    Pos.add(i + "¦" + m.start() + "¦" + hit.length());
                }
                j++;
                m.reset();
            }
        }
        return getPositionsAO(Pos);
    }

    @Override
    public int[][] getHitPosNearCR(String content, ArrayList<String> Query, int queryLn, float reFactor, int sepNumber, int avgTokenLn) {
        String regex;
        int refLength = (int) (reFactor * (queryLn + sepNumber * avgTokenLn));
        ArrayList<String> Pos = new ArrayList<>();
        ArrayList<Integer> startPos = new ArrayList<>();
        ArrayList<Integer> lastPos = new ArrayList<>();
        String first, res, last;
        Pattern p;
        Matcher m;
        startPos.clear();
        lastPos.clear();

        first = Query.get(0);
        last = Query.get(Query.size() - 1);
        regex = REGEX_BEFORE_TOKEN + first + REGEX_AFTER_TOKEN;
        p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        m = p.matcher(content);
        if (m.find()) {
//            System.out.println("start found at : " + m.start());
            startPos.add(m.start());
            while (m.find()) {
                startPos.add(m.start());
//                System.out.println("Start found at : " + m.start());
            }
        }
        regex = REGEX_BEFORE_TOKEN + last + REGEX_AFTER_TOKEN;
        p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        m = p.matcher(content);
        if (m.find()) {
//            System.out.println("last found at : " + m.start());
            lastPos.add(m.start() + last.length());
            while (m.find()) {
                lastPos.add(m.start() + last.length());
//                System.out.println("last found at : " + m.start());
            }
        }
        int startp, lastp;
        for (int s = 0; s < startPos.size(); s++) {
            startp = startPos.get(s);
            for (int l = 0; l < lastPos.size(); l++) {
                lastp = lastPos.get(l);
//                System.out.println("refLength: " + refLength);
                if ((Math.abs(lastp - startp) >= queryLn) && (Math.abs(lastp - startp) <= refLength)) {
                    res = startp + "¦" + (lastp - startp);
                    Pos.add(res);
                }
            }
        }

//        for (int i = 0; i < Pos.size(); i++) {
//            System.out.println("Positions found in Line: " + Pos.get(i));
//        }
        return getPositionsRef(Pos);
    }

    @Override
    public int[][] getHitPosNear(int[][] positions, String content, ArrayList<String> Query, int queryLn, int sepNumber, int avgTokenLn) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private int[][] getPositionsAO(ArrayList<String> Pos) {
        int[][] posit;
        int i, k, pos, ln, j, len;
        String curr;
        if (!Pos.isEmpty()) {
            posit = new int[Pos.size()][3];
            for (int s = 0; s < Pos.size(); s++) {
                curr = Pos.get(s);
                i = curr.indexOf("¦");
                j = curr.lastIndexOf("¦");
                k = curr.length();
                pos = Integer.parseInt(curr.substring(0, i));
                ln = Integer.parseInt(curr.substring(i + 1, j));
                ln = (ln > 0) ? ln + 1 : ln;
                len = Integer.parseInt(curr.substring(j + 1, k));

                posit[s][0] = pos; // index de la ligne qui contient le mot
                posit[s][1] = ln; // index du mot dans la phrase
                posit[s][2] = len; // longueur à highlighter
            }
        } else {
            posit = new int[1][1];
            posit[0][0] = -1;
        }
        return posit;
    }

    public GwtRef html2GwtRef(String htmlref) {
        GwtRef refL = new GwtRef();
        if (htmlref.contains("<!--MYQUOTEREF")) {
            String comments = htmlref.substring(htmlref.indexOf("<!--MYQUOTEREF"), htmlref.indexOf("MYQUOTEREF-->"));
            refL.nbref = getRefNumber(comments);
            refL.DOC_REF_SEPARATOR = REFResultNice.DOC_REF_SEPARATOR;
            refL.htmlref = htmlref;
            getRefDocText(refL, comments, refL.DOC_REF_SEPARATOR);
        }
        // treat this properly!!!
        return refL;
    }

    private int getRefNumber(String comments) {
        int i = 0;
        if (!(comments.isEmpty())) {
            if (comments.contains("0|")) {
                String number = comments.substring(15, comments.indexOf("0|") - 1);
                if ((!(number.isEmpty())) && (number.matches("^\\d+"))) {
                    i = Integer.parseInt(number);
//                    System.out.println("Ref number = " + i);
                }
            }
        }
        return i;
    }

    private void getRefDocText(GwtRef refL, String lines, String separator) {
        if ((!(lines.isEmpty())) && (refL.nbref > 0)) {
            if (lines.contains("0|")) {
                String curlines = lines.substring(lines.indexOf("0"));
                refL.listofref = new String[refL.nbref];
                refL.reftext = new String[refL.nbref];
                int j = 0;
                String[] Lines = curlines.split("[\n]+");
                for (int i = 0; i < Lines.length; i++) {
                    if ((!(Lines[i].isEmpty())) && (Lines[i].matches("(^\\d+)(.*)")) && (Lines[i].contains(separator))) {
                        curlines = Lines[i].substring(Lines[i].indexOf(separator) + 1);
                        if ((!(curlines.isEmpty())) && (curlines.contains(separator))) {
                            refL.reftext[j] = curlines.substring(0, curlines.indexOf(separator));
                            refL.listofref[j] = curlines.substring(curlines.indexOf(separator) + 1);
//                            System.out.println("reference " + i + " text " + refL.reftext[j]);
//                            System.out.println("reference " + i + " list " + refL.listofref[j]);
                            j++;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String[] getExpandTerms(String wildQuery) {
        String[] terms = null;
        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
            terms = is.ExpandTerm(wildQuery);
//            System.out.println("succeded getting wild char query terms: "+terms.length);
        } catch (RemoteException ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return terms;
    }

    @Override
    public ArrayList<String> getDocumentBrowseList(String request, String LangS, ArrayList<String> collections, boolean PATH_ON, int maxBrowse, String order, boolean ONLY_ON_FILE_NAME) {
        ArrayList<String> documents = new ArrayList<>();
        String longName, docName, listElem;
        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
//            Timer t1 = new Timer("------------- " + query);
            QLResultNice res = is.browseNice(request, LangS, 0, maxBrowse, getCollections(collections), order, ONLY_ON_FILE_NAME);

            if (res.docname != null) {
                if (!collections.isEmpty()) {
                    for (int s = 0; s < collections.size(); s++) {
                        for (int i = 0; i < res.docname.length; i++) {//res.result or res.docname
                            int lastslash = res.docname[i].lastIndexOf("/") - 2;
                            longName = res.docname[i].substring(lastslash);
                            if (longName.contains(collections.get(s))) {
                                docName = getDocListElement(longName.substring(3), PATH_ON);
                                listElem = docName + "¦]" + "[¦" + longName;
                                if (!documents.contains(listElem)) {
                                    documents.add(listElem);
                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < res.docname.length; i++) {//res.result or res.docname
                        int lastslash = res.docname[i].lastIndexOf("/") - 2;
                        longName = res.docname[i].substring(lastslash);
                        docName = getDocListElement(longName.substring(3), PATH_ON);
                        listElem = docName + "¦]" + "[¦" + longName;
                        documents.add(listElem);
                    }
                }
            }
        } catch (RemoteException ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documents;
    }

    @Override
    public GwtProp InitPropertiesFromFile() {
        if ((CONST == null) || (RELOAD_PARAM_ON)) {
            String fileName = SenseOS.getMYCAT_HOME() + "/config/GUI_fix.xml";
            System.out.println("found properties file:" + fileName);
            FileInputStream f = null;
            try {
                f = new FileInputStream(fileName);
            } catch (Exception e) {
                System.out.println("cannot find properties file:" + fileName);
                Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, e);
            }
            try {
                prop = new Properties();
                prop.loadFromXML(f);
                RELOAD_PARAM_ON = Boolean.valueOf(prop.getProperty("RELOAD_PARAM_ON", "false"));
                REGEX_BEFORE_TOKEN = prop.getProperty("REGEX_BEFORE_TOKEN", "([^a-zA-Z0-9]|[\\s\\p{Punct}\\r\\n\\(\\{\\[\\)\\}\\]]|^)");
                REGEX_AFTER_TOKEN = prop.getProperty("REGEX_AFTER_TOKEN", "([^a-zA-Z0-9\\-\\_\\/]|[\\s\\p{Punct}\\r\\n\\)\\}\\]\\(\\{\\[]|$)");
//                prop.list(System.out);
                InitProperties();
            } catch (Exception e) {
                System.out.println("errors in properties file:" + fileName);
                Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, e);
            }
//        System.out.println("Success getting all properties, object sent to the client");
            return CONST;
        } else {
            return CONST;
        }
    }

    private void InitProperties() {
        CONST = new GwtProp();
        String propPath = prop.getProperty("INTERFACE_MESSAGE_PATH");
        String interLang = prop.getProperty("INTERFACE_MESSAGE_LANG");

        CONST.TA_TEXTAREA_WIDTH = Integer.parseInt(prop.getProperty("TA_TEXTAREA_WIDTH"));
        CONST.TA_TEXTAREA_HEIGHT = Integer.parseInt(prop.getProperty("TA_TEXTAREA_HEIGHT"));
        CONST.QD_TEXTAREA_HEIGHT = Integer.parseInt(prop.getProperty("QD_TEXTAREA_HEIGHT"));
        CONST.QD_HTMLAREA_HEIGHT = Integer.parseInt(prop.getProperty("QD_HTMLAREA_HEIGHT"));
        CONST.DOC_LIST_WIDTH = Integer.parseInt(prop.getProperty("DOC_LIST_WIDTH"));
        CONST.DOC_LIST_HEIGHT = Integer.parseInt(prop.getProperty("DOC_LIST_HEIGHT"));
        CONST.QD_DOC_LIST_HEIGHT = Integer.parseInt(prop.getProperty("QD_DOC_LIST_HEIGHT"));
        CONST.ORIGINAL_ON = Boolean.valueOf(prop.getProperty("ORIGINAL_ON", "true"));
        CONST.PATH_ON = Boolean.valueOf(prop.getProperty("PATH_ON", "true"));
        CONST.AUTO_ON = Boolean.valueOf(prop.getProperty("AUTO_ON", "false"));
        CONST.FILE_NAME_RIGHT = Boolean.valueOf(prop.getProperty("FILE_NAME_RIGHT", "false"));
        CONST.ONLY_ON_FILE_NAME = Boolean.valueOf(prop.getProperty("ONLY_ON_FILE_NAME", "false"));
        CONST.BITEXT_ONLY = Boolean.valueOf(prop.getProperty("BITEXT_ONLY", "false"));
        CONST.SAVE_ON = Boolean.valueOf(prop.getProperty("SAVE_ON", "true"));
        CONST.MAXIMIZE_ON = Boolean.valueOf(prop.getProperty("MAXIMIZE_ON", "true"));
        CONST.TA_HILITE_OVER_CR = Boolean.valueOf(prop.getProperty("TA_HILITE_OVER_CR", "false"));
        CONST.EXP_DAYS = Integer.parseInt(prop.getProperty("EXP_DAYS"));
        CONST.MAX_RESPONSE = Integer.parseInt(prop.getProperty("MAX_RESPONSE"));
        CONST.MAX_BROWSE = Integer.parseInt(prop.getProperty("MAX_BROWSE"));
        CONST.MAX_SEARCH_SIZE = Integer.parseInt(prop.getProperty("MAX_SEARCH_SIZE"));
        CONST.MIN_OCCU = Integer.parseInt(prop.getProperty("MIN_OCCU"));
        CONST.MAX_OCCU = Integer.parseInt(prop.getProperty("MAX_OCCU"));
        CONST.CHARACTER_WIDTH = Integer.parseInt(prop.getProperty("CHARACTER_WIDTH"));
        CONST.JOBS_ITEMS = prop.getProperty("JOBS_ITEMS");
        CONST.TEXT_ALIGNER_LBL = prop.getProperty("TEXT_ALIGNER_LBL");
        CONST.QUOTE_DETECTOR_LBL = prop.getProperty("QUOTE_DETECTOR_LBL");
        CONST.QD_FILE_EXT = prop.getProperty("QD_FILE_EXT");
        CONST.QD_HELP_URL = prop.getProperty("QD_HELP_URL");
        CONST.TA_HELP_URL = prop.getProperty("TA_HELP_URL");
        CONST.LOGO_PATH = prop.getProperty("LOGO_PATH");
        CONST.LOGO_URL = prop.getProperty("LOGO_URL");
        CONST.W_OPEN_FEATURES = prop.getProperty("W_OPEN_FEATURES");
        CONST.OLANTO_URL = prop.getProperty("OLANTO_URL");
        CONST.TA_DL_SORTBY = prop.getProperty("TA_DL_SORTBY");
        CONST.FEEDBACK_MAIL = prop.getProperty("FEEDBACK_MAIL");
        CONST.REF_FACTOR = Float.parseFloat(prop.getProperty("REF_FACTOR"));
        CONST.REF_MIN_LN = Integer.parseInt(prop.getProperty("REF_MIN_LN"));
        CONST.PP_H_MIN = Integer.parseInt(prop.getProperty("PP_H_MIN"));
        CONST.PP_H_MAX = Integer.parseInt(prop.getProperty("PP_H_MAX"));
        CONST.TA_NEAR_AVG_TERM_CHAR = Integer.parseInt(prop.getProperty("TA_NEAR_AVG_TERM_CHAR", "6"));
        CONST.NEAR_DISTANCE = Integer.parseInt(prop.getProperty("NEAR_DISTANCE", "8"));
        /**
         * **********************************************************************************
         */
        try {
            if (interLang.isEmpty()) {
                stringMan = new ConstStringManager(home + propPath + ".properties");
            } else {
                stringMan = new ConstStringManager(home + propPath + "_" + interLang + ".properties");
            }
            CONST.TA_BTN_SRCH = stringMan.get("ta.btn.srch");
            CONST.TA_BTN_NXT = stringMan.get("ta.btn.nxt");
            CONST.TA_BTN_PVS = stringMan.get("ta.btn.pvs");
            CONST.TA_BTN_OGN = stringMan.get("ta.btn.ogn");
            CONST.TA_BTN_ALGN = stringMan.get("ta.btn.algn");
            CONST.TA_BTN_SAVE = stringMan.get("ta.btn.save");
            CONST.TA_BTN_SEARCH = stringMan.get("ta.btn.search");
            CONST.TA_BTN_CCL = stringMan.get("ta.btn.ccl");
            CONST.WIDGET_BTN_SUBMIT = stringMan.get("widget.btn.submit");
            CONST.WIDGET_BTN_COLL_ON = stringMan.get("widget.btn.collection.on");
            CONST.WIDGET_BTN_COLL_OFF = stringMan.get("widget.btn.collection.off");
            CONST.WIDGET_BTN_QD = stringMan.get("widget.btn.qd");
            CONST.WIDGET_BTN_HELP = stringMan.get("widget.btn.help");
            CONST.WIDGET_BTN_TA = stringMan.get("widget.btn.ta");
            CONST.WIDGET_BTN_QD_NXT = stringMan.get("widget.btn.qd.nxt");
            CONST.WIDGET_BTN_QD_PVS = stringMan.get("widget.btn.qd.pvs");
            CONST.WIDGET_LBL_QD_LN = stringMan.get("widget.label.qd.length");
            CONST.WIDGET_BTN_QD_SAVE = stringMan.get("widget.btn.ta.save");
            CONST.WIDGET_BTN_TA_SAVE = stringMan.get("widget.btn.qd.save");
            CONST.WIDGET_LIST_TA_SBY = stringMan.get("widget.list.ta.sortby");
            CONST.WIDGET_COLL_WND = stringMan.get("widget.coll.wnd");
            CONST.WIDGET_COLL_SET = stringMan.get("widget.coll.set");
            CONST.WIDGET_COLL_CLOSE = stringMan.get("widget.coll.close");
            CONST.WIDGET_COLL_CLEAR = stringMan.get("widget.coll.clear");
            /**
             * **********************************************************************************
             */
            CONST.MSG_1 = stringMan.get("widget.MSG_1");
            CONST.MSG_2 = stringMan.get("widget.MSG_2");
            CONST.MSG_3 = stringMan.get("widget.MSG_3");
            CONST.MSG_4 = stringMan.get("widget.MSG_4");
            CONST.MSG_5 = stringMan.get("widget.MSG_5");
            CONST.MSG_6 = stringMan.get("widget.MSG_6");
            CONST.MSG_7 = stringMan.get("widget.MSG_7");
            CONST.MSG_8 = stringMan.get("widget.MSG_8");
            CONST.MSG_9 = stringMan.get("widget.MSG_9");
            CONST.MSG_10 = stringMan.get("widget.MSG_10");
            CONST.MSG_11 = stringMan.get("widget.MSG_11");
            CONST.MSG_12 = stringMan.get("widget.MSG_12");
            CONST.MSG_13 = stringMan.get("widget.MSG_13");
            CONST.MSG_14 = stringMan.get("widget.MSG_14");
            CONST.MSG_15 = stringMan.get("widget.MSG_15");
            CONST.MSG_16 = stringMan.get("widget.MSG_16");
            CONST.MSG_17 = stringMan.get("widget.MSG_17");
            CONST.MSG_18 = stringMan.get("widget.MSG_18");
            CONST.MSG_19 = stringMan.get("widget.MSG_19");
            CONST.MSG_20 = stringMan.get("widget.MSG_20");
            CONST.MSG_21 = stringMan.get("widget.MSG_21");
            CONST.MSG_22 = stringMan.get("widget.MSG_22");
            CONST.MSG_23 = stringMan.get("widget.MSG_23");
            CONST.MSG_24 = stringMan.get("widget.MSG_24");
            CONST.MSG_25 = stringMan.get("widget.MSG_25");
            CONST.MSG_26 = stringMan.get("widget.MSG_26");
            CONST.MSG_27 = stringMan.get("widget.MSG_27");
            CONST.MSG_28 = stringMan.get("widget.MSG_28");
            CONST.MSG_29 = stringMan.get("widget.MSG_29");
            CONST.MSG_30 = stringMan.get("widget.MSG_30");
            CONST.MSG_31 = stringMan.get("widget.MSG_31");
            CONST.MSG_32 = stringMan.get("widget.MSG_32");
            CONST.MSG_33 = stringMan.get("widget.MSG_33");
            CONST.MSG_34 = stringMan.get("widget.MSG_34");
            CONST.MSG_35 = stringMan.get("widget.MSG_35");
            CONST.MSG_36 = stringMan.get("widget.MSG_36");
            CONST.MSG_37 = stringMan.get("widget.MSG_37");
            CONST.MSG_38 = stringMan.get("widget.MSG_38");
            CONST.MSG_39 = stringMan.get("widget.MSG_39");
            CONST.MSG_40 = stringMan.get("widget.MSG_40");
            CONST.MSG_41 = stringMan.get("widget.MSG_41");
            CONST.MSG_42 = stringMan.get("widget.MSG_42");
            CONST.MSG_43 = stringMan.get("widget.MSG_43");
            CONST.MSG_44 = stringMan.get("widget.MSG_44");
            CONST.MSG_45 = stringMan.get("widget.MSG_45");
            CONST.MSG_46 = stringMan.get("widget.MSG_46");
            CONST.MSG_47 = stringMan.get("widget.MSG_47");
            CONST.MSG_48 = stringMan.get("widget.MSG_48");
            CONST.MSG_49 = stringMan.get("widget.MSG_49");
            CONST.MSG_50 = stringMan.get("widget.MSG_50");
            CONST.MSG_51 = stringMan.get("widget.MSG_51");
            CONST.MSG_52 = stringMan.get("widget.MSG_52");
            CONST.MSG_53 = stringMan.get("widget.MSG_53");
            CONST.MSG_54 = stringMan.get("widget.MSG_54");
            CONST.MSG_55 = stringMan.get("widget.MSG_55");
            CONST.MSG_56 = stringMan.get("widget.MSG_56");
            CONST.MSG_57 = stringMan.get("widget.MSG_57");
            CONST.MSG_58 = stringMan.get("widget.MSG_58");
            CONST.MSG_59 = stringMan.get("widget.MSG_59");
            CONST.MSG_60 = stringMan.get("widget.MSG_60");
            CONST.MSG_61 = stringMan.get("widget.MSG_61");
            CONST.MSG_62 = stringMan.get("widget.MSG_62");
            CONST.MSG_63 = stringMan.get("widget.MSG_63");
        } catch (IOException ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String createTempFile(String FileName, String Content) {
        if (is == null) {
            is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
        }
        try {
            return is.createTemp(FileName, Content);
        } catch (RemoteException ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String createTempZip(String fileName) {
        return UtilsFiles.byte2fileString(buildZipfromFiles(fileName), getZipFileName(fileName));
    }

    private byte[] buildZipfromFiles(String fileName) {
        try {
            System.out.println("fileName:" + fileName);
            int lastslash = fileName.lastIndexOf("/");

            String fName = fileName.substring(lastslash);
            System.out.println("fName:" + fName);
            String[] languages;
            byte[] cont;
            String filePath, FileN;
            if (is == null) {
                is = org.olanto.conman.server.GetContentService.getServiceMYCAT("rmi://localhost/VLI");
            }

            languages = is.getCorpusLanguages();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);
            for (int i = 0; i < languages.length; i++) {
                System.out.println("Request to download file : " + languages[i] + fName);
                filePath = is.getOriginalPath(languages[i] + fName);
                System.out.println("File original Path : " + filePath);
                cont = is.getByte(filePath);
//                FileN = filePath.replace("\\", "_").replace("/", "_").replace(":", "");
                FileN = is.getOriginalZipName(languages[i] + fName);  // JG modif
                if (cont != null) {
//                    System.out.write(cont, 0, cont.length);
                    System.out.println("creating a new zip entry ! : " + FileN);
                    zos.putNextEntry(new ZipEntry(FileN));
                    zos.write(cont);
                    zos.closeEntry();
                }
            }
            zos.flush();
            baos.flush();
            zos.close();
            baos.close();
            System.out.println("Zip of files created successfully");
            return baos.toByteArray();

        } catch (IOException ex) {
            Logger.getLogger(TranslateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String getZipFileName(String fileName) {
        int lastslash = fileName.lastIndexOf("/") - 2;
        String fName = fileName.substring(lastslash);
        String zipPath;
        zipPath = fName.substring(3, fName.length() - 4).replace("¦", "_") + ".zip";   //JG modif
        return zipPath;
    }
}

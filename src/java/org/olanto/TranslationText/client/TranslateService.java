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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;

/**
 * les services pour le GUI
 */
@RemoteServiceRelativePath("translateservice")
public interface TranslateService extends RemoteService {

    public String myMethod(String s);

    public ArrayList<String> getDocumentList(String Query, ArrayList<String> collections, boolean PATH_ON, int maxSize, String order, boolean exact, boolean number);

    public ArrayList<String> getDocumentBrowseList(String request, String LangS, ArrayList<String> collections, boolean PATH_ON, int maxBrowse, String order, boolean ONLY_ON_FILE_NAME);

    public GwtAlignBiText getContent(String File1, String langS, String langT, String Query, int w, int h);

    public CollectionTree SetCollection();

    public ArrayList<String> getStopWords();

    public String getOriginalUrl(String docName);

    public GwtProp InitPropertiesFromFile(String cookieLang);

    public int[][] getQueryWordsPos(int[][] positions, String content, ArrayList<String> Query, int queryLn);

    public int[][] getHitPosNearCR(String content, ArrayList<String> Query, int queryLn, float reFactor, int sepNumber, int avgTokenLn);

    public int[][] getHitPosNear(int[][] positions, String content, ArrayList<String> Query, int queryLn, float reFactor, int sepNumber, int avgTokenLn);

    public int[][] getQueryWordsPosAO(int[][] positions, String content, ArrayList<String> Query, int queryLn);

    public int[][] getRefWordsPos(String content, ArrayList<String> Query, int queryLn, float reFactor, int minRefLn);

    public GwtRef getHtmlRef(String Content, String fileName, int minCons, String langS, String LangT, ArrayList<String> collections, String QDFileExtension);

    public String[] getCorpusLanguages();

    public String[] getExpandTerms(String wildQuery);

    public String createTempFile(String FileName, String Content);

    public String createTempZip(String FileName);
}

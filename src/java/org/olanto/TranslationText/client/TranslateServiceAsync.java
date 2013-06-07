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

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;

/**
 * les services pour le GUI
 */
public interface TranslateServiceAsync {

    public void getContent(String File1, String langS, String langT, String Query, int w, int h, AsyncCallback<GwtAlignBiText> asyncCallback);

    public void myMethod(String s, AsyncCallback<String> callback);

    public void getDocumentList(String Query, ArrayList<String> collections, boolean PATH_ON, int maxSize, String order, boolean exact, boolean number, AsyncCallback<ArrayList<String>> asyncCallback);

    public void getDocumentBrowseList(String request, String LangS, ArrayList<String> collections, boolean PATH_ON, int maxBrowse, String order, boolean ONLY_ON_FILE_NAME, AsyncCallback<ArrayList<String>> asyncCallback);

    public void SetCollection(AsyncCallback<CollectionTree> asyncCallback);

    public void getStopWords(AsyncCallback<ArrayList<String>> asyncCallback);

    public void getOriginalUrl(String docName, AsyncCallback<String> asyncCallback);

    public void getQueryWordsPos(int[][] positions, String content, ArrayList<String> Query, int queryLn, AsyncCallback<int[][]> asyncCallback);

    public void getHitPosNearCR(String content, ArrayList<String> Query, int queryLn, float reFactor, int sepNumber, int avgTokenLn, AsyncCallback<int[][]> asyncCallback);

    public void getHitPosNear(int[][] positions, String content, ArrayList<String> Query, int queryLn, float reFactor, int sepNumber, int avgTokenLn, AsyncCallback<int[][]> asyncCallback);

    public void getQueryWordsPosAO(int[][] positions, String content, ArrayList<String> Query, int queryLn, AsyncCallback<int[][]> asyncCallback);

    public void getRefWordsPos(String content, ArrayList<String> Query, int queryLn, float reFactor, int minRefLn, AsyncCallback<int[][]> asyncCallback);

    public void getHtmlRef(String content, String fileName, int minCons, String langS, String LangT, ArrayList<String> collections, String QDFileExtension, AsyncCallback<GwtRef> asyncCallback);

    public void getCorpusLanguages(AsyncCallback<String[]> asyncCallback);

    public void getExpandTerms(String wildQuery, AsyncCallback<String[]> asyncCallback);

    public void InitPropertiesFromFile(String cookieLang, AsyncCallback<GwtProp> asyncCallback);

    public void createTempFile(String FileName, String Content, AsyncCallback<String> asyncCallback);

    public void createTempZip(String FileName, AsyncCallback<String> asyncCallback);
}

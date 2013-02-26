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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

/**
 *
 * @author simple
 */
public class MyCatDownload {

    private static TranslateServiceAsync rpcD = RpcInit.initRpc();
    private static final String fileDownloadURLBase = GWT.getModuleBaseURL() + "DownloadServlet?filename=";

    public MyCatDownload() {
    }

    public static void downloadFileFromServer(String FileName, String Content, final Label msg) {
        rpcD.createTempFile(FileName, Content, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(GuiMessageConst.MSG_29);
                msg.setText(GuiMessageConst.MSG_30);
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    String fileDownloadURL = fileDownloadURLBase + result.replace("¦", "$$$$$$").replace("+", "|||") + "&mode=file";
                    MainEntryPoint.download(fileDownloadURL, msg);
                } else {
                    Window.alert(GuiMessageConst.MSG_29);
                }
            }
        });

    }

    public static void downloadZipFromServlet(String FileName, final Label msg) {
        String fileDownloadURL = fileDownloadURLBase + FileName.replace("¦", "$$$$$$").replace("+", "|||") + "&mode=zip";
        MainEntryPoint.download(fileDownloadURL, msg);
    }
}

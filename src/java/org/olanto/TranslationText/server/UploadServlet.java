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

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.olanto.convsrv.server.ConvertService;
import org.olanto.senseos.SenseOS;

/**
 * Cette servlet est utilisée uniquement pour l'upload de fichier. Elle répond à
 * un upload avec les informations du fichier.
 *
 */
public class UploadServlet extends UploadAction {

    private static final long serialVersionUID = 1L;
    private static String ext;
    private static final Logger _logger = Logger.getLogger(UploadServlet.class);

    @Override
    public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
        String response = "";
        _logger.info("Execute action.");
        ext = getFileExtension();
        System.out.println("Do not convert files ending with: " + ext);

        for (FileItem item : sessionFiles) {
            if (false == item.isFormField()) {
                try {
                    if ((item.getName().toLowerCase().endsWith(".txt")) || (item.getName().endsWith(ext))) { // charge sous forme de txt
                        response += addMissingIds(UtilsFiles.file2String(item.getInputStream(), "UTF-8"));
                        System.out.println("File uploaded successfully ");
                    } else {
                        // need conversion
                        response += convertFileWithRMI(item.get(), item.getName());
                        System.out.println("File converted successfully");
                    }

                } catch (Exception ex2) {
                    _logger.error(ex2);
                }
            }
        }
        // Remove files from session because we have a copy of them    
        removeSessionFileItems(request);
        // Send your customized message to the client.    
        return response;
    }

    private String convertFileWithRMI(byte[] bytes, String fileName) {
        String ret = "Warning: System seems to be unavailable, please contact the Translation Support Section";
        _logger.info("Request to convert file: " + fileName);
        System.out.println("Request to convert file: " + fileName);

        try {
            Remote r = Naming.lookup("rmi://localhost/CONVSRV");
            if (r instanceof ConvertService) {
                ConvertService is = (ConvertService) r;
                // ret = is.getInformation();

                int pos = fileName.lastIndexOf('\\');

                if (pos >= 0) {
                    fileName = fileName.substring(pos + 1);
                }

                ret = is.File2Txt(bytes, fileName);
            } else {
                return "CONVSRV Service not found or not compatible.";
            }

        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            _logger.error(ex);
        }

        return ret;
    }

    private String getFileExtension() {
        Properties prop;
        String fileName = SenseOS.getMYCAT_HOME() + "/config/GUI_fix.xml";
        FileInputStream f = null;
        try {
            f = new FileInputStream(fileName);
        } catch (Exception e) {
            System.out.println("cannot find properties file:" + fileName);
            _logger.error(e);
        }
        try {

            prop = new Properties();
            prop.loadFromXML(f);
            return prop.getProperty("QD_FILE_EXT");
        } catch (Exception e) {
            System.out.println("errors in properties file:" + fileName);
            _logger.error(e);
        }
        return null;
    }

    private String addMissingIds(String content) {
        if (content.contains("id=\"ref")) {
            return content;
        } else {
            String regex = "(<a)(\\s+)(href=\")(#)(\\d+)(\")";
            return content.replaceAll(regex, "$1$2$3$4$5$6 id=\"ref$5$6");
        }
    }
}

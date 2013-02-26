/**********
    Copyright © 2010-2012 Olanto Foundation Geneva

   This file is part of myCAT.

   myCAT is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of
    the License, or (at your option) any later version.

    myCAT is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with myCAT.  If not, see <http://www.gnu.org/licenses/>.

**********/

package org.olanto.convsrv.server;

import java.rmi.*;

/* -server -Xmx600m -Djava.rmi.server.codebase="file:///c:/MYCAT/prog/ConverterServer/dist/ConverterServer.jar"  -Djava.security.policy="c:/MYCAT/rmi.policy" */
// JG modifier pour le rmi
/**
 *  Facade pour les services du service de conversion en modeRMI 
 * (ces services peuvent être augmentés en publiant des méthodes utiles pour la conversion
 * par exemple des
 * init, stop, restart, statistiques ...)
 *
 */
public interface ConvertService extends Remote {

    /**
     * Pour test si le service est ok
     * @return un message ...
     * @throws java.rmi.RemoteException
     */
    public String getInformation() throws RemoteException;

    /**
     * Pour convertir un fichier dont on connait le nom
     * @param fileName nom du fichier (utiliser les extensions pour la conversion)  
     * @return le txt convertit
     * @throws java.rmi.RemoteException
     */
    public String File2Txt(byte[] content, String fileName) throws RemoteException;

    /**
     * Pour convertir un fichier dont on connait le nom
     * @param fileName nom du fichier (utiliser les extensions pour la conversion)
     * @return le fichier html  au format UTF8 convertit
     * @throws java.rmi.RemoteException
     */
    public String File2HtmlUTF8(byte[] content, String fileName) throws RemoteException;

    /**
     * Pour convertir un fichier dont on connait le nom
     * @param fileName nom du fichier (utiliser les extensions pour la conversion)  
     * @return le fichier txt  au format UTF8 convertit
     * @throws java.rmi.RemoteException
     */
    public byte[] File2UTF8(byte[] content, String fileName) throws RemoteException;



    /**
     * Pour convertir un fichier dont on le format (PDF,DOC,...) vers un autre format
     * @param Format de la source (PDF,DOC,...)
     * @param Format de la target (PDF,DOC,...)  dans les limites des formats accepter par open office
     * @return le fichier converti
     * @throws java.rmi.RemoteException
     */
    public byte[] File2File(byte[] content, String SourceFormat, String TargetFormat) throws RemoteException;
}

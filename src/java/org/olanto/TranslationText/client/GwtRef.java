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

package org.olanto.TranslationText.client;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * struture pour quote detector
 */
public class GwtRef implements IsSerializable{
    public String DOC_REF_SEPARATOR;
    /* la position en caractères depuis le début de la référence */
//    public int[] posref;
    /* la liste des documents contenant le texte de la référence */
    public String[] listofref;
    /* la liste du texte référencé */
    public String[] reftext;
    /* visualisation des ref en html */
    public String htmlref;
    /* nbr de références */
    public int nbref;
    /* la durée d'exécution */
//    public long duration;  // en ms

}

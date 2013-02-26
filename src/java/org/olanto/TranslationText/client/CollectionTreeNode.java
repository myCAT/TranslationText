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

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * pour un noeud
 */
public class CollectionTreeNode extends TreeNode {

    public CollectionTreeNode(String CollectionId, String SubCollectionOf, String CollectionName, int cNum, boolean fin) {
        setAttribute("CollectionId", CollectionId);
        setAttribute("UnderFolder", SubCollectionOf);
        setAttribute("CollectionName", CollectionName);
        setAttribute("ChildNumber", cNum);
        setAttribute("IsFinal", fin);
    }
}

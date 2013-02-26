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

import com.google.gwt.user.client.ui.Composite;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;
import java.util.ArrayList;

/**
 * pour stocker les collections
 */
public class CollectionTreeWidget extends Composite {

    public TreeNode[] CollectionData;
    public CollectionTree root;
    public ArrayList<String> Selection = new ArrayList<String>();
    public ArrayList<String> selectedRec = new ArrayList<String>();
    public ArrayList<String> selected = new ArrayList<String>();
    public TreeGrid collectionTreeGrid = new TreeGrid();
    public ListGridRecord[] Rec;
    public VLayout layout = new VLayout();
    private String SelPaths = "[]";

    public CollectionTreeWidget() {
    }

    @SuppressWarnings("static-access")
    public void Draw(CollectionTree root) {

        ArrayList<TreeNode> tree = new ArrayList<TreeNode>();
        toCollTree(tree, root);
        CollectionData = new TreeNode[tree.size()];
        tree.toArray(CollectionData);

        Tree collectionTree = new Tree();
        collectionTree.setModelType(TreeModelType.PARENT);
        collectionTree.setRootValue("1");
        collectionTree.setNameProperty("CollectionName");
        collectionTree.setIdField("CollectionId");
        collectionTree.setParentIdField("UnderFolder");
        collectionTree.setPathDelim("¦");
        collectionTree.setData(CollectionData);

        collectionTreeGrid.setNodeIcon("icons/16/icon_add_files.png");
        collectionTreeGrid.setFolderIcon("icons/16/folder_document.png");
        collectionTreeGrid.setShowOpenIcons(false);
        collectionTreeGrid.setShowDropIcons(false);
        collectionTreeGrid.setClosedIconSuffix("");
        collectionTreeGrid.setData(collectionTree);
        collectionTreeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        collectionTreeGrid.setShowSelectedStyle(true);
        collectionTreeGrid.setShowPartialSelection(true);
        collectionTreeGrid.setCascadeSelection(true);

        collectionTreeGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                String current = event.getRecord().getAttribute("CollectionId").substring(1);
                if (event.getState()) {
                    if (!selectedRec.contains(current)) {
                        selectedRec.add(current);
                    }
                } else {
                    selectedRec.remove(current);
                }
            }
        });
    }

    private void getSelections() {
        Rec = collectionTreeGrid.getSelectedRecords(true);
        selected.clear();
        int i = 0;
        while (i < Rec.length) {
            String val = Rec[i].getAttribute("CollectionId");
            String upper = Rec[i].getAttribute("UnderFolder");
            int chNumber = Rec[i].getAttributeAsInt("ChildNumber");
            if ((val != null) && (!val.equalsIgnoreCase("1"))) {
                val = val.substring(1);
                if (upper.equalsIgnoreCase("1") && (!selected.contains(val))) {
                    selected.add(val);
                }
                if ((!selected.contains(val)) && (upper != null) && (!upper.equalsIgnoreCase("1"))) {
                    upper = upper.substring(1);
                    if (!selected.contains(upper)) {
                        selected.add(val);
                    }
                }
            }
            i += chNumber;
        }
    }

    public void OrderSelection() {
        Selection.clear();
        getSelections();
        if (!selectedRec.isEmpty()) {
            for (int s = 0; s < selectedRec.size(); s++) {
                String select = selectedRec.get(s);
                for (int i = 0; i < selected.size(); i++) {
                    if (selected.contains(select)) {
                        if (!Selection.contains(select)) {
                            Selection.add(select);
                        }
                    }
                }
            }
        }
    }

    public void setCurrentSelection() {
        collectionTreeGrid.deselectAllRecords();
        collectionTreeGrid.setSelectedPaths(SelPaths);
    }

    public void clearSelection() {
        collectionTreeGrid.deselectAllRecords();
        SelPaths = "[]";
    }

    public void setSelection() {
        if (!Selection.isEmpty()) {
            SelPaths = Selection.toString().replace("\n", "").replace("[", "[\n\"¦").replace(", ", "\",\n\"¦").replace("]", "\"\n]");
        } else {
            collectionTreeGrid.deselectAllRecords();
            SelPaths = "[]";
        }
//        Window.alert(SelPaths);
    }

    private void toCollTree(ArrayList<TreeNode> tree, CollectionTree s) {

        for (CollectionTree a : s.ChildFolders) {
            tree.add(new CollectionTreeNode(a.id, a.idUp, a.currFolder, a.childNumber, a.isEndOfCollection));
            toCollTree(tree, a);
        }
    }
}

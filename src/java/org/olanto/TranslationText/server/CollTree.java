/**********
Copyright © 2010-2012  Olanto Foundation Geneva

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
package org.olanto.TranslationText.server;

/** gestion des collections
 */
import java.util.ArrayList;

public class CollTree {
    //declare

    public int level;
    public String id;
    public String idUp;
    public String currFolder;
    public String upperFolder;
    public boolean isEndOfCollection;
    public ArrayList<CollTree> ChildFolders;

    //constructors
    public CollTree(String c, int l, String id, String upper, boolean end) {
        this.level = l;
        this.currFolder = c;
        this.upperFolder = upper;
        this.ChildFolders = new ArrayList<CollTree>();
        this.id = id + "¦" + c;
        if (l == 0) {
            this.idUp = "";
        } else {
            this.idUp = id;
        }
        this.isEndOfCollection = end;
    }

    public boolean SubcontainsFolder(String folder, int lev, String id) {
//        System.out.println("*** SubcontainsFolder ***");
//        System.out.println("Check if the folder exists " + folder + " at level " + lev + " ??? " + id);
//        System.out.println("Current folder: " + currFolder + " at level " + level + " ...");
        CollTree a = this;
        for (CollTree temp : ChildFolders) {
//            System.out.println("Child folder: " + temp.currFolder + " at level " + temp.level + " ... " + temp.id);
            if ((temp.currFolder.equals(folder)) && (temp.id.equals(id + "¦" + folder)) && (temp.level == lev)) {
//                System.out.println("Contained folder found: " + temp.currFolder + " at level " + temp.level + " ...");
                a.isEndOfCollection = false;
                return true;
            }
        }
        return false;
    }

    public CollTree Subcontains(String folder, int lev, String id) {
//        System.out.println("*** SubcontainsFolder ***");
//        System.out.println("Looking for folder " + folder + " at level " + lev + " ??? " + id);
//        System.out.println("Current folder: " + currFolder + " at level " + level + " ...");
        CollTree a = this;
        for (CollTree temp : ChildFolders) {
//            System.out.println("Child folder: " + temp.currFolder + " at level " + temp.level + " ... " + temp.id);
            if ((temp.currFolder.equals(folder)) && (temp.id.equals(id)) && (temp.level == lev)) {
//                System.out.println("Contained folder found: " + temp.currFolder + " at level " + temp.level + " ...");
                a.isEndOfCollection = false;
                return temp;
            }
        }
        return null;
    }

    public CollTree getCollectionSubTree(String c, int lev, String id) {
        //set pointer
//        System.out.println("*** getCollectionSubTree ***");

        //recursive function
//        System.out.println("Check folder " + c + " at level " + lev + " ...");
//        System.out.println("Current folder: " + currFolder + " at level " + level + " ...");
        if (level == lev) {
            if ((currFolder.equalsIgnoreCase(c)) && (id.equalsIgnoreCase(id))) {
//                System.out.println("folder found: " + currFolder + " ...");
                isEndOfCollection = false;
                return this;
            }
        } else if (level == lev - 1) {
            return this.Subcontains(c, lev, id);
        } else if (level < lev - 1) {
            for (CollTree a : ChildFolders) {
                return a.getCollectionSubTree(c, lev, id);
            }
        }
        return null;
    }

    public int debug() {
        int size = 1;
        for (CollTree a : ChildFolders) {
            size += a.debug();
//            System.out.println(a.currFolder + " (level " + a.level + " Upper: " + a.idUp + " id: " + a.id + ")");
        }
        return size;
    }
}

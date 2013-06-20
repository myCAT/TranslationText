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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.ArrayList;

/**
 *
 * pour stocker un appel
 */
public class FormCallWidget extends Composite {

    private String source = "";
    private String query = "";
    private String lS = "";
    private String lT = "";
    public VerticalPanel pWidget = new VerticalPanel();
    public HorizontalPanel statusPanel = new HorizontalPanel();
    public Label msg = new Label();
    private BitextWidget tS = new BitextWidget(msg);

    // Ajouter un widget pour la gestion des appels externes
    public FormCallWidget(String src, String qry, String lsrc, String ltgt) {
        this.source = src;
        this.query = qry;
        this.lS = lsrc;
        this.lT = ltgt;
        pWidget.add(tS);
        tS.DrawEffects();
        pWidget.setCellHorizontalAlignment(tS, HorizontalPanel.ALIGN_CENTER);
        pWidget.add(statusPanel);
        pWidget.setCellHorizontalAlignment(statusPanel, HorizontalPanel.ALIGN_CENTER);
        statusPanel.setStylePrimaryName("searchHeader");
        statusPanel.add(msg);
        statusPanel.setCellVerticalAlignment(msg, VerticalPanel.ALIGN_MIDDLE);
        statusPanel.setCellHorizontalAlignment(msg, HorizontalPanel.ALIGN_CENTER);
    }

    public void draWidget(ArrayList<String> stopWords) {
        statusPanel.setSize(tS.getOffsetWidth() + "px", "20px");
        if (!(source.equalsIgnoreCase("undefined")) && !(query.equalsIgnoreCase("undefined"))
                && !(lS.equalsIgnoreCase("undefined")) && !(lT.equalsIgnoreCase("undefined"))) {
            tS.words = Utility.getQueryWords(query + " ", stopWords);
            tS.queryLength = query.length();
            tS.getTextContent(source, lS, lT, query);
        }
    }
}

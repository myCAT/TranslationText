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

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * panneau de coté
 */
public class SideWidget extends Composite {

    private VerticalPanel pWidget = new VerticalPanel();
    public Button GoSrch = new Button("Go");
    private Label findS = new Label();
    private Label findT = new Label();
    public FileUpload txtS = new FileUpload();
    public FileUpload txtT = new FileUpload();
    private ListBox langS = new ListBox();
    private ListBox langT = new ListBox();

    public SideWidget() {

        HorizontalPanel hp = new HorizontalPanel();
        hp.setHeight("50");
        hp.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        hp.add(findS);
        hp.add(txtS);
        hp.add(langS);

        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.setHeight("50");
        hp1.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        hp1.add(findT);
        hp1.add(txtT);
        hp1.add(langT);




        ContentPanel srch = new ContentPanel();
        srch.setBodyBorder(true);
        srch.setHeading("File Finder");
        srch.setButtonAlign(HorizontalAlignment.CENTER);
        srch.add(hp);
        srch.add(hp1);
        srch.add(GoSrch);
        initWidget(pWidget);
        pWidget.add(srch);
    }

    public void draWidget() {

        setbuttonstyle(GoSrch, 80, 30);

        findS.setWidth("30");
        findS.setText("So:");
        findT.setWidth("30");
        findT.setText("Ta:");
        txtS.setWidth("100");
        txtS.setStyleName("x-form-text");
        txtT.setWidth("100");
        txtT.setStyleName("x-form-text");
        langS.addItem("FR");
        langS.addItem("EN");
        langS.addItem("RU");
        langS.addItem("AR");

        langT.addItem("FR");
        langT.addItem("EN");
        langT.addItem("RU");
        langT.addItem("AR");


    }

    public void setbuttonstyle(Button b, int w, int h) {
        b.setStyleName(".x-btn-click");
        b.setWidth(w);
        b.setHeight(h);
    }
}

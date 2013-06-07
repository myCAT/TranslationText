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

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import java.util.ArrayList;

/**
 * panneau de recherche
 */
public class ResearchWidget extends Composite {

    private VerticalPanel mainWidget = new VerticalPanel();
    private VerticalPanel mainContentWidget = new VerticalPanel();
    public HorizontalPanel resultsPanel = new HorizontalPanel();
    private HorizontalPanel statusPanel = new HorizontalPanel();
    private HorizontalPanel headerPanel = new HorizontalPanel();
    private HorizontalPanel headPanel = new HorizontalPanel();
    private VerticalPanel leftheadPanel = new VerticalPanel();
    private HorizontalPanel contact = new HorizontalPanel();
    public Button GoSrch = new Button(GuiMessageConst.WIDGET_BTN_SUBMIT);
    public TextBox search = new TextBox();
    public ListBox langS = new ListBox();
    public ListBox langT = new ListBox();
    private ListBox sortBy = new ListBox();
    public ContentPanel docListContainer = new ContentPanel();
    private DecoratorPanel staticDecorator = new DecoratorPanel();
    public ScrollPanel staticTreeWrapper = new ScrollPanel();
    private ContentPanel mainContainer = new ContentPanel();
    private ContentPanel headerContainer = new ContentPanel();
    private ContentPanel statusContainer = new ContentPanel();
    private TranslateServiceAsync rpcSch;
    private ArrayList<String> docList;
    public Label msg = new Label();
    private Label TAText = new Label();
    private Image im = new Image(GuiConstant.LOGO_PATH);
    public Button coll = new Button(GuiMessageConst.WIDGET_BTN_COLL_OFF);
    public Button myQuote = new Button(GuiMessageConst.WIDGET_BTN_QD);
    private Button help = new Button(GuiMessageConst.WIDGET_BTN_HELP);
    // Height and width units for buttons and text labels
    private final int H_Unit = 30;
    private final int W_Unit = 20;
    private static final int CHAR_W = GuiConstant.CHARACTER_WIDTH;
    private final String[] SORT_BY = GuiMessageConst.WIDGET_LIST_TA_SBY.split("\\;");
    private final String[] SORT_BY_Eff = GuiConstant.TA_DL_SORTBY.split("\\;");
    private final String[] INT_LANG = GuiConstant.CHOOSE_GUI_LANG_LIST.split("\\;");
    private TabSet topJobsSet = new TabSet();
    private Label chooseLang = new Label(GuiMessageConst.MSG_64);
    private ListBox langInterface = new ListBox();

    public ResearchWidget() {
        rpcSch = RpcInit.initRpc();
        setHeader();
    }

    private void setHeader() {
        initWidget(mainWidget);
        mainWidget.setStyleName("mainContent");
        mainWidget.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        mainWidget.add(mainContentWidget);

        mainContentWidget.add(headerContainer);
        mainContentWidget.add(mainContainer);
        mainContentWidget.add(statusContainer);

        headerContainer.setBodyBorder(true);
        headerContainer.setHeaderVisible(false);
        headerContainer.add(headPanel);
        headerContainer.setStylePrimaryName("searchHeader");
        headPanel.setStylePrimaryName("searchHeader");
        headPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

        headerPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        headerPanel.add(search);
        headerPanel.add(GoSrch);
        headerPanel.add(langS);
        headerPanel.add(langT);
        headerPanel.add(coll);
        headerPanel.add(sortBy);
        headerPanel.add(myQuote);
        headerPanel.add(help);
        if (GuiConstant.CHOOSE_GUI_LANG) {
            headerPanel.add(chooseLang);
            headerPanel.add(langInterface);
        }

        headerPanel.setStylePrimaryName("searchHeader");

        headPanel.add(leftheadPanel);
        headPanel.add(TAText);
        headPanel.setCellHorizontalAlignment(TAText, HorizontalPanel.ALIGN_RIGHT);
        headPanel.add(new HTML("&nbsp;"));

        if ((!GuiConstant.LOGO_PATH.isEmpty()) && (!GuiConstant.LOGO_PATH.isEmpty())) {
            if ((!GuiConstant.LOGO_PATH.equalsIgnoreCase(" ")) && (!GuiConstant.LOGO_PATH.equalsIgnoreCase(" "))) {
                headPanel.add(new HTML("&nbsp;"));
                headPanel.add(im);
                headPanel.setCellHorizontalAlignment(im, HorizontalPanel.ALIGN_RIGHT);
            }
        }

        topJobsSet.setTabBarPosition(Side.TOP);
        topJobsSet.setDefaultHeight(H_Unit);
        topJobsSet.setPaneContainerClassName("searchHeader");
        leftheadPanel.add(topJobsSet);
        leftheadPanel.add(headerPanel);

        mainContainer.setBodyBorder(true);
        mainContainer.setHeaderVisible(false);
        mainContainer.add(resultsPanel);
        mainContainer.setStylePrimaryName("mainContent");

        statusContainer.setBodyBorder(true);
        statusContainer.setHeaderVisible(false);
        statusContainer.add(statusPanel);
        statusPanel.setHeight(H_Unit + "px");
        statusPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        statusPanel.add(new HTML("&nbsp;"));
        statusPanel.add(msg);
        statusPanel.setCellHorizontalAlignment(msg, HorizontalPanel.ALIGN_LEFT);
        statusPanel.add(contact);
        statusPanel.setCellHorizontalAlignment(contact, HorizontalPanel.ALIGN_RIGHT);
        contact.setPixelSize(300, 25);
        contact.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        Anchor poweredBy = new Anchor(GuiConstant.OLANTO_URL.substring(0, GuiConstant.OLANTO_URL.indexOf("|")), true,
                GuiConstant.OLANTO_URL.substring(GuiConstant.OLANTO_URL.indexOf("|") + 1, GuiConstant.OLANTO_URL.lastIndexOf("|")),
                GuiConstant.OLANTO_URL.substring(GuiConstant.OLANTO_URL.lastIndexOf("|") + 1));
        contact.add(poweredBy);
        poweredBy.setStylePrimaryName("contactInfo");
        contact.setCellHorizontalAlignment(poweredBy, HorizontalPanel.ALIGN_LEFT);
        contact.add(new HTML("||"));
        Anchor feedback = new Anchor(GuiConstant.FEEDBACK_MAIL.substring(0, GuiConstant.FEEDBACK_MAIL.indexOf("|")));
        contact.add(feedback);
        feedback.setStylePrimaryName("contactInfo");
        contact.setCellHorizontalAlignment(feedback, HorizontalPanel.ALIGN_RIGHT);
        contact.add(new HTML("&nbsp;"));

        statusContainer.setStylePrimaryName("statusPanel");
        statusPanel.setStylePrimaryName("statusPanel");

        docListContainer.setBodyBorder(true);
        docListContainer.setHeading(GuiMessageConst.MSG_41);
        resultsPanel.add(docListContainer);

        docListContainer.add(staticDecorator);

        langS.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                MyCatCookies.updateCookie(CookiesNamespace.MyCatlangS, langS.getItemText(langS.getSelectedIndex()));
            }
        });
        langT.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                MyCatCookies.updateCookie(CookiesNamespace.MyCatlangT, langT.getItemText(langT.getSelectedIndex()));
            }
        });
        sortBy.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                MyCatCookies.updateCookie(CookiesNamespace.SortBy, SORT_BY_Eff[sortBy.getSelectedIndex()]);
            }
        });
        help.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                Window.open(GuiConstant.TA_HELP_URL, "", GuiConstant.W_OPEN_FEATURES);
            }
        });
        if (GuiConstant.CHOOSE_GUI_LANG) {
            langInterface.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    MyCatCookies.updateCookie(CookiesNamespace.InterfaceLanguage, INT_LANG[langInterface.getSelectedIndex()]);
                    Window.Location.reload();
                }
            });
        }
        if ((!GuiConstant.LOGO_PATH.isEmpty()) && (!GuiConstant.LOGO_PATH.isEmpty())) {
            im.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    Window.open(GuiConstant.LOGO_URL, "", GuiConstant.W_OPEN_FEATURES);
                }
            });
        }
        feedback.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                mailto(GuiConstant.FEEDBACK_MAIL.substring(GuiConstant.FEEDBACK_MAIL.lastIndexOf("|") + 1), "Feedback about " + GuiConstant.TEXT_ALIGNER_LBL);
            }
        });
    }

    public void mailto(String address, String subject) {
        Window.open("mailto:" + address + "?subject=" + URL.encode(subject), "_blank", "");
    }

    public void setbuttonstyle(Button b, int w, int h) {
        b.setStyleName("x-btn-click");
        b.setPixelSize(w, h);
    }

    public void draWidget() {
        setbuttonstyle(GoSrch, GoSrch.getText().length() * 2 * CHAR_W, H_Unit);
        setbuttonstyle(myQuote, myQuote.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(coll, coll.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(help, help.getText().length() * (CHAR_W + 2), H_Unit);
        GoSrch.setAutoWidth(true);
        myQuote.setAutoWidth(true);
        coll.setAutoWidth(true);
        help.setAutoWidth(true);

        search.setWidth("300px");
        search.setStyleName("x-form-text");
        TAText.setText(GuiConstant.TEXT_ALIGNER_LBL);
        TAText.setStyleName("gwt-im-text");
        chooseLang.setStyleName("gwt-w-label");

        setMessage("info", "");
        for (int i = 0; i < SORT_BY.length; i++) {
            sortBy.addItem(SORT_BY[i]);
        }
        langS.setWidth(3 * W_Unit + "px");
        langT.setWidth(3 * W_Unit + "px");
        sortBy.setWidth(6 * W_Unit + "px");
        sortBy.setSelectedIndex(Utility.getIndex(SORT_BY_Eff, Cookies.getCookie(CookiesNamespace.SortBy)));

        for (int i = 0; i < INT_LANG.length; i++) {
            langInterface.addItem(INT_LANG[i]);
        }
        langInterface.setWidth(2 * W_Unit + "px");
        langInterface.setSelectedIndex(Utility.getIndex(INT_LANG, Cookies.getCookie(CookiesNamespace.InterfaceLanguage)));

        docListContainer.setSize(GuiConstant.DOC_LIST_WIDTH, GuiConstant.DOC_LIST_HEIGHT);
        staticDecorator.setStyleName("doclist");
        staticDecorator.setWidget(staticTreeWrapper);
        staticTreeWrapper.setAlwaysShowScrollBars(true);
        staticTreeWrapper.setPixelSize(GuiConstant.DOC_LIST_WIDTH, GuiConstant.DOC_LIST_HEIGHT - H_Unit);
        if ((GuiConstant.JOBS_ITEMS != null) && (GuiConstant.JOBS_ITEMS.length() > 1)) {
            String jobs = GuiConstant.JOBS_ITEMS;
            String[] tablist = jobs.split("\\;");
            Tab tinv = new Tab();
            topJobsSet.addTab(tinv);
            tinv.setTitle("Select Tab : ");
            tinv.setID("Empty");
            tinv.setDisabled(true);
            if (tablist.length > 0) {
                for (int i = 0; i < tablist.length; i++) {
                    Tab tTab = new Tab();
                    tTab.setIcon("./icons/16/folder_document.png");
                    tTab.setTitle(tablist[i].substring(0, tablist[i].indexOf("|")));
                    tTab.setAttribute("URL", tablist[i].substring(tablist[i].indexOf("|") + 1, tablist[i].lastIndexOf("|")));
                    tTab.setAttribute("MODE", tablist[i].substring(tablist[i].lastIndexOf("|") + 1));
                    topJobsSet.addTab(tTab);
                }
            }
            topJobsSet.setSelectedTab(0);
            topJobsSet.addTabSelectedHandler(new TabSelectedHandler() {
                @Override
                public void onTabSelected(TabSelectedEvent event) {
                    if (!(event.getTab().getID().contains("Empty"))) {
//                        Window.alert("URL: " + event.getTab().getAttribute("URL") + "mode: " + event.getTab().getAttribute("MODE"));
                        Window.open(event.getTab().getAttribute("URL"), event.getTab().getAttribute("MODE"), "");
                    }
                }
            });
            topJobsSet.setWidth(headerPanel.getOffsetWidth());
            topJobsSet.draw();
            topJobsSet.setCursor(Cursor.HAND);
        } else {
            leftheadPanel.remove(topJobsSet);
        }
    }

    private void createSourceTree(ArrayList<String> doclist, final BitextWidget tS, final String Query) {
        final String lS = langS.getItemText(langS.getSelectedIndex());
        final String lT = langT.getItemText(langT.getSelectedIndex());
        // Create the tree
        Tree staticTree = new Tree();
        String docName, longName, listElem;
        final String racine = lS + "/";
        int k, l;

        for (int i = 0; i < doclist.size(); i++) {

            TreeItem docItem = new TreeItem();
            listElem = doclist.get(i);
            k = listElem.indexOf("¦]");
            l = listElem.indexOf("[¦") + 2;
            longName = listElem.substring(l);
            docName = listElem.substring(0, k);
            if ((!GuiConstant.FILE_NAME_RIGHT) && (GuiConstant.PATH_ON)) {
                docItem.setTitle(longName.substring(3).replace("¦", "/"));
                docItem.setHTML(longName.substring(3).replace("¦", "/"));
            } else {
                docItem.setTitle(longName.substring(3).replace("¦", "/"));
                docItem.setHTML(docName);
            }
            staticTree.addItem(docItem);
        }

        staticTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
            @Override
            public void onSelection(SelectionEvent<TreeItem> event) {
                if (event.getSelectedItem().getText() != null) {
                    setMessage("info", GuiMessageConst.MSG_51 + event.getSelectedItem().getTitle());
                    tS.reset();
                    tS.words = MainEntryPoint.words;
                    tS.getTextContent(racine + event.getSelectedItem().getTitle().replace("/", "¦"), lS, lT, Query);
                }
            }
        });
        staticTree.ensureDebugId("cwTree-staticTree");
        staticTree.setStyleName("gwt-Tree");

        // Wrap the static tree in a DecoratorPanel
        staticTreeWrapper.add(staticTree);
    }

    public void DrawDocumentList(final String Query, final BitextWidget tS, final ArrayList<String> collections) {
        staticTreeWrapper.clear();
        adaptSize();
        // remote procedure call to the server to get the document list that satisfies the query
        rpcSch.getDocumentList(Query, collections, GuiConstant.PATH_ON, GuiConstant.MAX_RESPONSE, SORT_BY_Eff[sortBy.getSelectedIndex()], GuiConstant.EXACT_FLG, GuiConstant.EXACT_NBR_FLG, new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(GuiMessageConst.MSG_53 + Query);
                GoSrch.enable();
            }

            @Override
            public void onSuccess(ArrayList<String> doclist) {
                docList = doclist;
                if (docList.size() > 0) {
                    docListContainer.setHeading(GuiMessageConst.MSG_41 + docList.size() + GuiMessageConst.MSG_52);
                    createSourceTree(docList, tS, Query);
                    GoSrch.enable();
                    if (docList.size() >= GuiConstant.MAX_RESPONSE) {
                        setMessage("warning", GuiMessageConst.MSG_54 + " (>" + GuiConstant.MAX_RESPONSE + ")");
                    } else {
                        setMessage("info", GuiMessageConst.MSG_41 + docList.size() + GuiMessageConst.MSG_52);
                    }
                } else {
                    docListContainer.setHeading(GuiMessageConst.MSG_59);
                    setMessage("warning", GuiMessageConst.MSG_59);
                    GoSrch.enable();
                }
            }
        });
    }

    public void DrawDocumentBrowseList(final String Query, final BitextWidget tS, final ArrayList<String> collections) {
        staticTreeWrapper.clear();
        // remote procedure call to the server to get the document list that satisfies the query
        rpcSch.getDocumentBrowseList(Query, langS.getItemText(langS.getSelectedIndex()), collections, GuiConstant.PATH_ON, GuiConstant.MAX_RESPONSE, SORT_BY_Eff[sortBy.getSelectedIndex()], GuiConstant.ONLY_ON_FILE_NAME, new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(GuiMessageConst.MSG_53 + Query);
                GoSrch.enable();
            }

            @Override
            public void onSuccess(ArrayList<String> doclist) {
                docList = doclist;
                if (docList.size() > 0) {
                    docListContainer.setHeading(GuiMessageConst.MSG_41 + docList.size() + GuiMessageConst.MSG_52);
                    if (docList.size() >= GuiConstant.MAX_BROWSE) {
                        setMessage("warning", GuiMessageConst.MSG_54 + " (>" + GuiConstant.MAX_BROWSE + ")");
                    } else {
                        setMessage("info", GuiMessageConst.MSG_41 + docList.size() + GuiMessageConst.MSG_52);
                    }
                    createSourceTree(docList, tS, Query);
                    GoSrch.enable();
                } else {
                    docListContainer.setHeading(GuiMessageConst.MSG_59);
                    setMessage("warning", GuiMessageConst.MSG_59);
                    GoSrch.enable();
                }
            }
        });
    }

    public void setMessage(String type, String message) {
        msg.setStyleName("gwt-TA-" + type.toLowerCase());
        msg.setText(message);
    }

    public void adaptSize() {
        int width = getMaximumWidth();
        statusPanel.setPixelSize(width, statusPanel.getOffsetHeight());
        headPanel.setPixelSize(width, headPanel.getOffsetHeight());
        resultsPanel.setPixelSize(width, resultsPanel.getOffsetHeight());
        msg.setWidth((width - contact.getOffsetWidth()) + "px");
    }

    private int getMaximumWidth() {
        int max = resultsPanel.getOffsetWidth();
        if (statusPanel.getOffsetWidth() > max) {
            max = statusPanel.getOffsetWidth();
        }
        if (headPanel.getOffsetWidth() > max) {
            max = headPanel.getOffsetWidth();
        }
        return max;
    }
}

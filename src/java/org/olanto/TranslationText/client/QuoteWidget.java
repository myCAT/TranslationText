/**
 * ********
 * Copyright © 2010-2012 Olanto Foundation Geneva
 *
 * This messageContainer is part of myCAT.
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
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.smartgwt.client.types.Cursor;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.Uploader;
import java.util.ArrayList;

/**
 * modif 2012.4.28 J. Guyot modif du minquote passé de 6 à 3
 */
public class QuoteWidget extends Composite {

    private VerticalPanel mainWidget = new VerticalPanel();
    private HorizontalPanel resultsPanel = new HorizontalPanel();
    private HorizontalPanel statusPanel = new HorizontalPanel();
    private HorizontalPanel headerPanel = new HorizontalPanel();
    private HorizontalPanel headPanel = new HorizontalPanel();
    private VerticalPanel leftheadPanel = new VerticalPanel();
    private HorizontalPanel contact = new HorizontalPanel();
    private QuoteBitextWidget tS;
    public Button GoSrch = new Button(GuiMessageConst.WIDGET_BTN_SUBMIT);
    public Button TextAligner = new Button(GuiMessageConst.WIDGET_BTN_TA);
    private Uploader fileUpload = new Uploader();
    public ListBox langS = new ListBox();
    public ListBox langT = new ListBox();
    private ListBox minLength = new ListBox();
    private ContentPanel docListContainer = new ContentPanel();
    private ContentPanel mainContainer = new ContentPanel();
    private ContentPanel headerContainer = new ContentPanel();
    private ContentPanel refpanel = new ContentPanel();
    private ContentPanel statusContainer = new ContentPanel();
    private TranslateServiceAsync rpcRef;
    private Label msg = new Label();
    public Label refIndic = new Label();
    private Label minLn = new Label(GuiMessageConst.WIDGET_LBL_QD_LN);
    private Label QDText = new Label(GuiConstant.QUOTE_DETECTOR_LBL);
    private Image im = new Image(GuiConstant.LOGO_PATH);
    public Button coll = new Button(GuiMessageConst.WIDGET_BTN_COLL_OFF);
    private Button prev = new Button(GuiMessageConst.WIDGET_BTN_QD_PVS);
    private Button next = new Button(GuiMessageConst.WIDGET_BTN_QD_NXT);
    private Button help = new Button(GuiMessageConst.WIDGET_BTN_HELP);
    private Button save = new Button(GuiMessageConst.WIDGET_BTN_QD_SAVE);
    private String fileName = null;
    private String fileContent = "";
    public HtmlContainer refArea = new HtmlContainer();
    private ScrollPanel htmlWrapper = new ScrollPanel(refArea);
    private DecoratorPanel staticDecorator = new DecoratorPanel();
    private ScrollPanel staticTreeWrapper = new ScrollPanel();
    private GwtRef refDoc;
    private int refIdx = 1;
    private ArrayList<String> docList;
    // Height and width units for buttons and text labels
    private final int H_Unit = 30;
    private final int W_Unit = 20;
    private static final int CHAR_W = GuiConstant.CHARACTER_WIDTH;
    private TabSet topJobsSet = new TabSet();
    private boolean canGo = false;

    public QuoteWidget() {
        rpcRef = RpcInit.initRpc();
        setHeader();
    }

    private void setHeader() {
        initWidget(mainWidget);
        mainWidget.setStyleName("mainContent");
        mainWidget.add(headerContainer);
        mainWidget.setCellHorizontalAlignment(headerContainer, VerticalPanel.ALIGN_CENTER);
        mainWidget.add(refpanel);
        mainWidget.setCellHorizontalAlignment(refpanel, VerticalPanel.ALIGN_CENTER);
        mainWidget.add(mainContainer);
        mainWidget.setCellHorizontalAlignment(mainContainer, VerticalPanel.ALIGN_LEFT);
        mainWidget.add(statusContainer);
        mainWidget.setCellHorizontalAlignment(statusContainer, VerticalPanel.ALIGN_CENTER);

        headerContainer.setBodyBorder(true);
        headerContainer.setHeaderVisible(false);
        headerContainer.add(headPanel);
        headerContainer.setStylePrimaryName("searchHeader");
        headPanel.setStylePrimaryName("searchHeader");
        headPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

        headerPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
        headerPanel.add(fileUpload);
        headerPanel.add(GoSrch);
        headerPanel.add(langS);
        headerPanel.add(langT);
        headerPanel.add(coll);
        headerPanel.add(minLn);
        headerPanel.add(minLength);
        headerPanel.add(prev);
        headerPanel.add(next);
        headerPanel.add(refIndic);
        headerPanel.add(TextAligner);
        headerPanel.add(help);
        if (GuiConstant.SAVE_ON) {
            headerPanel.add(save);
        }
        headerPanel.setStylePrimaryName("searchHeader");

        headPanel.add(leftheadPanel);
        headPanel.add(QDText);
        headPanel.setCellHorizontalAlignment(QDText, HorizontalPanel.ALIGN_RIGHT);
        headPanel.add(new HTML("&nbsp;"));
        if ((!GuiConstant.LOGO_PATH.isEmpty()) && (!GuiConstant.LOGO_PATH.isEmpty())) {
            if ((!GuiConstant.LOGO_PATH.equalsIgnoreCase(" ")) && (!GuiConstant.LOGO_PATH.equalsIgnoreCase(" "))) {
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
        statusPanel.add(msg);
        statusPanel.setCellHorizontalAlignment(msg, HorizontalPanel.ALIGN_LEFT);
        statusPanel.add(contact);
        statusPanel.setCellHorizontalAlignment(contact, HorizontalPanel.ALIGN_RIGHT);
        contact.setPixelSize(GuiConstant.DOC_LIST_WIDTH, 25);
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

        refpanel.setBodyBorder(true);
        refpanel.setHeaderVisible(false);
        refpanel.add(htmlWrapper);

        docListContainer.setBodyBorder(true);
        resultsPanel.add(docListContainer);
        resultsPanel.setCellHorizontalAlignment(docListContainer, HorizontalPanel.ALIGN_LEFT);
        docListContainer.add(staticDecorator);
        tS = new QuoteBitextWidget(msg);
        resultsPanel.add(tS);

        refArea.setVisible(true);
        refArea.setStyleName("TextBlock");
        fileUpload.addOnStartUploadHandler(onStartUploaderHandler);
        fileUpload.addOnFinishUploadHandler(onFinishUploaderHandler);
        help.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                Window.open(GuiConstant.QD_HELP_URL, "", GuiConstant.W_OPEN_FEATURES);
            }
        });
        staticDecorator.setStyleName("doclist");
        staticDecorator.setWidget(staticTreeWrapper);
        fileUpload.setAutoSubmit(true);
        setMessage("info", "");
        for (int i = GuiConstant.MIN_OCCU; i < GuiConstant.MAX_OCCU; i++) {
            minLength.addItem("" + i);
        }
        minLength.setSelectedIndex(Integer.parseInt(Cookies.getCookie(CookiesNamespace.MyQuoteMinLength)));
        minLength.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                GoSrch.enable();
                MyCatCookies.updateCookie(CookiesNamespace.MyQuoteMinLength, "" + minLength.getSelectedIndex());
            }
        });
        langS.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                GoSrch.enable();
                MyCatCookies.updateCookie(CookiesNamespace.MyQuotelangS, langS.getItemText(langS.getSelectedIndex()));
            }
        });
        langT.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                GoSrch.enable();
                MyCatCookies.updateCookie(CookiesNamespace.MyQuotelangT, langT.getItemText(langT.getSelectedIndex()));
            }
        });
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
                mailto(GuiConstant.FEEDBACK_MAIL.substring(GuiConstant.FEEDBACK_MAIL.lastIndexOf("|") + 1), GuiMessageConst.MSG_39 + GuiConstant.QUOTE_DETECTOR_LBL);
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
//        headPanel.setWidth((Window.getClientWidth() - 2 * W_Unit) + "px");
//        statusPanel.setWidth((Window.getClientWidth() - 2 * W_Unit) + "px");
//        msg.setWidth((statusPanel.getOffsetWidth() - contact.getOffsetWidth()) + "px");
        setbuttonstyle(GoSrch, GoSrch.getText().length() * 2 * CHAR_W, H_Unit);
        setbuttonstyle(TextAligner, TextAligner.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(coll, coll.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(help, help.getText().length() * (CHAR_W + 2), H_Unit);
        setbuttonstyle(save, save.getText().length() * (CHAR_W + 2), H_Unit);
        setbuttonstyle(prev, prev.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(next, next.getText().length() * (CHAR_W + 1), H_Unit);

        GoSrch.setAutoWidth(true);
        TextAligner.setAutoWidth(true);
        coll.setAutoWidth(true);
        help.setAutoWidth(true);
        save.setAutoWidth(true);
        prev.setAutoWidth(true);
        next.setAutoWidth(true);

        langS.setWidth((int) (2.5 * W_Unit) + "px");
        langT.setWidth((int) (2.5 * W_Unit) + "px");
        minLn.setStyleName("gwt-w-label");
        minLn.setWidth("70px");
        refIndic.setStyleName("gwt-w-label");
        refIndic.setWidth("40px");
        QDText.setStyleName("gwt-im-text");
        htmlWrapper.setPixelSize(Window.getClientWidth() - 3 * W_Unit, (Window.getClientHeight() - (H_Unit + GuiConstant.QD_DOC_LIST_HEIGHT + htmlWrapper.getAbsoluteTop())));
        refArea.setWidth(Window.getClientWidth() - 5 * W_Unit);
        setMessage("warning", GuiMessageConst.MSG_60);
        docListContainer.setHeading(GuiMessageConst.MSG_60);
        staticTreeWrapper.setAlwaysShowScrollBars(true);
        staticTreeWrapper.setPixelSize(GuiConstant.DOC_LIST_WIDTH, GuiConstant.QD_DOC_LIST_HEIGHT - H_Unit);
        docListContainer.setSize(GuiConstant.DOC_LIST_WIDTH, GuiConstant.QD_DOC_LIST_HEIGHT);
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
        GoSrch.disable();
        adaptSize();
    }
    private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
        @Override
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == Status.SUCCESS) {
                canGo = true;
                // need gwtupload version 0.6.3 to use this feature
                final IUploader.UploadedInfo info = uploader.getServerInfo();
                fileName = info.name;
                fileContent = info.message;
                setMessage("info", GuiMessageConst.MSG_42 + fileName);
                fileUpload.setTitle(GuiMessageConst.MSG_42 + fileName);
                if (fileName.endsWith(GuiConstant.QD_FILE_EXT)) {
                    drawReferences(null);
                } else if (fileName.endsWith(GuiConstant.QD_GENERAL_EXT)) {
                    setMessage("error", GuiMessageConst.MSG_58);
                    Window.alert("Ereur: " + GuiMessageConst.MSG_58);
                } else {
                    setMessage("info", GuiMessageConst.MSG_43);
                    GoSrch.enable();
                }
            } else {
                canGo = false;
                setMessage("error", GuiMessageConst.MSG_11);
            }
        }
    };
    private IUploader.OnStartUploaderHandler onStartUploaderHandler = new IUploader.OnStartUploaderHandler() {
        @Override
        public void onStart(IUploader uploader) {
            setMessage("info", GuiMessageConst.MSG_12);
            refArea.setHtml("");
            refIndic.setText(" / ");
            staticTreeWrapper.clear();
            tS.reset();
            docListContainer.setHeading(GuiMessageConst.MSG_60);
            GoSrch.disable();
        }
    };

    private void clearHitHandlers() {
        refIdx = 1;
        prev.removeAllListeners();
        next.removeAllListeners();
        setMessage("info", "");
        save.removeAllListeners();
    }

    public void drawReferences(final ArrayList<String> collections) {
        if (canGo) {
            String lgs = langS.getValue(langS.getSelectedIndex());
            String lgt = langT.getValue(langT.getSelectedIndex());
            int consMin = Integer.parseInt(minLength.getValue(minLength.getSelectedIndex()));
            if (fileName == null) {
                setMessage("error", GuiMessageConst.MSG_44);
                GoSrch.enable();
            } else {
                setMessage("info", GuiMessageConst.MSG_61 + fileName);
                rpcRef.getHtmlRef(fileContent, fileName, consMin, lgs, lgt, collections, GuiConstant.QD_FILE_EXT, new AsyncCallback<GwtRef>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        setMessage("error", GuiMessageConst.MSG_45);
                        GoSrch.enable();
                    }

                    @Override
                    public void onSuccess(GwtRef result) {
                        if (result != null) {
                            clearHitHandlers();
                            refDoc = result;
                            setMessage("info", GuiMessageConst.MSG_46);
                            refIndic.setText(refIdx + " / " + refDoc.nbref);
                            refArea.setHtml(refDoc.htmlref);
                            save.addListener(Events.OnClick, new Listener<BaseEvent>() {
                                @Override
                                public void handleEvent(BaseEvent be) {
                                    MyCatDownload.downloadFileFromServer(getSavedFileName() + GuiConstant.QD_FILE_EXT, refDoc.htmlref, msg);
                                    setMessage("info", GuiMessageConst.MSG_13 + fileName + GuiMessageConst.MSG_14 + getSavedFileName() + GuiConstant.QD_FILE_EXT);
                                }
                            });
                            staticTreeWrapper.clear();
                            docList = Utility.getDocumentlist(refDoc.listofref[refIdx - 1] + "|", refDoc.DOC_REF_SEPARATOR);
                            if (!(fileName.endsWith(GuiConstant.QD_FILE_EXT))) {
                                GoSrch.enable();
                            }

                            if (refDoc.nbref > 0) {
                                refIndic.setText(refIdx + " / " + refDoc.nbref);
                                setMessage("info", GuiMessageConst.MSG_8 + refIdx + " / " + refDoc.nbref);
                                DOM.getElementById("ref" + refIdx).scrollIntoView();
                                addHitHandlers();
                            }
                        } else {
                            setMessage("error", GuiMessageConst.MSG_47);
                            GoSrch.enable();
                        }
                    }
                });
            }
        } else {
            setMessage("error", GuiMessageConst.MSG_48);
        }
    }

    private void addHitHandlers() {
        // Handler of the going to the next line in the source text
        next.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {

                if (refIdx < refDoc.nbref) {
                    refIdx++;
                    if (refIdx == 1) {
                        setMessage("info", GuiMessageConst.MSG_49 + " / " + refDoc.nbref);
                    }
                    if (refIdx == refDoc.nbref) {
                        setMessage("info", GuiMessageConst.MSG_50 + " / " + refDoc.nbref);
                    }
                    DOM.getElementById("ref" + refIdx).scrollIntoView();
                    staticTreeWrapper.clear();
                    refIndic.setText(refIdx + " / " + refDoc.nbref);
                    setMessage("info", GuiMessageConst.MSG_8 + refIdx + " / " + refDoc.nbref);
                    docList = Utility.getDocumentlist(refDoc.listofref[refIdx - 1] + "|", refDoc.DOC_REF_SEPARATOR);
                    DrawDocumentList();
                } else {
                    setMessage("info", GuiMessageConst.MSG_50 + " / " + refDoc.nbref);
                    DOM.getElementById("ref" + refIdx).scrollIntoView();
                }
            }
        });

        // Handler of the going to the previous line in the source text
        prev.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (refIdx > 0) {
                    refIdx--;
                    setMessage("info", GuiMessageConst.MSG_8 + refIdx + " / " + refDoc.nbref);
                    if (refIdx == refDoc.nbref) {
                        setMessage("info", GuiMessageConst.MSG_50 + " / " + refDoc.nbref);
                    }
                    if (refIdx == 1) {
                        setMessage("info", GuiMessageConst.MSG_49 + " / " + refDoc.nbref);
                    }
                    DOM.getElementById("ref" + refIdx).scrollIntoView();
                    staticTreeWrapper.clear();
                    refIndic.setText(refIdx + " / " + refDoc.nbref);
                    docList = Utility.getDocumentlist(refDoc.listofref[refIdx - 1] + "|", refDoc.DOC_REF_SEPARATOR);
                    DrawDocumentList();
                } else {
                    setMessage("info", GuiMessageConst.MSG_49 + " / " + refDoc.nbref);
                    DOM.getElementById("ref" + refIdx).scrollIntoView();
                }
            }
        });

    }

    public void getRefHitContent(int i) {
        refIdx = i;

        if (refIdx == 1) {
            setMessage("info", GuiMessageConst.MSG_49 + " / " + refDoc.nbref);
        }
        if (refIdx == refDoc.nbref) {
            setMessage("info", GuiMessageConst.MSG_50 + " / " + refDoc.nbref);
        }
        DOM.getElementById("ref" + refIdx).scrollIntoView();
        staticTreeWrapper.clear();
        refIndic.setText(refIdx + " / " + refDoc.nbref);
        setMessage("info", GuiMessageConst.MSG_8 + refIdx + " / " + refDoc.nbref);
        docList = Utility.getDocumentlist(refDoc.listofref[refIdx - 1] + "|", refDoc.DOC_REF_SEPARATOR);
        DrawDocumentList();
    }

    private void DrawDocumentList() {
        tS.reset();
        adaptSize();
        if ((!docList.isEmpty()) && (docList != null) && (refDoc.nbref > 0)) {
            docListContainer.setHeading(GuiMessageConst.MSG_41 + refIdx);
            createSourceTree();
        } else {
            setMessage("error", GuiMessageConst.MSG_59);
            docListContainer.setHeading(GuiMessageConst.MSG_59);
        }

    }

    private void createSourceTree() {
        final String lS = langS.getItemText(langS.getSelectedIndex());
        final String lT = langT.getItemText(langT.getSelectedIndex());

        // Create the tree
        Tree staticTree = new Tree();
        String docName, longName, listElem;
        final String racine = lS + "/";
        int k, l;

        for (int i = 0; i < docList.size(); i++) {

            TreeItem docItem = new TreeItem();
            listElem = docList.get(i);
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

            //docItem.setStyleName("gwt-TreeItem");
            staticTree.addItem(docItem);
        }

        staticTree.addSelectionHandler(new SelectionHandler<TreeItem>() {
            @Override
            public void onSelection(SelectionEvent<TreeItem> event) {
                if (event.getSelectedItem().getText() != null) {
                    setMessage("info", GuiMessageConst.MSG_51 + event.getSelectedItem().getTitle());
                    tS.reset();
                    tS.words = Utility.getRefWords(refDoc.reftext[refIdx - 1] + " ");
                    tS.queryLength = refDoc.reftext[refIdx - 1].length();
                    tS.getTextContent(racine + event.getSelectedItem().getTitle().replace("/", "¦"), lS, lT, refDoc.reftext[refIdx - 1]);
                }
            }
        });
        staticTree.setAnimationEnabled(true);
        staticTree.ensureDebugId("cwTree-staticTree");
        staticTree.setStyleName("gwt-Tree");
        // Wrap the static tree in a DecoratorPanel
        staticTreeWrapper.add(staticTree);
    }

    public void setMessage(String type, String message) {
        msg.setStyleName("gwt-TA-" + type.toLowerCase());
        msg.setText(message);
    }

    public void adaptSize() {
        int width = getMaximumWidth();
        resultsPanel.setPixelSize(width, resultsPanel.getOffsetHeight());
        statusPanel.setPixelSize(width, statusPanel.getOffsetHeight());
        headPanel.setPixelSize(width, headPanel.getOffsetHeight());
        refpanel.setPixelSize(width, refpanel.getOffsetHeight());
        htmlWrapper.setPixelSize(width, htmlWrapper.getOffsetHeight());
        refArea.setWidth(width - 2 * W_Unit);
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

    private String getSavedFileName() {
        int idx = fileName.lastIndexOf("/") + 1;
        if (idx == 0) {
            idx = fileName.lastIndexOf("\\") + 1;
        }
        int ext = fileName.lastIndexOf(".");
        if (ext > idx) {
            return fileName.substring(idx, ext);
        } else if (idx > 0) {
            return fileName.substring(idx);
        }
        return fileName;
    }
}

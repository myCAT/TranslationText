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
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 * recherche de quotation
 */
public class QuoteBitextWidget extends Composite {

    private VerticalPanel mainWidget = new VerticalPanel();
    private HorizontalPanel hS = new HorizontalPanel();
    private HorizontalPanel hT = new HorizontalPanel();
    private Grid textAreaGrid = new Grid(2, 3);
    private Button NextHitS = new Button(GuiMessageConst.TA_BTN_NXT);
    private Button PreviousHitS = new Button(GuiMessageConst.TA_BTN_PVS);
    private Button AlignS = new Button(GuiMessageConst.TA_BTN_ALGN);
    private Button orgnS = new Button(GuiMessageConst.TA_BTN_OGN);
    private Button AlignT = new Button(GuiMessageConst.TA_BTN_ALGN);
    private Button orgnT = new Button(GuiMessageConst.TA_BTN_OGN);
    public Button save = new Button(GuiMessageConst.TA_BTN_SAVE);
    private Button NextHitT = new Button(GuiMessageConst.TA_BTN_NXT);
    private Button PreviousHitT = new Button(GuiMessageConst.TA_BTN_PVS);
    private TextArea sourceTextArea = new TextArea();
    private TextArea targetTextArea = new TextArea();
    private Label msg;
    public int indexHS = 0;
    private int indexS = 0;
    private int indexT = 0;
    // indexes de la dernière occurence du mot recherché dans le contenu du codument
    private int curIndS = 0;
    private TranslateServiceAsync rpcS;
    // Matrices (nombre de lignes, position du top, correction, position en pixel)
    private int[][] resultS;
    private int[][] resultT;
    private int[][] Positions;
    public ArrayList<String> words;
    private GwtIntMap Map;
    private GwtAlignBiText Align;
    private PopupPanel pp = new PopupPanel(false);
    private String contentS = "";
    private int height = 0;
    private int totlinesS = 0;
    private int height1 = 0;
    private int totlinesT = 0;
    private float magicS = 0;
    private float magicT = 0;
    private int pixS = 0;
    private int pixT = 0;
    private int pposS = 0;
    private int pposT = 0;
    public int queryLength = 0;
    private static String features = "menubar=no, location=no, resizable=yes, scrollbars=yes, status=no";
    private int Twidth = GuiConstant.TA_TEXTAREA_WIDTH;
    private int Theight = GuiConstant.QD_TEXTAREA_HEIGHT;
    private static final int H_Unit = 30;
    private static final int CHAR_W = GuiConstant.CHARACTER_WIDTH;
    private static final int PP_H_MIN = GuiConstant.PP_H_MIN;
    private static final int PP_H_MAX = GuiConstant.PP_H_MAX;

    public QuoteBitextWidget(Label msg) {
        this.msg = msg;
        rpcS = RpcInit.initRpc();
        setHeader();
    }

    private void setHeader() {
        initWidget(mainWidget);
        mainWidget.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
        mainWidget.add(textAreaGrid);
        textAreaGrid.setStyleName("headerNav");
        hS.add(PreviousHitS);
        hS.add(NextHitS);
        hS.add(AlignS);

        hT.add(PreviousHitT);
        hT.add(NextHitT);
        hT.add(AlignT);

        if (GuiConstant.ORIGINAL_ON) {
            hS.add(orgnS);
            hT.add(orgnT);
        }
        if (GuiConstant.SAVE_ON) {
            hS.add(save);
        }

        textAreaGrid.setWidget(0, 0, hS);
        textAreaGrid.setWidget(0, 2, hT);
        textAreaGrid.setWidget(1, 0, sourceTextArea);
        textAreaGrid.setWidget(1, 2, targetTextArea);

        setbuttonstyle(NextHitS, NextHitS.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(PreviousHitS, PreviousHitS.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(AlignS, AlignS.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(orgnS, orgnS.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(save, save.getText().length() * CHAR_W, H_Unit);

        setbuttonstyle(NextHitT, NextHitT.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(PreviousHitT, PreviousHitT.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(AlignT, AlignT.getText().length() * CHAR_W, H_Unit);
        setbuttonstyle(orgnT, orgnT.getText().length() * CHAR_W, H_Unit);

        sourceTextArea.setCursorPos(0);
        sourceTextArea.setVisible(true);
        sourceTextArea.setEnabled(true);
        sourceTextArea.setReadOnly(true);
        sourceTextArea.setCharacterWidth(this.Twidth);
        sourceTextArea.setVisibleLines(this.Theight);
        sourceTextArea.setStyleName("gwt-Textarea");
        sourceTextArea.getElement().setAttribute("spellCheck", "false");

        targetTextArea.setCursorPos(0);
        targetTextArea.setVisible(true);
        targetTextArea.setEnabled(true);
        targetTextArea.setReadOnly(true);
        targetTextArea.setCharacterWidth(this.Twidth);
        targetTextArea.setVisibleLines(this.Theight);
        targetTextArea.setStyleName("gwt-Textarea");
        targetTextArea.getElement().setAttribute("spellCheck", "false");

        pp.setAnimationEnabled(true);
        pp.setAutoHideEnabled(true);
        pp.setStyleName("focusPanel");
        pp.add(new HTML("&nbsp;"));
        ClickHandler hidepanT = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                pp.hide();
                sourceTextArea.setFocus(true);
            }
        };
        pp.sinkEvents(Event.ONCLICK);
        pp.addHandler(hidepanT, ClickEvent.getType());
        hS.setVisible(true);
        hT.setVisible(true);
    }

    public void setbuttonstyle(Button b, int w, int h) {
        b.setStyleName("x-btn-click");
        b.setPixelSize(w, h);
    }

    public void setlabelstyle(Label b, int w, int h) {
        b.setStyleName("gwt-TA-msg");
        b.setPixelSize(w, h);
    }

    /**
     * on return, draw the data to screen
     */
    public void testserver(String greeting) {

        rpcS.myMethod(greeting, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(GuiMessageConst.MSG_1);
            }

            @Override
            public void onSuccess(String result) {
                Window.alert(result);
            }
        });
    }

    public void reset() {
        pp.hide();
        curIndS = 0;
        sourceTextArea.setText("");
        targetTextArea.setText("");

        sourceTextArea.setCharacterWidth(this.Twidth);
        sourceTextArea.setVisibleLines(this.Theight);
        targetTextArea.setCharacterWidth(this.Twidth);
        targetTextArea.setVisibleLines(this.Theight);

        Positions = null;
        PreviousHitS.removeAllListeners();
        NextHitS.removeAllListeners();
        AlignS.removeAllListeners();
        orgnS.removeAllListeners();
        save.removeAllListeners();
        msg.setText("");

        PreviousHitT.removeAllListeners();
        NextHitT.removeAllListeners();
        AlignT.removeAllListeners();
        orgnT.removeAllListeners();
        msg.setText("");
    }

    public void setVariables() {
        sourceTextArea.setCharacterWidth(this.Twidth);
        sourceTextArea.setVisibleLines(this.Theight);
        targetTextArea.setCharacterWidth(this.Twidth);
        targetTextArea.setVisibleLines(this.Theight);

        sourceTextArea.setText(Align.source.content);
        targetTextArea.setText(Align.target.content);

        targetTextArea.setEnabled(true);
        PreviousHitT.enable();
        NextHitT.enable();
        AlignT.enable();
        orgnT.enable();
        AlignS.enable();

        // Matrice (nombre de lignes, position du top, correction, position en pixel)
        resultS = Align.source.positions;
        resultT = Align.target.positions;
        contentS = Align.source.content.toLowerCase();

        totlinesS = resultS[Align.source.nblines - 1][3];
        totlinesT = resultT[Align.target.nblines - 1][3];
        height1 = targetTextArea.getElement().getScrollHeight();
        height = sourceTextArea.getElement().getScrollHeight();

        pixS = sourceTextArea.getOffsetHeight()/sourceTextArea.getVisibleLines();
        int scrollines = height / pixS;
        magicS = (float) (scrollines - totlinesS) / (float) (scrollines - Align.source.Ncal) + 1f;
        pposS = sourceTextArea.getOffsetWidth() - pixS;

        pixT = targetTextArea.getOffsetHeight()/targetTextArea.getVisibleLines();
        int scrollines1 = height1 / pixT;
        magicT = (float) (scrollines1 - totlinesT) / (float) (scrollines1 - Align.target.Ncal) + 1f;
        pposT = targetTextArea.getOffsetWidth() - pixT;
    }

    public void setVariablesMono() {

        sourceTextArea.setCharacterWidth(this.Twidth);
        sourceTextArea.setVisibleLines(this.Theight);
        sourceTextArea.setText(Align.source.content);

        targetTextArea.setEnabled(false);
        PreviousHitT.disable();
        NextHitT.disable();
        AlignT.disable();
        orgnT.disable();
        AlignS.disable();

        // Matrice (nombre de lignes, position du top, correction, position en pixel)
        resultS = Align.source.positions;
        contentS = Align.source.content;

        totlinesS = resultS[resultS.length - 1][3];
        height = sourceTextArea.getElement().getScrollHeight();

        pixS = height / totlinesS;
        int scrollines = height / pixS;
        magicS = (float) (scrollines - totlinesS) / (float) (scrollines - Align.source.Ncal) + 1f;
    }

    public void showpanel(boolean source, int hight, int index) {
        if (hight < PP_H_MIN) {
            hight = PP_H_MIN;
        }
        if (hight > PP_H_MAX) {
            hight = PP_H_MAX;
        }
        int lineNum, restOfLines;
        if (source) {
            restOfLines = (resultS[resultS.length - 1][3] - resultS[index][3]);
            if (resultS[index][3] < (sourceTextArea.getVisibleLines() / 2)) {
                lineNum = resultS[index][3];
            } else {
                if (restOfLines > (sourceTextArea.getVisibleLines() / 2)) {
                    lineNum = (sourceTextArea.getVisibleLines() / 2 - 2);
                } else {
                    if (restOfLines > 0) {
                        lineNum = sourceTextArea.getVisibleLines() - restOfLines - 2;
                    } else {
                        lineNum = sourceTextArea.getVisibleLines() - (sourceTextArea.getVisibleLines() / 4) - 2;
                    }
                }
            }
            pp.setPopupPosition(sourceTextArea.getAbsoluteLeft() - 2, (lineNum * pixS) + sourceTextArea.getAbsoluteTop());
            if (((lineNum + hight) * pixS) < sourceTextArea.getOffsetHeight()) {
                pp.setPixelSize(pposS, pixS * hight);
            } else {
                pp.setPixelSize(pposS, sourceTextArea.getOffsetHeight() - (pixS * lineNum));
            }
            pp.show();
        } else {
            restOfLines = (resultT[resultT.length - 1][3] - resultT[index][3]);
            if (resultT[index][3] < (targetTextArea.getVisibleLines() / 2)) {
                lineNum = resultT[index][3];
            } else {
                if (restOfLines > (targetTextArea.getVisibleLines() / 2)) {
                    lineNum = (targetTextArea.getVisibleLines() / 2 - 2);
                } else {
                    if (restOfLines > 0) {
                        lineNum = targetTextArea.getVisibleLines() - restOfLines - 2;
                    } else {
                        lineNum = targetTextArea.getVisibleLines() - (targetTextArea.getVisibleLines() / 4) - 2;
                    }
                }
            }
//            Window.alert("restOfLines = " + restOfLines
//                    + "\nlineNum = " + lineNum
//                    + "\nsourceTextArea.getAbsoluteTop() = " + targetTextArea.getAbsoluteTop()
//                    + "\nPixT = " + pixT
//                    + "\n(lineNum * pixT) + targetTextArea.getAbsoluteTop() = " + ((lineNum * pixT) + targetTextArea.getAbsoluteTop()));
            pp.setPopupPosition(targetTextArea.getAbsoluteLeft() - 2, (lineNum * pixT) + targetTextArea.getAbsoluteTop());

            if (((lineNum + hight) * pixT) < targetTextArea.getOffsetHeight()) {
                pp.setPixelSize(pposT, pixT * hight);
            } else {
                pp.setPixelSize(pposT, targetTextArea.getOffsetHeight() - (pixT * lineNum));
            }
            pp.show();
        }
    }

    public void setNetScapePos(int idxS, int idxT, int h) {
        int lin = resultS[idxS][3] + (resultS[idxS][0] / 2);
        int ln = resultS[idxS][4];
        int lin1 = resultT[idxT][3] + (resultT[idxT][0] / 2);
        int ln1 = resultT[idxT][4];

        float frtop1 = ((lin1 - h) * pixT * magicT) + (ln1 * pixT * (1 - magicT));
        float frtop = ((lin - h) * pixS * magicS) + (ln * pixS * (1 - magicS));
        int posf = (frtop > height) ? height : (int) frtop;
        int posf1 = (frtop1 > height1) ? height1 : (int) frtop1;
        sourceTextArea.setFocus(true);
        sourceTextArea.getElement().setScrollTop(posf);
        targetTextArea.getElement().setScrollTop(posf1);
    }

    public void setNetScapePosT(int idxS, int idxT, int h) {
        int lin = resultS[idxS][3] + (resultS[idxS][0] / 2);
        int ln = resultS[idxS][4];
        int lin1 = resultT[idxT][3] + (resultT[idxT][0] / 2);
        int ln1 = resultT[idxT][4];

        float frtop1 = ((lin1 - h) * pixT * magicT) + (ln1 * pixT * (1 - magicT));
        float frtop = ((lin - h) * pixS * magicS) + (ln * pixS * (1 - magicS));
        int posf = (frtop > height) ? height : (int) frtop;
        int posf1 = (frtop1 > height1) ? height1 : (int) frtop1;
        targetTextArea.setFocus(true);
        targetTextArea.getElement().setScrollTop(posf1);
        sourceTextArea.getElement().setScrollTop(posf);
    }

    public void setNetScapePosMono(int idxS, int h) {
        int lin = resultS[idxS][3] + (resultS[idxS][0] / 2);
        int ln = resultS[idxS][4];

        float frtop = ((lin - h - 2) * pixS * magicS) + (ln * pixS * (1 - magicS));
        int posf = ((frtop) > height) ? height : (int) frtop;
        sourceTextArea.getElement().setScrollTop(posf);
    }

    public void nextHit() {
        pp.hide();
        if ((Positions.length - 1 == 0)) {
            setMessage("info", GuiMessageConst.MSG_38);
        } else if (curIndS == 0) {
            setMessage("info", GuiMessageConst.MSG_37);
        } else if (curIndS == (Positions.length - 1)) {
            setMessage("info", GuiMessageConst.MSG_38);
        }
        if (words.size() > 1000) {
            setMessage("warning", GuiMessageConst.MSG_34);
        }
        if (curIndS < Positions.length) {
            int pos = Positions[curIndS][0];
            indexS = Utility.getInd(pos, resultS);
            indexT = Map.from[indexS];
            int idxlast = Map.from[Utility.getInd(pos + queryLength / 4, resultS)];
            showpanel(false, Utility.getln(indexT, idxlast, resultT), indexT);

            if ((Window.Navigator.getUserAgent().contains("MSIE 7.0")) || (Window.Navigator.getUserAgent().contains("MSIE 8.0"))) {
                if (pos > 0) {
                    pos += indexS;
                }
                indexS = Utility.getInd(pos, resultS);
                indexT = Map.from[indexS];
                int idx = resultS[indexS][2];
                int idxt = resultT[indexT][2];
                sourceTextArea.setCursorPos(0);
                targetTextArea.setCursorPos(0);
                targetTextArea.setCursorPos(idxt);
                sourceTextArea.setCursorPos(idx);
            } else {
                setNetScapePos(indexS, indexT, (sourceTextArea.getVisibleLines() / 2));
            }
            sourceTextArea.setSelectionRange(pos, Positions[curIndS][1]);
        }
        sourceTextArea.setFocus(true);
    }

    public void previousHit() {
        pp.hide();
        if ((Positions.length - 1 == 0)) {
            setMessage("info", GuiMessageConst.MSG_38);
        } else if (curIndS == (Positions.length - 1)) {
            setMessage("info", GuiMessageConst.MSG_38);
        } else if (curIndS == 0) {
            setMessage("info", GuiMessageConst.MSG_37);
        }
        if (curIndS >= 0) {
            int pos = Positions[curIndS][0];
            indexS = Utility.getInd(pos, resultS);
            indexT = Map.from[indexS];
            int idxlast = Map.from[Utility.getInd(pos + queryLength / 4, resultS)];
            showpanel(false, Utility.getln(indexT, idxlast, resultT), indexT);

            if ((Window.Navigator.getUserAgent().contains("MSIE 7.0")) || (Window.Navigator.getUserAgent().contains("MSIE 8.0"))) {
                if (pos > 0) {
                    pos += indexS;
                }
                indexS = Utility.getInd(pos, resultS);
                indexT = Map.from[indexS];
                int idx = resultS[indexS][2];
                int idxt = resultT[indexT][2];
                sourceTextArea.setCursorPos(0);
                targetTextArea.setCursorPos(0);
                targetTextArea.setCursorPos(idxt);
                sourceTextArea.setCursorPos(idx);
            } else {
                setNetScapePos(indexS, indexT, (sourceTextArea.getVisibleLines() / 2));
            }
            sourceTextArea.setSelectionRange(pos, Positions[curIndS][1]);
        }
        sourceTextArea.setFocus(true);
    }

    public void nextHitMono() {
        if ((Positions.length - 1 == 0)) {
            setMessage("info", GuiMessageConst.MSG_38);
        } else if (curIndS == 0) {
            setMessage("info", GuiMessageConst.MSG_37);
        } else if (curIndS == (Positions.length - 1)) {
            setMessage("info", GuiMessageConst.MSG_38);
        }
        if (words.size() > 1000) {
            setMessage("warning", GuiMessageConst.MSG_34);
        }
        if (curIndS < Positions.length) {
            int pos = Positions[curIndS][0];
            indexS = Utility.getInd(pos, resultS);

            if ((Window.Navigator.getUserAgent().contains("MSIE 7.0")) || (Window.Navigator.getUserAgent().contains("MSIE 8.0"))) {
                if (pos > 0) {
                    pos += indexS;
                }
                indexS = Utility.getInd(pos, resultS);
                int idx = resultS[indexS][2];
                sourceTextArea.setCursorPos(0);
                sourceTextArea.setCursorPos(idx);
            } else {
                setNetScapePosMono(indexS, (sourceTextArea.getVisibleLines() / 2));
            }
            sourceTextArea.setSelectionRange(pos, Positions[curIndS][1]);
        }
        sourceTextArea.setFocus(true);
    }

    public void previousHitMono() {
        if ((Positions.length - 1 == 0)) {
            setMessage("info", GuiMessageConst.MSG_38);
        } else if (curIndS == (Positions.length - 1)) {
            setMessage("info", GuiMessageConst.MSG_38);
        } else if (curIndS == 0) {
            setMessage("info", GuiMessageConst.MSG_37);
        }
        if (curIndS >= 0) {
            int pos = Positions[curIndS][0];
            indexS = Utility.getInd(pos, resultS);

            if ((Window.Navigator.getUserAgent().contains("MSIE 7.0")) || (Window.Navigator.getUserAgent().contains("MSIE 8.0"))) {
                if (pos > 0) {
                    pos += indexS;
                }
                indexS = Utility.getInd(pos, resultS);
                int idx = resultS[indexS][2];
                sourceTextArea.setCursorPos(0);
                sourceTextArea.setCursorPos(idx);
            } else {
                setNetScapePosMono(indexS, (sourceTextArea.getVisibleLines() / 2));
            }
            sourceTextArea.setSelectionRange(pos, Positions[curIndS][1]);
        }
        sourceTextArea.setFocus(true);
    }

    public void ClearHitsEvents() {
        curIndS = 0;
        Positions = null;
        PreviousHitS.removeAllListeners();
        NextHitS.removeAllListeners();
        PreviousHitT.removeAllListeners();
        NextHitT.removeAllListeners();
    }

    public void AddHitsEvents() {

        // Handler of the going to the next line in the source text
        NextHitS.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (curIndS < Positions.length - 1) {
                    curIndS++;
                    setMessage("info", GuiMessageConst.MSG_36 + (1 + curIndS));
                    nextHit();
                } else {
                    nextHit();
                }

            }
        });

        // Handler of the going to the previous line in the source text
        PreviousHitS.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (curIndS > 0) {
                    curIndS--;
                    setMessage("info", GuiMessageConst.MSG_36 + (1 + curIndS));
                    previousHit();
                } else {
                    previousHit();
                }

            }
        });
    }

    public void AddHitsEventsMono() {

        // Handler of the going to the next line in the source text
        NextHitS.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (curIndS < Positions.length - 1) {
                    curIndS++;
                    setMessage("info", GuiMessageConst.MSG_36 + (1 + curIndS));
                    nextHitMono();
                } else {
                    nextHitMono();
                }

            }
        });

        // Handler of the going to the previous line in the source text
        PreviousHitS.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                if (curIndS > 0) {
                    curIndS--;
                    setMessage("info", GuiMessageConst.MSG_36 + (1 + curIndS));
                    previousHitMono();
                } else {
                    previousHitMono();
                }

            }
        });
    }

    public void AddOtherEvents() {

        // Handler of the going to align the line in the source text
        AlignS.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                pp.hide();
                int posi = sourceTextArea.getCursorPos();
                int len = sourceTextArea.getSelectedText().length();
                indexS = Utility.getInd(posi, resultS);
                indexT = Map.from[indexS];
                showpanel(false, resultT[indexT][0] + 1, indexT);

                if ((Window.Navigator.getUserAgent().contains("MSIE 7.0")) || (Window.Navigator.getUserAgent().contains("MSIE 8.0"))) {
                    int idx = resultS[indexS][2];
                    int idxt = resultT[indexT][2];
                    sourceTextArea.setCursorPos(0);
                    targetTextArea.setCursorPos(0);
                    sourceTextArea.setCursorPos(idx);
                    targetTextArea.setCursorPos(idxt);
                } else {
                    setNetScapePos(indexS, indexT, (sourceTextArea.getVisibleLines() / 2));
                }
                if (len > 1) {
                    sourceTextArea.setSelectionRange(posi, len);
                } else {
                    sourceTextArea.setSelectionRange(posi, 1);
                }
                sourceTextArea.setFocus(true);
            }
        });

        // Handler of the going to align the line in the target text
        AlignT.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                pp.hide();
                int posi = targetTextArea.getCursorPos();
                int len = targetTextArea.getSelectedText().length();
                indexT = Utility.getInd(posi, resultT);
                indexS = Map.to[indexT];
                showpanel(true, resultS[indexS][0] + 1, indexS);

                if ((Window.Navigator.getUserAgent().contains("MSIE 7.0")) || (Window.Navigator.getUserAgent().contains("MSIE 8.0"))) {
                    int idx = resultS[indexS][2];
                    int idxt = resultT[indexT][2];
                    sourceTextArea.setCursorPos(0);
                    targetTextArea.setCursorPos(0);
                    sourceTextArea.setCursorPos(idx);
                    targetTextArea.setCursorPos(idxt);
                } else {
                    setNetScapePosT(indexS, indexT, (targetTextArea.getVisibleLines() / 2));
                }
                if (len > 1) {
                    targetTextArea.setSelectionRange(posi, len);
                } else {
                    targetTextArea.setSelectionRange(posi, 1);
                }
                targetTextArea.setFocus(true);
            }
        });

        save.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                setMessage("info", GuiMessageConst.MSG_35);
                MyCatDownload.downloadZipFromServlet(Align.source.uri, msg);
            }
        });

        orgnS.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                rpcS.getOriginalUrl(Align.source.uri, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        setMessage("error", GuiMessageConst.MSG_4);
                    }

                    @Override
                    public void onSuccess(String result) {
                        Window.open(result, "Original", features);
                    }
                });
            }
        });

        orgnT.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                rpcS.getOriginalUrl(Align.target.uri.substring(20), new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        setMessage("error", GuiMessageConst.MSG_5);
                    }

                    @Override
                    public void onSuccess(String result) {
                        Window.open(result, "Original", features);
                    }
                });
            }
        });
    }

    public void AddOtherEventsMono() {
        save.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                setMessage("info", GuiMessageConst.MSG_35);
                MyCatDownload.downloadZipFromServlet(Align.source.uri, msg);
            }
        });

        orgnS.addListener(Events.OnClick, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                rpcS.getOriginalUrl(Align.source.uri, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        setMessage("error", GuiMessageConst.MSG_4);
                    }

                    @Override
                    public void onSuccess(String result) {
                        Window.open(result, "Original", features);
                    }
                });
            }
        });
    }

    public void getTextContent(String File1, String langS, String langT, String Query) {

        if (langT.contains("AR")) {
            targetTextArea.setDirection(Direction.RTL);
        }
        if (langS.contains("AR")) {
            sourceTextArea.setDirection(Direction.RTL);
        }

        // remote procedure call to the server to get the content of the text areas
        rpcS.getContent(File1, langS, langT, Query, sourceTextArea.getCharacterWidth() - 5, sourceTextArea.getVisibleLines(), new AsyncCallback<GwtAlignBiText>() {
            @Override
            public void onFailure(Throwable caught) {
                setMessage("error", GuiMessageConst.MSG_1);
            }

            @Override
            public void onSuccess(GwtAlignBiText result) {
                Align = result;
                setMessage("info", GuiMessageConst.MSG_9 + Align.source.uri);
                if (Align.target.content.contains("** ERROR")) {
                    SetMonoTextBehaviour();
                } else {
                    SetBiTextBehaviour();
                }
            }
        });
    }

    public void SetBiTextBehaviour() {
        Map = Align.map;

        ClearHitsEvents();
        setVariables();
        AddOtherEvents();
        curIndS = 0;
        Positions = null;
        getPositions(contentS, words, queryLength);
    }

    public void SetMonoTextBehaviour() {
        ClearHitsEvents();
        setVariablesMono();
        AddOtherEventsMono();
        curIndS = 0;
        Positions = null;
        getMonoPositions(contentS, words, queryLength);
    }

    public void getPositions(String content, ArrayList<String> Query, int queryLn) {
        if ((!Query.isEmpty()) && !(Query == null)) {
            rpcS.getRefWordsPos(content, Query, queryLn, GuiConstant.REF_FACTOR, GuiConstant.REF_MIN_LN, new AsyncCallback<int[][]>() {
                @Override
                public void onFailure(Throwable caught) {
                    setMessage("error", GuiMessageConst.MSG_10);
                }

                @Override
                public void onSuccess(int[][] result) {
                    ClearHitsEvents();
                    Positions = result;
                    if (Positions[0][0] > -1) {
                        if (words.size() > GuiConstant.MAX_SEARCH_SIZE) {
                            setMessage("warning", GuiMessageConst.MSG_34);
                        }
                        AddHitsEvents();
                        nextHit();
                        sourceTextArea.setFocus(true);
                    } else {
                        setMessage("error", GuiMessageConst.MSG_33);
                    }
                }
            });
        }
    }

    public void getMonoPositions(String content, ArrayList<String> Query, int queryLn) {
        if ((!Query.isEmpty()) && !(Query == null)) {
            rpcS.getRefWordsPos(content, Query, queryLn, GuiConstant.REF_FACTOR, GuiConstant.REF_MIN_LN, new AsyncCallback<int[][]>() {
                @Override
                public void onFailure(Throwable caught) {
                    setMessage("error", GuiMessageConst.MSG_10);
                }

                @Override
                public void onSuccess(int[][] result) {
                    ClearHitsEvents();
                    Positions = result;
                    if (Positions[0][0] > -1) {
                        if (words.size() > GuiConstant.MAX_SEARCH_SIZE) {
                            setMessage("warning", GuiMessageConst.MSG_34);
                        }
                        AddHitsEventsMono();
                        nextHitMono();
                        sourceTextArea.setFocus(true);
                    } else {
                        setMessage("error", GuiMessageConst.MSG_33);
                    }
                }
            });
        }
    }

    public void setMessage(String type, String message) {
        msg.setStyleName("gwt-TA-" + type.toLowerCase());
        msg.setText(message);
    }
}

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

/**
 * Class for declaring the constants of the client
 *
 */
public class GuiConstant {

    // Boolean variable for setting automatically
    // the size of areas wrt the screen size, not implemented yet.
    public static boolean AUTO_ON;
    public static int EXP_DAYS;
    public static boolean ORIGINAL_ON;
    public static boolean PATH_ON;
    public static boolean FILE_NAME_RIGHT;
    public static boolean ONLY_ON_FILE_NAME;
    public static boolean BITEXT_ONLY;
    public static boolean SAVE_ON;
    public static boolean MAXIMIZE_ON;
    public static boolean TA_HILITE_OVER_CR;
    public static boolean CHOOSE_GUI_LANG;
    public static String CHOOSE_GUI_LANG_LIST;
    public static boolean EXACT_FLG = false;
    public static boolean EXACT_NBR_FLG = false;
    public static int TA_LINE_HEIGHT;
    public static int TA_TEXTAREA_WIDTH;
    public static int TA_TEXTAREA_HEIGHT;
    public static int QD_HTMLAREA_HEIGHT;
    public static int QD_TEXTAREA_HEIGHT;
    public static int DOC_LIST_WIDTH;
    public static int DOC_LIST_HEIGHT;
    public static int QD_DOC_LIST_HEIGHT;
    public static int MAX_RESPONSE;
    public static int MAX_BROWSE;
    public static int MAX_SEARCH_SIZE;
    public static int MIN_OCCU;
    public static int MAX_OCCU;
    public static float REF_FACTOR;
    public static int REF_MIN_LN;
    public static int CHARACTER_WIDTH;
    public static int PP_H_MIN;
    public static int PP_H_MAX;
    public static int TA_NEAR_AVG_TERM_CHAR;
    public static int NEAR_DISTANCE;
    public static String JOBS_ITEMS;
    public static String QUOTE_DETECTOR_LBL;
    public static String TEXT_ALIGNER_LBL;
    public static String QD_FILE_EXT;
    public static String QD_GENERAL_EXT;
    public static String QD_HELP_URL;
    public static String TA_HELP_URL;
    public static String W_OPEN_FEATURES;
    public static String OLANTO_URL;
    public static String LOGO_PATH;
    public static String LOGO_URL;
    public static String TA_DL_SORTBY;
    public static String FEEDBACK_MAIL;

    /**
     * **********************************************************************************
     */
    public static String show() {
        return "GUI"
                + "\nPARAMETERS"
                + "\n    AUTO_ON: " + AUTO_ON
                + "\n    EXP_DAYS: " + EXP_DAYS
                + "\n    ORIGINAL_ON: " + ORIGINAL_ON
                + "\n    SAVE_ON: " + SAVE_ON
                + "\n    PATH_ON: " + PATH_ON
                + "\n    FILE_NAME_RIGHT: " + FILE_NAME_RIGHT
                + "\n    ONLY_ON_FILE_NAME: " + ONLY_ON_FILE_NAME
                + "\n    QD_FILE_EXT: " + QD_FILE_EXT
                + "\n    QD_HELP_URL: " + QD_HELP_URL
                + "\n    TA_HELP_URL: " + TA_HELP_URL
                + "\n    W_OPEN_FEATURES: " + W_OPEN_FEATURES
                + "\n    TA_TEXTAREA_WIDTH: " + TA_TEXTAREA_WIDTH
                + "\n    TA_TEXTAREA_HEIGHT: " + TA_TEXTAREA_HEIGHT
                + "\n    QD_HTMLAREA_HEIGHT: " + QD_HTMLAREA_HEIGHT
                + "\n    QD_TEXTAREA_HEIGHT: " + QD_TEXTAREA_HEIGHT
                + "\n    DOC_LIST_WIDTH: " + DOC_LIST_WIDTH
                + "\n    DOC_LIST_HEIGHT: " + DOC_LIST_HEIGHT
                + "\n    QD_DOC_LIST_HEIGHT: " + QD_DOC_LIST_HEIGHT
                + "\n    MAX_RESPONSE: " + MAX_RESPONSE
                + "\n    MAX_BROWSE: " + MAX_BROWSE
                + "\n    MAX_SEARCH_SIZE: " + MAX_SEARCH_SIZE
                + "\n    JOBS_ITEMS: " + JOBS_ITEMS
                + "\n    MIN_OCCU: " + MIN_OCCU
                + "\n    MAX_OCCU: " + MAX_OCCU
                + "\n    QUOTE_DETECTOR: " + QUOTE_DETECTOR_LBL
                + "\n    TEXT_ALIGNER: " + TEXT_ALIGNER_LBL
                + "\n    TA_DL_SORTBY: " + TA_DL_SORTBY;
    }
}

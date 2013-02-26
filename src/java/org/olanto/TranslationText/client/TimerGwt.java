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

import com.google.gwt.user.client.ui.TextArea;

/**
 * timer
 */
public class TimerGwt {

    long time0;
    String timerName;
    TextArea render;

    TimerGwt(String timerName, TextArea render) {
        render.setText(render.getText() + "\nStart " + timerName);
        time0 = System.currentTimeMillis();
        this.timerName = timerName;
        this.render = render;
    }

    public void stop() {
        render.setText(render.getText() + "\nStop " + timerName + " -> " + (System.currentTimeMillis() - time0) + " ms");
        render.setCursorPos(render.getText().length());
    }
}

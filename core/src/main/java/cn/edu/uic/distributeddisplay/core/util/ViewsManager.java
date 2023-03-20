/**
 * The ViewsManger class registers the references of the views
 *
 * @author Team 3
 * @version 1.0
 */

package cn.edu.uic.distributeddisplay.core.util;

import cn.edu.uic.distributeddisplay.core.view.DisplayWindowView;

public class ViewsManager {

    private static DisplayWindowView displayWindowView;

    public static DisplayWindowView getDisplayView() {
        return displayWindowView;
    }

    public static void setDisplayView(DisplayWindowView displayWindowView) {
        // Close the old DisplayView
        if (ViewsManager.displayWindowView != null) {
            ViewsManager.displayWindowView.dispose();
        }
        ViewsManager.displayWindowView = displayWindowView;
    }
}

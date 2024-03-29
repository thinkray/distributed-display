/**
 * The DefaultConst class stores all the default values for the program
 *
 * @author Team 3
 * @version 1.0
 */

package cn.edu.uic.distributeddisplay.core.util;

import java.awt.*;

final public class DefaultConst {

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    public static final int FIT = 0;

    public static final int STRETCH = 1;

    public static final int RAW = 2;

    public static final String DEFAULT_TEXT = "";

    public static final int DEFAULT_LETTER_SPACING = -20;

    public static final int DEFAULT_MARGIN = 25;

    public static final int DEFAULT_H_OFFSET = 0;

    public static final int DEFAULT_V_OFFSET = 0;

    public static final Font DEFAULT_FONT = new Font("Calibri", Font.PLAIN, 72);

    public static final Color DEFAULT_COLOR = Color.BLACK;

    public static final int DEFAULT_TEXT_ORIENTATION = HORIZONTAL;

    public static final int DEFAULT_FIT_STYLE = FIT;

    public static final String DEFAULT_BACKGROUND_IMG_DIR = "";

    public static final Insets INSETS_LEFT = new Insets(0, 25, 10, 0);

    public static final Insets INSETS_CENTER = new Insets(0, 0, 0, 0);

    public static final Insets INSETS_RIGHT = new Insets(0, 0, 0, 25);

    public static final int INVALID_SESSION = 404;

    public static final int SESSION_RENEWED_NO_NEW_CONFIG = 304;

    public static final int SESSION_RENEWED_NEW_CONFIG_AVAILABLE = 200;

    public static final int PREVIEW_MODE = 0;

    public static final int DISPLAY_MODE = 1;

    public static final int SERVICE_MODE = 2;

    public static final String DEFAULT_LISTEN_ADDRESS = "0.0.0.0";

    public static final int CLIENT_NOT_CONNECTED = 0;

    public static final int CLIENT_CONNECTED = 1;

    public static final int CLIENT_DISCONNECTED = 2;

    public static final int CLIENT_RETRYING = 3;

    public static final int CLIENT_MODE_FIRST_TIME_CHECK_IN = 0;

    public static final int CLIENT_MODE_RECHECK_IN = 1;

    public static final int CLIENT_MODE_HEARTBEAT = 2;
}

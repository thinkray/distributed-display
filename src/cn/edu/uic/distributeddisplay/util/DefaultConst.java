/**
 * The DefaultConst class stores all the default values for the program
 *
 * @author Bohui WU
 * @version 1.0
 * @since 12/28/2019
 */

package cn.edu.uic.distributeddisplay.util;

import java.awt.*;

public class DefaultConst {

    public static final String DEFAULT_NAME = LangManger.get("default_name");

    public static final String DEFAULT_TEXT = LangManger.get("default_text");

    public static final int DEFAULT_LETTER_SPACING = -20;

    public static final int DEFAULT_MARGIN = 25;

    public static final int DEFAULT_H_OFFSET = 0;

    public static final int DEFAULT_V_OFFSET = 0;

    public static final Font DEFAULT_FONT = new Font("Calibri", Font.PLAIN, 12);

    public static final Color DEFAULT_COLOR = Color.BLACK;

    public static final int DEFAULT_TEXT_ORIENTATION = 0;

    public static final int DEFAULT_FIT_STYLE = 0;

    public static final Insets INSETS_LEFT = new Insets(0, 25, 10, 0);

    public static final Insets INSETS_CENTER = new Insets(0, 0, 0, 0);

    public static final Insets INSETS_RIGHT = new Insets(0, 0, 0, 25);

}

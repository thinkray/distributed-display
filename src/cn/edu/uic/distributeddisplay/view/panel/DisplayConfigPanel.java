/**
 * The panel provides an interface for adjusting the parameters of the profile
 *
 * @author Team 3
 * @version 1.8
 */

package cn.edu.uic.distributeddisplay.view.panel;

import cn.edu.uic.distributeddisplay.util.CommonUtils;
import cn.edu.uic.distributeddisplay.util.DefaultConst;
import cn.edu.uic.distributeddisplay.util.LangManger;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class DisplayConfigPanel extends JPanel {

    private Container centerPanel;
    private JScrollPane centerPanelScrollPane;
    private JPanel bottomPanel;

    private JComboBox<String> fontComboBox;
    private JComboBox<Integer> fontSizeComboBox;
    private JComboBox<String> fontStyleComboBox;
    private JComboBox<String> colorComboBox;
    private JComboBox<String> textDirectionComboBox;
    private JTextArea textArea;
    private JSlider letterSpacingSlider;
    private JSlider marginSlider;
    private JSlider vOffsetSlider;
    private JSlider hOffsetSlider;
    private JTextField imageDirectoryTextField;
    private JButton selectImageDirectoryButton;
    private JComboBox<String> imageFitStyleComboBox;
    Hashtable<Integer, Color> colorHashtable;

    private boolean initializing = true;

    //    JButton confirmButton;
    JButton applyButton;
    JButton previewButton;
//    JButton cancelButton;


    public DisplayConfigPanel() {
        // Initialize center panel
        initCenterPanelComponents();
        initCenterPanelLayout();

        // Initialize bottom panel
        initBottomPanel();

        // The panel is disabled until a profile is selected
        setPanelEnabled(false);
    }

    public JButton getSelectImageDirectoryButton() {
        return selectImageDirectoryButton;
    }

    private void initCenterPanelComponents() {
        initFontComboBox();
        initColorComboBox();
        initTextDirectionComboBox();
        textArea = new JTextArea();
        textArea.setRows(3);
        textArea.setLineWrap(true);
        textArea.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(98, 98, 98)));
        initSliders();
        imageDirectoryTextField = new JTextField();
        selectImageDirectoryButton = new JButton("...");
        initImageFitStyleComboBox();
    }

    private void initSliders() {
        // Margin Slider
        marginSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 20);
        // Create the label table
        Hashtable<Integer, JLabel> marginSliderLabelTable = new Hashtable<>();
        marginSliderLabelTable.put(50, new JLabel(LangManger.get("small")));
        marginSliderLabelTable.put(25, new JLabel(LangManger.get("moderate")));
        marginSliderLabelTable.put(0, new JLabel(LangManger.get("large")));
        marginSlider.setLabelTable(marginSliderLabelTable);
        marginSlider.setPaintLabels(true);
        marginSlider.setInverted(true);

        // Letter spacing slider
        Hashtable<Integer, JLabel> verticalLetterSpacingLabelTable = new Hashtable<>();
        verticalLetterSpacingLabelTable.put(-50, new JLabel(LangManger.get("small")));
        verticalLetterSpacingLabelTable.put(-20, new JLabel(LangManger.get("moderate")));
        verticalLetterSpacingLabelTable.put(10, new JLabel(LangManger.get("large")));
        letterSpacingSlider = new JSlider(JSlider.HORIZONTAL, -50, 10, -20);
        letterSpacingSlider.setLabelTable(verticalLetterSpacingLabelTable);
        letterSpacingSlider.setPaintLabels(true);
        letterSpacingSlider.setEnabled(false);

        // The label table for vertical and horizontal offset sliders
        Hashtable<Integer, JLabel> hSliderTable = new Hashtable<>();
        hSliderTable.put(-25, new JLabel(LangManger.get("left")));
        hSliderTable.put(0, new JLabel(LangManger.get("center")));
        hSliderTable.put(25, new JLabel(LangManger.get("right")));
        Hashtable<Integer, JLabel> vSliderTable = new Hashtable<>();
        vSliderTable.put(-25, new JLabel(LangManger.get("top")));
        vSliderTable.put(0, new JLabel(LangManger.get("center")));
        vSliderTable.put(25, new JLabel(LangManger.get("bottom")));

        // Vertical Offset Slider
        vOffsetSlider = new JSlider(JSlider.HORIZONTAL, -25, 25, 0);
        vOffsetSlider.setLabelTable(vSliderTable);
        vOffsetSlider.setPaintLabels(true);
        vOffsetSlider.setInverted(true);
        vOffsetSlider.setSnapToTicks(true);

        // Horizontal Offset Slider
        hOffsetSlider = new JSlider(JSlider.HORIZONTAL, -25, 25, 0);
        hOffsetSlider.setLabelTable(hSliderTable);
        hOffsetSlider.setPaintLabels(true);
        hOffsetSlider.setSnapToTicks(true);
    }

    private void initCenterPanelLayout() {
        setBorder(BorderFactory.createTitledBorder(LangManger.get("current_profile")));
        setLayout(new BorderLayout());
        centerPanel = new Container();
        centerPanel.setLayout(new GridBagLayout());

        // Line 1: Font
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("font") + ": "), CommonUtils.getGridBagConstraints(0, 1, 1, 0.2,
                1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(fontComboBox, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0, DefaultConst.INSETS_RIGHT));

        // Line 2: Font Size
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("size") + ": "), CommonUtils.getGridBagConstraints(0, 1, 1, 0.2,
                1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(fontSizeComboBox, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0,
                DefaultConst.INSETS_RIGHT));

        // Line 3: Font Style
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("style") + ": "), CommonUtils.getGridBagConstraints(0, 1, 1, 0.2,
                1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(fontStyleComboBox, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0,
                DefaultConst.INSETS_RIGHT));

        // Line 4: Color
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("color") + ": "), CommonUtils.getGridBagConstraints(0, 1, 1, 0.2,
                1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(colorComboBox, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0, DefaultConst.INSETS_RIGHT));

        // Line 5: TextDirection
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("text_direction") + ": "), CommonUtils.getGridBagConstraints(0, 1,
                1, 0.2, 1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(textDirectionComboBox, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0,
                DefaultConst.INSETS_RIGHT));

        // Line 6: Text
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("text") + ": "), CommonUtils.getGridBagConstraints(0, 1, 3, 0.2,
                1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(textArea, CommonUtils.getGridBagConstraints(1, 8, 3, 1.6, 1.0, DefaultConst.INSETS_RIGHT));

        // Line 7: Vertical letter spacing
        CommonUtils.gbcNewLine(3);
        centerPanel.add(new JLabel(LangManger.get("vertical_letter_spacing") + ": "),
                CommonUtils.getGridBagConstraints(0, 1, 1, 0.2, 1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(letterSpacingSlider, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0,
                DefaultConst.INSETS_RIGHT));

        // Line 8: Vertical Margin
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("line_space") + ": "), CommonUtils.getGridBagConstraints(0, 1, 1,
                0.2, 1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(marginSlider, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0, DefaultConst.INSETS_RIGHT));

        // Line 9: Vertical Offset
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("vertical_offset") + ": "), CommonUtils.getGridBagConstraints(0, 1,
                1, 0.2, 1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(vOffsetSlider, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0, DefaultConst.INSETS_RIGHT));

        // Line 10: Horizontal Offset
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("horizontal_offset") + ": "), CommonUtils.getGridBagConstraints(0,
                1, 1, 0.2, 1.0, DefaultConst.INSETS_LEFT));
        centerPanel.add(hOffsetSlider, CommonUtils.getGridBagConstraints(1, 8, 1, 1.6, 1.0, DefaultConst.INSETS_RIGHT));

        // Line 11: Background image
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("image") + ": "), CommonUtils.getGridBagConstraints(0, 1, 1, 0.2,
                0.8, DefaultConst.INSETS_LEFT));
        centerPanel.add(imageDirectoryTextField, CommonUtils.getGridBagConstraints(1, 1, 1, 1.6, 0.8,
                DefaultConst.INSETS_CENTER));
        centerPanel.add(selectImageDirectoryButton, CommonUtils.getGridBagConstraints(2, 1, 1, 0.1, 0.8,
                DefaultConst.INSETS_RIGHT));
        selectImageDirectoryButton.setFont(new Font(null, Font.BOLD, 8));

        // Line 12: TextDirection
        CommonUtils.gbcNewLine();
        centerPanel.add(new JLabel(LangManger.get("fit") + ": "), CommonUtils.getGridBagConstraints(0, 1, 1, 0.2, 1.0
                , DefaultConst.INSETS_LEFT));
        centerPanel.add(imageFitStyleComboBox, CommonUtils.getGridBagConstraints(1, 6, 1, 1.6, 1.0,
                DefaultConst.INSETS_RIGHT));

        centerPanelScrollPane = new JScrollPane(centerPanel);
        add(centerPanelScrollPane, BorderLayout.CENTER);
    }

    private void initBottomPanel() {
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Initialize buttons
//        confirmButton = new JButton(LangManger.get("confirm"));
        applyButton = new JButton(LangManger.get("apply"));
        previewButton = new JButton(LangManger.get("preview"));
//        cancelButton = new JButton(LangManger.get("cancel"));

        // Add buttons to the panel
//        bottomPanel.add(cancelButton);
        bottomPanel.add(previewButton);
        bottomPanel.add(applyButton);
//        bottomPanel.add(confirmButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initFontComboBox() {
        this.fontComboBox = new JComboBox<>();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();
        for (Font font : allFonts) {
            fontComboBox.addItem(font.getFontName());
        }
        this.fontSizeComboBox = new JComboBox<>();
        fontSizeComboBox.setEditable(true);

        // Typographic Scale
        // Equation: a1 = round(a*r)
        int a = 12;
        double r = Math.pow(2, 0.2);
        while (a <= 1032) {
            fontSizeComboBox.addItem(a);
            a = (int) Math.round(a * r);
        }
        this.fontStyleComboBox = new JComboBox<>();
        // Line up with the static constants in the class Font
        fontStyleComboBox.addItem(LangManger.get("plain"));  // Font.PLAIN
        fontStyleComboBox.addItem(LangManger.get("bold"));  // Font.BOLD
        fontStyleComboBox.addItem(LangManger.get("italic"));  // Font.ITALIC
    }

    private void initTextDirectionComboBox() {
        this.textDirectionComboBox = new JComboBox<>();
        textDirectionComboBox.addItem(LangManger.get("horizontal"));
        textDirectionComboBox.addItem(LangManger.get("vertical"));
        textDirectionComboBox.addItem(LangManger.get("vertical_inverted"));
    }

    private void initImageFitStyleComboBox() {
        this.imageFitStyleComboBox = new JComboBox<>();
        imageFitStyleComboBox.addItem(LangManger.get("fit"));
        imageFitStyleComboBox.addItem(LangManger.get("stretch"));
        imageFitStyleComboBox.addItem(LangManger.get("raw"));
    }

    private void initColorComboBox() {
        this.colorComboBox = new JComboBox<>();
        colorHashtable = new Hashtable<>();
        colorHashtable.put(0, Color.BLACK);
        colorHashtable.put(1, Color.RED);
        colorHashtable.put(2, Color.ORANGE);
        colorHashtable.put(3, Color.YELLOW);
        colorHashtable.put(4, Color.GREEN);
        colorHashtable.put(5, Color.BLUE);
        colorHashtable.put(6, Color.WHITE);
        colorComboBox.addItem(LangManger.get("black"));
        colorComboBox.addItem(LangManger.get("red"));
        colorComboBox.addItem(LangManger.get("orange"));
        colorComboBox.addItem(LangManger.get("yellow"));
        colorComboBox.addItem(LangManger.get("green"));
        colorComboBox.addItem(LangManger.get("blue"));
        colorComboBox.addItem(LangManger.get("white"));
        colorComboBox.addItem(LangManger.get("others"));
    }

    public void setPanelEnabled(boolean enabled) {
        Component[] centerComponents = centerPanel.getComponents();
        for (Component component : centerComponents) {
            component.setEnabled(enabled);
        }
        previewButton.setEnabled(enabled);
        applyButton.setEnabled(enabled);
    }

    public JComboBox<String> getFontComboBox() {
        return fontComboBox;
    }

    public JComboBox<Integer> getFontSizeComboBox() {
        return fontSizeComboBox;
    }

    public JComboBox<String> getFontStyleComboBox() {
        return fontStyleComboBox;
    }

    public JComboBox<String> getColorComboBox() {
        return colorComboBox;
    }

    public JComboBox<String> getTextDirectionComboBox() {
        return textDirectionComboBox;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JSlider getLetterSpacingSlider() {
        return letterSpacingSlider;
    }

    public JSlider getMarginSlider() {
        return marginSlider;
    }

    public JSlider getvOffsetSlider() {
        return vOffsetSlider;
    }

    public JSlider gethOffsetSlider() {
        return hOffsetSlider;
    }

    public JTextField getImageDirectoryTextField() {
        return imageDirectoryTextField;
    }

    public JComboBox<String> getImageFitStyleComboBox() {
        return imageFitStyleComboBox;
    }

    public Hashtable<Integer, Color> getColorHashtable() {
        return colorHashtable;
    }

    public boolean isInitializing() {
        return initializing;
    }

//    public JButton getConfirmButton() {
//        return confirmButton;
//    }

    public JButton getApplyButton() {
        return applyButton;
    }

    public JButton getPreviewButton() {
        return previewButton;
    }

//    public JButton getCancelButton() {
//        return cancelButton;
//    }

    public void markInitialized() {
        initializing = false;
    }

    public void setInitializing(boolean initializing) {
        this.initializing = initializing;
    }
}


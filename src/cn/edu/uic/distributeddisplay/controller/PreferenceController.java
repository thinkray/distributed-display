/**
 * The controller for the frame Preference
 *
 * @author Bohui WU
 * @since 12/20/2019
 */
package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.Profile;
import cn.edu.uic.distributeddisplay.util.*;
import cn.edu.uic.distributeddisplay.model.PreferenceModel;
import cn.edu.uic.distributeddisplay.util.*;
import cn.edu.uic.distributeddisplay.view.PreferenceView;
import cn.edu.uic.distributeddisplay.view.panel.PreferenceInfoPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PreferenceController {

    private PreferenceModel m;
    private PreferenceView v;

    public PreferenceController() {
        m = new PreferenceModel();
        v = new PreferenceView();
        initController();
    }

    private void initController() {
        initPreferenceView();
        updateFields();
        initPreferenceInfoPanel();
        v.setVisible(true);
    }

    private void initPreferenceView() {
        // PreferenceView
        v.getSaveItem().addActionListener(e -> applyBtnClicked());
        v.getExitItem().addActionListener(e -> System.exit(0));
        v.getEnusItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "en-US");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });
        v.getZhhansItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "zh-Hans");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });
        v.getZhhantItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "zh-Hant");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });
        v.getAboutItem().addActionListener(e ->
                JOptionPane.showMessageDialog(null, "Version 1.2\n" +
                                "Copyright \u00A9 2019-2022 Bohui WU (\u0040RapDoodle)",
                        LangManger.get("about"), JOptionPane.INFORMATION_MESSAGE));
        v.getHelpItem().addActionListener(e -> {
            if (!CommonUtils.openLocalFile("./help/README-" + ConfigManager.getConfigEntry("lang") + ".html")) {
                // When the manual is not found
                JOptionPane.showMessageDialog(null, LangManger.get("err_help_not_found"),
                        LangManger.get("message"), JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void initPreferenceInfoPanel() {
        PreferenceInfoPanel p = v.getProfilePanel();

        // Button actions
        p.getConfirmButton().addActionListener(e -> confirmBtnClicked());
        p.getApplyButton().addActionListener(e -> applyBtnClicked());
        p.getPreviewButton().addActionListener(e -> previewBtnClicked());
        p.getCancelButton().addActionListener(e -> cancelBtnClicked());
        p.getSelectImageDirectoryButton().addActionListener(e -> {
            JFileChooser fc = new JFileChooser(m.getProfile().getBackgroundImageDir());
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileNameExtensionFilter("Image(*.jpg, *.jpeg, *.png)",
                    "jpg", "JPG", "jpeg", "JPEG", "png", "PNG"));
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                p.getImageDirectoryTextField().setText(fc.getSelectedFile().getPath());
            }
        });

        // ComboBox actions
        p.getTextDirectionComboBox().addActionListener(e ->
                p.getLetterSpacingSlider().setEnabled(p.getTextDirectionComboBox().getSelectedIndex() != 0));
        JComboBox<String> colorComboBox = p.getColorComboBox();
        colorComboBox.addActionListener(e -> {
            Color localColor = p.getColorHashtable().get(colorComboBox.getSelectedIndex());
            if (localColor != null) {
                m.setColor(localColor);
            }
            if (!p.isInitializing() && colorComboBox.getSelectedIndex() == colorComboBox.getMaximumRowCount() - 1) {
                // When selected the "Others" option
                Color selectedColor = JColorChooser.showDialog(null, "Color Picker", m.getColor());
                if (selectedColor != null) {
                    m.setColor(selectedColor);

                    // Redefine the color for the "others" option
                    p.getColorHashtable().put(7, m.getColor());

                    // Rerender for the combo box
                    colorComboBox.setRenderer(new ColorfulListRenderer(p.getColorHashtable()));
                } else {
                    int colorIdx = CommonUtils.getColorFromIndex(m.getColor());
                    if (colorIdx >= 0) {
                        colorComboBox.setSelectedIndex(colorIdx);
                    }
                    // Do nothing when the color previously selected was not in the color table
                }
            }
        });

        // Render the list of colors
        if (CommonUtils.getColorFromIndex(m.getColor()) == -1) {
            // When the color is not in the hash map (the default set), select "others"
            p.getColorComboBox().setSelectedIndex(p.getColorComboBox().getMaximumRowCount() - 1);
            p.getColorHashtable().put(7, m.getColor());
        }
        p.getColorComboBox().setRenderer(new ColorfulListRenderer(p.getColorHashtable()));

        // Mark the panel initialized
        p.markInitialized();
    }

    private void updateFields() {
        Profile profile = m.getProfile();
        PreferenceInfoPanel p = v.getProfilePanel();
        p.getProfileNameTextField().setText(profile.getName());
        Font font = profile.getFont();
        p.getFontComboBox().setSelectedItem(font.getFontName());
        p.getFontSizeComboBox().setSelectedItem(font.getSize());
        p.getFontStyleComboBox().setSelectedIndex(font.getStyle());
        m.setColor(profile.getColor());
        int colorIdx = CommonUtils.getColorFromIndex(profile.getColor());
        if (colorIdx >= 0) {
            p.getColorComboBox().setSelectedIndex(CommonUtils.getColorFromIndex(profile.getColor()));
        } else {
            p.getColorComboBox().setSelectedIndex(p.getColorComboBox().getMaximumRowCount() - 1);
        }
        p.getLetterSpacingSlider().setValue(profile.getLetterSpacing());
        p.getvOffsetSlider().setValue(profile.getvOffset());
        p.gethOffsetSlider().setValue(profile.gethOffset());
        p.getTextDirectionComboBox().setSelectedIndex(profile.getTextOrientation());
        p.getTextArea().setText(profile.getText());
        p.getMarginSlider().setValue(profile.getMargin());
        try {
            p.getImageDirectoryTextField().setText(profile.getBackgroundImageDir().getPath());
        } catch (NullPointerException e) {
            // Do nothing when the file does not exist
        }
        p.getImageFitStyleComboBox().setSelectedIndex(profile.getImgFitStyle());
    }

    private void trySave(Profile profile) {
        try {
            m.save(profile);
        } catch (IOException ex) {
            Log.logError(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error occurred while saving the profile!");
        }
    }

    public void confirmBtnClicked() {
        Profile profile = wrapProfile();
        trySave(profile);
        SwingUtilities.invokeLater(() -> {
            new DisplayController(profile, false);
        });
        ViewsManager.getPreferenceView().setVisible(false);
    }

    public void previewBtnClicked() {
        Profile profile = wrapProfile();
        SwingUtilities.invokeLater(() -> {
            new DisplayController(profile, true);
        });
        ViewsManager.getPreferenceView().setVisible(false);
    }

    public void applyBtnClicked() {
        Profile profile = wrapProfile();
        trySave(profile);
    }

    public void cancelBtnClicked() {
        if (ViewsManager.getDisplayView() == null) {
            ViewsManager.getPreferenceView().dispose();
        } else {
            ViewsManager.getPreferenceView().setVisible(false);
            ViewsManager.getDisplayView().setVisible(true);
        }
    }

    public Profile wrapProfile() {
        PreferenceInfoPanel p = v.getProfilePanel();
        return new Profile(p.getProfileNameTextField().getText(),
                p.getTextArea().getText(),
                new Font(p.getFontComboBox().getSelectedItem().toString(),
                        p.getFontStyleComboBox().getSelectedIndex(),
                        Integer.parseInt(p.getFontSizeComboBox().getSelectedItem().toString())),
                m.getColor(),
                p.getLetterSpacingSlider().getValue(),
                p.getMarginSlider().getValue(),
                p.getvOffsetSlider().getValue(),
                p.gethOffsetSlider().getValue(),
                new File(p.getImageDirectoryTextField().getText()),
                p.getTextDirectionComboBox().getSelectedIndex(),
                p.getImageFitStyleComboBox().getSelectedIndex(),
                false
                );
    }

}

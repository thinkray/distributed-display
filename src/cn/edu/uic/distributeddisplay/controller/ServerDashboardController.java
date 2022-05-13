/**
 * The controller for the frame MainWindow
 *
 * @author Bohui WU
 * @since 12/20/2019
 */
package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;
import cn.edu.uic.distributeddisplay.util.*;
//import cn.edu.uic.distributeddisplay.model.PreferenceModel;
import cn.edu.uic.distributeddisplay.view.ServerMainWindowView;
import cn.edu.uic.distributeddisplay.view.panel.ConsolePanel;
import cn.edu.uic.distributeddisplay.view.panel.DisplayConfigPanel;
import cn.edu.uic.distributeddisplay.view.panel.NodeListPanel;
import cn.edu.uic.distributeddisplay.view.panel.ServerConfigPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.*;
import java.util.Date;

public class ServerDashboardController {

    private ServerSideProfile tempServerSideProfile;
    private ServerMainWindowView v;
    private RMIServerController rmiServerController;

    public ServerDashboardController() {
        tempServerSideProfile = new ServerSideProfile();
        v = new ServerMainWindowView();
        initController();
    }

    private void initController() {
        initMainWindowView();
//        updateFields();
        initMainWindowInfoPanel();
        rmiServerController = new RMIServerController(this);
        v.setVisible(true);
    }

    private void initMainWindowView() {
        // MainWindowView
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
        v.getAboutItem().addActionListener(e -> JOptionPane.showMessageDialog(null, "Version 1.2\n" + "Copyright " +
                        "\u00A9 2019-2022 Bohui WU (\u0040RapDoodle)", LangManger.get("about"),
                JOptionPane.INFORMATION_MESSAGE));
        v.getHelpItem().addActionListener(e -> {
            if (!CommonUtils.openLocalFile("./help/README-" + ConfigManager.getConfigEntry("lang") + ".html")) {
                // When the manual is not found
                JOptionPane.showMessageDialog(null, LangManger.get("err_help_not_found"), LangManger.get("message"),
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void initMainWindowInfoPanel() {
        ServerConfigPanel serverConfigPanel = v.getServerConfigPanel();
        NodeListPanel nodeListPanel = v.getNodeListPanel();
        DisplayConfigPanel displayConfigPanel = v.getDisplayConfigPanel();
        ConsolePanel consolePanel = v.getConsolePanel();

        JTable nodeListTable = nodeListPanel.getNodeListTable();

        nodeListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                int[] selectedRows = nodeListTable.getSelectedRows();
                if (selectedRows.length == 1) {
                    ProfileRow profileRow =
                            ProfileManager.getProfileRow((String) nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 0));
                    tempServerSideProfile = new ServerSideProfile(profileRow.serverSideProfile);
                }
                updateFields(tempServerSideProfile);
            }
        });

        serverConfigPanel.getListenButton().addActionListener(e -> {
            serverConfigPanel.getListenButton().setEnabled(false);
            if (rmiServerController.isRunning()) {
                if (rmiServerController.stopServer()) {
                    serverConfigPanel.getListenButton().setText("Start");
                }
            } else {
                if (rmiServerController.startServer(serverConfigPanel.getListenAddressTextField().getText(),
                        (Integer) serverConfigPanel.getListenPortJSpinner().getValue())) {
                    serverConfigPanel.getListenButton().setText("Stop");
                }
            }
            serverConfigPanel.getListenButton().setEnabled(true);
        });

        // Button actions
//        displayConfigPanel.getConfirmButton().addActionListener(e -> confirmBtnClicked());
        displayConfigPanel.getApplyButton().addActionListener(e -> applyBtnClicked());
        displayConfigPanel.getPreviewButton().addActionListener(e -> previewBtnClicked());
//        displayConfigPanel.getCancelButton().addActionListener(e -> cancelBtnClicked());
        displayConfigPanel.getSelectImageDirectoryButton().addActionListener(e -> {
            JFileChooser fc = new JFileChooser(v.getDisplayConfigPanel().getImageDirectoryTextField().getText());
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileNameExtensionFilter("Image(*.jpg, *.jpeg, *.png)", "jpg", "JPG", "jpeg"
                    , "JPEG", "png", "PNG"));
            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                displayConfigPanel.getImageDirectoryTextField().setText(fc.getSelectedFile().getPath());
            }
        });

        // ComboBox actions
        displayConfigPanel.getTextDirectionComboBox().addActionListener(e -> displayConfigPanel.getLetterSpacingSlider().setEnabled(displayConfigPanel.getTextDirectionComboBox().getSelectedIndex() != 0));
        JComboBox<String> colorComboBox = displayConfigPanel.getColorComboBox();
        colorComboBox.addActionListener(e -> {
            Color localColor = displayConfigPanel.getColorHashtable().get(colorComboBox.getSelectedIndex());
            if (localColor != null) {
                tempServerSideProfile.setColor(localColor);
            }
            if (!displayConfigPanel.isInitializing() && colorComboBox.getSelectedIndex() == colorComboBox.getMaximumRowCount() - 1) {
                // When selected the "Others" option
                Color selectedColor = JColorChooser.showDialog(null, "Color Picker", tempServerSideProfile.getColor());
                if (selectedColor != null) {
                    tempServerSideProfile.setColor(selectedColor);

                    // Redefine the color for the "others" option
                    displayConfigPanel.getColorHashtable().put(7, tempServerSideProfile.getColor());

                    // Rerender for the combo box
                    colorComboBox.setRenderer(new ColorfulListRenderer(displayConfigPanel.getColorHashtable()));
                } else {
                    int colorIdx = CommonUtils.getColorFromIndex(tempServerSideProfile.getColor());
                    if (colorIdx >= 0) {
                        colorComboBox.setSelectedIndex(colorIdx);
                    }
                    // Do nothing when the color previously selected was not in the color table
                }
            }
        });

        // Render the list of colors
        if (CommonUtils.getColorFromIndex(tempServerSideProfile.getColor()) == -1) {
            // When the color is not in the hash map (the default set), select "others"
            displayConfigPanel.getColorComboBox().setSelectedIndex(displayConfigPanel.getColorComboBox().getMaximumRowCount() - 1);
            displayConfigPanel.getColorHashtable().put(7, tempServerSideProfile.getColor());
        }
        displayConfigPanel.getColorComboBox().setRenderer(new ColorfulListRenderer(displayConfigPanel.getColorHashtable()));

        // Mark the panel initialized
        displayConfigPanel.markInitialized();
    }

    private void updateFields(ServerSideProfile serverSideProfile) {
        DisplayConfigPanel p = v.getDisplayConfigPanel();
        Font font = serverSideProfile.getFont();
        p.getFontComboBox().setSelectedItem(font.getFontName());
        p.getFontSizeComboBox().setSelectedItem(font.getSize());
        p.getFontStyleComboBox().setSelectedIndex(font.getStyle());
        tempServerSideProfile.setColor(serverSideProfile.getColor());
        int colorIdx = CommonUtils.getColorFromIndex(serverSideProfile.getColor());
        if (colorIdx >= 0) {
            p.getColorComboBox().setSelectedIndex(CommonUtils.getColorFromIndex(serverSideProfile.getColor()));
        } else {
            p.getColorComboBox().setSelectedIndex(p.getColorComboBox().getMaximumRowCount() - 1);
        }
        p.getLetterSpacingSlider().setValue(serverSideProfile.getLetterSpacing());
        p.getvOffsetSlider().setValue(serverSideProfile.getvOffset());
        p.gethOffsetSlider().setValue(serverSideProfile.gethOffset());
        p.getTextDirectionComboBox().setSelectedIndex(serverSideProfile.getTextOrientation());
        p.getTextArea().setText(serverSideProfile.getText());
        p.getMarginSlider().setValue(serverSideProfile.getMargin());
        try {
            p.getImageDirectoryTextField().setText(serverSideProfile.getBackgroundImageDir().getPath());
        } catch (NullPointerException e) {
            // Do nothing when the file does not exist
        }
        p.getImageFitStyleComboBox().setSelectedIndex(serverSideProfile.getImgFitStyle());
    }

//    private void trySave(ServerSideProfile serverSideProfile) {
//        // TODO: Get selection list
////        try {
////            tempServerSideProfile.save(serverSideProfile);
////        } catch (IOException ex) {
////            Log.logError(ex.getMessage());
////            JOptionPane.showMessageDialog(null, "Error occurred while saving the profile!");
////        }
//    }

//    public void confirmBtnClicked() {
//        ServerSideProfile serverSideProfile = wrapProfile();
//        trySave(serverSideProfile);
//        SwingUtilities.invokeLater(() -> {
//            new DisplayController(serverSideProfile, false);
//        });
//        ViewsManager.getMainWindowView().setVisible(false);
//    }

    public void previewBtnClicked() {
        ServerSideProfile serverSideProfile = wrapProfile();
        SwingUtilities.invokeLater(() -> {
            new DisplayController(serverSideProfile, true);
        });
        ViewsManager.getMainWindowView().setVisible(false);
    }

    public void applyBtnClicked() {
        NodeListPanel nodeListPanel = v.getNodeListPanel();
        JTable nodeListTable = nodeListPanel.getNodeListTable();
        int[] selectedRows = nodeListTable.getSelectedRows();
        ServerSideProfile serverSideProfileTemplate = wrapProfile();

        for (int i = 0; i < selectedRows.length; i++) {
            String currentNodeName = nodeListTable.getValueAt(selectedRows[i], 0).toString();
            ProfileRow currentProfileRow = ProfileManager.getProfileRow(currentNodeName);
            if (currentProfileRow == null) {
                currentProfileRow = new ProfileRow(new ServerSideProfile(serverSideProfileTemplate), false,
                        new Date(0L), "");
                ProfileManager.putProfileRow(currentNodeName, currentProfileRow);
            } else {
                currentProfileRow.serverSideProfile = new ServerSideProfile(serverSideProfileTemplate);
                currentProfileRow.newConfigAvailable = true;
            }
        }

//        trySave(serverSideProfile);
    }

//    public void cancelBtnClicked() {
//        if (ViewsManager.getDisplayView() == null) {
//            ViewsManager.getMainWindowView().dispose();
//        } else {
//            ViewsManager.getMainWindowView().setVisible(false);
//            ViewsManager.getDisplayView().setVisible(true);
//        }
//    }

    public ServerSideProfile wrapProfile() {
        DisplayConfigPanel p = v.getDisplayConfigPanel();
        return new ServerSideProfile(p.getTextArea().getText(),
                new Font(p.getFontComboBox().getSelectedItem().toString(),
                        p.getFontStyleComboBox().getSelectedIndex(),
                        Integer.parseInt(p.getFontSizeComboBox().getSelectedItem().toString())),
                tempServerSideProfile.getColor(), p.getLetterSpacingSlider().getValue(),
                p.getMarginSlider().getValue(), p.getvOffsetSlider().getValue(), p.gethOffsetSlider().getValue(),
                new File(p.getImageDirectoryTextField().getText()), p.getTextDirectionComboBox().getSelectedIndex(),
                p.getImageFitStyleComboBox().getSelectedIndex());
    }

    public void updateConsole(String message) {
        JEditorPane console = v.getConsolePanel().getConsoleEditorPane();

        try {
            HTMLDocument doc = (HTMLDocument) console.getDocument();
            doc.insertBeforeEnd((Element) doc.getElement("container"), message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

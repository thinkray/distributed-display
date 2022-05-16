/**
 * The controller for the frame MainWindow
 *
 * @author Team 3
 */
package cn.edu.uic.distributeddisplay.controller;

import cn.edu.uic.distributeddisplay.profile.ServerSideProfile;
import cn.edu.uic.distributeddisplay.util.*;
import cn.edu.uic.distributeddisplay.view.ServerMainWindowView;
import cn.edu.uic.distributeddisplay.view.panel.ConsolePanel;
import cn.edu.uic.distributeddisplay.view.panel.DisplayConfigPanel;
import cn.edu.uic.distributeddisplay.view.panel.NodeListPanel;
import cn.edu.uic.distributeddisplay.view.panel.ServerConfigPanel;
import org.apache.commons.text.StringEscapeUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

public class ServerDashboardController {

    private ServerSideProfile tempServerSideProfile;
    private final ServerMainWindowView serverMainWindowView;
    private RMIServerController rmiServerController;

    public ServerDashboardController() {
        tempServerSideProfile = new ServerSideProfile();
        serverMainWindowView = new ServerMainWindowView();
        initController();
    }

    private void initController() {
        ProfileManager.loadProfileListFromFile();
        initMainWindowView();
//        updateFields();
        initMainWindowInfoPanel();
        rmiServerController = new RMIServerController(this);
        serverMainWindowView.setVisible(true);
    }

    private void initMainWindowView() {
        // MainWindowView
        serverMainWindowView.getSaveItem().addActionListener(e -> {
            if (ProfileManager.saveProfileListToFile()) {
                updateConsole(String.format("<div>%s</div>", LangManger.get("profile_list_saved")));
            } else {
                updateConsole(String.format("<div style=\"background-color: red; color: white;\">%s</div>", LangManger.get("cannot_save_profile_list")));
            }
        });
        serverMainWindowView.getExitItem().addActionListener(e -> System.exit(0));
        serverMainWindowView.getEnusItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "en-US");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });
        serverMainWindowView.getZhhansItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "zh-Hans");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });
        serverMainWindowView.getZhhantItem().addActionListener(e -> {
            ConfigManager.setConfigEntry("lang", "zh-Hant");
            JOptionPane.showMessageDialog(null, LangManger.get("lang_update_msg"));
        });
        serverMainWindowView.getAboutItem().addActionListener(e -> JOptionPane.showMessageDialog(null,
                "Version 1.2" + "\n" + "Copyright " + "\u00A9 2022 Team 3", LangManger.get("about"),
                JOptionPane.INFORMATION_MESSAGE));
        serverMainWindowView.getHelpItem().addActionListener(e -> {
            if (!CommonUtils.openLocalFile("./help/README-" + ConfigManager.getConfigEntry("lang") + ".html")) {
                // When the manual is not found
                JOptionPane.showMessageDialog(null, LangManger.get("err_help_not_found"), LangManger.get("message"),
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void initMainWindowInfoPanel() {
        ServerConfigPanel serverConfigPanel = serverMainWindowView.getServerConfigPanel();
        NodeListPanel nodeListPanel = serverMainWindowView.getNodeListPanel();
        DisplayConfigPanel displayConfigPanel = serverMainWindowView.getDisplayConfigPanel();
        ConsolePanel consolePanel = serverMainWindowView.getConsolePanel();

        JTable nodeListTable = nodeListPanel.getNodeListTable();

        nodeListTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                int[] selectedRows = nodeListTable.getSelectedRows();
                if (selectedRows.length == 0) {
                    tempServerSideProfile = new ServerSideProfile();
                    serverMainWindowView.getDisplayConfigPanel().setPanelEnabled(false);
                } else if (selectedRows.length == 1) {
                    ProfileRow profileRow =
                            ProfileManager.getProfileRow((String) nodeListTable.getValueAt(nodeListTable.getSelectedRow(), 0));
                    tempServerSideProfile = new ServerSideProfile(profileRow.serverSideProfile);
                    serverMainWindowView.getDisplayConfigPanel().setPanelEnabled(true);
                }

                updateFields(tempServerSideProfile);
            }
        });

        nodeListPanel.getAddNodeButton().addActionListener(e -> {
            nodeListPanel.getAddNodeButton().setEnabled(false);
            nodeListPanel.getNewNodeNameTextField().setEnabled(false);

            String newNodeName = nodeListPanel.getNewNodeNameTextField().getText();
            if (Objects.equals(newNodeName, "")) {
                JOptionPane.showMessageDialog(null, LangManger.get("error_empty_node_name"), "Error", JOptionPane.ERROR_MESSAGE);
            } else if (ProfileManager.getProfileRow(newNodeName) == null) {
                ProfileRow newProfileRow = new ProfileRow(new ServerSideProfile(), false, new Date(0), "");
                ProfileManager.putProfileRow(newNodeName, newProfileRow);
                nodeListPanel.getNewNodeNameTextField().setText("");
                updateConsole(String.format("<div>%s [%s] %s</div>", LangManger.get("node"),
                        StringEscapeUtils.escapeHtml3(newNodeName), LangManger.get("added_msg")));
            } else {
                JOptionPane.showMessageDialog(null, LangManger.get("error_duplicate_node_name"), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            nodeListPanel.getNewNodeNameTextField().setEnabled(true);
            nodeListPanel.getAddNodeButton().setEnabled(true);
        });

        nodeListPanel.getDeleteNodeButton().addActionListener(e -> {
            nodeListPanel.getDeleteNodeButton().setEnabled(false);
            nodeListTable.setAutoCreateRowSorter(false);

            int[] selectedRows = nodeListTable.getSelectedRows();
            for (int i = 0; i < selectedRows.length; i++) {
                String currentNodeName = (String) nodeListTable.getValueAt(selectedRows[i], 0);
                ProfileManager.removeProfileRow(currentNodeName);
                updateConsole(String.format("<div>%s[%s] %s</div>", LangManger.get("node"),
                        StringEscapeUtils.escapeHtml3(currentNodeName), LangManger.get("deleted_msg")));
            }

            nodeListTable.setAutoCreateRowSorter(true);
            nodeListPanel.getDeleteNodeButton().setEnabled(true);
        });

        serverConfigPanel.getListenButton().addActionListener(e -> {
            serverConfigPanel.getListenButton().setEnabled(false);
            if (rmiServerController.isRunning()) {
                if (rmiServerController.stopServer()) {
                    serverConfigPanel.getListenButton().setText(LangManger.get("start"));
                    serverConfigPanel.getListenAddressTextField().setEnabled(true);
                    serverConfigPanel.getListenPortJSpinner().setEnabled(true);
                }
            } else {
                if (rmiServerController.startServer(serverConfigPanel.getListenAddressTextField().getText(),
                        (Integer) serverConfigPanel.getListenPortJSpinner().getValue())) {
                    serverConfigPanel.getListenAddressTextField().setEnabled(false);
                    serverConfigPanel.getListenPortJSpinner().setEnabled(false);
                    serverConfigPanel.getListenButton().setText(LangManger.get("stop"));
                }
            }
            serverConfigPanel.getListenButton().setEnabled(true);
        });

        // Button actions
        displayConfigPanel.getApplyButton().addActionListener(e -> {
            applyBtnClicked();
        });
        displayConfigPanel.getPreviewButton().addActionListener(e -> previewBtnClicked());
        displayConfigPanel.getSelectImageDirectoryButton().addActionListener(e -> {
            JFileChooser fc =
                    new JFileChooser(serverMainWindowView.getDisplayConfigPanel().getImageDirectoryTextField().getText());
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileNameExtensionFilter(LangManger.get("image") + "(*.jpg, *.jpeg, *.png)", "jpg", "JPG", "jpeg"
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
        DisplayConfigPanel p = serverMainWindowView.getDisplayConfigPanel();

        // Fix: When the color is selected to "others", the menu may pop up when profile changes
        p.setInitializing(true);

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
        p.setInitializing(false);
    }

    public void previewBtnClicked() {
        ServerSideProfile serverSideProfile = wrapProfile();
        SwingUtilities.invokeLater(() -> {
            new DisplayController(serverSideProfile, DefaultConst.PREVIEW_MODE);
        });
        ViewsManager.getMainWindowView().setVisible(false);
    }

    public void applyBtnClicked() {
        NodeListPanel nodeListPanel = serverMainWindowView.getNodeListPanel();
        JTable nodeListTable = nodeListPanel.getNodeListTable();
        int[] selectedRows = nodeListTable.getSelectedRows();
        ServerSideProfile serverSideProfileTemplate = wrapProfile();

        for (int i = 0; i < selectedRows.length; i++) {
            String currentNodeName = nodeListTable.getValueAt(selectedRows[i], 0).toString();
            updateConsole(String.format("<div>%s [%s].</div>", LangManger.get("applied_to_node"), StringEscapeUtils.escapeHtml3(currentNodeName)));
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
    }

    public ServerSideProfile wrapProfile() {
        DisplayConfigPanel p = serverMainWindowView.getDisplayConfigPanel();
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
        JEditorPane console = serverMainWindowView.getConsolePanel().getConsoleEditorPane();

        try {
            HTMLDocument doc = (HTMLDocument) console.getDocument();
            doc.insertBeforeEnd(doc.getElement("container"), message);
        } catch (BadLocationException | IOException e) {
            System.out.println(e);
        }
    }
}

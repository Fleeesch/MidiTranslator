package com.fleeesch.miditranslator.gui;

import com.fleeesch.miditranslator.Main;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.gui.button.ButtonSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Gui {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Generated

    public static File rootPath;

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Colors

    // window default
    public static final Color colorWindowBackground = Color.decode("0x303030");
    public static final Color colorWindowText = Color.decode("0xA0A0A0");
    public static final Color colorWindowTextDim = Color.decode("0x808080");
    // button default
    public static final Color colorButtonBackground = Color.decode("0xA0A0A0");
    public static final Color colorButtonText = Color.decode("0x0E0E0E");
    // active
    public static final Color colorButtonBackgroundActive = Color.decode("0x70C070");
    public static final Color colorButtonTextActive = Color.decode("0x103010");
    // missing
    public static final Color colorButtonBackgroundMissing = Color.decode("0xC07070");
    public static final Color colorButtonTextMissing = Color.decode("0x301010");
    // not loaded
    public static final Color colorButtonBackgroundNotLoaded = Color.decode("0x606060");
    public static final Color colorButtonTextNotLoaded = Color.decode("0x202020");
    // locked
    public static final Color colorButtonBackgroundLocked = Color.decode("0xA0A0A0");
    public static final Color colorButtonTextLocked = Color.decode("0x202020");
    // disabled
    public static final Color colorButtonBackgroundDisabled = Color.decode("0xC0A030");
    public static final Color colorButtonTextDisabled = Color.decode("0x402000");

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Instance

    // instance of gui
    private static Gui guiInstance;

    // current row
    private static int row = 0;

    // image of tray icon
    private static Image trayImage;

    // frame + panel
    private JFrame frame;
    private JPanel panel;

    // last created elements
    public JButton lastButton;
    public JLabel lastLabel;

    // button set (button + label)
    final List<ButtonSet> sets = new ArrayList<>();


    //************************************************************
    //      Constructor
    //************************************************************

    public Gui() {

    }

    //************************************************************
    //      Method : Setup
    //************************************************************

    public static void setup() {

        guiInstance = new Gui(); // just create the instance, layouting happens in constructor

        loadTrayIcon("/img/icon.png"); // load icon

        // initialize panel and frame
        guiInstance.frame = new JFrame();
        guiInstance.panel = new JPanel();

        // title
        guiInstance.frame.setTitle("Midi Translator");

        // panel / frame fusion
        guiInstance.panel.setLayout(new GridBagLayout()); // grid layout
        guiInstance.frame.add(guiInstance.panel, BorderLayout.CENTER); // add panel

        // panel format
        guiInstance.panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        guiInstance.panel.setBackground(colorWindowBackground);

        // buttons
        guiInstance.addReconnectButton(); // add reconnect-all-devices button
        guiInstance.addDeviceButton(Device.list); // add device buttons from device list

        // osc information
        guiInstance.printInfoLine("Sending OSC on Ports...", "In / Out", colorWindowTextDim, colorWindowTextDim, 10, 0);
        guiInstance.printInfoLine("Arrange", Main.deviceDaw.oscPortArrangeIn + " / " + Main.deviceDaw.oscPortArrangeOut, colorWindowTextDim, colorWindowText, 0, 0);
        guiInstance.printInfoLine("Control", Main.deviceDaw.oscPortControlIn + " / " + Main.deviceDaw.oscPortControlOut, colorWindowTextDim, colorWindowText, 0, 0);

        // frame formatting
        guiInstance.frame.pack(); // no idea what this one does
        guiInstance.frame.setLocationRelativeTo(null); // center frame
        guiInstance.frame.setMinimumSize(new Dimension(300, 100 + Device.list.size() * 18)); // minimum size to allow window moving
        guiInstance.frame.setIconImage(trayImage); // icon

        // add system tray icon / menu
        addSystemTray();

    }

    //************************************************************
    //      Method : Add System Tray
    //************************************************************

    public static void addSystemTray() {

        if (!SystemTray.isSupported()) return; // only if supported

        // tray icon double click action
        ActionListener doubleClickListener = e -> showGui();

        // Reload All Action
        ActionListener reloadAllListener = e -> reconnectAllDevices();

        // Open Folder Action
        ActionListener folderListener = e -> openRootPath();

        // Exit Action
        ActionListener exitListener = e -> System.exit(0);

        // get the SystemTray instance
        SystemTray tray = SystemTray.getSystemTray();

        // create a popup menu for tray icon
        PopupMenu popup = new PopupMenu();

        // Menu -> Reload All
        MenuItem miReloadAll = new MenuItem("Reload All");
        miReloadAll.addActionListener(reloadAllListener);
        popup.add(miReloadAll);

        // Menu -> Open Folder
        MenuItem miFolder = new MenuItem("Open Folder");
        miFolder.addActionListener(folderListener);
        popup.add(miFolder);

        // Menu -> Exit
        MenuItem miExit = new MenuItem("Exit");
        miExit.addActionListener(exitListener);
        popup.add(miExit);

        // create tray icon, add action listener
        TrayIcon trayIcon = new TrayIcon(trayImage, "Tray Demo", popup);
        trayIcon.addActionListener(doubleClickListener);

        // add tray icon
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println(e.getMessage());
        }

    }

    //************************************************************
    //      Method : Open Root Path
    //************************************************************

    static void openRootPath() {

        // try to open the path
        try {
            Desktop.getDesktop().open(getRootPath());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    //************************************************************
    //      Method : Get Root Path
    //************************************************************

    static File getRootPath() {

        //!! currently using a dictated path, not flexible
        // try to get path
        try {
            return new File("c:\\root\\int\\development\\java\\MidiTranslator\\out\\artifacts\\Main_jar");
        } catch (Exception e) {
            return null;
        }
    }

    //************************************************************
    //      Method : Get Icon
    //************************************************************

    public static void loadTrayIcon(String pUrl) {

        // store image from url
        trayImage = Toolkit.getDefaultToolkit().getImage(Main.class.getResource(pUrl));

    }


    //************************************************************
    //      Method : Show Gui
    //************************************************************

    public static void showGui() {
        guiInstance.frame.setVisible(true);
    }

    //************************************************************
    //      Method : Hide Gui
    //************************************************************

    public static void hideGui() {
        guiInstance.frame.setVisible(false);
    }

    //************************************************************
    //     Method : Print Line
    //************************************************************

    public void printInfoLine(String pText1, String pText2, Color pColor1, Color pColor2, int pOffsetYTop, int pOffsetYBot) {

        JTextField label1 = new JTextField("");
        JTextField label2 = new JTextField("");

        // initiate gridbag
        GridBagConstraints c = new GridBagConstraints();

        // some formatting
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2 + pOffsetYTop, 4, 2 + pOffsetYBot, 4);
        c.gridy = row++;

        c.gridx = 0;
        // add labels
        panel.add(label1, c);
        c.gridx = 1;
        panel.add(label2, c);

        // format labels
        label1.setForeground(pColor1);
        label1.setOpaque(false);
        label1.setEditable(false);
        label1.setBorder(null);
        label1.setText(pText1);
        label1.setFont(new Font(label1.getFont().getFontName(), 0, 12));

        label2.setForeground(pColor2);
        label2.setOpaque(false);
        label2.setEditable(false);
        label2.setBorder(null);
        label2.setText(pText2);
        label2.setFont(new Font(label2.getFont().getFontName(), 0, 12));


    }

    //************************************************************
    //      Method : Add Device Button (Multiple at once)
    //************************************************************

    public void addDeviceButton(List<Device> dList) {

        for (Device d : dList) addDeviceButton(d); // add button for device

    }

    //************************************************************
    //     Method : Add Button Label Set
    //************************************************************

    public void addButtonLabelSet(int pOffetY) {

        // create button and label
        JButton button = new JButton("");
        JLabel label = new JLabel("");

        // initiate gridbag
        GridBagConstraints c = new GridBagConstraints();

        // some formatting
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 4, 2 + pOffetY, 4);
        c.gridy = row++;
        c.gridx = 0;

        // button and label
        panel.add(button, c);
        c.gridx = 1;
        panel.add(label, c);

        // format button
        button.setBackground(colorButtonBackground);
        button.setForeground(colorButtonText);
        button.setRolloverEnabled(false);
        button.setFocusPainted(false);

        // set padded border
        button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(colorButtonText, 1), BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        // format label
        label.setForeground(colorWindowText);
        label.setVerticalAlignment(1);

        // store created button and label
        guiInstance.lastButton = button;
        guiInstance.lastLabel = label;

    }

    //************************************************************
    //     Method : Reconnect All Devices
    //************************************************************

    static void reconnectAllDevices() {
        for (ButtonSet s : guiInstance.sets) s.doAction(true); // use 'force enable' action
    }

    //************************************************************
    //     Method : Add Reconnect Button
    //************************************************************

    public void addReconnectButton() {
        {

            addButtonLabelSet(10); // add button set with a little y-offset

            lastButton.setText("Reconnect All"); // change label of button

            // create button click task
            lastButton.addActionListener(e -> reconnectAllDevices());

        }

    }

    //************************************************************
    //     Method : Add Device Button
    //************************************************************

    public void addDeviceButton(Device d) {
        {

            if (!d.showInGui) return; // don't add to gui if not allowed

            addButtonLabelSet(0); // create set, use default padding

            sets.add(new ButtonSet(lastButton, lastLabel, d)); // add set to list

        }

    }

}

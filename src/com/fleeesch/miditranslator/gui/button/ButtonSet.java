package com.fleeesch.miditranslator.gui.button;

import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.gui.Gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ButtonSet {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public final JButton button;
    public final JLabel label;

    public final Device device;

    public boolean userAccess = true;

    final ActionListener buttonAction;


    //************************************************************
    //      Constructor
    //************************************************************

    public ButtonSet(JButton pButton, JLabel pLabel, Device pDevice) {

        button = pButton;

        label = pLabel;

        device = pDevice;

        buttonAction = new ButtonAction(this);

        button.setText(device.name);

        button.addActionListener(buttonAction);

        label.setText("Placeholder Text");

        updateStatus();

    }

    //************************************************************
    //      Method : Set User Access
    //************************************************************

    public void setUserAccess(boolean pAccess) {

        userAccess = pAccess;
        updateStatus();

        if (!userAccess) {
            button.removeActionListener(buttonAction);
        }

    }

    //************************************************************
    //      Method : Do Action
    //************************************************************

    public void doAction(boolean forcePositive) {

        ((ButtonAction) buttonAction).action(forcePositive);
        updateStatus();

    }

    //************************************************************
    //      Method : Update Status
    //************************************************************

    public void updateStatus() {

        // device is enabled?
        if (!device.enabledState) {
            label.setText("Disabled");
            button.setForeground(Gui.colorButtonTextDisabled);
            button.setBackground(Gui.colorButtonBackgroundDisabled);
            return;
        }

        // device is setup but not available?
        if (device.isSetup && !device.available) {
            label.setText("Missing");
            button.setForeground(Gui.colorButtonTextMissing);
            button.setBackground(Gui.colorButtonBackgroundMissing);

        }

        // device is not available?
        if (!device.available) {

            label.setText("Not found");
            button.setForeground(Gui.colorButtonTextNotLoaded);
            button.setBackground(Gui.colorButtonBackgroundNotLoaded);
            return;
        }

        // device is locked?
        if (!userAccess) {
            button.setForeground(Gui.colorButtonTextLocked);
            button.setBackground(Gui.colorButtonBackgroundLocked);
            return;
        }


        // device available and midi is working?
        if (device.midi.portsOpen) {
            label.setText("On");
            button.setForeground(Gui.colorButtonTextActive);
            button.setBackground(Gui.colorButtonBackgroundActive);
        }


    }

}

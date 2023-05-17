package com.fleeesch.miditranslator.gui.button;

import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.device.Device;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonAction implements ActionListener {

    //************************************************************
    //      Variables
    //************************************************************

    public final Device device; // device that the button targets

    public final ButtonSet set; // set of button


    //************************************************************
    //      Constructor
    //************************************************************

    public ButtonAction(ButtonSet bt) {

        set = bt; // store set

        device = set.device; // get device from set

    }

    //************************************************************
    //      Method : Button Action
    //************************************************************

    public void action(boolean ignoreDeviceSettings) {

        // ::: Initial Setup :::

        // device is not setup?
        if (!device.isSetup) {

            device.setEnabledState(true); // enable device

            device.ignoreEnableSettings = true; // ignore stored settings

            device.setupDevice(); // try to setup device

            device.ignoreEnableSettings = false; // follow stored settings again

            set.updateStatus(); // update button set status

            Osc.sendInitMessages();

            return; // thats it, nothing more
        }

        // ::: Reconnect / Toggle Device Availability :::

        boolean available = device.available; // store current device availability

        // rebuild the midi ports
        if(!device.midi.rebuild()){
            device.available = false;
            set.updateStatus();
            return;
        }

        // device is available, has been available and settings are to be followed?
        if (!ignoreDeviceSettings && available && device.available)
            device.toggleEnabledState(); // toggle enable state of device


        // ::: Reconnect :::

        if (ignoreDeviceSettings)
            device.setEnabledState(true); // ignore device settings? just set device state to enabled

        Osc.sendInitMessages();

        set.updateStatus(); // update gui


    }

    //************************************************************
    //      Method : Action Performed
    //************************************************************

    @Override
    public void actionPerformed(ActionEvent e) {

        action(false); // trigger action, follow settings

    }

}

package com.fleeesch.miditranslator;

import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.device.hardware.*;
import com.fleeesch.miditranslator.device.software.DAW;
import com.fleeesch.miditranslator.file.config.Configuration;
import com.fleeesch.miditranslator.gui.Gui;

public class Main {

    //************************************************************
    //      Variables
    //************************************************************

    public static DAW deviceDaw;

    //************************************************************
    //      Main Routine
    //************************************************************

    public static void main(String[] args) {

        // - Add DAW -

        deviceDaw = (DAW) Device.add(new DAW());

        // - add MIDI Devices -

        Device.add(new FaderPort8());
        Device.add(new FaderPortV2());
        Device.add(new ioStation24c());
        Device.add(new LaunchpadX());
        Device.add(new LaunchpadXCompanion());
        Device.add(new DJControlStarlight());

        // - Setup Devices -

        Configuration.allowWrite(true); // make config available writable

        for (Device d : Device.list) d.setupDevice();

        // - Start Gui -

        Gui.setup();

        // - Init Messages -

        Osc.startListening();
        Osc.sendInitMessages();


    }
}

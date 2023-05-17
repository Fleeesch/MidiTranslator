package com.fleeesch.miditranslator.device.hardware;

import com.fleeesch.miditranslator.data.parameter.settings.Settings;

public class LaunchpadXCompanion extends LaunchpadX {


    //************************************************************
    //      Constructor
    //************************************************************

    public LaunchpadXCompanion() {

        super(true);

        name = "Launchpad X Companion";
        vendor = "Novation";
        product = "Launchpad X";

        midiInList.add("MIDIIN2 (2- LPX MIDI)");
        midiOutList.add("MIDIOUT2 (2- LPX MIDI)");

        settings = new Settings("Launchpad X Companion Settings"); // setup settings

        axis = 1; // rotate 90°

        useAltPort = true;

        setupConfiguration();



    }


}

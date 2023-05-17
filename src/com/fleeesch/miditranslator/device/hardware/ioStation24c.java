package com.fleeesch.miditranslator.device.hardware;

import com.fleeesch.miditranslator.data.parameter.settings.Settings;

public class ioStation24c extends FaderPortV2{

    public ioStation24c(){

        super(true);

        // name of device
        name = "ioStation 24c";
        vendor = "PreSonus";
        product = "ioStation 24c";

        midiInList.add("ioStation 24c MIDI In");
        midiOutList.add("ioStation 24c MIDI Out");

        // faderport system exclusive header
        setSysExHeader(0x00, 0x01, 0x06, 0x16);
        sysexForceOpenClose = true;

        // setup settings
        settings = new Settings("FaderPort ioStation24c Settings");

        setupConfiguration();

    }
}

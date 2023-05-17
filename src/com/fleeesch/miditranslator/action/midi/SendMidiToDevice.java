package com.fleeesch.miditranslator.action.midi;

import com.fleeesch.miditranslator.device.Device;

public class SendMidiToDevice extends SendMidiAction {

    //************************************************************
    //      Variables
    //************************************************************

    private final Device device;

    private final int[] midiMessage;

    //************************************************************
    //      Constructor
    //************************************************************

    public SendMidiToDevice(Device pDevice, int pPortIdx, int[] pMessage) {

        super(pPortIdx, pMessage);

        device = pDevice;

        midiMessage = pMessage;

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        device.midi.SendMessage(portIdx, midiMessage[0], midiMessage[1], midiMessage[2]);

    }


}

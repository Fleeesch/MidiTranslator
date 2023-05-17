package com.fleeesch.miditranslator.action.midi;

import com.fleeesch.miditranslator.data.midi.Midi;

public class SendMidiOnRelease extends SendMidiAction {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    //************************************************************
    //      Constructor
    //************************************************************

    public SendMidiOnRelease(int pPortIdx, int[] pMessage) {

        super(pPortIdx, pMessage);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // don't send anything on release
        if (pVal > 0) return;

        // simple 7-bit message
        if (twoByte) Midi.Daw.SendMessage(portIdx, message[0], message[1]);
        else if (setValue)
            Midi.Daw.SendMessage(portIdx, message[0], message[1], message[2]);
        else
            Midi.Daw.SendMessage(portIdx, message[0], message[1], (int) (pVal * 0x7F));


    }

}

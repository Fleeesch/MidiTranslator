package com.fleeesch.miditranslator.action.midi;

import com.fleeesch.miditranslator.data.midi.Midi;

public class SendMidiDirect extends SendMidiAction {

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

    public SendMidiDirect(int pPortIdx, int[] pMessage) {

        super(pPortIdx, pMessage);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // simple 7-bit message
        if (twoByte) Midi.Daw.SendMessage(portIdx, message[0], message[1]);
        else if (setValue) Midi.Daw.SendMessage(portIdx, message[0], message[1], message[2]);
        else Midi.Daw.SendMessage(portIdx, message[0], message[1], (int) (pVal * 0x7F));

    }

}

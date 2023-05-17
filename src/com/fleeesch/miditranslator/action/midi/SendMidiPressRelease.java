package com.fleeesch.miditranslator.action.midi;

import com.fleeesch.miditranslator.data.midi.Midi;

public class SendMidiPressRelease extends SendMidiAction {

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

    public SendMidiPressRelease(int pPortIdx, int[] pMessage) {

        super(pPortIdx, pMessage);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // send 7 bit message depending on press / release

        if (pVal > 0) {

            if (twoByte) Midi.Daw.SendMessage(portIdx, message[0], message[1]);
            else Midi.Daw.SendMessage(portIdx, message[0], message[1], 0x7F);


        } else {

            if (twoByte) Midi.Daw.SendMessage(portIdx, message[0], message[1]);
            else Midi.Daw.SendMessage(portIdx, message[0], message[1], 0);

        }

    }

}

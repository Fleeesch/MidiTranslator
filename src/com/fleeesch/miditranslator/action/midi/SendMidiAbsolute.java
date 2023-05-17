package com.fleeesch.miditranslator.action.midi;

import com.fleeesch.miditranslator.data.midi.Midi;

public class SendMidiAbsolute extends SendMidiAction {

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

    public SendMidiAbsolute(int pPortIdx, int[] pMessage) {

        super(pPortIdx, pMessage);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // get 7 bit value
        int msg = (int) (pVal * 127);

        // don't do anything if last sent value matches
        if (lastSentValue == msg) return;

        // send midi
        Midi.Daw.SendMessage(portIdx, message[0], message[1], msg);

        // store last message
        lastSentValue = msg;

    }




}

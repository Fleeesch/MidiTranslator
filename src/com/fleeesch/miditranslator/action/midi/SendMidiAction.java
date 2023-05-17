package com.fleeesch.miditranslator.action.midi;

import com.fleeesch.miditranslator.action.Action;

//* * * * * * * * * * * * * * * * * * * * * * * *
//  Class

public abstract class SendMidiAction extends Action {

    //************************************************************
    //      Variables
    //************************************************************

    protected int portIdx; // port index to use on device

    protected final int[] message; // message to send
    protected double lastSentValue = -1; // last sent value

    boolean twoByte = false; // is a 2 byte message ?
    boolean setValue = false; // has a set value ?


    //************************************************************
    //      Constructor
    //************************************************************

    public SendMidiAction(int pPortIdx, int[] pMessage) {

        portIdx = pPortIdx; // store port index

        message = pMessage; // store message array

        // check if message is just 2 byte
        if (message.length < 3 && (message[0] & 0xF0) == 0xC0) twoByte = true;

        // use set value if given message is long enough
        if (message.length >= 3) setValue = true;

    }


}

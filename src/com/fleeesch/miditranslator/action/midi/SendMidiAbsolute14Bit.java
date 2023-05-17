package com.fleeesch.miditranslator.action.midi;

import com.fleeesch.miditranslator.data.midi.Midi;
import com.fleeesch.miditranslator.data.parameter.Parameter;

public class SendMidiAbsolute14Bit extends SendMidiAbsolute {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public boolean bypass14Bit;

    //************************************************************
    //      Constructor
    //************************************************************

    public SendMidiAbsolute14Bit(int pPortIdx, int[] pMessage) {

        super(pPortIdx, pMessage);

    }

    //************************************************************
    //      Method : Bypass 14 Bit by Parameter
    //************************************************************

    public void bypass14BitByParameter(Parameter pParameter){

        pParameter.addEventHandler(this);

    }

    //************************************************************
    //      Event : On Parameter Change
    //************************************************************

    @Override
    public void onParameterChange(Parameter pParameter) {

        bypass14Bit = pParameter.get() > 0;


    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        if(bypass14Bit){
            super.trigger(pVal);
            return;
        }

        // get 7 bit value
        int msg = (int) (pVal * 16383);

        int b1 = msg >> 7;
        int b2 = msg & 127;

        // don't do anything if last sent value matches
        if (lastSentValue == msg) return;

        // send midi
        Midi.Daw.SendMessage(portIdx, message[0], message[1], b1);
        Midi.Daw.SendMessage(portIdx, message[0], message[1] + 32, b2);

        // store last message
        lastSentValue = msg;

    }


}

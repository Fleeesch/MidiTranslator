package com.fleeesch.miditranslator.action.midi;

import com.fleeesch.miditranslator.data.midi.Midi;

public class SendMidiRelative extends SendMidiAction {

    //************************************************************
    //      Variables
    //************************************************************

    private boolean useFixedValue;
    private int fixedValueRange;

    //************************************************************
    //      Constructor
    //************************************************************

    public SendMidiRelative(int pPortIdx, int[] pMessage) {

        super(pPortIdx, pMessage);

    }

    //************************************************************
    //      Method : Fixed Value
    //************************************************************

    public void fixedValue(int pVal){

        fixedValueRange = pVal;

        useFixedValue = true;

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        if(useFixedValue) {

            if (twoByte)
                Midi.Daw.SendMessage(portIdx, message[0], pVal > 0 ? 64 + fixedValueRange : 64 - fixedValueRange, 0);
            else
                Midi.Daw.SendMessage(portIdx, message[0], message[1], pVal > 0 ? 64 + fixedValueRange : 64 - fixedValueRange);

        }else{

            if (twoByte)
                Midi.Daw.SendMessage(portIdx, message[0], (int) (pVal * 127) + 64, 0);
            else
                Midi.Daw.SendMessage(portIdx, message[0], message[1], (int) (pVal * 127) + 64);

        }

    }


}

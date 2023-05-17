package com.fleeesch.miditranslator.element.virtual.interpreter;


import com.fleeesch.miditranslator.data.midi.Midi;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.element.input.fader.FaderMotorized;
import com.fleeesch.miditranslator.element.virtual.listener.ListenerMidiLSB;

public class InterpreterMidi14Bit extends InterpreterDirect {

    //************************************************************
    //      Variables
    //************************************************************

    int midiAddressMSB;
    int midiAddressLSB;

    int valueMSB;

    boolean bypass14Bit;


    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterMidi14Bit(String pName) {

        super(pName);

    }

    //************************************************************
    //      Method : Link to Feedback
    //************************************************************

    public void linkToFeedback(int pStatusByte, int pControlChange) {

        midiAddressMSB = Midi.getLookupAddress(pStatusByte, pControlChange);
        midiAddressLSB = Midi.getLookupAddress(pStatusByte, pControlChange + 32);

        linkToFeedback(midiAddressMSB);

        new ListenerMidiLSB(this, midiAddressLSB);

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
    //      Method : On LSB Input
    //************************************************************

    @Override
    public void onLsbInput(int pVal) {

        if(bypass14Bit)return;

        double outVal = (pVal + (128 * valueMSB)) / 16383.0;

        // shift condition is ok?
        if (conditionCheckPositive) {

            // go through motorized faders and set their positions
            for (FaderMotorized mt : sourceMotorFader) mt.setPositionViaSoftware(outVal);

        }

    }

    //************************************************************
    //      Method : Handle Software Input
    //************************************************************

    public void handleSoftwareInput(double pVal) {
        // original method -> store value

        if (bypass14Bit) {
            super.handleSoftwareInput(pVal);
            return;
        }

        valueMSB = (int) (pVal * 127);

    }

}

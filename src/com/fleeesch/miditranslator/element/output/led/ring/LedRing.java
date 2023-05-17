package com.fleeesch.miditranslator.element.output.led.ring;


import com.fleeesch.miditranslator.element.output.led.Led;

// !! whole thing needs an overhaul

public class LedRing extends Led {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public int lastSentValue = -1; // last sent value
    final int[] offset = new int[5]; // offset values for different display-styles

    //************************************************************
    //      Constructor
    //************************************************************


    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Name + Adress

    public LedRing(String pName, int m1, int m2) {

        super(pName, m1, m2);

        // offset for display-styles based on product / company
        if (device.vendor.contains("Behringer")) {
            offset[0] = 1;
            offset[1] = 17;
            offset[2] = 33;
        }


    }

    //************************************************************
    //      Method : SetValue
    //************************************************************


    public void setParameterValue(double val) {

        int valOut;
        int mode = 0;

        valOut = (int) (val * 12.0) + offset[0];
        if (valOut == lastSentValue) return;
        device.midi.SendMessage(midiAddress1, midiAddress2, valOut);
        lastSentValue = valOut;
    }


}

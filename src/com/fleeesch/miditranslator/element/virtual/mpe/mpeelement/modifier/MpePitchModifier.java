package com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.modifier;

import com.fleeesch.miditranslator.element.virtual.mpe.MpeDataHandler;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.MpeElement;

public class MpePitchModifier extends MpeElement {

    //************************************************************
    //      Variables
    //************************************************************

    // pitch value of modifier
    public double pitchValue;

    // pitch range of modifier
    final double pitchRange;

    //************************************************************
    //      Constructor
    //************************************************************

    public MpePitchModifier(String pName, MpeDataHandler pDataHandler, double pPitch) {

        super(pName, pDataHandler); // use original constructor

        // store pitch range
        pitchRange = pPitch;
        // set led color
        pDataHandler.setColor(0.3,1,0.2);

        if (mpeDataHandler.padPressureSensitive != null) {
            mpeDataHandler.padPressureSensitive.instantPressure = false;
        }

    }


    //************************************************************
    //      Event : On MPE Z Change
    //************************************************************

    public void handleMpeZ(double pVal) {

        pitchValue = pVal * pitchRange; // calculate pitch value

        mpeSurface.handlePitch(pVal); // let the surface handle the new value

    }


}
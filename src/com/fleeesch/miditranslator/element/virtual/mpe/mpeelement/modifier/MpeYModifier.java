package com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.modifier;

import com.fleeesch.miditranslator.element.virtual.mpe.MpeDataHandler;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.MpeElement;

public class MpeYModifier extends MpeElement {

    //************************************************************
    //      Variables
    //************************************************************


    // current modulation value
    public double modValue;


    //************************************************************
    //      Constructor
    //************************************************************

    public MpeYModifier(String pName, MpeDataHandler pDataHandler) {

        super(pName, pDataHandler); // original constructor

        pDataHandler.setColor(1,0.7,0);

        if (mpeDataHandler.padPressureSensitive != null) {
            mpeDataHandler.padPressureSensitive.instantPressure = false;
        }

    }


    //************************************************************
    //      Event : On MPE Z Change
    //************************************************************


    public void handleMpeZ(double pVal) {

        modValue = pVal; // store mod value

        mpeSurface.handleY(pVal); // pass to surface to take care of the data

    }


}
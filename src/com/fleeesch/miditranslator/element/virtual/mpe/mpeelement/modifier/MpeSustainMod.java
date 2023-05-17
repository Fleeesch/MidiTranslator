package com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.modifier;

import com.fleeesch.miditranslator.element.virtual.mpe.MpeDataHandler;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.MpeElement;

public class MpeSustainMod extends MpeElement {

    //************************************************************
    //      Variables
    //************************************************************

    // sustain state
    public boolean active;

    // latch state
    public boolean latch;


    //************************************************************
    //      Constructor
    //************************************************************

    public MpeSustainMod(String pName, MpeDataHandler pDataHandler) {

        super(pName, pDataHandler); // original constructor

        mpeDataHandler.setColor(0.1, 0, 1);


    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleTrigger(double pVal) {

        // pad is pressed?
        if (pVal > 0) {

            // trigger latch if source is pressed hard
            if (mpeDataHandler.hardPress) latch = !latch;

            // sustain is active
            active = true;

            // process sustain state as true
            setSustain(true);

        } else if (!latch) {
            // process sustain state as false
            setSustain(false);
        }

        // update the led
        updateLed();

    }

    //************************************************************
    //      Method : Update Led
    //************************************************************

    public void updateLed() {

        // set color depending on latch/active or nonactive
        if (latch) {
            mpeDataHandler.setColor(0.3, 0.1, 1);
        } else {
            if (active) mpeDataHandler.setColor(0.2, 0.1, 1);
            else mpeDataHandler.setColor(0.1, 0.05, 1);
        }

    }


    //************************************************************
    //      Method : Set Sustain
    //************************************************************

    public void setSustain(boolean pState) {

        active = pState; // store state

        mpeSurface.handleSustain(this, active); // pass to surface to handle the sustain

    }

}




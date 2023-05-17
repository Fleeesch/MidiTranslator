package com.fleeesch.miditranslator.element.virtual.controller.motorfader.control;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.MotorFaderController;

import java.util.ArrayList;
import java.util.List;

public class MfcMulti extends MfcControl {

    //************************************************************
    //      Variables
    //************************************************************

    public List<MfcSingle> singleElements = new ArrayList<>(); // list of elements linked to the global switch

    public boolean hasActivation; // can activate all triggers at once?

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // Optional Led Inversion
    public MfcMulti(String pName, MotorFaderController pController, boolean pHasActivation, boolean pInvertLed) {

        super(pName); // use virtual instrument constructor

        controller = pController; // store controller

        parameterControlsLed = true; // led has dynamic control

        hasActivation = pHasActivation; // can batch-turn-on?

        invertLed = pInvertLed;

    }

    //* * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // No Led Inversion
    public MfcMulti(String pName, MotorFaderController pController, boolean pHasActivation) {

        super(pName); // use virtual instrument constructor

        controller = pController; // store controller

        parameterControlsLed = true; // led has dynamic control

        hasActivation = pHasActivation; // can batch-turn-on?

    }

    //************************************************************
    //      Method : Add Target
    //************************************************************

    public void addTarget(OutputElement e) {

        super.addTarget(e); // add target

        setParameterValue(0); // set led off by default


    }

    //************************************************************
    //      Method : Set Members
    //************************************************************

    public void setMembers(List<MfcSingle> pMember) {

        singleElements = pMember; // store addresses to single switches

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        if (pVal <= 0) return; // ignore button release

        trigger(); // trigger action

        controller.update(); // update motor fader controller

    }

    //************************************************************
    //      Method : Set State
    //************************************************************

    public void trigger() {

        // can turn on all switches at once?
        if (hasActivation) {

            boolean active = false; // assume nothing is pressed

            // go through elements
            for (MfcSingle s : singleElements) {
                // activated switch found?
                if (s.toggleState) {
                    active = true; // remember that there's an active one, escape
                    break;
                }
            }

            // no active switches found? activate all at once!
            if (!active) {
                for (MfcSingle s : singleElements) s.setState(true);
                return;
            }

        }

        // turn off all switches by default
        for (MfcSingle s : singleElements) s.setState(false);


    }

    //************************************************************
    //      Method : Update Led
    //************************************************************

    public void updateLed() {

        // go through elements
        for (MfcSingle s : singleElements) {

            // found one active?
            if (s.toggleState) {

                setParameterValue(1);
                //if (!invertLed) setParameterValue(1);
                //else setParameterValue(0);

                for (OutputElement e : targetElements) e.update(); // inform led
                return;
            }

        }

        // turn off led
        setParameterValue(0);
        //if (!invertLed) setParameterValue(0);
        //else setParameterValue(1);

        for (OutputElement e : targetElements) e.update(); // inform led

    }

}

package com.fleeesch.miditranslator.element.virtual.controller.motorfader.control;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.fader.FaderMotorized;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.MotorFaderController;

public class MfcSingle extends MfcControl {

    //************************************************************
    //      Variables
    //************************************************************

    public int index; // fader index

    public final FaderMotorized fader; // fader the switch is linked to

    public boolean toggleState = false; // current state of switch

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // Optional Led Inversion
    public MfcSingle(String pName, MotorFaderController pController, boolean pInvertLed) {

        super(pName); // use virtual instrument constructor

        controller = pController; // store controller

        parameterControlsLed = true; // led can be controlled by parameter

        fader = controller.faders.get(index); // store fader

        parameter.name = pName + " MFC"; // give parameter a custom name (for storing in settings

        invertLed = pInvertLed;

    }

    //* * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // No Led Inversion

    public MfcSingle(String pName, MotorFaderController pController) {

        super(pName); // use virtual instrument constructor

        controller = pController; // store controller

        parameterControlsLed = true; // led can be controlled by parameter

        fader = controller.faders.get(index); // store fader

        parameter.name = pName + " MFC"; // give parameter a custom name (for storing in settings

    }


    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        if (pVal <= 0) return; // ignore button release

        setState(!toggleState); // toggle state

        controller.update(); // update the motor controller

    }

    //************************************************************
    //      Method : Set State
    //************************************************************

    public void setState(boolean pState) {

        // set parameter value for led and boolean for data analysis
        if (pState) {

            parameter.set(1);

            //if (!invertLed) parameter.set(1); else
            //parameter.set(0);

            toggleState = true;
        } else {

            parameter.set(0);
            //if (!invertLed) parameter.set(0); else
            //parameter.set(1);

            toggleState = false;
        }

    }

    //************************************************************
    //      Method : Add Target
    //************************************************************

    public void addTarget(OutputElement e) {

        super.addTarget(e); // add target

        setParameterValue(0); // make sure leds is off by default

    }


    //************************************************************
    //      Method : Update Led
    //************************************************************

    public void updateLed() {

        for (OutputElement e : targetElements) e.update(); // just update leds

    }


}

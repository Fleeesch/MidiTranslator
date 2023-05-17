package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.action.ActionGroup;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.fader.FaderMotorized;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

public class InterpreterDirect extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    public boolean ledAlwaysOn = false;

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterDirect(String pName) {

        super(pName);

    }

    //************************************************************
    //      Method : Led Always On
    //************************************************************

    public void ledAlwaysOn() {

        ledAlwaysOn = true;

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        // original method -> store value
        super.handleInput(pSource, pVal);

        // trigger all the action groups one by one
        if (!hasMotorFader()) for (ActionGroup a : actions) a.trigger(pVal);
        else actions.get(0).trigger(pVal);

    }


    //************************************************************
    //      Method : Handle Software Input
    //************************************************************

    public void handleSoftwareInput(double pVal) {

        // original method -> store value
        super.handleSoftwareInput(pVal);


        // shift condition is ok?
        if (conditionCheckPositive) {

            // go through motorized faders and set their positions
            for (FaderMotorized mt : sourceMotorFader) mt.setPositionViaSoftware(pVal);

        }

        if (hasRGBControl()) {

            if (pVal >= 1) setParameterValue(4, 1);
            else if (ledAlwaysOn || pVal != 0) setParameterValue(4, 0.10);
            else setParameterValue(4, 0);

        }

        // go through linked output elements
        for (OutputElement et : targetElements) {

            try {

                // set their value and refresh
                et.setParameterValue(pVal);
                et.update();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }

}

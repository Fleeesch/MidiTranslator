package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.pad.PadPressureSensitive;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

public class InterpreterVelocity extends VirtualElement {

    /*
        ~~

        Uses a velocity sensitive source to trigger a different action
        on a hard press.

     */

    //************************************************************
    //      Variables
    //************************************************************

    // values for both actions
    final double value1;
    final double value2;

    // pressure sensitive pad as source
    PadPressureSensitive padPressureSensitive;

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterVelocity(String pName, double pVal1, double pVal2) {

        super(pName); // use original constructor

        // store values for both actions
        value1 = pVal1;
        value2 = pVal2;

    }


    //************************************************************
    //      Method : Add Source
    //************************************************************

    @Override
    public void addSource(InputElement e) {
        super.addSource(e);

        // store pressure sensitive pad if it exists
        if (e instanceof PadPressureSensitive) padPressureSensitive = (PadPressureSensitive) e;

    }


    //************************************************************
    //      Event : Handle Input
    //************************************************************

    @Override
    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {
        super.handleInput(pSource, pVal, pMidiMsg);

        // do nothing on release
        if (pVal <= 0) return;

        // trigger 2nd action if available
        if (dualAction && padPressureSensitive != null && padPressureSensitive.hardPress) {
            actions.get(1).trigger(value2);
            return;
        }

        // trigger normal action on regular hit
        actions.get(0).trigger(value1);

    }


}

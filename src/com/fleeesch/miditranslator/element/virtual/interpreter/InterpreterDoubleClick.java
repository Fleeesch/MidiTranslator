package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.action.ActionGroup;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

import javax.swing.*;

public class InterpreterDoubleClick extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    final Timer countTimer; // timer

    final int duration = 250; // double click timeout

    boolean timeOut = false; // timeout indicator

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterDoubleClick(String pName) {

        super(pName);

        // setup timer
        countTimer = new Timer(duration, e -> {
            timeOut = true; // indicate timeout
        });

        countTimer.setRepeats(false); // only a single call


    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        // original method -> store value
        super.handleInput(pSource, pVal);

        // ignore release
        if (pVal <= 0) return;

        // timer not running?
        if (!countTimer.isRunning()) {

            // reset timeout, start timer
            timeOut = false;
            countTimer.start();

            if (dualAction) actions.get(0).trigger(1);

            // escape
            return;

        }

        // timeout? don't do anything
        if (timeOut) return;


        // double click ok: trigger actions!
        if (!dualAction) for (ActionGroup a : actions) a.trigger(1);
        else actions.get(1).trigger(1);


    }


    //************************************************************
    //      Method : Handle Software Input
    //************************************************************

    public void handleSoftwareInput(double pVal) {

        // original method -> store value
        super.handleSoftwareInput(pVal);

        if (hasRGBControl) {

            if (pVal >= 1) setParameterValue(4, 1);
            else if (pVal != 0) setParameterValue(4, 0.20);
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

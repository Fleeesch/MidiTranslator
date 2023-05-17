package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

import javax.swing.*;
import java.awt.event.ActionListener;

public class InterpreterHold extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    public final int time = 200; // time for hold activation

    // routine / timer related
    public Timer routine; // routine
    public ActionListener routineTask; // routine task
    public int routineClock = 0; // clock for routine

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterHold(String pName) {

        super(pName);

        setupRoutine(); // setup the routine
    }

    //************************************************************
    //      Method : Setup Routine
    //************************************************************

    public void setupRoutine() {

        // setup routine task
        routineTask = evt -> {

            routineClock++; // increment clock

            // toggle led flashing
            if ((routineClock & 15) == 0) setTargetLedVisibility(2);


            // time passed ? stop routine, pass check as a success
            if (routineClock > time) stopRoutine(true);


        };

        // setup timer to be used as a routine
        routine = new Timer(1, routineTask);

    }


    //************************************************************
    //      Method : Start Routine
    //************************************************************

    public void startRoutine() {

        if (routine.isRunning()) return; // don't do anything if routine is already running

        routineClock = 0; // reset clock

        routine.start(); // start routine

        setTargetLedVisibility(0); // turn off leds if there are any

    }

    //************************************************************
    //      Method : Stop Routine
    //************************************************************

    public void stopRoutine(boolean pass) {

        // don't do anything if routine is already running
        if (!routine.isRunning()) return;

        // enable led again
        setTargetLedVisibility(1);

        // stop routine
        routine.stop();

        // activate actions depending on hold success and availability
        if (pass) {

            if (dualAction)
                actions.get(1).trigger(1);
            else
                actions.get(0).trigger(1);

        } else {

            if (dualAction) actions.get(0).trigger(1);

        }


    }


    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        super.handleInput(pSource, pVal); // store value

        // start or stop routine depending on button state
        if (pVal > 0) {
            startRoutine();
        } else {
            stopRoutine(false);
        }

    }

    //************************************************************
    //      Method : Handle Software Input
    //************************************************************

    public void handleSoftwareInput(double pVal) {

        // original method -> store value
        super.handleSoftwareInput(pVal);

        if (hasRGBControl) {

            if (pVal > 0) setParameterValue(4, 1);
            else setParameterValue(4, 0.10);

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

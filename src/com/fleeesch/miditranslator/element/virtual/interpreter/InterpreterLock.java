package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

import javax.swing.*;
import java.awt.event.ActionListener;

public class InterpreterLock extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    public final int time = 200; // time for hold activation

    public boolean timePassed; // positive if button is held long enough

    // routine / timer related
    public Timer routine; // routine
    public ActionListener routineTask; // routine task
    public int routineClock = 0; // clock for routine

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterLock(String pName) {

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

        timePassed = false;

        routine.start(); // start routine

    }

    //************************************************************
    //      Method : Stop Routine
    //************************************************************

    public void stopRoutine(boolean pass) {

        // don't do anything if routine is already running
        if (!routine.isRunning()) return;

        // stop routine
        routine.stop();

        if (pass) timePassed = true;

    }


    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        super.handleInput(pSource, pVal); // store value

        // start or stop routine depending on button state
        if (pVal > 0) {

            actions.get(0).trigger(1);
            startRoutine();

        } else {

            if (timePassed) {

                if (dualAction) actions.get(1).trigger(1);
                else actions.get(0).trigger(1);

            }

            stopRoutine(false);
        }

    }


}

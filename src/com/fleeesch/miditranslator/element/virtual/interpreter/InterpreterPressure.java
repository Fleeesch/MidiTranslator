package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.pad.PadPressureSensitive;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.functions.math.Calculate;

import javax.swing.*;
import java.awt.event.ActionListener;

public class InterpreterPressure extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    // repeat rates
    final int rateMinimum;
    final int rateMaximum;

    // values depending on pressure
    final double valueMinimum;
    final double valueMaximum;

    // clock for Timer for flexible timer adjustments
    int timerClock;

    // clock ceiling, how many ticks for an action trigger
    int clockCeil;

    // raw pressure
    double pressure;

    // calculated value for routine
    double routineValue;

    // flag raised when velocity is registered as hard press
    boolean hardLock = false;

    // pressed state of source element
    boolean pressed = false;

    // timer for looped routine
    final Timer routineTimer;
    final ActionListener routineTask;

    // source element stored as pressure sensitive pad
    PadPressureSensitive padPressureSensitive;

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterPressure(String pName, double pRateMin, double pRateMax, double pValMin, double pValMax) {

        super(pName); // use original constructor

        // store rates
        rateMinimum = (int) pRateMin;
        rateMaximum = (int) pRateMax;

        // store values
        valueMinimum = pValMin;
        valueMaximum = pValMax;

        clockCeil = rateMinimum;
        timerClock = 0;

        // task that is called during the routine
        routineTask = e -> routine();

        // routine (using minimum rate by default
        routineTimer = new Timer(1, routineTask);
        routineTimer.setInitialDelay(10);


    }


    //************************************************************
    //      Method : Add Source
    //************************************************************

    @Override
    public void addSource(InputElement e) {
        super.addSource(e);

        // store input element cast as pressure pad if it is one
        if (e instanceof PadPressureSensitive) padPressureSensitive = (PadPressureSensitive) e;

    }


    //************************************************************
    //      Method : Routine
    //************************************************************

    public void routine() {

        timerClock++;

        if (timerClock >= clockCeil) {

            // trigger first action using the value calculated at pressure input
            actions.get(0).trigger(routineValue);

            timerClock = 0;
        }

    }

    //************************************************************
    //      Method : Start Routine
    //************************************************************

    public void startRoutine() {
        routineTimer.start(); // start routine
    }

    //************************************************************
    //      Method : Stop Routine
    //************************************************************

    public void stopRoutine() {
        routineTimer.stop(); // stop routine
    }

    //************************************************************
    //      Event : Handle Input
    //************************************************************

    @Override
    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {
        super.handleInput(pSource, pVal, pMidiMsg); // original method

        // source is released?
        if (pVal <= 0) {

            hardLock = false; // reset hard lock
            pressed = false; // is not pressed anymore

            return; // skip the rest
        }

        // is pressed
        pressed = true;

        // 2nd action available and pressure sensitive pad registered a hard press?
        if (dualAction && padPressureSensitive != null && padPressureSensitive.hardPress) {

            actions.get(1).trigger(1); // trigger second action
            hardLock = true; // lock, preventing routine start

            stopRoutine(); // stop any ongoing routine

            return; // skip the rest
        }

        // calculate delay for timer based on velocity (harder hit > longer delay)
        routineTimer.setInitialDelay((int) Calculate.rescaleValue(pVal, 0, 1, 20, 60));

        // trigger original action on regular hit
        actions.get(0).trigger(valueMinimum);

    }

    //************************************************************
    //      Method : handle Pressure Change
    //************************************************************


    @Override
    public void handleInputRawPressure(double pVal) {

        pressure = pVal; // store pressure

        // hardlock is on, or source is not pressed? skip the rest
        if (hardLock || !pressed) return;

        // pressure is above 0?
        if (pVal > 0) {

            // calculate timer interval
            clockCeil = (int) Calculate.rescaleValue(pVal, 0, 1, rateMinimum, rateMaximum);

            // calculate value used in routine
            routineValue = Calculate.rescaleValue(pVal, 0, 1, valueMinimum, valueMaximum);


            // start the routine
            startRoutine();

        } else {
            stopRoutine(); // stop if source is not pressed
        }

    }
}

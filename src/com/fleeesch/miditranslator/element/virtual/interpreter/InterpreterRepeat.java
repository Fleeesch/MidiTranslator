package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.action.ActionGroup;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

import javax.swing.*;
import java.awt.event.ActionListener;

public class InterpreterRepeat extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    public final int initSpeed; // initial timer speed
    public final int destSpeed; // fasted timer speed after acceleration

    public int speed; // current speed
    // routine / timer related
    public final Timer routine; // routine
    public final ActionListener routineTask; // routine task
    public int routineClock = 0; // clock for routine
    double accelerationClock = 0; // accelearation counter
    double acceleration; // acceleration factor

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterRepeat(String pName, int pInitSpeed, int pDestSpeed, double pAcceleration) {

        super(pName); // store name

        // transfer speed values
        initSpeed = pInitSpeed;
        acceleration = pAcceleration;
        destSpeed = pDestSpeed;

        // setup routine task
        routineTask = evt -> routine();

        // setup timer to be used as a routine
        routine = new Timer(pInitSpeed, routineTask);

    }


    //************************************************************
    //      Method : Routine
    //************************************************************

    void routine() {

        // trigger actions
        for (ActionGroup a : actions) a.trigger(1);

        // speed is already at goal speed? don't do anything
        if (speed <= destSpeed) return;

        // increment acceleration clock
        accelerationClock += acceleration;

        // clock is at least 1?
        if (accelerationClock > 1) {

            // fasten things up
            speed -= accelerationClock;

            // limit speed
            if (speed < destSpeed) speed = destSpeed;

            // setup timer with new speed value
            routine.setDelay(speed);

            // reset the clock
            accelerationClock = 0;
        }


    }

    //************************************************************
    //      Method : Start Routine
    //************************************************************

    public void startRoutine() {

        if (routine.isRunning()) return; // don't do anything if routine is already running

        // reset speed clock values
        speed = initSpeed;
        accelerationClock = 0;

        // set initial timer speed
        routine.setDelay(speed);

        routine.start(); // start routine

    }

    //************************************************************
    //      Method : Stop Routine
    //************************************************************

    public void stopRoutine() {

        // don't do anything if routine is already running
        if (!routine.isRunning()) return;

        // stop routine
        routine.stop();


    }


    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        super.handleInput(pSource, pVal); // store value

        // start or stop routine depending on button state
        if (pVal > 0) {

            // always send actions on initial press
            for (ActionGroup a : actions) a.trigger(1);

            // start the timer
            startRoutine();

        } else {

            // stop the timer
            stopRoutine();

        }

    }


}

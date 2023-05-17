package com.fleeesch.miditranslator.element.input.fader;


import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.functions.math.Calculate;

import javax.swing.*;
import java.awt.event.ActionListener;


/*

    ~~

    There's currently a smoothing feature included that allows you to smooth out incoming data.
    It's just a value chasing routine. In theory, it should limit the amount of data to process for fast motions,
    but practically it's very convoluted and messes up all the motor fader motions.

 */
public class Fader7bit extends InputElement {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public Timer routine; // routine for smoothing
    public double lastValue = 0; // last value
    public boolean useSmoothing = false; // indicator whether to use smoothing or not
    public final double smoothFactor; // smoothing factor
    // values for optional rescaling
    public boolean rescaleInput = false;
    ActionListener routineTask; // routine method
    double rescaleMin = 0.0;
    double rescaleMax = 1.0;


    //************************************************************
    //      Constructor
    //************************************************************


    public Fader7bit(String pName, double pSmooth, int m1, int m2) {

        super();

        name = pName; // transfer name

        setMidiAddress(m1, m2); // setup midi lookup address

        device.fader.add(this); // add fader to device indexing list

        smoothFactor = pSmooth; // store smoothing factor

        setupRoutine(); // setup potential routine process

    }

    //************************************************************
    //      Method : Rescale Input
    //************************************************************

    public void rescaleInput(double pLow, double pHigh) {

        rescaleInput = true;

        rescaleMin = pLow;
        rescaleMax = pHigh;

    }

    //************************************************************
    //      Method : Start Routine
    //************************************************************

    public void startRoutine() {

        if (routine.isRunning()) return; // don't do anything if routine is already running

        routine.start();

    }

    //************************************************************
    //      Method : Stop Routine
    //************************************************************

    public void stopRoutine() {

        routine.stop();

    }

    //************************************************************
    //      Method : Routine
    //************************************************************

    public void setupRoutine() {

        InputElement self = this;

        // smoothing task declaration
        routineTask = evt -> {


            // increment by partial delta value using the smoothing factor
            lastValue += (parameter.get() - lastValue) * smoothFactor;


            // if delta is small enough quit the routine, send a final message
            if (Math.abs(parameter.get() - lastValue) < 0.001) {

                lastValue = parameter.get(); // make source and destination value equal

                // set value for all virtual elements
                for (VirtualElement e : availableTargetElements) e.handleInput(self, lastValue);

                // stop the routine
                stopRoutine();

            } else {

                // set value for all connected virtual elements
                for (VirtualElement e : availableTargetElements) e.handleInput(self, lastValue);

            }

        };

        // setup timer to be used as a routine
        routine = new Timer(1, routineTask);

        // don't do smoothing if factor is set to 0
        useSmoothing = !(smoothFactor <= 0);

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    @Override
    public void handleInput(double val, int[] midiMsg) {

        if (rescaleInput) val = Calculate.rescaleValue(val, rescaleMin, rescaleMax, 0, 1);

        setParameterValue(val); // store current value as target value

        // start smoothing routine if enabled
        if (useSmoothing) {
            startRoutine();
            return;
        }

        // set value directly if there is no smoothing to do
        for (VirtualElement e : availableTargetElements) e.handleInput(this, val);


    }


}

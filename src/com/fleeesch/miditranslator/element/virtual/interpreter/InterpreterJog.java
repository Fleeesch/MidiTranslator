package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.action.ActionGroup;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

import javax.swing.*;
import java.awt.event.ActionListener;

public class InterpreterJog extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    // buffer
    public int buffer;
    public boolean useBuffer;

    // step
    public int step;
    public double stepBuffer;
    public boolean useStep;

    // scale
    public double scalePre;
    public double scalePost;

    // minimal value
    public double minValue;
    public boolean useMinimalValue;

    // value
    public double value;

    // timer
    private int timeOut = 0;
    private final Timer routineTimer;

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterJog(String pName, int pLatency, int pStep, double pMinValue, double pScalePre, double pScalePost) {

        super(pName);

        // settings
        buffer = pLatency;
        step = pStep;
        minValue = pMinValue;

        // usage flags
        if (buffer > 0) useBuffer = true;
        if (pStep > 0) useStep = true;
        if (minValue > 0) useMinimalValue = true;

        // scaling
        scalePre = pScalePre;
        scalePost = pScalePost;

        // timer setup
        ActionListener routineTask = e -> routine();

        routineTimer = new Timer(pLatency, routineTask);


    }

    //************************************************************
    //      Method : Routine
    //************************************************************

    public void routine() {


        if (value != 0) {

            double valOut = value * scalePost; // scaled value
            double valOutCapped = Math.min(Math.max(valOut, -1), 1); // capped value for overshoot

            launchActions(valOut); // trigger actions without overshoot

            // work through value overshoot
            while (Math.abs(value) > 1) {

                value = value > 0 ? value - 1 : value + 1; // increment / decrement to 0

                launchActions(valOutCapped); // launch actions with capped value

            }

            value = 0;
        }

        if (timeOut++ > 10) routineTimer.stop(); // stop routine only after timeout

    }

    //************************************************************
    //      Method : Process Input
    //************************************************************

    public void processIncrement(double pVal) {

        value += pVal * scalePre;

        if (useBuffer) {

            timeOut = 0;
            routineTimer.start();

        } else {

            launchActions(value);
            value = 0;

        }

    }

    //************************************************************
    //      Method : Launch Actions
    //************************************************************

    public void launchActions(double pVal) {

        // keep value above minimal value if required
        if (useMinimalValue && Math.abs(pVal) < minValue) pVal = (pVal > 0) ? minValue : -minValue;

        double outScaled = pVal * scalePost;

        for (ActionGroup a : actions) a.trigger(outScaled);
    }

    //************************************************************
    //      Method : Increment Step Buffer
    //************************************************************

    private boolean incrementStepBuffer(double pVal) {

        stepBuffer += pVal;

        if (Math.abs(stepBuffer) < step) return false;

        stepBuffer = stepBuffer > 0 ? -step : step; // flip buffer counter (no hysteresis)

        return true;
    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        if (useStep) {
            if (incrementStepBuffer(pVal)) processIncrement(pVal);
        } else {
            processIncrement(pVal);
        }


    }


}

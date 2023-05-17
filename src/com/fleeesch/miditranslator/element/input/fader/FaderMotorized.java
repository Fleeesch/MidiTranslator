package com.fleeesch.miditranslator.element.input.fader;


import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.fader.handler.FaderTouchHandler;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.functions.math.Calculate;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FaderMotorized extends Fader14Bit {

    //************************************************************
    //      Variables
    //************************************************************

    public final List<VirtualElement> bypassTriggerElements = new ArrayList<>();
    public List<FaderMotorized> faderSet = new ArrayList<>();

    public final FaderTouchHandler touchListener; // touch listener

    public boolean touchState; // touch state of fader

    public double position = 0; // position (0-1)

    public boolean bypassTouchData = false;

    // exact midi position bytes
    public int positionByte1;
    public int positionByte2;

    // bypass state
    public boolean bypass = false;

    // freeze state
    public boolean freeze = false;

    // freeze functionality
    public boolean useFreeze = false;

    // hard reset timer
    public final Timer hardResetTimer;
    public final ActionListener hardResetTask;


    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Name + Adress

    public FaderMotorized(String pName, double pSmooth, int dataStatusByte, int touchM1, int touchM2) {

        // use original fader constructor
        super(pName, pSmooth, dataStatusByte, 0);

        // add to device indexing list
        device.motorFader.add(this);

        // create touch listener from midi data
        touchListener = new FaderTouchHandler(this, touchM1, touchM2);

        // hard reset task (has a little delay)
        hardResetTask = evt -> resetFader();

        // setup hard reset timer
        hardResetTimer = new Timer(100, hardResetTask);
        hardResetTimer.setRepeats(false);

    }

    //************************************************************
    //      Method : Add FaderSet
    //************************************************************

    public void addFaderSet(List<InputElement> pSet) {

        for (InputElement e : pSet) {
            faderSet.add((FaderMotorized) e);
        }

    }

    //************************************************************
    //      Method : Freeze other Faders
    //************************************************************

    public void tryFreezeFaders(boolean pState) {

        if (pState) {

            // try freezing other faders
            for (FaderMotorized e : faderSet) e.trySetFreeze(true);

        } else {

            // check if there is absolutely no fader touch
            for (FaderMotorized e : faderSet) if (e.touchState) return;
            // unfreeze faders
            for (FaderMotorized e : faderSet) e.trySetFreeze(false);

        }

    }

    //************************************************************
    //      Method : Set Touch State
    //************************************************************

    public void setTouchState(boolean pState) {

        touchState = pState; // just update the touch state

        tryFreezeFaders(pState); // try freezing other faders

        // trigger bypass on release
        if (bypass && !touchState) hardReset();

        // don't trigger action if fader is bypassed
        if (bypass || bypassTouchData) return;


        for (VirtualElement e : availableTargetElements) if (e.dualAction) e.handleInputFaderTouch(touchState);

    }


    //************************************************************
    //      Method : Hard Reset
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Skip Delay on Choice

    public void hardReset(boolean noDelay) {

        // should happen instantly?
        if (noDelay) {

            hardResetTimer.stop(); // stop timer

            resetFader(); // reset fader position

            return; // skip reset

        }

        // (re)start the timer
        hardResetTimer.stop();
        hardResetTimer.start();

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  With Delay

    public void hardReset() {
        hardReset(false);
    }

    //************************************************************
    //      Method : Reset Fader (don't store Position)
    //************************************************************

    public void resetFader() {
        // move fader to bottom position
        device.midi.SendMessage(0, midiAddress1, 0, 0);
    }

    //************************************************************
    //      Method : Move Fader
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Given Position

    public void moveFader(int byte1, int byte2) {

        // don't do anything if bypassed or frozen
        if (bypass || freeze) return;

        // send message using bytes
        device.midi.SendMessage(0, midiAddress1, byte1, byte2);

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Last Position

    public void moveFader() {
        // use last position
        moveFader(positionByte1, positionByte2);


    }

    //************************************************************
    //      Method : Set Position via Software
    //************************************************************

    public void setPositionViaSoftware(double pos) {


        // rescale input if required
        if (rescaleInput) {

            // put fader to dead end position of position indicates a border

            if (pos <= 0) {
                setPosition(0);
                return;
            }

            if (pos >= 1) {
                setPosition(1);
                return;
            }


            pos = Calculate.rescaleValueLimit(pos, 0, 1, rescaleMin, rescaleMax);
        }

        setPosition(pos);

    }

    //************************************************************
    //      Method : Set Position
    //************************************************************

    public void setPosition(double pos) {

        position = pos; // store position

        // calculate 14-bit data bytes
        int byte1 = (int) (pos * 16383) & 127;
        int byte2 = (int) (pos * 16383) >> 7;

        // store data bytes to prevent recalculations and rounding errors
        positionByte1 = byte1;
        positionByte2 = byte2;

        // fader is touched ? skip the rest
        if (touchState) return;

        // move fader
        moveFader(byte1, byte2);

    }


    //************************************************************
    //      Method : Handle Input
    //************************************************************

    @Override
    public void handleInput(double val, int[] midiMsg) {

        // don't do anything if bypassed
        if (bypass) return;

        if (rescaleInput) val = Calculate.rescaleValueLimit(val, rescaleMin, rescaleMax, 0, 1);

        // store position as double
        position = val;

        // set parameter value for smoothing
        setParameterValue(position);

        // store position bytes directly from midi message
        positionByte1 = midiMsg[1];
        positionByte2 = midiMsg[2];

        // stop if fader is not touched
        if (!touchState) return;

        // start smoothing routine if enabled
        if (useSmoothing) {
            startRoutine();
            return;
        }

        // set value directly if there is no smoothing to do
        for (VirtualElement e : availableTargetElements) e.handleInput(this, val, midiMsg);


    }

    //************************************************************
    //      Method : Set Freeze
    //************************************************************

    public void trySetFreeze(boolean pState) {

        // no freeze enabled for fader? unfreeze!
        if (!useFreeze) {
            freeze = false;
            return;
        }

        // freeze enabled and currently unfrozen? move fader to position!
        if (freeze && !pState) {
            freeze = pState;
            moveFader();
            return;
        }

        // set freeze state
        freeze = pState;

    }

    //************************************************************
    //      Method : Set Bypass
    //************************************************************

    public void setBypass(boolean pState) {

        bypass = pState; // set bypass state

        // bypass is deactivated?
        if (!bypass) {

            // restore last fader position
            moveFader();

            // turn off linked LEDs using feedback chain
            for (VirtualElement ve : bypassTriggerElements)
                ve.handleSoftwareInput(0);

        } else {

            // instant hard reset, moving the fader to the bottom
            hardReset(true);

            // change LEDs via feedback chain
            for (VirtualElement ve : bypassTriggerElements)
                ve.handleSoftwareInput(1);
        }


    }

}

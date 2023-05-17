package com.fleeesch.miditranslator.element.input.pad;

import com.fleeesch.miditranslator.device.hardware.LaunchpadX;
import com.fleeesch.miditranslator.element.input.pad.handler.PadPressureHandler;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.event.EventHandler;
import com.fleeesch.miditranslator.functions.math.Calculate;
import com.fleeesch.miditranslator.data.lookup.table.launchpadx.LpxAftertouchLookup;
import com.fleeesch.miditranslator.data.lookup.table.launchpadx.LpxVelocityAftertouchLookup;

import javax.swing.*;
import java.awt.event.ActionListener;

public class PadPressureSensitive extends Pad {

    //************************************************************
    //      Variables
    //************************************************************

    public final double smoothDest = 0.5;
    public double smooth = smoothDest;

    public double pressureTarget;

    public boolean instantPressure;

    public final Timer interpolationTimer;
    public final ActionListener interpolationTask;

    //************************************************************
    //      Constructor
    //************************************************************

    public PadPressureSensitive(String pName, int m1, int m2, int ATm1, int ATm2) {

        super(pName, m1, m2);

        new PadPressureHandler(this, name + "Z Handler", ATm1, ATm2);

        interpolationTask = e -> interpolate();

        interpolationTimer = new Timer(1, interpolationTask);

        if (device instanceof LaunchpadX) {
            useLookupTable = true;
            aftertouchTable = LpxAftertouchLookup.table;
            velocityAftertouchTable = LpxVelocityAftertouchLookup.table;
        }

    }

    //************************************************************
    //      Method: Handle Input
    //************************************************************

    @Override
    public void handleInput(double val, int[] msg) {

        super.handleInput(val, msg);

        if (val > 0) {
            initPress();
        } else {
            smooth = smoothDest; // prevents interpolation overload (too many timers?)
            handleZ(0);
        }


    }

    //************************************************************
    //      Method: Initial Hit
    //************************************************************

    public void initPress() {

        if(instantPressure) {

            pressureTarget = pressure = velocityAftertouchTable.get(velocity);

            smooth = 0.001;

            sendMpePressureValue();

        }else{

            pressureTarget = velocityAftertouchTable.get(velocity);

            smooth = smoothDest;

        }

        startInterpolation();
    }


    //************************************************************
    //      Method: Interpolate
    //************************************************************

    public void interpolate() {

        pressure = Calculate.interpolateValue(pressure, pressureTarget, smooth);

        if (smooth < smoothDest) smooth += 0.005;

        if (pressure == pressureTarget) stopInterpolation();

        sendMpePressureValue();

    }

    //************************************************************
    //      Method: Start Interpolation
    //************************************************************

    public void startInterpolation() {

        if (interpolationTimer.isRunning()) return;

        interpolationTimer.start();

    }

    //************************************************************
    //      Method: Stop Interpolation
    //************************************************************

    public void stopInterpolation() {

        interpolationTimer.stop();

    }

    //************************************************************
    //      Method: Send Z
    //************************************************************

    public void sendMpePressureValue() {

        for (VirtualElement e : availableTargetElements) e.onMpeZChange(this, pressure);

    }

    //************************************************************
    //      Method: Handle Z
    //************************************************************

    public void handleZ(double pVal) {

        if (useLookupTable) pressureTarget = aftertouchTable.get(pVal);
        else pressureTarget = pVal;

        startInterpolation();

        for (VirtualElement e : availableTargetElements) {
            e.handleInputRawPressure(pVal);

        }

    }

}

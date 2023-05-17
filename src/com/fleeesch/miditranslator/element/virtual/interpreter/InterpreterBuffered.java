package com.fleeesch.miditranslator.element.virtual.interpreter;

import javax.swing.*;
import java.awt.event.ActionListener;

public class InterpreterBuffered extends InterpreterDirect {

    //************************************************************
    //      Variables
    //************************************************************

    // buffer on press or release
    private final boolean bufferOff;
    private final boolean bufferOn;

    private final Timer timer;

    private double bufferValue;

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterBuffered(String pName, boolean pBufferOff, boolean pBufferOn) {

        super(pName);

        bufferOff = pBufferOff;
        bufferOn = pBufferOn;

        ActionListener task = e -> handleBufferedValue();

        timer = new Timer(50, task);
        timer.setRepeats(false);

    }

    //************************************************************
    //      Method : Handle Buffered Value
    //************************************************************

    public void handleBufferedValue() {

        super.handleSoftwareInput(bufferValue);

    }

    //************************************************************
    //      Method : Handle Software Input
    //************************************************************

    public void handleSoftwareInput(double pVal) {


        if ((pVal > 0 && !bufferOn) || (pVal <= 0 && !bufferOff)) {

            super.handleSoftwareInput(pVal);
            timer.stop();
            return;

        }

        bufferValue = pVal;
        timer.stop();
        timer.start();

    }

}

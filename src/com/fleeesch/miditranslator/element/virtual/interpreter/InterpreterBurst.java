package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.action.ActionGroup;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

import javax.swing.*;
import java.awt.event.ActionListener;

public class InterpreterBurst extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    public boolean timeout;

    private final Timer timer;

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterBurst(String pName, int pTimeout) {

        super(pName);

        ActionListener timerTask = e -> clearTimeout();

        timer = new Timer(pTimeout, timerTask);
        timer.setRepeats(false);

    }

    //************************************************************
    //      Method : Clear Timeout
    //************************************************************

    public void clearTimeout() {

        timeout = false;

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        if(timeout)return;

        timeout = true;

        timer.start();

        super.handleInput(pSource, pVal);

        if(dualAction)
            if(pVal < 0) actions.get(0).trigger(1); else actions.get(1).trigger(1);
        else{
            for (ActionGroup a : actions) a.trigger(pVal);
        }
    }


}

package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

public class InterpreterLatch extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    // indicator whether other button is pressed during hold
    public boolean interrupt = false;

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterLatch(String pName) {

        super(pName);

        isLatch = true;

    }


    //************************************************************
    //      Method : Pending Event
    //************************************************************

    public void onPendingEvent(VirtualElement pSourceElement) {

        // ignore own instance
        if (pSourceElement == this) return;

        interrupt = true; // mark that other button has been pressed

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        super.handleInput(pSource, pVal); // store value


        if (pVal > 0) {

            device.pendingListeners.add(this); // listen for other buttons

            interrupt = false; // reset interrupt flag

            actions.get(0).trigger(pVal); // always trigger press-action


        } else {

            // interrupt passed? trigger second action
            if (interrupt) {


                if (dualAction)
                    actions.get(1).trigger(1);
                else
                    actions.get(0).trigger(0);
            }

            device.pendingListeners.remove(this); // stop listening for other buttons

        }

    }


}

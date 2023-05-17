package com.fleeesch.miditranslator.element.input.jogwheel;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

public class JogWheel extends InputElement {

    //************************************************************
    //      Variables
    //************************************************************

    //************************************************************
    //      Constructor
    //************************************************************

    public JogWheel(String pName, int pJogM1, int pJogM2) {
        super();

        name = pName;

        setMidiAddress(pJogM1, pJogM2);
    }

    //************************************************************
    //      Handle Input
    //************************************************************

    @Override
    public void handleInput(double val, int[] msg) {

        // convert to binary increment signal
        val = val > 0.5 ? -0.5 : 0.5;

        super.handleInput(val, msg);

        for (VirtualElement e : availableTargetElements)
            device.notifyPendingVirtualElements(e);


    }

}

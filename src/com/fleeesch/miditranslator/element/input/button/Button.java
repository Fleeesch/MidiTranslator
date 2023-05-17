package com.fleeesch.miditranslator.element.input.button;


import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

public class Button extends InputElement {


    //************************************************************
    //      Constructor
    //************************************************************

    public Button(String pName, int m1, int m2) {

        super();

        name = pName; // transfer name

        setMidiAddress(m1, m2); // setup midi lookup address

        device.button.add(this); // add to device list

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************


    @Override
    public void handleInput(double val, int[] msg) {

        // use original input element method
        super.handleInput(val, msg);

        // button-exclusive: add and remove element from pending input list of the device
        if (val > 0) {
            for (VirtualElement ve : availableTargetElements) device.addPendingVirtualElement(ve);
        } else {
            for (VirtualElement ve : availableTargetElements) device.removePendingVirtualElement(ve);
        }
    }
}

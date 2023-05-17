package com.fleeesch.miditranslator.data.osc.listeners;

import com.fleeesch.miditranslator.data.osc.OscListener;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.illposed.osc.OSCMessageEvent;


public class VElement extends OscListener {

    //************************************************************
    //      Variables
    //************************************************************

    public final VirtualElement targetElement;
    public final Device device;

    //************************************************************
    //      Constructor
    //************************************************************

    public VElement(VirtualElement pElement) {

        targetElement = pElement; // store target element

        device = targetElement.device; // store device of element (for bypass check)

    }


    //************************************************************
    //      Methods
    //************************************************************

    @Override
    public void acceptMessage(OSCMessageEvent event) {

        // skip devices that are disabled
        if (!device.enabledState) return;

        // convert and store message as float

        float f;

        try {
            f = (float) event.getMessage().getArguments().get(0);


        } catch (Exception e) {
            return;
        }

        // convert it back to double
        targetElement.handleSoftwareInput(f);

    }
}

package com.fleeesch.miditranslator.element.input.pad;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.data.lookup.table.ValueTable;

public class Pad extends InputElement {

    //************************************************************
    //      Variables
    //************************************************************

    public boolean pressed;

    public double velocity;

    public double pressure = 0;
    public boolean hardPress = false;

    public boolean useLookupTable = false;
    public ValueTable aftertouchTable;
    public ValueTable velocityAftertouchTable;

    //************************************************************
    //      Constructor
    //************************************************************

    public Pad(String pName, int m1, int m2) {

        name = pName;

        setMidiAddress(m1, m2);

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    @Override
    public void handleInput(double val, int[] msg) {

        if (val > 0) {

            if (val > 0.75) hardPress = true;

            pressed = true;

            if (useLookupTable)
                velocity = aftertouchTable.get(val);
            else
                velocity = val;


        } else {
            pressed = false;
            hardPress = false;
        }

        super.handleInput(val, msg);

        //!! this one is a copy, maybe you should make the pad an extension of a button
        // copied from button: add and remove element from pending input list of the device
        if (val > 0) {
            for (VirtualElement ve : availableTargetElements) device.addPendingVirtualElement(ve);
        } else {
            for (VirtualElement ve : availableTargetElements) device.removePendingVirtualElement(ve);
        }

    }


}

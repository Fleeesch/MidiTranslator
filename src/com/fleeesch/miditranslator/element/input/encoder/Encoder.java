package com.fleeesch.miditranslator.element.input.encoder;


import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

public abstract class Encoder extends InputElement {



    //************************************************************
    //      Constructor
    //************************************************************

    public Encoder(String pName, int m1, int m2) {

        super();

        name = pName; // transfer name

        setMidiAddress(m1, m2); // store midi address

        device.encoder.add(this); // add to device encoder indexing list


    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************
    public void handleInput(double val, int[] msg) {

        for (VirtualElement e : availableTargetElements) {

            e.handleInput(this, val); // handle adjusted input value

            device.notifyPendingVirtualElements(e); // notify pending element listeners that input happened

        }

    }


}

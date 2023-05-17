package com.fleeesch.miditranslator.element.output.led;

import com.fleeesch.miditranslator.element.output.OutputElement;

import java.util.ArrayList;
import java.util.List;

public abstract class Led extends OutputElement {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    static final List<Led> list = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public int lastSentValue = -1;

    //************************************************************
    //      Constructor
    //************************************************************

    public Led(String pName, int m1, int m2) {

        super();

        name = pName;

        setMidiAddress(m1, m2);

        Led.list.add(this);

        device.led.add(this);

    }

    //************************************************************
    //      Method : Reset Last Sent Value
    //************************************************************

    public void resetLastSentValue() {

        lastSentValue = -1;

    }


}

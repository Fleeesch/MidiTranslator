package com.fleeesch.miditranslator.element;


import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.event.EventHandler;
import com.fleeesch.miditranslator.event.EventSource;

import java.util.ArrayList;
import java.util.List;

public abstract class Element extends EventSource implements EventHandler {

    //************************************************************
    //      Variable
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    public static Element last;
    public static final List<Element> list = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public String name;
    public final Device device;

    public int midiAddress1, midiAddress2;

    public final List<Parameter> parameters = new ArrayList<>();
    public final Parameter parameter;

    public Settings settings;

    //************************************************************
    //      Constructor
    //************************************************************

    public Element() {

        Element.list.add(this);

        device = Device.last;
        device.elements.add(this);

        Element.last = this;

        // default parameter
        parameters.add(new Parameter("Value"));
        parameter = parameters.get(0);

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Link to Settings
    //************************************************************

    public void linkToSettings(Settings pSettings) {

        settings = pSettings;
        settings.addEventHandler(this);

    }

    //************************************************************
    //      Method : Set Midi Address
    //************************************************************

    public void setMidiAddress(int m1, int m2) {

        midiAddress1 = m1;
        midiAddress2 = m2;

    }

    //************************************************************
    //      Method : Set Value
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  First Parameter

    public void setParameterValue(double val) {

        parameter.set(val);

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  One Parameter By Index

    public void setParameterValue(int idx, double val) {

        parameters.get(idx).set(val);

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Multiple Addresses

    public void setParameterValue(double... val) {

        int c = 0;

        for (double v : val) parameters.get(c++).set(v);

    }

    //************************************************************
    //      Method : Get Value
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  First Address

    public double getParameterValue() {

        return parameter.get();

    }


    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Single Address by Index

    public double getParameterValue(int pIndex) {

        return parameters.get(pIndex).get();

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Multiple Addresses (Various Indexes)

    public double[] getParameterValue(int... pIndex) {

        double[] rtn = new double[pIndex.length];
        int c = 0;

        for (int i : pIndex) rtn[c++] = parameters.get(i).get();

        return rtn;

    }


}

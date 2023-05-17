package com.fleeesch.miditranslator.element.input;

//* * * * * * * * * * * * * * * * * * * * * * * *
//  Class

import com.fleeesch.miditranslator.element.Element;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

import java.util.ArrayList;
import java.util.List;

public abstract class InputElement extends Element {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    public static InputElement last; // last created input element

    public static final List<InputElement> list = new ArrayList<>(); // list of input elements

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public final ArrayList<VirtualElement> targetElements = new ArrayList<>(); // target elements
    public final List<VirtualElement> availableTargetElements = new ArrayList<>(); // filtered target elements

    public int[] lastMidiMessage; // last received midi message

    //************************************************************
    //      Constructor
    //************************************************************

    public InputElement() {

        device.inputElements.add(this); // add element to device list

        device.elements.add(this); // add element to elements list

        InputElement.list.add(this); // add to indexing lists

        InputElement.last = this; // store as last created instance

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Public
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    //************************************************************
    //      Static Method : Filter Available Input Elements
    //************************************************************

    public void filterTargets() {

        availableTargetElements.clear();

        // go trough registered target elements
        for (VirtualElement ve : targetElements) {
            // condition is positive? add target element to list of available target elements
            if (ve.conditionCheckPositive) availableTargetElements.add(ve);

        }


    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(double val, int[] msg) {

        lastMidiMessage = msg; // store last midi message for later use

        setParameterValue(val); // store input in first parameter value

        for (VirtualElement e : availableTargetElements)
            e.handleInput(this, val); // go through filtered elements, process input

    }

    //************************************************************
    //      Method : Set Midi Address
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  2 Byte

    public void setMidiAddress(int m1, int m2) {

        super.setMidiAddress(m1,m2);

        device.AddMidiInputLookup(this, m1, m2); // add lookup

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  1 Byte

    public void setMidiAddress(int m1) {

        super.setMidiAddress(m1,0);

        device.AddMidiInputLookup(this, m1, 0); // add lookup

    }


}

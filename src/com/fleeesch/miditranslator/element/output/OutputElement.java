package com.fleeesch.miditranslator.element.output;

import com.fleeesch.miditranslator.element.Element;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

import java.util.ArrayList;
import java.util.List;

public abstract class OutputElement extends Element {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    public static OutputElement last;

    public static final List<OutputElement> list = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public final ArrayList<VirtualElement> sourceElements = new ArrayList<>();
    public VirtualElement sourceFocus = null;

    public boolean visible = true;

    //************************************************************
    //      Constructor
    //************************************************************

    public OutputElement() {

        super();

        device.outputElements.add(this);

        OutputElement.last = this;

        OutputElement.list.add(this);

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Set Midi Address
    //************************************************************
    public void setMidiAddress(int m1, int m2) {

        super.setMidiAddress(m1, m2);

        device.AddMidiOutputLookup(this, m1, m2);

    }

    //************************************************************
    //      Method : filter Source Elements
    //************************************************************

    public void filterSources() {

        for (VirtualElement ve : sourceElements) {
            if (ve.conditionCheckPositive) {
                sourceFocus = ve;
                return;
            }
        }

        sourceFocus = null;

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Visibility
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Enable Visibility
    //************************************************************

    public void enableVisibility() {

        visible = true;
        update();

    }

    //************************************************************
    //      Method : Disable Visibility
    //************************************************************

    public void disableVisibility() {

        visible = false;
        update();

    }

    //************************************************************
    //      Method : Toggle Visibility
    //************************************************************

    public void toggleVisibility() {

        if (visible) disableVisibility();
        else enableVisibility();

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Placeholder
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void update() {

    }

    public void resetLastSentValue() {

    }


}

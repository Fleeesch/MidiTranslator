package com.fleeesch.miditranslator.data.parameter;

import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.event.EventHandler;
import com.fleeesch.miditranslator.event.EventSource;

import java.util.ArrayList;
import java.util.List;

public class Parameter extends EventSource {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    static Parameter lastParameter; // last created parameter
    static final List<Parameter> list = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public String name; // name (for tracking / debugging)

    public double value = 0; // double value, init at 0

    public final Device device; // original device

    public boolean storeInConfig = false; // store in config flag


    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  No Name

    public Parameter() {

        Parameter.lastParameter = this; // set last shift as this one

        device = Device.last; // use last device

        if (device != null) device.parameters.add(this);

        list.add(this);

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  name Given

    public Parameter(String pName) {

        name = pName; // transfer name

        Parameter.lastParameter = this; // set last shift as this one

        device = Device.last; // use last device

        if (device != null) device.parameters.add(this);

        list.add(this);

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Static
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Update all of Device
    //************************************************************

    public static void updateAllOfDevice(Device pDevice) {

        for (Parameter p : pDevice.parameters) p.set();

    }

    //************************************************************
    //      Method : Update all Parameters
    //************************************************************

    public static void updateAll() {

        for (Parameter p : Parameter.list) p.set();

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Public
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Update Conditions
    //************************************************************

    private void updateLinkedConditions() {

        // init lists storing in/out elements
        List<InputElement> inputList = new ArrayList<>();
        List<OutputElement> outputList = new ArrayList<>();

        // go through parameter conditions containing this shift

        for (Condition c : conditionEvents) {

            c.check(this);

            for (VirtualElement e : c.virtualElementHandlers) {

                // collect linked elements
                for (InputElement se : e.sourceElements) if (!inputList.contains(se)) inputList.add(se);
                for (OutputElement te : e.targetElements) if (!outputList.contains(te)) outputList.add(te);

            }


        }

        // filter input elements
        for (InputElement e : inputList) e.filterTargets();

        // filter output elements
        for (OutputElement e : outputList) e.filterSources();

        device.killPendingVirtualElement();

    }

    //************************************************************
    //      Method : Update Settings
    //************************************************************

    public void updateEventHandlers() {

        for (EventHandler eventHandler : eventHandlers) eventHandler.onParameterChange(this);

    }

    //************************************************************
    //      Method : Set Value
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Given Value

    public void set(double val) {

        value = val; // change value

        // used in a setting? inform settings
        updateEventHandlers();

        // used in a condition? update condition
        if (usedInCondition) updateLinkedConditions();

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Refresh

    public void set() {
        set(value);
    }


    //************************************************************
    //      Method : Get Value
    //************************************************************

    public double get() {
        return value;
    }

    //************************************************************
    //      Method : Store in Config
    //************************************************************

    public void storeInConfig(boolean pState) {
        storeInConfig = pState;
    }


}

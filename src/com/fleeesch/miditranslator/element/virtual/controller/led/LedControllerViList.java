package com.fleeesch.miditranslator.element.virtual.controller.led;

import com.fleeesch.miditranslator.action.ActionGroup;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

import java.util.List;

public class LedControllerViList extends LedController {

    //************************************************************
    //      Variables
    //************************************************************

    public final List<VirtualElement> virtualElements;


    //************************************************************
    //      Constructor
    //************************************************************

    public LedControllerViList(String pName, List<VirtualElement> pList) {

        super(pName);

        virtualElements = pList;

        for (VirtualElement e : virtualElements) e.parameter.addEventHandler(this);

        parameterControlsLed = true;

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        // original method -> store value
        //super.handleInput(pSource, pVal);

        // trigger all the action groups one by one
        if (!hasMotorFader()) for (ActionGroup a : actions) a.trigger(pVal);
        else actions.get(0).trigger(pVal);


    }

    //************************************************************
    //      Method : onParameterChange
    //************************************************************

    @Override
    public void onParameterChange(Parameter pParameter) {
        //super.onParameterChange(pParameter);

        for (VirtualElement e : virtualElements) {
            if (e.parameter.get() > 0) {
                setParameterValue(1);
                for (OutputElement oe : targetElements) oe.update();
                return;
            }
        }

        setParameterValue(0);
        for (OutputElement oe : targetElements) oe.update();

    }
}

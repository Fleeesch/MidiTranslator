package com.fleeesch.miditranslator.element.virtual.controller.led;

import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;

public class LedControllerParameter extends LedController {

    //************************************************************
    //      Variables
    //************************************************************

    public final Parameter linkedParameter;
    public final double linkedParameterValue;


    //************************************************************
    //      Constructor
    //************************************************************

    public LedControllerParameter(String pName, Parameter pParameter, double pVal) {

        super(pName);

        linkedParameter = pParameter;

        linkedParameter.addEventHandler(this);

        linkedParameterValue = pVal;


    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

    }

    //************************************************************
    //      Method : onParameterChange
    //************************************************************

    @Override
    public void onParameterChange(Parameter pParameter) {
        super.onParameterChange(pParameter);

        if (pParameter.get() == linkedParameterValue) setParameterValue(4, 1);
        else setParameterValue(4, 0.1);

        for (OutputElement e : targetElements) e.update();

    }
}

package com.fleeesch.miditranslator.action.parameter;

import com.fleeesch.miditranslator.data.parameter.Parameter;

public class ParameterIncrement extends ParameterAction {

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Single Value

    public ParameterIncrement(Parameter pActionParameter, double pVal) {

        super(pActionParameter, pVal);

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Multiple Values

    public ParameterIncrement(Parameter pActionParameter, double... pVal) {

        super(pActionParameter, pVal);

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Public
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // don't do anything on button release
        if (pVal <= 0) return;

        // set parameter based on stored target value
        parameter.set(parameter.get() + setValues.get(0));


    }


}

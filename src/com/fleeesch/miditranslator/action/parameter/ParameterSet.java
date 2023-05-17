package com.fleeesch.miditranslator.action.parameter;

import com.fleeesch.miditranslator.data.parameter.Parameter;

public class ParameterSet extends ParameterAction {

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Single Value

    public ParameterSet(Parameter pActionParameter, double pVal) {

        super(pActionParameter, pVal);

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Multiple Values

    public ParameterSet(Parameter pActionParameter, double... pVal) {

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
        parameter.set(setValues.get(0));


    }


}

package com.fleeesch.miditranslator.action.parameter;

import com.fleeesch.miditranslator.data.parameter.Parameter;

public class ParameterIncrement extends ParameterAction {

    //************************************************************
    //      Variables
    //************************************************************


    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Parameter Reference

    public ParameterIncrement(Parameter pActionParameter, double pVal, Parameter pParameterLink) {

        super(pActionParameter, pVal, pParameterLink);


    }

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

        storeParameterValue();

        // don't do anything on button release
        if (pVal <= 0) return;

        // use parameter value if any is given
        if (hasParameterLink) {

            // use stored value as a direction factor
            parameter.set(parameter.get() + parameterLink.get() * setValues.get(0));

            // skip the rest
            return;

        }

        // set parameter based on stored target value
        parameter.set(parameter.get() + setValues.get(0));


    }


}

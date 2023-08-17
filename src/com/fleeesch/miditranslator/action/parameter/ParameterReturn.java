package com.fleeesch.miditranslator.action.parameter;

import com.fleeesch.miditranslator.data.parameter.Parameter;

public class ParameterReturn extends ParameterAction {

    //************************************************************
    //      Variables
    //************************************************************

    ParameterAction action; // reference Action

    //************************************************************
    //      Constructor
    //************************************************************


    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Parameter Action Reference

    public ParameterReturn(ParameterAction pActionParameter){

        super(pActionParameter.parameter);

        action = pActionParameter;

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Public
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // restore last parameter value of linked action
        action.restoreParameterValue();


    }


}

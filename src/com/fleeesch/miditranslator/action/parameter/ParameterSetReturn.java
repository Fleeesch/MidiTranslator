package com.fleeesch.miditranslator.action.parameter;

import com.fleeesch.miditranslator.data.parameter.Parameter;


public class ParameterSetReturn extends ParameterAction {

    //************************************************************
    //      Variables
    //************************************************************

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Single Value

    public ParameterSetReturn(Parameter pActionParameter, double pVal) {

        super(pActionParameter, pVal);


    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Multi Value

    public ParameterSetReturn(Parameter pActionParameter, double... pVal) {

        super(pActionParameter, pVal);

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Public
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // check if more than one target value has been declared
        if (setValues.size() > 1) {

            // if so, use both on a set and return
            if (pVal > 0)
                parameter.set(setValues.get(1));
            else
                parameter.set(setValues.get(0));

        } else {

            // use target value on press, 0 on release
            if (pVal > 0)
                parameter.set(setValues.get(0));
            else
                parameter.set(0);

        }

    }


}

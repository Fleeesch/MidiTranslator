package com.fleeesch.miditranslator.action.parameter;

import com.fleeesch.miditranslator.data.parameter.Parameter;


public class ParameterToggle extends ParameterAction {

    //************************************************************
    //      Variables
    //************************************************************

    public double lastValue;
    private boolean rememberState;

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Single Value

    public ParameterToggle(Parameter pActionParameter, double pVal) {

        super(pActionParameter, pVal);

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Multi Value

    public ParameterToggle(Parameter pActionParameter, double... pVal) {

        super(pActionParameter, pVal);

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Public
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Remember State
    //************************************************************

    public void rememberState(){

        rememberState = true;
        lastValue = setValues.get(setValues.size()-1);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // don't do anything on release
        if (pVal <= 0) return;

        if(rememberState){

            boolean hasValue = false;

            double currentValue = parameter.get();

            for(double v : setValues){
                if(v == currentValue){
                    hasValue = true;
                    break;
                }
            }

            if(!hasValue){
                parameter.set(lastValue);
                return;
            }


        }

        // check if multiple target values have been declared
        if (setValues.size() > 1) {

            //toggle between first two target values
            if (parameter.get() != setValues.get(1))
                parameter.set(lastValue = setValues.get(1));
            else
                parameter.set(lastValue = setValues.get(0));


        } else {

            // toggle between first target value and 0
            if (parameter.get() != setValues.get(0))
                parameter.set(lastValue = setValues.get(0));
             else
                parameter.set(lastValue = 0);



        }


    }


}

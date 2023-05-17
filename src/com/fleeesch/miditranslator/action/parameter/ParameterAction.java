package com.fleeesch.miditranslator.action.parameter;

import com.fleeesch.miditranslator.action.Action;
import com.fleeesch.miditranslator.data.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;

public abstract class ParameterAction extends Action {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public final Parameter parameter; // parameter to be manipulated

    // multiple values can be used for auto-generated combinations
    final List<Double> setValues = new ArrayList<>();

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Single Value

    public ParameterAction(Parameter pActionParameter, double pValDest) {

        parameter = pActionParameter; // store value

        setValues.add(pValDest); // add value

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Multiple Values

    public ParameterAction(Parameter pActionParameter, double... pValDest) {

        parameter = pActionParameter; // add value

        for (double d : pValDest) setValues.add(d); // transfer values

    }



}

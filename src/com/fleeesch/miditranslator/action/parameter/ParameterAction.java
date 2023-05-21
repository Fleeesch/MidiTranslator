package com.fleeesch.miditranslator.action.parameter;

import com.fleeesch.miditranslator.action.Action;
import com.fleeesch.miditranslator.data.parameter.Parameter;

import java.util.ArrayList;
import java.util.List;

public abstract class ParameterAction extends Action {

    //************************************************************
    //      Variables
    //************************************************************

    public final Parameter parameter; // parameter to be manipulated

    protected Parameter parameterLink;

    protected final boolean hasParameterLink;

    // multiple values can be used for auto-generated combinations
    final List<Double> setValues = new ArrayList<>();

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Parameter Link

    public ParameterAction(Parameter pActionParameter, double pValDest, Parameter pLinkedParameter) {

        parameter = pActionParameter; // store value

        setValues.add(pValDest); // add value

        parameterLink = pLinkedParameter;

        hasParameterLink = true;

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Single Value

    public ParameterAction(Parameter pActionParameter, double pValDest) {

        parameter = pActionParameter; // store value

        setValues.add(pValDest); // add value

        hasParameterLink = false;

    }


    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Multiple Values

    public ParameterAction(Parameter pActionParameter, double... pValDest) {

        parameter = pActionParameter; // add value

        for (double d : pValDest) setValues.add(d); // transfer values

        hasParameterLink = false;

    }



}

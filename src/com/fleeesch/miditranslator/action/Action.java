package com.fleeesch.miditranslator.action;

import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.event.EventSource;

import java.util.ArrayList;
import java.util.List;


public abstract class Action extends EventSource implements ActionInterface{

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    public static Action last; // last created action
    public static final List<Action> list = new ArrayList<>(); // total list of actions

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public VirtualElement virtualElement; // virtual element that the action belongs to

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Last Virtual Element

    public Action() {

        list.add(this); // add this one to indexing list

        last = this; // mark this one as last action

    }


    //************************************************************
    //      Placeholder Methods
    //************************************************************
    public void trigger(double pVal) {

    }

}

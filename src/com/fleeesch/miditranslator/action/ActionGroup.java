package com.fleeesch.miditranslator.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionGroup implements ActionInterface {

    //************************************************************
    //      Variables
    //************************************************************

    public final List<Action> actions = new ArrayList<>(); // members of action group

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Empty

    public ActionGroup() {

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Single Action

    public ActionGroup(Action a) {
        addAction(a);
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Multiple Actions

    public ActionGroup(Action... a) {
        actions.addAll(Arrays.asList(a));
    }

    //************************************************************
    //      Method : Add Action
    //************************************************************

    public void addAction(Action a) {
        actions.add(a);
    }

    //************************************************************
    //      Method : Process Actions
    //************************************************************

    public void trigger(double pVal) {
        for (Action a : actions) a.trigger(pVal);
    }

}

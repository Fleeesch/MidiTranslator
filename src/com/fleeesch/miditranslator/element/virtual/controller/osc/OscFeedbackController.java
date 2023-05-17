package com.fleeesch.miditranslator.element.virtual.controller.osc;

import com.fleeesch.miditranslator.action.ActionGroup;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

public class OscFeedbackController extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    public final boolean ignoreOff;

    //************************************************************
    //      Method : Constructor
    //************************************************************

    public OscFeedbackController(String pName, boolean pIgnoreOff){

        super(pName);

        ignoreOff = pIgnoreOff;

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    @Override
    public void handleSoftwareInput(double pVal) {
        super.handleSoftwareInput(pVal);

        if(ignoreOff && pVal <= 0)return;

        for(ActionGroup a : actions)a.trigger(1);


    }
}

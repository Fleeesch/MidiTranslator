package com.fleeesch.miditranslator.element.virtual.listener;

import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.event.EventHandler;

public class ListenerMidiLSB extends VirtualElement {

    //************************************************************
    //      Method : Constructor
    //************************************************************

    public ListenerMidiLSB(VirtualElement pLsbHandler, int pMidiAddress) {

        super("LSB Handler");

        VirtualElement.last = pLsbHandler;

        linkToFeedback(pMidiAddress);

        this.addEventHandler(pLsbHandler);

    }

    //************************************************************
    //      Method : Handle Software Input
    //************************************************************

    @Override
    public void handleSoftwareInput(double pVal) {

        int v = (int) (pVal * 127);

        for (EventHandler e : eventHandlers) e.onLsbInput(v);


    }
}

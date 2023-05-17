package com.fleeesch.miditranslator.data.osc.listeners;

import com.fleeesch.miditranslator.data.external.DataSet;
import com.fleeesch.miditranslator.data.osc.OscListener;
import com.illposed.osc.OSCMessageEvent;


public class DataCollector extends OscListener {

    //************************************************************
    //      Variables
    //************************************************************

    public final DataSet targetElement;

    //************************************************************
    //      Constructor
    //************************************************************

    public DataCollector(DataSet pElement) {

        targetElement = pElement;

    }


    //************************************************************
    //      Methods
    //************************************************************

    @Override
    public void acceptMessage(OSCMessageEvent event) {

    }
}

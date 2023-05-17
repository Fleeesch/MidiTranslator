package com.fleeesch.miditranslator.action.osc;

import com.fleeesch.miditranslator.Main;

public class SendOscAbsolute extends SendOscAction {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    //************************************************************
    //      Constructor
    //************************************************************

    public SendOscAbsolute(int pPortIdx, String pAdr) {

        super(pPortIdx, pAdr);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // don't send same value twice
        if (pVal == lastSentValue) return;

        // send message
        Main.deviceDaw.osc.get(portIdx).sendMessage(address, pVal);

        // store value
        lastSentValue = pVal;

    }

}

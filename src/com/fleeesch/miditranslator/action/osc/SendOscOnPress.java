package com.fleeesch.miditranslator.action.osc;

import com.fleeesch.miditranslator.Main;

public class SendOscOnPress extends SendOscAction {

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

    public SendOscOnPress(int pPortIdx, String pAdr) {

        super(pPortIdx, pAdr);

    }

    public SendOscOnPress(int pPortIdx, String pAdr, double pVal) {

        super(pPortIdx, pAdr, pVal);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // quit if button is released
        if (pVal <= 0) return;

        // send message
        if (!fixedSendValue) Main.deviceDaw.osc.get(portIdx).sendMessage(address, pVal);
        else Main.deviceDaw.osc.get(portIdx).sendMessage(address, sendValue);

    }

}

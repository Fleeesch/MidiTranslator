package com.fleeesch.miditranslator.action.osc;

import com.fleeesch.miditranslator.Main;

public class SendOscOnRelease extends SendOscAction {

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

    public SendOscOnRelease(int pPortIdx, String pAdr) {

        super(pPortIdx, pAdr);

    }

    public SendOscOnRelease(int pPortIdx, String pAdr, double pVal) {

        super(pPortIdx, pAdr, pVal);

    }
    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // quit if button is pressed
        if (pVal > 0) return;

        // send message
        if (!fixedSendValue) Main.deviceDaw.osc.get(portIdx).sendMessage(address, pVal);
        else Main.deviceDaw.osc.get(portIdx).sendMessage(address, sendValue);

    }

}

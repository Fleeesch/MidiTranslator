package com.fleeesch.miditranslator.action.osc;

import com.fleeesch.miditranslator.Main;

public class SendOscDirect extends SendOscAction {

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

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Port + Address

    public SendOscDirect(int pPortIdx, String pAdr) {

        super(pPortIdx, pAdr);

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Port + Address + Send value
    public SendOscDirect(int pPortIdx, String pAdr, double pSendValue) {

        super(pPortIdx, pAdr, pSendValue);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        // send message
        if (fixedSendValue)
            Main.deviceDaw.osc.get(portIdx).sendMessage(address, sendValue);
        else
            Main.deviceDaw.osc.get(portIdx).sendMessage(address, pVal);

    }

}

package com.fleeesch.miditranslator.action.osc;

import com.fleeesch.miditranslator.action.Action;


//* * * * * * * * * * * * * * * * * * * * * * * *
//  Class

public abstract class SendOscAction extends Action {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    public int portIdx = 0; // port index to use

    public String address; // message address

    public double sendValue;
    public boolean fixedSendValue = false;

    public double lastSentValue = -1; // last sent value


    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Empty

    public SendOscAction() {

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Port Index + Address

    public SendOscAction(int pPortIdx, String pAdr) {

        portIdx = pPortIdx; // store port index

        address = pAdr; // store address

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Port Index + Address + Send Value

    public SendOscAction(int pPortIdx, String pAdr, double pSendValue) {

        portIdx = pPortIdx; // store port index

        address = pAdr; // store address

        sendValue = pSendValue;

        fixedSendValue = true;

    }

}

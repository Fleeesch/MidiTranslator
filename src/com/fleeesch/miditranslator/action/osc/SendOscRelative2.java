package com.fleeesch.miditranslator.action.osc;

import com.fleeesch.miditranslator.Main;

public class SendOscRelative2 extends SendOscRelative {


    //************************************************************
    //      Constructor
    //************************************************************

    public SendOscRelative2(int pPortIdx, String pAdr) {

        super(pPortIdx, pAdr);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        if(doRescale)pVal = scaleValue(pVal);

        // send value with 0.5 offset
        Main.deviceDaw.osc.get(portIdx).sendMessage(address, pVal + 0.5);


    }

}

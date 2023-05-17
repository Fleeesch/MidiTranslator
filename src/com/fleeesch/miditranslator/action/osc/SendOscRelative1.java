package com.fleeesch.miditranslator.action.osc;

import com.fleeesch.miditranslator.Main;

public class
SendOscRelative1 extends SendOscRelative {


    //************************************************************
    //      Constructor
    //************************************************************

    public SendOscRelative1(int pPortIdx, String pAdr) {

        super(pPortIdx, pAdr);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        if(doRescale)pVal = scaleValue(pVal);

        // get amount of ticks to send
        int count = Math.abs((int) (pVal * 127));

        // send multiple single osc messages depending on direction
        if (pVal > 0)
            for (int i = 0; i < count; i++) Main.deviceDaw.osc.get(portIdx).sendMessage(address, 1);
        else
            for (int i = 0; i < count; i++) Main.deviceDaw.osc.get(portIdx).sendMessage(address, 0);


    }

}

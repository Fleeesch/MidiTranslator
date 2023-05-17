package com.fleeesch.miditranslator.action.osc;

import com.fleeesch.miditranslator.Main;

public class SendOscRelative3 extends SendOscRelative {

    //************************************************************
    //      Variables
    //************************************************************

    final String address1;
    final String address2;

    final double value1;
    final double value2;

    //************************************************************
    //      Constructor
    //************************************************************

    public SendOscRelative3(int pPortIdx, String pAdr1, double pVal1, String pAdr2, double pVal2) {

        super(pPortIdx, pAdr1);

        portIdx = pPortIdx;

        address1 = pAdr1;
        address2 = pAdr2;

        value1 = pVal1;
        value2 = pVal2;

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
            for (int i = 0; i < count; i++) Main.deviceDaw.osc.get(portIdx).sendMessage(address1, value1);
        else
            for (int i = 0; i < count; i++) Main.deviceDaw.osc.get(portIdx).sendMessage(address2, value2);


    }

}

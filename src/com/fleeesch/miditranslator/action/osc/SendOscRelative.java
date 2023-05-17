package com.fleeesch.miditranslator.action.osc;

import com.fleeesch.miditranslator.Main;

public class SendOscRelative extends SendOscAction {

    //************************************************************
    //      Variables
    //************************************************************

    public boolean doRescale;
    public double rescaleValue;
    public double minimalValue;


    //************************************************************
    //      Constructor
    //************************************************************

    public SendOscRelative(int pPortIdx, String pAdr) {

        super(pPortIdx, pAdr);

    }

    //************************************************************
    //      Method : Trigger
    //************************************************************

    public void trigger(double pVal) {

        if (doRescale) pVal = scaleValue(pVal);

        Main.deviceDaw.osc.get(portIdx).sendMessage(address, pVal > 0 ? 1 : 0);

    }


    //************************************************************
    //      Method : Rescale
    //************************************************************

    public void rescale(double pVal, double pMinVal) {

        if (pVal == 1) return;

        doRescale = true;
        rescaleValue = pVal;

        minimalValue = pMinVal;

    }

    //************************************************************
    //      Method : Scale Value
    //************************************************************

    public double scaleValue(double pVal) {

        double rtn = pVal * rescaleValue;

        if (Math.abs(rtn) < minimalValue) {
            if (rtn > 0) return minimalValue;
            else return -minimalValue;
        }

        return rtn;

    }


}

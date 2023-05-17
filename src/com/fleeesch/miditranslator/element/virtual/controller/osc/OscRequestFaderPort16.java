package com.fleeesch.miditranslator.element.virtual.controller.osc;

import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;

public class OscRequestFaderPort16 extends VirtualElement {

    //************************************************************
    //      Variables
    //************************************************************

    //************************************************************
    //      Method : Constructor
    //************************************************************

    public OscRequestFaderPort16(String pName) {

        super(pName);


    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    @Override
    public void handleSoftwareInput(double pVal) {
        super.handleSoftwareInput(pVal);


        for (Device d : Device.list) {
            if (d.isFaderPort16()) {
                Osc.DawArrange.sendMessage(OscAddress.infoIsFP16, 1);
                return;

            }
        }


        Osc.DawArrange.sendMessage(OscAddress.infoIsFP16, 0);


    }
}

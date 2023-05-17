package com.fleeesch.miditranslator.element.virtual.mpe.channel;

import com.fleeesch.miditranslator.element.virtual.mpe.MpeSurface;

public class MpeSurfaceChannelSingle extends MpeSurfaceChannel {


    //************************************************************
    //      Constructor
    //************************************************************

    public MpeSurfaceChannelSingle(String pName, MpeSurface pMatrix, int pChannel) {

        super(pName, pMatrix, pChannel); // use original constructor

    }

    //************************************************************
    //      Method : Process
    //************************************************************

    public void handleDataInput() {

        // mpe is activated? don't do anything
        if (!mpeSurface.useMpe) return;

        // send pressure value
        sendMidiPressure();

    }


}

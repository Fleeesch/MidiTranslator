package com.fleeesch.miditranslator.element.virtual.mpe.channel;

import com.fleeesch.miditranslator.element.virtual.mpe.MpeSurface;

public class MpeSurfaceChannelGlobal extends MpeSurfaceChannel {

    //************************************************************
    //      Constructor
    //************************************************************

    public MpeSurfaceChannelGlobal(String pName, MpeSurface pMatrix, int pChannel) {

        super(pName, pMatrix, pChannel); // use original constructor

    }

    //************************************************************
    //      Method : Process
    //************************************************************

    public void handleDataInput() {

        // surface uses mpe?
        if (mpeSurface.useMpe) {

            // transfer mod to all channels
            for (MpeSurfaceChannel c : mpeSurface.channels) {
                c.mpePitch = mpePitch;
                c.mpeY = mpeY;

                if(!c.active) continue;
                
                c.sendMidiPitch();
                c.sendMidiTimbre();
            }


        } else {

            // no mpe used?

            mpeZ = mpeSurface.getAverageZ(); // get average pressure value

            // send midi data using global channel
            sendMidiPressure();
            sendMidiPitch();
            sendMidiTimbre();

        }

    }

}

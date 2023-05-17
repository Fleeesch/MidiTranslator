package com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.display;

import com.fleeesch.miditranslator.element.virtual.mpe.MpeDataHandler;

public class MpeSettingsRGB extends MpeSettings {

    //************************************************************
    //      Constructor
    //************************************************************

    public MpeSettingsRGB(String pName, MpeDataHandler pDataHandler) {

        super(pName, pDataHandler); // original constructor

        pDataHandler.setColor(0, 0, 0);

    }


    //************************************************************
    //      Method : Update
    //************************************************************

    public void update() {

        boolean sendPressure = mpeSurface.sendPressure;
        boolean useMpe = mpeSurface.useMpe;
        boolean velocityLock = mpeSurface.velocityLock;

        double r = 0, g = 0, b = 0;

        if (sendPressure) {
            r += 0;
            g += 0.5;
            b += 0.5;
        }

        if (useMpe) {
            r += 0.5;
            g += 0.5;
            b += 0.5;
        }

        if (velocityLock) {
            r *= 0.5;
            g *= 0.5;
            b *= 0.5;

            r += 0.1;
            b += 0.5;

        }

        r = Math.min(Math.max(r, 0), 1);
        g = Math.min(Math.max(g, 0), 1);
        b = Math.min(Math.max(b, 0), 1);

        mpeDataHandler.setColor(r, g, b);
    }


}
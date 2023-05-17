package com.fleeesch.miditranslator.element.virtual.mpe.mpeelement;

import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.element.virtual.mpe.MpeDataHandler;
import com.fleeesch.miditranslator.element.virtual.mpe.MpeSurface;

public class MpeElement implements MpeElementInterface{

    //************************************************************
    //      Variables
    //************************************************************

    // original surface
    public final MpeSurface mpeSurface;

    // data handler
    public final MpeDataHandler mpeDataHandler;

    public final String name;


    //************************************************************
    //      Constructor
    //************************************************************

    public MpeElement(String pName, MpeDataHandler pDataHandler){

        name = pName;

        mpeDataHandler = pDataHandler;

        mpeDataHandler.mpeElement = this;

        mpeSurface = mpeDataHandler.mpeSurface;

    }

    //************************************************************
    //      Event : Handle Trigger
    //************************************************************

    @Override
    public void handleTrigger(double pVal) {

    }

    //************************************************************
    //      Event : Handle Mpe Z
    //************************************************************

    @Override
    public void handleMpeZ(double pVal) {

    }

    //************************************************************
    //      Event : Handle Pressure
    //************************************************************

    @Override
    public void handlePressure(double pVal) {

    }

    //************************************************************
    //      Event : Handle Settings Change
    //************************************************************

    @Override
    public void handleSettingsChange(Settings pSettings) {

    }
}

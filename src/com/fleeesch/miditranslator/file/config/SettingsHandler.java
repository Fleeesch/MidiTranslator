package com.fleeesch.miditranslator.file.config;

import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.event.EventSource;

import java.util.ArrayList;
import java.util.List;

public class SettingsHandler extends EventSource {

    //************************************************************
    //      Variables
    //************************************************************

    public final Settings settings; // settings instance

    public final List<Parameter> parameters = new ArrayList<>(); // writeable parameters

    public final List<Parameter> parametersToWrite = new ArrayList<>(); // parameter write buffer

    //************************************************************
    //      Constructor
    //************************************************************

    public SettingsHandler(Settings pSettings) {

        settings = pSettings; // store settings instance

        settings.addEventHandler(this); // listen to settings changes

        // add config-writeable parameters of settings instance
        for (Parameter p : settings.parameterList) if (p.storeInConfig) parameters.add(p);


    }


    //************************************************************
    //      Method : Clear Write Buffer
    //************************************************************

    public void clearWriteBuffer() {
        parametersToWrite.clear(); // just clear parameters to write
    }

    //************************************************************
    //      Event : On Settings Change
    //************************************************************

    @Override
    public void onSettingsChange(Settings pSettings, Parameter pParameter) {

        if (!pParameter.storeInConfig) return; // parameter can't be stored ? ignore

        if (parametersToWrite.contains(pParameter)) return; // parameter already in buffer ? ignore

        parametersToWrite.add(pParameter); // add parameter to write request

        Configuration.storeSettingsChange(this); // request storing settings

    }

}

package com.fleeesch.miditranslator.data.parameter.settings;

import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.event.EventHandler;
import com.fleeesch.miditranslator.event.EventSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Settings extends EventSource {

    //************************************************************
    //      Variables
    //************************************************************

    public final String name; // name of settings

    public final Device device; // device where the settings are created

    // list of associated parameters
    public final List<Parameter> parameterList = new ArrayList<>();
    public final HashMap<String, Parameter> parameterHashMap = new HashMap<>();


    //************************************************************
    //      Constructor
    //************************************************************

    public Settings(String pName) {

        name = pName;

        device = Device.last;

    }

    //************************************************************
    //      Method : Add Parameter
    //************************************************************

    public void addParameter(Parameter pParameter) {

        // ignore Parameters without names
        if (pParameter.name.length() <= 0) return;

        // add parameter to lists
        parameterList.add(pParameter);
        parameterHashMap.put(pParameter.name, pParameter);

        // listen to parameter changes
        pParameter.addEventHandler(this);

    }

    //************************************************************
    //      Method : Get Parameter By Name
    //************************************************************

    public Parameter getParameterByName(String pName) {

        return parameterHashMap.get(pName); // use hashmap

    }

    //************************************************************
    //      Method : Get Parameter By Index
    //************************************************************

    public Parameter getParameterByIndex(int pIndex) {

        return parameterList.get(pIndex); // use index

    }

    //************************************************************
    //      Event : On Parameter Change
    //************************************************************

    public void onParameterChange(Parameter pParameter) {

        for (EventHandler ve : eventHandlers) ve.onSettingsChange(this, pParameter);

    }

}

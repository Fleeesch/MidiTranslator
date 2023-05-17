package com.fleeesch.miditranslator.event;


import com.fleeesch.miditranslator.data.external.DataSet;
import com.fleeesch.miditranslator.data.external.track.TrackData;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

import java.util.ArrayList;
import java.util.List;

public abstract class EventSource implements EventHandler {

    //************************************************************
    //      Variables
    //************************************************************

    // type independent ist of related event handlers and sources
    public final List<EventHandler> eventHandlers = new ArrayList<>();
    public final List<EventSource> eventSources = new ArrayList<>();

    // type specific handlers
    public final List<Condition> conditionHandlers = new ArrayList<>();
    public final List<Settings> settingsHandlers = new ArrayList<>();
    public final List<VirtualElement> virtualElementHandlers = new ArrayList<>();

    // type specific sources
    public final List<Condition> conditionEvents = new ArrayList<>();
    public final List<Settings> settingsEvents = new ArrayList<>();
    public final List<VirtualElement> virtualElementEvents = new ArrayList<>();

    // simple flags so the source knows it is used as an event handler by those types
    public boolean usedInCondition = false;
    public boolean usedInVirtualElement = false;
    public boolean usedInSettings = false;



    //************************************************************
    //      Method : Add Event Handler (Universal)
    //************************************************************

    public void addEventHandler(EventHandler e) {

        // inform the handler that this one uses a specific type if source
        if (this instanceof Condition) e.usesCondition((Condition) this);
        if (this instanceof Settings) e.usesSettings((Settings) this);

        // make sure this one here has all its type-specific relations registered

        if (e instanceof Condition) {
            conditionHandlers.add((Condition) e);
            usesCondition((Condition) e);
        }
        if (e instanceof Settings) {
            settingsHandlers.add((Settings) e);
            usesSettings((Settings) e);
        }
        if (e instanceof VirtualElement) {
            virtualElementHandlers.add((VirtualElement) e);
            usesVirtualElement((VirtualElement) e);
        }

        // add handler to global list
        eventHandlers.add(e);
        e.addEventSource(this);

    }


    //************************************************************
    //      Method : Add Event Source
    //************************************************************

    public void addEventSource(EventSource e) {

        eventSources.add(e); // just add source

    }

    //************************************************************
    //      Method : Mark Uses
    //************************************************************


    public void usesCondition(Condition c) {

        conditionEvents.add(c);
        usedInCondition = true;

    }

    public void usesSettings(Settings s) {

        settingsEvents.add(s);
        usedInSettings = true;

    }

    public void usesVirtualElement(VirtualElement v) {

        virtualElementEvents.add(v);
        usedInVirtualElement = true;

    }

    //************************************************************
    //      Method : Placeholders
    //************************************************************

    public void onSettingsChange(Settings pSettings, Parameter pParameter) {
    }

    public void onPendingEvent(VirtualElement pSourceElement) {
    }


    public void onConditionChange(Condition pCondition, boolean pState) {
    }


    public void onParameterChange(Parameter pParameter) {
    }

    public void onMpeZChange(InputElement pInputElement, double pVal) {

    }

    public void onDataChange(DataSet pSource) {

    }

    public void onPeakChange(DataSet pSource) {

    }

    public void onLsbInput(int pVal) {

    }

    public void onTrackDataChange(TrackData pSource){

    }

}

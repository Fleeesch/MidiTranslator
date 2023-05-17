package com.fleeesch.miditranslator.event;

import com.fleeesch.miditranslator.data.external.DataSet;
import com.fleeesch.miditranslator.data.external.track.TrackData;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

public interface EventHandler extends MpeEventHandler {

    void addEventSource(EventSource e);

    void usesCondition(Condition c);


    void usesSettings(Settings s);

    void usesVirtualElement(VirtualElement v);

    void onSettingsChange(Settings pSettings, Parameter pParameter);

    void onPendingEvent(VirtualElement pSourceElement);

    void onConditionChange(Condition pCondition, boolean pState);

    void onParameterChange(Parameter pParameter);

    void onDataChange(DataSet pSource);

    void onTrackDataChange(TrackData pSource);

    void onPeakChange(DataSet pSource);
    void onLsbInput(int pVal);


}

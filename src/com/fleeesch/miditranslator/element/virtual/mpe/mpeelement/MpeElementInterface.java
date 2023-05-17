package com.fleeesch.miditranslator.element.virtual.mpe.mpeelement;

import com.fleeesch.miditranslator.data.parameter.settings.Settings;

public interface MpeElementInterface {

    void handleTrigger(double pVal);
    void handleMpeZ(double pVal);
    void handlePressure(double pVal);

    void handleSettingsChange(Settings pSettings);

}

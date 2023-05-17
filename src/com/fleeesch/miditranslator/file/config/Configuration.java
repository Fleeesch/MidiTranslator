package com.fleeesch.miditranslator.file.config;

import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.file.FileHandler;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Configuration extends FileHandler {

    //************************************************************
    //      Variables
    //************************************************************

    // setting handlers (per device)
    public static final List<SettingsHandler> settingsHandlers = new ArrayList<>();

    // static configuration instance (there is only one)
    public static final Configuration instance = new Configuration("Configuration", "config.ini");

    // setting handlers that ask for writing
    private static final List<SettingsHandler> settingsToWrite = new ArrayList<>();

    // timer and action for delayed writing
    private static Timer storeTimer;

    // only write if allowed (has to be manually turned off)
    private static boolean doNotWrite = true;

    //************************************************************
    //      Constructor
    //************************************************************

    public Configuration(String pName, String pFileName) {

        super(pName, pFileName); // source constructor

        // setup timer task (just call store settings)
        ActionListener storeTimerTask = e -> storeSettings();

        // setup timer
        storeTimer = new Timer(1000, storeTimerTask);
        storeTimer.setRepeats(false);
    }

    //************************************************************
    //      Static Method : add Device
    //************************************************************

    public static void addDevice(Device d) {

        // device has no settings ? skip this one
        if (d.settings == null) return;

        // create settings handler for device
        settingsHandlers.add(new SettingsHandler(d.settings));

    }

    //************************************************************
    //      Static Method : Load Device Enabled State
    //************************************************************

    public static void loadDeviceEnabledState(Device pDevice) {

        // Ignore settings or file doesn't exist? Declare the device as enabled.
        if (pDevice.ignoreEnableSettings || !instance.file.exists()) {
            pDevice.enabledState = true;
            return;
        }

        instance.loadFile(); // load settings from file

        String key = pDevice.settings.name + "::Enabled"; // generate key

        double state = instance.loadData(key); // load data into double

        pDevice.enabledState = state > 0; // store enabled state from settings value

    }

    //************************************************************
    //      Static Method : Load Device Settings
    //************************************************************

    public static void loadDeviceSettings(Device pDevice) {

        // settings files doesn't exist or device has no settings? skip the rest
        if (!instance.file.exists() || pDevice.settings == null) return;

        instance.loadFile(); // load settings from file

        // go through parameters
        for (Parameter p : pDevice.settings.parameterList) {

            String key = pDevice.settings.name + "::" + p.name; // put together a key

            p.set(instance.loadData(key)); // set parameter from loaded data
        }

    }


    //************************************************************
    //      Static Method : Allow Writing
    //************************************************************

    public static void allowWrite(boolean state) {

        // !! this one is always inverted, kinda akward
        doNotWrite = !state;

    }


    //************************************************************
    //      Static Method : Query Settings Storage
    //************************************************************

    static void querySettingsStorage() {

        // only query settings storage if allowed to
        if (doNotWrite) return;

        // restart timer for storage task
        storeTimer.stop();
        storeTimer.start();

    }

    //************************************************************
    //      Static Method : Store Settings
    //************************************************************

    static void storeSettings() {

        // go through settings
        for (SettingsHandler sh : settingsToWrite) {

            // go through parameters
            for (Parameter p : sh.parametersToWrite) {

                String key = sh.settings.name + "::" + p.name; // get settings key access string

                instance.storeData(key, p.get()); // store data

            }

            sh.clearWriteBuffer(); // clear settings handler parameter write buffer

        }

        settingsToWrite.clear(); // no more settings to write

        instance.saveFile(); // save settings to file

    }

    //************************************************************
    //      Static Method : Store Settings Change
    //************************************************************

    public static void storeSettingsChange(SettingsHandler pSettings) {

        if (!settingsToWrite.contains(pSettings))
            settingsToWrite.add(pSettings); // add settings to write buffer of not already done

        querySettingsStorage(); // request a settings write task

    }

}

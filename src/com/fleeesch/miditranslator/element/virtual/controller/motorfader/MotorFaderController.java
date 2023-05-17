package com.fleeesch.miditranslator.element.virtual.controller.motorfader;

import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.element.input.fader.FaderMotorized;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.control.MfcMulti;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.control.MfcSingle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MotorFaderController extends VirtualElement {


    //************************************************************
    //      Variables
    //************************************************************

    // parameters
    public final HashMap<Parameter, MfcSingle> singleElementHash = new HashMap<>();

    // single buttons
    public final List<MfcSingle> singleElements = new ArrayList<>();
    public final List<MfcSingle> muteElements = new ArrayList<>();
    public final List<MfcSingle> soloElements = new ArrayList<>();
    public final List<MfcSingle> touchElements = new ArrayList<>();
    public final List<MfcSingle> freezeElements = new ArrayList<>();

    // global buttons
    public final List<MfcMulti> muteGlobalElements = new ArrayList<>();
    public final List<MfcMulti> soloGlobalElements = new ArrayList<>();
    public final List<MfcMulti> touchGlobalElements = new ArrayList<>();
    public final List<MfcMulti> freezeGlobalElements = new ArrayList<>();

    // motor faders
    public final List<FaderMotorized> faders = new ArrayList<>();

    public int muteCount = 0;
    public int soloCount = 0;
    public int touchCount = 0;
    public int freezeCount = 0;

    // total size (fader-dependent)
    public int size = 0;

    //************************************************************
    //      Constructor
    //************************************************************

    public MotorFaderController(String pName) {

        super(pName); // use parent contructor

    }

    //************************************************************
    //      Method : Link To Settings
    //************************************************************

    public void linkToSettings(Settings pSettings) {

        for (MfcSingle e : singleElements) {

            pSettings.addParameter(e.parameter);
            singleElementHash.put(e.parameter, e);

            e.parameter.storeInConfig(true);
        }

        pSettings.addEventHandler(this);

    }

    //***********************ß*************************************
    //      Event : On Settings Change
    //************************************************************

    @Override
    public void onSettingsChange(Settings pSettings, Parameter pParameter) {
        super.onSettingsChange(pSettings, pParameter);

        MfcSingle e = singleElementHash.get(pParameter);

        if (e == null) return;

        e.toggleState = pParameter.get() > 0;

        update();

    }

    //************************************************************
    //      Method : Add Element
    //************************************************************


    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Mute Single
    public void addMuteSingle(MfcSingle pElement) {
        muteElements.add(pElement);
        singleElements.add(pElement);
        muteCount++; // count mute elements
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Solo Single

    public void addSoloSingle(MfcSingle pElement) {
        soloElements.add(pElement);
        singleElements.add(pElement);
        soloCount++; // count solo elements
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Touch Single
    public void addTouchSingle(MfcSingle pElement) {
        touchElements.add(pElement);
        singleElements.add(pElement);
        touchCount++; // count touch elements
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Freeze Single
    public void addFreezeSingle(MfcSingle pElement) {
        freezeElements.add(pElement);
        singleElements.add(pElement);
        freezeCount++; // count touch elements
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Mute Global
    public void addMuteGlobal(MfcMulti pElement) {
        pElement.setMembers(muteElements);
        muteGlobalElements.add(pElement);
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Solo Global
    public void addSoloGlobal(MfcMulti pElement) {
        pElement.setMembers(soloElements);
        soloGlobalElements.add(pElement);
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Touch Multi
    public void addTouchGlobal(MfcMulti pElement) {
        pElement.setMembers(touchElements);
        touchGlobalElements.add(pElement);
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Freeze Multi
    public void addFreezeGlobal(MfcMulti pElement) {
        pElement.setMembers(freezeElements);
        freezeGlobalElements.add(pElement);
    }


    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  MotorFader

    public void addFader(FaderMotorized pFader) {

        faders.add(pFader);
        size++;

    }

    //************************************************************
    //      Method : Update
    //************************************************************

    public void update() {

        // go through elements, manually update the LEDs
        for (MfcSingle e : muteElements) e.updateLed();
        for (MfcSingle e : soloElements) e.updateLed();
        for (MfcSingle e : touchElements) e.updateLed();
        for (MfcSingle e : freezeElements) e.updateLed();
        for (MfcMulti e : muteGlobalElements) e.updateLed();
        for (MfcMulti e : soloGlobalElements) e.updateLed();
        for (MfcMulti e : touchGlobalElements) e.updateLed();
        for (MfcMulti e : freezeGlobalElements) e.updateLed();

        // apply values to Faders
        applyToFaders();

    }

    //************************************************************
    //      Method : Apply to Faders
    //************************************************************

    public void applyToFaders() {

        // lists for counting soloed and muted faders
        List<FaderMotorized> fadersSolo = new ArrayList<>();


        // pass touch bypass states
        for (int i = 0; i < touchCount; i++) faders.get(i).bypassTouchData = touchElements.get(i).toggleState;

        // pass touch bypass states
        for (int i = 0; i < freezeCount; i++) faders.get(i).useFreeze = freezeElements.get(i).toggleState;

        // go through mute and solo elements, collect faders
        for (int i = 0; i < soloCount; i++) if (soloElements.get(i).toggleState) fadersSolo.add(faders.get(i));


        // anything soloed?
        if (fadersSolo.size() > 0) {

            // bypass every fader that isn't soloed
            for (int i = 0; i < size; i++) faders.get(i).setBypass(!soloElements.get(i).toggleState);

            // nothing else to do
            return;
        }

        int l = Math.min(size,muteCount);

        // nothing soloed?
        for (int i = 0; i < l; i++) {

            // bypass via mute state
            faders.get(i).setBypass(muteElements.get(i).toggleState);

        }

    }

}

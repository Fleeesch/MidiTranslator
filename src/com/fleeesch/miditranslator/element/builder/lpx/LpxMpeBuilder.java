package com.fleeesch.miditranslator.element.builder.lpx;

import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.device.hardware.LaunchpadX;
import com.fleeesch.miditranslator.element.builder.Builder;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.mpe.MpeDataHandler;
import com.fleeesch.miditranslator.element.virtual.mpe.MpeSurface;

import javax.swing.*;
import java.awt.event.ActionListener;


public class LpxMpeBuilder extends Builder {

    //************************************************************
    //      Variables
    //************************************************************

    // Corner Coordinates : Up-Left, Up-Right, Down-Left, Down-Right
    // Modifier Flags for the corners of the Launchpad
    public final boolean[] pitchUpModulator = new boolean[4];
    public final boolean[] pitchDownModulator = new boolean[4];
    public final boolean[] yModulator = new boolean[4];
    public final boolean[] sustainModulator = new boolean[4];

    // launchpad device
    public final LaunchpadX device;
    public MpeSurface mpeSurface;

    // timer for delayed build task
    public final Timer buildTimer;
    public final ActionListener buildTask;
    public boolean blockBuilding;

    // mpe behaviour settings
    final Settings builderMpeSettings;

    // condition where the builder places its elements in
    final Condition builderCondition;

    // modifierMap
    public int[][] modifierMap;

    public int surfaceAngle = 0;

    //************************************************************
    //      Constructor
    //************************************************************

    public LpxMpeBuilder(String pName, boolean pBlockAutoBuild, LaunchpadX pLaunchpad, Settings pSettings) {

        super(pName); // store name

        device = pLaunchpad; // store launchpad

        builderCondition = Condition.get(); // store the current condition

        if(builderCondition != null) builderCondition.addEventHandler(this);

        builderMpeSettings = pSettings;

        blockBuilding = pBlockAutoBuild;

        // task called when build is queried
        buildTask = e -> build();

        // setup timer with delay
        int buildDelay = 1000;
        buildTimer = new Timer(buildDelay, buildTask);
        buildTimer.setRepeats(false);


    }

    //************************************************************
    //      Method : On Condition Change
    //************************************************************

    @Override
    public void onConditionChange(Condition pCondition, boolean pState) {

        if(!pState || !buildTimer.isRunning())return;

        buildTimer.stop();
        build();


    }


    //************************************************************
    //      Method : Allow Building
    //************************************************************

    public void allowBuild() {

        blockBuilding = false;

    }


    //************************************************************
    //      Method : Build
    //************************************************************

    public void build() {

        if (blockBuilding) return;

        // only create surface if not already created
        if (mpeSurface == null){

            // set device for mapping
            Device.setLast(device);

            // condition for surface
            Condition.set(builderCondition);

            // create surface and data handlers
            mpeSurface = new MpeSurface("MPE Matrix", 8, 8);
            mpeSurface.setupDataHandlers(device.pads, device.ledPads);

            //MpeDataHandler dh = mpeSurface.addDataHandler("Logo Indicator", mpeSurface, 0,0, null, device.ledLogo);
            //mpeSurface.addSettingsRGB(dh);

            // link this surface to the device settings
            mpeSurface.linkToSettings(builderMpeSettings);

            Condition.clear();
        }

        // clear elements
        mpeSurface.clearElementLists();

        createModifierMap(); // create map in order to know where to place modifiers


        // go through surface
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {

                int idx;

                switch (surfaceAngle) {
                    default -> idx = y * 8 + x;
                    case 1 -> idx = x * 8 + 7 - y;
                    case 2 -> idx = (7 - y) * 8 + 7 - x;
                    case 3 -> idx = (7 - x) * 8 + y;
                }

                MpeDataHandler h = mpeSurface.mpeDataHandlers.get(idx);

                // add pad depending on type
                switch (modifierMap[x][y]) {
                    case 0 -> mpeSurface.addGenerator(h);
                    case 1 -> mpeSurface.addSustainModifier(h);
                    case 2 -> mpeSurface.addPitchModifier(h,-1);
                    case 3 -> mpeSurface.addPitchModifier(h,1);
                    case 4 -> mpeSurface.addYModifier(h);
                }

            }

        }

        // update surface settings
        mpeSurface.updateAllSettings();

        // update the leds
        device.updateOutputElements();

    }


    //************************************************************
    //      Method : Create Modifier Map
    //************************************************************

    public void createModifierMap() {

        int inc;


        modifierMap = new int[8][8];

        for (int y = 0; y < 8; y++) for (int x = 0; x < 8; x++) modifierMap[x][y] = 0;

        // UL

        inc = 0;

        if (sustainModulator[0]) modifierMap[inc++][0] = 1;
        if (pitchDownModulator[0]) modifierMap[inc++][0] = 2;
        if (pitchUpModulator[0]) modifierMap[inc++][0] = 3;
        if (yModulator[0]) modifierMap[inc][0] = 4;

        // UR

        inc = 7;

        if (sustainModulator[1]) modifierMap[inc--][0] = 1;
        if (pitchUpModulator[1]) modifierMap[inc--][0] = 3;
        if (pitchDownModulator[1]) modifierMap[inc--][0] = 2;
        if (yModulator[1]) modifierMap[inc][0] = 4;

        // DL

        inc = 0;

        if (sustainModulator[2]) modifierMap[inc++][7] = 1;
        if (pitchDownModulator[2]) modifierMap[inc++][7] = 2;
        if (pitchUpModulator[2]) modifierMap[inc++][7] = 3;
        if (yModulator[2]) modifierMap[inc][7] = 4;

        // DR

        inc = 7;

        if (sustainModulator[3]) modifierMap[inc--][7] = 1;
        if (pitchUpModulator[3]) modifierMap[inc--][7] = 3;
        if (pitchDownModulator[3]) modifierMap[inc--][7] = 2;
        if (yModulator[3]) modifierMap[inc][7] = 4;

    }



    //************************************************************
    //      Method : Query Build
    //************************************************************

    public void queryBuild(){

        if(blockBuilding || !device.isSetup)return;

        buildTimer.stop();
        buildTimer.start();


    }

    //************************************************************
    //      Event : On Settings Change
    //************************************************************

    @Override
    public void onSettingsChange(Settings pSettings, Parameter pParameter) {

        // Sustain

        if (pParameter.name.equals("MPE LPX Builder Sustain UL")) {
            sustainModulator[0] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder Sustain UR")) {
            sustainModulator[1] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder Sustain DL")) {
            sustainModulator[2] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder Sustain DR")) {
            sustainModulator[3] = pParameter.get() > 0;
            queryBuild();
            return;
        }

        // Pitch Up
        if (pParameter.name.equals("MPE LPX Builder PitchUp UL")) {
            pitchUpModulator[0] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder PitchUp UR")) {
            pitchUpModulator[1] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder PitchUp DL")) {
            pitchUpModulator[2] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder PitchUp DR")) {
            pitchUpModulator[3] = pParameter.get() > 0;
            queryBuild();
            return;
        }

        // Pitch Down
        if (pParameter.name.equals("MPE LPX Builder PitchDown UL")) {
            pitchDownModulator[0] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder PitchDown UR")) {
            pitchDownModulator[1] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder PitchDown DL")) {
            pitchDownModulator[2] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder PitchDown DR")) {
            pitchDownModulator[3] = pParameter.get() > 0;
            queryBuild();
            return;
        }

        // Mod
        if (pParameter.name.equals("MPE LPX Builder Mod UL")) {
            yModulator[0] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder Mod UR")) {
            yModulator[1] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder Mod DL")) {
            yModulator[2] = pParameter.get() > 0;
            queryBuild();
            return;
        }
        if (pParameter.name.equals("MPE LPX Builder Mod DR")) {
            yModulator[3] = pParameter.get() > 0;
            queryBuild();
            return;
        }

        // Angle
        if (pParameter.name.equals("MPE Surface Angle")) {
            surfaceAngle = (int)pParameter.get();
            queryBuild();

        }


    }
}

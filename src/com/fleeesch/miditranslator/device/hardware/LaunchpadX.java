package com.fleeesch.miditranslator.device.hardware;

import com.fleeesch.miditranslator.Main;
import com.fleeesch.miditranslator.action.osc.SendOscDirect;
import com.fleeesch.miditranslator.action.osc.SendOscOnPress;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.device.macros.LaunchpadXMacros;
import com.fleeesch.miditranslator.element.builder.lpx.LpxMpeBuilder;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.button.Button;
import com.fleeesch.miditranslator.element.input.pad.PadPressureSensitive;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.output.led.rgb.LedRGBLaunchpad;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.controller.led.LedControllerTrackColor;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterHold;
import com.fleeesch.miditranslator.functions.math.Color;

import java.util.ArrayList;
import java.util.List;


public class LaunchpadX extends Device {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Colors

    public static final double[] colorWhite = {1, 1, 1};
    public static final double[] colorWhiteDim = {0.5, 0.48, 0.48};

    public static final double[] colorBeige = {1, 0.8, 0.7};
    public static double[] colorBeigeLight = {1, 0.9, 0.8};
    public static double[] colorBeigeTinted = {0.8, 0.5, 0.6};

    public static final double[] colorRed = {1, 0, 0};
    public static final double[] colorRedLight = {1, 0.2, 0.2};

    public static final double[] colorAmber = {1, 0.2, 0};
    public static double[] colorAmberLight = {1, 0.4, 0.2};

    public static final double[] colorRose = {1, 0.05, 0.2};
    public static final double[] colorRoseLight = {1, 0.3, 0.4};

    public static final double[] colorOrange = {1, 0.5, 0};
    public static final double[] colorOrangeLight = {1, 0.6, 0.2};

    public static final double[] colorYellow = {1, 1, 0};
    public static double[] colorYellowLight = {1, 1, 0.2};

    public static final double[] colorGreen = {0, 1, 0};
    public static final double[] colorGreenLight = {0.2, 1, 0.2};

    public static final double[] colorLeaf = {0, 1, 0.2};
    public static double[] colorLeafLight = {0.2, 1, 0.5};

    public static final double[] colorBlue = {0, 0, 1};
    public static final double[] colorBlueLight = {0.2, 0.2, 1};

    public static final double[] colorCyan = {0, 1, 1};
    public static final double[] colorCyanLight = {0.5, 1, 1};

    public static double[] colorPink = {1, 0, 1};
    public static final double[] colorPinkLight = {1, 0.3, 1};

    public static final double[] colorDefaultTrack = {1, 0.6, 0.1};


    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Output Elements

    public OutputElement ledLogo;

    public final List<OutputElement> ledPads = new ArrayList<>();
    public final List<OutputElement> ledButtons = new ArrayList<>();

    public final List<LedRGBLaunchpad> launchpadLeds = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Pads

    public final List<InputElement> pads = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Buttons

    public final List<InputElement> buttons = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  MPE Builder

    LpxMpeBuilder mpeBuilder;
    final Settings mpeBuilderSettings = new Settings("Mpe Builder Settings");
    final Settings mpeSettings = new Settings("Mpe Settings");

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Rotation

    public int axis = 0;

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Leader

    public boolean isLeader;

    public boolean useAltPort;

    //************************************************************
    //      Constructor
    //************************************************************

    public LaunchpadX() {

        name = "Launchpad X";
        vendor = "Novation";
        product = "Launchpad X";

        midiInList.add("MIDIIN2 (LPX MIDI)");
        midiOutList.add("MIDIOUT2 (LPX MIDI)");

        // launchpad system exclusive header
        setSysExHeader(0x00, 0x20, 0x29, 0x02, 0x0C);

        // setup settings
        settings = new Settings("Launchpad X Settings");

        isLeader = true; // main launchpad (for mpe sync)

        setupConfiguration();

    }

    public LaunchpadX(boolean pSetupOnlySysEx) {

        // launchpad system exclusive header
        setSysExHeader(0x00, 0x20, 0x29, 0x02, 0x0C);

    }

    //************************************************************
    //      Method : Setup Device
    //************************************************************

    public void setupDevice() {

        if (initiateSetup()) return;

        // create hardware map
        mapHardware(axis);

        // create function map
        mapControls();

        // finish setup
        super.setupDevice();

        // allow mpe builder to do its thing
        mpeBuilder.allowBuild();
        mpeBuilder.build();
    }

    //************************************************************
    //      Method : Setup Midi
    //************************************************************

    @Override
    public boolean setupMidi() {

        if (super.setupMidi()) return true;

        setBrightness(127); // max out brightness

        setToProgrammerMode(); // activate programmer mode

        setPressureThreshold(1); // set at threshold to medium

        setVelocitySensitivity(0); // set velocity to very stubborn

        return false;
    }


    //************************************************************
    //      Method : Set to Programmer Mode
    //************************************************************

    private void setToProgrammerMode() {

        midi.SendSysEx(0x00, 0x7F);

    }

    //************************************************************
    //      Event : On Midi Restart
    //************************************************************

    public void onMidiRestart() {

        setToProgrammerMode();

    }

    //************************************************************
    //      Method : Update Selected Leds (Single / Array)
    //************************************************************

    public void updateMultipleLED(LedRGBLaunchpad... ledBuffer) {

        byte[] msgOut = {(byte) 0x03}; // sysex rgb byte


        for (LedRGBLaunchpad led : ledBuffer) {

            // prevent sending data twice
            led.update();
            if (led.lastSentColor == led.colorHex) continue;

            led.lastSentColor = led.colorHex;

            // create rgb message
            byte[] msgB = {(byte) 0x03, (byte) led.id, (byte) led.colorR, (byte) led.colorG, (byte) led.colorB};

            // copy existing message
            byte[] msgA = new byte[msgOut.length];
            System.arraycopy(msgOut, 0, msgA, 0, msgOut.length);

            // recreate out message with extended length
            msgOut = new byte[msgA.length + msgB.length];

            // combine old + new message
            System.arraycopy(msgA, 0, msgOut, 0, msgA.length);
            System.arraycopy(msgB, 0, msgOut, msgA.length, msgB.length);

        }

        midi.SendSysEx(msgOut);


    }

    //************************************************************
    //      Method : Update Selected LEDs (List)
    //************************************************************

    public void updateMultipleLED(List<LedRGBLaunchpad> pLedList) {

        LedRGBLaunchpad[] arg = new LedRGBLaunchpad[pLedList.size()];

        pLedList.toArray(arg);

        updateMultipleLED(arg);

    }

    //************************************************************
    //      Method : Update All LEDs
    //************************************************************

    public void updateAllLED() {

        updateMultipleLED(launchpadLeds);

    }


    //************************************************************
    //      Method : Set Pressure Threshold
    //************************************************************

    private void setPressureThreshold(int pVal) {
        midi.SendSysEx(11, 0, pVal);

    }

    //************************************************************
    //      Method : Set Velocity Sensitivity
    //************************************************************

    private void setVelocitySensitivity(int pVal) {
        midi.SendSysEx(4, pVal);
    }

    //************************************************************
    //      Method : Set Brightness
    //************************************************************

    private void setBrightness(double pVal) {
        midi.SendSysEx(8, (int) (pVal * 127));
    }

    private void setBrightness(int pVal) {
        midi.SendSysEx(8, pVal);
    }

    //************************************************************
    //      Method : Map Hardware
    //************************************************************

    public void mapHardware(int pAxis) {

        int idx;
        int adr;

        //--- Pads ---

        idx = 0;


        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {

                switch (pAxis) {
                    default -> adr = x + 1 + (8 - y) * 10;
                    case 1 -> adr = 8 - y + (8 - x) * 10;
                    case 2 -> adr = 8 - x + (y + 1) * 10;
                    case 3 -> adr = y + 1 + (x + 1) * 10;
                }


                pads.add(new PadPressureSensitive("Pad " + idx, 144, adr, 160, adr));
                ledPads.add(new LedRGBLaunchpad("Pad LED " + idx, 176, adr));

                idx++;
            }

        }

        //--- Buttons Top ---

        idx = 0;


        for (int x = 0; x < 8; x++) {

            switch (pAxis) {
                default -> adr = x + 91;
                case 1 -> adr = (8 - x) * 10 + 9;
                case 2 -> adr = (7 - x) + 91;
                case 3 -> adr = (x + 1);
            }

            buttons.add(new Button("Button " + idx, 176, adr));
            ledButtons.add(new LedRGBLaunchpad("Button LED " + idx, 176, adr));

            idx++;
        }

        //--- Buttons Side ---

        for (int x = 0; x < 8; x++) {

            switch (pAxis) {
                default -> adr = (8 - x) * 10 + 9;
                case 1 -> adr = (7 - x) + 91;
                case 2 -> adr = (x + 1) * 10 + 9;
                case 3 -> adr = x + 91;
            }


            buttons.add(new Button("Button " + idx, 176, adr));
            ledButtons.add(new LedRGBLaunchpad("Button LED " + idx, 176, adr));

            idx++;
        }


        //--- Logo ---

        ledLogo = new LedRGBLaunchpad("Logo LED", 176, 99);


    }

    //************************************************************
    //      Method : Map Controls
    //************************************************************

    public void mapControls() {


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Parameters : Global
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Menu / View
        Parameter view = new Parameter("Menu");

        settings.addParameter(view);

        view.storeInConfig(true);

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Parameters : MPE
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Sustain
        Parameter susUL = new Parameter("MPE LPX Builder Sustain UL");
        Parameter susUR = new Parameter("MPE LPX Builder Sustain UR");
        Parameter susDL = new Parameter("MPE LPX Builder Sustain DL");
        Parameter susDR = new Parameter("MPE LPX Builder Sustain DR");

        // Pitch Up
        Parameter pitchupUL = new Parameter("MPE LPX Builder PitchUp UL");
        Parameter pitchupUR = new Parameter("MPE LPX Builder PitchUp UR");
        Parameter pitchupDL = new Parameter("MPE LPX Builder PitchUp DL");
        Parameter pitchupDR = new Parameter("MPE LPX Builder PitchUp DR");

        // Pitch Down
        Parameter pitchdownUL = new Parameter("MPE LPX Builder PitchDown UL");
        Parameter pitchdownUR = new Parameter("MPE LPX Builder PitchDown UR");
        Parameter pitchdownDL = new Parameter("MPE LPX Builder PitchDown DL");
        Parameter pitchdownDR = new Parameter("MPE LPX Builder PitchDown DR");

        // Mod
        Parameter modUL = new Parameter("MPE LPX Builder Mod UL");
        Parameter modUR = new Parameter("MPE LPX Builder Mod UR");
        Parameter modDL = new Parameter("MPE LPX Builder Mod DL");
        Parameter modDR = new Parameter("MPE LPX Builder Mod DR");

        // Mod CC
        Parameter mpeModCC = new Parameter("MPE Mod CC");
        mpeModCC.set(74);

        // Use MPE
        Parameter mpeActivated = new Parameter("MPE Activated");

        // Bend Range
        Parameter mpeBendRange = new Parameter("MPE Bend Range");
        mpeBendRange.set(1);

        // Row Offset
        Parameter mpeRowOffset = new Parameter("MPE Row Offset");
        mpeRowOffset.set(6);

        // MPE Channel First
        Parameter mpeChannelFirst = new Parameter("MPE Channel First");
        mpeChannelFirst.set(1);

        // MPE Channel Last
        Parameter mpeChannelLast = new Parameter("MPE Channel Last");
        mpeChannelLast.set(16);

        // Channel Pressure
        Parameter mpeSendPressure = new Parameter("MPE Send Pressure");

        // Velocity Lock
        Parameter mpeVelocityLock = new Parameter("MPE Velocity Lock");

        // Transpose
        Parameter mpeTranspose = new Parameter("MPE Transpose");

        // Surface Angle
        Parameter mpeSurfaceAngle = new Parameter("MPE Surface Angle");

        // --- Add Parameters to Settings ---

        settings.addParameter(susUL);
        settings.addParameter(susUR);
        settings.addParameter(susDL);
        settings.addParameter(susDR);

        settings.addParameter(pitchupUL);
        settings.addParameter(pitchupUR);
        settings.addParameter(pitchupDL);
        settings.addParameter(pitchupDR);

        settings.addParameter(pitchdownUL);
        settings.addParameter(pitchdownUR);
        settings.addParameter(pitchdownDL);
        settings.addParameter(pitchdownDR);

        settings.addParameter(modUL);
        settings.addParameter(modUR);
        settings.addParameter(modDL);
        settings.addParameter(modDR);

        settings.addParameter(mpeModCC);

        settings.addParameter(mpeActivated);
        settings.addParameter(mpeBendRange);
        settings.addParameter(mpeRowOffset);
        settings.addParameter(mpeSendPressure);
        settings.addParameter(mpeTranspose);
        settings.addParameter(mpeVelocityLock);

        settings.addParameter(mpeChannelFirst);
        settings.addParameter(mpeChannelLast);

        settings.addParameter(mpeSurfaceAngle);

        // --- Store Parameters in Settings ---

        susUL.storeInConfig(true);
        susUR.storeInConfig(true);
        susDL.storeInConfig(true);
        susDR.storeInConfig(true);

        pitchupUL.storeInConfig(true);
        pitchupUR.storeInConfig(true);
        pitchupDL.storeInConfig(true);
        pitchupDR.storeInConfig(true);

        pitchdownUL.storeInConfig(true);
        pitchdownUR.storeInConfig(true);
        pitchdownDL.storeInConfig(true);
        pitchdownDR.storeInConfig(true);

        modUL.storeInConfig(true);
        modUR.storeInConfig(true);
        modDL.storeInConfig(true);
        modDR.storeInConfig(true);

        mpeModCC.storeInConfig(true);

        mpeActivated.storeInConfig(true);
        mpeBendRange.storeInConfig(true);
        mpeRowOffset.storeInConfig(true);
        mpeSendPressure.storeInConfig(true);
        mpeTranspose.storeInConfig(true);
        mpeVelocityLock.storeInConfig(true);

        mpeChannelFirst.storeInConfig(true);
        mpeChannelLast.storeInConfig(true);

        mpeSurfaceAngle.storeInConfig(true);

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Conditions
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // View -> DAW Control
        Condition viewIsDawControl = Condition.add(view, 0);
        Condition.clear();
        // View -> MPE
        Condition viewIsMpe = Condition.add(view, 1);
        Condition.clear();
        // View -> MPE Options
        Condition viewIsMpeOptions = Condition.add(view, 2);
        Condition.clear();
        // View -> Trigger Pads (OSC)
        Condition viewIsTriggerPadsOsc = Condition.add(view, 3);
        Condition.clear();
        // View -> Trigger Pads (MIDI)
        Condition viewIsTriggerPadsMidi = Condition.add(view, 4);
        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Setup Macros
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Pass Parameters to Macro Creator
        LaunchpadXMacros.setupParameters(view, viewIsDawControl, viewIsMpe, viewIsMpeOptions, viewIsTriggerPadsOsc, viewIsTriggerPadsMidi);
        LaunchpadXMacros.setupHardware(pads, ledPads, buttons, ledButtons, ledLogo);


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Menus
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // add menu Interface
        LaunchpadXMacros.addMenuInterface();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Track Color
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        new LedControllerTrackColor("Track Color", Main.deviceDaw.trackData.tracks.get(0), 1, Color.RgbDoubleToHex(colorDefaultTrack[0],colorDefaultTrack[1],colorDefaultTrack[2]));
        VirtualElement.last.addTarget(ledLogo);
        ((LedControllerTrackColor) VirtualElement.last).setColor();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      MPE Surface : Parameters
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Sustain
        mpeBuilderSettings.addParameter(susUL);
        mpeBuilderSettings.addParameter(susUR);
        mpeBuilderSettings.addParameter(susDL);
        mpeBuilderSettings.addParameter(susDR);

        // Pitch Down
        mpeBuilderSettings.addParameter(pitchdownUL);
        mpeBuilderSettings.addParameter(pitchdownUR);
        mpeBuilderSettings.addParameter(pitchdownDL);
        mpeBuilderSettings.addParameter(pitchdownDR);

        // Pitch Up
        mpeBuilderSettings.addParameter(pitchupUL);
        mpeBuilderSettings.addParameter(pitchupUR);
        mpeBuilderSettings.addParameter(pitchupDL);
        mpeBuilderSettings.addParameter(pitchupDR);

        // Y-Modifier
        mpeBuilderSettings.addParameter(modUL);
        mpeBuilderSettings.addParameter(modUR);
        mpeBuilderSettings.addParameter(modDL);
        mpeBuilderSettings.addParameter(modDR);

        // Surface Rotation
        mpeBuilderSettings.addParameter(mpeSurfaceAngle);

        // MPE Settings
        mpeSettings.addParameter(mpeActivated);
        mpeSettings.addParameter(mpeRowOffset);
        mpeSettings.addParameter(mpeBendRange);
        mpeSettings.addParameter(mpeSendPressure);
        mpeSettings.addParameter(mpeTranspose);
        mpeSettings.addParameter(mpeModCC);
        mpeSettings.addParameter(mpeVelocityLock);

        mpeSettings.addParameter(mpeChannelFirst);
        mpeSettings.addParameter(mpeChannelLast);

        mpeSettings.addParameter(mpeSurfaceAngle);


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      MPE Surface : Builder
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: View -> MPE :::

        Condition.set(viewIsMpe);

        // create MPE Builder
        mpeBuilder = new LpxMpeBuilder("MPE Playing Surface", true, this, mpeSettings);

        // link to settings instance
        mpeBuilder.linkToSettings(mpeBuilderSettings);

        // add transpose Buttons
        LaunchpadXMacros.addTransposeSet(mpeTranspose, mpeRowOffset);

        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      MPE Surface : Settings
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: View -> MPE Settings :::

        Condition.set(viewIsMpeOptions);

        // Sustain
        LaunchpadXMacros.addParameterToggle("Sustain UL", pads.get(0), ledPads.get(0), susUL, colorBlueLight);
        LaunchpadXMacros.addParameterToggle("Sustain UR", pads.get(7), ledPads.get(7), susUR, colorBlueLight);
        LaunchpadXMacros.addParameterToggle("Sustain DL", pads.get(8), ledPads.get(8), susDL, colorBlueLight);
        LaunchpadXMacros.addParameterToggle("Sustain DR", pads.get(15), ledPads.get(15), susDR, colorBlueLight);

        // Pitch Down
        LaunchpadXMacros.addParameterToggle("Pitch Down UL", pads.get(1), ledPads.get(1), pitchdownUL, colorGreenLight);
        LaunchpadXMacros.addParameterToggle("Pitch Down UR", pads.get(5), ledPads.get(5), pitchdownUR, colorGreenLight);
        LaunchpadXMacros.addParameterToggle("Pitch Down DL", pads.get(9), ledPads.get(9), pitchdownDL, colorGreenLight);
        LaunchpadXMacros.addParameterToggle("Pitch Down DR", pads.get(13), ledPads.get(13), pitchdownDR, colorGreenLight);

        // Pitch Up
        LaunchpadXMacros.addParameterToggle("Pitch Up UL", pads.get(2), ledPads.get(2), pitchupUL, colorGreenLight);
        LaunchpadXMacros.addParameterToggle("Pitch Up UR", pads.get(6), ledPads.get(6), pitchupUR, colorGreenLight);
        LaunchpadXMacros.addParameterToggle("Pitch Up DL", pads.get(10), ledPads.get(10), pitchupDL, colorGreenLight);
        LaunchpadXMacros.addParameterToggle("Pitch Up DR", pads.get(14), ledPads.get(14), pitchupDR, colorGreenLight);

        // Mod
        LaunchpadXMacros.addParameterToggle("Mod UL", pads.get(3), ledPads.get(3), modUL, colorOrange);
        LaunchpadXMacros.addParameterToggle("Mod UR", pads.get(4), ledPads.get(4), modUR, colorOrange);
        LaunchpadXMacros.addParameterToggle("Mod DL", pads.get(11), ledPads.get(11), modDL, colorOrange);
        LaunchpadXMacros.addParameterToggle("Mod DR", pads.get(12), ledPads.get(12), modDR, colorOrange);

        // Mod CC
        LaunchpadXMacros.addParameterSet("Mod CC", pads.get(16), ledPads.get(16), mpeModCC, 74, colorAmber);
        LaunchpadXMacros.addParameterSet("Mod CC", pads.get(17), ledPads.get(17), mpeModCC, 11, colorAmber);
        LaunchpadXMacros.addParameterSet("Mod CC", pads.get(18), ledPads.get(18), mpeModCC, 1, colorAmber);

        // MPE functionality
        LaunchpadXMacros.addParameterToggle("Use MPE", pads.get(31), ledPads.get(31), mpeActivated, colorWhite);

        // Send Pressure Data
        LaunchpadXMacros.addParameterToggle("Send Pressure", pads.get(30), ledPads.get(30), mpeSendPressure, colorCyan);

        // Velocity Lock
        LaunchpadXMacros.addParameterToggle("Velocity Lock", pads.get(29), ledPads.get(29), mpeVelocityLock, colorBlueLight);

        // ~ ~ ~ Set MPE Surface Angle ~ ~ ~

        LaunchpadXMacros.addParameterSet("Mpe Surface Angle", pads.get(24), ledPads.get(24), mpeSurfaceAngle, 0, colorRedLight);
        LaunchpadXMacros.addParameterSet("Mpe Surface Angle", pads.get(25), ledPads.get(25), mpeSurfaceAngle, 1, colorRedLight);
        LaunchpadXMacros.addParameterSet("Mpe Surface Angle", pads.get(26), ledPads.get(26), mpeSurfaceAngle, 2, colorRedLight);
        LaunchpadXMacros.addParameterSet("Mpe Surface Angle", pads.get(27), ledPads.get(27), mpeSurfaceAngle, 3, colorRedLight);

        // ~ ~ ~ Set MPE Channel First ~ ~ ~

        // create lookup
        int[] mpeChannelFirstLookup = {1, 5, 9, 13};

        // place settings
        for (int i = 0; i < 4; i++) {
            LaunchpadXMacros.addParameterSet("Mpe Channel First", pads.get(36 + i), ledPads.get(36 + i), mpeChannelFirst, mpeChannelFirstLookup[i], colorWhiteDim);
        }

        // ~ ~ ~ Set MPE Channel Last ~ ~ ~

        // create lookup
        int[] mpeChannelLastLookup = {4, 9, 12, 16};

        // place settings
        for (int i = 0; i < 4; i++) {
            LaunchpadXMacros.addParameterSet("Mpe Channel First", pads.get(44 + i), ledPads.get(44 + i), mpeChannelLast, mpeChannelLastLookup[i], colorWhiteDim);
        }

        // ~ ~ ~ Set Bend Range ~ ~ ~

        // create lookup
        double[] bendLookup = new double[12];
        for (int i = 0; i < 12; i++) bendLookup[i] = (1.0 / 12.0) * (i + 1);

        // create picklist for settings
        int[] bendPick = {0, 1, 2, 3, 4, 5, 6, 11};

        // place settings
        for (int i = 0; i < 8; i++) {
            LaunchpadXMacros.addParameterSet("Bend Range", pads.get(48 + i), ledPads.get(48 + i), mpeBendRange, bendLookup[bendPick[i]], colorLeaf);
        }

        // ~ ~ ~ Set Row Offset ~ ~ ~

        for (int i = 0; i < 8; i++) {
            LaunchpadXMacros.addParameterSet("Row Offset", pads.get(56 + i), ledPads.get(56 + i), mpeRowOffset, 1 + i, colorOrange);
        }

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Trigger Pad View
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: View -> Trigger Pads (OSC) :::

        Condition.add(view, 3);
        LaunchpadXMacros.addTriggerPads(false);
        Condition.clear();

        // ::: View -> Trigger Pads (MIDI) :::

        Condition.add(view, 4);
        LaunchpadXMacros.addTriggerPads(true);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Main Transport Controls
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Save
        LaunchpadXMacros.addSimpleControlButton("Save", buttons.get(7), ledButtons.get(7), OscAddress.save, colorOrange);

        // ::: View -> NOT Trigger Pads :::

        Condition.add(view, 3, 2);

        // Jog
        LaunchpadXMacros.addSimpleControlButton("Scroll Left", buttons.get(10), ledButtons.get(10), OscAddress.jogLeft, colorOrangeLight);
        LaunchpadXMacros.addSimpleControlButton("Scroll Right", buttons.get(11), ledButtons.get(11), OscAddress.jogRight, colorOrangeLight);

        // Track Selection
        LaunchpadXMacros.addSimpleControlButtonTrackColor("Previous Track", buttons.get(8), ledButtons.get(8), OscAddress.trackSelectPrevious, colorDefaultTrack, 1);
        LaunchpadXMacros.addSimpleControlButtonTrackColor("Next Track", buttons.get(9), ledButtons.get(9), OscAddress.trackSelectNext, colorDefaultTrack, 1);

        Condition.clear();

        Condition.add(view, 3, 2);

        // Play, Record, Undo, Redo
        LaunchpadXMacros.addSimpleControlButton("Undo", buttons.get(12), ledButtons.get(12), OscAddress.undo, colorOrange);
        LaunchpadXMacros.addSimpleControlButton("Redo", buttons.get(13), ledButtons.get(13), OscAddress.redo, colorOrange);
        LaunchpadXMacros.addFeedbackControlButton("Record", buttons.get(14), ledButtons.get(14), 1, OscAddress.record, null, colorRed);
        LaunchpadXMacros.addFeedbackControlButton("Play", buttons.get(15), ledButtons.get(15), 1, OscAddress.play, null, colorWhite);

        Condition.clear();

        // ::: View -> Trigger Pads :::

        Condition.add(view, 3, 5);

        // Play, Record, Undo, Redo
        LaunchpadXMacros.addSimpleControlButton("Undo", buttons.get(0), ledButtons.get(0), OscAddress.undo, colorOrange);
        LaunchpadXMacros.addSimpleControlButton("Redo", buttons.get(1), ledButtons.get(1), OscAddress.redo, colorOrange);
        LaunchpadXMacros.addFeedbackControlButton("Record", buttons.get(2), ledButtons.get(2), 1,OscAddress.record, null, colorRed);
        LaunchpadXMacros.addFeedbackControlButton("Play", buttons.get(3), ledButtons.get(3), 1,OscAddress.play, null, colorWhite);


        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      DAW Control
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: View -> DAW Control :::

        Condition.set(viewIsDawControl);

        // Default Rates for repeated Actions
        int rateMin = 250;
        int rateMax = 20;

        //:::::::::::::::::::::::
        //  Transport
        //:::::::::::::::::::::::

        // Scroll
        LaunchpadXMacros.addPressureControl("Scroll Left", 56, OscAddress.jogGrid, rateMin, rateMax, 0.49, 0.49, colorOrange);
        LaunchpadXMacros.addPressureControl("Scroll Right", 57, OscAddress.jogGrid, rateMin, rateMax, 0.51, 0.51, colorOrange);

        // Zoom
        LaunchpadXMacros.addPressureControl("Zoom In", 48, OscAddress.zoomHorizontal, rateMin, rateMax, 0.49, 0.49, colorOrangeLight);
        LaunchpadXMacros.addPressureControl("Zoom Out", 49, OscAddress.zoomHorizontal, rateMin, rateMax, 0.51, 0.51, colorOrangeLight);

        // Grid Size
        LaunchpadXMacros.addPressureControl("Grid Half", 58, OscAddress.gridHalf, rateMin, (int) (rateMin * 0.5), 1, 1, colorOrangeLight);
        LaunchpadXMacros.addPressureControl("Grid Double", 59, OscAddress.gridDouble, rateMin, (int) (rateMin * 0.5), 0.51, 0.51, colorOrangeLight);

        // Dynamic Zoom
        LaunchpadXMacros.addSimpleControlButton("Dynamic Zoom In", pads.get(50), ledPads.get(50), OscAddress.dynamicZoomIn, colorBeige);
        LaunchpadXMacros.addSimpleControlButton("Dynamic Zoom Out", pads.get(51), ledPads.get(51), OscAddress.dynamicZoomOut, colorBeige);


        // Toggle Repeat
        LaunchpadXMacros.addFeedbackControlButton("Toggle Repeat", pads.get(46), ledPads.get(46), 1, OscAddress.toggleRepeat, null, colorBlue);

        // Toggle Continuous Scroll
        LaunchpadXMacros.addFeedbackControlButton("Toggle Scroll", pads.get(47), ledPads.get(47), 1, OscAddress.toggleAutoScroll, null, colorBlueLight);


        //:::::::::::::::::::::::
        //  Record Scopes
        //:::::::::::::::::::::::

        // Free
        LaunchpadXMacros.addFeedbackControlButton("Record Scope Normal", pads.get(61), ledPads.get(61), 1, OscAddress.recordScopeFree, null, colorRed);
        // Time
        LaunchpadXMacros.addFeedbackControlButton("Record Scope Time", pads.get(62), ledPads.get(62), 1, OscAddress.recordScopeTime, null, colorRed);
        // Item
        LaunchpadXMacros.addFeedbackControlButton("Record Scope Item", pads.get(63), ledPads.get(63), 1, OscAddress.recordScopeItem, null, colorRed);

        //:::::::::::::::::::::::
        //  Tempo
        //:::::::::::::::::::::::

        // Tap Tempo
        LaunchpadXMacros.addSimpleControlButton("Tap Tempo", pads.get(39), ledPads.get(39), OscAddress.tapTempo, colorWhiteDim);

        //:::::::::::::::::::::::
        //  Freezing
        //:::::::::::::::::::::::

        // Freeze
        LaunchpadXMacros.addSimpleControlButton("Unfreeze", pads.get(6), ledPads.get(6), OscAddress.trackUnfreeze, colorGreen);
        LaunchpadXMacros.addSimpleControlButton("Freeze", pads.get(7), ledPads.get(7), OscAddress.trackFreeze, colorGreen);

        // Auto Glue
        LaunchpadXMacros.addSimpleControlButton("Auto-Glue", pads.get(14), ledPads.get(14), OscAddress.autoGlue, colorOrange);

        // Bounce

        new InterpreterHold("Bounce");
        VirtualElement.last.addSource(pads.get(15));
        VirtualElement.last.addTarget(ledPads.get(15));
        VirtualElement.last.addAction(new SendOscDirect(1, OscAddress.trackBounceStereo));
        VirtualElement.last.addAction(new SendOscDirect(1, OscAddress.trackBounceStereo2ndPass));
        VirtualElement.last.setParameterValue(1, colorRed[0], colorRed[1], colorRed[2]);


        //:::::::::::::::::::::::
        //  Selection
        //:::::::::::::::::::::::

        // Selection Size
        LaunchpadXMacros.addPressureControl("Selection -", 40, OscAddress.selectionSize, rateMin, rateMax, 0.49, 0.49, colorGreen);
        LaunchpadXMacros.addPressureControl("Selection +", 41, OscAddress.selectionSize, rateMin, rateMax, 0.51, 0.51, colorGreen);

        // Selection Move
        LaunchpadXMacros.addPressureControl("Selection <", 42, OscAddress.selectionMoveRelative, rateMin, rateMax, 0.49, 0.49, colorGreenLight);
        LaunchpadXMacros.addPressureControl("Selection >", 43, OscAddress.selectionMoveRelative, rateMin, rateMax, 0.51, 0.51, colorGreenLight);

        // Selection Delete
        LaunchpadXMacros.addSimpleControlButton("Selection Delete", pads.get(44), ledPads.get(44), OscAddress.selectionRemove, colorGreen);

        //:::::::::::::::::::::::
        //  Track
        //:::::::::::::::::::::::

        // Dynamic Zoom
        LaunchpadXMacros.addSimpleControlButtonTrackColor("Dynamic Zoom In", buttons.get(0), ledButtons.get(0), OscAddress.dynamicZoomIn, colorDefaultTrack, 1);
        LaunchpadXMacros.addSimpleControlButtonTrackColor("Dynamic Zoom Out", buttons.get(1), ledButtons.get(1), OscAddress.dynamicZoomOut, colorDefaultTrack, 1);

        // Track Select
        LaunchpadXMacros.addPressureControlDoubleTrackColor("Select Previous Track", 0, OscAddress.trackSelectRelative, rateMin, rateMax, 0.49, 0.49, OscAddress.trackAddRelative, 0.49, colorDefaultTrack, 1);
        LaunchpadXMacros.addPressureControlDoubleTrackColor("Select Next Track", 1, OscAddress.trackSelectRelative, rateMin, rateMax, 0.51, 0.51, OscAddress.trackAddRelative, 0.51, colorDefaultTrack, 1);

        // Track Mute
        LaunchpadXMacros.addFeedbackControlButton("Toggle Track Mute", pads.get(2), ledPads.get(2), 1, OscAddress.trackToggleMute, OscAddress.trackMute + "/0", colorRed);

        // Track Solo
        LaunchpadXMacros.addFeedbackControlButton("Toggle Track Solo", pads.get(3), ledPads.get(3), 1, OscAddress.trackToggleSolo, OscAddress.trackSolo + "/0", colorYellow);

        // Track Record Arm
        new InterpreterHold("Toggle Track Record Arm");
        VirtualElement.last.addSource(pads.get(4));
        VirtualElement.last.addTarget(ledPads.get(4));
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackToggleRecordArm));
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackRecArmAutoOn));
        VirtualElement.last.setParameterValue(1, colorRed[0], colorRed[1], colorRed[2], 1);
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackArm + "/0");

        //:::::::::::::::::::::::
        //  Item
        //:::::::::::::::::::::::

        // Item Select
        LaunchpadXMacros.addPressureControlDoubleTrackColor("Select Previous Item", 8, OscAddress.itemSelectRelative, rateMin, rateMax, 0.49, 0.49, OscAddress.itemAddRelative, 0.49, colorDefaultTrack, 1);
        LaunchpadXMacros.addPressureControlDoubleTrackColor("Select Next Item", 9, OscAddress.itemSelectRelative, rateMin, rateMax, 0.51, 0.51, OscAddress.itemAddRelative, 0.51, colorDefaultTrack, 1);

        // Item Mute
        LaunchpadXMacros.addSimpleControlButton("Toggle Track Mute", pads.get(10), ledPads.get(10), OscAddress.itemToggleMute, colorRed);

        //:::::::::::::::::::::::
        //  Take
        //:::::::::::::::::::::::

        // Take Select
        LaunchpadXMacros.addPressureControlTrackColor("Select Previous Item", 16, OscAddress.takeSelectRelative, rateMin, rateMax, 0.49, 0.49, colorDefaultTrack,1);
        LaunchpadXMacros.addPressureControlTrackColor("Select Next Item", 17, OscAddress.takeSelectRelative, rateMin, rateMax, 0.51, 0.51, colorDefaultTrack, 1);

        // Take Delete / Crop
        LaunchpadXMacros.addHoldControlButton("Take Delete / Crop", pads.get(18), ledPads.get(18), OscAddress.takeRemove, OscAddress.takeCrop, colorRed);

        //:::::::::::::::::::::::
        //  Automation
        //:::::::::::::::::::::::

        // Overwrite Off
        LaunchpadXMacros.addFeedbackControlButton("Envelope Off", pads.get(32), ledPads.get(32), 1, OscAddress.automationModeFree, null, colorBeige);
        // Overwrite Latch Preview
        LaunchpadXMacros.addFeedbackControlButton("Envelope Latch Preview", pads.get(33), ledPads.get(33), 1, OscAddress.automationModeLatchPreview, null, colorYellow);
        // Overwrite Latch
        LaunchpadXMacros.addFeedbackControlButton("Envelope Latch", pads.get(34), ledPads.get(34), 1, OscAddress.automationModeLatch, null, colorYellow);
        // Overwrite Touch
        LaunchpadXMacros.addFeedbackControlButton("Envelope Touch", pads.get(35), ledPads.get(35), 1, OscAddress.automationModeTouch, null, colorBlue);
        // Overwrite Write
        LaunchpadXMacros.addFeedbackControlButton("Envelope Write", pads.get(36), ledPads.get(36), 1, OscAddress.automationModeWrite, null, colorRed);


        // Toggle Auto Add Envelopes
        LaunchpadXMacros.addFeedbackControlButton("Auto-Add Envelopes", pads.get(28), ledPads.get(28), 1, OscAddress.automationToggleAutoAddEnvelopes, null, colorGreenLight);
        // Toggle Track Envelope Arm
        LaunchpadXMacros.addSimpleControlButton("Toggle Track Envelope Arm", pads.get(25), ledPads.get(25), OscAddress.automationArmTrackEnvelopes, colorRed);

        // Write Envelope Data to End of Project
        LaunchpadXMacros.addSimpleControlButton("Write Envelope Data To End", pads.get(24), ledPads.get(24), OscAddress.automationWriteToEnd, colorRedLight);

        //:::::::::::::::::::::::
        //  Audio Settings
        //:::::::::::::::::::::::

        // Solo in Front
        LaunchpadXMacros.addFeedbackControlButton("Solo in Front", pads.get(54), ledPads.get(54), 1, OscAddress.toggleSoloInFront, null, colorYellow);

        // Master Mono
        LaunchpadXMacros.addFeedbackControlButton("Master Mono", pads.get(55), ledPads.get(55), 1, OscAddress.masterToggleMono, null, colorAmber);

    }


}

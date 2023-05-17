package com.fleeesch.miditranslator.device.hardware;

import com.fleeesch.miditranslator.action.midi.SendMidiDirect;
import com.fleeesch.miditranslator.action.osc.SendOscDirect;
import com.fleeesch.miditranslator.action.osc.SendOscOnPress;
import com.fleeesch.miditranslator.action.parameter.ParameterSet;
import com.fleeesch.miditranslator.action.parameter.ParameterSetReturn;
import com.fleeesch.miditranslator.action.parameter.ParameterToggle;
import com.fleeesch.miditranslator.data.lookup.midi.MidiAddress;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.device.macros.FaderPortV2Macros;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.button.Button;
import com.fleeesch.miditranslator.element.input.encoder.EncoderRelative3;
import com.fleeesch.miditranslator.element.input.fader.FaderMotorized;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.output.led.binary.LedBinary;
import com.fleeesch.miditranslator.element.output.led.rgb.LedRGBFaderPort;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.MotorFaderController;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.control.MfcSingle;
import com.fleeesch.miditranslator.element.virtual.interpreter.*;

import static com.fleeesch.miditranslator.Main.deviceDaw;


public class FaderPortV2 extends Device {

    //************************************************************
    //      Variables
    //************************************************************


    //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    //          INPUT

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Motor Fader

    public InputElement fader;

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Encoder

    public InputElement encoderButton;
    public InputElement encoderJog;

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Buttons

    public InputElement buttonPedal;

    public InputElement buttonPlay;
    public InputElement buttonRecord;
    public InputElement buttonRewind;
    public InputElement buttonForward;

    public InputElement buttonStop;
    public InputElement buttonLoop;

    public InputElement buttonLink;
    public InputElement buttonPan;
    public InputElement buttonChannel;
    public InputElement buttonScroll;

    public InputElement buttonMaster;
    public InputElement buttonClick;
    public InputElement buttonSection;
    public InputElement buttonMarker;

    public InputElement buttonPrev;
    public InputElement buttonNext;

    public InputElement buttonSolo;
    public InputElement buttonMute;
    public InputElement buttonArm;
    public InputElement buttonShift;

    public InputElement buttonBypass;
    public InputElement buttonTouch;
    public InputElement buttonWrite;
    public InputElement buttonRead;

    //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    //          OUTPUT

    public OutputElement ledButtonPlay;
    public OutputElement ledButtonRecord;
    public OutputElement ledButtonRewind;
    public OutputElement ledButtonForward;
    public OutputElement ledButtonLoop;
    public OutputElement ledButtonStop;

    public OutputElement ledButtonLink;
    public OutputElement ledButtonPan;
    public OutputElement ledButtonChannel;
    public OutputElement ledButtonScroll;

    public OutputElement ledButtonMaster;
    public OutputElement ledButtonClick;
    public OutputElement ledButtonSection;
    public OutputElement ledButtonMarker;

    public OutputElement ledButtonPrev;
    public OutputElement ledButtonNext;

    public OutputElement ledButtonSolo;
    public OutputElement ledButtonMute;
    public OutputElement ledButtonArm;
    public OutputElement ledButtonShift;

    public OutputElement ledButtonBypass;
    public OutputElement ledButtonTouch;
    public OutputElement ledButtonWrite;
    public OutputElement ledButtonRead;


    //************************************************************
    //      Constructor
    //************************************************************

    public FaderPortV2() {

        super();

        // name of device
        name = "FaderPort V2";
        vendor = "PreSonus";
        product = "FaderPort V2";

        midiInList.add("PreSonus FP2");
        midiOutList.add("PreSonus FP2");

        // faderport system exclusive header
        setSysExHeader(0x00, 0x01, 0x06, 0x16);
        sysexForceOpenClose = true;

        // setup settings
        settings = new Settings("FaderPort V2 Settings");

        setupConfiguration();

    }

    public FaderPortV2(boolean pSkipSetup) {

        super();

        // faderport system exclusive header
        setSysExHeader(0x00, 0x01, 0x06, 0x16);
        sysexForceOpenClose = true;

    }

    //************************************************************
    //      Method : Setup Device
    //************************************************************

    public void setupDevice() {

        if (initiateSetup()) return;

        // create hardware map
        mapHardware();

        // create function map
        mapControls();

        // finish setup
        super.setupDevice();

    }


    //************************************************************
    //      Method : Map Hardware
    //************************************************************

    public void mapHardware() {

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          INPUT

        // Fader

        fader = new FaderMotorized("IE Fader", 0, 224, 144, 104);
        ((FaderMotorized) fader).rescaleInput(0.01, 1 - 0.0115);

        //--- Transport ---

        // Pedal

        buttonPedal = new Button("IE Pedal", 144, 102);

        // Buttons

        buttonPlay = new Button("IE Play", 144, 94);
        buttonRecord = new Button("IE Record", 144, 95);
        buttonRewind = new Button("IE Rewind", 144, 91);
        buttonForward = new Button("IE Forward", 144, 92);

        buttonStop = new Button("IE Stop", 144, 93);
        buttonLoop = new Button("IE Stop", 144, 86);

        //--- Lower Matrix ---

        buttonMaster = new Button("IE Master", 144, 58);
        buttonClick = new Button("IE Click", 144, 59);
        buttonSection = new Button("IE Section", 144, 60);
        buttonMarker = new Button("IE Marker", 144, 61);

        buttonLink = new Button("IE Link", 144, 5);
        buttonPan = new Button("IE Pan", 144, 42);
        buttonChannel = new Button("IE Channel", 144, 54);
        buttonScroll = new Button("IE Scroll", 144, 56);

        //--- Encoder Section ---

        buttonPrev = new Button("IE Prev", 144, 46);
        buttonNext = new Button("IE Next", 144, 47);

        encoderJog = new EncoderRelative3("IE Encoder Jog", 176, 16);
        encoderButton = new Button("IE Encoder Push", 144, 32);

        //--- Upper Matrix ---

        buttonBypass = new Button("IE Bypass", 144, 3);
        buttonTouch = new Button("IE Touch", 144, 77);
        buttonWrite = new Button("IE Write", 144, 75);
        buttonRead = new Button("IE Read", 144, 74);

        buttonSolo = new Button("IE Solo", 144, 8);
        buttonMute = new Button("IE Mute", 144, 16);
        buttonArm = new Button("IE Arm", 144, 0);
        buttonShift = new Button("IE Shift", 144, 70);

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          OUTPUT

        //--- Transport ---

        ledButtonPlay = new LedBinary("OE Play", 144, 94);
        ledButtonRecord = new LedBinary("OE Record", 144, 95);
        ledButtonRewind = new LedBinary("OE Rewind", 144, 91);
        ledButtonForward = new LedBinary("OE Forward", 144, 92);

        ledButtonStop = new LedBinary("OE Stop", 144, 93);
        ledButtonLoop = new LedBinary("OE Loop", 144, 86);

        //--- Lower Matrix ---

        ledButtonLink = new LedRGBFaderPort("OE Link", 144, 5);
        ledButtonPan = new LedRGBFaderPort("OE Pan", 144, 42);
        ledButtonChannel = new LedRGBFaderPort("OE Channel", 144, 54);
        ledButtonScroll = new LedRGBFaderPort("OE Scroll", 144, 56);

        ledButtonMaster = new LedBinary("OE Master", 144, 58);
        ledButtonClick = new LedBinary("OE Click", 144, 59);
        ledButtonSection = new LedBinary("OE Section", 144, 60);
        ledButtonMarker = new LedBinary("OE Marker", 144, 61);

        //--- Encoder Section ---

        ledButtonPrev = new LedBinary("OE Prev", 144, 46);
        ledButtonNext = new LedBinary("OE Next", 144, 47);

        //--- Upper Matrix ---

        ledButtonSolo = new LedBinary("OE Solo", 144, 8);
        ledButtonMute = new LedBinary("OE Mute", 144, 16);
        ledButtonArm = new LedBinary("OE Arm", 144, 0);
        ledButtonShift = new LedBinary("OE Shift", 144, 70);

        ledButtonBypass = new LedBinary("OE Bypass", 144, 3);
        ledButtonTouch = new LedRGBFaderPort("OE Touch", 144, 77);
        ledButtonWrite = new LedRGBFaderPort("OE Write", 144, 75);
        ledButtonRead = new LedRGBFaderPort("OE Read", 144, 74);


    }

    //************************************************************
    //      Method : Map Controls
    //************************************************************

    public void mapControls() {

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Macros
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        FaderPortV2Macros.setup(this);

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Parameters
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // shift
        Parameter shift = new Parameter("Shift");

        // encoder push
        Parameter encoderPush = new Parameter("Encoder Push");

        // encoder mode
        Parameter encoderMode = new Parameter("Encoder Mode");

        // fader mode
        Parameter faderMode = new Parameter("Fader Mode");

        // banks
        Parameter faderBankTrack = new Parameter("Fader Bank Track");
        Parameter faderBankSend = new Parameter("Fader Bank Send");
        Parameter faderBankFree1 = new Parameter("Fader Bank Free 1");
        Parameter faderBankFree2 = new Parameter("Fader Bank Free 2");
        Parameter faderBankMidi1 = new Parameter("Fader Bank Midi");
        Parameter faderBankMidi2 = new Parameter("Fader Bank Midi 14Bit");
        Parameter faderBankFx1 = new Parameter("Fader Bank FX 1");
        Parameter faderBankFx2 = new Parameter("Fader Bank FX 2");

        // fader mode
        Parameter triggerButtonMode = new Parameter("Trigger Button Mode");

        // menu bank
        Parameter menuMode = new Parameter("Fader Mode");

        // midi 14 bit toggle
        Parameter midi14Bit = new Parameter("Midi 14-Bit Toggle");

        // pedal Mode
        Parameter pedalMode = new Parameter("Pedal Mode");

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Config
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // add Parameters to Settings
        settings.addParameter(faderMode);
        settings.addParameter(encoderMode);
        settings.addParameter(faderBankTrack);
        settings.addParameter(faderBankSend);
        settings.addParameter(faderBankFree1);
        settings.addParameter(faderBankFree2);
        settings.addParameter(faderBankMidi1);
        settings.addParameter(faderBankMidi2);
        settings.addParameter(faderBankFx1);
        settings.addParameter(faderBankFx2);
        settings.addParameter(triggerButtonMode);
        settings.addParameter(menuMode);
        settings.addParameter(midi14Bit);
        settings.addParameter(pedalMode);

        // allow Parameters to be stored in config
        faderMode.storeInConfig(true);
        encoderMode.storeInConfig(true);
        faderBankTrack.storeInConfig(true);
        faderBankSend.storeInConfig(true);
        faderBankFree1.storeInConfig(true);
        faderBankFree2.storeInConfig(true);
        faderBankMidi1.storeInConfig(true);
        faderBankMidi2.storeInConfig(true);
        triggerButtonMode.storeInConfig(true);
        menuMode.storeInConfig(true);
        midi14Bit.storeInConfig(true);
        pedalMode.storeInConfig(true);
        faderBankFx1.storeInConfig(true);
        faderBankFx2.storeInConfig(true);


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Conditions
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Shift On
        Condition shiftOn = Condition.add(shift, 1);
        Condition.clear();

        // Shift Off
        Condition shiftOff = Condition.add(shift, 0);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Foot Pedal
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~


        Condition.add(pedalMode, 0);
        new InterpreterLock("VE Foot Pedal");
        VirtualElement.last.addSource(buttonPedal);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.play));
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.play));
        Condition.clear();

        Condition.add(pedalMode, 1);
        new InterpreterDirect("VE Foot Pedal Sustain");
        VirtualElement.last.addSource(buttonPedal);
        VirtualElement.last.addAction(new SendMidiDirect(0, new int[]{0xB0, 64}));
        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Shift Button
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Shift Button -> Parameter Change
        new InterpreterLatch("VE Shift");
        VirtualElement.last.addSource(buttonShift);
        VirtualElement.last.addAction(new ParameterToggle(shift, 1));
        VirtualElement.last.addAction(new ParameterSet(shift, 0));

        // Shift LED (Active on Shift Activation)
        Condition.set(shiftOn);
        new InterpreterDirect("VE Shift LED");
        VirtualElement.last.addTarget(ledButtonShift);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Transport
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift Off :::

        Condition.set(shiftOff);

        // Play, Record, Loop (Feedback)
        FaderPortV2Macros.addFeedbackControlButton("Play", buttonPlay, ledButtonPlay, 1, OscAddress.play, 0);
        FaderPortV2Macros.addFeedbackControlButton("Record", buttonRecord, ledButtonRecord, 1, OscAddress.record, 0);
        FaderPortV2Macros.addFeedbackControlButton("Toggle Repeat", buttonLoop, ledButtonLoop, 1, OscAddress.toggleRepeat, 0);

        // Undo, Redo, Save
        FaderPortV2Macros.addSimpleControlButton("Undo", buttonRewind, ledButtonRewind, OscAddress.undo, 0);
        FaderPortV2Macros.addSimpleControlButton("Redo", buttonForward, ledButtonForward, OscAddress.redo, 0);
        FaderPortV2Macros.addSimpleControlButton("Save", buttonStop, ledButtonStop, OscAddress.save, 0);

        Condition.clear();

        // ::: Shift On :::

        // Auto Scroll Toggle
        Condition.set(shiftOn);
        FaderPortV2Macros.addFeedbackControlButton("Toggle Auto-Scroll", buttonLoop, ledButtonLoop, 1, OscAddress.toggleAutoScroll, 0);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          FX Bypass
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        Condition.set(shiftOn);

        // Freeze / Unfreeze
        FaderPortV2Macros.addSimpleControlButton("UnFreeze", buttonRewind, ledButtonRewind, OscAddress.trackUnfreeze, 0);
        FaderPortV2Macros.addSimpleControlButton("Freeze", buttonForward, ledButtonForward, OscAddress.trackFreeze, 0);

        // Auto-Glue
        FaderPortV2Macros.addSimpleControlButton("Auto-Glue", buttonStop, ledButtonStop, OscAddress.autoGlue, 0);


        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Track Settings
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift Off :::

        Condition.set(shiftOff);

        // Toggle Track Mute
        new InterpreterDirect("Track Toggle Mono");
        VirtualElement.last.addSource(buttonMute);
        VirtualElement.last.addTarget(ledButtonMute);
        VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.trackMute + "/0"));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackMute + "/0");

        // Toggle Track Solo
        new InterpreterDirect("Track Toggle Solo");
        VirtualElement.last.addSource(buttonSolo);
        VirtualElement.last.addTarget(ledButtonSolo);
        VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.trackSolo + "/0"));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackSolo + "/0");

        // Toggle Track Auto-Arm
        new InterpreterDirect("Track Auto-Arm");
        VirtualElement.last.addSource(buttonArm);
        VirtualElement.last.addTarget(ledButtonArm);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackToggleRecordArm));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackArm + "/0");

        // ::: Shift On :::

        Condition.set(shiftOn);

        // Clear Mute
        new InterpreterBuffered("Track UnMute", true, false);
        VirtualElement.last.addSource(buttonMute);
        VirtualElement.last.addTarget(ledButtonMute);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackUnmuteAll));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackMuteClear);

        // Clear Solo
        new InterpreterBuffered("Track UnSolo", true, false);
        VirtualElement.last.addSource(buttonSolo);
        VirtualElement.last.addTarget(ledButtonSolo);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackUnSoloAll));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackSoloClear);

        // Clear Rec-Arm
        new InterpreterBuffered("Track Rec Arm Clear", true, false);
        VirtualElement.last.addSource(buttonArm);
        VirtualElement.last.addTarget(ledButtonArm);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackToggleRecordArmAuto));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackArm + "/0");

        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Tap Tempo
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        // Record Button -> Tap Tempo
        Condition.set(shiftOn);
        new InterpreterDirect("Tap Tempo");
        VirtualElement.last.addSource(buttonPlay);
        VirtualElement.last.addTarget(ledButtonPlay);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.tapTempo));
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Record Scope
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        Condition.set(shiftOn);
        Condition.add(menuMode, 1);

        // Record Scope Free
        new InterpreterDirect("Record Scope Normal");
        VirtualElement.last.addSource(buttonPan);
        VirtualElement.last.addTarget(ledButtonPan);
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.recordScopeFree);
        VirtualElement.last.setParameterValue(1, 1, 0, 0);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.recordScopeFree));

        // Record Scope Time
        new InterpreterDirect("Record Scope Normal");
        VirtualElement.last.addSource(buttonChannel);
        VirtualElement.last.addTarget(ledButtonChannel);
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.recordScopeTime);
        VirtualElement.last.setParameterValue(1, 1, 0, 0);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.recordScopeTime));

        // Record Scope Item
        new InterpreterDirect("Record Scope Normal");
        VirtualElement.last.addSource(buttonScroll);
        VirtualElement.last.addTarget(ledButtonScroll);
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.recordScopeItem);
        VirtualElement.last.setParameterValue(1, 1, 0, 0);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.recordScopeItem));
        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Menu Selection
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        Condition.set(shiftOn);
        FaderPortV2Macros.addModeButton("Menu Mode Settings", shift, buttonMaster, ledButtonMaster, menuMode, 0);
        FaderPortV2Macros.addModeButton("Menu Mode Record Scope", shift, buttonClick, ledButtonClick, menuMode, 1);
        FaderPortV2Macros.addModeButton("Menu Mode Automation", shift, buttonSection, ledButtonSection, menuMode, 2);
        FaderPortV2Macros.addModeButtonDouble("Menu Mode Free", shift, buttonMarker, ledButtonMarker, menuMode, 4, 5);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          MIDI Trigger (Lower Row under Encoder)
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        Condition.set(shiftOn);

        // Osc Triggers
        Condition.add(menuMode, 4);
        FaderPortV2Macros.addOscButton("Osc Trigger 1", buttonLink, ledButtonLink, "/free/sw/0", 0x00007F);
        FaderPortV2Macros.addOscButton("Osc Trigger 2", buttonPan, ledButtonPan, "/free/sw/1", 0x00007F);
        FaderPortV2Macros.addOscButton("Osc Trigger 3", buttonChannel, ledButtonChannel, "/free/sw/2", 0x00007F);
        FaderPortV2Macros.addOscButton("Osc Trigger 4", buttonScroll, ledButtonScroll, "/free/sw/3", 0x00007F);

        Condition.back();


        // Midi Triggers
        Condition.add(menuMode, 5);
        FaderPortV2Macros.addMidiButton("MIDI Trigger 1", buttonLink, ledButtonLink, 176, MidiAddress.surfaceMidiModeSwitch[0], 0x10207F);
        FaderPortV2Macros.addMidiButton("MIDI Trigger 2", buttonPan, ledButtonPan, 176, MidiAddress.surfaceMidiModeSwitch[1], 0x10207F);
        FaderPortV2Macros.addMidiButton("MIDI Trigger 3", buttonChannel, ledButtonChannel, 176, MidiAddress.surfaceMidiModeSwitch[2], 0x10207F);
        FaderPortV2Macros.addMidiButton("MIDI Trigger 4", buttonScroll, ledButtonScroll, 176, MidiAddress.surfaceMidiModeSwitch[3], 0x10207F);

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Automation Modes
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::
        Condition.set(shiftOn);

        // Toggle Track Envelope Arm
        new InterpreterDirect("Record Arm Track Envelopes");
        VirtualElement.last.addSource(buttonWrite);
        VirtualElement.last.addTarget(ledButtonWrite);
        VirtualElement.last.setParameterValue(1, 1, 0.1, 0);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.automationArmTrackEnvelopes));

        // Write Envelope Data to End of Project
        new InterpreterDirect("Write Envelope Data to End");
        VirtualElement.last.addSource(buttonRead);
        VirtualElement.last.addTarget(ledButtonRead);
        VirtualElement.last.setParameterValue(1, 1, 0.4, 0.25);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.automationWriteToEnd));


        Condition.add(menuMode, 2);
        // Toggle Envelope Auto Add
        new InterpreterDirect("Envelope Auto-Add Toggle");
        VirtualElement.last.addSource(buttonLink);
        VirtualElement.last.addTarget(ledButtonLink);
        VirtualElement.last.setParameterValue(1, 0.25, 1, 0.3);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.automationToggleAutoAddEnvelopes));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.automationToggleAutoAddEnvelopes);
        VirtualElement.last.setParameterValue(1);

        // Automation Overwrite Off
        new InterpreterDirect("Automation Off");
        VirtualElement.last.addSource(buttonScroll);
        VirtualElement.last.addTarget(ledButtonScroll);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.automationModeFree));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.automationModeFree);
        VirtualElement.last.setParameterValue(1, 0.8, 0.55, 0.55);

        // Automation Overwrite Latch (DoubleClick -> Preview)
        new InterpreterDoubleClick("Automation Latch (Preview)");
        VirtualElement.last.addSource(buttonChannel);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.automationModeLatch));
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.automationModeLatchPreview));

        // ~ ~ ~ Latch ~ ~ ~

        // Latch -> Feedback to Parameter
        new InterpreterDirect("Latch Feedback");
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.automationModeLatch);
        Parameter parameterLatch = VirtualElement.last.parameter;

        // Latch Preview -> Feedback to Parameter
        new InterpreterDirect("Latch Preview Feedback");
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.automationModeLatchPreview);
        Parameter parameterLatchPreview = VirtualElement.last.parameter;

        // LED -> Latch On
        Condition.add(parameterLatch, 1);
        new InterpreterDirect("Latch Light");
        VirtualElement.last.addTarget(ledButtonChannel);
        VirtualElement.last.setParameterValue(1, 1, 0.9, 0);
        Condition.back();

        // LED -> Latch Preview On
        Condition.add(parameterLatchPreview, 1);
        new InterpreterDirect("Latch Light");
        VirtualElement.last.addTarget(ledButtonChannel);
        VirtualElement.last.setParameterValue(1, 1, 0.5, 0);
        Condition.back();

        // LED -> Latch Off (dim)
        new InterpreterDirect("Latch Light");
        VirtualElement.last.addTarget(ledButtonChannel);
        VirtualElement.last.setParameterValue(1, 0.1, 0.1, 0);

        // ~ ~ ~ Latch End ~ ~ ~

        // Automation Overwrite Touch
        new InterpreterDirect("Automation Touch");
        VirtualElement.last.addSource(buttonPan);
        VirtualElement.last.addTarget(ledButtonPan);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.automationModeTouch));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.automationModeTouch);
        VirtualElement.last.setParameterValue(1, 0.1, 0.1, 1);

        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Audio Settings
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        Condition.set(shiftOn);

        // Toggle Master Mono
        FaderPortV2Macros.addFeedbackControlButton("Master Mono Toggle", buttonBypass, ledButtonBypass, 1, OscAddress.masterToggleMono, 0);

        // Toggle Solo in Front
        FaderPortV2Macros.addFeedbackControlButton("Solo in Front", buttonTouch, ledButtonTouch, 1, OscAddress.toggleSoloInFront, 0x7F5000);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Settings
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        Condition.set(shiftOn);

        // Toggle MIDI 14 Bit

        double[] clrMenu = {0.2, 1, 0.4};

        Condition.add(menuMode, 0);
        new InterpreterDirect("Midi 14 Bit Toggle");
        VirtualElement.last.addSource(buttonPan);
        VirtualElement.last.addAction(new ParameterToggle(midi14Bit, 1));

        Condition.add(midi14Bit, 1);
        new InterpreterDirect("Midi 14 Bit Toggle LED");
        VirtualElement.last.addTarget(ledButtonPan);
        VirtualElement.last.setParameterValue(1, clrMenu[0] * 0.1, clrMenu[1] * 0.1, clrMenu[2] * 0.1);
        Condition.back();

        Condition.add(midi14Bit, 0);
        new InterpreterDirect("Midi 14 Bit Toggle LED");
        VirtualElement.last.addTarget(ledButtonPan);
        VirtualElement.last.setParameterValue(1, clrMenu[0], clrMenu[1], clrMenu[2]);
        Condition.back();


        // Sustain Pedal Mode

        double[] clrSustain = {0.2, 0.15, 1};

        Condition.add(menuMode, 0);
        new InterpreterDirect("Sustain Mode Toggle");
        VirtualElement.last.addSource(buttonScroll);
        VirtualElement.last.addAction(new ParameterToggle(pedalMode, 1));

        Condition.add(pedalMode, 0);
        new InterpreterDirect("Sustain Mode Toggle LED");
        VirtualElement.last.addTarget(ledButtonScroll);
        VirtualElement.last.setParameterValue(1, clrSustain[0] * 0.1, clrSustain[1] * 0.1, clrSustain[2] * 0.1);
        Condition.back();


        Condition.add(pedalMode, 1);
        new InterpreterDirect("Sustain Mode Toggle LED");
        VirtualElement.last.addTarget(ledButtonScroll);
        VirtualElement.last.setParameterValue(1, clrSustain[0], clrSustain[1], clrSustain[2]);
        Condition.back();

        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Extra Features
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        // Bounce
        Condition.set(shiftOn);
        new InterpreterHold("Bounce");
        VirtualElement.last.addSource(buttonRecord);
        VirtualElement.last.addTarget(ledButtonRecord);
        VirtualElement.last.addAction(new SendOscDirect(1, OscAddress.trackBounceStereo));
        VirtualElement.last.addAction(new SendOscDirect(1, OscAddress.trackBounceStereo2ndPass));
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Encoder
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Push -> Parameter Change
        Condition.clear();
        new InterpreterDirect("VE Encoder Push");
        VirtualElement.last.addSource(encoderButton);
        VirtualElement.last.addAction(new ParameterSetReturn(encoderPush, 1));
        Condition.clear();

        // Encoder Modes
        Condition.set(shiftOff);
        FaderPortV2Macros.addModeButton("Encoder Mode 1", shift, buttonBypass, ledButtonBypass, encoderMode, 0);
        FaderPortV2Macros.addModeButton("Encoder Mode 2", shift, buttonTouch, ledButtonTouch, encoderMode, 1, 0x00007F);
        FaderPortV2Macros.addModeButton("Encoder Mode 3", shift, buttonWrite, ledButtonWrite, encoderMode, 2, 0x00007F, deviceDaw.trackData.tracks.get(0));
        FaderPortV2Macros.addModeButton("Encoder Mode 4", shift, buttonRead, ledButtonRead, encoderMode, 3, 0x00007F, deviceDaw.trackData.tracks.get(0));
        Condition.clear();

        // --- Encoder Functionality ---

        FaderPortV2Macros.addEncoderSet("Project Navigation", encoderPush, encoderMode, 0, OscAddress.gridHalf, OscAddress.gridDouble, OscAddress.jogGrid, OscAddress.zoomHorizontal, 0.2, 0.2);
        FaderPortV2Macros.addEncoderSet("Time Selection", encoderPush, encoderMode, 1, OscAddress.goToPreviousMarker, OscAddress.goToNextMarker, OscAddress.selectionSize, OscAddress.selectionMoveRelative, 0.2, 0.2);
        FaderPortV2Macros.addEncoderSet("Track Selection", encoderPush, encoderMode, 2, OscAddress.dynamicZoomIn, OscAddress.dynamicZoomOut, OscAddress.trackSelectRelative, OscAddress.automationSelectTrackEnvelopeRelative, 0.2, 0.2);
        FaderPortV2Macros.addEncoderSet("Item Selection", encoderPush, encoderMode, 3, OscAddress.takeSelectPrevious, OscAddress.takeSelectNext, OscAddress.itemSelectRelative, "", 0.2, 0.2);


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Encoder Knob Double Click
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Encoder Mode 4 :::

        Condition.add(encoderMode, 3);

        // Remove Take / Crop to Take
        new InterpreterHold("Crop to Take");
        VirtualElement.last.addSource(encoderButton);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.takeRemove));
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.takeCrop));

        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Motorized Fader
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~


        // ::: Shift Off :::

        // Fader Mode Switches
        Condition.set(shiftOff);
        FaderPortV2Macros.addFaderModeButtonDouble("Fader Mode Track", shift, buttonMaster, ledButtonMaster, faderMode, 0, 1, OscAddress.faderModeMix);
        FaderPortV2Macros.addFaderModeButtonDouble("Fader Mode Send", shift, buttonClick, ledButtonClick, faderMode, 6, 7, OscAddress.faderModeFX);
        FaderPortV2Macros.addFaderModeButtonDouble("Fader Mode Free", shift, buttonSection, ledButtonSection, faderMode, 2, 3, OscAddress.faderModeFree);
        FaderPortV2Macros.addFaderModeButtonDouble("Fader Mode Midi", shift, buttonMarker, ledButtonMarker, faderMode, 4, 5, OscAddress.faderModeMidi);

        Condition.clear();


        // Fader Bank Elements
        InputElement[] bankButtons = {buttonLink, buttonPan, buttonChannel, buttonScroll};
        OutputElement[] bankLEDs = {ledButtonLink, ledButtonPan, ledButtonChannel, ledButtonScroll};

        // Fader Bank Switches
        FaderPortV2Macros.addFaderBankButtonSet("Fader Mode Track", shift, bankButtons, bankLEDs, faderMode, 0, faderBankTrack, 0x7F2A00);
        FaderPortV2Macros.addFaderBankButtonSet("Fader Mode Send", shift, bankButtons, bankLEDs, faderMode, 1, faderBankSend, 0x7F5830);
        FaderPortV2Macros.addFaderBankButtonSet("Fader Mode Fx 1", shift, bankButtons, bankLEDs, faderMode, 6, faderBankFx1, 0x007F00);
        FaderPortV2Macros.addFaderBankButtonSet("Fader Mode Fx 2", shift, bankButtons, bankLEDs, faderMode, 7, faderBankFx2, 0x307F20);
        FaderPortV2Macros.addFaderBankButtonSet("Fader Mode Free 1", shift, bankButtons, bankLEDs, faderMode, 2, faderBankFree1, 0x00007F);
        FaderPortV2Macros.addFaderBankButtonSet("Fader Mode Free 2", shift, bankButtons, bankLEDs, faderMode, 3, faderBankFree2, 0x1A1A7F);
        FaderPortV2Macros.addFaderBankButtonSet("Fader Mode Midi 1", shift, bankButtons, bankLEDs, faderMode, 4, faderBankMidi1, 0x00007F);
        FaderPortV2Macros.addFaderBankButtonSet("Fader Mode Midi 2", shift, bankButtons, bankLEDs, faderMode, 5, faderBankMidi2, 0x1A1A7F);

        // Motor Faders
        FaderPortV2Macros.addBankFader("Fader Track", fader, midi14Bit, faderMode, 0, faderBankTrack, "Mix");
        FaderPortV2Macros.addBankFader("Fader Send", fader, midi14Bit, faderMode, 1, faderBankSend, "Send");
        FaderPortV2Macros.addBankFader("Fader FX 1", fader, midi14Bit, faderMode, 6, faderBankFx1, "FX 1");
        FaderPortV2Macros.addBankFader("Fader FX 2", fader, midi14Bit, faderMode, 7, faderBankFx2, "FX 2");
        FaderPortV2Macros.addBankFader("Fader Free 1", fader, midi14Bit, faderMode, 2, faderBankFree1, "Free 1");
        FaderPortV2Macros.addBankFader("Fader Free 2", fader, midi14Bit, faderMode, 3, faderBankFree2, "Free 2");
        FaderPortV2Macros.addBankFader("Fader Midi 1", fader, midi14Bit, faderMode, 4, faderBankMidi1, "Midi 1");
        FaderPortV2Macros.addBankFader("Fader Midi 2", fader, midi14Bit, faderMode, 5, faderBankMidi2, "Midi 2");

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Motor Fader Controller
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Controller
        MotorFaderController mf = new MotorFaderController("Motor Fader Controller");

        // add Fader
        mf.addFader((FaderMotorized) fader);

        // ::: Shift On :::

        // Stop Button -> Toggle Touch Data Bypass
        Condition.set(shiftOn);
        Condition.add(menuMode, 0);
        MfcSingle touchControl = new MfcSingle("Touch Bypass 0", mf, true);
        touchControl.addSource(buttonLink);
        touchControl.addTarget(ledButtonLink);
        touchControl.setParameterValue(1, 0.7, 0.2, 0.9);
        Condition.clear();

        // add Touch Control to Motor Fader Controller
        mf.addTouchSingle(touchControl);

        // link Motor Fader Controller To Settings
        mf.linkToSettings(settings);


    }

}

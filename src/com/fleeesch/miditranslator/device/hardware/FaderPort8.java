package com.fleeesch.miditranslator.device.hardware;

import com.fleeesch.miditranslator.Main;
import com.fleeesch.miditranslator.action.Action;
import com.fleeesch.miditranslator.action.midi.SendMidiDirect;
import com.fleeesch.miditranslator.action.osc.SendOscDirect;
import com.fleeesch.miditranslator.action.osc.SendOscOnPress;
import com.fleeesch.miditranslator.action.osc.SendOscRelative;
import com.fleeesch.miditranslator.action.osc.SendOscRelative2;
import com.fleeesch.miditranslator.action.parameter.ParameterSet;
import com.fleeesch.miditranslator.action.parameter.ParameterSetReturn;
import com.fleeesch.miditranslator.action.parameter.ParameterToggle;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.device.macros.FaderPort8Macros;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.button.Button;
import com.fleeesch.miditranslator.element.input.encoder.EncoderRelative3;
import com.fleeesch.miditranslator.element.input.fader.FaderMotorized;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.output.display.fp8.FP8DisplaySet;
import com.fleeesch.miditranslator.element.output.display.fp8.FP8DisplayUnit;
import com.fleeesch.miditranslator.element.output.led.binary.LedBinary;
import com.fleeesch.miditranslator.element.output.led.rgb.LedRGBFaderPort;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.controller.display.FP8DisplayController;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.MotorFaderController;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.control.MfcMulti;
import com.fleeesch.miditranslator.element.virtual.interpreter.*;
import com.fleeesch.miditranslator.functions.math.Color;

import java.util.ArrayList;
import java.util.List;


public class FaderPort8 extends Device {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Displays

    public final List<FP8DisplayUnit> display = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Motor Fader

    public final List<InputElement> fader = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Encoder

    public InputElement buttonJogEncoder;
    public InputElement buttonPanEncoder;

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Buttons

    // --- Channel ---

    public final List<InputElement> buttonMute = new ArrayList<>();
    public final List<InputElement> buttonSolo = new ArrayList<>();
    public final List<InputElement> buttonSelect = new ArrayList<>();

    // --- Pedal ---

    public InputElement buttonPedal;

    // --- Encoder ---

    public InputElement encoderJog;
    public InputElement encoderPan;

    // --- Transport ---

    public InputElement buttonPlay;
    public InputElement buttonRewind;
    public InputElement buttonForward;
    public InputElement buttonStop;
    public InputElement buttonRecord;
    public InputElement buttonLoop;

    // --- Master Section ---

    public InputElement buttonPrev;
    public InputElement buttonNext;

    public InputElement buttonChannel;
    public InputElement buttonZoom;
    public InputElement buttonScroll;
    public InputElement buttonBank;
    public InputElement buttonMaster;
    public InputElement buttonClick;
    public InputElement buttonSection;
    public InputElement buttonMarker;

    public InputElement buttonLatch;
    public InputElement buttonTrim;
    public InputElement buttonOff;
    public InputElement buttonTouch;
    public InputElement buttonWrite;
    public InputElement buttonRead;

    // --- Side Buttons ---

    public InputElement buttonTrack;
    public InputElement buttonEditPlugins;
    public InputElement buttonSends;
    public InputElement buttonPan;

    public InputElement buttonAudio;
    public InputElement buttonVI;
    public InputElement buttonBus;
    public InputElement buttonVCA;
    public InputElement buttonAll;
    public InputElement buttonShiftR;

    public InputElement buttonArm;
    public InputElement buttonSoloClear;
    public InputElement buttonMuteClear;
    public InputElement buttonBypass;
    public InputElement buttonMacro;
    public InputElement buttonLink;
    public InputElement buttonShiftL;

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  LEDs

    // --- Channel ---

    public final List<OutputElement> ledButtonMute = new ArrayList<>();
    public final List<OutputElement> ledButtonSolo = new ArrayList<>();
    public final List<OutputElement> ledButtonSelect = new ArrayList<>();

    // --- Transport ---

    public OutputElement ledButtonPlay;
    public OutputElement ledButtonRewind;
    public OutputElement ledButtonForward;
    public OutputElement ledButtonStop;
    public OutputElement ledButtonRecord;
    public OutputElement ledButtonLoop;

    // --- Encoder ---

    public OutputElement ledButtonPrev;
    public OutputElement ledButtonNext;

    // --- Master Section ---

    public OutputElement ledButtonChannel;
    public OutputElement ledButtonZoom;
    public OutputElement ledButtonScroll;
    public OutputElement ledButtonBank;
    public OutputElement ledButtonMaster;
    public OutputElement ledButtonClick;
    public OutputElement ledButtonSection;
    public OutputElement ledButtonMarker;

    public OutputElement ledButtonLatch;
    public OutputElement ledButtonTrim;
    public OutputElement ledButtonOff;
    public OutputElement ledButtonTouch;
    public OutputElement ledButtonWrite;
    public OutputElement ledButtonRead;

    // --- Side Buttons ---

    public OutputElement ledButtonTrack;
    public OutputElement ledButtonEditPlugins;
    public OutputElement ledButtonSends;
    public OutputElement ledButtonPan;

    public OutputElement ledButtonAudio;
    public OutputElement ledButtonVI;
    public OutputElement ledButtonBus;
    public OutputElement ledButtonVCA;
    public OutputElement ledButtonAll;
    public OutputElement ledButtonShiftR;

    public OutputElement ledButtonArm;
    public OutputElement ledButtonSoloClear;
    public OutputElement ledButtonMuteClear;
    public OutputElement ledButtonBypass;
    public OutputElement ledButtonMacro;
    public OutputElement ledButtonLink;
    public OutputElement ledButtonShiftL;

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Groups / Collectors

    public FP8DisplaySet displaySet;
    public FP8DisplayController displayController;


    //************************************************************
    //      Constructor
    //************************************************************

    public FaderPort8() {

        // name of device
        name = "FaderPort 8";
        vendor = "PreSonus";
        product = "Faderport 8";

        midiInList.add("PreSonus FP8");
        midiOutList.add("PreSonus FP8");

        // faderport system exclusive header
        setSysExHeader(0x00, 0x01, 0x06, 0x02);
        sysexForceOpenClose = true;

        // setup settings
        settings = new Settings("FaderPort 8 Settings");

        setupConfiguration();

    }

    //************************************************************
    //      Method : Setup Device
    //************************************************************

    public void setupDevice() {

        if (initiateSetup()) return;

        // create hardware map
        mapHardware();

        // setup display set
        displaySet = new FP8DisplaySet();
        displayController = new FP8DisplayController("FP8 Display Controller", displaySet, Main.deviceDaw);
        displayController.linkToSettings(settings);

        // create function map
        mapControls();

        // finish setup
        super.setupDevice();

    }

    //************************************************************
    //      Method : Update Output Elements
    //************************************************************

    @Override
    public void updateOutputElements() {

        // super method
        super.updateOutputElements();

        // initialize display formatting by reloading the menu
        if (displayController != null) {
            displayController.reloadMenu();
        }

    }


    //************************************************************
    //      Method : Map Hardware
    //************************************************************

    public void mapHardware() {


        //--- Transport ---

        // Pedal

        buttonPedal = new Button("IE Pedal", 144, 102);

        // Buttons

        buttonPlay = new Button("IE Button Play", 144, 94);
        buttonRecord = new Button("IE Button Record", 144, 95);
        buttonStop = new Button("IE Button Stop", 144, 93);
        buttonRewind = new Button("IE Button Rewind", 144, 91);
        buttonForward = new Button("IE Button Forward", 144, 92);
        buttonLoop = new Button("IE Button Loop", 144, 86);

        // Led

        ledButtonPlay = new LedBinary("OE Button Play", 144, 94);
        ledButtonRecord = new LedBinary("OE Button Record", 144, 95);
        ledButtonStop = new LedBinary("OE Button Stop", 144, 93);
        ledButtonRewind = new LedBinary("OE Button Rewind", 144, 91);
        ledButtonForward = new LedBinary("OE Button Forward", 144, 92);
        ledButtonLoop = new LedBinary("OE Button Loop", 144, 86);


        //--- Encoders ---

        encoderJog = new EncoderRelative3("IE Encoder Jog", 176, 60);
        buttonJogEncoder = new Button("IE Button Encoder Jog", 144, 83);

        encoderPan = new EncoderRelative3("IE Encoder Pan", 176, 16);
        buttonPanEncoder = new Button("IE Button Encoder Pan", 144, 32);

        //--- Master Section ---

        // Button

        buttonPrev = new Button("IE Button Prev", 144, 46);
        buttonNext = new Button("IE Button Next", 144, 47);

        buttonChannel = new Button("IE Button Channel", 144, 54);
        buttonZoom = new Button("IE Button Zoom", 144, 55);
        buttonScroll = new Button("IE Button Scroll", 144, 56);
        buttonBank = new Button("IE Button Bank", 144, 57);

        buttonMaster = new Button("IE Button Master", 144, 58);
        buttonClick = new Button("IE Button Click", 144, 59);
        buttonSection = new Button("IE Button Section", 144, 60);
        buttonMarker = new Button("IE Button Marker", 144, 61);

        buttonLatch = new Button("IE Button Latch", 144, 78);
        buttonTrim = new Button("IE Button Latch", 144, 76);
        buttonOff = new Button("IE Button Latch", 144, 79);
        buttonTouch = new Button("IE Button Touch", 144, 77);
        buttonWrite = new Button("IE Button Touch", 144, 75);
        buttonRead = new Button("IE Button Touch", 144, 74);

        // Led

        ledButtonPrev = new LedBinary("OE Button Prev", 144, 46);
        ledButtonNext = new LedBinary("OE Button Next", 144, 47);

        ledButtonChannel = new LedBinary("OE Button Channel", 144, 54);
        ledButtonZoom = new LedBinary("OE Button Zoom", 144, 55);
        ledButtonScroll = new LedBinary("OE Button Scroll", 144, 56);
        ledButtonBank = new LedBinary("OE Button Bank", 144, 57);

        ledButtonMaster = new LedBinary("OE Button Master", 144, 58);
        ledButtonClick = new LedBinary("OE Button Click", 144, 59);
        ledButtonSection = new LedBinary("OE Button Section", 144, 60);
        ledButtonMarker = new LedBinary("OE Button Marker", 144, 61);

        ledButtonLatch = new LedRGBFaderPort("OE Button Latch", 144, 78);
        ledButtonTrim = new LedRGBFaderPort("OE Button Latch", 144, 76);
        ledButtonOff = new LedRGBFaderPort("OE Button Latch", 144, 79);
        ledButtonTouch = new LedRGBFaderPort("OE Button Touch", 144, 77);
        ledButtonWrite = new LedRGBFaderPort("OE Button Touch", 144, 75);
        ledButtonRead = new LedRGBFaderPort("OE Button Touch", 144, 74);

        //--- Side Buttons ---

        // Button

        buttonTrack = new Button("IE Button Track", 144, 40);
        buttonEditPlugins = new Button("IE Button Edit Plugins", 144, 43);
        buttonSends = new Button("IE Button Sends", 144, 41);
        buttonPan = new Button("IE Button Pan", 144, 42);

        buttonAudio = new Button("IE Button Audio", 144, 62);
        buttonVI = new Button("IE Button VI", 144, 63);
        buttonBus = new Button("IE Button Bus", 144, 64);
        buttonVCA = new Button("IE Button VCA", 144, 65);
        buttonAll = new Button("IE Button All", 144, 66);
        buttonShiftR = new Button("IE Button ShiftR", 144, 6);

        buttonArm = new Button("IE Button Arm", 144, 0);
        buttonSoloClear = new Button("IE Button Solo Clear", 144, 1);
        buttonMuteClear = new Button("IE Button Mute Clear", 144, 2);
        buttonBypass = new Button("IE Button Bypass", 144, 3);
        buttonMacro = new Button("IE Button Macro", 144, 4);
        buttonLink = new Button("IE Button Link", 144, 5);
        buttonShiftL = new Button("IE Button ShiftL", 144, 70);

        // Led

        ledButtonTrack = new LedBinary("OE Button Track", 144, 40);
        ledButtonEditPlugins = new LedBinary("OE Button Edit Plugins", 144, 43);
        ledButtonSends = new LedBinary("OE Button Sends", 144, 41);
        ledButtonPan = new LedBinary("OE Button Pan", 144, 42);

        ledButtonAudio = new LedRGBFaderPort("OE Button Audio", 144, 62);
        ledButtonVI = new LedRGBFaderPort("OE Button VI", 144, 63);
        ledButtonBus = new LedRGBFaderPort("OE Button Bus", 144, 64);
        ledButtonVCA = new LedRGBFaderPort("OE Button VCA", 144, 65);
        ledButtonAll = new LedRGBFaderPort("OE Button All", 144, 66);
        ledButtonShiftR = new LedBinary("OE Button ShiftR", 144, 6);

        ledButtonArm = new LedBinary("OE Button Arm", 144, 0);
        ledButtonSoloClear = new LedBinary("OE Button Solo Clear", 144, 1);
        ledButtonMuteClear = new LedBinary("OE Button Mute Clear", 144, 2);
        ledButtonBypass = new LedRGBFaderPort("OE Button Bypass", 144, 3);
        ledButtonMacro = new LedRGBFaderPort("OE Button Macro", 144, 4);
        ledButtonLink = new LedRGBFaderPort("OE Button Link", 144, 5);
        ledButtonShiftL = new LedBinary("OE Button ShiftL", 144, 70);

        //--- Channel ---

        // Button

        buttonMute.add(new Button("IE Button Mute 1", 144, 16));
        buttonMute.add(new Button("IE Button Mute 2", 144, 17));
        buttonMute.add(new Button("IE Button Mute 3", 144, 18));
        buttonMute.add(new Button("IE Button Mute 4", 144, 19));
        buttonMute.add(new Button("IE Button Mute 5", 144, 20));
        buttonMute.add(new Button("IE Button Mute 6", 144, 21));
        buttonMute.add(new Button("IE Button Mute 7", 144, 22));
        buttonMute.add(new Button("IE Button Mute 8", 144, 23));

        buttonSolo.add(new Button("IE Button Solo 1", 144, 8));
        buttonSolo.add(new Button("IE Button Solo 2", 144, 9));
        buttonSolo.add(new Button("IE Button Solo 3", 144, 10));
        buttonSolo.add(new Button("IE Button Solo 4", 144, 11));
        buttonSolo.add(new Button("IE Button Solo 5", 144, 12));
        buttonSolo.add(new Button("IE Button Solo 6", 144, 13));
        buttonSolo.add(new Button("IE Button Solo 7", 144, 14));
        buttonSolo.add(new Button("IE Button Solo 8", 144, 15));

        buttonSelect.add(new Button("IE Button Select 1", 144, 24));
        buttonSelect.add(new Button("IE Button Select 2", 144, 25));
        buttonSelect.add(new Button("IE Button Select 3", 144, 26));
        buttonSelect.add(new Button("IE Button Select 4", 144, 27));
        buttonSelect.add(new Button("IE Button Select 5", 144, 28));
        buttonSelect.add(new Button("IE Button Select 6", 144, 29));
        buttonSelect.add(new Button("IE Button Select 7", 144, 30));
        buttonSelect.add(new Button("IE Button Select 8", 144, 31));

        // Led

        ledButtonMute.add(new LedBinary("OE Button Mute 1", 144, 16));
        ledButtonMute.add(new LedBinary("OE Button Mute 2", 144, 17));
        ledButtonMute.add(new LedBinary("OE Button Mute 3", 144, 18));
        ledButtonMute.add(new LedBinary("OE Button Mute 4", 144, 19));
        ledButtonMute.add(new LedBinary("OE Button Mute 5", 144, 20));
        ledButtonMute.add(new LedBinary("OE Button Mute 6", 144, 21));
        ledButtonMute.add(new LedBinary("OE Button Mute 7", 144, 22));
        ledButtonMute.add(new LedBinary("OE Button Mute 8", 144, 23));

        ledButtonSolo.add(new LedBinary("OE Button Solo 1", 144, 8));
        ledButtonSolo.add(new LedBinary("OE Button Solo 2", 144, 9));
        ledButtonSolo.add(new LedBinary("OE Button Solo 3", 144, 10));
        ledButtonSolo.add(new LedBinary("OE Button Solo 4", 144, 11));
        ledButtonSolo.add(new LedBinary("OE Button Solo 5", 144, 12));
        ledButtonSolo.add(new LedBinary("OE Button Solo 6", 144, 13));
        ledButtonSolo.add(new LedBinary("OE Button Solo 7", 144, 14));
        ledButtonSolo.add(new LedBinary("OE Button Solo 8", 144, 15));

        ledButtonSelect.add(new LedRGBFaderPort("OE Button Select 1", 144, 24));
        ledButtonSelect.add(new LedRGBFaderPort("OE Button Select 2", 144, 25));
        ledButtonSelect.add(new LedRGBFaderPort("OE Button Select 3", 144, 26));
        ledButtonSelect.add(new LedRGBFaderPort("OE Button Select 4", 144, 27));
        ledButtonSelect.add(new LedRGBFaderPort("OE Button Select 5", 144, 28));
        ledButtonSelect.add(new LedRGBFaderPort("OE Button Select 6", 144, 29));
        ledButtonSelect.add(new LedRGBFaderPort("OE Button Select 7", 144, 30));
        ledButtonSelect.add(new LedRGBFaderPort("OE Button Select 8", 144, 31));

        //--- Motor Fader ---

        fader.add(new FaderMotorized("IE Motor Fader 1", 0, 0xE0, 0x90, 104));
        fader.add(new FaderMotorized("IE Motor Fader 2", 0, 0xE1, 0x90, 105));
        fader.add(new FaderMotorized("IE Motor Fader 3", 0, 0xE2, 0x90, 106));
        fader.add(new FaderMotorized("IE Motor Fader 4", 0, 0xE3, 0x90, 107));
        fader.add(new FaderMotorized("IE Motor Fader 5", 0, 0xE4, 0x90, 108));
        fader.add(new FaderMotorized("IE Motor Fader 6", 0, 0xE5, 0x90, 109));
        fader.add(new FaderMotorized("IE Motor Fader 7", 0, 0xE6, 0x90, 110));
        fader.add(new FaderMotorized("IE Motor Fader 8", 0, 0xE7, 0x90, 111));

        for (InputElement e : fader) {
            ((FaderMotorized) e).addFaderSet(fader);
        }

        // rescaling of faders, somehow presonus fucked up the first and last byte
        for (InputElement f : fader) ((FaderMotorized) f).rescaleInput(0.01, 1 - 0.0115);


        //--- Display ---

        display.add(new FP8DisplayUnit(0));
        display.add(new FP8DisplayUnit(1));
        display.add(new FP8DisplayUnit(2));
        display.add(new FP8DisplayUnit(3));
        display.add(new FP8DisplayUnit(4));
        display.add(new FP8DisplayUnit(5));
        display.add(new FP8DisplayUnit(6));
        display.add(new FP8DisplayUnit(7));

    }

    //************************************************************
    //      Method : Map Controls
    //************************************************************

    public void mapControls() {

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Init
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Parameters
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // fader mode
        Parameter faderMode = new Parameter("Fader Mode");

        // individual banks
        Parameter freeModeBank = new Parameter("Free Mode Bank");
        Parameter mixModeBank = new Parameter("Mix Mode Bank");
        Parameter trackModeBank = new Parameter("Track Mode Bank");
        Parameter fxModeBank = new Parameter("FX Mode Bank");
        Parameter midiModeBank = new Parameter("MIDI Mode Bank");
        Parameter presetModeBank = new Parameter("Preset Mode Bank");

        // midi 14 bit toggle
        Parameter midi14Bit = new Parameter("Midi 14-Bit Toggle");

        // bank extension
        Parameter bankExtension = new Parameter("Track Bank");

        // global shift button
        Parameter shift = new Parameter("Shift");

        // encoder push
        Parameter panEncoderPush = new Parameter("Pan Encoder Push");
        Parameter jogEncoderPush = new Parameter("Jog Encoder Push");

        // encoder mode
        Parameter jogEncoderMode = new Parameter("Jog Encoder Mode");

        // pedal Mode
        Parameter pedalMode = new Parameter("Pedal Mode");

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
        //          Settings
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // add Parameters to Setting
        settings.addParameter(faderMode);
        settings.addParameter(freeModeBank);
        settings.addParameter(mixModeBank);
        settings.addParameter(trackModeBank);
        settings.addParameter(fxModeBank);
        settings.addParameter(presetModeBank);
        settings.addParameter(midiModeBank);
        settings.addParameter(shift);
        settings.addParameter(bankExtension);
        settings.addParameter(midi14Bit);
        settings.addParameter(pedalMode);

        // store Parameters in File
        faderMode.storeInConfig(true);
        freeModeBank.storeInConfig(true);
        mixModeBank.storeInConfig(true);
        trackModeBank.storeInConfig(true);
        fxModeBank.storeInConfig(true);
        presetModeBank.storeInConfig(true);
        midiModeBank.storeInConfig(true);
        midi14Bit.storeInConfig(true);
        pedalMode.storeInConfig(true);

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

        // Button
        new InterpreterLatch("VE Shift Button");
        VirtualElement.last.addSource(buttonShiftL);
        VirtualElement.last.addSource(buttonShiftR);
        VirtualElement.last.addAction(new ParameterToggle(shift, 1));
        VirtualElement.last.addAction(new ParameterSet(shift, 0));

        // LED
        Condition.set(shiftOn);
        new InterpreterDirect("VE Shift Button Led");
        VirtualElement.last.addTarget(ledButtonShiftL);
        VirtualElement.last.addTarget(ledButtonShiftR);
        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Transport
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift Off :::

        // Play, Record, Loop (Feedback)
        Condition.set(shiftOff);
        FaderPort8Macros.addFeedbackButton("Play", buttonPlay, ledButtonPlay, 1, OscAddress.play);
        FaderPort8Macros.addFeedbackButton("Rec", buttonRecord, ledButtonRecord, 1, OscAddress.record);
        FaderPort8Macros.addFeedbackButton("Loop", buttonLoop, ledButtonLoop, 1, OscAddress.toggleRepeat);

        // Undo, Redo, Save
        Condition.set(shiftOff);
        FaderPort8Macros.addDawButtonSimple("Undo", buttonRewind, ledButtonRewind, OscAddress.undo);
        FaderPort8Macros.addDawButtonSimple("Redo", buttonForward, ledButtonForward, OscAddress.redo);
        FaderPort8Macros.addDawButtonSimple("Save", buttonStop, ledButtonStop, OscAddress.save);

        // Dynamic Zoom
        FaderPort8Macros.addDawButtonSimple("Dynamic Zoom In", buttonSection, ledButtonSection, OscAddress.dynamicZoomIn);
        FaderPort8Macros.addDawButtonSimple("Dynamic Zoom Out", buttonMarker, ledButtonMarker, OscAddress.dynamicZoomOut);

        // ::: Shift On :::

        Condition.set(shiftOn);

        // Freeze / Unfreeze
        FaderPort8Macros.addDawButtonSimple("Freeze", buttonForward, ledButtonForward, OscAddress.trackFreeze);
        FaderPort8Macros.addDawButtonSimple("Unfreeze", buttonRewind, ledButtonRewind, OscAddress.trackUnfreeze);

        // Auto-Glue
        FaderPort8Macros.addDawButtonSimple("Auto-Glue", buttonStop, ledButtonStop, OscAddress.autoGlue);


        // Bounce
        new InterpreterHold("Bounce");
        VirtualElement.last.addSource(buttonRecord);
        VirtualElement.last.addTarget(ledButtonRecord);
        VirtualElement.last.addAction(new SendOscDirect(1, OscAddress.trackBounceStereo));
        VirtualElement.last.addAction(new SendOscDirect(1, OscAddress.trackBounceStereo2ndPass));
        Condition.clear();


        // Auto-Scroll (Feedback)
        FaderPort8Macros.addFeedbackButton("Loop", buttonLoop, ledButtonLoop, 1, OscAddress.toggleAutoScroll);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          FX
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        Condition.set(shiftOn);

        // Toggle Preset Sync
        FaderPort8Macros.addFeedbackButton("Preset Sync", buttonAll, ledButtonAll, 0, OscAddress.presetSync, 0x20207F);

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Automation
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift Off :::

        // Automation Modes
        Condition.set(shiftOff);
        FaderPort8Macros.addFeedbackButton("Automation Free", buttonOff, ledButtonOff, 1, OscAddress.automationModeFree, 0x404040);
        FaderPort8Macros.addFeedbackButton("Automation Latch", buttonLatch, ledButtonLatch, 1, OscAddress.automationModeLatch, 0x7F6000);
        FaderPort8Macros.addFeedbackButton("Automation Latch Preview", buttonTrim, ledButtonTrim, 1, OscAddress.automationModeLatchPreview, 0x7F6000);
        FaderPort8Macros.addFeedbackButton("Automation Touch", buttonTouch, ledButtonTouch, 1, OscAddress.automationModeTouch, 0x0A0A7F);

        // Write Automation to End of Project
        FaderPort8Macros.addDawButtonSimple("Automation Write To End", buttonWrite, ledButtonWrite, OscAddress.automationWriteToEnd, 0x7F2020);

        // Toggle Envelope Auto-Add
        new InterpreterDirect("Envelope Auto-Add Toggle");
        VirtualElement.last.addSource(buttonRead);
        VirtualElement.last.addTarget(ledButtonRead);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.automationToggleAutoAddEnvelopes));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.automationToggleAutoAddEnvelopes);
        VirtualElement.last.setParameterValue(0, 0.2, 1, 0.5);

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Audio Settings
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift Off :::

        // Toggle Master Mono
        Condition.set(shiftOff);
        FaderPort8Macros.addFeedbackButton("Toggle Master Mono", buttonMaster, ledButtonMaster, 1, OscAddress.masterToggleMono);
        Condition.clear();

        // ::: Shift On :::

        // Toggle Solo in Front
        Condition.set(shiftOn);
        FaderPort8Macros.addFeedbackButton("Solo in Front", buttonMaster, ledButtonMaster, 1, OscAddress.toggleSoloInFront);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Record Scope
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        // Recording Scopes
        Condition.set(shiftOn);
        FaderPort8Macros.addFeedbackButton("Record Scope Free", buttonLatch, ledButtonLatch, 1, OscAddress.recordScopeFree, 0x7F0000);
        FaderPort8Macros.addFeedbackButton("Record Scope Time", buttonTrim, ledButtonTrim, 1, OscAddress.recordScopeTime, 0x07F0000);
        FaderPort8Macros.addFeedbackButton("Record Scope Selection", buttonOff, ledButtonOff, 1, OscAddress.recordScopeItem, 0x7F0000);

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Jog Wheel Settings
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Jog Mode Buttons (Row below Encoder)
        FaderPort8Macros.addEncoderJogModeButton("Encoder Mode 1", jogEncoderMode, 0, buttonChannel, ledButtonChannel);
        FaderPort8Macros.addEncoderJogModeButton("Encoder Mode 2", jogEncoderMode, 1, buttonZoom, ledButtonZoom);
        FaderPort8Macros.addEncoderJogModeButton("Encoder Mode 3", jogEncoderMode, 2, buttonScroll, ledButtonScroll);
        FaderPort8Macros.addEncoderJogModeButton("Encoder Mode 4", jogEncoderMode, 3, buttonBank, ledButtonBank);

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Jog Wheel
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Jog Button Push -> Parameter Change
        new InterpreterDirect("Jog Encoder Push");
        VirtualElement.last.addSource(buttonJogEncoder);
        VirtualElement.last.addAction(new ParameterSetReturn(jogEncoderPush, 1));

        // Encoder Set : Scroll
        FaderPort8Macros.addEncoderJogSet("Scroll", jogEncoderMode, 0, jogEncoderPush, encoderJog, buttonJogEncoder, buttonPrev, ledButtonPrev, buttonNext, ledButtonNext, new SendOscRelative2(1, OscAddress.jogGrid), new SendOscRelative2(1, OscAddress.zoomHorizontal), null, new SendOscOnPress(1, OscAddress.gridHalf), new SendOscOnPress(1, OscAddress.gridDouble), 0.2, 0.2);
        // Encoder Set : Item / Take
        FaderPort8Macros.addEncoderJogSet("Item/Take", jogEncoderMode, 1, jogEncoderPush, encoderJog, buttonJogEncoder, buttonPrev, ledButtonPrev, buttonNext, ledButtonNext, new SendOscRelative2(1, OscAddress.itemSelectRelative), new SendOscRelative2(1, OscAddress.itemAddRelative), null, new SendOscOnPress(1, OscAddress.takeSelectPrevious), new SendOscOnPress(1, OscAddress.takeSelectNext), 0.2, 0.2);
        // Encoder Set : Selection
        FaderPort8Macros.addEncoderJogSet("Selection", jogEncoderMode, 2, jogEncoderPush, encoderJog, buttonJogEncoder, buttonPrev, ledButtonPrev, buttonNext, ledButtonNext, new SendOscRelative2(1, OscAddress.selectionSize), new SendOscRelative2(1, OscAddress.selectionMoveRelative), null, new SendOscOnPress(1, OscAddress.gridHalf), new SendOscOnPress(1, OscAddress.gridDouble), 0.2, 0.2);
        // Encoder Set : Marker
        FaderPort8Macros.addEncoderJogSet("Marker", jogEncoderMode, 3, jogEncoderPush, encoderJog, buttonJogEncoder, buttonPrev, ledButtonPrev, buttonNext, ledButtonNext, new SendOscRelative2(1, OscAddress.jogGrid), null, new SendOscOnPress(1, OscAddress.insertMarkerAtPos), new SendOscOnPress(1, OscAddress.goToPreviousMarker), new SendOscOnPress(1, OscAddress.goToNextMarker), 0.2, 0.2);

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Additional Functions
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // --- Toggle Indexing by Selection ---

        Condition.set(shiftOff);

        new InterpreterDirect("Track Indexing");
        VirtualElement.last.addSource(buttonAll);
        VirtualElement.last.addTarget(ledButtonAll);
        VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.indexTracksBySelection));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.indexTracksBySelection);
        VirtualElement.last.setParameterValue(0, Color.HexToRgbDouble(0x704A00)[0], Color.HexToRgbDouble(0x704A00)[1], Color.HexToRgbDouble(0x704A00)[2], 1);

        Condition.clear();

        // ::: Shift Off :::

        // --- Previous 8 Tracks ---
        new InterpreterDirect("Previous 8 Track");
        VirtualElement.last.addSource(buttonBypass);
        VirtualElement.last.addTarget(ledButtonBypass);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackSelectRelative, 0.5 - 0.065));

        VirtualElement.last.setParameterValue(1, Color.HexToRgbDouble(0x7F3000)[0]);
        VirtualElement.last.setParameterValue(2, Color.HexToRgbDouble(0x7F3000)[1]);
        VirtualElement.last.setParameterValue(3, Color.HexToRgbDouble(0x7F3000)[2]);

        // --- Next 8 Tracks ---
        new InterpreterDirect("Previous 8 Track");
        VirtualElement.last.addSource(buttonMacro);
        VirtualElement.last.addTarget(ledButtonMacro);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackSelectRelative, 0.5 + 0.065));

        VirtualElement.last.setParameterValue(1, Color.HexToRgbDouble(0x7F3000)[0]);
        VirtualElement.last.setParameterValue(2, Color.HexToRgbDouble(0x7F3000)[1]);
        VirtualElement.last.setParameterValue(3, Color.HexToRgbDouble(0x7F3000)[2]);

        // ::: Shift On :::

        // --- Tap Tempo ---
        Condition.set(shiftOn);
        FaderPort8Macros.addDawButtonSimple("Tap Tempo", buttonPlay, ledButtonPlay, OscAddress.tapTempo);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Click Button
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // --> Encoder Mode 0

        // Arm Envelope
        Condition.set(shiftOff);
        Condition.add(jogEncoderMode, 0);
        FaderPort8Macros.addDawButtonSimple("Track Envelope Arm", buttonClick, ledButtonClick, OscAddress.automationArmTrackEnvelopes);
        Condition.clear();

        // --> Encoder Mode 1

        // Shift Off > Remove Take
        Condition.set(shiftOff);
        Condition.add(jogEncoderMode, 1);
        FaderPort8Macros.addDawButtonSimple("Delete Take", buttonClick, ledButtonClick, OscAddress.takeRemove);
        Condition.back();

        // Shift Off > Crop to Take
        Condition.set(shiftOn);
        Condition.add(jogEncoderMode, 1);
        FaderPort8Macros.addDawButtonSimple("Crop Take", buttonClick, ledButtonClick, OscAddress.takeCrop);
        Condition.clear();

        // --> Encoder Mode 2

        // Remove Selection
        Condition.set(shiftOff);
        Condition.add(jogEncoderMode, 2);
        FaderPort8Macros.addDawButtonSimple("Delete Selection", buttonClick, ledButtonClick, OscAddress.selectionRemove);
        Condition.back();

        // --> Encoder Mode 3

        // Remove Marker
        Condition.set(shiftOff);
        Condition.add(jogEncoderMode, 3);
        FaderPort8Macros.addDawButtonSimple("Delete Marker", buttonClick, ledButtonClick, OscAddress.markerRemove);
        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Track Selection Encoder
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Encoder Push -> Parameter Change
        new InterpreterDirect("Track Encoder Push");
        VirtualElement.last.addSource(buttonPanEncoder);
        VirtualElement.last.addAction(new ParameterSetReturn(panEncoderPush, 1));

        // ::: Shift Off :::

        // Select Track
        Condition.add(panEncoderPush, 0);
        new InterpreterDirect("Track Select");
        VirtualElement.last.addSource(encoderPan);
        VirtualElement.last.addAction(new SendOscRelative2(1, OscAddress.trackSelectRelative));
        ((SendOscRelative) Action.last).rescale(0.25, 0.005);
        Condition.clear();

        // Select Track (keep Selection)
        Condition.add(panEncoderPush, 1);
        new InterpreterDirect("Track Select (Keep)");
        VirtualElement.last.addSource(encoderPan);
        VirtualElement.last.addAction(new SendOscRelative2(1, OscAddress.trackAddRelative));
        ((SendOscRelative) Action.last).rescale(0.25, 0.005);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Fader - Additional Functions
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // 8-16 Track Extension Toggle
        FaderPort8Macros.addParameterToggleSwitch(bankExtension, buttonLink, ledButtonLink, 0x704A00);
        Condition.clear();

        // ::: Shift On :::

        Condition.set(shiftOn);

        // MIDI 14-Bit Toggle
        FaderPort8Macros.addParameterToggleSwitch(midi14Bit, buttonBus, ledButtonBus, 0x207020, true);

        // Pedal Mode
        FaderPort8Macros.addParameterToggleSwitch(pedalMode, buttonVCA, ledButtonVCA, 0x20107F);

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Fader Mode
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // :: Mix / Track
        FaderPort8Macros.addFaderModeButtonSetDouble(shift, "Mix", faderMode, 1, 2, buttonPan, ledButtonPan, 0, OscAddress.faderModeMix);
        // :: Preset
        FaderPort8Macros.addFaderModeButtonSet(shift, "Preset", faderMode, 5, buttonEditPlugins, ledButtonEditPlugins, 0, OscAddress.faderModePreset);
        // :: Free
        FaderPort8Macros.addFaderModeButtonSet(shift, "Free", faderMode, 0, buttonSends, ledButtonSends, 0, OscAddress.faderModeFree);
        // :: MIDI
        FaderPort8Macros.addFaderModeButtonSet(shift, "Midi", faderMode, 4, buttonTrack, ledButtonTrack, 0, OscAddress.faderModeMidi);


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Bank Section
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~


        // :: Mix
        FaderPort8Macros.addBankSelectSet(bankExtension, shift, "Mix", faderMode, 1, mixModeBank, buttonSelect, ledButtonSelect, 0x30007F, 0x600030, true);
        // :: Track
        FaderPort8Macros.addBankSelectSet(bankExtension, shift, "Track", faderMode, 2, trackModeBank, buttonSelect, ledButtonSelect, 0x30007F, 0, true);
        // :: Preset
        FaderPort8Macros.addBankSelectSet(bankExtension, shift, "Preset", faderMode, 5, presetModeBank, buttonSelect, ledButtonSelect, 0x00007F, 0, false);
        // :: Free
        FaderPort8Macros.addBankSelectSet(bankExtension, shift, "Free", faderMode, 0, freeModeBank, buttonSelect, ledButtonSelect, 0x007F30, 0, false);
        // :: MIDI
        FaderPort8Macros.addBankSelectSet(bankExtension, shift, "Midi", faderMode, 4, midiModeBank, buttonSelect, ledButtonSelect, 0x007F30, 0, false);


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Switches
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // :: Preset
        FaderPort8Macros.addToggleButtonSet(bankExtension, shift, "Preset", OscAddress.presetAddress, faderMode, 5, presetModeBank, buttonSelect, ledButtonSelect, 0x1A1A7F, 0);
        // :: Free
        FaderPort8Macros.addToggleButtonSet(bankExtension, shift, "Free", OscAddress.freeAddress, faderMode, 0, freeModeBank, buttonSelect, ledButtonSelect, 0x307F40, 0);
        // :: MIDI
        FaderPort8Macros.addToggleButtonSetMidi(bankExtension, shift, "Midi", faderMode, 4, midiModeBank, buttonSelect, ledButtonSelect, 0x307F40, 0);

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Track Mute / Solo Section
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift Off :::

        // Track Mute / Solo Toggles
        Condition.set(shiftOff);
        FaderPort8Macros.addTrackMuteSoloSet(bankExtension, buttonMute, buttonSolo, ledButtonMute, ledButtonSolo);

        // Track Mute Clear
        new InterpreterBuffered("Mute Clear", true, false);
        VirtualElement.last.addSource(buttonMuteClear);
        VirtualElement.last.addTarget(ledButtonMuteClear);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackUnmuteAll));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackMuteClear);


        // Track Solo Clear
        new InterpreterBuffered("Solo Clear", true, false);
        VirtualElement.last.addSource(buttonSoloClear);
        VirtualElement.last.addTarget(ledButtonSoloClear);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackUnSoloAll));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackSoloClear);

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Track Rec Arm
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // Rec Arm Toggles - Mix View
        FaderPort8Macros.addRecArmToggleSet(bankExtension, shift, "Record Arm Toggle (Mix)", faderMode, 1, presetModeBank, buttonSelect, ledButtonSelect, 0x7F0000, 0);
        // Rec Arm Toggles - Track View
        FaderPort8Macros.addRecArmToggleSet(bankExtension, shift, "Record Arm Toggle (Track)", faderMode, 2, presetModeBank, buttonSelect, ledButtonSelect, 0x7F0000, 0);

        // ::: Shift Off :::

        // Auto Rec Arm Toggle
        Condition.set(shiftOff);
        new InterpreterDirect("RecArm Auto Toggle");
        VirtualElement.last.addSource(buttonArm);
        VirtualElement.last.addTarget(ledButtonArm);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackToggleRecordArmAuto));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackArm + "/0");
        Condition.clear();

        // ::: Shift On :::

        // Rec Arm Clear
        Condition.set(shiftOn);
        new InterpreterBuffered("RecArm Clear", true, false);
        VirtualElement.last.addSource(buttonArm);
        VirtualElement.last.addTarget(ledButtonArm);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.trackUnarmAll));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackArmClear);
        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Motor Fader Control
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        // Motor Fader Bypass / Solo
        Condition.set(shiftOn);
        MotorFaderController mf = FaderPort8Macros.addMotorFaderControllerSet(shift, fader, buttonMute, buttonSolo, ledButtonMute, ledButtonSolo, buttonMuteClear, ledButtonMuteClear, buttonSoloClear, ledButtonSoloClear);
        mf.linkToSettings(settings);

        // Bypass Touch Data Button
        mf.addTouchGlobal(new MfcMulti("Fader Touch Bypass Global", mf, true, true));
        VirtualElement.last.addSource(buttonAudio);
        VirtualElement.last.addTarget(ledButtonAudio);
        VirtualElement.last.setParameterValue(1, 0.5, 0, 1);

        // Fader Freeze Button
        mf.addFreezeGlobal(new MfcMulti("Fader Freeze Global", mf, true));
        VirtualElement.last.addSource(buttonVI);
        VirtualElement.last.addTarget(ledButtonVI);
        VirtualElement.last.setParameterValue(1, 0.6, 0, 1);


        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          Motorized Faders
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        for (int b = 0; b < 8; b++) {

            // Mix
            FaderPort8Macros.addFaderSetMix(bankExtension, OscAddress.trackAddress, OscAddress.faderMix, fader, "Fader Mix", b, faderMode, 1, mixModeBank);
            // Track
            FaderPort8Macros.addFaderSetTrack(bankExtension, OscAddress.trackAddress, OscAddress.faderMix, fader, "Fader Track", b, faderMode, 2, trackModeBank);
            // Preset
            FaderPort8Macros.addFaderSet(bankExtension, OscAddress.faderPreset, fader, "Fader Preset", b, faderMode, 5, presetModeBank);
            // Free
            FaderPort8Macros.addFaderSet(bankExtension, OscAddress.faderFree, fader, "Fader Free", b, faderMode, 0, freeModeBank);
            // Midi
            FaderPort8Macros.addFaderSetMidi(bankExtension, fader, "Fader MIDI", b, midiModeBank, faderMode, 4, midi14Bit);

        }


    }

}

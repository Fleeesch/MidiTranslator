package com.fleeesch.miditranslator.device.hardware;

import com.fleeesch.miditranslator.action.midi.SendMidiToDevice;
import com.fleeesch.miditranslator.action.osc.SendOscOnPress;
import com.fleeesch.miditranslator.action.parameter.ParameterSet;
import com.fleeesch.miditranslator.action.parameter.ParameterToggle;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.device.macros.StarlightMacros;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.button.Button;
import com.fleeesch.miditranslator.element.input.fader.Fader14Bit;
import com.fleeesch.miditranslator.element.input.jogwheel.JogWheel;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.output.led.binary.LedBinary;
import com.fleeesch.miditranslator.element.output.led.rgb.LedRGBStarlight;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterDirect;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterDoubleClick;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterLatch;
import com.fleeesch.miditranslator.data.lookup.midi.MidiAddress;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;


public class DJControlStarlight extends Device {

    //************************************************************
    //      Variables
    //************************************************************


    //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    //          INPUT

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Faders / Knobs

    public InputElement crossFader;

    public InputElement[] faderTempoDeck1;
    public InputElement[] faderTempoDeck2;

    public InputElement knobMaster;
    public InputElement knobPhones;

    public InputElement[] knobVolumeDeck1;
    public InputElement[] knobVolumeDeck2;

    public InputElement[] knobBassDeck1;
    public InputElement[] knobBassDeck2;

    public InputElement[] knobFilterDeck1;
    public InputElement[] knobFilterDeck2;

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Jog Wheels

    public InputElement[] jogTouchDeck1;
    public InputElement[] jogTouchDeck2;

    public InputElement[] jogDeck1;
    public InputElement[] jogDeck2;

    public InputElement[] jogSideDeck1;
    public InputElement[] jogSideDeck2;


    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Buttons

    public InputElement buttonFilter;
    public InputElement buttonShift;
    public InputElement[] buttonVinyl;

    public InputElement[] buttonHotCueDeck1;
    public InputElement[] buttonHotCueDeck2;
    public InputElement[] buttonLoopDeck1;
    public InputElement[] buttonLoopDeck2;

    public InputElement[] buttonSyncDeck1;
    public InputElement[] buttonSyncDeck2;
    public InputElement[] buttonCueDeck1;
    public InputElement[] buttonCueDeck2;
    public InputElement[] buttonPlayDeck1;
    public InputElement[] buttonPlayDeck2;

    public InputElement[] buttonListenDeck1;
    public InputElement[] buttonListenDeck2;

    public InputElement[] buttonHotCue1Deck1;
    public InputElement[] buttonHotCue2Deck1;
    public InputElement[] buttonHotCue3Deck1;
    public InputElement[] buttonHotCue4Deck1;
    public InputElement[] buttonLoop1Deck1;
    public InputElement[] buttonLoop2Deck1;
    public InputElement[] buttonLoop3Deck1;
    public InputElement[] buttonLoop4Deck1;
    public InputElement[] buttonFx1Deck1;
    public InputElement[] buttonFx2Deck1;
    public InputElement[] buttonFx3Deck1;
    public InputElement[] buttonFx4Deck1;
    public InputElement[] buttonSampler1Deck1;
    public InputElement[] buttonSampler2Deck1;
    public InputElement[] buttonSampler3Deck1;
    public InputElement[] buttonSampler4Deck1;

    public InputElement[] buttonHotCue1Deck2;
    public InputElement[] buttonHotCue2Deck2;
    public InputElement[] buttonHotCue3Deck2;
    public InputElement[] buttonHotCue4Deck2;
    public InputElement[] buttonLoop1Deck2;
    public InputElement[] buttonLoop2Deck2;
    public InputElement[] buttonLoop3Deck2;
    public InputElement[] buttonLoop4Deck2;
    public InputElement[] buttonFx1Deck2;
    public InputElement[] buttonFx2Deck2;
    public InputElement[] buttonFx3Deck2;
    public InputElement[] buttonFx4Deck2;
    public InputElement[] buttonSampler1Deck2;
    public InputElement[] buttonSampler2Deck2;
    public InputElement[] buttonSampler3Deck2;
    public InputElement[] buttonSampler4Deck2;

    //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    //          OUTPUT

    // Transport
    public OutputElement ledSyncDeck1;
    public OutputElement ledSyncDeck2;
    public OutputElement ledCueDeck1;
    public OutputElement ledCueDeck2;
    public OutputElement ledPlayDeck1;
    public OutputElement ledPlayDeck2;

    // Listen
    public OutputElement ledListenDeck1;
    public OutputElement ledListenDeck2;

    // Vinyl
    public OutputElement ledShift;
    public OutputElement ledVinyl;
    public OutputElement ledFilter;

    // Modes
    public OutputElement ledHotCueDeck1;
    public OutputElement ledHotCueDeck2;
    public OutputElement ledLoopDeck1;
    public OutputElement ledLoopDeck2;
    public OutputElement ledFxDeck1;
    public OutputElement ledFxDeck2;
    public OutputElement ledSamplerDeck1;
    public OutputElement ledSamplerDeck2;

    // Hot Cue
    public OutputElement ledHotCue1Deck1;
    public OutputElement ledHotCue2Deck1;
    public OutputElement ledHotCue3Deck1;
    public OutputElement ledHotCue4Deck1;
    public OutputElement ledHotCue1Deck2;
    public OutputElement ledHotCue2Deck2;
    public OutputElement ledHotCue3Deck2;
    public OutputElement ledHotCue4Deck2;

    // Loop
    public OutputElement ledLoop1Deck1;
    public OutputElement ledLoop2Deck1;
    public OutputElement ledLoop3Deck1;
    public OutputElement ledLoop4Deck1;
    public OutputElement ledLoop1Deck2;
    public OutputElement ledLoop2Deck2;
    public OutputElement ledLoop3Deck2;
    public OutputElement ledLoop4Deck2;

    // Fx
    public OutputElement ledFx1Deck1;
    public OutputElement ledFx2Deck1;
    public OutputElement ledFx3Deck1;
    public OutputElement ledFx4Deck1;
    public OutputElement ledFx1Deck2;
    public OutputElement ledFx2Deck2;
    public OutputElement ledFx3Deck2;
    public OutputElement ledFx4Deck2;

    // Sampler
    public OutputElement ledSampler1Deck1;
    public OutputElement ledSampler2Deck1;
    public OutputElement ledSampler3Deck1;
    public OutputElement ledSampler4Deck1;
    public OutputElement ledSampler1Deck2;
    public OutputElement ledSampler2Deck2;
    public OutputElement ledSampler3Deck2;
    public OutputElement ledSampler4Deck2;

    // Base LED
    public OutputElement ledBottom;

    //************************************************************
    //      Constructor
    //************************************************************

    public DJControlStarlight() {

        super();

        // name of device
        name = "DJControlStarlight";
        vendor = "Hercules";
        product = "DJControlStarlight";

        midiInList.add("DJControl Starlight");
        midiOutList.add("DJControl Starlight");

        // setup settings
        settings = new Settings("Starlight Settings");

        setupConfiguration();

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

        //--- JogWheel ---

        jogTouchDeck1 = new Button[]{new Button("IE Jog Touch Deck 1", 145, 8), new Button("IE Jog Touch Deck 1 Shift", 148, 8)};
        jogTouchDeck2 = new Button[]{new Button("IE Jog Touch Deck 2", 146, 8), new Button("IE Jog Touch Deck 2 Shift", 149, 8)};

        jogDeck1 = new InputElement[2];
        jogDeck2 = new InputElement[2];

        jogSideDeck1 = new InputElement[2];
        jogSideDeck2 = new InputElement[2];

        jogSideDeck1[0] = new JogWheel("IE JogWheel Side Deck 1", 177, 9);
        jogSideDeck1[1] = new JogWheel("IE JogWheel Side Deck 1", 180, 9);

        jogSideDeck2[0] = new JogWheel("IE JogWheel Side Deck 1", 178, 9);
        jogSideDeck2[1] = new JogWheel("IE JogWheel Side Deck 1", 181, 9);

        jogDeck1[0] = new JogWheel("IE JogWheel Deck 1", 177, 10);
        jogDeck1[1] = new JogWheel("IE JogWheel Deck 1", 180, 10);

        jogDeck2[0] = new JogWheel("IE JogWheel Deck 2", 178, 10);
        jogDeck2[1] = new JogWheel("IE JogWheel Deck 2", 181, 10);

        //--- Fader ---

        // CrossFader
        crossFader = new Fader14Bit("IE CrossFader", 0, 176, 0);
        ((Fader14Bit) crossFader).rescaleInput(0.02, 1 - 0.02);

        // Output Volume
        knobMaster = new Fader14Bit("IE Knob Master", 0, 176, 3);
        knobPhones = new Fader14Bit("IE Knob Phones", 0, 176, 4);

        // Tempo
        faderTempoDeck1 = new Fader14Bit[]{new Fader14Bit("IE Fader Tempo Deck 1", 0, 177, 8), new Fader14Bit("IE Fader Tempo Deck 1 Shift", 0, 180, 8)};
        faderTempoDeck2 = new Fader14Bit[]{new Fader14Bit("IE Fader Tempo Deck 2", 0, 178, 8), new Fader14Bit("IE Fader Tempo Deck 2 Shift", 0, 181, 8)};

        // Volume
        knobVolumeDeck1 = new Fader14Bit[]{new Fader14Bit("IE Knob Volume Deck 1", 0, 177, 0), new Fader14Bit("IE Knob Volume Deck 1 Shift", 0, 180, 0)};
        knobVolumeDeck2 = new Fader14Bit[]{new Fader14Bit("IE Knob Volume Deck 2", 0, 178, 0), new Fader14Bit("IE Knob Volume Deck 2 Shift", 0, 181, 0)};

        // Bass
        knobBassDeck1 = new Fader14Bit[]{new Fader14Bit("IE Knob Bass Deck 1", 0, 177, 2), new Fader14Bit("IE Knob Bass Deck 1 Shift", 0, 180, 2)};
        knobBassDeck2 = new Fader14Bit[]{new Fader14Bit("IE Knob Bass Deck 2", 0, 178, 2), new Fader14Bit("IE Knob Bass Deck 2 Shift", 0, 181, 2)};

        // Filter
        knobFilterDeck1 = new Fader14Bit[]{new Fader14Bit("IE Knob Filter Deck 1", 0, 177, 1), new Fader14Bit("IE Knob Filter Deck 1 Shift", 0, 180, 1)};
        knobFilterDeck2 = new Fader14Bit[]{new Fader14Bit("IE Knob Filter Deck 2", 0, 178, 1), new Fader14Bit("IE Knob Filter Deck 2 Shift", 0, 181, 1)};

        //--- Special Buttons ---

        buttonShift = new Button("IE Button Shift", 144, 3);
        buttonFilter = new Button("IE Button Filter", 144, 1);
        buttonVinyl = new Button[]{new Button("IE Button Vinyl", 145, 3), new Button("IE Button Vinyl Shift", 148, 3)};

        //--- Listen ---

        buttonListenDeck1 = new Button[]{new Button("IE Button Listen Deck1", 145, 12), new Button("IE Button Listen Deck1 Shift", 148, 12)};
        buttonListenDeck2 = new Button[]{new Button("IE Button Listen Deck2", 146, 12), new Button("IE Button Listen Deck2 Shift", 149, 12)};

        //--- Macro-Mode ---

        buttonHotCueDeck1 = new Button[]{new Button("IE Button Hot Cue Deck 1", 145, 15), new Button("IE Button Hot Cue Deck 1 Shift", 145, 17)};
        buttonHotCueDeck2 = new Button[]{new Button("IE Button Hot Cue Deck 2", 146, 15), new Button("IE Button Hot Cue Deck 2 Shift", 146, 17)};
        buttonLoopDeck1 = new Button[]{new Button("IE Button Loop Deck 1", 145, 16), new Button("IE Button Loop Deck 1 Shift", 145, 18)};
        buttonLoopDeck2 = new Button[]{new Button("IE Button Loop Deck 2", 146, 16), new Button("IE Button Loop Deck 2 Shift", 146, 18)};

        //--- Hot Cue ---

        // Deck 1
        buttonHotCue1Deck1 = new Button[]{new Button("IE Button Hot Cue 1 Deck 1", 150, 0), new Button("IE Button Hot Cue 1 Deck 1 Shift", 150, 8)};
        buttonHotCue2Deck1 = new Button[]{new Button("IE Button Hot Cue 2 Deck 1", 150, 1), new Button("IE Button Hot Cue 2 Deck 1 Shift", 150, 9)};
        buttonHotCue3Deck1 = new Button[]{new Button("IE Button Hot Cue 3 Deck 1", 150, 2), new Button("IE Button Hot Cue 3 Deck 1 Shift", 150, 10)};
        buttonHotCue4Deck1 = new Button[]{new Button("IE Button Hot Cue 4 Deck 1", 150, 3), new Button("IE Button Hot Cue 4 Deck 1 Shift", 150, 11)};
        // Deck 2
        buttonHotCue1Deck2 = new Button[]{new Button("IE Button Hot Cue 1 Deck 2", 151, 0), new Button("IE Button Hot Cue 1 Deck 2 Shift", 151, 8)};
        buttonHotCue2Deck2 = new Button[]{new Button("IE Button Hot Cue 2 Deck 2", 151, 1), new Button("IE Button Hot Cue 2 Deck 2 Shift", 151, 9)};
        buttonHotCue3Deck2 = new Button[]{new Button("IE Button Hot Cue 3 Deck 2", 151, 2), new Button("IE Button Hot Cue 3 Deck 2 Shift", 151, 10)};
        buttonHotCue4Deck2 = new Button[]{new Button("IE Button Hot Cue 4 Deck 2", 151, 3), new Button("IE Button Hot Cue 4 Deck 2 Shift", 151, 11)};

        //--- Loop ---

        // Deck 1
        buttonLoop1Deck1 = new Button[]{new Button("IE Button Loop 1 Deck 1", 150, 16), new Button("IE Button Loop 1 Deck 1 Shift", 150, 24)};
        buttonLoop2Deck1 = new Button[]{new Button("IE Button Loop 2 Deck 1", 150, 17), new Button("IE Button Loop 2 Deck 1 Shift", 150, 25)};
        buttonLoop3Deck1 = new Button[]{new Button("IE Button Loop 3 Deck 1", 150, 18), new Button("IE Button Loop 3 Deck 1 Shift", 150, 26)};
        buttonLoop4Deck1 = new Button[]{new Button("IE Button Loop 4 Deck 1", 150, 19), new Button("IE Button Loop 4 Deck 1 Shift", 150, 27)};
        // Deck 2
        buttonLoop1Deck2 = new Button[]{new Button("IE Button Loop 1 Deck 2", 151, 16), new Button("IE Button Loop 1 Deck 2 Shift", 151, 24)};
        buttonLoop2Deck2 = new Button[]{new Button("IE Button Loop 2 Deck 2", 151, 17), new Button("IE Button Loop 2 Deck 2 Shift", 151, 25)};
        buttonLoop3Deck2 = new Button[]{new Button("IE Button Loop 3 Deck 2", 151, 18), new Button("IE Button Loop 3 Deck 2 Shift", 151, 26)};
        buttonLoop4Deck2 = new Button[]{new Button("IE Button Loop 4 Deck 2", 151, 19), new Button("IE Button Loop 4 Deck 2 Shift", 151, 27)};

        //--- FX ---

        // Deck 1
        buttonFx1Deck1 = new Button[]{new Button("IE Button FX 1 Deck 1", 150, 32), new Button("IE Button FX 1 Deck 1 Shift", 150, 40)};
        buttonFx2Deck1 = new Button[]{new Button("IE Button FX 2 Deck 1", 150, 33), new Button("IE Button FX 2 Deck 1 Shift", 150, 41)};
        buttonFx3Deck1 = new Button[]{new Button("IE Button FX 3 Deck 1", 150, 34), new Button("IE Button FX 3 Deck 1 Shift", 150, 42)};
        buttonFx4Deck1 = new Button[]{new Button("IE Button FX 4 Deck 1", 150, 35), new Button("IE Button FX 4 Deck 1 Shift", 150, 43)};
        // Deck 2
        buttonFx1Deck2 = new Button[]{new Button("IE Button FX 1 Deck 2", 151, 32), new Button("IE Button FX 1 Deck 2 Shift", 151, 40)};
        buttonFx2Deck2 = new Button[]{new Button("IE Button FX 2 Deck 2", 151, 33), new Button("IE Button FX 2 Deck 2 Shift", 151, 41)};
        buttonFx3Deck2 = new Button[]{new Button("IE Button FX 3 Deck 2", 151, 34), new Button("IE Button FX 3 Deck 2 Shift", 151, 42)};
        buttonFx4Deck2 = new Button[]{new Button("IE Button FX 4 Deck 2", 151, 35), new Button("IE Button FX 4 Deck 2 Shift", 151, 43)};

        //--- Sampler ---

        // Deck 1
        buttonSampler1Deck1 = new Button[]{new Button("IE Button Sampler 1 Deck 1", 150, 48), new Button("IE Button Sampler 1 Deck 1 Shift", 150, 56)};
        buttonSampler2Deck1 = new Button[]{new Button("IE Button Sampler 2 Deck 1", 150, 49), new Button("IE Button Sampler 2 Deck 1 Shift", 150, 57)};
        buttonSampler3Deck1 = new Button[]{new Button("IE Button Sampler 3 Deck 1", 150, 50), new Button("IE Button Sampler 3 Deck 1 Shift", 150, 58)};
        buttonSampler4Deck1 = new Button[]{new Button("IE Button Sampler 4 Deck 1", 150, 51), new Button("IE Button Sampler 4 Deck 1 Shift", 150, 59)};
        // Deck 2
        buttonSampler1Deck2 = new Button[]{new Button("IE Button Sampler 1 Deck 2", 151, 48), new Button("IE Button Sampler 1 Deck 2 Shift", 151, 56)};
        buttonSampler2Deck2 = new Button[]{new Button("IE Button Sampler 2 Deck 2", 151, 49), new Button("IE Button Sampler 2 Deck 2 Shift", 151, 57)};
        buttonSampler3Deck2 = new Button[]{new Button("IE Button Sampler 3 Deck 2", 151, 50), new Button("IE Button Sampler 3 Deck 2 Shift", 151, 58)};
        buttonSampler4Deck2 = new Button[]{new Button("IE Button Sampler 4 Deck 2", 151, 51), new Button("IE Button Sampler 4 Deck 2 Shift", 151, 59)};


        //--- Transport ---

        // Sync
        buttonSyncDeck1 = new Button[]{new Button("IE Button Sync Deck1", 145, 5), new Button("IE Button Sync Deck1 Shift", 148, 5)};
        buttonSyncDeck2 = new Button[]{new Button("IE Button Sync Deck1", 146, 5), new Button("IE Button Sync Deck1 Shift", 149, 5)};

        // Cue
        buttonCueDeck1 = new Button[]{new Button("IE Button Cue Deck1", 145, 6), new Button("IE Button Cue Deck1 Shift", 148, 6)};
        buttonCueDeck2 = new Button[]{new Button("IE Button Cue Deck1", 146, 6), new Button("IE Button Cue Deck1 Shift", 149, 6)};

        // Play
        buttonPlayDeck1 = new Button[]{new Button("IE Button Play Deck1", 145, 7), new Button("IE Button Play Deck1 Shift", 148, 7)};
        buttonPlayDeck2 = new Button[]{new Button("IE Button Play Deck1", 146, 7), new Button("IE Button Play Deck1 Shift", 149, 7)};

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //          OUTPUT

        //--- Transport ---

        ledSyncDeck1 = new LedBinary("OE Led Sync Deck 1", 145, 5);
        ledSyncDeck2 = new LedBinary("OE Led Sync Deck 2", 146, 5);
        ledCueDeck1 = new LedBinary("OE Led Cue Deck 1", 145, 6);
        ledCueDeck2 = new LedBinary("OE Led Cue Deck 2", 146, 6);
        ledPlayDeck1 = new LedBinary("OE Led Play Deck 1", 145, 7);
        ledPlayDeck2 = new LedBinary("OE Led Play Deck 2", 146, 7);

        //--- Listen ---

        ledListenDeck1 = new LedBinary("OE Led Listen Deck 1", 145, 12);
        ledListenDeck2 = new LedBinary("OE Led Listen Deck 2", 146, 12);

        //--- Special Buttons ---

        ledShift = new LedBinary("OE Led Shift", 144, 3);
        ledVinyl = new LedBinary("OE Led Vinyl", 145, 3);

        //--- Mode Set ---

        ledHotCueDeck1 = new LedBinary("OE Led HotCue Deck 1", 145, 15);
        ledHotCueDeck2 = new LedBinary("OE Led HotCue Deck 2", 146, 15);
        ledLoopDeck1 = new LedBinary("OE Led Loop Deck 1", 145, 16);
        ledLoopDeck2 = new LedBinary("OE Led Loop Deck 2", 146, 16);
        ledFxDeck1 = new LedBinary("OE Led FX Deck 1", 145, 17);
        ledFxDeck2 = new LedBinary("OE Led FX Deck 2", 146, 17);
        ledSamplerDeck1 = new LedBinary("OE Led Sampler Deck 1", 145, 18);
        ledSamplerDeck2 = new LedBinary("OE Led Sampler Deck 2", 146, 18);

        //--- Macro-Buttons ---

        // Hot Cue
        ledHotCue1Deck1 = new LedBinary("OE Led HotCue 1 Deck 1", 150, 0);
        ledHotCue2Deck1 = new LedBinary("OE Led HotCue 2 Deck 1", 150, 1);
        ledHotCue3Deck1 = new LedBinary("OE Led HotCue 3 Deck 1", 150, 2);
        ledHotCue4Deck1 = new LedBinary("OE Led HotCue 4 Deck 1", 150, 3);
        ledHotCue1Deck2 = new LedBinary("OE Led HotCue 1 Deck 2", 151, 0);
        ledHotCue2Deck2 = new LedBinary("OE Led HotCue 2 Deck 2", 151, 1);
        ledHotCue3Deck2 = new LedBinary("OE Led HotCue 3 Deck 2", 151, 2);
        ledHotCue4Deck2 = new LedBinary("OE Led HotCue 4 Deck 2", 151, 3);

        // Loop
        ledLoop1Deck1 = new LedBinary("OE Led Loop 1 Deck 1", 150, 16);
        ledLoop2Deck1 = new LedBinary("OE Led Loop 2 Deck 1", 150, 17);
        ledLoop3Deck1 = new LedBinary("OE Led Loop 3 Deck 1", 150, 18);
        ledLoop4Deck1 = new LedBinary("OE Led Loop 4 Deck 1", 150, 19);
        ledLoop1Deck2 = new LedBinary("OE Led Loop 1 Deck 2", 151, 16);
        ledLoop2Deck2 = new LedBinary("OE Led Loop 2 Deck 2", 151, 17);
        ledLoop3Deck2 = new LedBinary("OE Led Loop 3 Deck 2", 151, 18);
        ledLoop4Deck2 = new LedBinary("OE Led Loop 4 Deck 2", 151, 19);

        // FX
        ledFx1Deck1 = new LedBinary("OE Led FX 1 Deck 1", 150, 32);
        ledFx2Deck1 = new LedBinary("OE Led FX 2 Deck 1", 150, 33);
        ledFx3Deck1 = new LedBinary("OE Led FX 3 Deck 1", 150, 34);
        ledFx4Deck1 = new LedBinary("OE Led FX 4 Deck 1", 150, 35);
        ledFx1Deck2 = new LedBinary("OE Led FX 1 Deck 2", 151, 32);
        ledFx2Deck2 = new LedBinary("OE Led FX 2 Deck 2", 151, 33);
        ledFx3Deck2 = new LedBinary("OE Led FX 3 Deck 2", 151, 34);
        ledFx4Deck2 = new LedBinary("OE Led FX 4 Deck 2", 151, 35);

        // Sampler
        ledSampler1Deck1 = new LedBinary("OE Led Sampler 1 Deck 1", 150, 48);
        ledSampler2Deck1 = new LedBinary("OE Led Sampler 2 Deck 1", 150, 49);
        ledSampler3Deck1 = new LedBinary("OE Led Sampler 3 Deck 1", 150, 50);
        ledSampler4Deck1 = new LedBinary("OE Led Sampler 4 Deck 1", 150, 51);
        ledSampler1Deck2 = new LedBinary("OE Led Sampler 1 Deck 2", 151, 48);
        ledSampler2Deck2 = new LedBinary("OE Led Sampler 2 Deck 2", 151, 49);
        ledSampler3Deck2 = new LedBinary("OE Led Sampler 3 Deck 2", 151, 50);
        ledSampler4Deck2 = new LedBinary("OE Led Sampler 4 Deck 2", 151, 51);

        //--- Bottom LED ---


        ledBottom = new LedRGBStarlight("OE Led Bottom", 144, 36, 145, 35, 146, 35);

    }

    //************************************************************
    //      Method : Map Controls
    //************************************************************

    public void mapControls() {

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Parameters
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        //--- Shift ---

        Parameter shift = new Parameter("Shift");

        //--- Global Settings ---

        Parameter sendMidi = new Parameter("Send MIDI");
        Parameter softwareUseMidi = new Parameter("Software use MIDI");

        //--- Jog ---

        Parameter[] jogMode = new Parameter[]{new Parameter("Jog 1 Mode"), new Parameter("Jog 2 Mode")};
        Parameter[] jogTouch = new Parameter[]{new Parameter("Jog 1 Touch"), new Parameter("Jog 2 Touch")};
        Parameter[] jogScratchMode = new Parameter[]{new Parameter("Jog 1 Scratch Mode"), new Parameter("Jog 2 Scratch Mode")};


        //--- Config / Settings ---

        jogMode[0].storeInConfig(true);
        jogMode[1].storeInConfig(true);
        jogTouch[0].storeInConfig(true);
        jogTouch[1].storeInConfig(true);
        jogScratchMode[0].storeInConfig(true);
        jogScratchMode[1].storeInConfig(true);
        sendMidi.storeInConfig(true);
        softwareUseMidi.storeInConfig(true);

        settings.addParameter(jogMode[0]);
        settings.addParameter(jogMode[1]);
        settings.addParameter(jogTouch[0]);
        settings.addParameter(jogTouch[1]);
        settings.addParameter(jogScratchMode[0]);
        settings.addParameter(jogScratchMode[1]);
        settings.addParameter(sendMidi);
        settings.addParameter(softwareUseMidi);

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Conditions
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        //--- Shift On ---

        Condition shiftOn = Condition.add(shift, 1);
        Condition.clear();

        //--- Shift Off ---

        Condition shiftOff = Condition.add(shift, 0);
        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Shift Button
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        new InterpreterLatch("Shift");
        VirtualElement.last.addSource(buttonShift);
        VirtualElement.last.addAction(new ParameterToggle(shift, 1));
        VirtualElement.last.addAction(new ParameterSet(shift, 0));

        Condition.set(shiftOn);
        new InterpreterDirect("LED Shift On");
        VirtualElement.last.addTarget(ledBottom);
        VirtualElement.last.setParameterValue(1);

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Send Midi Button
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        Condition.set(shiftOn);

        new InterpreterLatch("Send Midi");
        VirtualElement.last.addSource(buttonVinyl[0]);
        VirtualElement.last.addSource(buttonVinyl[1]);
        VirtualElement.last.addAction(new ParameterToggle(sendMidi, 1));
        VirtualElement.last.addAction(new ParameterSet(sendMidi, 0));

        Condition.add(sendMidi, 1);
        new InterpreterDirect("LED Send Midi");
        VirtualElement.last.addTarget(ledVinyl);
        VirtualElement.last.setParameterValue(1);

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Transport
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift Off :::

        Condition.set(shiftOff);

        StarlightMacros.addSimpleButton("Undo", buttonHotCue1Deck2, ledHotCue1Deck2, 1, OscAddress.undo);
        StarlightMacros.addSimpleButton("Redo", buttonHotCue2Deck2, ledHotCue2Deck2, 1, OscAddress.redo);
        StarlightMacros.addFeedbackButton("Play", buttonHotCue3Deck2, ledHotCue3Deck2, 1, OscAddress.play, Osc.DawArrange, OscAddress.play);
        StarlightMacros.addFeedbackButton("Record", buttonHotCue4Deck2, ledHotCue4Deck2, 1, OscAddress.record, Osc.DawArrange, OscAddress.record);

        // ::: Shift On :::

        Condition.set(shiftOn);

        StarlightMacros.addFeedbackButton("Repeat", buttonHotCue3Deck2, ledHotCue3Deck2, 1, OscAddress.toggleRepeat, Osc.DawArrange, OscAddress.toggleRepeat);
        StarlightMacros.addFeedbackButton("AutoScroll", buttonHotCue4Deck2, ledHotCue4Deck2, 1, OscAddress.toggleAutoScroll, Osc.DawArrange, OscAddress.toggleAutoScroll);


        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Menu Button Overwrite
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        new InterpreterDirect("Hot Cue Mode");
        VirtualElement.last.addSource(buttonHotCueDeck1[1]);
        VirtualElement.last.addSource(buttonLoopDeck1[0]);
        VirtualElement.last.addSource(buttonLoopDeck1[1]);
        VirtualElement.last.addAction(new SendMidiToDevice(this, 0, new int[]{buttonHotCueDeck1[0].midiAddress1, buttonHotCueDeck1[0].midiAddress2, 0x7F}));

        new InterpreterDirect("Hot Cue Mode");
        VirtualElement.last.addSource(buttonHotCueDeck2[1]);
        VirtualElement.last.addSource(buttonLoopDeck2[0]);
        VirtualElement.last.addSource(buttonLoopDeck2[1]);
        VirtualElement.last.addAction(new SendMidiToDevice(this, 0, new int[]{buttonHotCueDeck2[0].midiAddress1, buttonHotCueDeck2[0].midiAddress2, 0x7F}));


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Special Functions
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift On :::

        Condition.set(shiftOn);

        // Scratch Mode
        StarlightMacros.addScratchModeButtonSet(jogScratchMode[0], new InputElement[][]{buttonSyncDeck1, buttonCueDeck1, buttonPlayDeck1}, new OutputElement[]{ledSyncDeck1, ledCueDeck1, ledPlayDeck1});
        StarlightMacros.addScratchModeButtonSet(jogScratchMode[1], new InputElement[][]{buttonSyncDeck2, buttonCueDeck2, buttonPlayDeck2}, new OutputElement[]{ledSyncDeck2, ledCueDeck2, ledPlayDeck2});

        Condition.clear();

        // ::: Shift Off :::

        Condition.set(shiftOff);

        // Play
        StarlightMacros.addFeedbackButton("Deck Play L", buttonVinyl, ledVinyl, 0, OscAddress.djDeckPlay[0], Osc.DawArrange, OscAddress.djDeckPlay[0]);

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Automation
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift Off :::

        Condition.set(shiftOff);

        StarlightMacros.addFeedbackButton("Automation Off", buttonHotCue1Deck1, ledHotCue1Deck1, 1, OscAddress.automationModeFree, Osc.DawArrange, OscAddress.automationModeFree);
        StarlightMacros.addFeedbackButton("Automation Touch", buttonHotCue3Deck1, ledHotCue3Deck1, 1, OscAddress.automationModeTouch, Osc.DawArrange, OscAddress.automationModeTouch);
        StarlightMacros.addFeedbackButton("Automation Write", buttonHotCue4Deck1, ledHotCue4Deck1, 1, OscAddress.automationModeWrite, Osc.DawArrange, OscAddress.automationModeWrite);

        // ~ ~ ~ Latch ~ ~ ~

        new InterpreterDoubleClick("Automation Latch (Preview)");
        VirtualElement.last.addSource(buttonHotCue2Deck1[0]);
        VirtualElement.last.addSource(buttonHotCue2Deck1[1]);
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.automationModeLatch));
        VirtualElement.last.addAction(new SendOscOnPress(1, OscAddress.automationModeLatchPreview));

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
        VirtualElement.last.addTarget(ledHotCue2Deck1);
        Condition.back();

        // LED -> Latch Preview On
        Condition.add(parameterLatchPreview, 1);
        new InterpreterDirect("Latch Light");
        VirtualElement.last.addTarget(ledHotCue2Deck1);
        Condition.back();

        // ~ ~ ~ Latch End ~ ~ ~

        Condition.clear();

        // ::: Shift On :::

        Condition.set(shiftOn);

        StarlightMacros.addSimpleButton("Envlope Arm", buttonHotCue3Deck1, ledHotCue3Deck1, 1, OscAddress.automationArmTrackEnvelopes);
        StarlightMacros.addFeedbackButton("Envlope Auto-Add", buttonHotCue1Deck1, ledHotCue1Deck1, 1, OscAddress.automationToggleAutoAddEnvelopes, Osc.DawArrange, OscAddress.automationToggleAutoAddEnvelopes);

        StarlightMacros.addSimpleButton("Write Data Until End", buttonHotCue4Deck1, ledHotCue4Deck1, 1, OscAddress.automationWriteToEnd);

        Condition.clear();

        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Faders
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        StarlightMacros.addFader("CrossFader", sendMidi, crossFader, 0, OscAddress.djCrossFader, MidiAddress.djCrossfader, true);

        StarlightMacros.addFader("Deck 1 Tempo", sendMidi, faderTempoDeck1, 0, OscAddress.djDeckTempo[0], MidiAddress.djTempoDeck1, true);
        StarlightMacros.addFader("Deck 2 Tempo", sendMidi, faderTempoDeck2, 0, OscAddress.djDeckTempo[1], MidiAddress.djTempoDeck2, true);

        StarlightMacros.addFader("Deck 1 Knob 1", sendMidi, knobMaster, 0, OscAddress.djDeckFader[0] + "/0", MidiAddress.djFadersDeck1[0], true);
        StarlightMacros.addFader("Deck 1 Knob 2", sendMidi, knobVolumeDeck1, 0, OscAddress.djDeckFader[0] + "/1", MidiAddress.djFadersDeck1[1], true);
        StarlightMacros.addFader("Deck 1 Knob 3", sendMidi, knobBassDeck1, 0, OscAddress.djDeckFader[0] + "/2", MidiAddress.djFadersDeck1[2], true);
        StarlightMacros.addFader("Deck 1 Knob 4", sendMidi, knobFilterDeck1, 0, OscAddress.djDeckFader[0] + "/2", MidiAddress.djFadersDeck1[2], true);

        StarlightMacros.addFader("Deck 2 Knob 1", sendMidi, knobPhones, 0, OscAddress.djDeckFader[1] + "/0", MidiAddress.djFadersDeck2[0], true);
        StarlightMacros.addFader("Deck 2 Knob 2", sendMidi, knobVolumeDeck2, 0, OscAddress.djDeckFader[1] + "/1", MidiAddress.djFadersDeck2[1], true);
        StarlightMacros.addFader("Deck 2 Knob 3", sendMidi, knobBassDeck2, 0, OscAddress.djDeckFader[1] + "/2", MidiAddress.djFadersDeck2[2], true);
        StarlightMacros.addFader("Deck 2 Knob 4", sendMidi, knobFilterDeck2, 0, OscAddress.djDeckFader[1] + "/2", MidiAddress.djFadersDeck2[2], true);


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Jog - Scratch
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // ::: Shift Off :::

        Condition.set(shiftOff);

        StarlightMacros.addJogModeControl(shift, jogMode[0], new InputElement[][]{buttonSyncDeck1, buttonCueDeck1, buttonPlayDeck1}, new OutputElement[]{ledSyncDeck1, ledCueDeck1, ledPlayDeck1});
        StarlightMacros.addJogModeControl(shift, jogMode[1], new InputElement[][]{buttonSyncDeck2, buttonCueDeck2, buttonPlayDeck2}, new OutputElement[]{ledSyncDeck2, ledCueDeck2, ledPlayDeck2});

        StarlightMacros.addScratchFunctionality(shift, jogScratchMode[0], jogMode[0], sendMidi, jogTouch[0], 0, jogDeck1, jogSideDeck1, jogTouchDeck1, 0);
        StarlightMacros.addScratchFunctionality(shift, jogScratchMode[1], jogMode[1], sendMidi, jogTouch[1], 0, jogDeck2, jogSideDeck2, jogTouchDeck2, 1);

        Condition.clear();

        // ::: Shift On :::

        Condition.set(shiftOn);

        StarlightMacros.addTouchModeControl(shift, jogMode[0], 0, jogTouch[0], buttonListenDeck1, ledListenDeck1);
        StarlightMacros.addTouchModeControl(shift, jogMode[1], 0, jogTouch[1], buttonListenDeck2, ledListenDeck2);

        Condition.clear();


        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        //      Jog - DAW Control
        //~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

        // * * * * * * * * * * * * * * * * * *
        //      DECK 1

        // ::: Shift Off :::

        Condition.set(shiftOff);

        // Jog Grid : Deck A
        Condition.add(jogMode[0], 2);
        StarlightMacros.addJogEncoderControl("Jog Grid", jogDeck1, 50, 4, 0.01, 0.01, 1, 1, OscAddress.jogGrid);
        Condition.back();
        // Jog Grid : Deck B
        Condition.add(jogMode[1], 2);
        StarlightMacros.addJogEncoderControl("Jog Grid", jogDeck2, 50, 4, 0.01, 0.01, 1, 1, OscAddress.jogGrid);
        Condition.back();

        // Track Select : Deck A
        Condition.add(jogMode[0], 2);
        StarlightMacros.addJogSideControl("Track Select", jogSideDeck1, 1, OscAddress.trackSelectRelative);
        Condition.back();
        // Track Select : Deck B
        Condition.add(jogMode[1], 2);
        StarlightMacros.addJogSideControl("Track Select", jogSideDeck2, 1, OscAddress.trackSelectRelative);
        Condition.back();

        // Zoom : Deck A
        Condition.add(jogMode[0], 1);
        StarlightMacros.addJogEncoderControl("Zoom", jogDeck1, 50, 4, 0.01, 0.01, 1, 1, OscAddress.zoomHorizontal);
        Condition.back();
        // Zoom : Deck B
        Condition.add(jogMode[1], 1);
        StarlightMacros.addJogEncoderControl("Zoom", jogDeck2, 50, 4, 0.01, 0.01, 1, 1, OscAddress.zoomHorizontal);
        Condition.back();

        // Item Select : Deck A
        Condition.add(jogMode[0], 1);
        StarlightMacros.addJogSideControl("Item Select", jogSideDeck1, 1, OscAddress.itemSelectRelative);
        Condition.back();
        // Item Select : Deck B
        Condition.add(jogMode[1], 1);
        StarlightMacros.addJogSideControl("Item Select", jogSideDeck2, 1, OscAddress.itemSelectRelative);
        Condition.back();


        // ::: Shift On :::

        Condition.set(shiftOn);

        // Selection : Deck A
        Condition.add(jogMode[0], 1);
        StarlightMacros.addJogEncoderControl("Selection", jogDeck1, 50, 4, 0.01, 0.01, 1, 1, OscAddress.selectionSize);
        Condition.back();
        // Selection : Deck B
        Condition.add(jogMode[1], 1);
        StarlightMacros.addJogEncoderControl("Selection", jogDeck2, 50, 4, 0.01, 0.01, 1, 1, OscAddress.selectionSize);
        Condition.back();

        // Track Select (Keep) : Deck A
        Condition.add(jogMode[0], 2);
        StarlightMacros.addJogSideControl("Track Select Keep", jogSideDeck1, 1, OscAddress.trackAddRelative);
        Condition.back();
        // Track Select (Keep) : Deck B
        Condition.add(jogMode[1], 2);
        StarlightMacros.addJogSideControl("Track Select Keep", jogSideDeck2, 1, OscAddress.trackAddRelative);
        Condition.back();

        // Grid : Deck A
        Condition.add(jogMode[0], 2);
        StarlightMacros.addJogEncoderControl("Grid", jogDeck1, 50, 8, 0.01, 0.01, 1, 1, OscAddress.gridRelative);
        Condition.back();
        // Grid : Deck B
        Condition.add(jogMode[1], 2);
        StarlightMacros.addJogEncoderControl("Grid", jogDeck2, 50, 8, 0.01, 0.01, 1, 1, OscAddress.gridRelative);
        Condition.back();

        // Take Select : Deck A
        Condition.add(jogMode[0], 1);
        StarlightMacros.addJogSideControl("Take Select", jogSideDeck1, 1, OscAddress.takeSelectRelative);
        Condition.back();
        // Take Select : Deck B
        Condition.add(jogMode[1], 1);
        StarlightMacros.addJogSideControl("Take Select", jogSideDeck2, 1, OscAddress.takeSelectRelative);
        Condition.back();

    }

}

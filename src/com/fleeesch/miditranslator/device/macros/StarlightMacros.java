package com.fleeesch.miditranslator.device.macros;

import com.fleeesch.miditranslator.action.Action;
import com.fleeesch.miditranslator.action.midi.*;
import com.fleeesch.miditranslator.action.osc.*;
import com.fleeesch.miditranslator.action.parameter.ParameterSet;
import com.fleeesch.miditranslator.action.parameter.ParameterToggle;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.device.hardware.DJControlStarlight;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterBurst;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterDirect;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterJog;
import com.fleeesch.miditranslator.data.lookup.midi.MidiAddress;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;

public class StarlightMacros {

    public static DJControlStarlight device;

    //************************************************************
    //      Method : Setup
    //************************************************************

    public static void setup(DJControlStarlight d) {
        device = d;
    }


    //************************************************************
    //      Add Jog Side control
    //************************************************************

    public static void addJogSideControl(String pName, InputElement[] pJogSide, int pOscPort, String pOscMessage) {

        new InterpreterBurst(pName,200);
        VirtualElement.last.addSource(pJogSide[0]);
        VirtualElement.last.addSource(pJogSide[1]);
        VirtualElement.last.addAction(new SendOscDirect(pOscPort, pOscMessage,0.49));
        VirtualElement.last.addAction(new SendOscDirect(pOscPort, pOscMessage,0.51));


    }

    //************************************************************
    //      Add Jog Encoder Control
    //************************************************************

    public static void addJogEncoderControl(String pName, InputElement[] pJogWheel, int pLatency, int pStep, double pMinVal, double pScalePre, double pScalePost, int pOscPort, String pOscMessage) {

        new InterpreterJog(pName, pLatency, pStep, pMinVal, pScalePre, pScalePost);
        VirtualElement.last.addSource(pJogWheel[0]);
        VirtualElement.last.addSource(pJogWheel[1]);
        VirtualElement.last.addAction(new SendOscRelative2(pOscPort, pOscMessage));

    }

    //************************************************************
    //      Add Scratch Functionality
    //************************************************************

    public static void addScratchFunctionality(Parameter pShift,Parameter pScratchMode,Parameter pMode, Parameter pMidi, Parameter pTouch, int pModeNumber, InputElement[] pJog, InputElement[] pJogSide, InputElement[] pJogTouch, int pDeckIndex) {

        Condition.add(pMode, pModeNumber);

        Condition.add(pScratchMode,0);


        // ::::::::::::::::
        // Scratch
        // ::::::::::::::::

        new InterpreterJog("Jog Deck " + pDeckIndex + "OSC", 0, 0, 0, 1, 1);
        VirtualElement.last.addSource(pJog[0]);
        VirtualElement.last.addSource(pJog[1]);
        VirtualElement.last.addAction(new SendOscRelative(0, OscAddress.djDeckJog[pDeckIndex]));

        new InterpreterJog("Jog Deck Side " + pDeckIndex + "OSC", 0, 0, 0, 1, 1);
        VirtualElement.last.addSource(pJogSide[0]);
        VirtualElement.last.addSource(pJogSide[1]);
        VirtualElement.last.addAction(new SendOscRelative(0, OscAddress.djDeckJogSide[pDeckIndex]));

        Condition.add(pTouch, 1);

        new InterpreterDirect("Jog Deck Touch " + pDeckIndex + "OSC");
        VirtualElement.last.addSource(pJogTouch[0]);
        VirtualElement.last.addSource(pJogTouch[1]);
        VirtualElement.last.addAction(new SendOscDirect(0, OscAddress.djDeckJogTouch[pDeckIndex]));

        Condition.back();

        Condition.add(pMidi, 1);

        new InterpreterJog("Jog Deck " + pDeckIndex + "MIDI", 0, 0, 0, 1, 1);
        VirtualElement.last.addSource(pJog[0]);
        VirtualElement.last.addSource(pJog[1]);
        VirtualElement.last.addAction(new SendMidiRelative(0, MidiAddress.djDeckJog[pDeckIndex]));
        ((SendMidiRelative) Action.last).fixedValue(1);

        new InterpreterJog("Jog Deck Side " + pDeckIndex + "MIDI", 0, 0, 0, 1, 1);
        VirtualElement.last.addSource(pJogSide[0]);
        VirtualElement.last.addSource(pJogSide[1]);
        VirtualElement.last.addAction(new SendMidiRelative(0, MidiAddress.djDeckJogSide[pDeckIndex]));
        ((SendMidiRelative) Action.last).fixedValue(1);

        Condition.add(pTouch, 1);

        new InterpreterDirect("Jog Deck Touch " + pDeckIndex + "MIDI");
        VirtualElement.last.addSource(pJogTouch[0]);
        VirtualElement.last.addSource(pJogTouch[1]);
        VirtualElement.last.addAction(new SendMidiPressRelease(0, MidiAddress.djDeckJogTouch[pDeckIndex]));

        Condition.back();
        Condition.back();
        Condition.back();

        // ::::::::::::::::
        // Cue
        // ::::::::::::::::

        Condition.add(pScratchMode,1);

        new InterpreterDirect("Jog " + pDeckIndex + " Cue");
        VirtualElement.last.addSource(pJogTouch[0]);
        VirtualElement.last.addSource(pJogTouch[1]);
        VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.djDeckJogTouchCue[pDeckIndex],1));
        VirtualElement.last.addAction(new SendOscOnRelease(0, OscAddress.djDeckJogTouchCue[pDeckIndex],0));

        Condition.add(pMidi, 1);

        int[] midiMsgTouchOn = {MidiAddress.djDeckJogTouch[pDeckIndex][0],MidiAddress.djDeckJogTouch[pDeckIndex][1],0x7F};
        int[] midiMsgTouchOff = {MidiAddress.djDeckJogTouch[pDeckIndex][0],MidiAddress.djDeckJogTouch[pDeckIndex][1],0x00};

        new InterpreterDirect("Jog " + pDeckIndex + " Cue MIDI");
        VirtualElement.last.addSource(pJogTouch[0]);
        VirtualElement.last.addSource(pJogTouch[1]);
        VirtualElement.last.addAction(new SendMidiOnPress(0, midiMsgTouchOn));
        VirtualElement.last.addAction(new SendMidiOnRelease(0, midiMsgTouchOff));

        Condition.back();

        Condition.back();


        // ::::::::::::::::
        // Cue Invert
        // ::::::::::::::::

        Condition.add(pScratchMode,2);

        new InterpreterDirect("Jog " + pDeckIndex + " Cue");
        VirtualElement.last.addSource(pJogTouch[0]);
        VirtualElement.last.addSource(pJogTouch[1]);
        VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.djDeckJogTouchCue[pDeckIndex],0));
        VirtualElement.last.addAction(new SendOscOnRelease(0, OscAddress.djDeckJogTouchCue[pDeckIndex],1));

        Condition.add(pMidi, 1);



        new InterpreterDirect("Jog " + pDeckIndex + " Cue MIDI");
        VirtualElement.last.addSource(pJogTouch[0]);
        VirtualElement.last.addSource(pJogTouch[1]);
        VirtualElement.last.addAction(new SendMidiOnPress(0, midiMsgTouchOff));
        VirtualElement.last.addAction(new SendMidiOnRelease(0, midiMsgTouchOn));

        Condition.back();

        Condition.back();

    }


    //************************************************************
    //      Add Jog Mode Control
    //************************************************************

    public static void addJogModeControl(Parameter pShift, Parameter pMode, InputElement[][] pInput, OutputElement[] pOutput) {


        for (int i = 0; i < pInput.length; i++) {


            new InterpreterDirect("JogMode Set");
            VirtualElement.last.addSource(pInput[i][0]);
            VirtualElement.last.addSource(pInput[i][1]);
            VirtualElement.last.addAction(new ParameterSet(pMode, i));

            Condition.add(pMode, i);

            new InterpreterDirect("LED JogMode");
            VirtualElement.last.addTarget(pOutput[i]);

            Condition.back();


        }

    }

    //************************************************************
    //      Add Midi Mode Control
    //************************************************************

    public static void addMidiModeControl(Parameter pShift, Parameter pMode, int pModeNumber, Parameter pMidi, InputElement[] pInput, OutputElement pOutput) {


        Condition.add(pMode, pModeNumber);

        new InterpreterDirect("Jogo Midi Set");
        VirtualElement.last.addSource(pInput[0]);
        VirtualElement.last.addSource(pInput[1]);
        VirtualElement.last.addAction(new ParameterToggle(pMidi, 1));

        Condition.add(pMidi, 1);

        new InterpreterDirect("LED Jog Midi");
        VirtualElement.last.addTarget(pOutput);

        Condition.back();

        Condition.back();


    }

    //************************************************************
    //      Add Touch Mode Control
    //************************************************************

    public static void addTouchModeControl(Parameter pShift, Parameter pMode, int pModeNumber, Parameter pTouch, InputElement[] pInput, OutputElement pOutput) {

        new InterpreterDirect("Jogo Touch Set");
        VirtualElement.last.addSource(pInput[0]);
        VirtualElement.last.addSource(pInput[1]);
        VirtualElement.last.addAction(new ParameterToggle(pTouch, 1));

        Condition.add(pTouch, 1);

        new InterpreterDirect("LED Touch");
        VirtualElement.last.addTarget(pOutput);

        Condition.back();


    }

    //************************************************************
    //      Add Scratch Mode Button Set
    //************************************************************
    public static void addScratchModeButtonSet(Parameter pMode, InputElement[][] pButtons, OutputElement[] pLeds){

        for(int i = 0; i < pButtons.length; i++) {

            new InterpreterDirect("Scratch Mode");
            VirtualElement.last.addSource(pButtons[i][0]);
            VirtualElement.last.addSource(pButtons[i][1]);
            VirtualElement.last.addAction(new ParameterSet(pMode, i));

            Condition.add(pMode, i);

            new InterpreterDirect("Scratch Mode LED");
            VirtualElement.last.addTarget(pLeds[i]);

            Condition.back();

        }

    }

    //************************************************************
    //      Add Fader
    //************************************************************

    public static void addFader(String pName, Parameter pSendMidi, InputElement[] pFader, int pOscPort, String pOscMessage, int[] pMidiMessage, boolean pMidi14Bit) {

        new InterpreterDirect(pName);
        for (InputElement f : pFader) VirtualElement.last.addSource(f);
        VirtualElement.last.addAction(new SendOscAbsolute(pOscPort, pOscMessage));

        Condition.add(pSendMidi, 1);

        new InterpreterDirect(pName);
        for (InputElement f : pFader) VirtualElement.last.addSource(f);
        if (pMidi14Bit) VirtualElement.last.addAction(new SendMidiAbsolute14Bit(0, pMidiMessage));
        else VirtualElement.last.addAction(new SendMidiAbsolute(0, pMidiMessage));

        Condition.back();


    }

    public static void addFader(String pName, Parameter pSendMidi, InputElement pFader, int pOscPort, String pOscMessage, int[] pMidiMessage, boolean pMidi14Bit) {

        addFader(pName, pSendMidi, new InputElement[]{pFader}, pOscPort, pOscMessage, pMidiMessage, pMidi14Bit);

    }


    //************************************************************
    //      Add Simple Button
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * *
    //      Input Array

    public static void addSimpleButton(String pName, InputElement[] pButton, OutputElement pLed, int pOscOutPortIndex, String pOscMessageOut) {

        new InterpreterDirect(pName);
        VirtualElement.last.addSource(pButton[0]);

        if (pButton.length > 1) VirtualElement.last.addSource(pButton[1]);
        if (pLed != null) VirtualElement.last.addTarget(pLed);
        VirtualElement.last.setParameterValue(1);
        VirtualElement.last.addAction(new SendOscOnPress(pOscOutPortIndex, pOscMessageOut));


    }

    //* * * * * * * * * * * * * * * * * * * * * *
    //      Single Input

    public static void addSimpleButton(String pName, InputElement pButton, OutputElement pLed, int pOscOutPortIndex, String pOscMessageOut) {
        addSimpleButton(pName, new InputElement[]{pButton}, pLed, pOscOutPortIndex, pOscMessageOut);
    }

    //************************************************************
    //      Add Feedback Button
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * *
    //      Input Array

    public static void addFeedbackButton(String pName, InputElement[] pButton, OutputElement pLed, int pOscOutPortIndex, String pOscMessageOut, Osc pOscFeedbackPort, String pOscMessageFeedback) {

        new InterpreterDirect(pName);
        VirtualElement.last.addSource(pButton[0]);

        if (pButton.length > 1) VirtualElement.last.addSource(pButton[1]);

        if (pLed != null) VirtualElement.last.addTarget(pLed);

        VirtualElement.last.setParameterValue(1);
        VirtualElement.last.linkToFeedback(pOscFeedbackPort, pOscMessageFeedback);
        VirtualElement.last.addAction(new SendOscOnPress(pOscOutPortIndex, pOscMessageOut));


    }

    //* * * * * * * * * * * * * * * * * * * * * *
    //      Single Input

    public static void addFeedbackButton(String pName, InputElement pButton, OutputElement pLed, int pOscOutPortIndex, String pOscMessageOut, Osc pOscFeedbackPort, String pOscMessageFeedback) {
        addFeedbackButton(pName, new InputElement[]{pButton}, pLed, pOscOutPortIndex, pOscMessageOut, pOscFeedbackPort, pOscMessageFeedback);
    }

}

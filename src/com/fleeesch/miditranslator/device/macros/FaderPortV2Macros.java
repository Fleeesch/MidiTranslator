package com.fleeesch.miditranslator.device.macros;

import com.fleeesch.miditranslator.action.Action;
import com.fleeesch.miditranslator.action.midi.SendMidiAbsolute14Bit;
import com.fleeesch.miditranslator.action.midi.SendMidiDirect;
import com.fleeesch.miditranslator.action.osc.*;
import com.fleeesch.miditranslator.action.parameter.ParameterSet;
import com.fleeesch.miditranslator.action.parameter.ParameterToggle;
import com.fleeesch.miditranslator.data.external.track.TrackData;
import com.fleeesch.miditranslator.data.lookup.midi.MidiAddress;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;
import com.fleeesch.miditranslator.data.midi.Midi;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.device.hardware.FaderPortV2;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.controller.led.LedControllerTrackColor;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterDirect;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterMidi14Bit;
import com.fleeesch.miditranslator.functions.math.Color;

public class FaderPortV2Macros {

    public static FaderPortV2 device;

    //************************************************************
    //      Method : Setup
    //************************************************************

    public static void setup(FaderPortV2 d) {
        device = d;
    }

    //************************************************************
    //      Method : Add Encoder Set
    //************************************************************

    public static void addEncoderSet(String pName, Parameter pEncoderPush, Parameter pEncoderMode, int pEncoderModeNumber, String pOscPrev, String pOscNext, String pOscEncoder, String pOscEncoderPushed, double pRescale1, double pRescale2) {


        Condition.add(pEncoderMode, pEncoderModeNumber);

        if (!pOscPrev.isEmpty()) {

            new InterpreterDirect(pName + " PREV");
            VirtualElement.last.addSource(device.buttonPrev);
            VirtualElement.last.addTarget(device.ledButtonPrev);
            VirtualElement.last.addAction(new SendOscOnPress(1, pOscPrev));

        }

        if (!pOscNext.isEmpty()) {

            new InterpreterDirect(pName + " NEXT");
            VirtualElement.last.addSource(device.buttonNext);
            VirtualElement.last.addTarget(device.ledButtonNext);
            VirtualElement.last.addAction(new SendOscOnPress(1, pOscNext));

        }

        if (!pOscEncoder.isEmpty()) {
            Condition.add(pEncoderPush, 0);

            new InterpreterDirect(pName + " JOG");
            VirtualElement.last.addSource(device.encoderJog);
            VirtualElement.last.addAction(new SendOscRelative2(1, pOscEncoder));
            ((SendOscRelative) Action.last).rescale(pRescale1, 0.005);

            Condition.back();
        }

        if (!pOscEncoderPushed.isEmpty()) {
            Condition.add(pEncoderPush, 1);

            new InterpreterDirect(pName + " JOG PUSHED");
            VirtualElement.last.addSource(device.encoderJog);
            VirtualElement.last.addAction(new SendOscRelative2(1, pOscEncoderPushed));
            ((SendOscRelative) Action.last).rescale(pRescale2, 0.005);

            Condition.back();

        }

        Condition.back();

    }

    //************************************************************
    //      Method : Add Bank Fader
    //************************************************************

    public static void addBankFader(String pName, InputElement pFader, Parameter pMidi14bit, Parameter pFaderMode, int pFaderModeNumber, Parameter pFaderBank, String pType) {

        Condition.add(pFaderMode, pFaderModeNumber);

        for (int i = 0; i < 4; i++) {

            Condition.add(pFaderBank, i);

            String n = pName + " " + pFaderModeNumber + " : " + i;


            if (pType.equals("Mix")) {

                String adr;

                if (i < 3) {
                    adr = OscAddress.trackAddress + "/0" + OscAddress.faderMix[i];
                } else {
                    adr = OscAddress.faderFxLast;
                }

                new InterpreterDirect(n);
                VirtualElement.last.addSource(pFader);
                VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
                VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
                VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            }

            if (pType.equals("Send")) {

                String adr = OscAddress.trackAddress + "/0" + OscAddress.faderMix[i + 3];

                new InterpreterDirect(n);
                VirtualElement.last.addSource(pFader);
                VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
                VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
                VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            }

            if (pType.equals("FX 1")) {

                String adr = OscAddress.faderFx + "/" + i;

                new InterpreterDirect(n);
                VirtualElement.last.addSource(pFader);
                VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
                VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
                VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            }


            if (pType.equals("FX 2")) {

                String adr = OscAddress.faderFx + "/" + (i + 4);

                new InterpreterDirect(n);
                VirtualElement.last.addSource(pFader);
                VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
                VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
                VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            }

            if (pType.equals("Free 1")) {

                String adr = OscAddress.faderFree + "/" + i;

                new InterpreterDirect(n);
                VirtualElement.last.addSource(pFader);
                VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
                VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
                VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            }


            if (pType.equals("Free 2")) {

                String adr = OscAddress.faderFree + "/" + (i + 4);

                new InterpreterDirect(n);
                VirtualElement.last.addSource(pFader);
                VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
                VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
                VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            }


            if (pType.contains("Midi")) {

                int status, cc, off;

                off = 0;

                if (pType.equals("Midi 2")) off = 4;

                status = MidiAddress.surfaceMidiModeStatus[0];
                cc = MidiAddress.surfaceMidiModeCC[i + off];

                new InterpreterMidi14Bit(n);
                VirtualElement.last.addSource(pFader);
                ((InterpreterMidi14Bit) VirtualElement.last).linkToFeedback(status, cc);
                ((InterpreterMidi14Bit) VirtualElement.last).bypass14BitByParameter(pMidi14bit);
                VirtualElement.last.addAction(new SendMidiAbsolute14Bit(0, new int[]{status, cc}));
                ((SendMidiAbsolute14Bit) Action.last).bypass14BitByParameter(pMidi14bit);


            }

            Condition.back();

        }

        Condition.back();

    }

    //************************************************************
    //      Method : Add Fader Bank Button
    //************************************************************

    public static void addFaderBankButtonSet(String pName, Parameter pShift, InputElement[] pButtons, OutputElement[] pLeds, Parameter pFaderMode, int pFaderModeNumber, Parameter pFaderBank, int pColor) {

        double[] rgb = Color.HexToRgbDouble(pColor);

        Condition.add(pShift, 0);

        Condition.add(pFaderMode, pFaderModeNumber);

        for (int i = 0; i < 4; i++) {


            new InterpreterDirect(pName);
            VirtualElement.last.addSource(pButtons[i]);
            VirtualElement.last.addAction(new ParameterSet(pFaderBank, i));

            Condition.add(pFaderBank, i);
            new InterpreterDirect(pName + " LED");
            VirtualElement.last.addTarget(pLeds[i]);
            VirtualElement.last.setParameterValue(1, rgb[0], rgb[1], rgb[2]);
            Condition.back();

        }

        Condition.clear();

    }


    //************************************************************
    //      Method : Add Fader Mode Button
    //************************************************************

    public static void addFaderModeButton(String pName, Parameter pShift, InputElement pButton, OutputElement pLed, Parameter pFaderMode, int pFaderModeNumber, int pOscBankValue) {


        new InterpreterDirect(pName);

        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addAction(new ParameterSet(pFaderMode, pFaderModeNumber));
        VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.faderMode, pOscBankValue));

        Condition.add(pFaderMode, pFaderModeNumber);
        new InterpreterDirect(pName + " LED");
        VirtualElement.last.addTarget(pLed);
        Condition.back();


    }

    //************************************************************
    //      Method : Add Fader Mode Button Free
    //************************************************************

    public static void addFaderModeButtonDouble(String pName, Parameter pShift, InputElement pButton, OutputElement pLed, Parameter pFaderMode, int pFaderModeNumber, int pFaderModeNumberDouble, int pOscBankValue) {


        new InterpreterDirect(pName);

        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addAction(new ParameterToggle(pFaderMode, pFaderModeNumberDouble, pFaderModeNumber));

        VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.faderMode, pOscBankValue));


        Condition.add(pFaderMode, pFaderModeNumber);
        new InterpreterDirect(pName + " LED");
        VirtualElement.last.addTarget(pLed);
        Condition.back();

        Condition.add(pFaderMode, pFaderModeNumberDouble);
        new InterpreterDirect(pName + " LED");
        VirtualElement.last.addTarget(pLed);
        Condition.back();


    }


    //************************************************************
    //      Method : Add Fader Mode Button
    //************************************************************

    public static void addModeButton(String pName, Parameter pShift, InputElement pButton, OutputElement pLed, Parameter pFaderMode, int pFaderModeNumber, int pColor, TrackData pTrackData) {

        new InterpreterDirect(pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addAction(new ParameterSet(pFaderMode, pFaderModeNumber));


        if (pColor != 0) {

            double[] rgb = Color.HexToRgbDouble(pColor);


            Condition.add(pFaderMode, pFaderModeNumber);

            if (pTrackData == null) {

                new InterpreterDirect(pName + " LED");
                VirtualElement.last.addTarget(pLed);
                VirtualElement.last.setParameterValue(1, rgb[0], rgb[1], rgb[2]);

            } else {

                new LedControllerTrackColor(pName + " LED", pTrackData, 1, pColor);
                VirtualElement.last.addTarget(pLed);
                ((LedControllerTrackColor) VirtualElement.last).setColor();

            }

            Condition.back();

            Condition.add(pFaderMode, pFaderModeNumber, 1);

            if (pTrackData == null) {

                new InterpreterDirect(pName + " LED");
                VirtualElement.last.addTarget(pLed);
                VirtualElement.last.setParameterValue(1, rgb[0] * 0.1, rgb[1] * 0.1, rgb[2] * 0.1);

            } else {

                new LedControllerTrackColor(pName + " LED", pTrackData, 0.1, pColor);
                VirtualElement.last.addTarget(pLed);
                ((LedControllerTrackColor) VirtualElement.last).setColor();

            }

            Condition.back();

        } else {

            Condition.add(pFaderMode, pFaderModeNumber);
            new InterpreterDirect(pName + " LED");
            VirtualElement.last.addTarget(pLed);
            VirtualElement.last.setParameterValue(0);
            Condition.back();

        }


    }

    public static void addModeButton(String pName, Parameter pShift, InputElement pButton, OutputElement pLed, Parameter pFaderMode, int pFaderModeNumber, int pColor) {
        addModeButton(pName, pShift, pButton, pLed, pFaderMode, pFaderModeNumber, pColor, null);
    }

    public static void addModeButton(String pName, Parameter pShift, InputElement pButton, OutputElement pLed, Parameter pFaderMode, int pFaderModeNumber) {
        addModeButton(pName, pShift, pButton, pLed, pFaderMode, pFaderModeNumber, 0, null);
    }


    //************************************************************
    //      Method : Add Fader Mode Button Free
    //************************************************************

    public static void addModeButtonDouble(String pName, Parameter pShift, InputElement pButton, OutputElement pLed, Parameter pFaderMode, int pFaderModeNumber, int pFaderModeNumberDouble) {

        new InterpreterDirect(pName);

        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addAction(new ParameterToggle(pFaderMode, pFaderModeNumberDouble, pFaderModeNumber));

        Condition.add(pFaderMode, pFaderModeNumber);
        new InterpreterDirect(pName + " LED");
        VirtualElement.last.addTarget(pLed);
        Condition.back();

        Condition.add(pFaderMode, pFaderModeNumberDouble);
        new InterpreterDirect(pName + " LED");
        VirtualElement.last.addTarget(pLed);
        Condition.back();


    }


    //************************************************************
    //      Method : Add MIDI Button
    //************************************************************

    public static void addMidiButton(String pName, InputElement pButton, OutputElement pLed, int pByte1, int pByte2, int pColor) {

        int[] msg = {pByte1, pByte2};

        new InterpreterDirect(pName + " " + pByte1 + ":" + pByte2);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addTarget(pLed);
        VirtualElement.last.addAction(new SendMidiDirect(0, msg));
        VirtualElement.last.linkToFeedback(Midi.getLookupAddress(pByte1, pByte2));


        if (pColor != 0) {
            double[] rgb = Color.HexToRgbDouble(pColor);
            VirtualElement.last.setParameterValue(0, rgb[0], rgb[1], rgb[2]);
        } else {
            VirtualElement.last.setParameterValue(0);
        }

    }

    //************************************************************
    //      Method : Add OSC Button
    //************************************************************

    public static void addOscButton(String pName, InputElement pButton, OutputElement pLed, String pOscMessage, int pColor) {

        new InterpreterDirect(pName + " " + pOscMessage);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addTarget(pLed);
        VirtualElement.last.addAction(new SendOscDirect(0, pOscMessage));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, pOscMessage);


        if (pColor != 0) {
            double[] rgb = Color.HexToRgbDouble(pColor);
            VirtualElement.last.setParameterValue(0, rgb[0], rgb[1], rgb[2]);
        } else {
            VirtualElement.last.setParameterValue(0);
        }


    }

    //************************************************************
    //      Method : Add Simple Control Button Feedback
    //************************************************************

    public static void addFeedbackControlButton(String pName, InputElement pButton, OutputElement pLed, int pOscPortId, String pOscAdress, int pColor) {

        new InterpreterDirect(pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addTarget(pLed);
        VirtualElement.last.addAction(new SendOscOnPress(pOscPortId, pOscAdress));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, pOscAdress);

        if (pColor != 0) {
            double[] rgb = Color.HexToRgbDouble(pColor);
            VirtualElement.last.setParameterValue(0, rgb[0], rgb[1], rgb[2]);
        } else {
            VirtualElement.last.setParameterValue(0);
        }

    }

    //************************************************************
    //      Method : Add Simple Control Button
    //************************************************************

    public static void addSimpleControlButton(String pName, InputElement pButton, OutputElement pLed, String pOscAdress, int pColor) {

        new InterpreterDirect(pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addTarget(pLed);
        VirtualElement.last.addAction(new SendOscOnPress(1, pOscAdress));

        if (pColor != 0) {
            double[] rgb = Color.HexToRgbDouble(pColor);
            VirtualElement.last.setParameterValue(1, rgb[0], rgb[1], rgb[2]);
        }

    }

}

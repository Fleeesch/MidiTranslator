package com.fleeesch.miditranslator.device.macros;

import com.fleeesch.miditranslator.Main;
import com.fleeesch.miditranslator.action.Action;
import com.fleeesch.miditranslator.action.midi.SendMidiAbsolute14Bit;
import com.fleeesch.miditranslator.action.midi.SendMidiDirect;
import com.fleeesch.miditranslator.action.osc.SendOscAbsolute;
import com.fleeesch.miditranslator.action.osc.SendOscDirect;
import com.fleeesch.miditranslator.action.osc.SendOscOnPress;
import com.fleeesch.miditranslator.action.osc.SendOscRelative;
import com.fleeesch.miditranslator.action.parameter.ParameterSet;
import com.fleeesch.miditranslator.action.parameter.ParameterToggle;
import com.fleeesch.miditranslator.data.lookup.midi.MidiAddress;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;
import com.fleeesch.miditranslator.data.midi.Midi;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.fader.FaderMotorized;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.controller.led.LedControllerTrackColor;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.MotorFaderController;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.control.MfcMulti;
import com.fleeesch.miditranslator.element.virtual.controller.motorfader.control.MfcSingle;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterDirect;
import com.fleeesch.miditranslator.element.virtual.interpreter.InterpreterMidi14Bit;
import com.fleeesch.miditranslator.functions.math.Color;

import java.util.List;

public class FaderPort8Macros {

    //************************************************************
    //      Method : Add Feedback Button (with Color)
    //************************************************************

    public static void addFeedbackButton(String pName, InputElement pButton, OutputElement pButtonLed, int pOscPortId, String pOscCommand, int pColor) {

        double[] rgb = Color.HexToRgbDouble(pColor);

        new InterpreterDirect("VE " + pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addTarget(pButtonLed);
        VirtualElement.last.addAction(new SendOscOnPress(pOscPortId, pOscCommand));
        VirtualElement.last.linkToFeedback(Osc.DawArrange, pOscCommand);

        if (pColor != 0) VirtualElement.last.setParameterValue(1, rgb[0], rgb[1], rgb[2]);
        else VirtualElement.last.setParameterValue(1);
    }

    public static void addFeedbackButton(String pName, InputElement pButton, OutputElement pButtonLed, int pOscPortId, String pOscCommand) {
        addFeedbackButton(pName, pButton, pButtonLed, pOscPortId, pOscCommand, 0);
    }

    //************************************************************
    //      Method : Add Single Button (with Color)
    //************************************************************

    public static void addDawButtonSimple(String pName, InputElement pButton, OutputElement pButtonLed, String pOscCommand, int pColor) {

        double[] rgb = Color.HexToRgbDouble(pColor);

        new InterpreterDirect("VE " + pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addTarget(pButtonLed);
        VirtualElement.last.setParameterValue(1, rgb[0], rgb[1], rgb[2]);
        VirtualElement.last.addAction(new SendOscOnPress(1, pOscCommand));


    }

    //************************************************************
    //      Method : Add Single Button
    //************************************************************

    public static void addDawButtonSimple(String pName, InputElement pButton, OutputElement pButtonLed, String pOscCommand) {

        new InterpreterDirect("VE " + pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addTarget(pButtonLed);
        VirtualElement.last.addAction(new SendOscOnPress(1, pOscCommand));

    }

    //************************************************************
    //      Method : Add Encoder Jog Set
    //************************************************************

    public static void addEncoderJogSet(String pName, Parameter pEncoderMode, int pEncoderModeValue, Parameter pEncoderPush, InputElement pEncoder, InputElement pEncoderButton, InputElement pButton1, OutputElement pButton1Led, InputElement pButton2, OutputElement pButton2Led, Action pActionEncoderNoPushMove, Action pActionEncoderPushMove, Action pActionEncoderPush, Action pActionButton1, Action pActionButton2, double pScale1, double pScale2) {


        Condition.add(pEncoderMode, pEncoderModeValue);

        if (pActionEncoderPush != null) {
            new InterpreterDirect(pName + " Encoder Push");
            VirtualElement.last.addSource(pEncoderButton);
            VirtualElement.last.addAction(pActionEncoderPush);
        }

        if (pActionEncoderNoPushMove != null) {
            Condition.add(pEncoderPush, 0);
            new InterpreterDirect(pName + " Jog");
            VirtualElement.last.addSource(pEncoder);
            VirtualElement.last.addAction(pActionEncoderNoPushMove);
            if (pActionEncoderNoPushMove instanceof SendOscRelative)
                ((SendOscRelative) pActionEncoderNoPushMove).rescale(pScale1, 0.005);
            Condition.back();
        }

        if (pActionEncoderPushMove != null) {
            Condition.add(pEncoderPush, 1);
            new InterpreterDirect(pName + "Jog Push");
            VirtualElement.last.addSource(pEncoder);
            VirtualElement.last.addAction(pActionEncoderPushMove);
            if (pActionEncoderPushMove instanceof SendOscRelative)
                ((SendOscRelative) pActionEncoderPushMove).rescale(pScale2, 0.005);
            Condition.back();
        }

        if (pActionButton1 != null) {
            new InterpreterDirect(pName + "Button <");
            VirtualElement.last.addSource(pButton1);
            VirtualElement.last.addTarget(pButton1Led);
            VirtualElement.last.addAction(pActionButton1);
        }

        if (pActionButton2 != null) {
            new InterpreterDirect(pName + "Button >");
            VirtualElement.last.addSource(pButton2);
            VirtualElement.last.addTarget(pButton2Led);
            VirtualElement.last.addAction(pActionButton2);
        }

        Condition.clear();

    }

    //************************************************************
    //      Method : Add Encoder Jog Mode Button
    //************************************************************

    public static void addEncoderJogModeButton(String pName, Parameter pJogMode, int pModeNumber, InputElement pButton, OutputElement pLed) {

        new InterpreterDirect("VI " + pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addAction(new ParameterSet(pJogMode, pModeNumber));

        Condition.add(pJogMode, pModeNumber);

        new InterpreterDirect("VI " + pName + " LED");
        VirtualElement.last.addTarget(pLed);

        Condition.clear();

    }

    //************************************************************
    //      Method : Add Fader Mode Button Set
    //************************************************************

    public static void addFaderModeButtonSet(Parameter pShift, String pName, Parameter pFaderMode, int pFaderModeNumber, InputElement pButton, OutputElement pLed, int pColor, int pOscBankValue) {

        double[] rgb = Color.HexToRgbDouble(pColor);

        // button
        new InterpreterDirect("VE " + pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addAction(new ParameterSet(pFaderMode, pFaderModeNumber));
        VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.faderMode, pOscBankValue));

        // led
        Condition.add(pFaderMode, pFaderModeNumber);

        new InterpreterDirect("VE + " + pName + " LED");
        VirtualElement.last.addTarget(pLed);

        // rgb color case (with dimmed light)

        if (pColor != 0) {

            VirtualElement.last.setParameterValue(0, rgb[0], rgb[1], rgb[2]);

            Condition.back();

            Condition.add(pFaderMode, pFaderModeNumber, 1);

            new InterpreterDirect("VE + " + pName + " LED");
            VirtualElement.last.addTarget(pLed);
            VirtualElement.last.setParameterValue(0, rgb[0] * 0.1, rgb[1] * 0.1, rgb[2] * 0.1);

            Condition.clear();
        }


        Condition.clear();

    }


    //************************************************************
    //      Method : Add Parameter Toggle Switch
    //************************************************************

    public static void addParameterToggleSwitch(Parameter pBank, InputElement pButton, OutputElement pLed, int pColor, boolean pNegative) {

        double[] rgb = Color.HexToRgbDouble(pColor);

        new InterpreterDirect("Bank Toggle");
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addAction(new ParameterToggle(pBank, 1));

        if (!pNegative) Condition.add(pBank, 0);
        else Condition.add(pBank, 1);

        new InterpreterDirect("Bank Toggle");
        VirtualElement.last.addTarget(pLed);
        VirtualElement.last.setParameterValue(1, rgb[0] * 0.05, rgb[1] * 0.05, rgb[2] * 0.05);


        Condition.back();

        if (!pNegative) Condition.add(pBank, 1);
        else Condition.add(pBank, 0);

        new InterpreterDirect("Bank Toggle");
        VirtualElement.last.addTarget(pLed);
        VirtualElement.last.setParameterValue(1, rgb[0], rgb[1], rgb[2]);

        Condition.back();

    }

    public static void addParameterToggleSwitch(Parameter pBank, InputElement pButton, OutputElement pLed, int pColor) {
        addParameterToggleSwitch(pBank, pButton, pLed, pColor, false);
    }

    //************************************************************
    //      Method : Add Bank Button Set
    //************************************************************

    public static void addBankSelectSet(Parameter pBankExtension, Parameter pShift, String pName, Parameter pFaderMode, int pFaderModeNumber, Parameter pModeBank, List<InputElement> pButtonSelect, List<OutputElement> pButtonSelectLed, int pButtonSelectColor, int pAltColor, boolean pTrackColorFeedback) {

        double[] rgb;

        Condition.add(pShift, 0);

        Condition.add(pFaderMode, pFaderModeNumber);

        for (int b = 0; b < 8; b++) {

            if (pFaderModeNumber == 1 && b >= 3) rgb = Color.HexToRgbDouble(pAltColor);
            else rgb = Color.HexToRgbDouble(pButtonSelectColor);

            new InterpreterDirect("VE " + pName + "Bank: " + b);
            VirtualElement.last.addSource(pButtonSelect.get(b));
            VirtualElement.last.addAction(new ParameterSet(pModeBank, b));

            Condition.add(pModeBank, b);

            if (!pTrackColorFeedback) {
                new InterpreterDirect(pName + "Bank LED: " + b);
                VirtualElement.last.addTarget(pButtonSelectLed.get(b));
                VirtualElement.last.setParameterValue(1, rgb[0], rgb[1], rgb[2]);
            } else {


                for (int i = 0; i < 2; i++) {

                    Condition.add(pBankExtension, i);
                    new LedControllerTrackColor("Bank LED", Main.deviceDaw.trackData.tracks.get(b + (8 * i)), 1, 0x706A60);
                    VirtualElement.last.addTarget(pButtonSelectLed.get(b));
                    ((LedControllerTrackColor) VirtualElement.last).setColor();
                    Condition.back();

                }

            }

            Condition.back();

            Condition.add(pModeBank, b, 1);

            new InterpreterDirect(pName + "Bank LED: " + b);

            if (!pTrackColorFeedback) {
                VirtualElement.last.addTarget(pButtonSelectLed.get(b));
                VirtualElement.last.setParameterValue(1, rgb[0] * 0.05, rgb[1] * 0.05, rgb[2] * 0.05);
            } else {

                for (int i = 0; i < 2; i++) {

                    Condition.add(pBankExtension, i);
                    new LedControllerTrackColor("Bank LED", Main.deviceDaw.trackData.tracks.get(b + (8 * i)), 0.05, 0x201C10);
                    VirtualElement.last.addTarget(pButtonSelectLed.get(b));
                    ((LedControllerTrackColor) VirtualElement.last).setColor();
                    Condition.back();

                }

            }


            Condition.back();


        }

        Condition.clear();

    }

    //************************************************************
    //      Method : Add Toggle Button Set (Midi)
    //************************************************************

    public static void addToggleButtonSetMidi(Parameter pSwitchBank, Parameter pShift, String pName, Parameter pFaderMode, int pFaderModeNumber, Parameter pModeBank, List<InputElement> pButtonSelect, List<OutputElement> pButtonSelectLed, int pButtonSelectColor, int pAltColor) {

        double[] rgb;

        Condition.add(pShift, 1);

        Condition.add(pFaderMode, pFaderModeNumber);

        for (int b = 0; b < 8; b++) {

            for (int z = 0; z < 8; z++) {

                Condition.add(pModeBank, z);

                rgb = Color.HexToRgbDouble(pButtonSelectColor);

                Condition.add(pSwitchBank, 0);

                int[] msg = {176 + z, MidiAddress.surfaceMidiModeSwitch[b]};

                new InterpreterDirect("VE " + pName + "Bank: " + b);
                VirtualElement.last.addSource(pButtonSelect.get(b));
                VirtualElement.last.addTarget(pButtonSelectLed.get(b));
                VirtualElement.last.addAction(new SendMidiDirect(0, msg));
                VirtualElement.last.linkToFeedback(Midi.getLookupAddress(msg[0], msg[1]));
                VirtualElement.last.setParameterValue(0, rgb[0], rgb[1], rgb[2]);

                Condition.back();

                Condition.add(pSwitchBank, 1);

                msg = new int[]{176 + z + 8, MidiAddress.surfaceMidiModeSwitch[b]};

                new InterpreterDirect("VE " + pName + "Bank: " + b);
                VirtualElement.last.addSource(pButtonSelect.get(b));
                VirtualElement.last.addTarget(pButtonSelectLed.get(b));
                VirtualElement.last.addAction(new SendMidiDirect(0, msg));
                VirtualElement.last.linkToFeedback(Midi.getLookupAddress(msg[0], msg[1]));
                VirtualElement.last.setParameterValue(0, rgb[0], rgb[1], rgb[2]);

                Condition.back();
                Condition.back();

            }

        }


        Condition.clear();

    }


    //************************************************************
    //      Method : Add RecArm Button Set
    //************************************************************

    public static void addRecArmToggleSet(Parameter pSwitchBank, Parameter pShift, String pName, Parameter pFaderMode, int pFaderModeNumber, Parameter pModeBank, List<InputElement> pButtonSelect, List<OutputElement> pButtonSelectLed, int pButtonSelectColor, int pAltColor) {

        double[] rgb;

        Condition.add(pShift, 1);

        Condition.add(pFaderMode, pFaderModeNumber);

        for (int b = 0; b < 8; b++) {

            rgb = Color.HexToRgbDouble(pButtonSelectColor);

            Condition.add(pSwitchBank, 0);

            String adr = OscAddress.trackArm + "/" + b;

            new InterpreterDirect("VE " + pName + "Bank: " + b);

            VirtualElement.last.addSource(pButtonSelect.get(b));
            VirtualElement.last.addTarget(pButtonSelectLed.get(b));
            VirtualElement.last.addAction(new SendOscDirect(0, adr));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
            VirtualElement.last.setParameterValue(0, rgb[0], rgb[1], rgb[2]);

            Condition.back();

            adr = OscAddress.trackArm + "/" + (b + 8);

            Condition.add(pSwitchBank, 1);

            new InterpreterDirect("VE " + pName + "Bank: " + b);

            VirtualElement.last.addSource(pButtonSelect.get(b));
            VirtualElement.last.addTarget(pButtonSelectLed.get(b));
            VirtualElement.last.addAction(new SendOscDirect(0, adr));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
            VirtualElement.last.setParameterValue(0, rgb[0], rgb[1], rgb[2]);

            Condition.back();

        }

        Condition.clear();

    }

    //************************************************************
    //      Method : Add Toggle Button Set
    //************************************************************

    public static void addToggleButtonSet(Parameter pSwitchBank, Parameter pShift, String pName, String pOscAddress, Parameter pFaderMode, int pFaderModeNumber, Parameter pModeBank, List<InputElement> pButtonSelect, List<OutputElement> pButtonSelectLed, int pButtonSelectColor, int pAltColor) {

        double[] rgb;


        Condition.add(pShift, 1);

        Condition.add(pFaderMode, pFaderModeNumber);

        for (int b = 0; b < 8; b++) {

            for (int z = 0; z < 8; z++) {

                Condition.add(pModeBank, z);

                rgb = Color.HexToRgbDouble(pButtonSelectColor);

                Condition.add(pSwitchBank, 0);

                String adr = pOscAddress + "/sw/" + ((z * 8) + b);

                new InterpreterDirect("VE " + pName + "Bank: " + b);

                VirtualElement.last.addSource(pButtonSelect.get(b));
                VirtualElement.last.addTarget(pButtonSelectLed.get(b));
                VirtualElement.last.addAction(new SendOscDirect(0, adr));
                VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);

                VirtualElement.last.setParameterValue(0, rgb[0], rgb[1], rgb[2]);

                Condition.back();

                adr = pOscAddress + "/sw/" + (((z + 8) * 8) + b);

                Condition.add(pSwitchBank, 1);

                new InterpreterDirect("VE " + pName + "Bank: " + b);

                VirtualElement.last.addSource(pButtonSelect.get(b));
                VirtualElement.last.addTarget(pButtonSelectLed.get(b));
                VirtualElement.last.addAction(new SendOscDirect(0, adr));
                VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
                VirtualElement.last.setParameterValue(0, rgb[0], rgb[1], rgb[2]);

                Condition.back();
                Condition.back();

            }

        }

        Condition.clear();

    }

    //************************************************************
    //      Method : Add Fader Set (Midi)
    //************************************************************

    public static void addFaderSetMidi(Parameter pTrackBank, List<InputElement> pFaders, String pName, int pBankId, Parameter pFaderBank, Parameter pFaderMode, int pFaderModeNumber, Parameter pMidi14Bit) {


        Condition.add(pFaderMode, pFaderModeNumber);

        int[] chan = MidiAddress.surfaceMidiModeStatus;
        int[] midiCC = MidiAddress.surfaceMidiModeCC;

        int[] msg;

        for (int i = 0; i < 8; i++) {

            Condition.add(pFaderBank, pBankId);

            Condition.add(pTrackBank, 0);

            msg = new int[]{chan[pBankId], midiCC[i]};

            new InterpreterMidi14Bit("VE " + pName + ": " + pBankId + "-" + i);
            VirtualElement.last.addSource(pFaders.get(i));
            ((InterpreterMidi14Bit) VirtualElement.last).linkToFeedback(msg[0], msg[1]);
            ((InterpreterMidi14Bit) VirtualElement.last).bypass14BitByParameter(pMidi14Bit);
            VirtualElement.last.addAction(new SendMidiAbsolute14Bit(0, msg));
            ((SendMidiAbsolute14Bit) Action.last).bypass14BitByParameter(pMidi14Bit);

            Condition.back();

            Condition.add(pTrackBank, 1);

            msg = new int[]{chan[pBankId + 8], midiCC[i]};

            new InterpreterMidi14Bit("VE " + pName + ": " + pBankId + "-" + i);
            VirtualElement.last.addSource(pFaders.get(i));
            ((InterpreterMidi14Bit) VirtualElement.last).linkToFeedback(msg[0], msg[1]);
            ((InterpreterMidi14Bit) VirtualElement.last).bypass14BitByParameter(pMidi14Bit);
            VirtualElement.last.addAction(new SendMidiAbsolute14Bit(0, msg));
            ((SendMidiAbsolute14Bit) Action.last).bypass14BitByParameter(pMidi14Bit);

            Condition.back();

            Condition.back();

        }


        Condition.clear();

    }


    //************************************************************
    //      Method : Add Fader Set (Track)
    //************************************************************

    public static void addFaderSetTrack(Parameter pTrackBank, String pOscAddressTrack, String[] pOscAdress, List<InputElement> pFaders, String pName, int pBankId, Parameter pFaderMode, int pFaderModeNumber, Parameter pFaderBank) {

        String adr;

        Condition.add(pFaderMode, pFaderModeNumber);

        for (int i = 0; i < 8; i++) {

            Condition.add(pFaderBank, pBankId);

            Condition.add(pTrackBank, 0);

            adr = pOscAddressTrack + "/" + pBankId + pOscAdress[i];

            new InterpreterDirect("VE " + pName + ": " + pBankId + "-" + i);
            VirtualElement.last.addSource(pFaders.get(i));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
            VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
            VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            Condition.back();

            adr = pOscAddressTrack + "/" + (pBankId + 8) + pOscAdress[i];

            Condition.add(pTrackBank, 1);

            new InterpreterDirect("VE " + pName + ": " + pBankId + "-" + (i + 8));
            VirtualElement.last.addSource(pFaders.get(i));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
            VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
            VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            Condition.back();

            Condition.back();

        }

        Condition.clear();

    }

    //************************************************************
    //      Method : Add Fader Set (Mix)
    //************************************************************

    public static void addFaderSetMix(Parameter pTrackBank, String pOscAddressTrack, String[] pOscAdress, List<InputElement> pFaders, String pName, int pBankId, Parameter pFaderMode, int pFaderModeNumber, Parameter pFaderBank) {

        String adr;

        Condition.add(pFaderMode, pFaderModeNumber);

        for (int i = 0; i < 8; i++) {

            Condition.add(pFaderBank, pBankId);

            Condition.add(pTrackBank, 0);

            adr = pOscAddressTrack + "/" + i + pOscAdress[pBankId];

            new InterpreterDirect("VE " + pName + ": " + pBankId + "-" + i);
            VirtualElement.last.addSource(pFaders.get(i));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
            VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
            VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            Condition.back();

            Condition.add(pTrackBank, 1);

            adr = pOscAddressTrack + "/" + (i + 8) + pOscAdress[pBankId];

            new InterpreterDirect("VE " + pName + ": " + pBankId + "-" + (i + 8));
            VirtualElement.last.addSource(pFaders.get(i));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
            VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
            VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            Condition.back();

            Condition.back();

        }

        Condition.clear();

    }

    //************************************************************
    //      Method : Add Fader Set (Free)
    //************************************************************

    public static void addFaderSet(Parameter pTrackBank, String pOscAdress, List<InputElement> pFaders, String pName, int pBankId, Parameter pFaderMode, int pFaderModeNumber, Parameter pFaderBank) {

        Condition.add(pFaderMode, pFaderModeNumber);

        String adr;

        for (int i = 0; i < 8; i++) {

            int idx = pBankId * 8 + i;

            adr = pOscAdress + "/" + idx;

            Condition.add(pFaderBank, pBankId);

            Condition.add(pTrackBank, 0);

            new InterpreterDirect("VE " + pName + ": " + idx);
            VirtualElement.last.addSource(pFaders.get(i));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
            VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
            VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));

            Condition.back();

            adr = pOscAdress + "/" + (idx + 64);

            Condition.add(pTrackBank, 1);

            new InterpreterDirect("VE " + pName + ": " + idx);
            VirtualElement.last.addSource(pFaders.get(i));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
            VirtualElement.last.addAction(new SendOscAbsolute(0, adr));
            VirtualElement.last.addAction(new SendOscDirect(0, adr + "/t"));


            Condition.back();

            Condition.back();

        }

        Condition.clear();

    }

    //************************************************************
    //      Method : Add Track Mute Solo Set
    //************************************************************

    public static void addTrackMuteSoloSet(Parameter pTrackBank, List<InputElement> pButtonMute, List<InputElement> pButtonSolo, List<OutputElement> pButtonMuteLed, List<OutputElement> pButtonSoloLed) {

        for (int i = 0; i < 8; i++) {

            Condition.add(pTrackBank, 0);

            new InterpreterDirect("Mute " + i);
            VirtualElement.last.addSource(pButtonMute.get(i));
            VirtualElement.last.addTarget(pButtonMuteLed.get(i));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackMute + "/" + i);
            VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.trackMute + "/" + i));


            new InterpreterDirect("Solo " + i);
            VirtualElement.last.addSource(pButtonSolo.get(i));
            VirtualElement.last.addTarget(pButtonSoloLed.get(i));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackSolo + "/" + i);
            VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.trackSolo + "/" + i));


            Condition.back();

            Condition.add(pTrackBank, 1);

            new InterpreterDirect("Mute " + i + 8);
            VirtualElement.last.addSource(pButtonMute.get(i));
            VirtualElement.last.addTarget(pButtonMuteLed.get(i));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackMute + "/" + (i + 8));
            VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.trackMute + "/" + (i + 8)));


            new InterpreterDirect("Solo " + i + 8);
            VirtualElement.last.addSource(pButtonSolo.get(i));
            VirtualElement.last.addTarget(pButtonSoloLed.get(i));
            VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackSolo + "/" + (i + 8));
            VirtualElement.last.addAction(new SendOscOnPress(0, OscAddress.trackSolo + "/" + (i + 8)));


            Condition.back();

        }

    }

    //************************************************************
    //      Method : Add Motor Fader Controller Set
    //************************************************************

    public static MotorFaderController addMotorFaderControllerSet(Parameter pShift, List<InputElement> pFaders, List<InputElement> pMute, List<InputElement> pSolo, List<OutputElement> pMuteLed, List<OutputElement> pSoloLed, InputElement pMuteClear, OutputElement pMuteClearLed, InputElement pSoloClear, OutputElement pSoloClearLed) {

        MotorFaderController mf = new MotorFaderController("MotorFader Controller");

        for (int i = 0; i < 8; i++) {

            mf.addFader((FaderMotorized) pFaders.get(i));

            mf.addMuteSingle(new MfcSingle("Fader Mute " + i, mf));
            VirtualElement.last.addSource(pMute.get(i));
            VirtualElement.last.addTarget(pMuteLed.get(i));

            mf.addSoloSingle(new MfcSingle("Fader Solo " + i, mf));
            VirtualElement.last.addSource(pSolo.get(i));
            VirtualElement.last.addTarget(pSoloLed.get(i));

            mf.addTouchSingle(new MfcSingle("Touch Bypass " + i, mf));
            mf.addFreezeSingle(new MfcSingle("Touch Freeze " + i, mf));

        }

        mf.addSoloGlobal(new MfcMulti("Fader Solo Global", mf, false));
        VirtualElement.last.addSource(pSoloClear);
        VirtualElement.last.addTarget(pSoloClearLed);

        mf.addMuteGlobal(new MfcMulti("Fader Mute Global", mf, true));
        VirtualElement.last.addSource(pMuteClear);
        VirtualElement.last.addTarget(pMuteClearLed);


        return mf;


    }

}

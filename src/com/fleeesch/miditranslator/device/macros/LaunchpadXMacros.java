package com.fleeesch.miditranslator.device.macros;

import com.fleeesch.miditranslator.Main;
import com.fleeesch.miditranslator.action.ActionBlind;
import com.fleeesch.miditranslator.action.midi.SendMidiDirect;
import com.fleeesch.miditranslator.action.osc.SendOscDirect;
import com.fleeesch.miditranslator.action.osc.SendOscOnPress;
import com.fleeesch.miditranslator.action.parameter.ParameterIncrement;
import com.fleeesch.miditranslator.action.parameter.ParameterSet;
import com.fleeesch.miditranslator.action.parameter.ParameterToggle;
import com.fleeesch.miditranslator.data.midi.Midi;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.device.hardware.LaunchpadX;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.controller.led.LedControllerParameter;
import com.fleeesch.miditranslator.element.virtual.controller.led.LedControllerTrackColor;
import com.fleeesch.miditranslator.element.virtual.interpreter.*;
import com.fleeesch.miditranslator.functions.math.Color;

import java.util.ArrayList;
import java.util.List;

public class LaunchpadXMacros {


    public static OutputElement ledLogo;
    public static List<OutputElement> ledPads = new ArrayList<>();
    public static List<OutputElement> ledButtons = new ArrayList<>();
    public static List<InputElement> pads = new ArrayList<>();
    public static List<InputElement> buttons = new ArrayList<>();

    public static Parameter view;

    public static Condition viewIsTracking;
    public static Condition viewIsMpe;
    public static Condition viewIsMpeOptions;
    public static Condition viewIsTriggerPadsOsc;
    public static Condition viewIsTriggerPadsMidi;


    //************************************************************
    //      Method : Setup Hardware
    //************************************************************

    public static void setupHardware(List<InputElement> pPads, List<OutputElement> pLedPads, List<InputElement> pButtons, List<OutputElement> pLedButtons, OutputElement pLedLogo) {

        pads = pPads;
        buttons = pButtons;

        ledPads = pLedPads;
        ledButtons = pLedButtons;

        ledLogo = pLedLogo;


    }

    //************************************************************
    //      Method : Setup Parameters
    //************************************************************

    public static void setupParameters(Parameter pView, Condition pViewIsTracking, Condition pViewIsMpe, Condition pViewIsMpeOptions, Condition pViewIsTriggerPadsOsc, Condition pViewIsTriggerPadsMidi) {

        view = pView;

        viewIsTracking = pViewIsTracking;
        viewIsMpe = pViewIsMpe;
        viewIsMpeOptions = pViewIsMpeOptions;
        viewIsTriggerPadsOsc = pViewIsTriggerPadsOsc;
        viewIsTriggerPadsMidi = pViewIsTriggerPadsMidi;

    }

    //************************************************************
    //      Method : Add Pressure Control Double
    //************************************************************

    public static void addPressureControlDouble(String pName, int pPadNumber, String pOscAddress, int pRateMin, int pRateMax, double pValMin, double pValMax, String pOscAddressHard, double pHardValue, double[] pColor) {

        new InterpreterPressure(pName, pRateMin, pRateMax, pValMin, pValMax);
        VirtualElement.last.addSource(pads.get(pPadNumber));
        VirtualElement.last.addTarget(ledPads.get(pPadNumber));
        VirtualElement.last.addAction(new SendOscOnPress(1, pOscAddress));
        VirtualElement.last.addAction(new SendOscDirect(1, pOscAddressHard, pHardValue));
        VirtualElement.last.setParameterValue(1, pColor[0], pColor[1], pColor[2]);

    }

    public static void addPressureControlDoubleTrackColor(String pName, int pPadNumber, String pOscAddress, int pRateMin, int pRateMax, double pValMin, double pValMax, String pOscAddressHard, double pHardValue, double[] pColor, double pPreScale) {

        new InterpreterPressure(pName, pRateMin, pRateMax, pValMin, pValMax);
        VirtualElement.last.addSource(pads.get(pPadNumber));

        VirtualElement.last.addAction(new SendOscOnPress(1, pOscAddress));
        VirtualElement.last.addAction(new SendOscDirect(1, pOscAddressHard, pHardValue));


        int clr = Color.RgbDoubleToHex(pColor[0], pColor[1], pColor[2]);

        new LedControllerTrackColor("Track Color", Main.deviceDaw.trackData.tracks.get(0), pPreScale, clr);
        VirtualElement.last.addTarget(ledPads.get(pPadNumber));
        ((LedControllerTrackColor) VirtualElement.last).setColor();


    }

    //************************************************************
    //      Method : Add Pressure Control
    //************************************************************

    public static void addPressureControl(String pName, int pPadNumber, String pOscAddress, int pRateMin, int pRateMax, double pValMin, double pValMax, double[] pColor) {

        new InterpreterPressure(pName, pRateMin, pRateMax, pValMin, pValMax);
        VirtualElement.last.addSource(pads.get(pPadNumber));
        VirtualElement.last.addTarget(ledPads.get(pPadNumber));
        VirtualElement.last.addAction(new SendOscOnPress(1, pOscAddress));
        VirtualElement.last.setParameterValue(1, pColor[0], pColor[1], pColor[2]);

    }

    public static void addPressureControlTrackColor(String pName, int pPadNumber, String pOscAddress, int pRateMin, int pRateMax, double pValMin, double pValMax, double[] pColor, double pPreScale) {

        new InterpreterPressure(pName, pRateMin, pRateMax, pValMin, pValMax);
        VirtualElement.last.addSource(pads.get(pPadNumber));
        VirtualElement.last.addAction(new SendOscOnPress(1, pOscAddress));

        int clr = Color.RgbDoubleToHex(pColor[0], pColor[1], pColor[2]);

        new LedControllerTrackColor("Track Color", Main.deviceDaw.trackData.tracks.get(0), pPreScale, clr);
        VirtualElement.last.addTarget(ledPads.get(pPadNumber));
        ((LedControllerTrackColor) VirtualElement.last).setColor();


    }


    //************************************************************
    //      Method : Add Hold Control Button
    //************************************************************

    public static void addHoldControlButton(String pName, InputElement pButton, OutputElement pLed, String pOscAddress, String pOscAddressHold, double[] pColor) {

        new InterpreterHold(pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addTarget(pLed);

        if (pOscAddress.isEmpty()) VirtualElement.last.addAction(new ActionBlind());
        else VirtualElement.last.addAction(new SendOscOnPress(1, pOscAddress));

        VirtualElement.last.addAction(new SendOscOnPress(1, pOscAddressHold));

        VirtualElement.last.setParameterValue(1, pColor[0], pColor[1], pColor[2]);

    }

    //************************************************************
    //      Method : Add Feedback Control Button
    //************************************************************

    public static void addFeedbackControlButton(String pName, InputElement pButton, OutputElement pLed, int pOscPortId, String pOscAddress, String pFeedbackOscAddress, double[] pColor) {

        new InterpreterDirect(pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addTarget(pLed);
        VirtualElement.last.addAction(new SendOscOnPress(pOscPortId, pOscAddress));


        if (pFeedbackOscAddress != null) VirtualElement.last.linkToFeedback(Osc.DawArrange, pFeedbackOscAddress);
        else VirtualElement.last.linkToFeedback(Osc.DawArrange, pOscAddress);

        VirtualElement.last.setParameterValue(1, pColor[0], pColor[1], pColor[2], 1);
        ((InterpreterDirect) VirtualElement.last).ledAlwaysOn();


    }

    //************************************************************
    //      Method : Add Simple Control Button
    //************************************************************

    public static void addSimpleControlButton(String pName, InputElement pButton, OutputElement pLed, String pOscAddress, double[] pColor) {

        new InterpreterDirect(pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addTarget(pLed);
        VirtualElement.last.addAction(new SendOscOnPress(1, pOscAddress));
        VirtualElement.last.setParameterValue(1, pColor[0], pColor[1], pColor[2]);

    }

    public static void addSimpleControlButtonTrackColor(String pName, InputElement pButton, OutputElement pLed, String pOscAddress, double[] pColor, double pPreScale) {

        new InterpreterDirect(pName);
        VirtualElement.last.addSource(pButton);
        VirtualElement.last.addAction(new SendOscOnPress(1, pOscAddress));

        int clr = Color.RgbDoubleToHex(pColor[0], pColor[1], pColor[2]);

        new LedControllerTrackColor("Track Color", Main.deviceDaw.trackData.tracks.get(0), pPreScale, clr);
        VirtualElement.last.addTarget(pLed);
        ((LedControllerTrackColor) VirtualElement.last).setColor();

    }

    //************************************************************
    //      Method : Add Parameter Increment
    //************************************************************

    public static void addTransposeSet(Parameter pParameterTranspose, Parameter pPrameterRowOffset) {

        double[] clr = LaunchpadX.colorWhite;

        // +- 6 Semitones
        new InterpreterChord("Transpose +- 6", false);
        VirtualElement.last.addSource(buttons.get(0));
        VirtualElement.last.addSource(buttons.get(1));
        VirtualElement.last.addTarget(ledButtons.get(0));
        VirtualElement.last.addTarget(ledButtons.get(1));
        VirtualElement.last.addAction(new ParameterIncrement(pParameterTranspose, 1, pPrameterRowOffset));
        VirtualElement.last.addAction(new ParameterIncrement(pParameterTranspose, -1, pPrameterRowOffset));
        VirtualElement.last.addAction(new ParameterSet(pParameterTranspose, 0));
        VirtualElement.last.setParameterValue(1, clr[0], clr[1], clr[2]);

        new InterpreterChord("Transpose +- 1", false);
        VirtualElement.last.addSource(buttons.get(2));
        VirtualElement.last.addSource(buttons.get(3));
        VirtualElement.last.addTarget(ledButtons.get(2));
        VirtualElement.last.addTarget(ledButtons.get(3));
        VirtualElement.last.addAction(new ParameterIncrement(pParameterTranspose, -1));
        VirtualElement.last.addAction(new ParameterIncrement(pParameterTranspose, 1));
        VirtualElement.last.addAction(new ParameterSet(pParameterTranspose, 0));
        VirtualElement.last.setParameterValue(1, clr[0], clr[1], clr[2]);


    }

    //************************************************************
    //      Method : Add Parameter Set
    //************************************************************

    public static void addParameterSet(String pName, InputElement pPad, OutputElement pPadLed, Parameter pParameter, double pParameterValue, double[] pColor) {
        new InterpreterDirect(pName + " Set");
        VirtualElement.last.addSource(pPad);
        VirtualElement.last.addAction(new ParameterSet(pParameter, pParameterValue));
        new LedControllerParameter(pName + " Set LED", pParameter, pParameterValue);
        VirtualElement.last.addTarget(pPadLed);
        VirtualElement.last.setParameterValue(1, pColor[0], pColor[1], pColor[2]);
    }

    //************************************************************
    //      Method : Add Parameter Toggle
    //************************************************************

    public static void addParameterToggle(String pName, InputElement pPad, OutputElement pPadLed, Parameter pParameter, double[] pColor) {
        new InterpreterDirect(pName + "Toggle");
        VirtualElement.last.addSource(pPad);
        VirtualElement.last.addAction(new ParameterToggle(pParameter, 1));
        new LedControllerParameter(pName + " Toggle LED", pParameter, 1);
        VirtualElement.last.addTarget(pPadLed);
        VirtualElement.last.setParameterValue(1, pColor[0], pColor[1], pColor[2]);
    }

    //************************************************************
    //      Method : Add Trigger Pads
    //************************************************************

    public static void addTriggerPads(boolean pUseMidi) {

        for (int y = 0; y < 8; y++) {

            double[] clr = new double[3];

            double initBrightness = 0;

            switch (y) {
                case 0 -> clr = LaunchpadX.colorBlueLight;
                case 1 -> clr = LaunchpadX.colorGreenLight;
                case 2 -> clr = LaunchpadX.colorRedLight;
                case 3 -> clr = LaunchpadX.colorAmber;
                case 4 -> clr = LaunchpadX.colorCyanLight;
                case 5 -> clr = LaunchpadX.colorRoseLight;
                case 6 -> clr = LaunchpadX.colorYellow;
                case 7 -> clr = LaunchpadX.colorPinkLight;
            }


            for (int x = 0; x < 8; x++) {

                int idx = y * 8 + x;
                int idxOut = y * 8 + x;

                String adr = "/lpx/trg/" + idx;

                new InterpreterDirect("Trigger Pad " + idx);
                VirtualElement.last.addSource(pads.get(idx));
                VirtualElement.last.addTarget(ledPads.get(idx));

                if (pUseMidi) {
                    VirtualElement.last.addAction(new SendMidiDirect(0, new int[]{176, idxOut}));
                    VirtualElement.last.linkToFeedback(Midi.getLookupAddress(0xB0, idxOut));
                } else {
                    VirtualElement.last.addAction(new SendOscDirect(0, adr));
                    VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
                }


                VirtualElement.last.setParameterValue(1, clr[0], clr[1], clr[2], initBrightness);


            }

            String adr = "/lpx/grp/" + y;

            new InterpreterDirect("Trigger Pad " + y);
            VirtualElement.last.addSource(buttons.get(y + 8));
            VirtualElement.last.addTarget(ledButtons.get(y + 8));

            if (pUseMidi) {
                VirtualElement.last.addAction(new SendMidiDirect(0, new int[]{177, y}));
                VirtualElement.last.linkToFeedback(Midi.getLookupAddress(0xB1, y));
            } else {
                VirtualElement.last.addAction(new SendOscDirect(0, adr));
                VirtualElement.last.linkToFeedback(Osc.DawArrange, adr);
            }

            VirtualElement.last.setParameterValue(1, clr[0], clr[1], clr[2], initBrightness);


        }


    }

    //************************************************************
    //      Method : Add Menu Toggles
    //************************************************************

    public static void addMenuInterface() {

        Condition.clear();

        // Menu Call : Daw Control
        new InterpreterDirect("Set Menu Tracking");
        VirtualElement.last.addSource(buttons.get(4));
        VirtualElement.last.addAction(new ParameterSet(view, 0));

        // Menu Call : Mpe Surface / Mpe Settings
        new InterpreterLatch("Set Menu MPE");
        VirtualElement.last.addSource(buttons.get(5));
        VirtualElement.last.addAction(new ParameterToggle(view, 2, 1));
        VirtualElement.last.addAction(new ParameterSet(view, 1));

        // Menu Call : Trigger Pads
        new InterpreterDirect("Set Menu Trigger Pads");
        VirtualElement.last.addSource(buttons.get(6));
        VirtualElement.last.addAction(new ParameterToggle(view, 4, 3));

        // LED : Daw Control
        new LedControllerParameter("Led : Daw Control", view, 0);
        VirtualElement.last.addTarget(ledButtons.get(4));
        VirtualElement.last.setParameterValue(1, LaunchpadX.colorGreenLight[0], LaunchpadX.colorGreenLight[1], LaunchpadX.colorGreenLight[2]);


        // LED : Trigger Pads
        Condition.add(view, 4, 1);
        new LedControllerParameter("Led : Trigger Pads", view, 3);
        VirtualElement.last.addTarget(ledButtons.get(6));
        VirtualElement.last.setParameterValue(1, LaunchpadX.colorGreenLight[0], LaunchpadX.colorGreenLight[1], LaunchpadX.colorGreenLight[2]);
        Condition.clear();

        // LED : Trigger Pads (Midi)
        Condition.add(view, 4);
        new LedControllerParameter("Led : Trigger Pads", view, 4);
        VirtualElement.last.addTarget(ledButtons.get(6));
        VirtualElement.last.setParameterValue(1, LaunchpadX.colorWhite[0], LaunchpadX.colorWhite[1], LaunchpadX.colorWhite[2]);
        Condition.clear();

        // LED : Mpe Settings
        Condition.add(view, 2);
        new LedControllerParameter("Led : Mpe Settings", view, 2);
        VirtualElement.last.addTarget(ledButtons.get(5));
        VirtualElement.last.setParameterValue(1, LaunchpadX.colorWhite[0], LaunchpadX.colorWhite[1], LaunchpadX.colorWhite[2]);
        Condition.clear();

        // LED : Mpe Play Surface
        Condition.add(view, 2, 1);
        new LedControllerParameter("Led : Mpe", view, 1);
        VirtualElement.last.addTarget(ledButtons.get(5));
        VirtualElement.last.setParameterValue(1, LaunchpadX.colorGreenLight[0], LaunchpadX.colorGreenLight[1], LaunchpadX.colorGreenLight[2]);
        Condition.clear();


    }

}

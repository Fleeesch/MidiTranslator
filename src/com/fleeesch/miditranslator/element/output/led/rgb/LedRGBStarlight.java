package com.fleeesch.miditranslator.element.output.led.rgb;

import java.util.Random;

public class LedRGBStarlight extends LedRGB {

    //************************************************************
    //      Variables
    //************************************************************

    private final int midiAddressL1;
    private final int midiAddressL2;
    private final int midiAddressR1;
    private final int midiAddressR2;

    // 24 - poison green
    // 23 - sky blue
    // 108 - amber
    // 2 - blue
    // 29 - cyan

    Random rnd = new Random();

    //************************************************************
    //      Constructor
    //************************************************************

    public LedRGBStarlight(String pName, int m1, int m2, int lM1, int lM2, int rM1, int rM2) {

        super(pName, m1, m2);

        midiAddressL1 = lM1;
        midiAddressL2 = lM2;

        midiAddressR1 = rM1;
        midiAddressR2 = rM2;

        off();

    }

    //************************************************************
    //      Method : Off
    //************************************************************

    @Override
    public void off() {

        if (lastSentValue != 0x00) device.midi.SendMessage(midiAddress1, midiAddress2, 0x00);

        lastSentValue = 0x00;
    }


    //************************************************************
    //      Method : On
    //************************************************************

    @Override
    public void on() {

        if (!visible) {

            if (lastSentValue != 0x00) device.midi.SendMessage(midiAddress1, midiAddress2, 0x00);

            lastSentValue = 0x00;

            return;
        }

        int ledColor = 23;
        if (lastSentValue != ledColor) {

            device.midi.SendMessage(midiAddress1, midiAddress2, ledColor);
            device.midi.SendMessage(midiAddressL1, midiAddressL2, ledColor);
            device.midi.SendMessage(midiAddressR1, midiAddressR2, ledColor);
        }

        lastSentValue = ledColor;


    }



    //************************************************************
    //      Method : Update
    //************************************************************

    @Override
    public void update() {

        if (sourceFocus == null) {
            setParameterValue(0);
            off();

        } else {

            if (!sourceFocus.parameterControlsLed()) {
                on();
                return;
            }

            if (sourceFocus.parameter.get() > 0) on();
            else off();
        }


    }


}



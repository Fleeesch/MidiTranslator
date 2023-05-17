package com.fleeesch.miditranslator.element.output.led.rgb;

import com.fleeesch.miditranslator.functions.math.Color;

public class LedRGBFaderPort extends LedRGB {

    //************************************************************
    //      Variables
    //************************************************************

    private int lastSentR = -1, lastSentG = -1, lastSentB = -1;

    //************************************************************
    //      Constructor
    //************************************************************

    public LedRGBFaderPort(String pName, int m1, int m2) {

        super(pName, m1, m2);

        setColor(0x7F7F7F);

    }

    //************************************************************
    //      Method : Color Off
    //************************************************************

    public void colorOff() {

        if (lastSentR != 0) device.midi.SendMessage(midiAddress1 + 1, midiAddress2, 0);
        if (lastSentG != 0) device.midi.SendMessage(midiAddress1 + 2, midiAddress2, 0);
        if (lastSentB != 0) device.midi.SendMessage(midiAddress1 + 3, midiAddress2, 0);

        lastSentR = lastSentG = lastSentB = 0;

    }

    //************************************************************
    //      Method : Send Color Values
    //************************************************************

    public void sendColorValues() {

        int valR = (int) getParameterValue(1);
        int valG = (int) getParameterValue(2);
        int valB = (int) getParameterValue(3);

        if (valR != lastSentR) device.midi.SendMessage(midiAddress1 + 1, midiAddress2, valR);
        if (valG != lastSentG) device.midi.SendMessage(midiAddress1 + 2, midiAddress2, valG);
        if (valB != lastSentB) device.midi.SendMessage(midiAddress1 + 3, midiAddress2, valB);

        lastSentR = valR;
        lastSentG = valG;
        lastSentB = valB;

    }

    //************************************************************
    //      Method : Set Color (R G B)
    //************************************************************
    public void setColor(int r, int g, int b) {

        setParameterValue(1, r);
        setParameterValue(2, g);
        setParameterValue(3, b);

        sendColorValues();

    }

    //************************************************************
    //      Method : Set Color (Hex)
    //************************************************************

    public void setColor(int clr) {

        int[] rgb = Color.HexToRgb(clr);

        setColor(rgb[0], rgb[1], rgb[2]);

    }

    //************************************************************
    //      Method : Transfer Color from Source
    //************************************************************

    private void transferColorFromSource() {

        setParameterValue(1, sourceFocus.getParameterValue(1) * 0x7F);
        setParameterValue(2, sourceFocus.getParameterValue(2) * 0x7F);
        setParameterValue(3, sourceFocus.getParameterValue(3) * 0x7F);

        sendColorValues();

    }

    //************************************************************
    //      Method : Update
    //************************************************************

    @Override
    public void update() {

        if (sourceFocus == null) {

            setParameterValue(0);
            colorOff();
            off();

        } else {

            if (!sourceFocus.parameterControlsLed()) {

                transferColorFromSource();

                on();

                return;
            }

            if (sourceFocus.parameter.get() > 0) {
                on();
            } else {
                off();
            }

            transferColorFromSource();

        }

    }

    //************************************************************
    //      Method : Reset Last Sent Value
    //************************************************************

    public void resetLastSentValue() {

        lastSentR = lastSentG = lastSentB = -1;

        super.resetLastSentValue();

    }


}

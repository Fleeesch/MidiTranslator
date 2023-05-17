package com.fleeesch.miditranslator.element.output.led.rgb;

import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.device.hardware.LaunchpadX;
import com.fleeesch.miditranslator.functions.math.Color;

public class LedRGBLaunchpad extends LedRGB {

    //************************************************************
    //      Variables
    //************************************************************

    public int lastSentColor = -1;

    public final int id;

    public int colorR = 0x7F, colorG = 0x7F, colorB = 0x7F, colorA = 0x7F, colorHex = 0x7F7F7F;

    final LaunchpadX device;

    //************************************************************
    //      Constructor
    //************************************************************

    public LedRGBLaunchpad(String pName, int m1, int m2) {

        super(pName, m1, m2);

        device = (LaunchpadX) Device.last;

        id = m2;

        device.launchpadLeds.add(this);

    }

    //************************************************************
    //      Method : Apply Color Values Now
    //************************************************************

    public void sendColorMessage() {

        if (colorHex == lastSentColor) return;

        byte[] msgOut = {(byte) 0x03, (byte) 0x03, (byte) id, (byte) colorR, (byte) colorG, (byte) colorB};

        device.midi.SendSysEx(msgOut);

        lastSentColor = colorHex;

    }


    //************************************************************
    //      Method : Enable
    //************************************************************

    @Override
    public void enableVisibility() {
        super.enableVisibility();

        on();

    }


    //************************************************************
    //      Method : Disable
    //************************************************************

    @Override
    public void disableVisibility() {
        super.disableVisibility();

        off();
    }

    //************************************************************
    //      Method : On
    //************************************************************


    public void on() {

        loadColorFromSource();
        sendColorMessage();

    }

    //************************************************************
    //      Method : Off
    //************************************************************

    @Override
    public void off() {

        setColorToOff();
        sendColorMessage();
    }


    //************************************************************
    //      Method : Set Color to Off
    //************************************************************

    public void setColorToOff() {

        colorHex = colorR = colorG = colorB = 0;

    }

    //************************************************************
    //      Method : Load Color from Source
    //************************************************************

    public void loadColorFromSource() {

        if (sourceFocus == null) {
            setColorToOff();
            return;
        }

        colorA = (int) (sourceFocus.getParameterValue(4) * 0x7F);
        colorR = (int) (sourceFocus.getParameterValue(1) * colorA);
        colorG = (int) (sourceFocus.getParameterValue(2) * colorA);
        colorB = (int) (sourceFocus.getParameterValue(3) * colorA);

        colorHex = Color.RgbToHex(colorR, colorG, colorB);

    }

    //************************************************************
    //      Method : Update
    //************************************************************

    @Override
    public void update() {

        if (sourceFocus == null) {

            setColorToOff();
            sendColorMessage();

        } else {

            if (visible) loadColorFromSource();
            else setColorToOff();

            sendColorMessage();

        }


    }

    //************************************************************
    //      Method : Reset Last Sent Value
    //************************************************************

    public void resetLastSentValue() {

        lastSentColor = -1;

        super.resetLastSentValue();

    }

}



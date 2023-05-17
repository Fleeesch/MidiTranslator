package com.fleeesch.miditranslator.element.output.led.binary;


import com.fleeesch.miditranslator.element.output.led.Led;

public class LedBinary extends Led {


    //************************************************************
    //      Constructor
    //************************************************************

    public LedBinary(String pName, int m1, int m2) {

        super(pName, m1, m2);

    }

    //************************************************************
    //      Method : On
    //************************************************************


    public void on() {


        if (sourceFocus != null && sourceFocus.invertLed) {
            offSend();

            return;
        }

        onSend();

    }

    //************************************************************
    //      Method : Off
    //************************************************************

    public void off() {

        if (sourceFocus != null && sourceFocus.invertLed) {
            onSend();
            return;
        }

        offSend();

    }

    //************************************************************
    //      Method : On Send
    //************************************************************

    private void onSend() {

        if (!visible) {

            if (lastSentValue != 0x00) device.midi.SendMessage(midiAddress1, midiAddress2, 0x00);

            lastSentValue = 0x00;

            return;
        }

        if (lastSentValue != 0x7F) device.midi.SendMessage(midiAddress1, midiAddress2, 0x7F);

        lastSentValue = 0x7F;


    }

    //************************************************************
    //      Method : Off Send
    //************************************************************

    private void offSend() {


        if (lastSentValue != 0x00) device.midi.SendMessage(midiAddress1, midiAddress2, 0x00);

        lastSentValue = 0x00;


    }


    //************************************************************
    //      Method : Select Source Element
    //************************************************************

    @Override
    public void filterSources() {

        super.filterSources();

        update();

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

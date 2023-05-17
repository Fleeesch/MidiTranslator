package com.fleeesch.miditranslator.element.input.encoder;

public class EncoderRelative1 extends Encoder {

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Name + Adress

    public EncoderRelative1(String pName, int m1, int m2) {

        super(pName, m1, m2);

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(double val, int[] msg) {

        // transform data
        if (val >= 0.5) val -= 0.5;
        else val = 0.5 - val;

        super.handleInput(val, msg);


    }

}

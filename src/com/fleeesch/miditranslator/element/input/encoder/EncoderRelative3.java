package com.fleeesch.miditranslator.element.input.encoder;

public class EncoderRelative3 extends Encoder {

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Name + Address

    public EncoderRelative3(String pName, int m1, int m2) {

        super(pName, m1, m2);

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(double val, int[] msg) {

        // transform data
        if (val > 0.5) val = -(val - 0.501);

        super.handleInput(val, msg);

    }

}

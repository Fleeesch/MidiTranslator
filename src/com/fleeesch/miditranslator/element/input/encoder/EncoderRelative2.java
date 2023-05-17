package com.fleeesch.miditranslator.element.input.encoder;

public class EncoderRelative2 extends Encoder {

    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Name + Address

    public EncoderRelative2(String pName, int m1, int m2) {

        super(pName, m1, m2);


    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(double val, int[] msg) {

        // transform data
        val -= 0.5;

        System.out.println(val);

        super.handleInput(val, msg);


    }

}

package com.fleeesch.miditranslator.element.output.led.rgb;

import com.fleeesch.miditranslator.data.external.track.TrackData;
import com.fleeesch.miditranslator.data.external.track.TrackDataSet;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.element.output.led.binary.LedBinary;

public abstract class LedRGB extends LedBinary {


    //************************************************************
    //      Constructor
    //************************************************************

    public LedRGB(String pName, int m1, int m2) {

        super(pName, m1, m2);
        
        parameters.add(new Parameter("R"));
        parameters.add(new Parameter("G"));
        parameters.add(new Parameter("B"));

    }


}

package com.fleeesch.miditranslator.element.virtual.controller.led;

import com.fleeesch.miditranslator.data.external.track.TrackData;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.functions.math.Color;

public class LedControllerTrackColor extends LedController {

    //************************************************************
    //      Variables
    //************************************************************

    public final TrackData track; // specific track data colector

    public double[] offColor; // off state color

    public final double preScale; // scale-factor for dynamic color


    //************************************************************
    //      Constructor
    //************************************************************

    public LedControllerTrackColor(String pName, TrackData pTrack, double pPreScale, int pOffColor) {

        super(pName);

        track = pTrack; // track data

        track.addEventHandler(this); // listen to track data changes

        preScale = Math.min(Math.max(pPreScale, 0), 1); // keep prescale factor within limits

        offColor = Color.HexToRgbDouble(pOffColor); // convert off-color-hex to double array

    }

    //************************************************************
    //      Method : Set Color
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Given Color

    public void setColor(double r, double g, double b) {

        // set parameter values to rgb
        setParameterValue(1, r * preScale);
        setParameterValue(2, g * preScale);
        setParameterValue(3, b * preScale);

        // update leds
        for (OutputElement e : targetElements) e.update();

    }

    //* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    //  Default Color

    public void setColor() {

        setColor(offColor[0], offColor[1], offColor[2]); // use off-color values to set color

    }

    //************************************************************
    //      Event : On Track Data Change
    //************************************************************

    @Override
    public void onTrackDataChange(TrackData pSource) {
        super.onTrackDataChange(pSource);

        track.formatColor(); // format incoming osc message

        // track color invalid? use off-color
        if (track.color == null) {
            setColor();
            return;
        }

        // get doubles from RGB int values
        double r = track.color.getRed() / 255.0;
        double g = track.color.getGreen() / 255.0;
        double b = track.color.getBlue() / 255.0;

        // calculate offset factor for brightness compensation
        double dif = 3.0 - r - g - b;

        // adjust color values for maximum brightness
        r = Math.min(1.0, r * dif);
        g = Math.min(1.0, g * dif);
        b = Math.min(1.0, b * dif);

        // set color using doubles
        setColor(r, g, b);

    }
}

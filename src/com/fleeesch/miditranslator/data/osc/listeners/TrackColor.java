package com.fleeesch.miditranslator.data.osc.listeners;

import com.fleeesch.miditranslator.data.external.track.TrackData;
import com.fleeesch.miditranslator.data.osc.OscListener;
import com.illposed.osc.OSCMessageEvent;
import com.illposed.osc.argument.OSCColor;


public class TrackColor extends OscListener {

    //************************************************************
    //      Variables
    //************************************************************

    public final TrackData targetTrack;

    //************************************************************
    //      Constructor
    //************************************************************

    public TrackColor(TrackData pElement) {

        targetTrack = pElement;

    }

    //************************************************************
    //      Methods
    //************************************************************

    @Override
    public void acceptMessage(OSCMessageEvent event) {

        targetTrack.inputColor(event.getMessage().getArguments().get(0));

    }
}

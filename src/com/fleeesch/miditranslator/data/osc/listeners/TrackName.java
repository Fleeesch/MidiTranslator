package com.fleeesch.miditranslator.data.osc.listeners;

import com.fleeesch.miditranslator.data.external.track.TrackData;
import com.fleeesch.miditranslator.data.osc.OscListener;
import com.illposed.osc.OSCMessageEvent;


public class TrackName extends OscListener {

    //************************************************************
    //      Variables
    //************************************************************

    public final TrackData targetTrack;

    //************************************************************
    //      Constructor
    //************************************************************

    public TrackName(TrackData pElement) {

        targetTrack = pElement;

    }

    //************************************************************
    //      Methods
    //************************************************************

    @Override
    public void acceptMessage(OSCMessageEvent event) {

        targetTrack.inputName(event.getMessage().getArguments().get(0));

    }
}

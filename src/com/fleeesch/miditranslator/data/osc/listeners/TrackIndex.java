package com.fleeesch.miditranslator.data.osc.listeners;

import com.fleeesch.miditranslator.data.external.track.TrackData;
import com.fleeesch.miditranslator.data.osc.OscListener;
import com.illposed.osc.OSCMessageEvent;


public class TrackIndex extends OscListener {

    //************************************************************
    //      Variables
    //************************************************************

    public final TrackData targetTrack;

    //************************************************************
    //      Constructor
    //************************************************************

    public TrackIndex(TrackData pElement) {

        targetTrack = pElement;

    }

    //************************************************************
    //      Methods
    //************************************************************

    @Override
    public void acceptMessage(OSCMessageEvent event) {

        try {
            targetTrack.inputIndex(event.getMessage().getArguments().get(0));
        } catch (Exception e) {
            return;
        }


    }
}

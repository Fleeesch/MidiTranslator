package com.fleeesch.miditranslator.data.external.track;

import com.fleeesch.miditranslator.data.external.DataSet;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.element.virtual.controller.display.DisplayController;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;

import java.util.ArrayList;
import java.util.List;


public class TrackDataSet extends DataSet {

    //************************************************************
    //      Variables
    //************************************************************

    // count of tracks
    public int trackCount;

    // track data objects
    public final List<TrackData> tracks = new ArrayList<>();

    // display controllers
    public final List<DisplayController> displayControllers = new ArrayList<>();

    // created device
    public final Device device;


    //************************************************************
    //      Constructor
    //************************************************************

    public TrackDataSet(Device d, int pTrackCount) {

        device = d; // store device

        trackCount = pTrackCount; // store track count

        // create track data entries
        for (int i = 0; i < trackCount; i++) tracks.add(new TrackData(this, i));

        // add osc listener to receive data
        device.osc.get(0).addListener(OscAddress.trackAddress, this);


    }

    //************************************************************
    //      Method : Format Data
    //************************************************************

    public void formatData() {

        for (TrackData t : tracks) t.formatData();

    }


}

package com.fleeesch.miditranslator.data.external.track;

import com.fleeesch.miditranslator.event.EventHandler;
import com.fleeesch.miditranslator.event.EventSource;
import com.illposed.osc.argument.OSCColor;

import java.awt.*;

public class TrackData extends EventSource {


    //************************************************************
    //      Variables
    //************************************************************

    // physical index
    public final int dataIndex;
    // formatted data
    public String name;
    public String index;
    public double peak;
    public Color color = new Color(255,255,255);

    // original dataset
    final TrackDataSet dataSet;
    // temporary data storage (straight from osc)
    Object bufferName;
    Object bufferIndex;
    Object bufferPeak;
    Object bufferColor;


    //************************************************************
    //      Constructor
    //************************************************************

    public TrackData(TrackDataSet pDataSet, int pIdx) {

        dataIndex = pIdx; // store track physical index

        // init data
        name = "";
        index = "";
        peak = 0;

        bufferName = "";
        bufferIndex = "";
        bufferPeak = (float) 0;

        dataSet = pDataSet; // store original data set

    }

    //************************************************************
    //      Method : Input Name
    //************************************************************

    public void inputName(Object pName) {
        bufferName = pName;
        dataSet.newData();
    }

    //************************************************************
    //      Method : Input Index
    //************************************************************

    public void inputIndex(Object pIndex) {
        bufferIndex = pIndex;
        dataSet.newData();

    }

    //************************************************************
    //      Method : Input Index
    //************************************************************

    public void inputColor(Object pColor) {

        bufferColor = pColor;
        for(EventHandler e : eventHandlers)e.onTrackDataChange(this); // trigger data change event
    }

    //************************************************************
    //      Method : Input Peak
    //************************************************************

    public void inputPeak(Object pPeak) {
        bufferPeak = pPeak;

    }

    //************************************************************
    //      Method : Format Data
    //************************************************************

    public void formatData() {

        name = (String) bufferName;
        index = (String) bufferIndex;

    }

    //************************************************************
    //      Method : Format Track Color
    //************************************************************

    public void formatColor() {

        // clear color if buffer is empty, skip rest
        if (bufferColor == null) {
            color = null;
            return;
        }

        OSCColor oscClr = (OSCColor) bufferColor; // try to translate color message

        color = new Color(oscClr.getRedInt(), oscClr.getGreenInt(), oscClr.getBlueInt()); // create java color from osc color RGB values


    }

}

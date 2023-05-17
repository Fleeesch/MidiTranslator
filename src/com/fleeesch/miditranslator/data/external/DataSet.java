package com.fleeesch.miditranslator.data.external;


import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.event.EventHandler;
import com.fleeesch.miditranslator.event.EventSource;

import java.util.List;

public abstract class DataSet extends EventSource {

    //************************************************************
    //      Variables
    //************************************************************

    // base osc address for receiving data
    public String oscAddress;


    //************************************************************
    //      Method : Link To Feedback (OSC)
    //************************************************************

    public void linkToFeedback(Osc pOsc, String pAdr) {

        oscAddress = pAdr; // store osc feedback address

        pOsc.addListener(pAdr, this); // create osc message listener


    }

    //************************************************************
    //      Method : OSC Message Input
    //************************************************************

    public void oscInput(List<Object> data) {


    }


    //************************************************************
    //      Method : Peak
    //************************************************************

    public void peakChange() {

        for(EventHandler e : eventHandlers)e.onPeakChange(this);

    }

    //************************************************************
    //      Method : New data
    //************************************************************

    public void newData() {

        for(EventHandler e : eventHandlers)e.onDataChange(this);

    }

    //************************************************************
    //      Method : Format Data
    //************************************************************

    public void formatData() {

        // placeholder

    }

}

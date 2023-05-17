package com.fleeesch.miditranslator.data.osc;

import com.illposed.osc.MessageSelector;
import com.illposed.osc.OSCMessageEvent;

/*

Straight implementation of MessageSelector, no addition

 */

public abstract class OscMessageSelector implements MessageSelector {

    //************************************************************
    //      Get : Is info Required
    //************************************************************

    @Override
    public boolean isInfoRequired() {
        return false;
    }

    //************************************************************
    //      Get : Matches
    //************************************************************

    @Override
    public boolean matches(OSCMessageEvent messageEvent) {
        return true;
    }
}

package com.fleeesch.miditranslator.data.osc.listeners;

import com.fleeesch.miditranslator.data.external.fx.FxData;
import com.fleeesch.miditranslator.data.osc.OscListener;
import com.illposed.osc.OSCMessageEvent;


public class FxName extends OscListener {

    //************************************************************
    //      Variables
    //************************************************************

    public final FxData targetFx;

    //************************************************************
    //      Constructor
    //************************************************************

    public FxName(FxData pElement) {

        targetFx = pElement;

    }

    //************************************************************
    //      Methods
    //************************************************************

    @Override
    public void acceptMessage(OSCMessageEvent event) {

        try {
            targetFx.inputName(event.getMessage().getArguments().get(0));
        } catch (Exception e) {
            return;
        }

    }
}

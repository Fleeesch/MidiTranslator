package com.fleeesch.miditranslator.element.builder;

import com.fleeesch.miditranslator.element.Element;
import com.fleeesch.miditranslator.event.EventHandler;

public abstract class Builder extends Element implements EventHandler {

    //************************************************************
    //      Variables
    //************************************************************

    final String name; // name of builder

    //************************************************************
    //      Constructor
    //************************************************************

    public Builder(String pName) {

        super(); // use original Constructor

        name = pName; // store name

    }


}

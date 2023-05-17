package com.fleeesch.miditranslator.data.lookup.table.launchpadx;

import com.fleeesch.miditranslator.data.lookup.table.ValueTable;

public class LpxAftertouchLookup extends ValueTable {

    //************************************************************
    //      Variables
    //************************************************************

    public static final ValueTable table = new LpxAftertouchLookup("Launchpad X Pad Pressure Lookup");

    //************************************************************
    //      Constructor
    //************************************************************

    public LpxAftertouchLookup(String pName) {

        super(pName);

        generateValues(128, 1.5, 1);

    }

}

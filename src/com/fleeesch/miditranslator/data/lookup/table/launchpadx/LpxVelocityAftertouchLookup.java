package com.fleeesch.miditranslator.data.lookup.table.launchpadx;

import com.fleeesch.miditranslator.data.lookup.table.ValueTable;

public class LpxVelocityAftertouchLookup extends ValueTable {

    //************************************************************
    //      Variables
    //************************************************************

    public static final ValueTable table = new LpxVelocityAftertouchLookup("Launchpad X Pad Pressure Lookup");

    //************************************************************
    //      Constructor
    //************************************************************

    public LpxVelocityAftertouchLookup(String pName) {

        super(pName);

        generateValues(128, 4.5, 1);

    }

}

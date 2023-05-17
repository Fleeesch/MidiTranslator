package com.fleeesch.miditranslator.element.output.display.fp8;

import com.fleeesch.miditranslator.element.output.display.Display;

import java.util.ArrayList;
import java.util.List;

public class FP8DisplaySet extends Display {

    //************************************************************
    //      Variables
    //************************************************************

    // displays within the set
    public final List<FP8DisplayUnit> displays = new ArrayList<>();

    // current display mode
    public int mode = 0;


    //************************************************************
    //      Constructor
    //************************************************************

    public FP8DisplaySet() {

        super();

        // create display instances
        for (int i = 0; i < 8; i++) displays.add(new FP8DisplayUnit(i));


    }


    //************************************************************
    //      Method : Init
    //************************************************************

    public void init() {

        for (FP8DisplayUnit d : displays) d.init();

    }

    //************************************************************
    //      Method : Clear Row
    //************************************************************

    public void clearRow(int pRow){

        for (FP8DisplayUnit d : displays) d.print(pRow,0,"");

    }

    //************************************************************
    //      Method : Clear Displays
    //************************************************************

    public void clearDisplays() {

        for (FP8DisplayUnit d : displays) d.clearDisplay();

    }

    //************************************************************
    //      Method : Set Display Mode
    //************************************************************

    public void setDisplayMode(int pMode, boolean pDel) {

        for (FP8DisplayUnit d : displays) d.setDisplayMode(pMode, pDel);

    }


}

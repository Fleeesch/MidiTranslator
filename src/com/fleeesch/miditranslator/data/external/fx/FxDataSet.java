package com.fleeesch.miditranslator.data.external.fx;

import com.fleeesch.miditranslator.data.external.DataSet;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.element.virtual.controller.display.DisplayController;

import java.util.ArrayList;
import java.util.List;

public class FxDataSet extends DataSet {

    //************************************************************
    //      Variables
    //************************************************************

    public final int fxCount;

    // track data objects
    public final List<FxData> fx = new ArrayList<>();
    public final FxData fxInstance;


    // display controllers
    public final List<DisplayController> displayControllers = new ArrayList<>();

    // created device
    public final Device device;

    //************************************************************
    //      Constructor
    //************************************************************

    public FxDataSet(Device d, int pFxCount, String pAdress) {

        device = d; // store device

        fxCount = pFxCount; // store track count

        // create track data entries
        for (int i = 0; i < fxCount; i++) fx.add(new FxData(this, i));

        fxInstance = new FxData(this, -1);

        // add osc listener to receive data
        device.osc.get(0).addListener(pAdress, this);


    }

    //************************************************************
    //      Method : Format Data
    //************************************************************

    public void formatData() {

        for (FxData f : fx) f.formatData();

        fxInstance.formatData();

    }


}

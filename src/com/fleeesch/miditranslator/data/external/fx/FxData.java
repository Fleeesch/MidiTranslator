package com.fleeesch.miditranslator.data.external.fx;

public class FxData {
    //************************************************************
    //      Variables
    //************************************************************

    // physical index
    public final int dataIndex;
    // formatted data
    public String name;
    // original dataset
    final FxDataSet dataSet;
    // temporary data storage (straight from osc)
    Object bufferName;

    //************************************************************
    //      Constructor
    //************************************************************

    public FxData(FxDataSet pDataSet, int pIdx) {

        dataIndex = pIdx; // store track physical index

        // init data
        name = "";

        bufferName = "";

        dataSet = pDataSet; // store original data set

    }


    //************************************************************
    //      Method : Input Name
    //************************************************************

    public void inputName(Object pName) {
        bufferName = pName;

        dataSet.newData(); // pass index to keep track of last fx
    }


    //************************************************************
    //      Method : Format Data
    //************************************************************

    public void formatData() {

        name = (String) bufferName;

    }

}

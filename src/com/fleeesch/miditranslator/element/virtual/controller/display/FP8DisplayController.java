package com.fleeesch.miditranslator.element.virtual.controller.display;

import com.fleeesch.miditranslator.data.external.DataSet;
import com.fleeesch.miditranslator.data.external.fx.FxDataSet;
import com.fleeesch.miditranslator.data.lookup.midi.MidiAddress;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.device.software.DAW;
import com.fleeesch.miditranslator.element.output.display.fp8.FP8DisplaySet;
import com.fleeesch.miditranslator.functions.string.Text;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class FP8DisplayController extends DisplayController {

    //************************************************************
    //      Variables
    //************************************************************

    // data
    public final DAW dawDataSource;
    public final List<DataSet> dataSets = new ArrayList<>();

    // display set
    public final FP8DisplaySet displaySet;

    // Timer for delayed display updates
    public final Timer updateTimer;
    public final ActionListener updateTask;

    // indicator that display update is currently going
    private boolean doUpdate = false;

    // parameter hashmap

    public final List<Parameter> parameters = new ArrayList<>();

    // buffered parameter data
    public int faderMode = 0;
    public int mixBank = 0;
    public int freeBank = 0;
    public int trackBank = 0;
    public int fxBank = 0;
    public int presetBank = 0;
    public int shift;
    public int bankExtender = 0;
    public int midiBank = 0;

    // last display mode (preventing unnecessary redraws)
    public int menu = -1;



    private final int maxCharacters = 5;

    private final int displayUpdateDelay = 100;

    // mix labels
    final String[] labelsMix = {"Volume", "Pan", "Width", "Send 1", "Send 2", "Send 3", "Send 4", "Send 5"};

    //************************************************************
    //      Constructor
    //************************************************************

    public FP8DisplayController(String pName, FP8DisplaySet pDisplaySet, DAW pDaw) {

        super(pName); // original constructor

        dawDataSource = pDaw;

        // display set
        displaySet = pDisplaySet;

        // get data sets
        dataSets.add(dawDataSource.freeData);
        dataSets.add(dawDataSource.freeSwitchData);
        dataSets.add(dawDataSource.presetData);
        dataSets.add(dawDataSource.presetSwitchData);
        dataSets.add(dawDataSource.trackData);
        dataSets.add(dawDataSource.fxData);

        // data storage event handler
        for (DataSet ds : dataSets) ds.addEventHandler(this);

        // Task for printing Data to Display Set
        updateTask = evt -> update();

        // setup timer for display output
        updateTimer = new Timer(displayUpdateDelay, updateTask);
        updateTimer.setRepeats(false);

        // set display mode to 2, clear all display data
        displaySet.setDisplayMode(2, true);

        linkToSettings(device.settings);

    }

    //************************************************************
    //      Method Store Data
    //************************************************************

    public void storeData() {

        // store parameter data
        faderMode = (int) settings.getParameterByName("Fader Mode").get();
        mixBank = (int) settings.getParameterByName("Mix Mode Bank").get();
        freeBank = (int) settings.getParameterByName("Free Mode Bank").get();
        trackBank = (int) settings.getParameterByName("Track Mode Bank").get();
        fxBank = (int) settings.getParameterByName("FX Mode Bank").get();
        presetBank = (int) settings.getParameterByName("Preset Mode Bank").get();
        midiBank = (int) settings.getParameterByName("MIDI Mode Bank").get();
        shift = (int) settings.getParameterByName("Shift").get();
        bankExtender = (int) settings.getParameterByName("Track Bank").get();

    }

    //************************************************************
    //      Method : Update
    //************************************************************

    public void update() {


        doUpdate = false; // update is starting, lower flag

        storeData(); // transfer data from parameters

        for (DataSet d : dataSets) d.formatData(); // format dataset data for output

        switch (faderMode) {
            // :: Mixing ::
            case 1, 2 -> changeMenu(0);
            // :: Preset ::
            case 5 -> changeMenu(1);
            // :: Plugin ::
            case 3 -> changeMenu(2);
            // :: Free ::
            case 0 -> changeMenu(3);
            // :: MIDI ::
            case 4 -> changeMenu(4);
        }

        switch (menu) {
            // :: Mixing ::
            case 0 -> displayTrackLabels();

            // :: Preset ::
            case 1 -> {
                int idx = presetBank * 8 + bankExtender * 64;

                if (shift <= 0) displayDataSetLabels(dawDataSource.presetData, 0, 2, idx);
                else displayDataSetLabels(dawDataSource.presetSwitchData, 0, 2, idx);

            }
            // :: Plugin ::
            case 2 -> {

                int idx = fxBank * 8 + bankExtender * 64;

                displayDataSetLabels(dawDataSource.fxData, 0, 2, idx);
            }
            // :: Free ::
            case 3 -> {

                int idx = freeBank * 8 + bankExtender * 64;

                if (shift <= 0) displayDataSetLabels(dawDataSource.freeData, 0, 2, idx);
                else displayDataSetLabels(dawDataSource.freeSwitchData, 0, 2, idx);


            }
            // :: MIDI ::
            case 4 -> displayMidiLabels(0);


        }

        // display update query triggered during update? update again...
        if(doUpdate){
            update();
        }




    }

    //************************************************************
    //      Method : Change Menu
    //************************************************************

    // * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // Change Menu

    private void changeMenu(int pMenu, boolean force) {

        if (!force && menu == pMenu) return;

        for (int i = 0; i < 8; i++) displaySet.displays.get(i).clearDisplay();

        displaySet.setDisplayMode(3, true); // clear upper row

        menu = pMenu;


    }

    // * * * * * * * * * * * * * * * * * * * * * * * * * * *
    // Change Menu (ignore if the same)i

    public void changeMenu(int pMenu) {

        changeMenu(pMenu, false);
    }

    //************************************************************
    //      Method : Reload Menu
    //************************************************************

    public void reloadMenu() {

        changeMenu(menu, true);

    }

    //************************************************************
    //      Method : Display MIDI Labels
    //************************************************************

    public void displayMidiLabels(int pLineOffset) {


        int chan = (midiBank + 1 + bankExtender * 8);

        displaySet.displays.get(0).print(0, 0x04, "CH");
        displaySet.displays.get(1).print(0, 0x04, chan + "");

        for (int i = 0; i < 8; i++) {

            // print midi information

            if (shift <= 0)
                displaySet.displays.get(i).print(1, 0x00, MidiAddress.surfaceMidiModeCC[i] + "");
            else displaySet.displays.get(i).print(1, 0x00, MidiAddress.surfaceMidiModeSwitch[i] + "");

        }


    }

    //************************************************************
    //      Method : Display DataSet Index
    //************************************************************

    public void displayIndex(int pLineOffset, int pIndex) {

        for (int i = 0; i < 8; i++) displaySet.displays.get(i).print(pLineOffset, 0, (pIndex + i + 1) + "");

    }

    //************************************************************
    //      Method : Display DataSet Labels
    //************************************************************

    public void displayDataSetLabels(FxDataSet pDataSet, int pLineOffset, int pLineCount, int pIndex) {

        for (int i = 0; i < 8; i++) {

            String name = pDataSet.fx.get(pIndex + i).name;
            String[] lines = Text.splitFixedDimensions(name, " ", pLineCount, pLineCount, maxCharacters, true);

            displaySet.displays.get(i).print(pLineOffset, 0x00, lines);

        }

    }

    //************************************************************
    //      Method : Display Mix Labels
    //************************************************************

    private void displayMixLabels() {

        for (int i = 0; i < 8; i++) displaySet.displays.get(i).print(3, 0x04, labelsMix[i]);

    }

    //************************************************************
    //      Method : Clear Header Info
    //************************************************************

    public void clearHeaderInfo() {

        // clear everything on line 1 for displays 3 to 8
        for (int i = 0; i < 8; i++) displaySet.displays.get(i).print(0, 0x00, "");

    }

    //************************************************************
    //      Method : Display Header Info
    //************************************************************

    public void displayHeaderInfo(String... pStr) {

        if (pStr.length <= 1) {

            String[] print = Text.splitFixedDimensions(pStr[0], "", 8, 8, maxCharacters, true);

            for (int i = 0; i < 8; i++) {

                int flag = 4;
                if (print[i].isEmpty()) flag = 0;

                displaySet.displays.get(i).print(0, flag, print[i]);
            }

            return;

        }

        int l = Math.min(pStr.length, 8);

        for (int i = 0; i < l; i++)
            displaySet.displays.get(i).print(0, 0x04, Text.limitTextLength(pStr[i], maxCharacters));
        for (int i = l; i < 8; i++) displaySet.displays.get(i).print(0, 0, " ");

    }

    //************************************************************
    //      Method : Display Track Labels
    //************************************************************

    public void displayTrackLabels() {

        int idx = 0;

        if (bankExtender > 0) idx += 8;

        for (int i = 0; i < 8; i++) {

            String name = dawDataSource.trackData.tracks.get(idx + i).name;

            String[] lines = Text.splitFixedDimensions(name, " ", 4, 4, maxCharacters, true);

            displaySet.displays.get(i).print(0, 0, lines);

        }

    }

    //************************************************************
    //      Method : Handle Display Update
    //************************************************************

    public void queryDisplayUpdate() {

        // mark that update is about to be started
        doUpdate = true;

        // restart timer
        updateTimer.restart();

    }


    //************************************************************
    //      Event : On Data Change
    //************************************************************

    @Override
    public void onDataChange(DataSet pSource) {

        queryDisplayUpdate();

    }


    //************************************************************
    //      Event : On Settings Change
    //************************************************************

    public void onSettingsChange(Settings pSettings, Parameter pParameter) {

        queryDisplayUpdate();

    }


}

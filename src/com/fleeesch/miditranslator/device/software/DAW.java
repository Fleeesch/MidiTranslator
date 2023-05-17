package com.fleeesch.miditranslator.device.software;

import com.fleeesch.miditranslator.action.osc.SendOscDirect;
import com.fleeesch.miditranslator.data.external.fx.FxDataSet;
import com.fleeesch.miditranslator.data.external.track.TrackDataSet;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;
import com.fleeesch.miditranslator.data.midi.Midi;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.controller.osc.OscFeedbackController;
import com.fleeesch.miditranslator.element.virtual.controller.osc.OscRequestFaderPort16;

//* * * * * * * * * * * * * * * * * * * * * * * *
//  Class

public class DAW extends Device {

    //************************************************************
    //      Variables
    //************************************************************

    public final TrackDataSet trackData;
    public final FxDataSet fxData;
    public final FxDataSet presetData;
    public final FxDataSet presetSwitchData;
    public final FxDataSet freeData;
    public final FxDataSet freeSwitchData;

    public static boolean allowSoftwareInput;

    public final int oscPortArrangeIn = 7416;
    public final int oscPortArrangeOut = 7616;
    public final int oscPortControlIn = 9000;
    public final int oscPortControlOut = 7500;

    //************************************************************
    //      Constructor
    //************************************************************

    public DAW() {

        // meta data
        name = "DAW";
        vendor = "Cockos";
        product = "Reaper";

        // don't' show DAW in gui (doesn't require reconnecting)
        showInGui = false;

        // add osc ports
        osc.add(new Osc(this, "127.0.0.1", oscPortArrangeIn, oscPortArrangeOut));
        osc.add(new Osc(this, "127.0.0.1", oscPortControlIn, oscPortControlOut));

        // create track data collector, link to osc port
        trackData = new TrackDataSet(this, 128);
        fxData = new FxDataSet(this, 128, OscAddress.fxAddress);
        freeData = new FxDataSet(this, 128, OscAddress.freeAddress);
        freeSwitchData = new FxDataSet(this, 128, OscAddress.freeAddress + "/sw");
        presetData = new FxDataSet(this, 128, OscAddress.presetAddress);
        presetSwitchData = new FxDataSet(this, 128, OscAddress.presetAddress + "/sw");

        // add midi ports to setup
        midiInList.add("Virtual Midi Port Feedback");
        midiOutList.add("Virtual Midi Port 1");
        midiOutList.add("Virtual Midi Port 2");

        // setup device
        setupDevice();

    }

    //************************************************************
    //      Method : Setup Device
    //************************************************************

    public void setupDevice() {

        if (isSetup) return; // don't setup if already done

        Device.setLast(this);

        if (setupMidi()) return; // try to setup midi, quit on error

        Osc.Daw = osc;
        Osc.DawArrange = osc.get(0);
        Osc.DawControl = osc.get(1);

        Midi.Daw = midi;
        Midi.DawArrange = midi.outList.get(0);
        Midi.DawControl = midi.outList.get(1);
        Midi.DawFeedback = midi.inList.get(0);

        setupControls();

        super.setupDevice(); // finish setup

    }

    //************************************************************
    //      Method : Setup Controls
    //************************************************************

    public void setupControls() {

        // --- Refresh Record Scope ---

        new OscFeedbackController("Record Scope Refresh", true);
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.recordScopeFree);
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.recordScopeTime);
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.recordScopeItem);
        VirtualElement.last.addAction(new SendOscDirect(0, OscAddress.refreshRecordScope));

        // --- Refresh Track Data ---

        new OscFeedbackController("Track Data Refresh", false);
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackMute + "/*");
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackSolo + "/*");
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.trackArm + "/*");
        VirtualElement.last.addAction(new SendOscDirect(0, OscAddress.refreshTrackData));

        // --- Request FaderPort 16 ---

        new OscRequestFaderPort16("Request FaderPort16");
        VirtualElement.last.linkToFeedback(Osc.DawArrange, OscAddress.requestIsFP16);

    }

}

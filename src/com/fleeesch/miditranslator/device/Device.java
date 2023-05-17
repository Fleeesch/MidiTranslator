package com.fleeesch.miditranslator.device;

import com.fleeesch.miditranslator.data.midi.Midi;
import com.fleeesch.miditranslator.data.midi.device.MidiDAW;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.device.software.DAW;
import com.fleeesch.miditranslator.element.Element;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.output.led.Led;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.file.config.Configuration;
import com.fleeesch.miditranslator.functions.math.Convert;

import java.util.ArrayList;
import java.util.List;


public abstract class Device {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    static public final List<Device> list = new ArrayList<>(); // list of devices

    static public Device last; // last created device

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public


    // ::: Meta Data :::

    public String name = "";
    public String vendor = "";
    public String product = "";

    // ::: Midi / Osc :::

    // midi handler
    public Midi midi;

    // midi device name list
    public final List<String> midiInList = new ArrayList<>();
    public final List<String> midiOutList = new ArrayList<>();

    // midi address lookup
    public final InputElement[] midiInputLookup = new InputElement[16384];
    public final VirtualElement[] midiVirtualLookup = new VirtualElement[16384];
    public final VirtualElement[] midiVirtualLookupLSB = new VirtualElement[16384];
    public final OutputElement[] midiOutputLookup = new OutputElement[16384];

    // sysex header
    public byte[] sysExHeader;
    public boolean sysexForceOpenClose = false;

    // osc handlers
    public final List<Osc> osc = new ArrayList<>();

    // ::: Parameters / Conditions :::

    // parameters
    public final List<Parameter> parameters = new ArrayList<>();

    // conditions
    public final List<Condition> conditions = new ArrayList<>();


    // ::: Elements :::

    // elements
    public final List<Element> elements = new ArrayList<>();
    public final List<VirtualElement> virtualElements = new ArrayList<>();
    public final List<InputElement> inputElements = new ArrayList<>();
    public final List<OutputElement> outputElements = new ArrayList<>();
    public final List<Led> led = new ArrayList<>();
    public final List<InputElement> button = new ArrayList<>();
    public final List<InputElement> encoder = new ArrayList<>();
    public final List<InputElement> fader = new ArrayList<>();
    public final List<InputElement> motorFader = new ArrayList<>();

    // pending elements
    public final List<VirtualElement> pendingVirtualElements = new ArrayList<>();
    public final List<VirtualElement> pendingListeners = new ArrayList<>();


    // ::: Settings :::

    public Settings settings; // settings, one allowed

    // ::: Availability :::

    public final Parameter enabled = new Parameter("Enabled"); // enabled state parameter (for storage via configuration)
    public boolean enabledState = true; // enabled state (for preventing setup of device / midi+osc data)
    public boolean ignoreEnableSettings = false; // force enable the device (ignore settings)

    // availability
    public boolean available = false;

    // show in gui
    public boolean showInGui = true;

    // device has finished the setup
    public boolean isSetup = false;


    // channel count

    //************************************************************
    //      Constructor
    //************************************************************

    public Device() {

        Condition.clear(); // clear existing condition (just in case)

        enabled.value = 1; // set to 1 to avoid config override

        Device.last = this; // store last device

        Device.list.add(this); // add device to list

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Static
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    //************************************************************
    //      Static Method : Set last
    //************************************************************

    public static void setLast(Device d) {
        last = d;
    }

    //************************************************************
    //      Static Method : Add Device
    //************************************************************

    public static Device add(Device d) {

        return d;
    }

    //************************************************************
    //      Static Method : List Available Elements
    //************************************************************

    public static void updateAllOutputElements() {

        for (Device d : list) d.updateOutputElements(); // update all output elements of all devices

    }

    //************************************************************
    //      Static Method : Update all Output Elements
    //************************************************************

    public void filterAvailableElements() {

        for (InputElement e : inputElements)
            e.filterTargets();

        for (OutputElement e : outputElements)
            e.filterSources();

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Public
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Initiate Setup
    //************************************************************

    public boolean initiateSetup() {

        if (isSetup) return true; // skip whole setup if already done

        Configuration.loadDeviceEnabledState(this); // load device enabled state

        if (!enabledState) {
            setEnabledState(false); // prevents config overwrite
            return true; // don't setup if disabled
        }

        if (setupMidi()) return true; // try to setup midi, skip if an error occurred

        Device.setLast(this); // this one is the last device -> needed for mappings

        return false; // setup done
    }

    //************************************************************
    //      Method : Request Channel Count
    //************************************************************
    public boolean isFaderPort16() {
        return false;
    }

    //************************************************************
    //      Method : Set Enabled State
    //************************************************************

    public void setEnabledState(boolean pState) {

        // state is enabled
        if (pState) {
            enabled.set(1); // set parameter to 1 (gets stored in config)
            enabledState = true; // enable device
            updateOutputElements(); // update leds

        } else {

            // state is disabled

            enabled.set(0); // set parameter to 0 (gets stored in config)
            enabledState = false; // disable device

            disableOutputElements(); // disable leds

        }

    }

    //************************************************************
    //      Method : Toggle Enabled State
    //************************************************************

    public void toggleEnabledState() {

        setEnabledState(!enabledState); // trigger with inverted state

    }

    //************************************************************
    //      Method : Setup Configuration
    //************************************************************

    public void setupConfiguration() {

        // only if device has settings
        if (settings == null) return;

        // link device to settings
        Configuration.addDevice(this);

        // store enabled flag
        enabled.storeInConfig(true);
        settings.addParameter(enabled);

    }

    //************************************************************
    //      Method : Setup Device
    //************************************************************
    public void setupDevice() {

        if (isSetup) return; // only setup if needed

        Configuration.loadDeviceSettings(this); // load device settings

        enabled.set(1); // set enabled to 1, forcing a settings store query

        Parameter.updateAllOfDevice(this); // update parameters

        updateOutputElements(); // refresh leds / displays

        isSetup = true; // setup was a success

    }

    //************************************************************
    //      Method : Disable Output Elements
    //************************************************************

    public void disableOutputElements() {

        // go through LEDs
        for (Led l : led) {

            l.resetLastSentValue();

            l.disableVisibility();

            //l.off(); // turn off led

        }

    }

    //************************************************************
    //      Method : Update Output Elements
    //************************************************************

    public void updateOutputElements() {

        // go through LEDs
        for (Led l : led) {

            l.resetLastSentValue(); // reset last sent values

            l.enableVisibility();

            //l.on();

            l.update(); // update LEDs
        }

        Condition.checkDeviceConditions(this); // check all shift conditions of device

    }


    //************************************************************
    //      Method : Setup Midi
    //************************************************************

    public boolean setupMidi() {

        // DAW needs a special midi handler (to route feedback input through a virtual cable)
        if (this instanceof DAW) midi = new MidiDAW(this);
        else midi = new Midi(this);

        // try to add ins and outs, quit if failed
        for (String n : midiInList) if (midi.addInput(n)) return true;
        for (String n : midiOutList) if (midi.addOutput(n)) return true;

        return midi.openAllPorts(); // try to open ports, quit if failed

    }

    //************************************************************
    //      Event : On Midi Restart
    //************************************************************

    public void onMidiRestart() {
        // placeholder
    }

    //************************************************************
    //      Method : Handle Midi Input
    //************************************************************

    public void handleMidiInput(int adr, double val, int[] midiMsg) {

        // quit if address isn't used
        if (midiInputLookup[adr] == null) return;

        // tell input element at address to handle the input (passing the midi message, too)
        midiInputLookup[adr].handleInput(val, midiMsg);

    }


    //************************************************************
    //      Method : Handle Software Input
    //************************************************************

    public void handleSoftwareInput(int adr, double val) {

        if (!enabledState) return; // skip if device is disabled

        if (midiVirtualLookup[adr] == null) return; // check if address has a virtual element

        midiVirtualLookup[adr].handleSoftwareInput(val); // handle input of virtual element via the feedback method

    }

    //************************************************************
    //      Method : Add Midi Input Lookup
    //************************************************************

    public void AddMidiInputLookup(InputElement ie, int m1, int m2) {

        int adr = (m1 - 0x7F) * 128 + m2; // calculate address from data bytes

        midiInputLookup[adr] = ie; // place element at address

    }

    //************************************************************
    //      Method : Add Midi Output Lookup
    //************************************************************

    public void AddMidiOutputLookup(OutputElement ie, int m1, int m2) {

        int adr = (m1 - 0x7F) * 128 + m2; // calculate address from data bytes

        midiOutputLookup[adr] = ie; // place element at address

    }


    /*

    ~~~ Pending Elements ~~~

    If an Input Element is a pressed Button it is added to a list of pending Virtual Elements,
    and removed if released. Killing all Pending Virtual Elements is enforced on Condition Changes
    to remove the possibility of hanging buttons.

     */

    //************************************************************
    //      Method : Notify Pending Elements
    //************************************************************

    public void notifyPendingVirtualElements(VirtualElement e) {

        for (VirtualElement pe : pendingListeners) pe.onPendingEvent(e);

    }

    //************************************************************
    //      Method : Add Pending Button Press
    //************************************************************

    public void addPendingVirtualElement(VirtualElement e) {

        if(e.isLatch())return; // ignore latch elements

        for (VirtualElement pe : pendingListeners) pe.onPendingEvent(e); // call event listeners

        pendingVirtualElements.add(e); // add ongoing button press

    }

    //************************************************************
    //      Method : Remove Pending Button Press
    //************************************************************

    public void removePendingVirtualElement(VirtualElement e) {

        if(e.isLatch())return; // ignore latch elements

        for (VirtualElement pe : pendingListeners) pe.onPendingEvent(e); // call event listeners

        pendingVirtualElements.remove(e); // remove ongoing button press

    }

    //************************************************************
    //      Method : Kill Pending Buttons
    //************************************************************

    public void killPendingVirtualElement() {

        // go through all pending buttons
        for (VirtualElement ve : pendingVirtualElements)
            if (!ve.conditionCheckPositive) ve.handleInput(null, 0); // condition is not true? force button release

        pendingVirtualElements.clear(); // clear list of pending buttons

    }

    //************************************************************
    //      Method : Set SysEx Header
    //************************************************************

    public void setSysExHeader(int... pArg) {

        sysExHeader = Convert.toByteArray(pArg); // store sysex header as byte array

    }


}

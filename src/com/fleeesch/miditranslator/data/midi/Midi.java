package com.fleeesch.miditranslator.data.midi;

import com.fleeesch.miditranslator.data.midi.buffer.MidiBuffer;
import com.fleeesch.miditranslator.data.midi.listener.MidiListener;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.functions.math.Convert;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

public class Midi {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    public static Midi Daw;
    public static MidiDevice DawArrange;
    public static MidiDevice DawControl;
    public static MidiDevice DawFeedback;

    static final boolean printMidi = false;

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    // in and out midi ports
    public final List<MidiDevice> inList = new ArrayList<>();
    public final List<MidiDevice> outList = new ArrayList<>();
    public final List<MidiDevice> list = new ArrayList<>();
    // device
    public Device device;
    // indicator that ports have been successfully opened
    public boolean portsOpen = false;
    public boolean isBlocked = false;


    //************************************************************
    //      Constructor
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Empty

    public Midi() {

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Device

    public Midi(Device pDevice) {

        device = pDevice; // device linked to this instance

    }

    //************************************************************
    //      Static Method : Get Lookup Address
    //************************************************************

    public static int getLookupAddress(int m1, int m2) {

        // calculate lookup address from addressing bytes
        return (m1 - 0x7F) * 128 + m2;

    }

    //************************************************************
    //      Method : Get Input By Name
    //************************************************************

    static MidiDevice[] getInputByName(String pName) {

        List<MidiDevice> midiDevices = new ArrayList<>();

        // get device list
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();

        // go through list
        for (MidiDevice.Info info : devices) {
            try {

                // store device
                MidiDevice device = MidiSystem.getMidiDevice(info);

                // device has input and the name matches?
                if (device.getMaxReceivers() >= 0 && info.getName().equals(pName)) {

                    midiDevices.add(device);

                }

                // error handling
            } catch (MidiUnavailableException e) {

                System.out.println(e.getMessage());


            }

        }

        if (midiDevices.size() > 0)
            return midiDevices.toArray(new MidiDevice[0]);
        else
            return null;
    }

    //************************************************************
    //      Method : Incoming Message
    //************************************************************

    static MidiDevice[] getOutputByName(String pName) {

        List<MidiDevice> midiDevices = new ArrayList<>();


        // get device list
        MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();

        // go through devices
        for (MidiDevice.Info info : devices) {
            try {

                // store device
                MidiDevice device = MidiSystem.getMidiDevice(info);

                // device has output and name matches?
                if (device.getMaxTransmitters() >= 0 && info.getName().equals(pName)) {

                    midiDevices.add(device);

                }

                // error handling
            } catch (MidiUnavailableException e) {
                System.out.println(e.getMessage());

            }

        }


        if (midiDevices.size() > 0)
            return midiDevices.toArray(new MidiDevice[0]);
        else
            return null;
    }

    //************************************************************
    //      Method : Get Input By Name
    //************************************************************

    public void errorDeviceUnavailable() {

        // nothing here, mainly used for debugging

    }

    //************************************************************
    //      Event : Incoming MIDI Message
    //************************************************************

    public void incomingMidiMessage(int[] msg) {

        if (printMidi) {
            // print message for debugging purposes
            for (int j : msg) System.out.print(j + " ");
            System.out.print("\n");
        }

        // skip if device is disabled
        if (!device.enabledState) return;

        try {

            // tread pitchbend message differently
            if ((msg[0] & 0xF0) == 0xE0) {

                // address only needs the status byte, value is calculated from the 14bit message
                device.handleMidiInput(getLookupAddress(msg[0], 0), (msg[2] * 128 + msg[1]) / 16383.0, msg);

                return;

            }

            // tread 2 byte messages differently

            if (msg.length < 2) {

                // address input element by midi lookup address, convert value to 0-1 double
                device.handleMidiInput(getLookupAddress(msg[0], 0), msg[1] / 127.0, msg);

                return;
            }

            // address input element by midi lookup address, convert value to 0-1 double
            device.handleMidiInput(getLookupAddress(msg[0], msg[1]), msg[2] / 127.0, msg);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //************************************************************
    //      Method : Rebuild
    //************************************************************

    public boolean rebuild() {

        closeAllPorts(); // close the ports

        // clear all lists
        inList.clear();
        outList.clear();
        list.clear();

        // add ins and outs, quit on error
        for (String n : device.midiInList) if (addInput(n)) return false;
        for (String n : device.midiOutList) if (addOutput(n)) return false;

        // open ports, quit on error
        if (openAllPorts()) return false;

        device.onMidiRestart();

        // everything worked   ? update the output elements of the device
        device.updateOutputElements();

        return true;


    }

    //************************************************************
    //      Method : Close all Midi Ports
    //************************************************************

    public void closeAllPorts() {


        // go through device list
        for (MidiDevice d : list) closePort(d);

        // ports are closed, no matter what
        // (error means that the ports are not there, basically the same)
        portsOpen = false;

        // pass the success marker

    }

    //************************************************************
    //      Method : Open all Midi Ports
    //************************************************************

    public boolean openAllPorts() {

        // success is automatically marked as true
        boolean success = true;

        // go through devices
        for (MidiDevice d : list) {
            // try to open port, mark fail if something went wrong
            if (!openPort(d)) success = false;
        }

        // declare availability on success
        if (success) {
            portsOpen = true;
            device.available = true;

        } else {
            portsOpen = false;
            device.available = false;
        }

        // return success marker
        return !success;

    }

    //************************************************************
    //      Method : Open Midi Port
    //************************************************************

    private boolean openPort(MidiDevice port) {

        // open the port, pass error as false

        try {

            port.open();


        } catch (Exception e) {

            System.out.println(e.getMessage());

            errorDeviceUnavailable();

            return false;
        }

        return true;
    }

    //************************************************************
    //      Method : Close Midi Port
    //************************************************************

    private void closePort(MidiDevice port) {

        // open the port, pass error as false (should be always true AFAIK)

        try {

            port.close();


        } catch (Exception e) {

            System.out.println(e.getMessage());

            errorDeviceUnavailable();

        }

    }

    //************************************************************
    //      Method : Add Input
    //************************************************************

    public boolean addInput(String pName) {

        // get device by name
        MidiDevice[] device = getInputByName(pName);

        // quit of not found
        if (device == null) {
            errorDeviceUnavailable();
            return true;
        }

        try {

            // add to input list
            inList.add(device[0]);
            list.add(device[0]);

            // check if device is input, setup listener for this midi handler
            device[0].getTransmitter().setReceiver(new MidiListener(this));

        } catch (Exception e) {
            errorDeviceUnavailable();
            System.out.println(e.getMessage());
        }

        return false;

    }

    //************************************************************
    //      Method : Add Output
    //************************************************************

    public boolean addOutput(String pName) {

        // get device by name
        MidiDevice[] device = getOutputByName(pName);

        // quit if not found
        if (device == null) {
            errorDeviceUnavailable();
            return true;
        }

        try {

            // add to output list
            outList.add(device[0]);
            list.add(device[0]);

        } catch (Exception e) {
            errorDeviceUnavailable();
            System.out.println(e.getMessage());

        }

        return false;
    }


    //************************************************************
    //      Method : Send Message
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Given Port + Packed Message

    public void SendMessage(int idx, MidiMessage msg) {

        try {
            // send to output port
            outList.get(idx).getReceiver().send(msg, -1);
            // error handling
        } catch (Exception e) {
            errorDeviceUnavailable();
            System.out.println(e.getMessage());

        }
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Given Port

    public void SendMessage(int idx, int m1, int m2, int m3) {

        // create short message
        ShortMessage m = new ShortMessage();

        try {

            // fill message with data
            m.setMessage(m1, m2, m3);

            if (isBlocked) MidiBuffer.addMessage(device, idx, m);

            // send to output port
            outList.get(idx).getReceiver().send(m, -1);

            // error handling
        } catch (Exception e) {
            errorDeviceUnavailable();
            System.out.println(e.getMessage());
        }
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  First port

    public void SendMessage(int m1, int m2, int m3) {

        // use first port if none given
        SendMessage(0, m1, m2, m3);

    }


    //************************************************************
    //      Method : Send SysEx
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Int as given Data

    public void SendSysEx(int... pMsg) {

        // convert in to byte array, pass to sysex messenger
        SendSysEx(Convert.toByteArray(pMsg));
    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Byte as given Data

    public void SendSysEx(byte... pMsg) {

        SysexMessage m = new SysexMessage(); // create new sysex message

        // get lengths
        int sl = device.sysExHeader.length; // header length
        int ml = pMsg.length; // message length
        int l = sl + ml + 2; // total length (+2 for status bytes)

        // declare output message
        byte[] msgOut = new byte[l];

        // set status bytes
        msgOut[0] = (byte) 0xF0;
        msgOut[l - 1] = (byte) 0xF7;

        // copy byte arrays into message
        System.arraycopy(device.sysExHeader, 0, msgOut, 1, sl);
        System.arraycopy(pMsg, 0, msgOut, sl + 1, ml);

        try {

            m.setMessage(msgOut, l); // input data to midi message


            /*
                ~~

                Buffer keeps hanging on FaderPort 8, or when sending longer SysEx Messages in general.
                No idea why, could be OS related. Probably buffer issues.

                Right now the solution is to set a flag per device to close and open Midi Ports before
                a System exclusive messages gets sent. Any message that is sent during this process
                will be stored in a buffer and then sent after the port mangling is done.

             */

            // require sysex fix?
            if (device.sysexForceOpenClose) {

                isBlocked = true; // midi is blocked right now

                // close and open port
                device.midi.outList.get(0).close();
                device.midi.outList.get(0).open();

                isBlocked = false; // midi is clear again

                // send all the messages that were about to be sent during the port closing
                MidiBuffer.send();

            }


            // send message, no timestamp given
            outList.get(0).getReceiver().send(m, -1);


        } catch (Exception e) {

            System.out.println(e.getMessage());

        }


    }


}
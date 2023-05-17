package com.fleeesch.miditranslator.data.osc;

import com.fleeesch.miditranslator.data.external.DataSet;
import com.fleeesch.miditranslator.data.external.fx.FxDataSet;
import com.fleeesch.miditranslator.data.external.track.TrackDataSet;
import com.fleeesch.miditranslator.data.osc.listeners.*;
import com.fleeesch.miditranslator.device.Device;
import com.fleeesch.miditranslator.data.lookup.osc.OscAddress;
import com.illposed.osc.MessageSelector;
import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.messageselector.OSCPatternAddressMessageSelector;
import com.illposed.osc.transport.OSCPortIn;
import com.illposed.osc.transport.OSCPortOut;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class Osc {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    public static List<Osc> Daw;
    public static Osc DawArrange;
    public static Osc DawControl;


    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    // Input and Output Port
    public OSCPortOut portOut;
    public OSCPortIn portIn;

    // device
    public Device device;

    //************************************************************
    //      Constructor
    //************************************************************

    public Osc(Device pDevice, String pAdress, int pPortin, int pPortOut) {

        try {

            // setup ports
            portOut = new OSCPortOut(InetAddress.getByName(pAdress), pPortOut);
            portIn = new OSCPortIn(pPortin);

            // store device
            device = pDevice;

            // start listening on in port
            portIn.startListening();


            // error handling
        } catch (IOException | NumberFormatException e1) {
            e1.printStackTrace();
        }


    }

    //************************************************************
    //      Start Listening
    //************************************************************

    public static void startListening() {

        for (Osc o : Daw) o.portIn.startListening();

    }

    //************************************************************
    //      Add Listener (Virtual Element)
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Virtual Element

    public void addListener(String adr, com.fleeesch.miditranslator.element.virtual.VirtualElement ve) {

        // message selector with wildcard for all messages
        MessageSelector selector = new OSCPatternAddressMessageSelector(adr);

        // create listener
        portIn.getDispatcher().addListener(selector, new VElement(ve));

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Data Collector

    public void addListener(String adr, DataSet dc) {

        // message selector
        MessageSelector selector = new OSCPatternAddressMessageSelector(adr);

        // create listener
        portIn.getDispatcher().addListener(selector, new DataCollector(dc));

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Data Collector - Track

    public void addListener(String adr, TrackDataSet dc) {

        // message selector
        MessageSelector selector;

        // create listener

        for (int i = 0; i < dc.trackCount; i++) {
            selector = new OSCPatternAddressMessageSelector(adr + "/" + i + OscAddress.addressName);
            portIn.getDispatcher().addListener(selector, new TrackName(dc.tracks.get(i)));

            selector = new OSCPatternAddressMessageSelector(adr + "/" + i + OscAddress.addressPeak);
            portIn.getDispatcher().addListener(selector, new TrackPeak(dc.tracks.get(i)));

            selector = new OSCPatternAddressMessageSelector(adr + "/" + i + OscAddress.addressIndex);
            portIn.getDispatcher().addListener(selector, new TrackIndex(dc.tracks.get(i)));

            selector = new OSCPatternAddressMessageSelector(adr + "/" + i + OscAddress.addressColor);
            portIn.getDispatcher().addListener(selector, new TrackColor(dc.tracks.get(i)));


        }

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Data Collector - FX

    public void addListener(String adr, FxDataSet dc) {

        // message selector
        MessageSelector selector;

        // create listener

        selector = new OSCPatternAddressMessageSelector(adr + OscAddress.addressFocus + OscAddress.addressName);
        portIn.getDispatcher().addListener(selector, new FxName(dc.fxInstance));

        for (int i = 0; i < dc.fxCount; i++) {


            String adrName = adr + "/" + i + OscAddress.addressName;

            selector = new OSCPatternAddressMessageSelector(adrName);
            portIn.getDispatcher().addListener(selector, new FxName(dc.fx.get(i)));

        }

    }

    //************************************************************
    //      Send Init Messages
    //************************************************************

    public static void sendInitMessages() {

        DawArrange.sendMessage(OscAddress.initArrange, 1);
        DawControl.sendMessage(OscAddress.initControl, 1);

    }


    //************************************************************
    //      Send Message
    //************************************************************

    public void sendMessage(String pAdress, double pValue) {

        try {

            // init parameter list, add paremter
            ArrayList<Object> arg = new ArrayList<>();
            arg.add((float) pValue);

            // create message, place in bundle
            OSCMessage msg = new OSCMessage(pAdress, arg);
            OSCBundle bundle = new OSCBundle();
            bundle.addPacket(msg);

            // send message
            portOut.send(bundle);

            // error handling
        } catch (Exception e) {

            e.printStackTrace();

        }

    }


}

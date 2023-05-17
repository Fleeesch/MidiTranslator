package com.fleeesch.miditranslator.data.midi.listener;

import com.fleeesch.miditranslator.data.midi.Midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import java.util.ArrayList;

public class MidiListener implements Receiver {

    //************************************************************
    //      Variables
    //************************************************************

    static final ArrayList<MidiListener> list = new ArrayList<>();

    // links
    public final Midi linkMidi;


    //************************************************************
    //      Constructor
    //************************************************************

    public MidiListener(Midi pMidiInstance) {

        // store link to midi instance
        linkMidi = pMidiInstance;

        MidiListener.list.add(this);

    }


    //************************************************************
    //      Event Listener : Incoming MIDI Message
    //************************************************************

    public void send(MidiMessage msg, long timeStamp) {

        // get message, init int array
        byte[] mIn = msg.getMessage();
        int[] mOut = new int[mIn.length];

        // transfer message to int array
        for (int i = 0; i < mIn.length; i++) mOut[i] = mIn[i];

        // byte 1 fix
        mOut[0] &= 0xFF;

        // send message to Midi instance
        linkMidi.incomingMidiMessage(mOut);

    }

    //************************************************************
    //      Method : Close
    //************************************************************

    public void close() {
    }

}

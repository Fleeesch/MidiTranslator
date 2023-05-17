package com.fleeesch.miditranslator.element.virtual.mpe.channel;

import com.fleeesch.miditranslator.data.midi.Midi;
import com.fleeesch.miditranslator.element.virtual.mpe.MpeSurface;

import javax.swing.*;

public class MpeSurfaceChannel {

    //************************************************************
    //      Variables
    //************************************************************

    public final int channel; // channel of the instance
    // mpe values
    public double mpeZ;
    public double mpePitch;
    public double mpeY;
    // last sent midi messages
    public int midiLastChanPressure = -1;
    public int midiLastPitch = -1;
    public int midiLastTimbre = -1;
    // cc number for timbre
    public int mpeYControlChange = 74;
    // current sustain state
    public boolean sustain = false;
    final MpeSurface mpeSurface; // source surface
    final String name; // name of channel

    public boolean active;

    //************************************************************
    //      Constructor
    //************************************************************

    public MpeSurfaceChannel(String pName, MpeSurface pSurface, int pChannel) {

        name = pName; // store name

        mpeSurface = pSurface; // store surface

        channel = pChannel; // store  channel

    }

    //************************************************************
    //      Method : Process
    //************************************************************

    public void handleDataInput() {
        // placeholder
    }

    //************************************************************
    //      Method : Send Midi Pressure
    //************************************************************

    public void sendMidiPressure() {

        // don't send pressure of surface won't allow it
        if (!mpeSurface.sendPressure) return;

        int val = (int) (mpeZ * 127); // calculate value

        // value is the same as the last one? escape
        if (val == midiLastChanPressure) return;

        // send midi message
        Midi.Daw.SendMessage(0, 0xD0 + channel, val, 0);

        // store value
        midiLastChanPressure = val;

    }

    //************************************************************
    //      Method : Send Midi Pitch
    //************************************************************

    public void sendMidiPitch() {

        // calculate value
        int val = (int) Math.ceil(((mpePitch * 0.5 + 0.5) * 16383));

        // value is the same as the last one? escape
        if (val == midiLastPitch) return;

        // get value bytes for pitchbend
        int b1 = val & 127;
        int b2 = val >> 7;

        // send pitchbend
        Midi.Daw.SendMessage(0, 0xE0 + channel, b1, b2);

        // store sent value
        midiLastPitch = val;

    }

    //************************************************************
    //      Method : Send Midi Timbre
    //************************************************************

    public void sendMidiTimbre() {

        // calculate value
        int val = (int) (mpeY * 127);

        // value is the same as the last one? quit
        if (val == midiLastTimbre) return;

        // send midi message
        Midi.Daw.SendMessage(0, 0xB0 + channel, mpeSurface.modCC, val);

        // store sent value
        midiLastTimbre = val;

    }

    //************************************************************
    //      Method : Send Sustain
    //************************************************************

    public void sendSustain(boolean pState) {

        // incoming sustain is positive?
        if (pState) {

            // sustain already on?
            if (sustain) {

                // send off message
                Midi.Daw.SendMessage(0, 0xB0, 64, 0);

                // prepare delayed sustain off message
                Timer delSus = new Timer(1, e -> Midi.Daw.SendMessage(0, 0xB0, 64, 127));

                // send delayed message using java swing timer
                delSus.setRepeats(false);
                delSus.start();

            } else {
                // send on message
                Midi.Daw.SendMessage(0, 0xB0, 64, 127);
            }
        } else {

            // sustain released? just send off message
            Midi.Daw.SendMessage(0, 0xB0, 64, 0);
        }


    }

    //************************************************************
    //      Method : Init Midi Messages
    //************************************************************

    public void initMidiMessages(double chanPress) {

        // store pressure
        mpeZ = chanPress;

        // reset last sent message values
        midiLastChanPressure = -1;

        // only send pitch and timbre if mpe us activated
        if (mpeSurface.useMpe) {
            midiLastPitch = -1;
            midiLastTimbre = -1;
        }

        // surface uses mpe? send pressure instantly
        if (mpeSurface.useMpe) {
            sendMidiPressure();
            sendMidiPitch();
            sendMidiTimbre();
        }

        sendMidiPressure();

    }


    //************************************************************
    //      Method : Set Z
    //************************************************************

    public void setZ(double pVal) {
        mpeZ = pVal; // store value
        handleDataInput(); // process the data
    }

    //************************************************************
    //      Method : Set Pitch
    //************************************************************

    public void setPitch(double pVal) {
        mpePitch = pVal; // store value
        handleDataInput(); // process the data
    }

    //************************************************************
    //      Method : Set Y
    //************************************************************

    public void setY(double pVal) {
        mpeY = pVal; // store data
        handleDataInput(); // process the value
    }


    //************************************************************
    //      Method : Set Sustain
    //************************************************************

    public void setSustain(boolean pState) {

        sendSustain(pState); // process incoming sustain

        sustain = pState; // store last state

    }

}

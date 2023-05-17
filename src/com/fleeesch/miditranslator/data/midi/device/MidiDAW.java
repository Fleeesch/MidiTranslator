package com.fleeesch.miditranslator.data.midi.device;

import com.fleeesch.miditranslator.data.midi.Midi;
import com.fleeesch.miditranslator.device.Device;

public class MidiDAW extends Midi {

    /*

    separate version of the midi handler for the DAW,
    necessary for MIDI feedback processing

     */

    //************************************************************
    //      Constructor
    //************************************************************

    public MidiDAW(Device pDevice) {

        device = pDevice;

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Incoming Message
    //************************************************************

    @Override
    public void incomingMidiMessage(int[] msg) {

        // convert to double, downscale to 0-1
        double val = msg[2] / 127.0;

        try {

            // go through devices, make them process the signal as a feedback signal
            for (Device d : Device.list) d.handleSoftwareInput(getLookupAddress(msg[0], msg[1]), val);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }


}

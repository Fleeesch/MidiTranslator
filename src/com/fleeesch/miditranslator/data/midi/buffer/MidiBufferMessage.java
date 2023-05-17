package com.fleeesch.miditranslator.data.midi.buffer;

import com.fleeesch.miditranslator.device.Device;

import javax.sound.midi.MidiMessage;

public class MidiBufferMessage {

    final MidiMessage message;

    final Device device;

    final int portId;

    public MidiBufferMessage(Device pDevice, int pPortId, MidiMessage pMessage) {

        device = pDevice;

        portId = pPortId;

        message = pMessage;

    }

    public void send() {

        device.midi.SendMessage(portId, message);

    }

}

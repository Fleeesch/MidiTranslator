package com.fleeesch.miditranslator.data.midi.buffer;

import com.fleeesch.miditranslator.device.Device;

import javax.sound.midi.MidiMessage;
import java.util.ArrayList;
import java.util.List;

public class MidiBuffer {

    private static final List<MidiBufferMessage> buffer = new ArrayList<>();

    public static void addMessage(Device pDevice, int pPortId, MidiMessage pMessage) {

        buffer.add(new MidiBufferMessage(pDevice, pPortId, pMessage));

    }

    public static void send() {

        for (MidiBufferMessage m : buffer) m.send();

        clear();

    }

    static void clear() {

        buffer.clear();

    }


}

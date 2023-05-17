package com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.generator;

import com.fleeesch.miditranslator.Main;
import com.fleeesch.miditranslator.element.virtual.mpe.channel.MpeSurfaceChannel;

public class MpeEvent {

    //************************************************************
    //      Variables
    //************************************************************

    // channel of event
    public final int channel;
    // note of event
    public final int note;
    // velocity
    public final double velocity;
    // pressure
    public double mpeZ;
    // generator that created the event
    final MpeEventGenerator creator;

    // mpe channel
    public final MpeSurfaceChannel mpeChannel;

    //************************************************************
    //      Constructor
    //************************************************************

    public MpeEvent(MpeEventGenerator pCreator) {

        creator = pCreator; // store creator

        channel = creator.mpeSurface.getNextChannel(); // grab the next available channel

        mpeChannel = creator.mpeSurface.channels.get(channel);

        mpeChannel.active = true;

        note = creator.note; // get note from creator

        mpeZ = creator.pressure;

        velocity = creator.velocity;

        creator.mpeSurface.channels.get(channel).initMidiMessages(mpeZ); // send initialization message with pressure value to channel instance

        Main.deviceDaw.midi.SendMessage(144 + channel, note, (int) (velocity * 127)); // send midi message

    }

    //************************************************************
    //      Method : Destroy
    //************************************************************

    public void destroy() {

        Main.deviceDaw.midi.SendMessage(128 + channel, note, (int) (velocity * 127)); // send release message (using original velocity for note-off velocity)

        creator.mpeSurface.mpeEvents.remove(this); // remove this event from source list

        creator.event = null;

        mpeChannel.active =false;

    }


}

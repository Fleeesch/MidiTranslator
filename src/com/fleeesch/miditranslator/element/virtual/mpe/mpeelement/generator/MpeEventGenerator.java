package com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.generator;

import com.fleeesch.miditranslator.element.virtual.mpe.MpeDataHandler;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.MpeElement;

public class MpeEventGenerator extends MpeElement {

    //************************************************************
    //      Variables
    //************************************************************

    // event created by the generator (only one allowed)
    public MpeEvent event;

    // note for new events
    public int note;
    public boolean noteOutOfRange = false;

    public final int posX;
    public final int posY;

    public double pressure;
    public double velocity;

    //************************************************************
    //      Constructor
    //************************************************************

    public MpeEventGenerator(String pName, MpeDataHandler mpeDataHandler) {

        super(pName, mpeDataHandler); // use original constructor

        // make sure pad is set up to send instant pressure data on strike
        if (mpeDataHandler.padPressureSensitive != null) {
            mpeDataHandler.padPressureSensitive.instantPressure = true;
        }

        posX = mpeDataHandler.posX;
        posY = mpeDataHandler.posY;

    }

    //************************************************************
    //      Event : Handle Trigger
    //************************************************************

    @Override
    public void handleTrigger(double pVal) {

        // don't do anything of note is illegal
        if (noteOutOfRange) return;

        if (pVal > 0) {
            // create event on press
            if (mpeSurface.velocityLock) velocity = 1;
            else velocity = pVal;

            pressure = pVal;

            event = mpeSurface.createEvent(this);


        } else {
            // remove event on release if it exists (can be potentially gone through surface rebuild)
            if (event != null) mpeSurface.destroyEvent(event);

        }


    }

    //************************************************************
    //      Event : Handle Mpe Z
    //************************************************************

    @Override
    public void handleMpeZ(double pVal) {

        pressure = pVal;

        if (noteOutOfRange) return;

        // use event values if one exists

        if (event != null) {
            event.mpeZ = pVal;
            mpeSurface.handleZInput(event); // let the surface handle the pressure change of the current event
        } else {
            mpeSurface.handleZInput(pVal);
        }


    }

    //************************************************************
    //      Event : Handle Pressure
    //************************************************************

    @Override
    public void handlePressure(double pVal) {

    }


    //************************************************************
    //      Method : Update Note
    //************************************************************

    public void updateNote() {

        note = mpeSurface.noteLookup[posX][posY]; // lookup note using the creator coordinates

        // mark note as out of range if it is the case
        noteOutOfRange = note < 0 || note > 127;

        // change color depending on note position
        if (noteOutOfRange) {
            // not allowed note
            mpeDataHandler.setColor(0, 0, 0);
        } else {
            if (note == mpeSurface.baseNote) {
                // base note
                mpeDataHandler.setColor(1, 0.8, 0.6);
            } else if (note % 12 == 0) {
                // 1st in octave (all Cs)
                double litA = Math.max((note - 64) / 64.0, 0);
                double litB = Math.max((32 - note) / 127.0, 0);
                mpeDataHandler.setColor(1, litA, litB);
            } else if (note % 4 == 0) {
                // every 4th semitones
                mpeDataHandler.setColor(0.1, 0.1, 0.1);
            } else {
                // in between notes
                mpeDataHandler.setColor(0.02, 0.02, 0.02);

            }
        }


    }


    //************************************************************
    //      Event : On Clear
    //************************************************************

    public void clear() {

        event = null;
    }
}
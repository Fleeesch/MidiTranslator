package com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.generator;

import com.fleeesch.miditranslator.element.virtual.mpe.MpeDataHandler;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.MpeElement;
import com.fleeesch.miditranslator.functions.math.Calculate;

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

    public double pressure = 0;
    public double velocity;

    private double lastPressure = 0;

    private boolean activeRelease;
    private double releaseValue = 0;
    private long releaseMillis = 0;

    private final int offVelocityLow = 300;
    private final int offVelocityHigh = 50;


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
            if (mpeSurface.velocityLock) {
                velocity = 1;
            } else {
                velocity = pVal;
            }

            pressure = pVal;

            storeReleasePoint();

            event = mpeSurface.createEvent(this);


        } else {

            // calculate release velocity
            double releaseVelocity = Calculate.rescaleValueLimit(System.currentTimeMillis() - releaseMillis, offVelocityLow, offVelocityHigh, 0, releaseValue);

            // remove event on release if it exists (can be potentially gone through surface rebuild)
            if (event != null) mpeSurface.destroyEvent(event, releaseVelocity);

        }


    }

    //************************************************************
    //      Event : Store Release Point
    //************************************************************

    private void storeReleasePoint() {

        // store pressure value
        releaseValue = pressure;

        // sture current system time
        releaseMillis = System.currentTimeMillis();

    }

    //************************************************************
    //      Event : Handle Mpe Z
    //************************************************************

    @Override
    public void handleMpeZ(double pVal) {

        // store last pressure value
        lastPressure = pressure;

        // store current pressure value
        pressure = pVal;

        // pressure goes up = no release motion
        if (activeRelease && lastPressure < pressure) {

            activeRelease = false;

        }

        // release motion initial trigger
        if (!activeRelease && lastPressure > pressure) {

            activeRelease = true;

            // store current release data
            storeReleasePoint();

        }


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
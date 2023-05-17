package com.fleeesch.miditranslator.element.virtual.interpreter;

import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;

public class InterpreterChord extends VirtualElement {

    // trigger actions for single buttons (not chord) only on release
    public boolean triggerOnRelease;

    // chord has been activaed flag -> no actions until all buttons are released
    public boolean chordActivated = false;

    //************************************************************
    //      Constructor
    //************************************************************

    public InterpreterChord(String pName, boolean pTriggerOnRelease) {

        super(pName); // original constructor

        triggerOnRelease = pTriggerOnRelease; // triggers single buttons only on release?

    }

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        // original method -> store value
        super.handleInput(pSource, pVal);

        if (pSource == null) return; // requires source identifier for getting the action ids

        // is pressed?
        if (pVal > 0) {

            if (chordActivated) return; // don't do anything if a chord has been previously activated

            // count number of pushed buttons
            int i = 0;
            for (InputElement ie : sourceElements) if (ie.parameter.get() > 0) i++;

            // all buttons pressed?
            if (i == sourceElements.size()) {

                // trigger actions for chrod
                if (dualAction) actions.get(sourceElements.size()).trigger(1);
                else actions.get(0).trigger(1);

                // chord has been activated!
                chordActivated = true;

                return; // skip the rest
            }

            // send action of single button if no chord is activated and the trigger is done via press
            if (!triggerOnRelease) actions.get(sourceElements.indexOf(pSource)).trigger(1);

        } else {

            // is released?

            // chord has been activated?
            if (chordActivated) {

                // don't do anything if there's still a button held down
                for (InputElement ie : sourceElements) if (ie.getParameterValue() > 0) return;

                // chord is released, skip the rest
                chordActivated = false;
                return;
            }

            // trigger action for single button on release if set so
            if (triggerOnRelease) actions.get(sourceElements.indexOf(pSource)).trigger(1);

        }


    }

}

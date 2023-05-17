package com.fleeesch.miditranslator.element.virtual;

import com.fleeesch.miditranslator.action.Action;
import com.fleeesch.miditranslator.action.ActionGroup;
import com.fleeesch.miditranslator.data.osc.Osc;
import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.condition.Condition;
import com.fleeesch.miditranslator.element.Element;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.input.fader.FaderMotorized;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.output.led.Led;
import com.fleeesch.miditranslator.element.output.led.rgb.LedRGB;

import java.util.ArrayList;
import java.util.List;


public abstract class VirtualElement extends Element {

    //************************************************************
    //      Variables
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    public static VirtualElement last;
    public static List<VirtualElement> list = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * *  * * *
    //  Public

    // input, output elements
    public final List<InputElement> sourceElements = new ArrayList<>();
    public final List<OutputElement> targetElements = new ArrayList<>();

    // type specific elements
    public final List<FaderMotorized> sourceMotorFader = new ArrayList<>();
    public final List<Led> leds = new ArrayList<>();

    // boolean indicators
    protected boolean hasMotorFader, hasRGBControl, parameterControlsLed, isLatch;

    // led inversion
    public boolean invertLed;

    // actions
    public final List<ActionGroup> actions = new ArrayList<>();
    public boolean dualAction = false; // has more than one action

    // feedback addresses
    public int midiFeedbackAddress;
    public String oscFeedbackAddress;

    // condition
    public final Condition condition;
    private final boolean conditionCheckInverted;
    public boolean conditionCheckPositive = true;


    //************************************************************
    //      Constructor
    //************************************************************

    public VirtualElement(String pName) {

        super();

        name = pName;

        device.virtualElements.add(this);
        VirtualElement.last = this;

        condition = Condition.get();
        conditionCheckInverted = Condition.inverted;

        if (condition != null) condition.addEventHandler(this);

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Elements
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Add Source Element
    //************************************************************

    public void addSource(InputElement e) {

        // keep track of motor faders related to this element
        if (e.getClass() == FaderMotorized.class) {
            sourceMotorFader.add((FaderMotorized) e);
            hasMotorFader = true;
        }

        // create link
        sourceElements.add(e);
        e.targetElements.add(this);

    }

    //************************************************************
    //      Method : Add Target Element
    //************************************************************

    public void addTarget(OutputElement e) {

        // keep track of leds
        if (e instanceof Led) {

            setParameterValue(1); // turn on led by default
            leds.add((Led) e);

        }

        if (e instanceof LedRGB) {

            parameters.add(new Parameter("R"));
            parameters.add(new Parameter("G"));
            parameters.add(new Parameter("B"));
            parameters.add(new Parameter("A"));

            parameters.get(parameters.size() - 1).set(1);

            hasRGBControl = true;

        }


        // create link
        targetElements.add(e);
        e.sourceElements.add(this);


        e.enableVisibility();


    }


    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Action
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    //************************************************************
    //      Method : Add Action
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Argument : Action

    public void addAction(Action a) {

        actions.add(new ActionGroup(a)); // create a group

        if (actions.size() > 1) dualAction = true; // at least 2 actions available

        a.virtualElement = this; // link action to this element

    }

    //* * * * * * * * * * * * * * * * * * * * * * * *
    //  Argument : ActionGroup

    public void addAction(ActionGroup a) {

        actions.add(a); // create a group

        // link actions of group to this element
        for (Action ac : a.actions)
            ac.virtualElement = this;

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Feedback
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Link To Feedback
    //************************************************************

    private void linkToFeedback() {

        setParameterValue(0); // prevent LEDs from being lit at startup

        parameterControlsLed = true; // feedback indicator for output elements

    }

    //************************************************************
    //      Method : Link To Feedback (MIDI)
    //************************************************************

    public void linkToFeedback(int pAdr) {

        midiFeedbackAddress = pAdr; // store feedback address

        device.midiVirtualLookup[pAdr] = this; // create lookup for feedback address

        linkToFeedback(); // finish setup

    }


    //************************************************************
    //      Method : Link To Feedback (OSC)
    //************************************************************

    public void linkToFeedback(Osc pOsc, String pAdr) {

        oscFeedbackAddress = pAdr; // store osc feedback address

        pOsc.addListener(pAdr, this); // create osc message listener

        linkToFeedback(); // finish setup

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Input Handling
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Handle Input
    //************************************************************

    public void handleInput(InputElement pSource, double pVal, int... pMidiMsg) {

        // store input value only if there's no feedback (too avoid led override) EXCEPT for motor fades, those require the value
        if (!parameterControlsLed || hasMotorFader) setParameterValue(pVal); // store value

    }

    //************************************************************
    //      Method : Handle Input Raw Pressure
    //************************************************************

    public void handleInputRawPressure(double pVal) {

    }

    //************************************************************
    //      Method : Handle Input Fader Touch
    //************************************************************

    public void handleInputFaderTouch(boolean pState) {

        // use 2nd action if available, send data on press and release

        if (dualAction) {
            if (pState) actions.get(1).trigger(1);
            else actions.get(1).trigger(0);
        } else {
            if (pState) actions.get(0).trigger(1);
            else actions.get(0).trigger(0);
        }

    }

    //************************************************************
    //      Method : Handle Software Input
    //************************************************************

    public void handleSoftwareInput(double pVal) {

        setParameterValue(pVal); // store value

    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Methods : Output elements
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Method : Set Linked Led State
    //************************************************************

    public void setTargetLedVisibility(int pState) {

        // disable, enable, toggle

        switch (pState) {
            case 0:
                for (Led l : leds) l.disableVisibility();
                break;
            case 1:
                for (Led l : leds) l.enableVisibility();
                break;
            case 2:
                for (Led l : leds) l.toggleVisibility();
                break;
        }


    }


    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Queues
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public boolean hasMotorFader() {
        return hasMotorFader;
    }

    public boolean hasRGBControl() {
        return hasRGBControl;
    }

    public boolean parameterControlsLed() {
        return parameterControlsLed;
    }

    public boolean isLatch() {
        return isLatch;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //                      Events
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    //************************************************************
    //      Event : On Condition Change
    //************************************************************

    public void onConditionChange(Condition pCondition, boolean pState) {

        conditionCheckPositive = pState; // store condition state

        if (conditionCheckInverted) conditionCheckPositive = !conditionCheckPositive;

        if (!conditionCheckPositive || !usedInCondition) return; // only do something on check pass

        for (FaderMotorized mt : sourceMotorFader)
            mt.setPosition(parameter.get()); // restore fader position via parameter value

    }


}

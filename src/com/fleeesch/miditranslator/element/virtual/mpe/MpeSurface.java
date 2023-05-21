package com.fleeesch.miditranslator.element.virtual.mpe;

import com.fleeesch.miditranslator.data.parameter.Parameter;
import com.fleeesch.miditranslator.data.parameter.settings.Settings;
import com.fleeesch.miditranslator.element.input.InputElement;
import com.fleeesch.miditranslator.element.output.OutputElement;
import com.fleeesch.miditranslator.element.virtual.VirtualElement;
import com.fleeesch.miditranslator.element.virtual.mpe.channel.MpeSurfaceChannel;
import com.fleeesch.miditranslator.element.virtual.mpe.channel.MpeSurfaceChannelGlobal;
import com.fleeesch.miditranslator.element.virtual.mpe.channel.MpeSurfaceChannelSingle;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.MpeElement;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.display.MpeSettings;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.display.MpeSettingsRGB;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.generator.MpeEvent;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.generator.MpeEventGenerator;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.modifier.MpePitchModifier;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.modifier.MpeSustainMod;
import com.fleeesch.miditranslator.element.virtual.mpe.mpeelement.modifier.MpeYModifier;

import java.util.ArrayList;
import java.util.List;

public class MpeSurface extends VirtualElement {

    //************************************************************
    //      Variable
    //************************************************************

    //* * * * * * * * * * * * * * * * * * * * * * * * *
    //  Static

    // surface list
    public static List<MpeSurface> list = new ArrayList<>();

    //* * * * * * * * * * * * * * * * * * * * * * * * *
    //  Public

    // mpe data handler
    public final List<MpeDataHandler> mpeDataHandlers = new ArrayList<>();

    // mpe Elements
    public final List<MpeElement> mpeElements = new ArrayList<>();

    // generators
    public final List<MpeEventGenerator> generators = new ArrayList<>();

    // events
    public final List<MpeEvent> mpeEvents = new ArrayList<>();

    // modifiers
    public final List<MpePitchModifier> pitchModifiers = new ArrayList<>();
    public final List<MpeYModifier> yModifiers = new ArrayList<>();
    public final List<MpeSustainMod> sustainModifiers = new ArrayList<>();

    // data display
    public final List<MpeSettings> mpeSettings = new ArrayList<>();

    // channels ( 2 - 16 )
    public final List<MpeSurfaceChannel> channels = new ArrayList<>();

    // global channel ( 1 )
    final MpeSurfaceChannel globalChannel;

    // size of surface
    public int sizeX;
    public int sizeY;

    // angle of surface
    public int angle = 0;

    // initial base note
    public final int baseNote = 48;

    // transpose per row
    public int rowTranspose = 6;

    // pitch transpose
    public int transpose = 0;

    // global pitch modulation value
    public double globalPitch = 0;

    // note lookup using x/y coordinates
    public int[][] noteLookup;

    // mpe channels
    public int channelFirst = 2;
    public int channelLast = 6;
    public int channelFirstStored = channelFirst;
    public int channelLastStored = channelLast;

    // index of first channel
    public int channelIndex = channelFirst;

    // MPE functionality setting
    public boolean useMpe = true;
    public boolean sendPressure = true;
    public double bendRange = 1;
    public int modCC = 74;
    public boolean velocityLock = false;


    //************************************************************
    //      Constructor
    //************************************************************

    public MpeSurface(String pName, int pX, int pY) {

        super(pName); // use original constructor

        // store size
        sizeX = pX;
        sizeY = pY;

        // create note lookup
        createTonalSurface();

        // create global channel
        globalChannel = new MpeSurfaceChannelGlobal("MPE Matrix Global Channel", this, 0);

        // add global channel to channel list
        channels.add(globalChannel);

        // crate remaining individual channels
        for (int i = 1; i < 16; i++) channels.add(new MpeSurfaceChannelSingle("MPE Matrix Channel " + i, this, i));

        list.add(this);

    }

    //************************************************************
    //      Method : Set Angle
    //************************************************************

    public void setSurfaceAngle(int pAngle){

        angle = pAngle;
        createTonalSurface();

    }

    //************************************************************
    //      Method : Set First MPE Channel
    //************************************************************

    public void setFirstMpeChannel(int pChannel) {

        channelFirst = channelFirstStored = Math.min(Math.max(pChannel, 1), 15);

        channelLast = Math.min(Math.max(channelLastStored, channelFirstStored + 1), 16);

        channelIndex = channelFirst; // reset channel index to prevent hangups

    }

    //************************************************************
    //      Method : Set Last MPE Channel
    //************************************************************

    public void setLastMpeChannel(int pChannel) {

        channelLastStored = pChannel;

        channelLast = Math.min(Math.max(channelLastStored, channelFirst), 16);

        channelIndex = channelFirst; // reset channel index to prevent hangups

    }

    //************************************************************
    //      Method : Set Per Row Transpose
    //************************************************************

    public void SetPerRowTranspose(double pVal) {

        rowTranspose = (int) pVal; // store per row transpose
        createTonalSurface(); // rearrange notes

    }

    //************************************************************
    //      Method : Set MPE Functionality
    //************************************************************

    public void setMpeFunctionality(boolean pState) {

        useMpe = pState; // adjust MPE usage setting

    }


    //************************************************************
    //      Method : Create Note Lookup
    //************************************************************

    public void createTonalSurface() {

        noteLookup = new int[sizeX][sizeY]; // declare 2D-array

        for (int y = 0; y < sizeX; y++) {
            for (int x = 0; x < sizeX; x++) {

                // store note depending on coordinate and transpose settings

                switch (angle) {
                    default -> noteLookup[x][y] = (7 - y) * rowTranspose + x + baseNote + transpose;
                    case 1 -> noteLookup[x][y] = x * rowTranspose + y + baseNote + transpose;
                    case 2 -> noteLookup[x][y] = y * rowTranspose + (7 - x) + baseNote + transpose;
                    case 3 -> noteLookup[x][y] = (7-x) * rowTranspose + (7 - y) + baseNote + transpose;
                }

            }
        }

        // update notes of generators, update colors
        for (MpeEventGenerator e : generators) e.updateNote();


    }

    //************************************************************
    //      Method : Transpose
    //************************************************************

    public void transposeNotes(int pTranspose) {

        transpose += pTranspose; // incremental transpose adjustment

        createTonalSurface(); // create note lookup

    }


    //************************************************************
    //      Method : Set Transpose
    //************************************************************

    public void setTranspose(int pVal) {

        transpose = pVal; // set transpose to specific value

        createTonalSurface(); // create note lookup

    }

    //************************************************************
    //      Method : Set Velocity Lock
    //************************************************************

    public void setVelocityLock(boolean pState) {

        velocityLock = pState;

    }

    //************************************************************
    //      Method : Clear Elemnent Lists
    //************************************************************

    public void clearElementLists() {

        mpeElements.clear();
        pitchModifiers.clear();
        yModifiers.clear();
        sustainModifiers.clear();
        generators.clear();

    }

    //************************************************************
    //      Method : Add Data Handler
    //************************************************************

    public MpeDataHandler addDataHandler(String pName, MpeSurface pSurface, int pX, int pY, InputElement pPad, OutputElement pLed) {

        MpeDataHandler e = new MpeDataHandler(pName, pSurface, pX, pY, pPad, pLed);

        mpeDataHandlers.add(e);

        return e;

    }

    //************************************************************
    //      Method : Setup Data Handlers
    //************************************************************

    public void setupDataHandlers(List<InputElement> pInput, List<OutputElement> pOutput) {

        mpeDataHandlers.clear();

        int idx = 0;

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {

                InputElement pad = pInput.get(idx);
                OutputElement led = pOutput.get(idx);

                addDataHandler("MPE Data Handler " + x + ":" + y, this, x, y, pad, led);

                idx++;

            }
        }

    }

    //************************************************************
    //      Method : Add Settings RGB
    //************************************************************

    public void addSettingsRGB(MpeDataHandler pDataHandler) {

        MpeSettingsRGB e = new MpeSettingsRGB("Settings RGB", pDataHandler);

        mpeSettings.add(e);
        mpeElements.add(e);

    }

    //************************************************************
    //      Method : Add Pitch Modifier
    //************************************************************

    public void addPitchModifier(MpeDataHandler pDataHandler, int pPitch) {

        MpePitchModifier e = new MpePitchModifier("Pitch Modifier " + pPitch, pDataHandler, pPitch);

        pitchModifiers.add(e);
        mpeElements.add(e);

    }

    //************************************************************
    //      Method : Add Y Modifier
    //************************************************************

    public void addYModifier(MpeDataHandler pDataHandler) {

        MpeYModifier e = new MpeYModifier("Y Modifier ", pDataHandler);

        yModifiers.add(e);
        mpeElements.add(e);

    }

    //************************************************************
    //      Method : Add Sustain Modifier
    //************************************************************

    public void addSustainModifier(MpeDataHandler pDataHandler) {

        MpeSustainMod e = new MpeSustainMod("Sustain Modifier", pDataHandler);

        sustainModifiers.add(e);
        mpeElements.add(e);


    }

    //************************************************************
    //      Method : Add Generator
    //************************************************************

    public void addGenerator(MpeDataHandler pDataHandler) {

        // create generator
        MpeEventGenerator e = new MpeEventGenerator("Generator", pDataHandler);

        generators.add(e); // add generator to list
        mpeElements.add(e);

        createTonalSurface(); // create note lookup

    }

    //************************************************************
    //      Method : Create Event
    //************************************************************

    public MpeEvent createEvent(MpeEventGenerator pCreator) {

        MpeEvent event = new MpeEvent(pCreator); // create new MPE event

        mpeEvents.add(event); // add event to list

        return event; // return event


    }


    //************************************************************
    //      Method : Destroy Event
    //************************************************************

    public void destroyEvent(MpeEvent pEvent) {

        pEvent.destroy(); // destroy event using its internal self-destruction method

    }

    //************************************************************
    //      Method : Get Next Channel
    //************************************************************

    public int getNextChannel() {

        if (!useMpe) return 0; // without MPE functionality only Channel 1 is used

        channelIndex++; // increment channel index

        if (channelIndex >= channelLast) channelIndex = channelFirst; // channel index is maxed ? return to channel 2

        return channelIndex; // return new channel

    }

    //************************************************************
    //      Method : Handle Z
    //************************************************************


    //* * * * * * * * * * * * * * * * * * * * * * * * *
    //  via Event
    public void handleZInput(MpeEvent pEvent) {

        channels.get(pEvent.channel).setZ(pEvent.mpeZ); // get channel from event, pass to MPE channel handler

    }

    //* * * * * * * * * * * * * * * * * * * * * * * * *
    //  via Value

    public void handleZInput(double pVal) {

        globalChannel.setZ(pVal);

    }

    //************************************************************
    //      Method : Get Average Z
    //************************************************************

    public double getAverageZ() {

        double sum = 0; // assume lowest z is 0;

        for (MpeSurface s : list) {
            // get the maximum Z value from all events
            for (MpeEvent e : s.mpeEvents) sum = Math.max(sum, e.mpeZ);

        }

        // return pressure
        return sum;

    }

    //************************************************************
    //      Method : Get Average Pitch
    //************************************************************

    public double getAveragePitch() {

        double sumTotal = 0; // assume pitch is centered

        for (MpeSurface s : list) {

            double sum = 0;

            // calculate pitches against each other
            for (MpePitchModifier p : s.pitchModifiers) sum += p.pitchValue;

            sumTotal += sum * bendRange;

        }

        // return pitch
        return globalPitch = sumTotal;

    }

    //************************************************************
    //      Method : Get Average Y
    //************************************************************

    public double getAverageY() {

        // assume mod is 0
        double sum = 0;

        // use largest mod value
        for (MpeYModifier y : yModifiers) sum = Math.max(sum, y.modValue);

        // return timbre
        return sum;

    }

    //************************************************************
    //      Method : Handle Pitch
    //************************************************************

    public void handlePitch(double pVal) {

        double pitch = getAveragePitch(); // get average pitch

        globalChannel.setPitch(pitch); // pass pitch to global channel

    }

    //************************************************************
    //      Method : Handle Y
    //************************************************************

    public void handleY(double pVal) {

        double modVal = getAverageY(); // get average mod

        globalChannel.setY(modVal); // pass to global channel, also pass the CC number

    }

    //************************************************************
    //      Method : Handle Sustain
    //************************************************************

    public void handleSustain(MpeSustainMod pModifier, boolean pState) {

        globalChannel.setSustain(pState); // pass sustain to global channel

        for (MpeSurface s : list) {

            // transfer state to other sustain modifiers
            for (MpeSustainMod m : s.sustainModifiers) {

                if (m == pModifier) continue; // ignore the source modifier

                // transfer attributes
                m.active = pModifier.active;
                m.latch = pModifier.latch;

                // update LEDs
                m.updateLed();
            }

        }

    }

    //************************************************************
    //      Method : Store all Settings
    //************************************************************

    public void updateAllSettings() {

        // store parameters
        Parameter mpeActivated = settings.getParameterByName("MPE Activated");
        Parameter mpeRowOffset = settings.getParameterByName("MPE Row Offset");
        Parameter mpeBendRange = settings.getParameterByName("MPE Bend Range");
        Parameter mpeSendPressure = settings.getParameterByName("MPE Send Pressure");
        Parameter mpeModCC = settings.getParameterByName("MPE Mod CC");
        Parameter mpeTranspose = settings.getParameterByName("MPE Transpose");
        Parameter mpeVelocityLock = settings.getParameterByName("MPE Velocity Lock");

        Parameter mpeChannelFirst = settings.getParameterByName("MPE Channel First");
        Parameter mpeChannelLast = settings.getParameterByName("MPE Channel Last");

        Parameter mpeSurfaceAngle = settings.getParameterByName("MPE Surface Angle");


        // apply settings if parameter exists
        if (mpeActivated != null) setMpeFunctionality(mpeActivated.get() > 0);

        if (mpeRowOffset != null) {
            rowTranspose = (int) mpeRowOffset.get();
            createTonalSurface();
        }

        if (mpeBendRange != null) bendRange = mpeBendRange.get();

        if (mpeSendPressure != null) sendPressure = mpeSendPressure.get() > 0;

        if (mpeModCC != null) modCC = (int) mpeModCC.get();

        if (mpeTranspose != null) {
            transpose = (int) mpeTranspose.get();
            setTranspose(transpose);
        }

        if (mpeVelocityLock != null) {
            setVelocityLock(mpeVelocityLock.get() > 0);
        }

        if (mpeChannelFirst != null) {
            setFirstMpeChannel((int) mpeChannelFirst.get());
        }

        if (mpeChannelLast != null) {
            setLastMpeChannel((int) mpeChannelLast.get());
        }

        if (mpeSurfaceAngle != null) {
            setSurfaceAngle((int) mpeSurfaceAngle.get());
        }

        updateSettingsDisplay();

    }

    //************************************************************
    //      Method : Update Settings Display
    //************************************************************

    public void updateSettingsDisplay() {
        for (MpeSettings e : mpeSettings) e.update();
    }

    //************************************************************
    //      Method : On Settings Change
    //************************************************************

    @Override
    public void onSettingsChange(Settings pSettings, Parameter pParameter) {


        // apply settings if parameter exists
        if (pParameter.name.equals("MPE Activated")) {
            setMpeFunctionality(pParameter.get() > 0);
            updateSettingsDisplay();
            return;
        }

        if (pParameter.name.equals("MPE Row Offset")) {
            rowTranspose = (int) pParameter.get();
            createTonalSurface();
            return;
        }

        if (pParameter.name.equals("MPE Bend Range")) {
            bendRange = pParameter.get();
            return;
        }

        if (pParameter.name.equals("MPE Send Pressure")) {
            sendPressure = pParameter.get() > 0;
            updateSettingsDisplay();
            return;
        }

        if (pParameter.name.equals("MPE Mod CC")) {
            modCC = (int) pParameter.get();
            return;
        }

        if (pParameter.name.equals("MPE Transpose")) {
            setTranspose((int) pParameter.get());
            return;        }


        if (pParameter.name.equals("MPE Velocity Lock")) {
            setVelocityLock(pParameter.get() > 0);
            updateSettingsDisplay();
            return;
        }

        if (pParameter.name.equals("MPE Channel First")) {
            setFirstMpeChannel((int) pParameter.get());
            return;
        }

        if (pParameter.name.equals("MPE Channel Last")) {
            setLastMpeChannel((int) pParameter.get());
            return;
        }

        if (pParameter.name.equals("MPE Surface Angle")) {
            setSurfaceAngle((int) pParameter.get());
        }

    }
}

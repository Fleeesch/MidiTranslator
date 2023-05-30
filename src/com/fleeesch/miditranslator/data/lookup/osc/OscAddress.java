package com.fleeesch.miditranslator.data.lookup.osc;

public class OscAddress {

    // --- File ---

    public static final String save = "/daw/save";

    // --- Init Messages ---

    public static final String initArrange = "/init";
    public static final String initControl = "/daw/init";

    // --- DJ ---

    public static final String djAddress = "/dj";

    public static final String[] djDeck = {djAddress + "/0", djAddress + "/1", djAddress + "/2", djAddress + "/3"};

    public static final String djFlipDecks = djAddress + "/flip";
    public static final String djSoftwareUseMidi = djAddress + "/sw_midi";

    public static final String djJog = "/jog";
    public static final String djJogSide = "/jog/s";
    public static final String djJogTouch = "/jog/t";

    public static final String djJogTouchCue = "/jog/t_cue";


    public static final String djFader = "/fdr";

    public static final String djPlay = "/play";

    public static final String djTempo = "/bpm";

    public static final String djCrossFader = djAddress + "/cf";

    public static final String[] djDeckJog = {djDeck[0] + djJog, djDeck[1] + djJog, djDeck[2] + djJog, djDeck[3] + djJog};
    public static final String[] djDeckJogSide = {djDeck[0] + djJogSide, djDeck[1] + djJogSide, djDeck[2] + djJogSide, djDeck[3] + djJogSide};
    public static final String[] djDeckJogTouch = {djDeck[0] + djJogTouch, djDeck[1] + djJogTouch, djDeck[2] + djJogTouch, djDeck[3] + djJogTouch};

    public static final String[] djDeckJogTouchCue = {djDeck[0] + djJogTouchCue, djDeck[1] + djJogTouchCue, djDeck[2] + djJogTouchCue, djDeck[3] + djJogTouchCue};

    public static final String[] djDeckTempo = {djDeck[0] + djTempo, djDeck[1] + djTempo, djDeck[2] + djTempo, djDeck[3] + djTempo};

    public static final String[] djDeckFader = {djDeck[0] + djFader, djDeck[1] + djFader, djDeck[2] + djFader, djDeck[3] + djFader};

    public static final String[] djDeckPlay = {djDeck[0] + djPlay, djDeck[1] + djPlay, djDeck[2] + djPlay, djDeck[3] + djPlay};


    // --- Control Surface Mixing ---

    public static final String faderMode = "/fdr_m";

    //public static final String dataTrack = "/trk/";

    public static final String addressName = "/n";
    public static final String addressPeak = "/p";
    public static final String addressIndex = "/i";
    public static final String addressColor = "/clr";
    public static final String addressFocus = "/fc";

    public static final String freeAddress = "/fr";
    public static final String presetAddress = "/ps";
    public static final String fxAddress = "/fx";
    public static final String trackAddress = "/trk";
    public static final String[] faderMix = {"/vol", "/pan", "/wid", "/s1", "/s2", "/s3", "/s4", "/s5"};

    public static final String trackMute = trackAddress + "/m";
    public static final String trackSolo = trackAddress + "/s";

    public static final String trackArm = trackAddress + "/a";

    public static final String trackMuteClear = trackMute + "_clr";
    public static final String trackSoloClear = trackSolo + "_clr";
    public static final String trackArmClear = trackArm + "_clr";

    public static final String faderFree = "/fdr" + freeAddress;
    public static final String faderPreset = "/fdr" + presetAddress;
    public static final String faderFx = "/fdr" + fxAddress;
    public static final String faderFxLast = "/fdr" + fxAddress + "/last";

    public static final String toggleSoloInFront = "/daw/solo_f/t";

    public static final String refreshTrackData = trackAddress + "/u";
    public static final String refreshRecordScope = "/daw/rsc/u";

    public static final String fxToggleBypass = fxAddress + "/byp/t";


    // --- Request Messages ---

    public static final String requestIsFP16 = "/rq/fp16";

    // --- Answer Messages ---

    public static final String infoIsFP16 = "/rq/fp16/i";

    // --- Control Surface Behaviour ---

    public static final String indexTracksBySelection = trackAddress + "/idx_sel";

    public static final int faderModeMix = 0;
    public static final int faderModeTrack = 1;
    public static final int faderModePreset = 2;
    public static final int faderModeFX = 3;
    public static final int faderModeFree = 4;
    public static final int faderModeMidi = 5;

    public static final String presetSync = "/daw/ps/+";


    // --- Transport ---

    public static final String play = "/daw/play";
    public static final String record = "/daw/rec";
    public static final String undo = "/daw/undo";
    public static final String redo = "/daw/redo";

    // --- Playback ---

    public static final String toggleRepeat = "/daw/loop/t";
    public static final String tapTempo = "/daw/tap";


    // --- Recording ---


    public static final String recordScopeFree = "/daw/rsc/free";
    public static final String recordScopeTime = "/daw/rsc/time";
    public static final String recordScopeItem = "/daw/rsc/item";


    // --- Jog ---

    public static final String jogGrid = "/daw/jog/grd";

    public static final String jogLeft = "/daw/jog/grd/-";
    public static final String jogRight = "/daw/jog/grd/+";

    public static final String gridDouble = "/daw/grid/+";
    public static final String gridHalf = "/daw/grid/-";
    public static final String gridRelative = "/daw/grid/r";

    // --- View ---

    public static final String zoomHorizontal = "/daw/zoom/h";
    public static String zoomHorizontalIn = "/daw/zoom/h/+";
    public static String zoomHorizontalOut = "/daw/zoom/h/-";

    public static final String dynamicZoomIn = "/daw/dyn_z/+";
    public static final String dynamicZoomOut = "/daw/dyn_z/-";

    public static final String toggleAutoScroll = "/daw/a_scr/t";

    // --- Editing ---

    public static final String autoGlue = "/daw/a_glue";

    // --- Selection ---

    public static final String selectionSize = "/daw/sel";
    public static String selectionSizePlus = "/daw/sel/+";
    public static String selectionSizeMinus = "/daw/sel/-";

    public static final String selectionMoveRelative = "/daw/sel_m/r";
    public static String selectionMoveLeft = "/daw/sel_m/l";
    public static String selectionMoveRight = "/daw/sel_m/r";

    public static final String selectionRemove = "/daw/sel/del";

    // --- Master ---

    public static final String masterToggleMono = "/mst/mono/t";

    // --- Track Selection ---

    public static final String trackSelectRelative = "/daw/trk/sel/r";
    public static final String trackSelectNext = "/daw/trk/sel/+";
    public static final String trackSelectPrevious = "/daw/trk/sel/-";

    public static final String trackAddRelative = "/daw/trk/seladd/r";
    public static String trackAddNext = "/daw/trk/seladd/+";
    public static String trackAddPrevious = "/daw/trk/seladd/-";

    public static final String trackSelect8Pair = "/daw/trk/8p";
    public static final String trackSelect16Pair = "/daw/trk/16p";

    public static final String trackToggleSelectionAB = "/daw/trk/sel_ab/t";

    // --- Track Settings ---

    public static final String trackToggleMute = "/daw/trk/mute/t";
    public static final String trackToggleSolo = "/daw/trk/mute/s";

    public static final String trackUnmuteAll = "/daw/trk/nomute";
    public static final String trackUnSoloAll = "/daw/trk/nosolo";

    public static final String trackToggleRecordArm = "/daw/trk/rarm/t";

    public static String trackToggleRecordArmAuto = "/daw/trk/rauto/t";

    public static String trackRecArmOn = "/daw/trk/rarm/+";
    public static String trackRecArmOff = "/daw/trk/rarm/-";

    public static String trackUnarmAll = "/daw/trk/noarm";

    public static final String trackRecArmAutoOn = "/daw/trk/rauto/+";

    public static final String trackRecArmAutoOff = "/daw/trk/rauto/-";

    public static final String trackToggleFXBypass = "/daw/trk/byp/t";

    public static final String trackFreeze = "/daw/trk/frz/+";
    public static final String trackUnfreeze = "/daw/trk/frz/-";

    public static final String trackBounceStereo = "/daw/trk/bnc/st";
    public static final String trackBounceStereo2ndPass = "/daw/trk/bnc/st/2nd";

    // --- Item Selection ---

    public static final String itemSelectRelative = "/daw/itm/sel/r";
    public static final String itemSelectNext = "/daw/itm/sel/+";
    public static final String ItemSelectPrevious = "/daw/itm/sel/-";

    public static final String itemAddRelative = "/daw/itm/seladd/r";
    public static final String itemAddNext = "/daw/itm/seladd/+";
    public static final String itemAddPrevious = "/daw/itm/seladd/-";

    public static final String itemToggleMute = "/daw/itm/mute/t";

    // --- Take Selection ---

    public static final String takeSelectRelative = "/daw/tk/sel/r";
    public static final String takeSelectNext = "/daw/tk/sel/+";
    public static final String takeSelectPrevious = "/daw/tk/sel/-";

    public static final String takeRemove = "/daw/tk/del";
    public static final String takeCrop = "/daw/tk/crp";

    // --- Marker ---

    public static final String insertMarkerAtPos = "/daw/mrk/+";

    public static final String goToNextMarker = "/daw/mrk_go/+";
    public static final String goToPreviousMarker = "/daw/mrk_go/-";

    public static final String markerRemove = "/daw/mrk/-";

    // --- Automation ---

    public static final String automationSelectTrackEnvelopeRelative = "/env/sel/r";
    public static final String automationSelectTrackEnvelopeNext = "/env/sel/+";
    public static final String automationSelectTrackEnvelopePrevious = "/env/sel/-";

    public static final String automationModeFree = "/daw/aut/free";
    public static final String automationModeTrim = "/daw/aut/trim";
    public static final String automationModeRead = "/daw/aut/read";
    public static final String automationModeLatch = "/daw/aut/latch";
    public static final String automationModeLatchPreview = "/daw/aut/latchp";
    public static final String automationModeTouch = "/daw/aut/touch";
    public static final String automationModeWrite = "/daw/aut/write";

    public static final String automationModeLatchGeneric = "/daw/aut/latch_gen";

    public static final String automationWriteToCursor = "/daw/aut/w_cur";
    public static final String automationWriteToEnd = "/daw/aut/w_end";

    public static final String automationArmTrackEnvelopes = "/daw/aut/arm_env";

    public static final String automationAutoAddEnvelopesOn = "/daw/aut/aut_add/+";
    public static final String automationAutoAddEnvelopesOff = "/daw/aut/aut_add/-";
    public static final String automationToggleAutoAddEnvelopes = "/daw/aut/aut_add/t";

}

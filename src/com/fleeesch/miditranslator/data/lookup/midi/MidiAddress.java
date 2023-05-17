package com.fleeesch.miditranslator.data.lookup.midi;

public class MidiAddress {

    public static final int[] surfaceMidiModeStatus = {0xB0, 0XB1, 0XB2, 0xB3, 0XB4, 0XB5, 0xB6, 0xB7, 0XB8, 0XB9, 0xBA, 0xBB, 0XBC, 0XBD, 0XBE, 0xBF};
    public static final int[] surfaceMidiModeCC = {1, 2, 3, 4, 5, 6, 11, 12, 1, 2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 16, 17};

    public static final int[] surfaceMidiModeSwitch = {74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89};

    public static final int[][] djDeckJog = {{176, 74}, {177, 74}};
    public static final int[][] djDeckJogSide = {{176, 75}, {177, 75}};
    public static final int[][] djDeckJogTouch = {{176, 76}, {177, 76}};
    public static final int[][] djDeckJogTouchCue = {{176, 77}, {177, 77}};

    public static final int[] djCrossfader = {176, 1};

    public static final int[][] djFadersDeck1 = {{177, 11}, {177, 12}, {177, 14}, {177, 15}, {177, 16}, {177, 17}, {177, 18}, {177, 19}, {177, 20}};
    public static final int[][] djFadersDeck2 = {{178, 11}, {178, 12}, {178, 14}, {178, 15}, {178, 16}, {178, 17}, {178, 18}, {178, 19}, {178, 20}};
    public static final int[][] djFadersDeck3 = {{179, 11}, {179, 12}, {179, 14}, {179, 15}, {179, 16}, {179, 17}, {179, 18}, {179, 19}, {179, 20}};
    public static final int[][] djFadersDeck4 = {{180, 11}, {180, 12}, {180, 14}, {180, 15}, {180, 16}, {180, 17}, {180, 18}, {180, 19}, {180, 20}};

    public static final int[] djTempoDeck1 = {177, 22};
    public static final int[] djTempoDeck2 = {178, 22};
    public static final int[] djTempoDeck3 = {179, 22};
    public static final int[] djTempoDeck4 = {180, 22};

}

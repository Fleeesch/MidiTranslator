package com.fleeesch.miditranslator.functions.string;


import java.util.regex.Pattern;

public class Text {

    //************************************************************
    //      Variables
    //************************************************************

    public static Pattern patternLineBreak = Pattern.compile("(?<=[a-z])(?=[A-Z])|[ *]|[_]");

    //************************************************************
    //      Method : Limit Text Length
    //************************************************************

    public static String limitTextLength(String pStr, int pMaxLength) {

        return pStr.substring(0, Math.min(pMaxLength, pStr.length()));


    }

    //************************************************************
    //      Method : Split with fixed Dimensions
    //************************************************************

    public static String[] splitFixedDimensions(String inputString, String padString, int lineCountMin, int lineCountMax, int charCountMax, boolean cutVowels) {

        String[] line = patternLineBreak.split(inputString, lineCountMax); // split into lines

        int lineCount = line.length; // store lines count

        int maxLines = Math.max(lineCountMin, lineCountMax); // get final string length

        String[] rtn = new String[maxLines]; // init return string

        // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        // Single Line


        if (lineCount == 1) {

            // remove vowels if required
            if (cutVowels && inputString.length() > charCountMax) {
                inputString = inputString.substring(0, 3) + inputString.substring(3).replaceAll("[aeiou]", "");

            }

            // if more than one line is required print string overshoot on 2nd line
            if (maxLines > 1 && inputString.length() > charCountMax + 1) {

                rtn[1] = inputString.substring(charCountMax);

                rtn[1] = rtn[1].substring(0, Math.min(rtn[1].length(), charCountMax));
                lineCount++;


            }

            // trim original string
            rtn[0] = inputString.substring(0, Math.min(inputString.length(), charCountMax));


            // pad remaining lines
            for (int i = lineCount; i < maxLines; i++) rtn[i] = padString;

            // escape
            return rtn;
        }

        // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
        // Multiple Lines


        // transfer lines with limited character length
        for (int i = 0; i < lineCount; i++) {

            // remove vowels if required
            if (cutVowels && line[i].length() > charCountMax) {
                line[i] = line[i].substring(0, 3) + line[i].substring(3).replaceAll("[aeiou]", "");

            }

            rtn[i] = line[i].substring(0, Math.min(line[i].length(), charCountMax));

        }

        // pad remaining lines
        for (int i = lineCount; i < maxLines; i++) rtn[i] = padString;

        return rtn; // return new string
    }


}

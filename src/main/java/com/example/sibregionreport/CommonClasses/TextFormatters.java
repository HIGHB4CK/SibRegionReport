package com.example.sibregionreport.CommonClasses;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.ShortStringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.function.UnaryOperator;

public class TextFormatters {
    public static TextFormatter<Short> createShortTextFormatter() {
        StringConverter<Short> converter = new ShortStringConverter();
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^[0-9]{0,5}$")) {
                try {
                    if (newText.isEmpty() || (Short.parseShort(newText) >= 0 && Short.parseShort(newText) <= Short.MAX_VALUE)) {
                        return change;
                    }
                } catch (NumberFormatException e) {

                }
            }
            return null;
        };
        return new TextFormatter<>(converter, null, filter);
    }

    public static TextFormatter<Float> createFloatTextFormatter() {
        StringConverter<Float> converter = new FloatStringConverter();
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^-?\\d+\\.?\\d*$")) {
                try {
                    if (newText.isEmpty() || (Float.parseFloat(newText) >=0 && Float.parseFloat(newText) <= Float.MAX_VALUE)) {
                        return change;
                    }
                } catch (NumberFormatException e) {

                }
            }
            return null;
        };
        return new TextFormatter<>(converter, null, filter);
    }

    public static TextFormatter<Integer> createIntegerTextFormatter() {
        StringConverter<Integer> converter = new IntegerStringConverter();
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("^[0-9]{0,10}$")) {
                try {
                    if (newText.isEmpty() || (Integer.parseInt(newText) >= 0 && Integer.parseInt(newText) <= Integer.MAX_VALUE)) {
                        return change;
                    }
                } catch (NumberFormatException e) {

                }
            }
            return null;
        };
        return new TextFormatter<>(converter, null, filter);
    }

//    public static TextFormatter<LocalTime> createTimeTextFormatter() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
//        StringConverter<LocalTime> converter = new LocalTimeStringConverter(formatter, formatter);
//
//        UnaryOperator<TextFormatter.Change> filter = change -> {
//            String newText = change.getControlNewText();
//            if (newText.isEmpty() || newText.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")) {
//                try {
//                    LocalTime time = LocalTime.parse(newText, formatter);
//                    return change;
//                } catch (DateTimeParseException e) {
//
//                }
//            }
//            return null;
//        };
//        return new TextFormatter<>(converter, null, filter);
//    }
}

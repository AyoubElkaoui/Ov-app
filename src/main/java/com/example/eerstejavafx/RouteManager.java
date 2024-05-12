package com.example.eerstejavafx;

import java.util.HashMap;
import java.util.Map;

public class RouteManager {

    public static Map<String, Map<String, String>> currentTransportation;

    final private static Map<String, Map<String, String>> routeTimesTrain;
    final private static Map<String, Map<String, String>> routeTimesBus;

    // Initialize Hashmaps for Train and Bus
    static {

        routeTimesTrain = new HashMap<>();

        routeTimesTrain.put("Amersfoort", Map.of("Eindhoven", "51min", "Utrecht", "12min", "Maastricht", "93min", "Amsterdam", "60min", "Den Haag", "40min"));
        routeTimesTrain.put("Eindhoven", Map.of("Amersfoort", "51min", "Utrecht", "41min", "Maastricht", "42min", "Amsterdam", "50min", "Den Haag", "67min"));
        routeTimesTrain.put("Utrecht", Map.of("Amersfoort", "12min", "Eindhoven", "41min", "Maastricht", "85min", "Amsterdam", "20min", "Den Haag", "29min"));
        routeTimesTrain.put("Maastricht", Map.of("Amersfoort", "90min", "Eindhoven", "42min", "Utrecht", "85min", "Amsterdam", "101min", "Den Haag", "109min"));
        routeTimesTrain.put("Amsterdam", Map.of("Amersfoort", "60min", "Eindhoven", "51min", "Utrecht", "21min", "Maastricht", "101min", "Den Haag", "31min"));
        routeTimesTrain.put("Den Haag", Map.of("Amersfoort", "40min", "Eindhoven", "67min", "Utrecht", "30min", "Maastricht", "109min", "Amsterdam", "31min"));

        routeTimesBus = new HashMap<>();

        routeTimesBus.put("Amersfoort", Map.of("Eindhoven", "84min", "Utrecht", "19min", "Maastricht", "155min", "Amsterdam", "99min", "Den Haag", "67min"));
        routeTimesBus.put("Eindhoven", Map.of("Amersfoort", "84min", "Utrecht", "71min", "Maastricht", "70min", "Amsterdam", "84min", "Den Haag", "112min"));
        routeTimesBus.put("Utrecht", Map.of("Amersfoort", "19min", "Eindhoven", "71min", "Maastricht", "135min", "Amsterdam", "34min", "Den Haag", "48min"));
        routeTimesBus.put("Maastricht", Map.of("Amersfoort", "151min", "Eindhoven", "69min", "Utrecht", "142min", "Amsterdam", "169min", "Den Haag", "183min"));
        routeTimesBus.put("Amsterdam", Map.of("Amersfoort", "89min", "Eindhoven", "124min", "Utrecht", "34min", "Maastricht", "169min", "Den Haag", "51min"));
        routeTimesBus.put("Den Haag", Map.of("Amersfoort", "67min", "Eindhoven", "118min", "Utrecht", "48min", "Maastricht", "183min", "Amsterdam", "51min"));

        currentTransportation = routeTimesTrain;
    }

    public static String getRouteTime(String departure, String destination) {
        if (currentTransportation.equals(routeTimesTrain)) {
            return getRouteTimeForTransportation(routeTimesTrain, departure, destination);
        } else if (currentTransportation.equals(routeTimesBus)) {
            return getRouteTimeForTransportation(routeTimesBus, departure, destination);
        } else {
            return "";
        }
    }

    private static String getRouteTimeForTransportation(Map<String, Map<String, String>> routeTimes, String departure, String destination) {
        if (routeTimes.containsKey(departure) && routeTimes.get(departure).containsKey(destination)) {
            return routeTimes.get(departure).get(destination);
        } else {
            return "Zelfde halte geselecteerd";
        }
    }

    public static void setTransportationBus() {
        currentTransportation = routeTimesBus;
    }

    public static void setTransportationTrain() {
        currentTransportation= routeTimesTrain;
    }


}

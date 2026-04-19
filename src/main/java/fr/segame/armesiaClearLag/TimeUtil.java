package fr.segame.armesiaClearLag;

public class TimeUtil {

    public static String formatTimeFull(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        StringBuilder sb = new StringBuilder();

        if (hours > 0) {
            sb.append(hours).append("h");
        }

        if (minutes > 0) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(minutes).append("m");
        }

        if (secs > 0) {
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(secs).append("s");
        }

        if (sb.isEmpty()) {
            return "0s";
        }

        return sb.toString();
    }
}
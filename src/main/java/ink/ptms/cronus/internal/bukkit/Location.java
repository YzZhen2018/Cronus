package ink.ptms.cronus.internal.bukkit;

import ink.ptms.cronus.util.Utils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-05-24 14:09
 */
public class Location {

    private Mode mode;
    private org.bukkit.Location[] area;
    private org.bukkit.Location[] points;

    public Location(Mode mode, org.bukkit.Location[] area, org.bukkit.Location[] points) {
        this.mode = mode;
        this.area = area;
        this.points = points;
    }

    public org.bukkit.Location toBukkit() {
        return points[0];
    }

    public boolean isBukkit() {
        try {
            return toBukkit() != null;
        } catch (Throwable ignored) {
        }
        return false;
    }

    public boolean inSelect(org.bukkit.Location location) {
        if (!isSelectWorld(location)) {
            return false;
        }
        if (mode == Mode.AREA) {
            return location.toVector().isInAABB(area[0].toVector(), area[1].toVector());
        } else {
            return Arrays.asList(points).contains(location);
        }
    }

    public boolean isSelectWorld(org.bukkit.Location location) {
        if (mode == Mode.AREA) {
            return location.getWorld().equals(area[0].getWorld());
        } else {
            return Arrays.stream(points).anyMatch(p -> p.getWorld().equals(location.getWorld()));
        }
    }

    public Mode getMode() {
        return mode;
    }

    public org.bukkit.Location[] getArea() {
        return area;
    }

    public org.bukkit.Location[] getPoints() {
        return points;
    }

    public enum Mode {

        AREA, POINT
    }

    public String asString() {
        if (mode == Mode.AREA) {
            return Utils.fromLocation(area[0]) + "~" + Utils.fromLocation(area[1]);
        } else {
            return Arrays.stream(points).map(Utils::fromLocation).collect(Collectors.joining(";"));
        }
    }

    @Override
    public String toString() {
        return "Location{" +
                "mode=" + mode +
                ", area=" + Arrays.toString(area) +
                ", points=" + Arrays.toString(points) +
                '}';
    }
}

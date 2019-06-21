package ink.ptms.cronus.internal.bukkit;

import me.skymc.taboolib.TabooLib;
import org.bukkit.util.NumberConversions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-05-30 13:46
 */
public class Block {

    private List<Point> points;

    public Block(String in) {
        points = Arrays.stream(in.split(";")).map(Point::new).collect(Collectors.toList());
    }

    public boolean isSelect(org.bukkit.block.Block block) {
        return points.stream().anyMatch(b -> b.isSelect(block));
    }

    public class Point {

        private String name;
        private int data;

        public Point(String in) {
            String[] v = in.split("[:~]");
            name = v[0];
            data = TabooLib.getVersionNumber() < 11300 && v.length > 1 ? NumberConversions.toInt(v[1]) : -1;
        }

        public boolean isSelect(org.bukkit.block.Block block) {
            return block.getType().name().equalsIgnoreCase(name) && (data == -1 || block.getData() == data);
        }

        public String asString() {
            return name + ":" + data;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "name='" + name + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    public String asString() {
        return String.join(";", points.stream().map(Point::asString).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "Block{" +
                "points=" + points +
                '}';
    }
}

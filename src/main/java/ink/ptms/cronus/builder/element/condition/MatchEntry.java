package ink.ptms.cronus.builder.element.condition;

import com.google.common.collect.Lists;
import ink.ptms.cronus.internal.condition.ConditionParser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author 坏黑
 * @Since 2019-06-21 9:46
 */
public class MatchEntry {

    private MatchType type;
    private String single;
    private List<MatchEntry> collect;

    public MatchEntry(MatchType type, String single) {
        this.type = type;
        this.single = single;
    }

    public MatchEntry(MatchType type, List<MatchEntry> collect) {
        this.type = type;
        this.collect = collect;
    }

    public List<String> asList(int index) {
        List<String> list = Lists.newArrayList();
        if (type == MatchType.SINGLE) {
            list.add(IntStream.range(0, index).mapToObj(i -> "  ").collect(Collectors.joining()) + (index > 0 ? "§f- " : "") + "§8" + getSingleTranslate());
        } else {
            list.add(IntStream.range(0, index).mapToObj(i -> "  ").collect(Collectors.joining()) + (index > 0 ? "§f- " : "") + "§7" + type.getName() + ":");
            for (MatchEntry matchEntry : collect) {
                list.addAll(matchEntry.asList(index + 1));
            }
        }
        return list;
    }

    public String getSingleTranslate() {
        try {
            return Optional.ofNullable(ConditionParser.parse(single).translate()).orElse(single);
        } catch (Throwable ignored) {
            return single;
        }
    }

    public MatchType getType() {
        return this.type;
    }

    public String getSingle() {
        return this.single;
    }

    public List<MatchEntry> getCollect() {
        return this.collect;
    }

    public void setType(MatchType type) {
        this.type = type;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public void setCollect(List<MatchEntry> collect) {
        this.collect = collect;
    }
}

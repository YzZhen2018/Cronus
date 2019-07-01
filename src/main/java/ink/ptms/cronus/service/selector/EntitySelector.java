package ink.ptms.cronus.service.selector;

import com.google.common.collect.Lists;
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser;
import ink.ptms.cronus.service.Service;
import ink.ptms.cronus.service.selector.impl.SelectorCitizens;
import ink.ptms.cronus.service.selector.impl.SelectorMythicMobs;
import ink.ptms.cronus.service.selector.impl.SelectorShopkeepers;
import ink.ptms.cronus.uranus.annotations.Auto;
import me.skymc.taboolib.common.util.SimpleI18n;
import me.skymc.taboolib.string.ArrayUtils;
import org.bukkit.entity.Entity;

import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-10 16:36
 */
@Auto
public class EntitySelector implements Service {

    private List<Selector> selectors = Lists.newArrayList();

    @Override
    public void init() {
        selectors.add(new SelectorCitizens());
        selectors.add(new SelectorMythicMobs());
        selectors.add(new SelectorShopkeepers());
        selectors.forEach(Selector::init);
    }

    @Override
    public void cancel() {
    }

    public boolean isSelect(Entity entity, String in) {
        if (in == null) {
            return false;
        }
        String[] v = in.split("=");
        if (v.length > 1) {
            for (Selector selector : selectors) {
                if (selector.match(v[0])) {
                    return selector.isHooked() && selector.isSelect(entity, ArrayUtils.arrayJoin(v, 1));
                }
            }
        }
        return BukkitParser.toEntity(in).isSelect(entity);
    }

    public String getSelectDisplay(String in) {
        String[] v = in.split("=");
        if (v.length > 1) {
            for (Selector selector : selectors) {
                if (selector.match(v[0])) {
                    return selector.isHooked() ? selector.getDisplay(ArrayUtils.arrayJoin(v, 1)) : "No Hooked";
                }
            }
        }
        return in;
    }

    public String fromEntity(Entity entity) {
        for (Selector selector : selectors) {
            if (selector.isHooked()) {
                String fromEntity = selector.fromEntity(entity);
                if (fromEntity != null) {
                    return selector.getPrefix()[0] + "=" + fromEntity;
                }
            }
        }
        return "type=" + entity.getType() + ",name=" + SimpleI18n.getName(entity);
    }
}

package ink.ptms.cronus.internal.condition;

import com.ilummc.tlib.logger.TLogger;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.internal.condition.impl.CondNull;
import me.skymc.taboolib.common.inject.TInject;

import java.util.Map;
import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-29 13:07
 */
public class ConditionParser {

    @TInject
    static TLogger logger;

    public static Condition parse(String in) {
        for (Map.Entry<String, ConditionCache> entry : Cronus.getCronusService().getRegisteredCondition().entrySet()) {
            Matcher matcher = entry.getValue().getPattern().matcher(in);
            if (matcher.find()) {
                try {
                    Condition instance = entry.getValue().instance();
                    instance.init(matcher, in);
                    return instance;
                } catch (Throwable t) {
                    logger.error("Condition \"" + in + "\" parsing failed.");
                    t.printStackTrace();
                }
                return new CondNull(in);
            }
        }
        logger.error("Condition \"" + in + "\" parsing failed.");
        return new CondNull(in);
    }
}

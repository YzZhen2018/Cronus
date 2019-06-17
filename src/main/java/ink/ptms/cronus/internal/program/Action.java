package ink.ptms.cronus.internal.program;

/**
 * @Author 坏黑
 * @Since 2019-05-28 17:15
 */
public enum Action {

    ACCEPT, SUCCESS, FAILURE;

    public static Action fromName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (Throwable ignored) {
        }
        return null;
    }
}

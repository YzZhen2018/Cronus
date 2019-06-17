package ink.ptms.cronus.database;

import ink.ptms.cronus.CronusMirror;
import ink.ptms.cronus.database.data.DataPlayer;
import ink.ptms.cronus.service.Service;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:11
 */
public abstract class Database implements Service {

    public void upload(Player player, DataPlayer dataPlayer) {
        CronusMirror.getMirror("Database:AsyncUpload").start();
        upload0(player, dataPlayer);
        CronusMirror.getMirror("Database:AsyncUpload").stop();
    }

    public DataPlayer download(Player player) {
        CronusMirror.getMirror("Database:AsyncDownload").start();
        DataPlayer dataPlayer = download0(player);
        CronusMirror.getMirror("Database:AsyncDownload").stop();
        return dataPlayer;
    }

    abstract protected void upload0(Player player, DataPlayer dataPlayer);

    abstract protected DataPlayer download0(Player player);

}

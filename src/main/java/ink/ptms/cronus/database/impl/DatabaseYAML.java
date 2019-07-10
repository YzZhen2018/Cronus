package ink.ptms.cronus.database.impl;

import ink.ptms.cronus.database.Database;
import ink.ptms.cronus.database.data.DataPlayer;
import io.izzel.taboolib.module.db.local.LocalPlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:11
 */
public class DatabaseYAML extends Database {

    @Override
    public void init() {
    }

    @Override
    public void cancel() {
    }

    @Override
    protected void upload0(Player player, DataPlayer dataPlayer) {
        LocalPlayer.get(player).set("Cronus.data", dataPlayer.writeBase64());
    }

    @Override
    protected DataPlayer download0(Player player) {
        DataPlayer dataPlayer = new DataPlayer(player);
        FileConfiguration playerData = LocalPlayer.get(player);
        if (playerData.contains("Cronus.data")) {
            dataPlayer.readBase64(playerData.getString("Cronus.data"));
        }
        return dataPlayer;
    }
}

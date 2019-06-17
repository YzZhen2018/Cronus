package ink.ptms.cronus.database.impl;

import ink.ptms.cronus.database.Database;
import ink.ptms.cronus.database.data.DataPlayer;
import me.skymc.taboolib.database.PlayerDataManager;
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
        PlayerDataManager.getPlayerData(player).set("Cronus.data", dataPlayer.writeBase64());
    }

    @Override
    protected DataPlayer download0(Player player) {
        DataPlayer dataPlayer = new DataPlayer(player);
        FileConfiguration playerData = PlayerDataManager.getPlayerData(player);
        if (playerData.contains("Cronus.data")) {
            dataPlayer.readBase64(playerData.getString("Cronus.data"));
        }
        return dataPlayer;
    }
}

package ink.ptms.cronus.database.impl;

import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.database.Database;
import ink.ptms.cronus.database.data.DataPlayer;
import io.izzel.taboolib.module.db.source.DBSource;
import io.izzel.taboolib.module.db.sql.SQLColumn;
import io.izzel.taboolib.module.db.sql.SQLColumnType;
import io.izzel.taboolib.module.db.sql.SQLHost;
import io.izzel.taboolib.module.db.sql.SQLTable;
import org.bukkit.entity.Player;

import javax.sql.DataSource;

/**
 * @Author 坏黑
 * @Since 2019-05-24 0:11
 */
public class DatabaseSQL extends Database {

    private SQLHost host;
    private SQLTable table;
    private DataSource dataSource;
    private boolean uniqueId;

    @Override
    public void init() {
        host = new SQLHost(Cronus.getConf().getConfigurationSection("Database"), Cronus.getInst());
        table = new SQLTable(Cronus.getConf().getStringColored("Database.table"), SQLColumn.PRIMARY_KEY_ID, new SQLColumn(SQLColumnType.TEXT, "player"), new SQLColumn(SQLColumnType.LONGTEXT, "data"));
        try {
            dataSource = DBSource.create(host);
            table.executeUpdate(table.createQuery()).dataSource(dataSource).run();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        uniqueId = Cronus.getConf().getBoolean("Database.uniqueId");
    }

    @Override
    public void cancel() {
        DBSource.closeDataSource(host);
    }

    @Override
    protected void upload0(Player player, DataPlayer dataPlayer) {
        if (isExists(player)) {
            table.executeUpdate("data = ?", "player = ?")
                    .dataSource(dataSource)
                    .statement(s -> {
                        s.setString(1, dataPlayer.writeBase64());
                        s.setString(2, toName(player));
                    }).run();
        } else {
            table.executeInsert("?, ?")
                    .dataSource(dataSource)
                    .statement(s -> {
                        s.setString(1, toName(player));
                        s.setString(2, dataPlayer.writeBase64());
                    }).run();
        }
    }

    @Override
    protected DataPlayer download0(Player player) {
        DataPlayer dataPlayer = new DataPlayer(player);
        table.executeSelect("player = ?")
                .dataSource(dataSource)
                .statement(s -> s.setString(1, toName(player)))
                .resultNext(r -> dataPlayer.readBase64(r.getString("data")))
                .run();
        return dataPlayer;
    }

    public boolean isExists(Player player) {
        return table.executeSelect("player = ?")
                .dataSource(dataSource)
                .statement(s -> s.setString(1, toName(player)))
                .resultNext(r -> true)
                .run(false, false);
    }

    public String toName(Player player) {
        return uniqueId ? player.getUniqueId().toString() : player.getName();
    }
}

package ink.ptms.cronus.builder.element;

import com.google.common.collect.Lists;
import me.skymc.taboolib.inventory.builder.v2.CloseTask;
import me.skymc.taboolib.json.tellraw.TellrawJson;
import me.skymc.taboolib.message.ChatCatcher;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-18 23:09
 */
public class BuilderStageList extends BuilderList {

    private List<BuilderStage> stages = Lists.newArrayList();

    public BuilderStageList() {
        super("任务阶段", Lists.newArrayList());
        this.defaultEdit = index -> {
            // 新增
            if (list.get(index).equals("$append")) {
                // 事件
                ChatCatcher.call(player, new ChatCatcher.Catcher() {
                    @Override
                    public ChatCatcher.Catcher before() {
                        toggle = true;
                        player.closeInventory();
                        TellrawJson.create().append("§7§l[§f§lCronus§7§l] §7在对话框中输入新的任务阶段名称. ")
                                .append("§8(取消)").hoverText("§7点击").clickCommand("quit()")
                                .send(player);
                        return this;
                    }

                    @Override
                    public boolean after(String s) {
                        if (list.contains(s)) {
                            error(player, "任务阶段名称重复.");
                            return true;
                        } else {
                            stages.add(new BuilderStage(s));
                            open(player, page, close);
                            return false;
                        }
                    }

                    @Override
                    public void cancel() {
                        open(player, page, close);
                    }
                });
            } else {
                stages.get(index).open(player, this);
            }
        };
        this.defaultDelete = index -> {
            stages.remove(index);
            open(player, page, close);
        };
    }

    @Override
    public void open(Player player, int page, CloseTask close, Candidate candidate) {
        this.list = Lists.newArrayList(stages.stream().map(BuilderStage::getId).collect(Collectors.toList()));
        this.listOrigin = Lists.newArrayList();
        super.open(player, page, close, candidate);
    }

    public List<BuilderStage> getStages() {
        return stages;
    }
}

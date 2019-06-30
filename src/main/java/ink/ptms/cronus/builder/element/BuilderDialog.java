package ink.ptms.cronus.builder.element;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.Builders;
import ink.ptms.cronus.builder.element.dialog.Dialog;
import ink.ptms.cronus.internal.version.MaterialControl;
import ink.ptms.cronus.service.hook.EntitySelector;
import me.skymc.taboolib.fileutils.ConfigUtils;
import me.skymc.taboolib.fileutils.FileUtils;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import me.skymc.taboolib.inventory.builder.v2.ClickType;
import me.skymc.taboolib.json.tellraw.TellrawJson;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.List;

/**
 * @Author 坏黑
 * @Since 2019-06-24 14:21
 */
public class BuilderDialog extends BuilderQuest {

    private String title = "对话";
    private String target;
    private List<String> actionOpen = Lists.newArrayList();
    private List<String> actionClose = Lists.newArrayList();
    private Dialog dialog;

    public BuilderDialog(String id) {
        super(id);
    }

    public BuilderDialog(File file) {
        super("");
        YamlConfiguration yaml = ConfigUtils.loadYaml(Cronus.getInst(), file);
        for (String id : yaml.getKeys(false)) {
            import0(yaml.getConfigurationSection(id));
            return;
        }
    }

    public void import0(ConfigurationSection section) {
        id = section.getName();
        title = section.getString("title", "对话");
        target = section.getString("target");
        if (section.contains("open")) {
            actionOpen = section.getStringList("open");
        }
        if (section.contains("close")) {
            actionClose = section.getStringList("close");
        }
        if (section.contains("dialog")) {
            dialog = new Dialog(false);
            dialog.import0(section.getConfigurationSection("dialog").getValues(false));
        }
    }

    @Override
    public void export() {
        File file = new File(Cronus.getCronusService().getService(ink.ptms.cronus.service.dialog.Dialog.class).getFolder(), "builder/" + id + ".yml");
        FileUtils.createNewFileAndPath(file);
        YamlConfiguration yaml = ConfigUtils.loadYaml(Cronus.getInst(), file);
        yaml.set(id + ".title", title);
        yaml.set(id + ".target", target);
        yaml.set(id + ".dialog", dialog.export0());
        if (!actionOpen.isEmpty()) {
            yaml.set(id + ".open", actionOpen);
        }
        if (!actionClose.isEmpty()) {
            yaml.set(id + ".close", actionClose);
        }
        try {
            yaml.save(file);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void open(Player player) {
        if (dialog == null) {
            dialog = new Dialog(false);
        }
        Inventory inventory = Builders.normal("对话构建 : " + id,
                e -> {
                    if (e.getClickType() == ClickType.CLICK) {
                        e.castClick().setCancelled(true);
                        switch (e.castClick().getRawSlot()) {
                            case 10:
                                editString(e.getClicker(), "对话标题", title, r -> title = r);
                                break;
                            case 11:
                                editString(e.getClicker(), "对话目标", target, r -> target = r);
                                normal(e.getClicker(), "可用：");
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("[name]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7生物名称",
                                                "§7根据生物名称判断对话目标",
                                                "",
                                                "§7示范:",
                                                "§f僵尸 §8(所有名字中含有 \"僵尸\" 的实体均可作为对话目标)"
                                                ))).clickSuggest("[name]").send(e.getClicker());
                                TellrawJson.create()
                                    .append("§7§l[§f§lCronus§7§l] §7- ")
                                    .append(formatTime("citizens=[id]"))
                                    .hoverText(String.join("\n", Lists.newArrayList(
                                            "§7Citizens 扩展支持",
                                            "§7根据 Citizens 序号判断对话目标",
                                            "",
                                            "§7别名:",
                                            "§fnpc=[id]",
                                            "",
                                            "§7示范:",
                                            "§fnpc=0 §8(序号为 \"0\" 的 Citizens 实体作为对话目标)"
                                    ))).clickSuggest("citizens=[id]").send(e.getClicker());
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("mythicmob=[id]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7Mythicmobs 扩展支持",
                                                "§7根据 Mythicmobs 序号判断对话目标",
                                                "",
                                                "§7别名:",
                                                "§fmm=[id]",
                                                "",
                                                "§7示范:",
                                                "§fmm=mob_0 §8(序号为 \"mob_0\" 的 Mythicmobs 实体作为对话目标)"
                                        ))).clickSuggest("mythicmob=[id]").send(e.getClicker());
                                TellrawJson.create()
                                        .append("§7§l[§f§lCronus§7§l] §7- ")
                                        .append(formatTime("shopkeeper=[uuid]"))
                                        .hoverText(String.join("\n", Lists.newArrayList(
                                                "§7Shopkeepers 扩展支持",
                                                "§7根据 Shopkeepers 序号判断对话目标",
                                                "",
                                                "§7别名:",
                                                "§fshop=[uuid]",
                                                "",
                                                "§7示范:",
                                                "§fshop=ea23 ... §8(序号为 \"ea23 ...\" 的 Shopkeepers 实体作为对话目标)"
                                        ))).clickSuggest("shopkeeper=[uuid]").send(e.getClicker());
                                break;
                            case 12:
                                new BuilderListEffect("对话开始动作", actionOpen).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 13:
                                new BuilderListEffect("对话结束动作", actionClose).open(e.getClicker(), 0, c -> open(e.getClicker()), this::getEffect);
                                break;
                            case 14:
                                dialog.open(player, c -> open(player));
                                break;
                            case 49:
                                player.closeInventory();
                                normal(player, "正在导出...");
                                try {
                                    export();
                                    normal(player, "导出完成!");
                                } catch (Throwable t) {
                                    t.printStackTrace();
                                    error(player, "导出失败: " + t.getMessage());
                                }
                                break;
                        }
                    }
                },
                e -> {

                });
        inventory.setItem(10, new ItemBuilder(Material.NAME_TAG)
                .name("§b对话标题")
                .lore("", "§f" + title)
                .build());
        inventory.setItem(11, new ItemBuilder(Material.NAME_TAG)
                .name("§b对话目标")
                .lore("", "§f" + (target == null ? "无" : getTargetDisplay()))
                .build());
        inventory.setItem(12, new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                .name("§b对话开始动作")
                .lore(toLore(actionOpen))
                .build());
        inventory.setItem(13, new ItemBuilder(MaterialControl.REPEATER.parseMaterial())
                .name("§b对话结束动作")
                .lore(toLore(actionClose))
                .build());
        inventory.setItem(14, new ItemBuilder(MaterialControl.BOOK.parseMaterial())
                .name("§b对话结构")
                .lore("", dialog == null ? "§f无" : "§f...")
                .build());
        inventory.setItem(49, new ItemBuilder(MaterialControl.WRITABLE_BOOK.parseMaterial())
                .name("§a保存配置")
                .lore("", "§7文件位置", "§8§nplugins/Cronus/dialog/builder/" + id + ".yml")
                .build());
        player.openInventory(inventory);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
    }

    protected String getTargetDisplay() {
        return Cronus.getCronusService().getService(EntitySelector.class).getSelectDisplay(target);
    }
}

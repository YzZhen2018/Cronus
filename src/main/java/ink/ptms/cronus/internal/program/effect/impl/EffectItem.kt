package ink.ptms.cronus.internal.program.effect.impl

import ink.ptms.cronus.Cronus
import ink.ptms.cronus.internal.bukkit.ItemStack
import ink.ptms.cronus.internal.bukkit.parser.BukkitParser
import ink.ptms.cronus.internal.program.QuestProgram
import ink.ptms.cronus.uranus.annotations.Auto
import ink.ptms.cronus.uranus.program.Program
import ink.ptms.cronus.uranus.program.effect.Effect
import io.izzel.taboolib.cronus.CronusUtils
import io.izzel.taboolib.util.item.Items
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.util.NumberConversions

import java.util.regex.Matcher

/**
 * @Author 坏黑
 * @Since 2019-05-11 17:06
 */
@Auto
class EffectItem : Effect() {

    private var action: String? = null
    private var itemStack: Any? = null

    override fun pattern(): String {
        return "item\\.(?<action>\\S+) (?<item>.+)"
    }

    override fun getExample(): String {
        return "item.[action] [item]"
    }

    override fun match(matcher: Matcher) {
        action = matcher.group("action").toLowerCase()
        when (action) {
            "take", "remove" -> {
                val item = matcher.group("item")
                if (item.startsWith("bukkit=")) {
                    itemStack = BukkitParser.toItemStack(item.substring("bukkit=".length))
                } else {
                    itemStack = Cronus.getCronusService().itemStorage.getItem(item.substring("bukkit=".length))
                }
            }
            "add", "give" -> {
                val item = matcher.group("item")
                if (item.startsWith("bukkit=")) {
                    itemStack = BukkitParser.toItemStack(item.substring("bukkit=".length))
                } else {
                    val args = item.split(" ")
                    itemStack = Cronus.getCronusService().itemStorage.getItem(args[0])?.run {
                        this.amount = if (args.size > 1) NumberConversions.toInt(args[1]) else 1
                        this
                    }
                }
            }
        }
    }

    override fun eval(program: Program) {
        if (program is QuestProgram) {
            val player = program.player
            when (action) {
                "take", "remove" -> {
                    if (itemStack is ItemStack) {
                        (itemStack as ItemStack).takeItem(player)
                    } else if (itemStack is org.bukkit.inventory.ItemStack) {
                        val item = itemStack as org.bukkit.inventory.ItemStack
                        Items.takeItem(player.inventory, { it.isSimilar(item) }, (item).amount)
                    }
                }
                "add", "give" -> {
                    if (itemStack is ItemStack) {
                        CronusUtils.addItem(player, (itemStack as ItemStack).toBukkitItem())

                    } else if (itemStack is org.bukkit.inventory.ItemStack) {
                        CronusUtils.addItem(player, itemStack as org.bukkit.inventory.ItemStack)
                    }
                }
            }
        }
    }

    override fun toString(): String {
        return "EffectItem(action=$action, itemStack=$itemStack)"
    }
}

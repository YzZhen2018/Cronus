package ink.ptms.cronus.internal.program.effect.impl

import ink.ptms.cronus.CronusAPI
import ink.ptms.cronus.database.data.DataQuest
import ink.ptms.cronus.internal.QuestStage
import ink.ptms.cronus.internal.QuestTask
import ink.ptms.cronus.internal.program.QuestProgram
import ink.ptms.cronus.uranus.annotations.Auto
import ink.ptms.cronus.uranus.function.FunctionParser
import ink.ptms.cronus.uranus.program.Program
import ink.ptms.cronus.uranus.program.effect.Effect
import io.izzel.taboolib.module.inject.TInject
import io.izzel.taboolib.module.locale.logger.TLogger

import java.util.regex.Matcher

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
class EffectQuestTask : Effect() {

    private var action: String? = null
    private var value: String? = null

    override fun pattern(): String {
        return "quest\\.task\\.(?<action>\\S+) (?<value>.+)"
    }

    override fun getExample(): String {
        return "quest.task.[action] [content]"
    }

    override fun match(matcher: Matcher) {
        action = matcher.group("action").toLowerCase()
        value = matcher.group("value")
    }

    override fun eval(program: Program) {
        if (program is QuestProgram) {
            val dataQuest = program.dataQuest
            val questStage = dataQuest.stage
            if (questStage == null) {
                logger!!.warn("No Stage: " + dataQuest.currentStage)
                return
            }
            val content = FunctionParser.parseAll(program, value)
            when (action) {
                "reset" -> questStage.task.filter { content == "*" || content.split(";").contains(it.id)  }.forEach { it.reset(dataQuest) }
                "complete" -> questStage.task.filter { content == "*" || content.split(";").contains(it.id)  }.forEach { it.complete(dataQuest) }
                else -> logger!!.warn("Invalid Action: $action $value")
            }
        }
    }

    override fun toString(): String {
        return "EffectQuestTask{" +
                "action='" + action + '\''.toString() +
                ", value='" + value + '\''.toString() +
                '}'.toString()
    }

    companion object {

        @TInject
        private val logger: TLogger? = null
    }
}

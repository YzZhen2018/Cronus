package ink.ptms.cronus.internal.program.function;

import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.Function;
import ink.ptms.cronus.uranus.program.Program;

/**
 * @Author 坏黑
 * @Since 2019-05-12 15:24
 */
@Auto
public class FunctionQuest extends Function {

    @Override
    public String getName() {
        return "quest";
    }

    @Override
    public Object eval(Program program, String... args) {
        if (program instanceof QuestProgram) {
            switch (args[0].toLowerCase()) {
                case "name":
                    return ((QuestProgram) program).getDataQuest().getCurrentQuest();
                case "name.stage":
                    return ((QuestProgram) program).getDataQuest().getCurrentStage();
                case "time.start":
                    return ((QuestProgram) program).getDataQuest().getTimeStart();
                case "time.complete":
                    return ((QuestProgram) program).getDataPlayer().getQuestCompleted().getOrDefault(((QuestProgram) program).getDataQuest().getCurrentQuest(), 0L);
                default:
                    return ((QuestProgram) program).getDataQuest().getCurrentQuest();
            }
        }
        return "<Non-Quest>";
    }
}

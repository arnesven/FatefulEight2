package rulebook.chapters;

import control.FatefulEight;
import model.Model;
import model.actions.PassiveCombatAction;
import model.combat.abilities.AbilityCombatAction;
import model.combat.abilities.CombatAction;
import model.combat.abilities.SkillAbilityCombatAction;
import view.DrawingArea;
import view.ScreenHandler;
import view.help.CombatHelpChapter;
import view.help.SpecificSkillHelpDialog;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AbilitiesChapter extends RulebookChapter {
    public AbilitiesChapter() {
        super("Abilities");
    }

    @Override
    public void generate(BufferedWriter writer) throws IOException {
        writer.write(CombatHelpChapter.CombatAbilitiesChapter.ABILITIES_TEXT+"\n");
        writer.newLine();

        List<CombatAction> abilities = new ArrayList<>();
        abilities.addAll(AbilityCombatAction.getAllCombatAbilities(null));
        abilities.addAll(AbilityCombatAction.getAllPassiveCombatActions());

        abilities.sort(Comparator.comparing(CombatAction::getName));

        DrawingArea drawingArea = new DrawingArea();
        FatefulEight frame = new FatefulEight(drawingArea);
        frame.dispose();
        Model model = new Model(drawingArea.getScreenHandler(), frame);

        for (CombatAction ca : abilities) {
            writer.write("## " + ca.getName() + "\n");
            writer.write(ca.getHelpChapter(model).getText() + "\n");
            writer.newLine();
        }

    }
}

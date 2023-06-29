package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.Item;
import model.items.spells.Spell;
import util.BeforeAndAfterLine;

import java.util.ArrayList;
import java.util.List;


public class AnalyzeSpellDialog extends AnalyzeDialog {

    private static final int DIALOG_HEIGHT_BASE = 16;
    private final Spell spell;
    private final List<BeforeAndAfterLine<Double>> content;

    public AnalyzeSpellDialog(Model model, Spell spell) {
        super(model, DIALOG_HEIGHT_BASE);
        this.spell = spell;
        this.content = analyzeCastChance(model, spell);
    }

    public static List<BeforeAndAfterLine<Double>> analyzeCastChance(Model model, Spell spell) {
        List<BeforeAndAfterLine<Double>> result = new ArrayList<>();
        for (GameCharacter caster : model.getParty().getPartyMembers()) {
            int successes = 0;
            for (int dieRoll = 2; dieRoll <= 10; ++dieRoll) {
                int modifiedResult = dieRoll + caster.getRankForSkill(spell.getSkill()) +
                        caster.getRankForSkill(Skill.SpellCasting);
                if (modifiedResult >= spell.getDifficulty()) {
                    successes++;
                }
            }
            result.add(new BeforeAndAfterLine<>(caster.getFirstName(), successes * 10.0, 0.0));
        }
        return result;
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        objs.addAll(makeHeader(spell, xStart, yStart));
        yStart += 8;
        objs.add(new TextDecoration(spell.getSkill().getName(), xStart, ++yStart,  MyColors.WHITE, MyColors.BLUE, true));
        objs.add(new TextDecoration("Difficulty " + spell.getDifficulty(), xStart, ++yStart,  MyColors.WHITE, MyColors.BLUE, true));
        objs.add(new TextDecoration("HP Cost " + spell.getHPCost(), xStart, ++yStart, MyColors.WHITE, MyColors.BLUE, true));
        yStart += 2;
        objs.addAll(makeDrawableObjects(content, xStart, yStart));
        return objs;
    }

    protected List<DrawableObject> makeDrawableObjects(List<BeforeAndAfterLine<Double>> content, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        for (BeforeAndAfterLine<Double> line : content) {
            String text =  String.format("%-11s %2.0f", line.getLabel(), line.getBefore());
            objs.add(new TextDecoration(text + "%", xStart+5, yStart,
                    MyColors.WHITE, MyColors.BLUE, false));
            yStart++;
        }
        return objs;
    }

    @Override
    public List<DrawableObject> getAnalysisDrawableObjects(Model model, Item it, int xStart, int yStart) {
        return makeDrawableObjects(analyzeCastChance(model, (Spell)it), xStart, yStart);
    }
}

package view;

import model.Model;
import model.items.Item;
import model.items.Scroll;
import model.items.spells.Spell;
import util.BeforeAndAfterLine;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeScrollDialog extends AnalyzeSpellDialog {
    private final Scroll scroll;
    private final List<BeforeAndAfterLine<Double>> content;

    public AnalyzeScrollDialog(Model model, Scroll scroll) {
        super(model, scroll.getSpell());
        this.scroll = scroll;
        this.content = analyzeCastChance(model, scroll.getSpell());
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        objs.add(new TextDecoration("Cast Chance for", xStart, ++yStart,  MyColors.WHITE, MyColors.BLUE, true));
        yStart+=2;
        objs.add(new DrawableObject(xStart, yStart++) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                scroll.drawYourself(model.getScreenHandler(), 38, y);
            }
        });
        yStart+=2;
        objs.add(new TextDecoration(scroll.getSpell().getName(), xStart, ++yStart,  MyColors.WHITE, MyColors.BLUE, true));
        yStart += 1;
        objs.add(new TextDecoration(scroll.getSpell().getSkill().getName(), xStart, ++yStart,  MyColors.WHITE, MyColors.BLUE, true));
        objs.add(new TextDecoration("Difficulty " + scroll.getSpell().getDifficulty(), xStart, ++yStart,  MyColors.WHITE, MyColors.BLUE, true));
        yStart += 2;
        objs.addAll(makeDrawableObjects(content, xStart, yStart));
        return objs;
    }

    @Override
    public List<DrawableObject> getAnalysisDrawableObjects(Model model, Item it, int xStart, int yStart) {
        return makeDrawableObjects(analyzeCastChance(model, ((Scroll)it).getSpell()), xStart, yStart);
    }
}

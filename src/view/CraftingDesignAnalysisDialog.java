package view;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.items.Item;
import model.items.designs.CraftingDesign;
import model.states.dailyaction.CraftItemState;
import util.BeforeAndAfterLine;
import view.party.DrawableObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CraftingDesignAnalysisDialog extends AnalyzeDialog {
    private final List<BeforeAndAfterLine<Double>> content;
    private final CraftingDesign design;

    public CraftingDesignAnalysisDialog(Model model, CraftingDesign design) {
        super(model, 12);
        this.design = design;
        this.content = analyzeCraftingSuccess(model, design.getCraftable());
    }

    private List<BeforeAndAfterLine<Double>> analyzeCraftingSuccess(Model model, Item craftable) {
        List<BeforeAndAfterLine<Double>> result = new ArrayList<>();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            result.add(new BeforeAndAfterLine<>(gc.getFirstName(), 0.0, calcCraftProbability(gc, craftable)));
        }
        return result;
    }

    private Double calcCraftProbability(GameCharacter gc, Item craftable) {
        int logicRank = gc.getRankForSkill(Skill.Logic);
        int laborRank = gc.getRankForSkill(Skill.Labor);
        int difficulty = CraftItemState.calculateDifficulty(craftable);
        return successProbability(logicRank, difficulty) * successProbability(laborRank, difficulty);
    }

    private double successProbability(int rank, int difficulty) {
        return (double)Math.min(9, Math.max(10 - difficulty + rank, 0)) / 10.0;
    }

    @Override
    public List<DrawableObject> getAnalysisDrawableObjects(Model model, Item it, int xStart, int yStart) {
        return makeDrawableObjects(content, xStart, yStart);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>(makeHeader(design, xStart, yStart));
        yStart += 9;
        objs.addAll(makeDrawableObjects(content, xStart, yStart));
        return objs;
    }

    private List<DrawableObject> makeDrawableObjects(List<BeforeAndAfterLine<Double>> content, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        for (BeforeAndAfterLine<Double> line : content) {
            int percentage = (int)Math.round(line.getAfter() * 100);
            String text = String.format("%-17s %3d%%", line.getLabel(), percentage);
            objs.add(new TextDecoration(text, xStart + 2, yStart,
                    MyColors.WHITE, MyColors.BLUE, false));
            yStart++;
        }
        return objs;
    }
}

package view.help;

import model.Model;
import view.GameView;
import view.party.DrawableObject;
import view.widget.TopText;

import java.util.List;

public class TutorialCarryingCapacity extends HelpDialog {
    private static final String TEXT = "Your party's carrying capacity " +
            "is how much your party members, and your horses can carry.\n\n" +
            "Each party member can carry an extra load (equipped items " +
            "do not contribute to this limit), dependent on their race. Human " +
            "and elves can carry 20 kilograms. Halflings can carry 10, Half-Orcs 25 and Dwarves 30. " +
            "Each horses you have can carry 50 kilograms extra.\n\n" +
            "If your party has a greater load than what you can carry when you want to travel, you will be forced " +
            "to abandon items or resources before being able to venture forth.\n\n" +
            "Your current load and carrying capacity can be seen in the top bar " +
            "by the icon that looks like this:\n\n.";

    public TutorialCarryingCapacity(GameView view) {
        super(view, "Carrying Capacity", TEXT);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = super.buildDecorations(model, xStart, yStart);
        textContent.add(new DrawableObject(xStart+16, yStart+29) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                model.getScreenHandler().put(x+1, y, TopText.WEIGHT_ICON_SPRITE);
            }
        });
        return textContent;
    }
}

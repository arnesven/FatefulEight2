package view.help;

import view.GameView;

public class TutorialHorses extends HelpDialog {
    private static final String TEXT =
            "With horses your party can travel more quickly. During travel, if you have enough horses for your " +
            "whole party, you will be able to travel two hexes instead of the normal one.\n\n" +
            "Horses can be bought and sold at inns and taverns. There are two types of horses, steeds and ponies. " +
            "Halflings and dwarves can only ride on steeds together with another non-halfling, non-dwarf character. " +
            "Only halflings and dwarves can ride on ponies.\n\n" +
            "When you are out in the world horses can find their own food while traveling, but at inns and taverns " +
            "your horses will need to stay at a stable, which costs 1 gold per horse.\n\n" +
            "You can see your current number of horses in the top bar at any time. The digit to the left is " +
            "the number of steeds and the digit to the right is the number of ponies you have. " +
            "You can have at most two more horses than the number of characters in your party.";

    public TutorialHorses(GameView view) {
        super(view, "Horses", TEXT);
    }
}

package view.help;

import view.GameView;

public class TutorialFeeding extends SubChapterHelpDialog {
    private static final String TEXT = "Vampires must feed on living humanoid beings to replenish their stamina. " +
            "Each night in a town or castle a vampire has three attempts to enter a house in town to find a victim. There are four steps to each attempt.\n\n" +
            "Stake Out: The vampire uses its senses to determine who lives in the house and if they are asleep.\n\n" +
            "Enter: The vampire must enter the house. Most people lock their doors at night, but sometimes they forget. " +
            "Sometimes it may be possible to climb in through a window instead.\n\n" +
            "Sneaking: If there are any people awake in the house, the vampire must avoid detection from them as it makes its way to the bed of the victim. " +
            "The more people are awake in the house, the harder the skill check will be. If the vampire is detected, the inhabitants will report the crime " +
            "and your party will gain notoriety.\n\n" +
            "Feeding: The vampire may feed on a sleeping person. Different races have different quality of blood, see Blood Types.";

    public TutorialFeeding(GameView view) {
        super(view, "Feeding", TEXT);
    }
}

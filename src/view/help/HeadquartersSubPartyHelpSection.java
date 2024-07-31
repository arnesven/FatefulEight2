package view.help;

import view.GameView;

public class HeadquartersSubPartyHelpSection extends SubChapterHelpDialog {
    private static final String TEXT =
            "Sub-parties are a group of characters which are staying at your headquarters. " +
            "They will periodically leave on adventuring trips, if there is enough food at " +
            "headquarters to bring and if all characters assigned to the sub-party are at " +
            "full Stamina. Trips have different length. The longer the trip the more likely " +
            "the sub-party will find good bounty (gold, items and experience). Likewise, longer trips are more dangerous and " +
            "may be harmful (even lethal) to characters.\n\n" +
            "TRIP LENGTH  DAYS  RISK/REWARD\n" +
            "Short           3          Low\n" +
            "Medium          5       Medium\n" +
            "Long            7         High\n\n" +
            "Going on adventuring trips exhausts a characters stamina equal to the days of the trip.\n\n" +
            "Characters who are away on adventuring trips in the sub-party can not be picked " +
            "up from headquarters or given another assignment until they return.\n\n" +
            "If a character is assigned to the sub-party while it is out adventuring, they " +
            "will await the sub-party's return before joining them.";

    public HeadquartersSubPartyHelpSection(GameView view) {
        super(view, "Sub-party", TEXT);
    }
}

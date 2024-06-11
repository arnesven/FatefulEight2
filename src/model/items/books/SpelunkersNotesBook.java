package model.items.books;

import model.items.Item;
import view.MyColors;

public class SpelunkersNotesBook extends BookItem {
    public SpelunkersNotesBook() {
        super("Spelunker's Notes", 0, MyColors.GRAY_RED,
                "Exploration Notes on the FatUE", "Francis Alesiay", makeContent());
    }

    private static String makeContent() {
        return "26th of Frostburn\n" +
            "While exploring the cave system we encountered a strange gate-like structure. " +
            "From the markings upon it we learned the name of this peculiar place; " +
            "'The Fortress at the Utmost Edge'. With some reluctance we entered. A narrow path " +
            "lead us to a very large cavern housing a huge walled fortress. We passed " +
            "through the portcullis into the main hall. There we were beset by some goblins which " +
            "had taken up residence there. We quickly chased them off and made camp." +
            "\n\n\n\n\n\n\n\n\n\n" +
            "27th of Frostburn\n" +
            "We have scouted the main part of the fortress today. It's easy to get lost here! " +
            "Apart from the keep, the fortress has an outer and an inner garden, two " +
            "wings and two towers. There's also a sprawling system of mine tunnels below the fortress. " +
            "Finally, there is a large stone door in the main hall but it has " +
            "no ordinary keyhole. All of us are completely stumped as how to get it open." +
            "\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
            "28th of Frostburn\n" +
            "We started exploring the mines today, but we quickly gave back. After catching our breath back at our " +
            "camp in the main hall we explored the outer garden thinking it would be more tranquil. How wrong we were." +
            "We were at once assailed by bats, rats, spiders and snakes!\n\n" +
            "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
            "29th of Frostburn\n" +
            "The group had a long discussion today. Some of us (me included) were in favor of leaving this " +
            "horrid place at once. Others (including our leader, Mr. Delusions of Grandeur) were insistent on " +
            " continuing. There was a bit of a row. Afterward three of them went off to explore the west wing. " +
            "Only one of them came back, badly beaten, and delirious. He kept going on and on about giant rats " +
            "and skeletons." +
            "\n\n\n\n\n\n\n\n\n\n\n\n\n" +
            "30th of Frostburn\n" +
            "We were preparing to leave for the surface when suddenly we were attacked by terrifying spectres! " +
            "I was the only one who could escape alive. With no provisions, and no equipment, I'm surely done for. " +
            "At least I still have my wits." +
            "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
            "1st of Meltwater\n" +
            "Wandered around in the caves... found this spot where some light is coming in from the surface " +
            "through a tiny crack. I wish I could turn into a butterfly and fly away." +
            "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
            "2nd of Meltwater\n" +
            "Found an exit. Now very weak. Hungry" +
            "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" +
            "4th of Meltwater... I think\n" +
            "Lost... found mushrooms, ate them. Tummy hurts.";
    }

    @Override
    public Item copy() {
        return new SpelunkersNotesBook();
    }
}

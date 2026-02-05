package model.mainstory;

import model.Model;
import model.characters.GameCharacter;
import model.characters.preset.KruskTalandro;
import model.classes.Classes;
import model.items.special.CommunicatorDevice;
import model.items.special.MagicMirror;
import model.journal.PartSevenStoryPart;
import model.journal.PartSixStoryPart;
import model.journal.ZeppelinStoryPart;
import model.quests.MindMachineQuest;
import model.races.Race;
import util.MyStrings;

import java.awt.*;

public enum MainStoryStep {
    NOT_STARTED((model, mainStory) -> {
    }),

    STARTED((model, mainStory) -> {
        GameCharacter dummy = new GameCharacter("Dummy", "Delacroix", Race.HALF_ORC, Classes.WIT,
                new KruskTalandro(), Classes.NO_OTHER_CLASSES);
        mainStory.setupStory(dummy); // Get task "visit uncle"
    }),
    UNCLE_VISITED((model, mainStory) -> {
        mainStory.getStoryParts().get(0).progress();     // Visit uncle, get Frogmen Problem quest
    }),
    FROGMEN_QUEST_DONE((model, mainStory) -> {
        mainStory.getStoryParts().get(0).progress();     // Completes frogmen problem quest
    }),
    RETURNED_TO_UNCLE((model, mainStory) -> {
        mainStory.getStoryParts().get(0).progress();     // Returns to uncle, get visit Everix task
        mainStory.getStoryParts().get(0).transitionStep(model, 0); // Gets "Reward at ... Castle" Task
    }),
    EVERIX_VISITED((model, mainStory) -> {
        mainStory.getStoryParts().get(0).progress();     // Visits Everix
        mainStory.getStoryParts().get(0).transitionStep(model, 1); // Gets "Find Witch" Task
    }),
    LORD_VISITED((model, mainStory) -> {
        mainStory.getStoryParts().get(1).progress();  // Visit lord
    }),
    RESCUE_QUEST_DONE((model, mainStory) -> {
        mainStory.getStoryParts().get(1).progress();  // Do rescue mission quest
    }),
    RETURNED_TO_LORD_1((model, mainStory) -> {
        mainStory.getStoryParts().get(1).progress();  // Returns to lord, task completed
        mainStory.getStoryParts().get(1).progress();
    }),
    WITCH_VISITED((model, mainStory) -> {
        mainStory.getStoryParts().get(2).progress();   // Visits witch, get Special Delivery Quest
    }),
    WITCH_QUEST_DONE((model, mainStory) -> {
        mainStory.getStoryParts().get(2).progress();   // Completes special delivery quests
    }),
    RETURNED_TO_WITCH((model, mainStory) -> {
        mainStory.getStoryParts().get(2).progress();   // Returns to witch, get Crimson Pearl info -> completes task
        mainStory.getStoryParts().get(2).transitionStep(model); // Gets part three story part
    }),
    RETURNED_TO_LORD_2((model, mainStory) -> {
        mainStory.getStoryParts().get(3).progress();  // Progressed at lord because witch part is done
    }),
    WILLIS_TALKED_TO((model, mainStory) -> {
        mainStory.getStoryParts().get(3).progress();  // Talks to Willis, gets trouble in the library quest
    }),
    LIBRARY_QUEST_DONE((model, mainStory) -> {
        mainStory.getStoryParts().get(3).progress();  // Completes Trouble in the Library Quest,
    }),
    RETURNED_TO_WILLIS((model, mainStory) -> {
        mainStory.getStoryParts().get(3).progress();  // Returns to willis, cleans library and gets more info on quad.
        mainStory.getStoryParts().get(3).transitionStep(model);
        model.setWorldState(model.getMainStory().getExpandDirection());
    }),
    RETURNED_TO_LORD_3((model, mainStory) -> {
        mainStory.getStoryParts().get(4).progress(); // Get Go to Orc War Camp task
    }),
    ORC_QUEST_DONE((model, mainStory) -> {
        mainStory.getStoryParts().get(4).progress();
    }),
    RETURNED_TO_LORD_4((model, mainStory) -> {
        mainStory.getStoryParts().get(4).progress(); // Get stuff from lord
        mainStory.addStoryPart(new ZeppelinStoryPart(model.getMainStory().getXelbiPosition(), "FJANT"));
        mainStory.getStoryParts().get(4).transitionStep(model);
        model.getParty().earnGold(300);
    }),
    XELBI_MET((model, mainStory) -> {
        mainStory.getStoryParts().get(5).progress();  // Meet Xelbi and find out about zeppelin
        new CommunicatorDevice(true).addYourself(model.getParty().getInventory());
    }),
    ZEPPELIN_BOUGHT((model, mainStory) -> {
        mainStory.getStoryParts().get(5).progress();  // Zeppelin bought
    }),
    A_S_QUEST_DONE((model, mainStory) -> {
        mainStory.getStoryParts().get(6).progress(); // Ancient Stronghold Quest done
    }),
    RETURNED_TO_LORD_5((model, mainStory) -> {
        mainStory.getStoryParts().get(6).progress();     // Returned to lord, get thrown in the dungeon
        mainStory.getStoryParts().get(6).transitionStep(model);
    }),
    ESCAPE_QUEST_DONE((model, mainStory) -> {
       mainStory.getStoryParts().get(7).progress();      // Escape from castle dungeon quest done
    }),
    EVERIX_TALKED_TO((model, mainStory) -> {
       mainStory.getStoryParts().get(7).progress();       // Wake Everix up and talk to her.
    }),
    WITCH_MET_AGAIN((model, mainStory) -> {
        mainStory.getStoryParts().get(7).progress();       // Meet witch and get kingdom and remote people tasks.
    }),
    THREE_SUPPORT_GOT((model, mainStory) -> {
        PartSixStoryPart partSix = (PartSixStoryPart) mainStory.getStoryParts().get(7);
        partSix.setSupportTasksCompleted();
    }),
    AT_RENDEZVOUS((model, mainStory) -> {
        PartSixStoryPart partSix = (PartSixStoryPart) mainStory.getStoryParts().get(7);
        partSix.setCompleted(true);
        partSix.transitionStep(model);
    }),
    INTO_THE_PAST((model, mainStory) -> {
        PartSevenStoryPart partSeven = (PartSevenStoryPart) mainStory.getStoryParts().get(8);
        partSeven.progress();
        new MagicMirror().addYourself(model.getParty().getInventory());
    });

    private final MainStoryProgressor progressor;

    MainStoryStep(MainStoryProgressor prog) {
        progressor = prog;
    }

    public String makeNiceString() {
        StringBuilder bldr = new StringBuilder(MyStrings.capitalize(name().replace('_', ' ')));
        while (bldr.length() < 18) {
            bldr.append(' ');
        }
        return bldr.toString();
    }

    public void progress(Model model, MainStory mainStory) {
        progressor.progress(model, mainStory);
    }
}

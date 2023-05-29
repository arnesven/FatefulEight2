package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.states.DailyEventState;
import model.states.RecruitState;

import java.util.List;

public class WhiteKnightEvent extends DailyEventState {
    public WhiteKnightEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.PAL, "Paladin");
        println("You meet a white knight, a paladin.");
        portraitSay("We are the Paladins, the protectors of our sacred " +
                "temple!");
        println("The paladin seems almost comically serious as he " +
                "says this with a solemn face. The party members dare not " +
                "make fun of him however. With a little encouragement " +
                "the Paladin tells all about the order and his martial skills " +
                "and duties");
        GameCharacter pala = null;
        for (GameCharacter gc : model.getAllCharacters()) {
            if (gc.getCharClass().id() == Classes.PAL.id()) {
                pala = gc;
            }
        }
        if (pala != null) {
            pala.setLevel(2);
            println("The paladin offers to join your party.");
            RecruitState recruit = new RecruitState(model, List.of(pala));
            recruit.run(model);
        }
        println("The paladin offers to instruct you in the way of his order, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.PAL);
        change.areYouInterested(model);
    }
}

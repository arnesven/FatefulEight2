package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.states.DailyEventState;
import sound.BackgroundMusic;
import sound.ClientSound;
import sound.ClientSoundManager;
import view.subviews.CollapsingTransition;
import view.subviews.HorseRacingSubView;

import java.util.ArrayList;
import java.util.List;

public class HorseRacingEvent extends DailyEventState {
    private final Horse horse;
    private final HorseRacingSubView subView;
    private boolean didWin;
    private int targetlaps = 1;

    public HorseRacingEvent(Model model, Horse horse) {
        super(model);
        this.horse = horse;
        subView = new HorseRacingSubView(model.getParty().getPartyMember(0), horse);
    }

    @Override
    protected void doEvent(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.citySong);
        CollapsingTransition.transition(model, subView);
        print("Welcome to the horse race. Try to come in first place after two laps. Press enter to start!");
        waitForReturn();
        subView.startRace();
        while (!subView.raceIsOver(targetlaps)) {
            sleep();
        }

        int place = subView.getPlayerPlacement();
        subView.stopRace();
        print("The race is over! ");
        if (place == 1) {
            println("Congratulations, you came in 1st place!");
            didWin = true;
        } else {
            println("You finished with position " + place + ".");
            didWin = false;
        }
        print("Press enter to continue.");
        waitForReturn();
    }

    public void addNPC(GameCharacter gameCharacter) {
        subView.addNPC(gameCharacter, HorseHandler.generateHorse());
    }

    public boolean didWin() {
        return didWin;
    }

    public void setLaps(int i) {
        targetlaps = i;
    }
}

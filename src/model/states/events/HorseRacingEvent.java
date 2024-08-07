package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.states.DailyEventState;
import model.states.horserace.HorseRacer;
import sound.BackgroundMusic;
import sound.ClientSound;
import sound.ClientSoundManager;
import util.MyStrings;
import view.subviews.CollapsingTransition;
import view.subviews.HorseRacingSubView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HorseRacingEvent extends DailyEventState {
    private final Horse horse;
    private final HorseRacingSubView subView;
    private boolean didWin;
    private int targetlaps = 1;
    private boolean npcsAdded = false;
    private int track;

    public HorseRacingEvent(Model model, GameCharacter racer, Horse horse) {
        super(model);
        this.horse = horse;
        subView = new HorseRacingSubView(racer, horse);
    }

    @Override
    protected void doEvent(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.horseRacingSong);
        CollapsingTransition.transition(model, subView);
        print("Welcome to the horse race. Try to come in first place after " + MyStrings.numberWord(targetlaps) +
                " laps. Press enter to start!");
        model.getTutorial().horseRacing(model);
        waitForReturn();
        subView.startRace();
        while (!subView.raceIsOver(targetlaps)) {
            sleep();
        }

        int place = subView.getPlayerPlacement();
        subView.stopRace();
        print("The race is over! ");
        if (npcsAdded) {
            if (place == 1) {
                println("Congratulations, you came in 1st place!");
                didWin = true;
            } else {
                println("You finished with position " + place + ".");
                didWin = false;
            }
        }
        print("Press enter to continue.");
        waitForReturn();
        ClientSoundManager.playPreviousBackgroundMusic();
    }

    public void addNPC(GameCharacter gameCharacter, Horse horse) {
        npcsAdded = true;
        subView.addNPC(gameCharacter, horse);
    }

    public void addNPC(GameCharacter gameCharacter) {
        addNPC(gameCharacter, HorseHandler.generateHorse());
    }

    public boolean didWin() {
        return didWin;
    }

    public void setLaps(int i) {
        targetlaps = i;
    }

    public void setTimeModeEnabled(boolean b) {
        subView.setTimeModeEnabled(b);
    }

    public int getTimeResultSeconds() {
        return subView.getTimeResult();
    }

    public void setTrack(int track) {
        subView.setTrack(track);
    }

    public List<HorseRacer> getPlacements() {
        return subView.getPlacements();
    }
}

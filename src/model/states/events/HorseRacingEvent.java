package model.states.events;

import model.Model;
import model.horses.Horse;
import model.horses.HorseHandler;
import model.states.DailyEventState;
import sound.BackgroundMusic;
import sound.ClientSound;
import sound.ClientSoundManager;
import view.subviews.HorseRacingSubView;

public class HorseRacingEvent extends DailyEventState {
    public HorseRacingEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        Horse horse = HorseHandler.generateHorse();
        if (model.getParty().getHorseHandler().size() > 0) {
            horse = model.getParty().getHorseHandler().get(0);
        }
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.citySong);
        HorseRacingSubView subView = new HorseRacingSubView(model.getParty().getPartyMember(0), horse);
        model.setSubView(subView);
        subView.addNPC(model.getAllCharacters().get(0), HorseHandler.generateHorse());
        subView.addNPC(model.getAllCharacters().get(1), HorseHandler.generateHorse());
        subView.addNPC(model.getAllCharacters().get(2), HorseHandler.generateHorse());
        subView.addNPC(model.getAllCharacters().get(3), HorseHandler.generateHorse());
        print("Welcome to the horse race. Press enter to start!");
        waitForReturn();
        subView.startRace();
        while (!subView.raceIsOver()) {
            sleep();
        }

    }
}

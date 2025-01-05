package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.CombatEvent;
import model.states.duel.MagicDuelEvent;
import model.states.duel.MagicDuelist;
import model.states.duel.MatrixDuelistController;
import model.states.duel.PlayerDuelistController;
import model.states.duel.gauges.*;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyLists;
import util.MyRandom;
import view.MyColors;
import view.subviews.MagicDuelSubView;
import view.subviews.SetupMagicDuelSubView;
import view.subviews.StripedTransition;

import java.util.ArrayList;
import java.util.List;

public class NPCOnlyMagicDuelEvent extends MagicDuelEvent {

    public NPCOnlyMagicDuelEvent(Model model, GameCharacter duelistA, PowerGauge gaugeA, GameCharacter duelistB, PowerGauge gaugeB) {
        super(model);
        this.duelists = new ArrayList<>();
        duelists.add(new MagicDuelist(duelistA, findBestMagicColor(duelistA), gaugeA.copy(), true));
        duelists.add(new MagicDuelist(duelistB, findBestMagicColor(duelistB), gaugeB.copy(), true));
    }

    @Override
    protected void doEvent(Model model) {
        BackgroundMusic previousMusic = ClientSoundManager.getCurrentBackgroundMusic();
        CombatEvent.startMusic();

        this.controller1 = new MatrixDuelistController(duelists.get(0),
                duelists.get(0).getGauge().getAIMatrices(duelists.get(0).getCharacter()));
        this.controller2 = new MatrixDuelistController(duelists.get(1),
                duelists.get(1).getGauge().getAIMatrices(duelists.get(1).getCharacter()));

        this.subView = new MagicDuelSubView(model.getCurrentHex().getCombatTheme(),
                duelists.get(0), duelists.get(1));
        StripedTransition.transition(model, subView);

        runDuel(model);
        MagicDuelist winner = MyLists.find(duelists, d -> !d.isKnockedOut());
        println("The duel is over, the winner is " + winner.getName() + "!");
        print("Press enter to continue.");
        waitForReturn();
        ClientSoundManager.playBackgroundMusic(previousMusic);
    }
}

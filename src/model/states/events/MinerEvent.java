package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.enemies.CompanionEnemy;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Pickaxe;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.util.ArrayList;
import java.util.List;

public class MinerEvent extends DarkDeedsEvent {
    private boolean withIntro;

    public MinerEvent(Model model, boolean withIntro) {
        super(model);
        this.withIntro = withIntro;
    }

    public MinerEvent(Model model) {
        this(model, true);
    }

    @Override
    protected void doEvent(Model model) {
        CharacterAppearance app = PortraitSubView.makeRandomPortrait(Classes.MIN);
        showExplicitPortrait(model, app, "Miner");
        if (withIntro) {
            print("The party encounters a miner. ");
        }
        if (darkDeedsMenu("miner", makeMinerCharacter(app), MyRandom.randInt(10, 40), makeCompanions(),
                ProvokedStrategy.FIGHT_TO_DEATH)) {
            return;
        }
        print("The miner has all the gear needed to descend deep into " +
                "the earth, to dig for precious gems and metal ore. The " +
                "miner gladly demonstrates the gear and offers to teach you about the life of a miner, ");
        ChangeClassEvent changeClassEvent = new ChangeClassEvent(model, Classes.MIN);
        changeClassEvent.areYouInterested(model);
    }

    private List<Enemy> makeCompanions() {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = MyRandom.randInt(6); i > 0; --i) {
            enemies.add(new CompanionEnemy(PortraitSubView.makeRandomPortrait(Classes.MIN),
                    Classes.MIN, new Pickaxe()));
        }
        return enemies;
    }

    private GameCharacter makeMinerCharacter(CharacterAppearance app) {
        GameCharacter gc = new GameCharacter("Miner", "", app.getRace(), Classes.MIN, app,
                Classes.NO_OTHER_CLASSES,
                new Equipment(new Pickaxe(), new LeatherArmor(), new SkullCap()));
        gc.setLevel(MyRandom.randInt(1, 4));
        return gc;
    }
}

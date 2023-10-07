package model.states.events;

import model.Model;
import model.classes.Skill;
import model.combat.CombatLoot;
import model.combat.StandardCombatLoot;
import model.states.DailyEventState;
import view.sprites.MiniPictureSprite;
import view.subviews.MiniPictureSubView;

public class CairnEvent extends DailyEventState {
    private static final MiniPictureSprite SPRITE = new MiniPictureSprite(0x20);

    public CairnEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new MiniPictureSubView(model.getSubView(), SPRITE, "Cairn"));
        println("You pass a unusually large pillar of stacked stones. It's a cairn. " +
                "You inspect it more closely and realize that there may be something hidden inside it.");
        boolean success = model.getParty().doSoloSkillCheck(model, this, Skill.Endurance, 7);
        if (success) {
            CombatLoot loot = new StandardCombatLoot(model, 4);
            String lootText = loot.getText();
            if (loot.equals("")) {
                println("You brake the cairn but there was nothing of value inside it. Just some old bones and pottery shards.");
            } else {
                println("You brake the cairn and find " + lootText.replaceAll("\n", ","));
            }
            loot.giveYourself(model.getParty());
        }
    }
}

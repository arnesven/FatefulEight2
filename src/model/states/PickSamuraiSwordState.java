package model.states;

import model.Model;
import model.states.swords.LargeSamuraiSword;
import model.states.swords.MediumSamuraiSword;
import model.states.swords.SamuraiSword;
import model.states.swords.SmallSamuraiSword;
import util.MyRandom;
import view.MyColors;
import view.subviews.CollapsingTransition;
import view.subviews.PickSamuraiSwordSubView;

import java.util.ArrayList;
import java.util.List;

public class PickSamuraiSwordState extends DailyEventState {
    // TODO: Use this
    private static final List<MyColors> SWORD_COLORS =
            List.of(MyColors.BLUE, MyColors.DARK_PURPLE, MyColors.DARK_GREEN, MyColors.DARK_RED, MyColors.DARK_GRAY);

    public PickSamuraiSwordState(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        List<SamuraiSword> swords = makeSwords();
        PickSamuraiSwordSubView subView = new PickSamuraiSwordSubView(swords);
        CollapsingTransition.transition(model, subView);
        waitForReturn();
    }

    private List<SamuraiSword> makeSwords() {
        List<SamuraiSword> result = new ArrayList<>();
        for (int i = 0; i < 16; ++i) {
            result.add(makeRandomSword());
        }
        return result;
    }

    private SamuraiSword makeRandomSword() {
        MyColors color = MyRandom.sample(SWORD_COLORS);
        boolean inscription = MyRandom.flipCoin();
        return MyRandom.sample(List.of(
                new SmallSamuraiSword(color, inscription),
                new MediumSamuraiSword(color, inscription),
                new LargeSamuraiSword(color, inscription)));
    }
}

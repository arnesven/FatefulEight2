package model.actions;

import model.Model;
import model.map.UrbanLocation;
import model.map.WorldHex;
import model.states.DailyEventState;
import model.states.events.BrotherhoodCroniesEvent;
import util.MyPair;
import util.MyRandom;

public class Loan extends MyPair<Integer, Integer> {
    public static final int REPAY_WITHIN_DAYS = 10;

    public Loan(Integer amount, Integer day) {
        super(amount, day);
    }

    public static DailyEventState eventDependentOnLoan(Model model, WorldHex worldHex) {
        if (model.getParty().getLoan() != null) {
            if (model.getParty().getLoan().getDay() + Loan.REPAY_WITHIN_DAYS < model.getDay()) {
                int target = 8;
                if (worldHex.getLocation() != null && worldHex.getLocation() instanceof UrbanLocation) {
                    target -= 2;
                }
                if (model.getParty().isOnRoad()) {
                    target -= 1;
                }
                if (model.getParty().getLoan().getAmount() > 50) {
                    target -= 1;
                }
                if (model.getParty().getLoan().getDay() + Loan.REPAY_WITHIN_DAYS * 2 < model.getDay()) {
                    target -= 2;
                }
                if (MyRandom.rollD10() > target) {
                    return new BrotherhoodCroniesEvent(model);
                }
            }
        }
        return null;
    }

    public int getAmount() {
        return first;
    }

    public int getDay() {
        return second;
    }

    public int repayCost() {
        return (int)(getAmount() * 1.5);
    }
}

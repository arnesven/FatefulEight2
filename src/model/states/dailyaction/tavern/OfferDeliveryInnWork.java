package model.states.dailyaction.tavern;

import model.Model;
import model.states.AcceptDeliveryEvent;

public class OfferDeliveryInnWork extends InnWorkAction {
    public OfferDeliveryInnWork() {
        super("Delivery", "I have something I need delivered, if you're interested in taking a little trip.");
    }

    @Override
    public void doWork(Model model, TalkToBartenderState state) {
        AcceptDeliveryEvent deliveryEvent = new AcceptDeliveryEvent(model, "Bartender") {
            @Override
            protected void senderSpeak(String text) {
                state.bartenderSay(model, text);
            }
        };
        deliveryEvent.offerDeliveryTask(model);
    }
}

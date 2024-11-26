package view.help;

import view.GameView;

public class TutorialDeliveries extends HelpDialog {
    private static final String TEXT =
            "Sometimes people ask you to deliver something for them. If you accept " +
            "you will be given a parcel to transport to a particular location. Once " +
            "you are there you can make the delivery and get paid.\n\n" +
            "If you open the parcel before delivering it, you will receive the contents " +
            "of that parcel, but you also forfeit the opportunity to deliver it. Doing so " +
            "counts as theft and therefore raises your Notoriety.";

    public TutorialDeliveries(GameView view) {
        super(view, "Deliveries", TEXT);
    }
}

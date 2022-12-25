package view.subviews;

public class OnTheRoadSubView extends ImageSubView {

    public static final String TITLE_TEXT = "ON THE ROAD";
    public static SubView instance = new OnTheRoadSubView();

    private OnTheRoadSubView() {
        super("ontheroad", TITLE_TEXT, "You are traveling along a road.", true);
    }
}

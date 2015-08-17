package zeitfaden.com.zeitfaden;

/**
 * Created by tobias on 17.08.15.
 */
public class SpinnerIntegerItem {
    private int mValue;
    private String mCaption;

    public SpinnerIntegerItem(int value, String caption){
        mValue = value;
        mCaption = caption;
    }

    public Integer getValue(){
        return mValue;
    }

    public String toString(){
        return mCaption;
    }
}

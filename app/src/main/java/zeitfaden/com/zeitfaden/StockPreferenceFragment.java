package zeitfaden.com.zeitfaden;

import android.os.Bundle;
import android.preference.PreferenceFragment;


/**
 * Created by tobias on 17.08.15.
 */
public class StockPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int res=getActivity()
                .getResources()
                .getIdentifier(getArguments().getString("resource"),
                        "xml",
                        getActivity().getPackageName());

        addPreferencesFromResource(res);
    }
}

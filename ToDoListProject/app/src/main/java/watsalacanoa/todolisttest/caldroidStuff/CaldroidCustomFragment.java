package watsalacanoa.todolisttest.caldroidStuff;

/**
 * Created by spide on 17/3/2017.
 */
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.HashMap;

import hirondelle.date4j.DateTime;

public class CaldroidCustomFragment extends CaldroidFragment {
    protected HashMap<DateTime, DailyEvent> events = new HashMap<DateTime, DailyEvent>();
    CaldroidAdapter cAdapter;
    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new CaldroidAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }
    
    public void refreshView() {
        // If month and year is not yet initialized, refreshView doesn't do
        // anything
        if (month == -1 || year == -1) {
            return;
        }

        cAdapter = new CaldroidAdapter(getActivity(), getMonth(), getYear(), getCaldroidData(), getExtraData());

        refreshMonthTitleTextView();

        // Refresh the date grid views
        for (CaldroidGridAdapter adapter : this.datePagerAdapters) {
            // Reset caldroid data
            adapter.setCaldroidData(getCaldroidData());
            //cAdapter.setCaldroidData(getCaldroidData());

            // Reset extra data
            adapter.setExtraData(extraData);
            //cAdapter.setExtraData(extraData);

            // reset events

            // Refresh view
            //cAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
        cAdapter.setEvents(events);
    }
}

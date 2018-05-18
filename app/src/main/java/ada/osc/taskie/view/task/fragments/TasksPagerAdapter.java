package ada.osc.taskie.view.task.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avukelic on 18-May-18.
 */
public class TasksPagerAdapter  extends FragmentPagerAdapter {

    private FragmentRefreshListener fragmentRefreshListener;

    public TasksPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AllTasksFragment();
        } else {
            return new FavoriteTasksFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

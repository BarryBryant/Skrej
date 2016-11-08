package com.willowtreeapps.skrej;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by barrybryant on 11/7/16.
 */

public interface ConferencePresenter {
    void bindView(ConferenceView view);
    void unbindView();
    void onActivityResult(int request, int response, Intent data);
    void onClickSchedule();
    void initializeLoader(Activity activity);
    void loadCalendar();
}

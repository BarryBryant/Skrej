package com.willowtreeapps.skrej;

/**
 * Created by barrybryant on 11/7/16.
 */

public interface ConferencePresenter {
    void bindView(ConferenceView view);
    void unbindView();
    void onActivityResult(int request, int response, String name);
    void onClickSchedule();
    void loadCalendar();
}

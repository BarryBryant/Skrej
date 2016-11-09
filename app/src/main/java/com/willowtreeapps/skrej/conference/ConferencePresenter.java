package com.willowtreeapps.skrej.conference;

/**
 * Created by barrybryant on 11/7/16.
 */

public interface ConferencePresenter {
    void bindView(ConferenceView view);
    void unbindView();
    void onClickSchedule();
    void loadCalendar();
}

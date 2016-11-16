package com.willowtreeapps.skrej.conference;

import com.google.api.services.calendar.model.Event;

import java.util.List;

/**
 * Created by barrybryant on 11/7/16.
 */

public interface ConferencePresenter {

    void bindView(ConferenceView view);

    void unbindView();

    void onEventsLoaded(List<Event> events);

    void onClickSchedule();


}

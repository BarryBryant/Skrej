package com.willowtreeapps.skrej.conference;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.willowtreeapps.skrej.calendarapi.CalendarWizard;

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

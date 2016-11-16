package com.willowtreeapps.skrej.conference;

import com.google.api.services.calendar.model.Event;
import com.willowtreeapps.skrej.calendarapi.CalendarWizard;
import com.willowtreeapps.skrej.model.RoomAvailabilityStatus;

import java.text.DateFormat;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by barrybryant on 11/7/16.
 */

public class ConferencePresenterImpl implements ConferencePresenter {

    //Log tag.
    private static final String TAG = ConferencePresenterImpl.class.getSimpleName();
    //The calendar wizard.
    @Inject
    CalendarWizard calendarWizard;
    //Our view.
    private ConferenceView view;
    private List<Event> events;

    public ConferencePresenterImpl(CalendarWizard wizard) {
        this.calendarWizard = wizard;
    }

    //region ConferencePresenter interface:

    @Override
    public void bindView(ConferenceView view) {
        this.view = view;
        String date = getFormattedDate();
        view.updateDate(date);
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    @Override
    public void onEventsLoaded(List<Event> events) {
        RoomAvailabilityStatus myRoomStat;
        this.events = events;
        //Pass event data down to our wizard.
        myRoomStat = calendarWizard.parseFirstEvent(events);

        //Update our view with wizard data.
        if (view != null) {
            view.updateAvailability(myRoomStat.getRoomAvailability());
            view.updateAvailabilityTimeInfo(myRoomStat.getRoomAvailabilityTimeInfo());
        }
    }

    @Override
    public void onClickSchedule() {
        //Show spinner in view.
        view.showSpinner();
        RoomAvailabilityStatus roomStatus = calendarWizard.parseFirstEvent(events);
        view.createEventPrompt(roomStatus);
    }

    //endregion

    private String getFormattedDate() {
        long currentTime = System.currentTimeMillis();
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG);
        return formatter.format(currentTime);
    }

}

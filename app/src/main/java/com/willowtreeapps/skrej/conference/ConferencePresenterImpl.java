package com.willowtreeapps.skrej.conference;

import com.google.api.services.calendar.model.Event;
import com.willowtreeapps.skrej.calendarApi.CalendarWizard;
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
    private int chosenNumOfBlocks;

    public ConferencePresenterImpl(CalendarWizard wizard) {
        this.calendarWizard = wizard;
    }

    //region ConferencePresenter interface:

    @Override
    public void bindView(ConferenceView view) {
        this.view = view;
        String date = getFormattedDate();
        view.updateDate(date);
        if (events == null) {
            view.disableScheduleButton();
        }
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
            view.hideLoading();
            view.enableScheduleButton();
            view.updateAvailability(myRoomStat.getRoomAvailability());
            view.updateAvailabilityTimeInfo(myRoomStat.getRoomAvailabilityTimeInfo());
        }
    }

    @Override
    public void onClickSchedule() {
        //Show spinner in view.
        view.showLoading();
        RoomAvailabilityStatus roomStatus = calendarWizard.parseFirstEvent(events);
        view.showEventDurationPrompt(roomStatus);
    }

    @Override
    public void onNumOfBlocksChosen(int chosenNumOfBlocks) {
        this.chosenNumOfBlocks = chosenNumOfBlocks;
        view.showEventAttendeesPrompt();
    }

    @Override
    public void onAttendeesSelected(List<String> attendees) {
        view.createEvent(chosenNumOfBlocks, attendees);
    }

    //endregion

    private String getFormattedDate() {
        long currentTime = System.currentTimeMillis();
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG);
        return formatter.format(currentTime);
    }

}

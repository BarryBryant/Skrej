package com.willowtreeapps.skrej.conference;

import android.content.Intent;
import android.util.Log;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.willowtreeapps.skrej.calendarapi.CalendarLoader;
import com.willowtreeapps.skrej.calendarapi.CalendarWizard;
import com.willowtreeapps.skrej.calendarapi.RoomAvailabilityStatus;
import com.willowtreeapps.skrej.util.EventTimeUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by barrybryant on 11/7/16.
 */

public class ConferencePresenterImpl

    implements
    ConferencePresenter
{

    //Log tag.
    private static final String TAG = ConferencePresenterImpl.class.getSimpleName();

    //Our view.
    private ConferenceView view;

    //The calendar wizard.
    @Inject
    CalendarWizard calendarWizard;

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

        //Pass event data down to our wizard.
        myRoomStat = calendarWizard.parseEventData(events);

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
    }

    //endregion

    private String getFormattedDate() {
        long currentTime = System.currentTimeMillis();
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG);
        return formatter.format(currentTime);
    }

}

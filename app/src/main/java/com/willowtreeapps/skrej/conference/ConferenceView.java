package com.willowtreeapps.skrej.conference;

import com.willowtreeapps.skrej.model.RoomAvailabilityStatus;

import java.util.List;

/**
 * Created by barrybryant on 11/7/16.
 */

public interface ConferenceView {
    void showLoading();

    void hideLoading();

    void updateAvailability(String availability);

    void updateAvailabilityTimeInfo(String availabilityTimeInfo);

    void updateDate(String date);

    void enableScheduleButton();

    void disableScheduleButton();

    void showEventDurationPrompt(RoomAvailabilityStatus roomStatus);

    void showEventAttendeesPrompt();

    void createEvent(int chosenNumOfBlocks, List<String> attendees);

}

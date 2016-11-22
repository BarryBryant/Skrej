package com.willowtreeapps.skrej.conference;

import com.willowtreeapps.skrej.model.RoomAvailabilityStatus;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by barrybryant on 11/7/16.
 */

public class ConferencePresenterImpl implements ConferencePresenter, ConferenceRepositoryImpl.ConferenceRepositoryListener {

    private final ConferenceRepository conferenceRepository;
    private ConferenceView view;
    private RoomAvailabilityStatus roomAvailabilityStatus;
    private int chosenNumOfBlocks;
    private String roomId;

    public ConferencePresenterImpl(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
        this.conferenceRepository.bindListener(this);
    }

    //region ConferencePresenter interface:

    @Override
    public void bindView(ConferenceView view) {
        this.view = view;
        String date = getFormattedDate();
        view.updateDate(date);
        if (roomAvailabilityStatus == null && roomId != null) {
            view.disableScheduleButton();
            conferenceRepository.getEvents(roomId);
        } else if (roomId == null) {
            throw new Error("ROOM ID DON'T EXIST ?!");
        } else {
            onRoomStatusLoaded(roomAvailabilityStatus);
        }
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    @Override
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public void onClickSchedule() {
        //Show spinner in view.
        if (view != null) {
            view.showEventDurationPrompt(roomAvailabilityStatus);
        }
    }

    @Override
    public void onNumOfBlocksChosen(int chosenNumOfBlocks) {
        this.chosenNumOfBlocks = chosenNumOfBlocks;
        if (view != null) {
            view.showEventAttendeesPrompt();
        }
    }

    @Override
    public void onAttendeesSelected(List<String> attendees) {
        if (view != null) {
            view.createEvent(chosenNumOfBlocks, attendees);
        }
    }

    //endregion

    private String getFormattedDate() {
        long currentTime = System.currentTimeMillis();
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG);
        return formatter.format(currentTime);
    }

    @Override
    public void onRoomStatusLoaded(RoomAvailabilityStatus roomAvailabilityStatus) {
        if (view != null) {
            view.hideLoading();
            if (roomAvailabilityStatus.getAvailableBlocks() < 1) {
                view.disableScheduleButton();
            } else view.enableScheduleButton();
            view.updateAvailabilityTimeInfo(roomAvailabilityStatus.getRoomAvailabilityTimeInfo());
            view.updateAvailability(roomAvailabilityStatus.getRoomAvailability());
        }
        this.roomAvailabilityStatus = roomAvailabilityStatus;
    }

    @Override
    public void onError(Throwable error) {

    }
}

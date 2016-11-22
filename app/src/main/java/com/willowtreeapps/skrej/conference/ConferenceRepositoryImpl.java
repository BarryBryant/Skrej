package com.willowtreeapps.skrej.conference;

import com.willowtreeapps.skrej.calendarApi.CalendarEventService;
import com.willowtreeapps.skrej.model.RoomAvailabilityStatus;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by barrybryant on 11/21/16.
 */

public class ConferenceRepositoryImpl implements ConferenceRepository {

    private final CalendarEventService calendarEventService;
    private ConferenceRepositoryListener listener;

    public ConferenceRepositoryImpl(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @Override
    public void getEvents(String roomId) {
        calendarEventService.getRoomAvailability(roomId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        roomAvailabilityStatus -> onRoomStatusLoaded(roomAvailabilityStatus),
                        error -> onNetworkError(error)
                );
    }

    @Override
    public void bindListener(ConferenceRepositoryImpl.ConferenceRepositoryListener listener) {
        this.listener = listener;
    }

    @Override
    public void unbindListener() {
        this.listener = null;
    }

    private void onRoomStatusLoaded(RoomAvailabilityStatus roomAvailabilityStatus) {
        if (listener != null) {
            listener.onRoomStatusLoaded(roomAvailabilityStatus);
        }
    }

    private void onNetworkError(Throwable error) {
        if (listener != null) {
            listener.onError(error);
        }
    }

    public interface ConferenceRepositoryListener {

        void onRoomStatusLoaded(RoomAvailabilityStatus roomAvailabilityStatus);

        void onError(Throwable error);

    }
}

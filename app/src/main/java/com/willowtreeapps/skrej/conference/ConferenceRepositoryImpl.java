package com.willowtreeapps.skrej.conference;

import com.willowtreeapps.skrej.calendarApi.CalendarService;
import com.willowtreeapps.skrej.model.RoomAvailabilityStatus;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by barrybryant on 11/21/16.
 */

public class ConferenceRepositoryImpl implements ConferenceRepository {

    private final CalendarService calendarService;
    private ConferenceRepositoryListener listener;

    public ConferenceRepositoryImpl(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Override
    public void getEvents(String roomId) {
        calendarService.getRoomAvailability(roomId).subscribeOn(Schedulers.io())
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

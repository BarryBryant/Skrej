package com.willowtreeapps.skrej.calendarApi;

import com.google.api.services.admin.directory.model.CalendarResources;
import com.willowtreeapps.skrej.model.RoomModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by barrybryant on 11/21/16.
 */

public class RoomService {

    private final CredentialWizard credentialWizard;

    public RoomService(CredentialWizard credentialWizard) {
        this.credentialWizard = credentialWizard;
    }

    public Observable<List<RoomModel>> getConferenceRoomsObservable() {
        //The Observable to return.
        Observable<List<RoomModel>> retVal;

        //Create an observable from a call to our credential helper to get all bookable resources
        //from the directory service.
        retVal = getCalendarObservable()

                //Make sure CalendarResources we get back is not null.
                .filter(calendars -> calendars != null)

                //Get list of CalendarResource objects from CalendarResources object.
                .map(calendars -> calendars.getItems())

                //Get item from list.
                .flatMap(calendars -> Observable.from(calendars))

                //We don't want any calendars that have no resource type.
                .filter(room -> room.getResourceType() != null)

                //We are only interested in conference room calendars.
                .filter(room -> room.getResourceType().equals("Conference Room"))

                //Create RoomModel object from room calendar's resource name and ID.
                .map(roomCalendar -> new RoomModel(roomCalendar.getResourceName(), roomCalendar.getResourceEmail()))

                //Collect all rooms into a list.
                .collect(
                        () -> new ArrayList<RoomModel>(),
                        (list, contact) -> list.add(contact));

        //Return our observable.
        return(retVal);
    }

    private Observable<CalendarResources> getCalendarObservable() {
        return (Observable.fromCallable(() -> getCalendarResourcesDirectory()));
    }

    private CalendarResources getCalendarResourcesDirectory() throws IOException {
        return credentialWizard
                        .getDirectoryService()
                        .resources()
                        .calendars()
                        .list("my_customer")
                        .execute();
    }
}

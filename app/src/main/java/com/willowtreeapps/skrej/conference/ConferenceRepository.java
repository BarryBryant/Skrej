package com.willowtreeapps.skrej.conference;

/**
 * Created by barrybryant on 11/21/16.
 */

public interface ConferenceRepository {

    void getEvents(String roomId);

    void bindListener(ConferenceRepositoryImpl.ConferenceRepositoryListener listener);

    void unbindListener();
}

package com.willowtreeapps.skrej.model;

/**
 * Created by barrybryant on 11/21/16.
 */

public interface ConferenceRepository {

    void getEvents(String roomId);

    void bindListener(ConferenceRepositoryImpl.ConferenceRepositoryListener listener);

    void unbindListener();
}

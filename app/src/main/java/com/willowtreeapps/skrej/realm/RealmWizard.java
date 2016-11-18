package com.willowtreeapps.skrej.realm;

import com.google.api.services.admin.directory.model.User;
import com.willowtreeapps.skrej.model.Attendee;
import com.willowtreeapps.skrej.model.RealmUser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by barrybryant on 11/17/16.
 */

public class RealmWizard {


    public void storeContacts(List<User> users) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        for (User user : users) {
            String email = user.getPrimaryEmail();
            String name = user.getName().getFullName();
            if (email != null && name != null) {
                RealmUser realmUser = new RealmUser(email, name);
                realm.copyToRealmOrUpdate(realmUser);
            }
        }
        realm.commitTransaction();
    }

    public List<Attendee> getStoredContacts() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<RealmUser> query = realm.where(RealmUser.class);
        RealmResults<RealmUser> results = query.findAll();
        results = results.sort("name");
        ArrayList<Attendee> attendees = new ArrayList<>();
        for (RealmUser user : results) {
            attendees.add(new Attendee(user.getName(), user.getEmail()));
        }
        return attendees;
    }


}

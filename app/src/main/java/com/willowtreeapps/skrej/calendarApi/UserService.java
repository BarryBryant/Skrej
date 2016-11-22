package com.willowtreeapps.skrej.calendarApi;

import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;
import com.willowtreeapps.skrej.model.Attendee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by barrybryant on 11/21/16.
 */

public class UserService {

    @Inject
    CredentialWizard credentialWizard;

    public UserService(CredentialWizard credentials) {
        this.credentialWizard = credentials;
    }

    public Observable<List<Attendee>> getListObservable() {
        return (
                getDirectoryObservable(500)
                        .map(users -> users.getUsers())
                        .flatMap(usersList -> Observable.from(usersList))
                        .map(user -> apiUserToUserModel(user))
                        .collect(
                                () -> new ArrayList<Attendee>(),
                                (list, contact) -> list.add(contact)
                        )
        );
    }

    private Attendee apiUserToUserModel(User user) {
        return (new Attendee(user.getName().getFullName(), user.getPrimaryEmail()));
    }

    private Observable<Users> getDirectoryObservable(int maxContacts) {
        return (Observable.fromCallable(() -> getCompanyDirectory(maxContacts)));
    }

    private Users getCompanyDirectory(int maxContacts) throws IOException {
        return (
                credentialWizard.getDirectoryService()
                        .users()
                        .list()
                        .setMaxResults(maxContacts)
                        .setViewType("domain_public")
                        .setCustomer("my_customer")
                        .setOrderBy("email")
                        .execute()
        );
    }

}

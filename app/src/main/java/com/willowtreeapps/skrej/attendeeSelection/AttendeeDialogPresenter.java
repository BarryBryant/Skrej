package com.willowtreeapps.skrej.attendeeSelection;

import android.os.Bundle;

/**
 * Created by barrybryant on 11/18/16.
 */

public interface AttendeeDialogPresenter {

    void bindView(AttendeeDialogView view);

    void unbindView();

    void onSearchTextChanged(String searchText);

    void onClickDone();

    void restoreInstanceState(Bundle bundle);

    void saveInstanceState(Bundle bundle);
}

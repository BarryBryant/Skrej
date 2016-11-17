package com.willowtreeapps.skrej.conference;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.adapter.AttendeeAdapter;
import com.willowtreeapps.skrej.adapter.AttendeeCheckedListener;
import com.willowtreeapps.skrej.model.Attendee;
import com.willowtreeapps.skrej.model.RealmUser;
import com.willowtreeapps.skrej.realm.RealmWizard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barrybryant on 11/17/16.
 */

public class AttendeeDialogFragment extends DialogFragment implements AttendeeCheckedListener, View.OnClickListener, TextWatcher {

    private RecyclerView recyclerView;
    private AttendeeAdapter adapter;
    private RealmWizard realmWizard;
    private List<String> selectedAttendees = new ArrayList<>();
    private List<Attendee> attendees = new ArrayList<>();
    private AttendeeSelectedListener listener;

    public AttendeeDialogFragment() {}

    public static AttendeeDialogFragment getInstance() {
        return new AttendeeDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attendee_dialog, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.attendee_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        realmWizard = new RealmWizard();
        attendees = realmWizard.getStoredContacts();
        adapter = new AttendeeAdapter(attendees, this);
        recyclerView.setAdapter(adapter);
        Button doneButton = (Button) view.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);
        EditText searchText = (EditText) view.findViewById(R.id.attendee_search_text);
        searchText.addTextChangedListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AttendeeSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement onAttendeesSelected");
        }
    }

    @Override
    public void onAttendeeChecked(Attendee attendee) {
        if (attendee.isChecked()) {
            selectedAttendees.add(attendee.getEmail());
        } else selectedAttendees.remove(selectedAttendees.indexOf(attendee.getEmail()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doneButton:
                listener.onAttendeesSelected(selectedAttendees);
                dismiss();
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        final List<Attendee> filteredAttendees = filter(attendees, charSequence.toString());
        adapter.animateTo(filteredAttendees);
    }

    @Override
    public void afterTextChanged(Editable editable) {}

    public interface AttendeeSelectedListener {
        void onAttendeesSelected(List<String> attendees);
    }

    private static List<Attendee> filter(List<Attendee> attendees, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Attendee> filteredAttendees = new ArrayList<>();
        for (Attendee attendee : attendees) {
            final String name = attendee.getName().toLowerCase();
            if(name.contains(lowerCaseQuery)) {
                filteredAttendees.add(attendee);
            }
        }
        return filteredAttendees;
    }
}

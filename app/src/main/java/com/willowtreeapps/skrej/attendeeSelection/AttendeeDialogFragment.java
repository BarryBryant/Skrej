package com.willowtreeapps.skrej.attendeeSelection;


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

import com.willowtreeapps.skrej.ConferenceApplication;
import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.adapter.AttendeeAdapter;
import com.willowtreeapps.skrej.model.Attendee;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by barrybryant on 11/17/16.
 */

public class AttendeeDialogFragment extends DialogFragment implements View.OnClickListener, TextWatcher, AttendeeDialogView {

    @Inject
    AttendeeDialogPresenter presenter;

    private RecyclerView recyclerView;
    private AttendeeAdapter adapter;
    private AttendeeSelectedListener listener;
    private EditText searchText;

    public AttendeeDialogFragment() {
    }

    public static AttendeeDialogFragment getInstance() {
        return new AttendeeDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ConferenceApplication.get(getContext()).component().inject(this);
        View view = inflater.inflate(R.layout.attendee_dialog, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.attendee_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Button doneButton = (Button) view.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(this);
        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        searchText = (EditText) view.findViewById(R.id.attendee_search_text);
        searchText.addTextChangedListener(this);
        this.setCancelable(false);
        if (savedInstanceState != null) {
            presenter.restoreInstanceState(savedInstanceState);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.bindView(this);
        initializeSearchIfNotNull();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unbindView();
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doneButton:
                presenter.onClickDone();
                break;
            case R.id.cancelButton:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void initializeAttendeeList(List<Attendee> attendees) {
        adapter = new AttendeeAdapter(attendees, (AttendeeAdapter.AttendeeCheckedListener) presenter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateAttendees(String search) {
        adapter.filter(search);
    }

    @Override
    public void dismissAndReturnSelectedAttendees(List<String> attendees) {
        listener.onAttendeesSelected(attendees);
        dismiss();
    }

    private void initializeSearchIfNotNull() {
        String search = searchText.getText().toString();
        if (search.length() > 0) {
            presenter.onSearchTextChanged(search);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        presenter.onSearchTextChanged(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    public interface AttendeeSelectedListener {
        void onAttendeesSelected(List<String> attendees);
    }

}

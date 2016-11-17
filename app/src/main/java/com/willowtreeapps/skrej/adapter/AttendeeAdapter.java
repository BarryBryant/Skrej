package com.willowtreeapps.skrej.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.model.Attendee;
import com.willowtreeapps.skrej.model.RealmUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barrybryant on 11/17/16.
 */

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.ViewHolder> {

    private List<Attendee> attendees;
    private List<Attendee> filteredAttendees;
    private AttendeeCheckedListener listener;

    public AttendeeAdapter(List<Attendee> attendees, AttendeeCheckedListener listener) {
        this.attendees = attendees;
        this.listener = listener;
    }

    public void addItem(int position, Attendee attendee) {
        attendees.add(attendee);
        notifyItemInserted(position);
    }

    public void removeItem(Attendee attendee) {
        int position = attendees.indexOf(attendee);
        attendees.remove(position);
        notifyItemRemoved(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Attendee attendee = attendees.get(fromPosition);
        attendees.add(toPosition, attendee);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendee_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Attendee attendee = attendees.get(position);
        String name = attendee.getName();
        holder.checkBox.setText(name);
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(attendee.isChecked());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Attendee attendee = attendees.get(holder.getAdapterPosition());
                attendee.setChecked(b);
                attendees.set(holder.getAdapterPosition(), attendee);
                Log.d("HOLDER", attendee.getName() + attendee.isChecked());
                Log.d("HOLDER@@", "" + attendees.get(holder.getAdapterPosition()).isChecked());
                listener.onAttendeeChecked(attendee);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attendees.size();
    }

    public void animateTo(List<Attendee> attendees) {
        applyAndAnimateRemovals(attendees);
        applyAndAnimateAdditions(attendees);
        applyAndAnimateMovedItems(attendees);

    }

    private void applyAndAnimateRemovals(List<Attendee> newAttendees) {
        for (int i = attendees.size() - 1; i >= 0; i--) {
            final Attendee attendee = attendees.get(i);
            if (!newAttendees.contains(attendee)) {
                removeItem(attendee);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Attendee> newAttendees) {
        for (int i = 0, count = newAttendees.size(); i < count; i++) {
            final Attendee attendee = newAttendees.get(i);
            if (!attendees.contains(attendee)) {
                addItem(i, attendee);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Attendee> newAttendees) {
        for (int toPosition = newAttendees.size() - 1; toPosition >= 0; toPosition--) {
            final Attendee attendee = newAttendees.get(toPosition);
            final int fromPosition = attendees.indexOf(attendee);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        ViewHolder(View v) {
            super(v);
            checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        }
    }

}

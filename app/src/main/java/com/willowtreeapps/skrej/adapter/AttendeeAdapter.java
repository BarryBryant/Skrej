package com.willowtreeapps.skrej.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.model.Attendee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barrybryant on 11/17/16.
 */

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.ViewHolder> {

    private List<Attendee> attendees;
    private List<Attendee> baseList;
    private AttendeeCheckedListener listener;

    public AttendeeAdapter(List<Attendee> attendees, AttendeeCheckedListener listener) {
        this.attendees = new ArrayList<>(attendees);
        this.baseList = new ArrayList<>(attendees);
        this.listener = listener;
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
        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            attendee.setChecked(b);
            attendees.set(holder.getAdapterPosition(), attendee);
            listener.onAttendeeChecked(attendee);
        });
    }

    @Override
    public int getItemCount() {
        return attendees.size();
    }

    public void filter(String text) {
        attendees.clear();
        if (text.isEmpty()) {
            attendees.addAll(baseList);
        } else {
            text = text.toLowerCase();
            for (Attendee attendee : baseList) {
                if (attendee.getName().toLowerCase().contains(text)) {
                    attendees.add(attendee);
                }
            }
        }
        notifyDataSetChanged();
    }

    public interface AttendeeCheckedListener {

        void onAttendeeChecked(Attendee attendee);

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        ViewHolder(View v) {
            super(v);
            checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        }
    }

}

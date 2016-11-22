package com.willowtreeapps.skrej.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.model.Room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barrybryant on 11/21/16.
 */

public class RoomAdapter extends RecyclerView.Adapter {

    private List<Room> rooms = new ArrayList<>();

    public RoomAdapter(final List<Room> rooms) {
        if (rooms != null) {
            this.rooms = rooms;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RoomViewHolder) holder).bindData(rooms.get(position));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.room_selector;
    }
}

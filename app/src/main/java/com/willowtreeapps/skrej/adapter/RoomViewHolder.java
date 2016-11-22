package com.willowtreeapps.skrej.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.conference.ConferenceRoomActivity;
import com.willowtreeapps.skrej.model.Room;

/**
 * Created by barrybryant on 11/21/16.
 */
public class RoomViewHolder extends RecyclerView.ViewHolder {

    private Button roomButton;
    private Context context;

    public RoomViewHolder(View view) {
        super(view);
        this.context = view.getContext();
        roomButton = (Button) view.findViewById(R.id.room_selector_button);
    }

    public void bindData(Room room) {
        roomButton.setText(room.getRoomName());
        roomButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(context, ConferenceRoomActivity.class);
            intent.putExtra(context.getString(R.string.room_id_bundle_key), room);
            context.startActivity(intent);
        });
    }
}

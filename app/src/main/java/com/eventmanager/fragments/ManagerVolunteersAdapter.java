package com.eventmanager.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.eventmanager.R;
import com.eventmanager.database.AppDatabase;
import com.eventmanager.database.entity.Volunteer;

import java.util.List;

public class ManagerVolunteersAdapter extends RecyclerView.Adapter<ManagerVolunteersAdapter.ViewHolder> {
    private List<Volunteer> mDataset;
    private Context context;
    private RecyclerView recycler;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView volunteerRowName, volunteerRowId;
        public ImageView volunteerRowCircle;

        public ViewHolder(View view) {
            super(view);
            volunteerRowName = view.findViewById(R.id.manager_volunteers_row_name);
            volunteerRowId = view.findViewById(R.id.manager_volunteer_row_id);
            volunteerRowCircle = view.findViewById(R.id.manager_volunteers_row_circle);
        }
    }

    public ManagerVolunteersAdapter(List<Volunteer> volunteers, Context context, RecyclerView recycler) {
        this.context = context;
        this.recycler = recycler;
        mDataset = volunteers;
    }

    //Create new views (invoked by the layout manager).
    @Override
    public ManagerVolunteersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manager_volunteers_row, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from the dataset at this position
        // - replace the contents of the view with that element

        Volunteer volunteer = mDataset.get(position);

        holder.volunteerRowName.setText(volunteer.getName());
        holder.volunteerRowId.setText("ID: " + Integer.toString(volunteer.getId()));

        //To generate the colors for the row circle.
        ColorGenerator generator = ColorGenerator.MATERIAL;

        //Set the first letter of the volunteer's name in the circle.
        String letter = String.valueOf(volunteer.getName().charAt(0));

        //Create a TextDrawable for the image background.
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, generator.getRandomColor());
        holder.volunteerRowCircle.setImageDrawable(drawable);

        //Set the long click listener
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Show a dialog asking for confirmation to delete.
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle(R.string.delete_volunteer);
                builder.setMessage(R.string.delete_volunteer_prompt);

                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteVolunteer(holder);
                    }
                });

                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
                return true;
            }
        });
    }

    private void deleteVolunteer(ViewHolder holder) {
        //We use a thread instead of AsyncTask here because we don't need to do any UI operations
        //so keep it simple.

        final int position = holder.getAdapterPosition();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                AppDatabase database = AppDatabase.getInstance(context);
                database.eventDao().deleteVolunteers(mDataset.get(position));

                mDataset.remove(position);

                //Since we can not notify the data set change on the new thread, we need to
                //do it on the UI thread.
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

    public List<Volunteer> getDataset() {
        return mDataset;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

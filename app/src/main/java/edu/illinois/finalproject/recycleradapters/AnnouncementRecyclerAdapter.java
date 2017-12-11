package edu.illinois.finalproject.recycleradapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.javaobjects.AnnouncementList;
import edu.illinois.finalproject.javaobjects.Announcement;

public class AnnouncementRecyclerAdapter extends RecyclerView.Adapter<AnnouncementRecyclerAdapter.ViewHolder> {
    private HashMap<String, Announcement> announcements;

    public AnnouncementRecyclerAdapter(AnnouncementList announcements) {
        this.announcements = announcements.getAnnouncements();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View announcementElement = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_announcement_list_element, parent, false);
        return new ViewHolder(announcementElement);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Announcement announcement = announcements.get((String.valueOf(position) + "_key"));
        holder.announcementTitleField.setText(announcement.getTitle());
        holder.announcementMessageField.setText(announcement.getMessage());

        StringBuilder announcementInfo = new StringBuilder("Posted on ");
        announcementInfo.append(announcement.getDatePosted());
        announcementInfo.append(" at ");
        announcementInfo.append(announcement.getTimePosted());
        holder.announcementInfoField.setText(announcementInfo);
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView announcementTitleField;
        public TextView announcementInfoField;
        public TextView announcementMessageField;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.announcementTitleField = (TextView) itemView.findViewById(R.id.announcementTitleField);
            this.announcementInfoField = (TextView) itemView.findViewById(R.id.announcementInfoField);
            this.announcementMessageField = (TextView) itemView.findViewById(R.id.announcementMessageField);
        }
    }
}

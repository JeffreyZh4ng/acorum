package edu.illinois.finalproject.recycleradapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import edu.illinois.finalproject.R;
import edu.illinois.finalproject.javaobjects.ForumPost;


public class ForumPostRecyclerAdapter extends RecyclerView.Adapter<ForumPostRecyclerAdapter.ViewHolder> {
    private HashMap<String, ForumPost> forumPostHashMap;

    public ForumPostRecyclerAdapter(HashMap<String, ForumPost> forumPostHashMap) {
        this.forumPostHashMap = forumPostHashMap;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View forumPostHolder = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_forum_post_preview_element, parent, false);
        return new ViewHolder(forumPostHolder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ForumPost forumPost = forumPostHashMap.get((String.valueOf(position) + "_key"));
        holder.forumPostTitleField.setText(forumPost.getPostTitle());
        holder.forumPostMessageField.setText(forumPost.getPostMessage());
        holder.forumPostInfoField.setText("Posted on " + forumPost.getDatePosted());
    }

    @Override
    public int getItemCount() {
        return forumPostHashMap.size();
    }

    /**
     * The view holder inner class that sets the layout of each element in the list
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView forumPostTitleField;
        public TextView forumPostInfoField;
        public TextView forumPostMessageField;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.forumPostTitleField = (TextView) itemView.findViewById(R.id.forumPostTitleField);
            this.forumPostInfoField = (TextView) itemView.findViewById(R.id.forumPostInfoField);
            this.forumPostMessageField = (TextView) itemView.findViewById(R.id.forumPostMessageField);
        }
    }
}

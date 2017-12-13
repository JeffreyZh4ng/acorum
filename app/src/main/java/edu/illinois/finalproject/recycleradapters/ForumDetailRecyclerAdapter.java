package edu.illinois.finalproject.recycleradapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.javaobjects.ForumResponsePost;
import edu.illinois.finalproject.javaobjects.UserInformation;


public class ForumDetailRecyclerAdapter extends RecyclerView.Adapter<ForumDetailRecyclerAdapter.ViewHolder> {
    HashMap<String, ForumResponsePost> responseList;
    DataSnapshot dataSnapshot;

    public ForumDetailRecyclerAdapter(HashMap<String, ForumResponsePost> responseList, DataSnapshot dataSnapshot) {
        this.responseList = responseList;
        this.dataSnapshot = dataSnapshot;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View forumPostResponseHolder = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_forum_post_response_element, parent, false);
        return new ForumDetailRecyclerAdapter.ViewHolder(forumPostResponseHolder);
    }

    /**
     * Sets the text for each response to a forum post
     *
     * @param holder The holder for each response
     * @param position The position in the list of the element
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String postKey = String.valueOf(position) + "_key";
        ForumResponsePost response = responseList.get(postKey);

        String userKey = response.getPosterKey();
        StringBuilder userName = new StringBuilder();
        String userFirstName = dataSnapshot.child(Constants.USERS_CHILD).child(userKey).getValue(UserInformation.class).getFirstName();
        userName.append(userFirstName);
        userName.append(" ");
        String userLastName = dataSnapshot.child(Constants.USERS_CHILD).child(userKey).getValue(UserInformation.class).getLastName();
        userName.append(userLastName);
        holder.forumPostResponseInfoField.setText("Posted on: " + response.getDatePosted() + " By: " + userName);

        holder.forumPostResponseMessageField.setText(response.getPostMessage());
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    /**
     * The view holder inner class that sets the layout of each element in the list
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView forumPostResponseInfoField;
        public TextView forumPostResponseMessageField;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.forumPostResponseInfoField = (TextView) itemView.findViewById(R.id.forumPostResponseInfoField);
            this.forumPostResponseMessageField = (TextView) itemView.findViewById(R.id.forumPostResponseMessageField);
        }
    }
}

package edu.illinois.finalproject.recycleradapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.activities.CourseDashboardActivity;
import edu.illinois.finalproject.activities.UserDashboardActivity;
import edu.illinois.finalproject.javaobjects.Course;

/**
 * Created by jeffreyzhang on 12/12/17.
 */

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Course> courseList;
    private ArrayList<String> courseKeyList;
    private DataSnapshot dataSnapshot;
    private String userKey;

    public CourseRecyclerViewAdapter(ArrayList<Course> courseList, ArrayList<String> courseKeyList, DataSnapshot dataSnapshot, String userKey) {
        this.courseList = courseList;
        this.courseKeyList = courseKeyList;
        this.dataSnapshot = dataSnapshot;
        this.userKey = userKey;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View courseHolder = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_course_list_element, parent, false);
        return new CourseRecyclerViewAdapter.ViewHolder(courseHolder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = courseList.get(position);
        String courseKey = courseKeyList.get(position);
        setElementText(course, holder);
        setElementOnClick(courseKey, holder, dataSnapshot);
    }

    /**
     * Helper method that will be used to set each of the course element's text
     *
     * @param course The course object from the database
     * @param holder The holder of the inflater
     */
    private void setElementText(Course course, ViewHolder holder) {
        holder.courseNameView.setText(course.getCourseName());
        holder.courseInstructorView.setText("Instructor:" + course.getHeadInstructor());
        holder.courseUniversityView.setText(course.getUniversity());

        StringBuilder courseInfo = new StringBuilder(course.getTerm());
        courseInfo.append(" ");
        courseInfo.append(course.getYear());
        courseInfo.append(", Section: ");
        courseInfo.append(course.getSection());
        holder.courseInfoView.setText(courseInfo);
    }

    /**
     * Helper method that sets on click listeners to each of the elements in the list and adds in the
     * extra arguemnts needed for the course dashboard activity
     *
     * @param courseKey The courseKey of the course
     * @param holder The holder of the layout inflater
     * @param dataSnapshot the dataSnapshot of the database
     */
    private void setElementOnClick(final String courseKey, ViewHolder holder, final DataSnapshot dataSnapshot) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> courseMap = dataSnapshot.child(Constants.COURSES_CHILD)
                        .child(courseKey).getValue(Course.class).getInstructors();
                boolean isInstructor = courseMap.containsKey(userKey);

                Intent intent = new Intent(view.getContext(), CourseDashboardActivity.class)
                        .putExtra(Constants.COURSE_KEY_ARG, courseKey).putExtra(Constants.IS_INSTRUCTOR_ARG, isInstructor);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public TextView courseNameView;
        public TextView courseInstructorView;
        public TextView courseInfoView;
        public TextView courseUniversityView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.courseNameView = (TextView) itemView.findViewById(R.id.courseNameView);
            this.courseInstructorView = (TextView) itemView.findViewById(R.id.courseInstructorView);
            this.courseInfoView = (TextView) itemView.findViewById(R.id.courseInfoView);
            this.courseUniversityView = (TextView) itemView.findViewById(R.id.courseUniversityView);
        }
    }
}

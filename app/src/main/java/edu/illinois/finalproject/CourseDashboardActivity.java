package edu.illinois.finalproject;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.illinois.finalproject.javaobjects.Course;
import edu.illinois.finalproject.tabfragments.AnnouncementFragment;
import edu.illinois.finalproject.tabfragments.CourseWorkFragment;
import edu.illinois.finalproject.tabfragments.ForumFragment;

public class CourseDashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private String courseKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_dashboard);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
        courseKey = getIntent().getExtras().getString("courseKey");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String courseName = dataSnapshot.child("courses").child(courseKey).getValue(Course.class).getCourseName();
                setTitle(courseName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Override method that will create the settings icon and the back button in the menu bar
     *
     * @param menu The menu that the icons are being set to
     * @return True
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        getMenuInflater().inflate(R.menu.menu_back_button, menu);
        return true;
    }

    /**
     * Override method that controls what happens when you click on one of the icons in the menu bar
     *
     * @param item The item in the menu that was clicked on
     * @return True
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileButton:
                startActivity(new Intent(CourseDashboardActivity.this, ProfileActivity.class));
                finish();
                break;
            case R.id.profileSettingsButton:
                startActivity(new Intent(CourseDashboardActivity.this, ProfileSettingsActivity.class));
                finish();
                break;
            case R.id.logoutButton:
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    startActivity(new Intent(CourseDashboardActivity.this, LoginActivity.class));
                }
                break;
            case R.id.backButton:
                startActivity(new Intent(CourseDashboardActivity.this, UserDashboardActivity.class));
                break;
        }
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return AnnouncementFragment.newInstance(courseKey);
                case 1:
                    return CourseWorkFragment.newInstance(courseKey);
                case 2:
                    return ForumFragment.newInstance(courseKey);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Announcements";
                case 1:
                    return "In Class";
                case 2:
                    return "Forum";
            }
            return null;
        }
    }
}

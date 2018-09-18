package com.ensharp.seoul.seoultheplace.Course.PlaceView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.Fragments.CourseCardFragment;
import java.util.ArrayList;
import java.util.List;

public class CourseFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    List<CourseCardFragment> courseCards;
    private float baseElevation;

    public CourseFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        courseCards = new ArrayList<>();
        this.baseElevation = baseElevation;
    }

    public void setCourseData(ArrayList<CourseVO> courses) {
        for(int i = 0; i < courses.size(); i++){
            CourseCardFragment courseCard = new CourseCardFragment();
            courseCard.setPosition(i);
            courseCard.setData(courses.get(i));
            addCardFragment(courseCard);
        }
    }

    @Override
    public CardView getCardViewAt(int position) {
        return courseCards.get(position).getCardView();
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    public void addCardFragment(CourseCardFragment fragment) {
        courseCards.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return courseCards.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        courseCards.set(position, (CourseCardFragment)fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return courseCards.size();
    }
}
package com.example.administrator.mytestdemo.verticalviewpager;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.mytestdemo.R;

import java.util.Locale;


public class ViewPagerActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_viewpager);
//        setTitle("");
//        initViewPager();
//    }
//
//    private void initViewPager() {
//        VerticalViewPager viewPager = (VerticalViewPager) findViewById(R.id.vertical_viewpager);
//        //viewPager.setPageTransformer(false, new ZoomOutTransformer());
//        //viewPager.setPageTransformer(true, new StackTransformer());
//        String title = "ContentFragment";
//        viewPager.setAdapter(new ContentFragmentAdapter.Holder(getSupportFragmentManager())
//                .add(ContentFragment.newInstance("title1", 1))
//                .add(ContentFragment.newInstance("title2", 2))
//                .add(ContentFragment.newInstance("title3", 3))
//                .add(ContentFragment.newInstance("title4", 4))
//                .add(ContentFragment.newInstance("title5", 5))
//                .add(ContentFragment.newInstance("title6", 6))
//                .add(ContentFragment.newInstance("title7", 7))
//                .add(ContentFragment.newInstance("title8", 8))
//                .add(ContentFragment.newInstance("title9", 9))
//                .add(ContentFragment.newInstance("title10", 10))
//                .add(ContentFragment.newInstance("title11", 11))
//                .add(ContentFragment.newInstance("title12", 12))
//                .set());
//        //If you setting other scroll mode, the scrolled fade is shown from either side of display.
//        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
//    }

    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.75f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager2);
        VerticalViewPager2 verticalViewPager = (VerticalViewPager2) findViewById(R.id.verticalviewpager);

        verticalViewPager.setAdapter(new DummyAdapter(getSupportFragmentManager()));
//        verticalViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.pagemargin));
        verticalViewPager.setPageMarginDrawable(new ColorDrawable(getResources().getColor(android.R.color.holo_green_dark)));

//        verticalViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(View view, float position) {
//                int pageWidth = view.getWidth();
//                int pageHeight = view.getHeight();
//
//                if (position < -1) { // [-Infinity,-1)
//                    // This page is way off-screen to the left.
//                    view.setAlpha(0);
//
//                } else if (position <= 1) { // [-1,1]
//                    // Modify the default slide transition to shrink the page as well
//                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
//                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
//                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
//                    if (position < 0) {
//                        view.setTranslationY(vertMargin - horzMargin / 2);
//                    } else {
//                        view.setTranslationY(-vertMargin + horzMargin / 2);
//                    }
//
//                    // Scale the page down (between MIN_SCALE and 1)
//                    view.setScaleX(scaleFactor);
//                    view.setScaleY(scaleFactor);
//
//                    // Fade the page relative to its size.
//                    view.setAlpha(MIN_ALPHA +
//                            (scaleFactor - MIN_SCALE) /
//                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));
//
//                } else { // (1,+Infinity]
//                    // This page is way off-screen to the right.
//                    view.setAlpha(0);
//                }
//            }
//        });
    }

    public class DummyAdapter extends FragmentPagerAdapter {

        public DummyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 9;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "PAGE 1";
                case 1:
                    return "PAGE 2";
                case 2:
                    return "PAGE 3";
                case 3:
                    return "PAGE 4";
                case 4:
                    return "PAGE 5";
                case 5:
                    return "PAGE 6";
                case 6:
                    return "PAGE 7";
                case 7:
                    return "PAGE 8";
                case 8:
                    return "PAGE 9";
            }
            return null;
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_layout, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.textview);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }


    }
}

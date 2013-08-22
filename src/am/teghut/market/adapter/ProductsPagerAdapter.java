package am.teghut.market.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ProductsPagerAdapter extends PagerAdapter {

	private List<View> screens;

	public ProductsPagerAdapter(List<View> screens) {
		this.screens = screens;

	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View screen = screens.get(position);
		((ViewPager) container).addView(screen, 0);
		return screen;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public int getCount() {
		return screens.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0.equals(arg1);
	}
}

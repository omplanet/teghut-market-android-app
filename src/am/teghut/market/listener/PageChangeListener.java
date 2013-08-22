package am.teghut.market.listener;

import am.teghut.market.layout.FooterLayout;
import am.teghut.market.ui.ProductsActivity;
import android.support.v4.view.ViewPager;


public class PageChangeListener extends
		ViewPager.SimpleOnPageChangeListener {

	private FooterLayout footerLayout;

	public PageChangeListener(FooterLayout footerLayout) {
		this.footerLayout = footerLayout;
	}

	@Override
	public void onPageSelected(int position) {
		footerLayout.drawBorder(position);
		footerLayout.repaintPages(position);
		ProductsActivity.currentScreen = position;
	}
}

	package am.teghut.market.layout;

import java.util.ArrayList;
import java.util.List;

import am.teghut.market.R;
import am.teghut.market.constant.Const;
import am.teghut.market.ui.AppRootActivity;
import am.teghut.market.ui.ProductsActivity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FooterLayout {

	private Resources res;
	private Context context;
	private int pageCount;
	private int displayWidth;
	private int after = ProductsActivity.currentScreen;

	private Drawable selected;
	private Drawable notSelected;

	private LayoutInflater inflater;

	private List<TextView> pages;
	private View viewGroup;

	private LinearLayout footer;
	private LinearLayout next;
	private LinearLayout back;

	private TextView nextText;
	private TextView backText;
	private TextView afterPoint;
	private TextView firstPoint;


	public FooterLayout(int pageCount, Context context) {
		res = AppRootActivity.getAppResources();
		setPageCount(pageCount);
		this.context = context;
		initUi();
	}

	private void initUi() {
		setDrawableResource();
		inflater = ((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		viewGroup = inflater.inflate(R.layout.footer, null);
		footer = (LinearLayout) viewGroup.findViewById(R.id.page_content);
		footer.setMinimumHeight(displayWidth / 10);
		pages = new ArrayList<TextView>();
		setDisplayWidth();
		createPaging();
		createNextBackButton();
		afterPoint = initPoint();
		firstPoint = initPoint();
		repaintPages(after);
	}

	private void setDrawableResource() {
		notSelected = context.getResources().getDrawable(
				R.drawable.page_number_not_selected);
		selected = context.getResources().getDrawable(
				R.drawable.page_number_selected);
	}

	private void createPaging() {
		for (int i = 0; i < pageCount; i++) {
			TextView pageNumber = new TextView(context);
			setViewSizeAndAttrs(pageNumber, i);
			pages.add(pageNumber);
		}
	}

	private void setViewSizeAndAttrs(TextView textView, int i) {
		// textView.setFocusable(false);
		textView.setMinimumHeight(displayWidth / 10);
		textView.setMinimumWidth(displayWidth / 10);
		textView.setBackgroundColor(res.getColor(R.color.footer_ligth_grey));
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(res.getColor(R.color.white));
		textView.setBackgroundDrawable(notSelected);
		textView.setText((i + 1) + "");
	}

	private void setPageCount(int pageCount) {
		if (pageCount % Const.ITEMS_PER_PAGE == 0) {
			this.pageCount = pageCount / Const.ITEMS_PER_PAGE;
		} else {
			this.pageCount = pageCount / Const.ITEMS_PER_PAGE + 1;
		}
	}

	private void setDisplayWidth() {
		Display display = ((ProductsActivity) context).getWindowManager()
				.getDefaultDisplay();
		displayWidth = display.getWidth();
	}

	public void drawBorder(int position) {
		pages.get(after).setBackgroundDrawable(notSelected);
		after = position;
		pages.get(position).setBackgroundDrawable(selected);
	}

	private void createNextBackButton() {
		back = new LinearLayout(context);
		back.setLayoutParams(new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		back.setMinimumWidth(displayWidth / 5);
		back.setGravity(Gravity.CENTER);
		back.setBackgroundDrawable(res
				.getDrawable(R.drawable.page_number_not_selected));
		ImageView backImage = new ImageView(context);
		backImage.setBackgroundDrawable(res.getDrawable(R.drawable.arrow_left));
		backText = new TextView(context);
		backText.setMinimumHeight(displayWidth / 10);
		backText.setGravity(Gravity.CENTER);
		backText.setText(AppRootActivity.words.get(Const.BACK));
		backText.setTextColor(res.getColor(R.color.white));
		back.addView(backImage);
		back.addView(backText);

		next = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		if (pageCount < 6) {
			params.setMargins((6 - pageCount) * displayWidth / 10, 0, 0, 0);
		}
		next.setLayoutParams(params);
		next.setMinimumWidth(displayWidth / 5);
		next.setGravity(Gravity.CENTER);
		next.setBackgroundDrawable(res
				.getDrawable(R.drawable.page_number_not_selected));
		ImageView nextImage = new ImageView(context);
		nextImage
				.setBackgroundDrawable(res.getDrawable(R.drawable.arrow_rigth));
		nextText = new TextView(context);
		nextText.setMinimumHeight(displayWidth / 10);
		nextText.setGravity(Gravity.CENTER);
		nextText.setText(AppRootActivity.words.get(Const.NEXT));
		nextText.setTextColor(res.getColor(R.color.white));
		next.addView(nextText);
		next.addView(nextImage);
	}

	private TextView initPoint() {
		TextView point = new TextView(context);
		point.setMinimumHeight(displayWidth / 10);
		point.setMinimumWidth(displayWidth / 10);
		point.setGravity(Gravity.CENTER);
		point.setBackgroundDrawable(notSelected);
		point.setTextColor(res.getColor(R.color.white));
		point.setText("...");
		return point;
	}

	public void repaintPages(int position) {
		if (pageCount > 6 && position > 2 && position < pageCount - 4) {
			footer.removeAllViewsInLayout();
			footer.addView(back);
			footer.addView(pages.get(0));
			footer.addView(firstPoint);
			footer.addView(pages.get(position));
			footer.addView(pages.get(position + 1));
			footer.addView(afterPoint);
			footer.addView(pages.get(pages.size() - 1));
			footer.addView(next);
		} else if (pageCount > 6 && position >= pageCount - 4) {
			footer.removeAllViewsInLayout();
			footer.addView(back);
			footer.addView(pages.get(0));
			footer.addView(firstPoint);
			for (int i = pages.size() - 4; i < pages.size(); i++) {
				footer.addView(pages.get(i));
			}
			footer.addView(next);
		} else {
			footer.removeAllViewsInLayout();
			if (pageCount <= 6) {
				footer.addView(back);
				for (int i = 0; i < pages.size(); i++) {
					footer.addView(pages.get(i));
				}
				footer.addView(next);
			} else {
				footer.addView(back);
				for (int i = 0; i < 4; i++) {
					footer.addView(pages.get(i));
				}
				footer.addView(afterPoint);
				footer.addView(pages.get(pages.size() - 1));
				footer.addView(next);
			}
		}
	}

	public List<TextView> getTextVierws() {
		return pages;
	}

	public View getContainer() {
		return viewGroup;
	}

	public int getPageCount() {
		return pages.size();
	}

	public Context getContext() {
		return context;
	}

	public LinearLayout getNextButton() {
		return next;
	}

	public LinearLayout getBackButton() {
		return back;
	}

	public TextView getFirstPoint() {
		return firstPoint;
	}

	public TextView getAfterPoint() {
		return afterPoint;
	}

	public TextView getNextText() {
		return nextText;
	}

	public TextView getBackText() {
		return backText;
	}


}

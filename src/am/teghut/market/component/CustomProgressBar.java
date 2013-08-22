package am.teghut.market.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class CustomProgressBar extends ProgressBar {

	private String text;
	private Paint painter;

	public CustomProgressBar(Context context) {
		super(context);
		initPainter();
	}

	public CustomProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPainter();
	}

	public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPainter();
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Rect bounds = new Rect();
		painter.getTextBounds(text, 0, text.length(), bounds);
		int x = getWidth() / 2 - bounds.centerX();
		int y = getHeight() / 2 - bounds.centerY();
		canvas.drawText(text, x, y, painter);
	}

	public synchronized void setText(String text) {
		this.text = text;
		drawableStateChanged();
	}

	public void setTextColor(int color) {
		painter.setColor(color);
		drawableStateChanged();
	}

	private void initPainter() {
		painter = new Paint();
		painter.setColor(Color.WHITE);
		text = "";
	}
}

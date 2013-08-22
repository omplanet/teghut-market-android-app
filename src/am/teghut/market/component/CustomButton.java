package am.teghut.market.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class CustomButton extends Button {
	
	private String productId; 

	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomButton(Context context) {
		super(context);
	}

	@Override
	public void setPressed(boolean pressed) {
		if (pressed && getParent() instanceof View
				&& ((View) getParent()).isPressed()) {
			return;
		}
		super.setPressed(pressed);
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	

}

package am.teghut.market.ui;

import am.teghut.market.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class AboutUsActivity extends AppRootActivity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		initWebView();
	}
	
	private void initWebView() {
		webView = (WebView) findViewById(R.id.web);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(res.getString(R.string.poweredByURL));
		webView.setWebViewClient(new AboutUsClient());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public class AboutUsClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

}

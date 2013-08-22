package am.teghut.market.api.connector;

@SuppressWarnings("serial")
public class ApiException extends Exception {

	public ApiException() {
		super();
	}

	public ApiException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ApiException(String arg0) {
		super(arg0);
	}

	public ApiException(Throwable arg0) {
		super(arg0);
	}

}

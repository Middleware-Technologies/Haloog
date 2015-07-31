package haloog;


import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Represents a single log entry making easier to get informations from it
 */
import java.util.Locale;
import java.util.regex.*;


public class LogEntry {
	
	public static final String NO_DOMAIN = "unknown domain";
	
	/** Pattern representing the structure of a long entry **/
	private static final String logEntryPattern = "^([\\w.]+) (\\S+) (.+?) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\S+) \"(.*?)\" \"(.*?)\"";

	/** Pattern for recognizing a URL, based off RFC 3986 **/
	private static final Pattern urlPattern = Pattern.compile(
			"(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
					+ "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
					+ "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	
	/** The entire log entry as string **/
	private String logEntryLine;
	
	/** The date of the log **/
	private Date loggedTime;
	
	/** The logged request **/
	private String loggedRequest;
	
	/** The logged referer **/
	private String loggedReferer;

	public LogEntry(String logEntryLine) {
		this.logEntryLine = logEntryLine;

		try {
			parseLogEntry();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void parseLogEntry() throws ParseException {
		Pattern p = Pattern.compile(logEntryPattern);
		Matcher matcher = p.matcher(logEntryLine);

		if (!matcher.matches()) {
			System.err.append("Bad log entry (or problem with RE?):");
			System.err.append(logEntryLine);
		}

		DateFormat format = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss X", Locale.US);
		loggedTime = format.parse(matcher.group(4));

		loggedRequest = matcher.group(5);
		loggedReferer = matcher.group(8);
	}

	public Date getLoggedTime() {
		return loggedTime;
	}

	public String getLoggedRequest() {
		return loggedRequest;
	}

	public String getLoggedReferer() {
		return loggedReferer;
	}

	/**
	 * Tries to extract the referer's domain. If it doesn't succeed, returns a string that reflects this fact.
	 * 
	 * @return String | "unknown domain"
	 */
	public String getRefererDomain() {
		
		// Look for an URL in the string
		Matcher matcher = urlPattern.matcher(loggedReferer);
		
		if (matcher.find()) {
			// If one is found, try to get the domain (host) from it
			URI domain;
			
			try {
				domain = new URI(matcher.group());
			} catch (URISyntaxException e) {
				return NO_DOMAIN;
			}

			try {
				return domain.getHost().replace("www.", "");
			} catch (NullPointerException e) {
				return NO_DOMAIN;
			}
			
			// Yeah, I don't like it either

		}

		return "unknown domain";
	}
}
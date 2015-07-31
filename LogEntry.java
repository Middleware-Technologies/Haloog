package haloog;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/*
 * Copyright (c) Ian F. Darwin, http://www.darwinsys.com/, 1996-2002.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java 
 * language and environment is gratefully acknowledged.
 * 
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */
import java.util.regex.*;


/**
 * Parse an Apache log file with Regular Expressions
 */
public class LogEntry {

	private String logEntryLine;

	private String logEntryPattern = "^([\\w.]+) (\\S+) (.+?) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\S+) \"(.*?)\" \"(.*?)\"";

	private Date loggedTime;
	private String loggedRequest;
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
}
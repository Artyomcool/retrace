package org.artyomcool.retrace;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class RetraceTest {

	@Test
	public void readMapping() throws IOException {
		Retrace retrace = createRetrace();
		ClassMapping retraceClass = retrace.getClass("b.a.a.e");
		assertEquals("org.artyomcool.retrace.StackTraceAnalyzer$1", retraceClass.getName());
	}

	@Test
	public void retrace() throws IOException {
		Retrace retrace = createRetrace();
		String retraced = retrace.stacktrace(readResource("/stacktrace.txt"));
		assertEquals("java.lang.RuntimeException: some text\n" +
						"\tat com.google.common.base.Joiner.access$000$202dd7f0(SourceFile:103)\n" +
						"\t\t\t\taccess$200$7a4c6e58\n" +
						"\tat org.artyomcool.retrace.StackTraceAnalyzer$1.apply(SourceFile:62)\n" +
						"\tat org.artyomcool.retrace.StackTraceAnalyzer.resolveClassName(SourceFile:105)\n" +
						"Caused by: java.lang.IllegalArgumentException: some text 2\n" +
						"\tat org.artyomcool.retrace.Retrace.stacktrace(SourceFile:40)\n" +
						"\tat some.unknown.method(SourceFile:76)\n" +
						"\tat some.unknown.method2(UnknownSource)\n" +
						"\tat org.artyomcool.retrace.ClassMapping.getObfuscatedName(UnknownSource)\n" +
						"\t\t\t\taddLine\n" +
						"Caused by: java.lang.NullPointerException\n" +
						"\tat org.artyomcool.retrace.MethodMapping.toString(SourceFile:45)\n" +
						"\t... 2 more\n",
				retraced);
	}

	private Retrace createRetrace() throws IOException {
		try (BufferedReader reader = readResource("/mapping.txt")) {
			return new Retrace(reader);
		}
	}

	private BufferedReader readResource(String resource) {
		InputStream resourceAsStream = getClass().getResourceAsStream(resource);
		return new BufferedReader(new InputStreamReader(resourceAsStream));
	}

}

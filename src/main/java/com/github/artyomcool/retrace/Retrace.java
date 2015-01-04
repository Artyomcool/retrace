package com.github.artyomcool.retrace;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Retrace {

	private final Map<String, ClassMapping> classes = new HashMap<>();
	private final StackTraceAnalyzer analyzer;

	public Retrace(BufferedReader mappings) throws IOException {
		String line = mappings.readLine();
		ClassMapping currentClass = null;
		while (line != null) {
			boolean isNewClass = currentClass == null || !line.startsWith(" ");
			if (isNewClass) {
				if (currentClass != null) {
					classes.put(currentClass.getObfuscatedName(), currentClass);
				}
				currentClass = new ClassMapping(line);
			} else {
				currentClass.addLine(line);
			}
			line = mappings.readLine();
		}
		if (currentClass != null) {
			classes.put(currentClass.getObfuscatedName(), currentClass);
		}
		analyzer = new StackTraceAnalyzer(classes);
	}

	public ClassMapping getClass(String className) {
		return classes.get(className);
	}

	public String stacktrace(BufferedReader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		for(;;) {
			String line = reader.readLine();
			if (line == null) {
				return builder.toString();
			}
			analyzer.appendLine(builder, line);
		}
	}
}

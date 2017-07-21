package com.ailpcs.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringPrintWriter extends PrintWriter {
	
	public StringPrintWriter() {
		super(new StringWriter());
	}
	
	public StringPrintWriter(int initalSize) {
		super(new StringWriter(initalSize));
	}
	
	public String getString() {
		flush();
		return ((StringWriter) this.out).toString();
	}
	
	@Override
	public String toString(){
		return getString();
	}
 
	
	
}

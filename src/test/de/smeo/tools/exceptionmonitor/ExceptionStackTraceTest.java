package de.smeo.tools.exceptionmonitor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class ExceptionStackTraceTest {
	private final String STACK_TRACE_SAMPLE = "at sun.reflect.GeneratedMethodAccessor80.invoke(Unknown Source)\n" + 
			"at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"at java.lang.Thread.run(Thread.java:662)\n"; 
		
	@Test
	public void testSourcePathExctraction() {
		String[] expectedSourcePath = new String[]{
				"Unknown Source",
				"DelegatingMethodAccessorImpl.java:25",
				"Method.java:597",
				"MessageDispatcher.java:406",
				"MessageDispatcher.java:294",
				"MessageDispatcher.java:55",
				"MessageDispatcher.java:155",
				"RunnableWithTimings.java:81",
				"ThreadPoolExecutor.java:886",
				"ThreadPoolExecutor.java:908",
				"Thread.java:662"
		};

		ExceptionStackTrace exceptionStackTrace = createExceptionStackTrace(STACK_TRACE_SAMPLE);
		List<String> sourcePath = exceptionStackTrace.getSourcePath();
		
		
		assertEquals(expectedSourcePath.length, sourcePath.size());
		
		for (int i=0; i < sourcePath.size(); i++){
			assertEquals(expectedSourcePath[i], sourcePath.get(i));
		}
	}


	private ExceptionStackTrace createExceptionStackTrace(
			String stackTraceSample) {
		String[] splittedSample = STACK_TRACE_SAMPLE.split("\n");
		ExceptionStackTrace exceptionStackTrace = new ExceptionStackTrace();
		for (String currSampleLine : splittedSample){
			exceptionStackTrace.addLine(currSampleLine);
		}
		return exceptionStackTrace;
	}

}

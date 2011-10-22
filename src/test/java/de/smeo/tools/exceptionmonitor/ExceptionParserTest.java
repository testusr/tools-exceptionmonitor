package de.smeo.tools.exceptionmonitor;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class ExceptionParserTest {
	private static final String EXCEPTION_SAMPLE1 = "" +
			"ERROR [13.10.11 05:44:19.449] MessageDispatcher .ExecutionRunnable.run Error executing command.\n" + 
			"    |-| AnyId=ausv6v-a0350-gtpbq26u-cdz | OrderReferenceId=SO-13642970-R1 | ProviderOrderReferenceId=SO-13642970-R1\n" + 
			"    |-| exception=com.three60t.tex.app.exceptions.processing.ProcessingException: Cannot invoke method in object\n" + 
			"    |-| callerVM=configservice | nanos=5992342220894010 | threadId=7803 | threadName=MessageDispatcher - CachingCompanyLimitService-ING Vysya-Receiver-thread-1\n" + 
			"      | timestamp=1318484659449 | vm=sep2node2 |-| layer=App |-| [SSSOSllSlS]\n" + 
			"com.three60t.tex.app.exceptions.processing.ProcessingException: Cannot invoke method in object\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:437)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"        at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"        at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"        at java.lang.Thread.run(Thread.java:662)\n" + 
			"Caused by: java.lang.reflect.InvocationTargetException\n" + 
			"        at sun.reflect.GeneratedMethodAccessor94.invoke(Unknown Source)\n" + 
			"        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"        at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"        ... 7 more\n" + 
			"Caused by: java.lang.IllegalArgumentException: no cached limit change found for ausv6v-a0350-gtpbq26u-cdz\n" + 
			"        at org.apache.commons.lang.Validate.isTrue(Validate.java:136)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.TradingLimitChangeCache.remove(TradingLimitChangeCache.java:59)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.setCompanyTradingLimitConfig(CachingCompanyLimitService.java:534)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.limitsChanged(CachingCompanyLimitService.java:578)\n" + 
			"        ... 11 more";
	
	@Test
	public void testExceptionChainCreation() {
		ExceptionParser exceptionParser = new ExceptionParser();
		exceptionParser.parseAndFlush(EXCEPTION_SAMPLE1);

		List<ExceptionCausedByChain> exceptionChains = exceptionParser.getExceptionChains();
		assertEquals(1, exceptionChains.size());
		
		ExceptionCausedByChain exceptionCausedByChain = exceptionChains.get(0);
		assertEquals(3, exceptionCausedByChain.getExceptions().size());
		
		List<LoggedException> exceptions = exceptionCausedByChain.getExceptions();

		assertEquals(exceptions.get(0).getExceptionClassName(),"com.three60t.tex.app.exceptions.processing.ProcessingException");
		assertEquals(exceptions.get(1).getExceptionClassName(), "java.lang.reflect.InvocationTargetException");
		assertEquals(exceptions.get(2).getExceptionClassName(), "java.lang.IllegalArgumentException");
	}

	@Test
	public void testExceptionCreation(){
		ExceptionParser exceptionParser = new ExceptionParser();
		exceptionParser.parseAndFlush(EXCEPTION_SAMPLE1);
		
		List<LoggedException> loggedExceptions = exceptionParser.getExceptions();
		assertEquals(3, loggedExceptions.size());
		assertEquals("com.three60t.tex.app.exceptions.processing.ProcessingException", loggedExceptions.get(0).getExceptionClassName());
		assertEquals("java.lang.reflect.InvocationTargetException", loggedExceptions.get(1).getExceptionClassName());
		assertEquals("java.lang.IllegalArgumentException", loggedExceptions.get(2).getExceptionClassName());

	} 
	
	@Test
	public void testFirstLineMarksStartOfExceptionTrace(){
		String firstLine = "com.three60t.tex.app.exceptions.processing.ProcessingException: Cannot invoke method in object";
		String secondLine = "at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:437)";
		
		ExceptionParser exceptionParser = new ExceptionParser();
		assertTrue(exceptionParser.firstLineMarksStartOfExceptionTrace(firstLine, secondLine));
	}
	
	@Test
	public void testFirstLineIstStartsCausedByExceptionTrace(){
		String firstLine = "Caused by: java.lang.reflect.InvocationTargetException"; 
		String secondLine =	"at sun.reflect.GeneratedMethodAccessor94.invoke(Unknown Source)"; 

		ExceptionParser exceptionParser = new ExceptionParser();
		assertTrue(exceptionParser.firstLineIstStartsCausedByExceptionTrace(firstLine, secondLine));
	}
	
	@Test
	public void testFirstLineIsPartOfCurrentException(){
		String firstLine  = "at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:437)";
		String secondLine = "at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)";
	
		ExceptionParser exceptionParser = new ExceptionParser();
		assertTrue(exceptionParser.firstLineIsPartOfCurrentException(firstLine, secondLine));
	}
	
	@Test
	public void testFirstLineIsPartOfCurrentException_firstLineCausedBy(){
		String firstLine  = "Caused by: java.lang.IllegalArgumentException: no cached limit change found for ausv6v-a0350-gtpbq26u-cdz)";
		String secondLine = "at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)";
	
		ExceptionParser exceptionParser = new ExceptionParser();
		assertTrue(!exceptionParser.firstLineIsPartOfCurrentException(firstLine, secondLine));
	}
	
	@Test
	public void testFirstLineMarksEOFExceptionTrace() {
		String firstLine = "... 11 more";
		String secondLine = "asdcysxcsdrfwtre325tgrvg";
		
		ExceptionParser exceptionParser = new ExceptionParser();
		assertTrue(exceptionParser.firstLineMarksEOFExceptionTrace(firstLine, secondLine));
	}
	

	@Test
	public void testRegExp_REGEXP_EXCEPTION_START(){
		//System.out.println(ExceptionParser.REGEXP_EXCEPTION_START);
		assertTrue(("com.three60t.tex.app.exceptions.processing.ProcessingException: Cannot invoke method in object".matches(ExceptionParser.REGEXP_EXCEPTION_START)));
	}
	@Test
	public void testRegExp_REGEXP_EXCEPTION_START_CAUSED_BY(){
		//System.out.println(ExceptionParser.REGEXP_EXCEPTION_START_CAUSED_BY);
		assertTrue(("Caused by: java.lang.IllegalArgumentException: no cached limit change found for ausv6v-a0350-gtpbq26u-cdz".matches(ExceptionParser.REGEXP_EXCEPTION_START_CAUSED_BY)));
	}
	
	@Test
	public void testRegExp_REGEXP_STACKTRACE_MEMBER_AT(){
		//System.out.println(ExceptionParser.REGEXP_STACKTRACE_MEMBER_AT);
		assertTrue(("at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:437)".matches(ExceptionParser.REGEXP_STACKTRACE_MEMBER_AT)));
		assertTrue(("at com.three60t.tex.communication.message.core.GeneralErrorMessage.<init>(GeneralErrorMessage.java:46)".matches(ExceptionParser.REGEXP_STACKTRACE_MEMBER_AT)));
		assertTrue(("at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)".matches(ExceptionParser.REGEXP_STACKTRACE_MEMBER_AT)));
		assertFalse(("at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Met234hod)".matches(ExceptionParser.REGEXP_STACKTRACE_MEMBER_AT)));
	}
	
	@Test
	public void testRegExp_REGEXP_SOURCE_LINE(){
		//System.out.println(ExceptionParser.REGEXP_SOURCE_LINE_OR_UNKNOWN);
		assertTrue(("(MessageDispatcher.java:437)".matches(ExceptionParser.REGEXP_SOURCE_LINE_OR_UNKNOWN)));
		assertTrue(("(Unknown Source)".matches(ExceptionParser.REGEXP_SOURCE_LINE_OR_UNKNOWN)));
	}
	
	@Test
	public void testFileParsing() throws IOException{
		String[] expectedExcepionClassNames = {
				"java.lang.reflect.InvocationTargetException",
				"com.three60t.tex.app.exceptions.processing.ProcessingException",
				"java.lang.ExceptionInInitializerError",
				"java.lang.reflect.InvocationTargetException",
				"com.three60t.tex.app.exceptions.processing.ProcessingException",
				"java.lang.RuntimeException"
		};
		
		LogFileExceptionParser logFileExceptionParser = new LogFileExceptionParser();
		logFileExceptionParser.parseFile("resources/exceptionsamples.txt");
		
		List<ExceptionCausedByChain> exceptionChains = logFileExceptionParser.getExceptionChains();
		assertEquals(6, exceptionChains.size());
		
		for (int i = 0; i < expectedExcepionClassNames.length; i++){
			ExceptionCausedByChain currExceptionChain = exceptionChains.get(i);
			assertEquals(expectedExcepionClassNames[i], currExceptionChain.getExceptions().get(0).getExceptionClassName());
		}
	}
}

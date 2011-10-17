package de.smeo.tools.exceptionmonitor;

import static org.junit.Assert.*;

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
		exceptionParser.parse(EXCEPTION_SAMPLE1);
				
		List<ExceptionChain> exceptionChains = exceptionParser.getExceptionChains();
		assertEquals(exceptionChains.size(), 1);
	}

	public void testExceptionCreation(){
		ExceptionParser exceptionParser = new ExceptionParser();
		exceptionParser.parse(EXCEPTION_SAMPLE1);
		
		List<LoggedException> exceptions = exceptionParser.getExceptions();

		assertEquals(exceptions.get(0).getExceptionClassName(),"com.three60t.tex.app.exceptions.processing.ProcessingException");
		assertEquals(exceptions.get(1).getExceptionClassName(), "java.lang.reflect.InvocationTargetException");
		assertEquals(exceptions.get(2).getExceptionClassName(), "java.lang.IllegalArgumentException");
	} 
}

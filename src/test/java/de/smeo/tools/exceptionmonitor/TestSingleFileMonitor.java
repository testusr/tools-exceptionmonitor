package de.smeo.tools.exceptionmonitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Test;


public class TestSingleFileMonitor {
	private static String EXCEPTION1_ROOTCAUSE_1 = "java.lang.reflect.InvocationTargetException\n" + 
			"	at sun.reflect.GeneratedMethodAccessor80.invoke(Unknown Source)\n" + 
			"	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"	at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"	at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"	at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"	at java.lang.Thread.run(Thread.java:662)\n" + 
			"Caused by: java.lang.IllegalArgumentException: no cached limit change found for dwcs4r-a036f-gtwa2fib-fep\n" + 
			"	at org.apache.commons.lang.Validate.isTrue(Validate.java:136)\n" + 
			"	at com.three60t.tex.service.sep2.marketdatapool.TradingLimitChangeCache.remove(TradingLimitChangeCache.java:59)\n" + 
			"	at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.setCompanyTradingLimitConfig(CachingCompanyLimitService.java:535)\n" + 
			"	at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.limitsChanged(CachingCompanyLimitService.java:579)\n" + 
			"	... 11 more\n" +
			" ";
	private static String EXCEPTION2_ROOTCAUSE_1 = "ERROR [18.10.11 02:36:42.938] MessageDispatcher . ProcessingException\n" + 
			"    |-| AnyId=dchxsz-a036f-gtwa81v2-fob | OrderReferenceId=SO-13700897-S1 | ProviderOrderReferenceId=SO-13700897-S1\n" + 
			"    |-| exception=java.lang.reflect.InvocationTargetException\n" + 
			"    |-| callerVM=configservice | nanos=6413085709878010 | threadId=14654\n" + 
			"      | threadName=MessageDispatcher - CachingCompanyLimitService-Bank of Nova Scotia TOR-Receiver-thread-1 | timestamp=1318905402938 | vm=sep2node2\n" + 
			"    |-| layer=App | domain=class |-| [SSSOSllSlS]\n" + 
			"java.lang.reflect.InvocationTargetException\n" + 
			"	at sun.reflect.GeneratedMethodAccessor80.invoke(Unknown Source)\n" + 
			"	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"	at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"	at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"	at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"	at java.lang.Thread.run(Thread.java:662)\n" + 
			"Caused by: java.lang.IllegalArgumentException: no cached limit change found for dchxsz-a036f-gtwa81v2-fob\n" + 
			"	at org.apache.commons.lang.Validate.isTrue(Validate.java:136)\n" + 
			"	at com.three60t.tex.service.sep2.marketdatapool.TradingLimitChangeCache.remove(TradingLimitChangeCache.java:59)\n" + 
			"	at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.setCompanyTradingLimitConfig(CachingCompanyLimitService.java:535)\n" + 
			"	at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.limitsChanged(CachingCompanyLimitService.java:579)\n" + 
			"	... 11 more\n" +
			" ";
	
	private static String EXCEPTION3_ROOTCAUSE_2 = "java.lang.reflect.InvocationTargetException\n" + 
			"	at sun.reflect.GeneratedMethodAccessor72.invoke(Unknown Source)\n" + 
			"	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"	at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"	at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"	at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"	at java.lang.Thread.run(Thread.java:662)\n" + 
			"Caused by: java.lang.NoSuchFieldError: WARMUP_PROVIDER_INSTITUTION\n" + 
			"	at com.three60t.tex.service.sep2.bankbasket.ProviderManagerImpl.getProvidersForUser(ProviderManagerImpl.java:74)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.PassiveMarketDataStreamHandler.subscribe(PassiveMarketDataStreamHandler.java:237)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.PassiveMarketDataStreamManager.createAndStartStreamHandler(PassiveMarketDataStreamManager.java:33)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.MarketDataStreamManager.createAndStartStreamHandler(MarketDataStreamManager.java:362)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.MarketDataStreamManager.addMarketDataStreamRequest(MarketDataStreamManager.java:108)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.subscribe(GUIClientGatewayImpl.java:159)\n" + 
			"	... 11 more\n" +
			" ";
	
	private static String ONE_AND_A_HALF_EXCEPTIONS = "ERROR [13.10.11 05:44:19.449] MessageDispatcher . ProcessingException\n" + 
			"|-| AnyId=ausv6v-a0350-gtpbq26u-cdz | OrderReferenceId=SO-13642970-R1 | ProviderOrderReferenceId=SO-13642970-R1\n" + 
			"|-| exception=java.lang.reflect.InvocationTargetException\n" + 
			"|-| callerVM=configservice | nanos=5992342220569010 | threadId=7803 | threadName=MessageDispatcher - CachingCompanyLimitService-ING Vysya-Receiver-thread-1\n" + 
			"  | timestamp=1318484659449 | vm=sep2node2 |-| layer=App | domain=class |-| [SSSOSllSlS]\n" + 
			"java.lang.reflect.InvocationTargetException\n" + 
			"    at sun.reflect.GeneratedMethodAccessor94.invoke(Unknown Source)\n" + 
			"    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"    at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"    at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"    at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"    at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"    at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"    at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"    at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"    at java.lang.Thread.run(Thread.java:662)\n" + 
			"Caused by: java.lang.IllegalArgumentException: no cached limit change found for ausv6v-a0350-gtpbq26u-cdz\n" + 
			"    at org.apache.commons.lang.Validate.isTrue(Validate.java:136)\n" + 
			"    at com.three60t.tex.service.sep2.marketdatapool.TradingLimitChangeCache.remove(TradingLimitChangeCache.java:59)\n" + 
			"    at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.setCompanyTradingLimitConfig(CachingCompanyLimitService.java:534)\n" + 
			"    at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.limitsChanged(CachingCompanyLimitService.java:578)\n" + 
			"    ... 11 more\n" + 
			"ERROR [13.10.11 05:44:19.449] MessageDispatcher .ExecutionRunnable.run Error executing command.\n" + 
			"|-| AnyId=ausv6v-a0350-gtpbq26u-cdz | OrderReferenceId=SO-13642970-R1 | ProviderOrderReferenceId=SO-13642970-R1\n" + 
			"|-| exception=com.three60t.tex.app.exceptions.processing.ProcessingException: Cannot invoke method in object\n" + 
			"|-| callerVM=configservice | nanos=5992342220894010 | threadId=7803 | threadName=MessageDispatcher - CachingCompanyLimitService-ING Vysya-Receiver-thread-1\n" + 
			"  | timestamp=1318484659449 | vm=sep2node2 |-| layer=App |-| [SSSOSllSlS]\n" + 
			"com.three60t.tex.app.exceptions.processing.ProcessingException: Cannot invoke method in object\n" + 
			"    at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:437)\n" + 
			"    at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"    at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"    at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"    at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"    at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"    at java.lang.Thread.run(Thread.java:662)";

	private static String SECOND_HALF = "Caused by: java.lang.reflect.InvocationTargetException\n" + 
			"    at sun.reflect.GeneratedMethodAccessor94.invoke(Unknown Source)\n" + 
			"    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"    at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"    at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"    ... 7 more\n" + 
			"Caused by: java.lang.IllegalArgumentException: no cached limit change found for ausv6v-a0350-gtpbq26u-cdz\n" + 
			"    at org.apache.commons.lang.Validate.isTrue(Validate.java:136)\n" + 
			"    at com.three60t.tex.service.sep2.marketdatapool.TradingLimitChangeCache.remove(TradingLimitChangeCache.java:59)\n" + 
			"    at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.setCompanyTradingLimitConfig(CachingCompanyLimitService.java:534)\n" + 
			"    at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.limitsChanged(CachingCompanyLimitService.java:578)\n" + 
			"    ... 11 more\n" + 
			"\n" + 
			"FATAL [13.10.11 20:59:55.156] FatalLogger .uncaughtException() The following throwable was not caught anywhere in the thread Thread-44\n" + 
			"|-|  |-| exception=java.lang.ExceptionInInitializerError\n" + 
			"|-| nanos=6047277927453010 | threadId=143 | threadName=Thread-44 | timestamp=1318539595156 | vm=sep2node2 |-| layer=App |-| [OllSlS]\n" + 
			"java.lang.ExceptionInInitializerError\n" + 
			"    at org.eclipse.jetty.server.Server.doStop(Server.java:315)\n" + 
			"    at org.eclipse.jetty.util.component.AbstractLifeCycle.stop(AbstractLifeCycle.java:80)\n" + 
			"    at com.three60t.framework.communication.hornetq.server.HornetQHttpContainer$2.run(HornetQHttpContainer.java:137)\n" + 
			"Caused by: java.lang.IllegalStateException: Shutdown in progress\n" + 
			"    at java.lang.ApplicationShutdownHooks.add(ApplicationShutdownHooks.java:39)\n" + 
			"    at java.lang.Runtime.addShutdownHook(Runtime.java:192)\n" + 
			"    at org.eclipse.jetty.util.thread.ShutdownThread.<init>(ShutdownThread.java:48)\n" + 
			"    at org.eclipse.jetty.util.thread.ShutdownThread.<clinit>(ShutdownThread.java:36)\n" + 
			"    ... 3 more\n" +
			" ";
	
	private final long checkIntervalInMs = 5000;

	
	@Test
	public void testRunCheckIfNecessary() {
		SingleFileMonitorTestFixture singleFileMonitorTestFixture = new SingleFileMonitorTestFixture(checkIntervalInMs);
		long currentTime = System.currentTimeMillis();
		
		singleFileMonitorTestFixture.setTime(currentTime);
		assertTrue(singleFileMonitorTestFixture.getLastFileCheckTime() < 0);
		
		singleFileMonitorTestFixture.checkFileIfNecessary();
		assertEquals(singleFileMonitorTestFixture.getLastFileCheckTime(), currentTime);

		singleFileMonitorTestFixture.checkFileIfNecessary();
		assertEquals(singleFileMonitorTestFixture.getLastFileCheckTime(), currentTime);

		singleFileMonitorTestFixture.setTime(currentTime + checkIntervalInMs);
		
		singleFileMonitorTestFixture.checkFileIfNecessary();
		assertEquals(singleFileMonitorTestFixture.getLastFileCheckTime(), (currentTime + checkIntervalInMs));
		
	}
	
	@Test
	public void testReadingExceptionWithinTwoFileChecks(){
		SingleFileMonitorTestFixture singleFileMonitorTestFixture = new SingleFileMonitorTestFixture(checkIntervalInMs);

		singleFileMonitorTestFixture.writeToFile(ONE_AND_A_HALF_EXCEPTIONS);
		List<ExceptionCausedByChain> loggedExceptionChains = singleFileMonitorTestFixture.checkFile();
		
		loggedExceptionChains = singleFileMonitorTestFixture.checkFile();
		assertEquals(0, loggedExceptionChains.size());

		singleFileMonitorTestFixture.writeToFile(SECOND_HALF);
		loggedExceptionChains = singleFileMonitorTestFixture.checkFile();
		assertEquals(2, loggedExceptionChains.size());
	}
	
	@Test
	public void testTwoExceptionsWithSameRootCauseInTwoFileChecks(){
		SingleFileMonitorTestFixture singleFileMonitorTestFixture = new SingleFileMonitorTestFixture(-1);

		singleFileMonitorTestFixture.writeToFile(EXCEPTION1_ROOTCAUSE_1);

		SingleFileExceptionReport singleFileExceptionReport = singleFileMonitorTestFixture.getExecutionReportAndSeparateExceptions();
		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().size());
		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().get(0).size());

		singleFileMonitorTestFixture.writeToFile(EXCEPTION2_ROOTCAUSE_1);
		singleFileExceptionReport = singleFileMonitorTestFixture.getExecutionReportAndSeparateExceptions();

		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().size());
		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().get(0).size());
		
	}

	@Test
	public void testTwoExceptionsWithSameRootCauseInOneFileCheck(){
		SingleFileMonitorTestFixture singleFileMonitorTestFixture = new SingleFileMonitorTestFixture(-1);

		singleFileMonitorTestFixture.writeToFile(EXCEPTION1_ROOTCAUSE_1);
		singleFileMonitorTestFixture.writeToFile(EXCEPTION2_ROOTCAUSE_1);

		SingleFileExceptionReport singleFileExceptionReport = singleFileMonitorTestFixture.getExecutionReportAndSeparateExceptions();
		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().size());
		assertEquals(2, singleFileExceptionReport.getUnkownExceptions().get(0).size());
		
		
	}

	@Test 
	public void testTwoExceptionsWithDifferentRootCauseInTwoFileChecks(){
		SingleFileMonitorTestFixture singleFileMonitorTestFixture = new SingleFileMonitorTestFixture(-1);

		singleFileMonitorTestFixture.writeToFile(EXCEPTION2_ROOTCAUSE_1);
		
		SingleFileExceptionReport singleFileExceptionReport = singleFileMonitorTestFixture.getExecutionReportAndSeparateExceptions();
		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().size());
		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().get(0).size());
		
		singleFileMonitorTestFixture.writeToFile(EXCEPTION3_ROOTCAUSE_2);

		singleFileExceptionReport = singleFileMonitorTestFixture.getExecutionReportAndSeparateExceptions();
		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().size());
		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().get(0).size());
	}

	@Test 
	public void testTwoExceptionsWithDifferentRootCauseInOneFileChecks(){
		SingleFileMonitorTestFixture singleFileMonitorTestFixture = new SingleFileMonitorTestFixture(-1);

		singleFileMonitorTestFixture.writeToFile(EXCEPTION2_ROOTCAUSE_1);
		singleFileMonitorTestFixture.writeToFile(EXCEPTION3_ROOTCAUSE_2);

		SingleFileExceptionReport singleFileExceptionReport = singleFileMonitorTestFixture.getExecutionReportAndSeparateExceptions();
		
		assertEquals(2, singleFileExceptionReport.getUnkownExceptions().size());
		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().get(0).size());
		assertEquals(1, singleFileExceptionReport.getUnkownExceptions().get(1).size());
	}

	private class SingleFileMonitorTestFixture extends SingleFileMonitor {
		private long time = System.currentTimeMillis();
		private StringBuffer unreadFileContent = new StringBuffer();
		
		
		public SingleFileMonitorTestFixture(long checkIntervalInMs) {
			super(new MonitoredFile(checkIntervalInMs, "dummy.log"));
		}

		public void writeToFile(String logFileContent) {
			unreadFileContent.append(logFileContent);
		}

		public void setTime(long time) {
			this.time = time;
		}
		
		private SingleFileExceptionReport getExecutionReportAndSeparateExceptions() {
			checkFileIfNecessary();
			SingleFileExceptionReport singleFileExceptionReport = getExceptionsSinceLastUpdateAndReset();
			singleFileExceptionReport.separateSightedUnsightedAndReturnUnkownExceptions(Collections.EMPTY_LIST);
			return singleFileExceptionReport;
		}
		
		@Override
		protected String getNextLogFileChunk() {
			if (unreadFileContent.length() == 0){
				return null;
		}						

			StringBuffer nextUnreadChunk = unreadFileContent;
			unreadFileContent = new StringBuffer();
			return nextUnreadChunk.toString();
		}
		
		@Override
		protected long getCurrTime(){
			return time;
		}
		

	}

}

package de.smeo.tools.exceptionmonitor;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class MultiFileMonitorTest {
	
	private static final String SEPECIFIC_EMAIL = "receiver@homegrow.de";
	private static final String DEFAULT_EMAIL = "smeo@homegrow.de";
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	private static AtomicInteger tempLogFileCounter = new AtomicInteger(0);



	
	private static String EXCEPTION1_ROOTCAUSE1 = "java.lang.reflect.InvocationTargetException\n" + 
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
	
	private static final String EXCEPTION2_ROOTCAUSE1 = "java.lang.reflect.InvocationTargetException\n" + 
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
	private static final String EXCEPTION3_ROOTCAUSE2 = "java.lang.reflect.InvocationTargetException\n" + 
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

//	@Test
//	public void testReportedExceptionWithSameRootCauseAreEqual(){
//		ExceptionParser exceptionParser = new ExceptionParser();
//		exceptionParser.parse(EXCEPTION1_ROOTCAUSE1);
//		ExceptionCausedByChain exceptionCausedByChain = exceptionParser.getExceptionChains().get(0);
//		ReportedException reportedException = new ReportedException(exceptionCausedByChain);
//
//		ExceptionParser exceptionParser2 = new ExceptionParser();
//		exceptionParser2.parse(EXCEPTION2_ROOTCAUSE1);
//		ExceptionCausedByChain exceptionCausedByChain2 = exceptionParser.getExceptionChains().get(0);
//		ReportedException reportedException2 = new ReportedException(exceptionCausedByChain2);
//
//		assertTrue(reportedException.hasEqualRootCause(reportedException2));
//	}
//	
//
//	@Test
//	public void testReportedExceptionWithdiffRootCauseAreNotEqual(){
//		ExceptionParser exceptionParser = new ExceptionParser();
//		exceptionParser.parse(EXCEPTION1_ROOTCAUSE1);
//		ExceptionCausedByChain exceptionCausedByChain = exceptionParser.getExceptionChains().get(0);
//		ReportedException reportedException = new ReportedException(exceptionCausedByChain);
//
//		ExceptionParser exceptionParser2 = new ExceptionParser();
//		exceptionParser2.parse(EXCEPTION3_ROOTCAUSE2);
//		ExceptionCausedByChain exceptionCausedByChain2 = exceptionParser.getExceptionChains().get(0);
//		ReportedException reportedException2 = new ReportedException(exceptionCausedByChain2);
//
//		assertTrue(reportedException.hasEqualRootCause(reportedException2));
//	}

	
}

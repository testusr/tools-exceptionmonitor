package de.smeo.tools.exceptionmonitor;

import static org.junit.Assert.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.smeo.tools.exceptionmonitor.reporting.MonitoredFile;
import de.smeo.tools.exceptionmonitor.reporting.SingleFileExceptionReport;



public class FileMonitorTest {

	@Rule
	public static TemporaryFolder tempFolder = new TemporaryFolder();
	private static AtomicInteger tempLogFileCounter = new AtomicInteger(0);

	private static final String EXCEPTION1_ROOTCAUSE1 = "java.lang.reflect.InvocationTargetException\n" + 
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

	public static File createTempFile(String fileName) {
		try {
			return tempFolder.newFile(fileName + tempLogFileCounter.incrementAndGet() + ".log");
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Test
	public void testExcpetionInLogFile() {
		FileMonitorFixture fileMonitor = new FileMonitorFixture();
		fileMonitor.init();
		
		fileMonitor.createReport();
		fileMonitor.verifyReportIsEmpty();
		
		fileMonitor.appendToLogFileA(EXCEPTION1_ROOTCAUSE1);
		
		fileMonitor.appendToLogFileB(EXCEPTION1_ROOTCAUSE1);
		fileMonitor.appendToLogFileB(EXCEPTION2_ROOTCAUSE1);

		fileMonitor.createReport();
		fileMonitor.verifyReportContainsFileWithNoOfExceptions(fileMonitor.getLogFilenNameA(), 1);
		fileMonitor.verifyReportContainsFileWithNoOfExceptions(fileMonitor.getLogFilenNameB(), 2);
		fileMonitor.verifyReportContainsFileWithNoOfUnknownRootCauses(fileMonitor.getLogFilenNameA(), 1);
		fileMonitor.verifyReportContainsFileWithNoOfUnknownRootCauses(fileMonitor.getLogFilenNameB(), 1);
		fileMonitor.verifyReportContainsFileWithDoesNotContainLogFile(fileMonitor.getLogFilenNameC());
	}



	@Test
	public void testReportUpdates(){
		FileMonitorFixture fileMonitor = new FileMonitorFixture();
		fileMonitor.init();
		
		fileMonitor.appendToLogFileA(EXCEPTION1_ROOTCAUSE1);
		fileMonitor.appendToLogFileB(EXCEPTION1_ROOTCAUSE1);
		fileMonitor.createReport();
		
		fileMonitor.appendToLogFileB(EXCEPTION2_ROOTCAUSE1);
		fileMonitor.appendToLogFileB(EXCEPTION3_ROOTCAUSE2);
		fileMonitor.appendToLogFileC(EXCEPTION2_ROOTCAUSE1);
		fileMonitor.createReport();

		fileMonitor.verifyReportContainsFileWithNoOfExceptions(fileMonitor.getLogFilenNameB(), 2);
		fileMonitor.verifyReportContainsFileWithNoOfExceptions(fileMonitor.getLogFilenNameC(), 1);
	
		fileMonitor.verifyReportContainsFileWithNoOfUnknownRootCauses(fileMonitor.getLogFilenNameC(), 1);
		fileMonitor.verifyReportContainsFileWithNoOfUnknownRootCauses(fileMonitor.getLogFilenNameB(), 1);
		
		fileMonitor.verifyReportContainsFileWithNoOfUnSightedRootCauses(fileMonitor.getLogFilenNameB(), 1);
		

	}
	
	@Test
	public void testReportUpdatesWithRollingFiles(){
		FileMonitorFixture fileMonitor = new FileMonitorFixture();
		fileMonitor.init();
		
		fileMonitor.appendToLogFileB(EXCEPTION1_ROOTCAUSE1);
		fileMonitor.createReport();
		
		fileMonitor.reCreateLogFileB();
		fileMonitor.appendToLogFileB(EXCEPTION2_ROOTCAUSE1);
		fileMonitor.appendToLogFileB(EXCEPTION3_ROOTCAUSE2);
		fileMonitor.createReport();

		fileMonitor.verifyReportContainsFileWithNoOfExceptions(fileMonitor.getLogFilenNameB(), 2);
		fileMonitor.verifyReportContainsFileWithNoOfUnknownRootCauses(fileMonitor.getLogFilenNameB(), 1);
	}
	
	public class FileMonitorFixture extends MultiFileMonitor {
		private File logFileA = createTempFile("logFileA");
		private File logFileB = createTempFile("logFileB");
		private File logFileC = createTempFile("logFileC");
		
		private MultiFileExceptionReport multiFileExceptionReport;

		public void init(){
			addMonitoredLogFile(new MonitoredFile(0, getLogFilenNameA()));
			addMonitoredLogFile(new MonitoredFile(0, getLogFilenNameB()));
			addMonitoredLogFile(new MonitoredFile(0, getLogFilenNameC()));
		}

		public void reCreateLogFileB() {
			logFileB.delete();
			try {
				logFileB.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Assert.fail();
			}	
		}

		public void appendToLogFileA(String logContent) {
			appendLogFile(logFileA, logContent);
		}

		public void appendToLogFileB(String logContent) {
			appendLogFile(logFileB, logContent);			
		}

		public void appendToLogFileC(String logContent) {
			appendLogFile(logFileC, logContent);			
		}

		private void appendLogFile(File logFile, String logFileContent) {
			if (logFileContent != null){
		        BufferedWriter out;
				try {
					out = new BufferedWriter(new FileWriter(logFile, true));
					out.write(logFileContent);
					out.close();
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}

		public void createReport() {
			checkFilesIfNecessary();
			this.multiFileExceptionReport = createExceptionReportUpdate();
		}

		public String getLogFilenNameA() {
			return logFileA.getAbsolutePath();
		}

		public String getLogFilenNameB() {
			return logFileB.getAbsolutePath();
		}

		public String getLogFilenNameC() {
			return logFileC.getAbsolutePath();
		}

		public void verifyReportContainsFileWithDoesNotContainLogFile(
				String logFilenName) {
			SingleFileExceptionReport singleFileExceptionReport = multiFileExceptionReport.getSingleFileReportForFilename(logFilenName);
			assertNull(singleFileExceptionReport);
		}

		public void verifyReportContainsFileWithNoOfExceptions(
				String logFilenName, int expectedNoOfExceptions) {
			SingleFileExceptionReport singleFileExceptionReport = multiFileExceptionReport.getSingleFileReportForFilename(logFilenName);

			assertNotNull(singleFileExceptionReport);
			assertEquals(expectedNoOfExceptions, singleFileExceptionReport.getTotalNoOfExceptions());
		}

		public void verifyReportContainsFileWithNoOfUnSightedRootCauses(
				String logFilenName, int expectedNoOfSightedRootCauses) {
			SingleFileExceptionReport singleFileExceptionReport = multiFileExceptionReport.getSingleFileReportForFilename(logFilenName);

			assertNotNull(singleFileExceptionReport);
			assertEquals(expectedNoOfSightedRootCauses, singleFileExceptionReport.getUnSightedExceptions().size());
		}

		public void verifyReportContainsFileWithNoOfUnknownRootCauses(
				String logFilenName, int expectedNoOfUnkownRootCauses) {
			SingleFileExceptionReport singleFileExceptionReport = multiFileExceptionReport.getSingleFileReportForFilename(logFilenName);

			assertNotNull(singleFileExceptionReport);
			assertEquals(expectedNoOfUnkownRootCauses, singleFileExceptionReport.getUnkownExceptions().size());
		}
		
		

		public void verifyReportIsEmpty() {
			assertTrue(multiFileExceptionReport.isEmpty());
		}

	}
}

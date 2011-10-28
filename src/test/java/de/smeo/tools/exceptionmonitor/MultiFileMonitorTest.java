package de.smeo.tools.exceptionmonitor;

import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.smeo.tools.exceptionmonitor.EmailOutBox.ExceptionReportEmail;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionParser;

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

	@Test
	public void testNoExceptionNoEmail() {
		MultiFileMonitorTestFixture testFixture = new MultiFileMonitorTestFixture();
		testFixture.setDefaultEmailForFileA(DEFAULT_EMAIL);
		testFixture.checkFiles();
		testFixture.verifyNoEmailSend();
	}

	@Test
	public void testSendEmailToDefaultReceiver() {
		MultiFileMonitorTestFixture testFixture = new MultiFileMonitorTestFixture();
		testFixture.setDefaultEmailForFileA(DEFAULT_EMAIL);
		testFixture.appendFileA(EXCEPTION1_ROOTCAUSE1);
		testFixture.checkFiles();
		testFixture.verifyEmailSendTo(DEFAULT_EMAIL, EXCEPTION1_ROOTCAUSE1);
	}

	@Test
	public void testSendEmailToSpecificReceiver(){
		MultiFileMonitorTestFixture testFixture = new MultiFileMonitorTestFixture();
		testFixture.setDefaultEmailForFileA(DEFAULT_EMAIL);
		testFixture.setReceiverListFileA(EXCEPTION1_ROOTCAUSE1, SEPECIFIC_EMAIL);
		testFixture.appendFileA(EXCEPTION1_ROOTCAUSE1);
		testFixture.checkFiles();
		testFixture.verifyEmailSendTo(SEPECIFIC_EMAIL, EXCEPTION1_ROOTCAUSE1);
	}
	
	@Test
	public void testSendNoEmailForIgnoredException(){
		MultiFileMonitorTestFixture testFixture = new MultiFileMonitorTestFixture();
		testFixture.setDefaultEmailForFileA(DEFAULT_EMAIL);
		
		testFixture.appendFileA(EXCEPTION1_ROOTCAUSE1);
		testFixture.ignoreExceptions(EXCEPTION1_ROOTCAUSE1);
		
		testFixture.checkFiles();
		testFixture.verifyNoEmailSend();
	}
	
	@Test
	public void testSendExceptionOnlyOnce(){
		MultiFileMonitorTestFixture testFixture = new MultiFileMonitorTestFixture();
		testFixture.setDefaultEmailForFileA(DEFAULT_EMAIL);
		testFixture.setReceiverListFileA(EXCEPTION1_ROOTCAUSE1, SEPECIFIC_EMAIL);
		testFixture.appendFileA(EXCEPTION1_ROOTCAUSE1);
		testFixture.checkFiles();
		testFixture.checkFiles();
		testFixture.verifyNoEmailSend();
		
	}
	
	@Test
	public void testReportedExceptionWithSameRootCauseAreEqual(){
		ExceptionParser exceptionParser = new ExceptionParser();
		exceptionParser.parse(EXCEPTION1_ROOTCAUSE1);
		ExceptionCausedByChain exceptionCausedByChain = exceptionParser.getExceptionChains().get(0);
		ReportedException reportedException = new ReportedException(exceptionCausedByChain);

		ExceptionParser exceptionParser2 = new ExceptionParser();
		exceptionParser2.parse(EXCEPTION2_ROOTCAUSE1);
		ExceptionCausedByChain exceptionCausedByChain2 = exceptionParser.getExceptionChains().get(0);
		ReportedException reportedException2 = new ReportedException(exceptionCausedByChain2);

		assertTrue(reportedException.hasEqualRootCause(reportedException2));
	}
	

	@Test
	public void testReportedExceptionWithdiffRootCauseAreNotEqual(){
		ExceptionParser exceptionParser = new ExceptionParser();
		exceptionParser.parse(EXCEPTION1_ROOTCAUSE1);
		ExceptionCausedByChain exceptionCausedByChain = exceptionParser.getExceptionChains().get(0);
		ReportedException reportedException = new ReportedException(exceptionCausedByChain);

		ExceptionParser exceptionParser2 = new ExceptionParser();
		exceptionParser2.parse(EXCEPTION3_ROOTCAUSE2);
		ExceptionCausedByChain exceptionCausedByChain2 = exceptionParser.getExceptionChains().get(0);
		ReportedException reportedException2 = new ReportedException(exceptionCausedByChain2);

		assertTrue(reportedException.hasEqualRootCause(reportedException2));
	}

	class MultiFileMonitorTestFixture extends MultiFileMonitor {
		private File fileA;
		private MonitoredFile monitoredFileA;
		private EmailOutBox lastEmailOutBox;
		
		private File createTempFile(){
			try {
				return  tempFolder.newFile("tempFilename_" + this.getClass().getSimpleName() + "_" + tempLogFileCounter.getAndIncrement() + ".log");
			} catch (IOException e) {
				return null;
			}
		}
		


		public MultiFileMonitorTestFixture() {
			fileA = createTempFile();
			monitoredFileA = new MonitoredFile(0, fileA.getName());
			addMonitoredLogFile(monitoredFileA);
		}
		
		public void setDefaultEmailForFileA(String emailAdress) {
			monitoredFileA.addDefaultEmailAdress(new EmailAdress(emailAdress));
			addMonitoredLogFile(monitoredFileA);
		}

		public int ignoreExceptions(String exception) {
			int exceptionIndex = addKnownException(exception);
			ReportedException reportedException = monitoredFileA.getExceptionWithIndex(exceptionIndex); 
			reportedException.setCanBeIgnored(true);
			
			return monitoredFileA.addKnownException(reportedException);
		}

		public int addKnownException(String exception) {
			ExceptionParser exceptionParser = new ExceptionParser();
			exceptionParser.parse(exception);
			ExceptionCausedByChain exceptionCausedByChain = exceptionParser.getExceptionChains().get(0);
			ReportedException reportedException = new ReportedException(exceptionCausedByChain);
			return monitoredFileA.addKnownException(reportedException);
		}
		
		public void setReceiverListFileA(String exception,
				String sepecificEmail) {
			setReceiverListFileA(addKnownException(exception), sepecificEmail);
		}

		public void setReceiverListFileA(int exceptionIndex, String emailAdress) {
			ReportedException reportedException = monitoredFileA.getExceptionWithIndex(exceptionIndex); 
			reportedException.addEmailReceiver(new EmailAdress(emailAdress));
		}

		public void appendFileA(String exception) {
			appendLogFile(fileA, exception);
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
		
		public void verifyEmailSendTo(String emailAdress,
				String exception) {
			for (ExceptionReportEmail preparedEmail : lastEmailOutBox.preparedEmails){
				if (preparedEmail.getEmailAdress().getAdressString().equals(emailAdress)){
					return;
				}
			}
			Assert.fail();
			
		}
		
		public void verifyNoEmailSend() {
			if (lastEmailOutBox != null){
				Assert.assertTrue(lastEmailOutBox.preparedEmails.size() == 0);
			}
		}

		public void checkFiles() {
			this.checkFilesIfNecessary();
		}
		
		@Override
		protected EmailOutBox getNewEmailOutBox() {
			this.lastEmailOutBox = super.getNewEmailOutBox();
			return lastEmailOutBox;
		}
		
	}
}

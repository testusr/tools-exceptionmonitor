package de.smeo.tools.exceptionmonitor.exceptionparser;

import java.util.List;

import org.junit.Test;

import de.smeo.tools.exceptionmonitor.exceptionparser.EqualCauseExceptionChainContainer;
import de.smeo.tools.exceptionmonitor.exceptionparser.EqualCauseExceptionContainerFactory;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionCausedByChain;
import de.smeo.tools.exceptionmonitor.exceptionparser.ExceptionParser;
import static org.junit.Assert.assertEquals;

public class EqualCauseExceptionContainerTest {
	
	private final String EXCEPTION_SAMPLE = 
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
			"        at sun.reflect.GeneratedMethodAccessor80.invoke(Unknown Source)\n" + 
			"        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"        at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"        ... 7 more\n" + 
			"Caused by: java.lang.IllegalArgumentException: no cached limit change found for dwcs4r-a036f-gtwa2fib-fep\n" + 
			"        at org.apache.commons.lang.Validate.isTrue(Validate.java:136)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.TradingLimitChangeCache.remove(TradingLimitChangeCache.java:59)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.setCompanyTradingLimitConfig(CachingCompanyLimitService.java:535)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.limitsChanged(CachingCompanyLimitService.java:579)\n" + 
			"        ... 11 more\n" + 
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
			"        at sun.reflect.GeneratedMethodAccessor80.invoke(Unknown Source)\n" + 
			"        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"        at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"        ... 7 more\n" + 
			"Caused by: java.lang.IllegalArgumentException: no cached limit change found for bfig72-a036f-gtwcvuhl-jeq\n" + 
			"        at org.apache.commons.lang.Validate.isTrue(Validate.java:136)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.TradingLimitChangeCache.remove(TradingLimitChangeCache.java:59)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.setCompanyTradingLimitConfig(CachingCompanyLimitService.java:535)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.limitsChanged(CachingCompanyLimitService.java:579)\n" + 
			"        ... 11 more\n" + 
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
			"        at sun.reflect.GeneratedMethodAccessor80.invoke(Unknown Source)\n" + 
			"        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"        at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"        ... 7 more\n" + 
			"Caused by: java.lang.IllegalArgumentException: no cached limit change found for x011tv-a036f-gtwd46x6-jpr\n" + 
			"        at org.apache.commons.lang.Validate.isTrue(Validate.java:136)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.TradingLimitChangeCache.remove(TradingLimitChangeCache.java:59)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.setCompanyTradingLimitConfig(CachingCompanyLimitService.java:535)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.limitsChanged(CachingCompanyLimitService.java:579)\n" + 
			"        ... 11 more\n" + 
			"";
	private final String SAMPLE2 = "java.lang.NullPointerException\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.PassiveMarketDataStreamHandler.unsubscribeAllProviders(PassiveMarketDataStreamHandler.java:337)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.PassiveMarketDataStreamHandler.stop(PassiveMarketDataStreamHandler.java:308)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.PassiveMarketDataStreamHandler.unsubscribeOnRemove(PassiveMarketDataStreamHandler.java:431)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.MarketDataStreamHandler.removeStreamRequest(MarketDataStreamHandler.java:824)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.MarketDataStreamManager.removeMarketDataStreamRequest(MarketDataStreamManager.java:306)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.unsubscribe(GUIClientGatewayImpl.java:182)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.unsubscribe(GUIClientGatewayImpl.java:170)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.unsubscribeAllSubscriptions(GUIClientGatewayImpl.java:649)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.cleanup(GUIClientGatewayImpl.java:654)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.ActiveGUIClientGateway.cleanup(ActiveGUIClientGateway.java:117)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GuiClientGatewayRegistrationHandler$RegistrationEntry.dispose(GuiClientGatewayRegistrationHandler.java:121)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GuiClientGatewayRegistrationHandler.removeRegistrationEntry(GuiClientGatewayRegistrationHandler.java:176)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GuiClientGatewayRegistrationHandler.deregister(GuiClientGatewayRegistrationHandler.java:167)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.ClientGatewayManager.deregister(ClientGatewayManager.java:154)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.ClientGatewayManager.notifyStreamClientRemoved(ClientGatewayManager.java:150)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.SepGuiClientServiceListener.sepGuiClientServiceRemoved(SepGuiClientServiceListener.java:113)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.SepGuiClientServiceListener.serviceRemoved(SepGuiClientServiceListener.java:73)\n" + 
			"	at sun.reflect.GeneratedMethodAccessor85.invoke(Unknown Source)\n" + 
			"	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"	at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher$2.run(MessageDispatcher.java:208)\n" + 
			"	at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"	at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"	at java.lang.Thread.run(Thread.java:662)\n" + 
			"\n" + 
			"Exception[No.546/ count: 1]\n" + 
			"java.lang.NullPointerException\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.PassiveMarketDataStreamHandler.unsubscribeAllProviders(PassiveMarketDataStreamHandler.java:337)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.PassiveMarketDataStreamHandler.stop(PassiveMarketDataStreamHandler.java:308)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.PassiveMarketDataStreamHandler.unsubscribeOnRemove(PassiveMarketDataStreamHandler.java:431)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.MarketDataStreamHandler.removeStreamRequest(MarketDataStreamHandler.java:824)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.MarketDataStreamManager.removeMarketDataStreamRequest(MarketDataStreamManager.java:306)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.unsubscribe(GUIClientGatewayImpl.java:182)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.unsubscribe(GUIClientGatewayImpl.java:170)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.unsubscribeAllSubscriptions(GUIClientGatewayImpl.java:649)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.cleanup(GUIClientGatewayImpl.java:654)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.ActiveGUIClientGateway.cleanup(ActiveGUIClientGateway.java:117)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GuiClientGatewayRegistrationHandler$RegistrationEntry.dispose(GuiClientGatewayRegistrationHandler.java:121)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GuiClientGatewayRegistrationHandler.removeRegistrationEntry(GuiClientGatewayRegistrationHandler.java:176)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.GuiClientGatewayRegistrationHandler.deregister(GuiClientGatewayRegistrationHandler.java:167)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.ClientGatewayManager.deregister(ClientGatewayManager.java:154)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.ClientGatewayManager.notifyStreamClientRemoved(ClientGatewayManager.java:150)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.SepGuiClientServiceListener.sepGuiClientServiceRemoved(SepGuiClientServiceListener.java:113)\n" + 
			"	at com.three60t.tex.service.sep2.clientgateway.SepGuiClientServiceListener.serviceRemoved(SepGuiClientServiceListener.java:73)\n" + 
			"	at sun.reflect.GeneratedMethodAccessor85.invoke(Unknown Source)\n" + 
			"	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"	at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"	at com.three60t.tex.communication.message.handler.MessageDispatcher$2.run(MessageDispatcher.java:208)\n" + 
			"	at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"	at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"	at java.lang.Thread.run(Thread.java:662)\n" + 
			"";

	@Test
	public void testCreation() {
		List<ExceptionCausedByChain> exceptionCausedByChains = new ExceptionParser().parseAndFlush(EXCEPTION_SAMPLE).getExceptionChains();
		List<EqualCauseExceptionChainContainer> equalCauseExceptionContainers = EqualCauseExceptionContainerFactory.createEqualCauseContainers(exceptionCausedByChains);

		
		assertEquals(1, equalCauseExceptionContainers.size());
		assertEquals(3, equalCauseExceptionContainers.get(0).getExceptionChains().size());
		
		assertExcpetionCauseSourceChain(new String[]{
				"MessageDispatcher.java:437",
				"MessageDispatcher.java:294",
				"MessageDispatcher.java:55",
				"MessageDispatcher.java:155",
				"RunnableWithTimings.java:81",
				"ThreadPoolExecutor.java:886",
				"ThreadPoolExecutor.java:908",
				"Thread.java:662",
				"Unknown Source",
				"DelegatingMethodAccessorImpl.java:25",
				"Method.java:597",
				"MessageDispatcher.java:406",
				"Validate.java:136",
				"TradingLimitChangeCache.java:59",
				"CachingCompanyLimitService.java:535",
				"CachingCompanyLimitService.java:579",
		}, equalCauseExceptionContainers.get(0));
	}
	
	@Test
	public void testProblemSample1(){
		List<ExceptionCausedByChain> exceptionCausedByChains = new ExceptionParser().parseAndFlush(SAMPLE2).getExceptionChains();
		List<EqualCauseExceptionChainContainer> equalCauseExceptionContainers = EqualCauseExceptionContainerFactory.createEqualCauseContainers(exceptionCausedByChains);

		
		assertEquals(1, equalCauseExceptionContainers.size());
	}

	private void assertExcpetionCauseSourceChain(String[] exptectedSourcePath, EqualCauseExceptionChainContainer exceptionChainContainer) {
		List<String> sourcePath = exceptionChainContainer.getSourcePath();
		assertEquals(exptectedSourcePath.length, sourcePath.size());
		
		for (int i=0; i < sourcePath.size(); i++){
			assertEquals(exptectedSourcePath[i], sourcePath.get(i));
		}
		
	}

}

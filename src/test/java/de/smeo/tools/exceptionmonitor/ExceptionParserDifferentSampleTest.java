package de.smeo.tools.exceptionmonitor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class ExceptionParserDifferentSampleTest {
	private final String BIG_SAMPLE = 
			
			" ERROR [13.10.11 05:44:19.449] MessageDispatcher . ProcessingException\n" + 
			"    |-| AnyId=ausv6v-a0350-gtpbq26u-cdz | OrderReferenceId=SO-13642970-R1 | ProviderOrderReferenceId=SO-13642970-R1\n" + 
			"    |-| exception=java.lang.reflect.InvocationTargetException\n" + 
			"    |-| callerVM=configservice | nanos=5992342220569010 | threadId=7803 | threadName=MessageDispatcher - CachingCompanyLimitService-ING Vysya-Receiver-thread-1\n" + 
			"      | timestamp=1318484659449 | vm=sep2node2 |-| layer=App | domain=class |-| [SSSOSllSlS]\n" + 
			"java.lang.reflect.InvocationTargetException\n" + 
			"        at sun.reflect.GeneratedMethodAccessor94.invoke(Unknown Source)\n" + 
			"        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"        at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"        at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"        at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"        at java.lang.Thread.run(Thread.java:662)\n" + 
			"Caused by: java.lang.IllegalArgumentException: no cached limit change found for ausv6v-a0350-gtpbq26u-cdz\n" + 
			"        at org.apache.commons.lang.Validate.isTrue(Validate.java:136)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.TradingLimitChangeCache.remove(TradingLimitChangeCache.java:59)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.setCompanyTradingLimitConfig(CachingCompanyLimitService.java:534)\n" + 
			"        at com.three60t.tex.service.sep2.marketdatapool.CachingCompanyLimitService.limitsChanged(CachingCompanyLimitService.java:578)\n" + 
			"        ... 11 more\n" + 
			"\n" + 
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
			"        ... 11 more\n" + 
			"\n" + 
			"FATAL [13.10.11 20:59:55.156] FatalLogger .uncaughtException() The following throwable was not caught anywhere in the thread Thread-44\n" + 
			"    |-|  |-| exception=java.lang.ExceptionInInitializerError\n" + 
			"    |-| nanos=6047277927453010 | threadId=143 | threadName=Thread-44 | timestamp=1318539595156 | vm=sep2node2 |-| layer=App |-| [OllSlS]\n" + 
			"java.lang.ExceptionInInitializerError\n" + 
			"        at org.eclipse.jetty.server.Server.doStop(Server.java:315)\n" + 
			"        at org.eclipse.jetty.util.component.AbstractLifeCycle.stop(AbstractLifeCycle.java:80)\n" + 
			"        at com.three60t.framework.communication.hornetq.server.HornetQHttpContainer$2.run(HornetQHttpContainer.java:137)\n" + 
			"Caused by: java.lang.IllegalStateException: Shutdown in progress\n" + 
			"        at java.lang.ApplicationShutdownHooks.add(ApplicationShutdownHooks.java:39)\n" + 
			"        at java.lang.Runtime.addShutdownHook(Runtime.java:192)\n" + 
			"        at org.eclipse.jetty.util.thread.ShutdownThread.<init>(ShutdownThread.java:48)\n" + 
			"        at org.eclipse.jetty.util.thread.ShutdownThread.<clinit>(ShutdownThread.java:36)\n" + 
			"        ... 3 more\n" + 
			"\n" + 
			"FATAL [12.10.11 00:52:18.170] FatalLogger . ProcessingException\n" + 
			"    |-| StreamRequestId=q29gh8-c0a83a-gtnlr9wa-ng |-| exception=java.lang.reflect.InvocationTargetException\n" + 
			"    |-| callerVM=core | nanos=5888420941782010 | threadId=31 | threadName=MessageDispatcher - ServiceRegistryListener-Receiver-thread-1\n" + 
			"      | timestamp=1318380738170 | vm=sep2node2 |-| layer=App | domain=class |-| [SOSllSlS]\n" + 
			"java.lang.reflect.InvocationTargetException\n" + 
			"        at sun.reflect.GeneratedMethodAccessor168.invoke(Unknown Source)\n" + 
			"        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)\n" + 
			"        at java.lang.reflect.Method.invoke(Method.java:597)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.dispatchToObject(MessageDispatcher.java:406)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.doExecuteObject(MessageDispatcher.java:294)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher.access$100(MessageDispatcher.java:55)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher$1.run(MessageDispatcher.java:155)\n" + 
			"        at com.three60t.tex.communication.message.handler.MessageDispatcher$2.run(MessageDispatcher.java:208)\n" + 
			"        at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"        at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"        at java.lang.Thread.run(Thread.java:662)\n" + 
			"Caused by: java.lang.NoSuchMethodError: com.three60t.api.services.sep2.clientgateway.MarketDataStreamSubscriptionMultiKey.<init>(Ljava/util/Collection;)V\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.invalidateClientMarketData(GUIClientGatewayImpl.java:732)\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.unsubscribeAllSubscriptions(GUIClientGatewayImpl.java:727)\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.GUIClientGatewayImpl.cleanup(GUIClientGatewayImpl.java:740)\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.ActiveGUIClientGateway.cleanup(ActiveGUIClientGateway.java:110)\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.GuiClientGatewayRegistrationHandler$RegistrationEntry.dispose(GuiClientGatewayRegistrationHandler.java:121)\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.GuiClientGatewayRegistrationHandler.removeRegistrationEntry(GuiClientGatewayRegistrationHandler.java:176)\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.GuiClientGatewayRegistrationHandler.deregister(GuiClientGatewayRegistrationHandler.java:167)\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.ClientGatewayManager.deregister(ClientGatewayManager.java:154)\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.ClientGatewayManager.notifyStreamClientRemoved(ClientGatewayManager.java:150)\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.SepGuiClientServiceListener.sepGuiClientServiceRemoved(SepGuiClientServiceListener.java:113)\n" + 
			"        at com.three60t.tex.service.sep2.clientgateway.SepGuiClientServiceListener.serviceRemoved(SepGuiClientServiceListener.java:73)\n" + 
			"        ... 12 more\n" + 
			"\n" + 
			"FATAL [13.10.11 09:37:51.809] FatalLogger .uncaughtException() The following throwable was not caught anywhere in the thread Thread-73000\n" + 
			"    |-|  |-| exception=com.three60t.tex.app.exceptions.processing.ProcessingException: Error processing 'com.three60t.dealexporter.base.translator.deal.TexTradeTranslator$TexTradeTranslatingException'\n" + 
			"    |-| nanos=4032445675567339 | threadId=135244 | threadName=Thread-73000 | timestamp=1318498671809 | vm=tex1 |-| layer=App |-| [OllSlS]\n" + 
			"com.three60t.tex.app.exceptions.processing.ProcessingException: Error processing 'com.three60t.dealexporter.base.translator.deal.TexTradeTranslator$TexTradeTranslatingException'\n" + 
			"        at com.three60t.tex.communication.message.core.GeneralErrorMessage.<init>(GeneralErrorMessage.java:46)\n" + 
			"        at com.three60t.tex.communication.message.core.GeneralErrorMessage.<init>(GeneralErrorMessage.java:59)\n" + 
			"        at com.three60t.tex.communication.message.core.GeneralErrorMessage.<init>(GeneralErrorMessage.java:85)\n" + 
			"        at com.three60t.tex.communication.message.core.GeneralMessageFactory.createMessageWithDecodedContent(GeneralMessageFactory.java:30)\n" + 
			"        at com.three60t.tex.communication.codecs.JavaBusCodec.decode(JavaBusCodec.java:46)\n" + 
			"        at com.three60t.tex.communication.communicator.CommunicatorBase.decodeWithCodecs(CommunicatorBase.java:353)\n" + 
			"        at com.three60t.tex.communication.communicator.CommunicatorBase.decodeMessage(CommunicatorBase.java:341)\n" + 
			"        at com.three60t.tex.communication.communicator.CommunicatorBase.sendRequest(CommunicatorBase.java:461)\n" + 
			"        at com.three60t.tex.communication.message.handler.CommandStub$1.run(CommandStub.java:200)\n" + 
			"        at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"        at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"        at java.lang.Thread.run(Thread.java:662)\n" + 
			"Caused by: java.lang.NoSuchMethodException: com.three60t.dealexporter.base.translator.deal.TexTradeTranslator$TexTradeTranslatingException.<init>(java.lang.String)\n" + 
			"        at java.lang.Class.getConstructor0(Class.java:2706)\n" + 
			"        at java.lang.Class.getConstructor(Class.java:1657)\n" + 
			"        at com.three60t.tex.communication.message.core.GeneralErrorMessage.<init>(GeneralErrorMessage.java:41)\n" + 
			"        ... 12 more\n" + 
			"\n" + 
			"FATAL [13.10.11 14:29:46.453] FatalLogger .uncaughtException() The following throwable was not caught anywhere in the thread Thread-127026\n" + 
			"    |-|  |-| exception=java.lang.RuntimeException: Trade #187536385 already exported successfully.\n" + 
			"    |-| nanos=4049960319603339 | threadId=234181 | threadName=Thread-127026 | timestamp=1318516186453 | vm=tex1 |-| layer=App |-| [OllSlS]\n" + 
			"java.lang.RuntimeException: Trade #187536385 already exported successfully.\n" + 
			"        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)\n" + 
			"        at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)\n" + 
			"        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)\n" + 
			"        at java.lang.reflect.Constructor.newInstance(Constructor.java:513)\n" + 
			"        at com.three60t.tex.communication.message.core.GeneralErrorMessage.<init>(GeneralErrorMessage.java:41)\n" + 
			"        at com.three60t.tex.communication.message.core.GeneralErrorMessage.<init>(GeneralErrorMessage.java:59)\n" + 
			"        at com.three60t.tex.communication.message.core.GeneralErrorMessage.<init>(GeneralErrorMessage.java:85)\n" + 
			"        at com.three60t.tex.communication.message.core.GeneralMessageFactory.createMessageWithDecodedContent(GeneralMessageFactory.java:30)\n" + 
			"        at com.three60t.tex.communication.codecs.JavaBusCodec.decode(JavaBusCodec.java:46)\n" + 
			"        at com.three60t.tex.communication.communicator.CommunicatorBase.decodeWithCodecs(CommunicatorBase.java:353)\n" + 
			"        at com.three60t.tex.communication.communicator.CommunicatorBase.decodeMessage(CommunicatorBase.java:341)\n" + 
			"        at com.three60t.tex.communication.communicator.CommunicatorBase.sendRequest(CommunicatorBase.java:461)\n" + 
			"        at com.three60t.tex.communication.message.handler.CommandStub$1.run(CommandStub.java:200)\n" + 
			"        at com.three60t.concurrent.executor.v2.RunnableWithTimings.run(RunnableWithTimings.java:81)\n" + 
			"        at java.util.concurrent.ThreadPoolExecutor$Worker.runTask(ThreadPoolExecutor.java:886)\n" + 
			"        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:908)\n" + 
			"        at java.lang.Thread.run(Thread.java:662)";
	
	String[] firstExceptionChain = new String[]{"java.lang.reflect.InvocationTargetException","java.lang.IllegalArgumentException"};
	String[] secondExceptionChain = new String[]{"com.three60t.tex.app.exceptions.processing.ProcessingException","java.lang.reflect.InvocationTargetException","java.lang.IllegalArgumentException"};
	String[] thirdExceptionChain = new String[]{"java.lang.ExceptionInInitializerError","java.lang.IllegalStateException"};
	String[] forthExceptionChain = new String[]{"java.lang.reflect.InvocationTargetException","java.lang.NoSuchMethodError"};
	String[] sixthExceptionChain = new String[]{"com.three60t.tex.app.exceptions.processing.ProcessingException","java.lang.NoSuchMethodException"};
	String[] seventhExceptionChain = new String[]{"java.lang.RuntimeException"};
	
	@Test
	public void testParsedExceptions() {
		ExceptionParserFixture exceptionParserFixture = new ExceptionParserFixture(BIG_SAMPLE);
		exceptionParserFixture.verifyExceptionChains(
				firstExceptionChain,
				secondExceptionChain,
				thirdExceptionChain,
				forthExceptionChain,
				sixthExceptionChain,
				seventhExceptionChain);
	}
	
	

	private class ExceptionParserFixture {
		private ExceptionParser exceptionParser = new ExceptionParser();
		
		public ExceptionParserFixture(String exceptionSample) {
			exceptionParser.parseAndFlush(exceptionSample);
		}

		public void verifyExceptionChains(String[] ...chains) {
			List<ExceptionCausedByChain> exceptionChains = exceptionParser.getExceptionChains();
			assertEquals(chains.length, exceptionChains.size());
			
			for (int i=0; i < chains.length; i++){
				verifyExceptionChain(exceptionChains.get(i), chains[i]);
			}
		}

		private void verifyExceptionChain(ExceptionCausedByChain exceptionCausedByChain, String[] exceptionClassNames) {
			List<LoggedException> exceptionsInChain = exceptionCausedByChain.getExceptions();
			assertEquals(exceptionClassNames.length, exceptionsInChain.size());
			
			for (int i=0; i < exceptionClassNames.length; i++){
				assertEquals(exceptionClassNames[i], exceptionsInChain.get(i).getExceptionClassName());
			}
		}
	}
}




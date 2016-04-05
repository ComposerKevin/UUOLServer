package com.cnkvha.uuol.clientent.server;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cnkvha.uuol.cache.protocol.UUOLComponent;
import com.cnkvha.uuol.cache.protocol.message.CacheAbstractMessage;
import com.cnkvha.uuol.clientent.server.net.NetworkServer;
import com.cnkvha.uuol.sjl.data.SerializationTool;
import com.cnkvha.uuol.sjl.data.StringTools;
import com.sun.enterprise.ee.cms.core.CallBack;
import com.sun.enterprise.ee.cms.core.GMSFactory;
import com.sun.enterprise.ee.cms.core.GroupManagementService;
import com.sun.enterprise.ee.cms.core.Signal;
import com.sun.enterprise.ee.cms.core.GMSConstants.shutdownType;
import com.sun.enterprise.ee.cms.core.GroupManagementService.MemberType;
import com.sun.enterprise.ee.cms.impl.client.MessageActionFactoryImpl;
import com.sun.enterprise.ee.cms.impl.client.PlannedShutdownActionFactoryImpl;
import com.sun.enterprise.ee.cms.impl.common.GMSConfigConstants;

public final class EntranceServer {
	
	private NetworkServer netsvr;
	

	private static EntranceServer INSTANCE;
	private final static Logger LOGGER = LogManager.getLogger("CacheServer");

	public static EntranceServer getInstance() {
		return INSTANCE;
	}

	public Logger getLogger() {
		return LOGGER;
	}

	public static void main(String[] args) {
		new EntranceServer().run();
	}

	private GroupManagementService gms;

	private EntranceHandler recv;

	private boolean keepRunning;

	private ExecutorService threadPool;

	private void run() {
		INSTANCE = this;
		LOGGER.info("Starting client entrance server... ");
		
		
		LOGGER.info("Initiating thread pool... ");
		threadPool = Executors.newFixedThreadPool(128);

		Properties prop = new Properties();
		prop.setProperty(GMSConfigConstants.MULTICAST_ADDRESS, "235.5.5.5");
		prop.setProperty(GMSConfigConstants.MULTICAST_PORT, "45588");
		prop.setProperty(GMSConfigConstants.PING_TIMEOUT, "2000");
		prop.setProperty(GMSConfigConstants.FD_MAX_RETRIES, "20");
		prop.setProperty(GMSConfigConstants.FD_TIMEOUT, "1000");

		try {
			gms = (GroupManagementService) GMSFactory.startGMSModule(
					StringTools.rndString(16), "UUOL_Network", MemberType.SPECTATOR,	//Non-Core
					prop);
			recv = new EntranceHandler(this);
		} catch (Exception e1) {
			LOGGER.error("Faild to get the JGroup config! ");
			System.exit(-1);
			return;
		}
		try {
			LOGGER.info("Joining the channel... ");
			gms.join();
		} catch (Exception e) {
			e.printStackTrace();
			if (gms != null)
				gms.shutdown(shutdownType.INSTANCE_SHUTDOWN);
			LOGGER.error("Faild to join the group! ");
			LOGGER.error("Server stopped! ");
			return;
		}
		
		gms.addActionFactory(new MessageActionFactoryImpl(recv), UUOLComponent.GENERAL);
		CallBack cbViewChange = new CallBack(){
			@Override
			public void processNotification(Signal notification) {
				recv.cleanUpRequests();
			}
		};
		gms.addActionFactory(new PlannedShutdownActionFactoryImpl(cbViewChange));

		
		//Networking server
		LOGGER.info("Initiating networking server... ");
		try {
			netsvr = new NetworkServer(this);
		} catch (IOException e2) {
			e2.printStackTrace();
			LOGGER.error("Faild to start networking server! ");
			System.exit(-1);
			return;
		}
		
		
		// Finished loading
		LOGGER.info("Server started! ");

		// START PARSING COMMANDS
		keepRunning = true;
		enterLoop();

		recv.stop();
		netsvr.shutdown();
		gms.shutdown(shutdownType.INSTANCE_SHUTDOWN);
		LOGGER.info("Server stopped! ");
	}
	
	private void enterLoop(){
		Scanner scn = new Scanner(System.in);
		while(keepRunning){
			scn.nextLine();
		}
		scn.close();
	}
	
	
	public synchronized void sendPacket(CacheAbstractMessage msg, String component){
		if(msg == null) {
			getLogger().warn("Can not send null message! ");
			return;
		}
		byte[] data = null;
		try {
			data = SerializationTool.encode(msg);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("Can not serialize packet! ");
			return;
		}
		if(data == null) return;
		try {
			gms.getGroupHandle().sendMessage(component.equals("") ? null : component , data);
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().error("Error sending packet! ");
		}
	}
	
	
	
	public GroupManagementService getGms() {
		return gms;
	}
	
	public EntranceHandler getHandler() {
		return recv;
	}
	
	public ExecutorService getThreadPool() {
		return threadPool;
	}
	
	public NetworkServer getNetworkServer() {
		return netsvr;
	}
	
}

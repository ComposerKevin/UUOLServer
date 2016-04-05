package com.cnkvha.uuol.cache.server.cache.entity;

import java.util.UUID;

import com.cnkvha.uuol.cache.protocol.UUOLComponent;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageBroadcast;
import com.cnkvha.uuol.cache.protocol.message.CacheSectionBroadcast;
import com.cnkvha.uuol.cache.server.CacheServer;
import com.cnkvha.uuol.cache.server.cache.planet.PlanetChunkCache;
import com.cnkvha.uuol.sjl.data.SerializationTool;
import com.cnkvha.uuol.sjl.math.Vector3Long;
import com.sun.enterprise.ee.cms.core.CallBack;
import com.sun.enterprise.ee.cms.core.MessageSignal;
import com.sun.enterprise.ee.cms.core.Signal;
import com.sun.enterprise.ee.cms.core.SignalAcquireException;
import com.sun.enterprise.ee.cms.core.SignalReleaseException;

public class EntitySectionBroadcastProcessor implements CallBack {
	
	private final EntityCache cache;
	
	
	public EntitySectionBroadcastProcessor(EntityCache cache) {
		this.cache = cache;
	}

	@Override
	public void processNotification(Signal notification) {
		try {
			notification.acquire();
		} catch (SignalAcquireException e) {
			e.printStackTrace();
			return;
		}
		if(!MessageSignal.class.isAssignableFrom(notification.getClass())){
			try {
				notification.release();
			} catch (SignalReleaseException e) {
			}
			return;
		}
		boolean shouldUnregister = receive(notification.getMemberToken(), ((MessageSignal)notification).getMessage());
		try {
			notification.release();
		} catch (SignalReleaseException e) {
		}
		if(shouldUnregister){
			CacheServer.getInstance().getGms().removeMessageActionFactory(((MessageSignal)notification).getTargetComponent());
		}
	}
	
	private boolean receive(String sender, byte[] msg) {
		Object obj = null;
		try {
			obj = SerializationTool.decode(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if(obj == null) return false;
		
		if(!CacheSectionBroadcast.class.isAssignableFrom(obj.getClass())) return false; //ONLY BROADCASTS
		
		UUID planetUniqueId = ((CacheSectionBroadcast)obj).planet;
		
		if(planetUniqueId == null){
			//Universe entity broadcast
			cache.getBroadcastRegister().process((CacheMessageBroadcast) obj);
			return false;
		}
		
		PlanetChunkCache planetCache = cache.getGeneralCache().getPlanet(planetUniqueId);
		
		if(planetCache == null){
			//No such planet cached and also not universe, useless handler registered. 
			return true;
		}
		
		planetCache.getBroadcastRegister().process((CacheMessageBroadcast)obj);
		return false;
	}
	
	/**
	 * Set planet to null for universe component. 
	 * @param planet
	 * @param section
	 * @return
	 */
	public static String getComponentName(UUID planet, Vector3Long section){
		return planet != null ? (UUOLComponent.PREFIX_PLANET + (planet != null ? planet.toString() + "-" : "UNIVERSE-")) : UUOLComponent.PREFIX_UNIVERSE + "Section-" + section.x + "_" + section.y + "_" + section.z;
	}
}

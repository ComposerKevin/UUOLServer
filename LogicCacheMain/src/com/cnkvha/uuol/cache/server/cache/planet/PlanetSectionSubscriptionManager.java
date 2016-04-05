package com.cnkvha.uuol.cache.server.cache.planet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.sjl.data.KeyHashSet;
import com.cnkvha.uuol.sjl.math.CoordinateConverter;
import com.cnkvha.uuol.sjl.math.Vector3Long;
import com.sun.enterprise.ee.cms.impl.client.MessageActionFactoryImpl;

public final class PlanetSectionSubscriptionManager {
	
	/*
	 * Only chunk loading/unloading in sections can cause this to update. 
	 * Such as loading chunk at (0,0,0) and (0,1,0) will cause it to listen on section (0,0,0).  
	 */
	
	
	private final Map<Vector3Long, KeyHashSet<PlanetSectionBroadcastProcessor, Vector3Long>> sectionSubscriptions = 
			new ConcurrentHashMap<>();
	
	private final PlanetChunkCache cache;

	public PlanetSectionSubscriptionManager(PlanetChunkCache cache) {
		this.cache = cache;
	}
	
	/**
	 * Lock current section processor, and start register component listener if needed. 
	 * @param chunk The chunk location. 
	 * @param who The chunk which locks this section. 
	 */
	public synchronized void listen(Vector3Long section, Vector3Long chunk){
		KeyHashSet<PlanetSectionBroadcastProcessor, Vector3Long> h = null;
		boolean newlyCreated = false;
		if(!sectionSubscriptions.containsKey(section)){
			newlyCreated = true;
			h = new KeyHashSet<>(cache.getServer().getCache().getUniversalPlanetBroadcastProcessor());
			sectionSubscriptions.put(section, h);
		}else{
			h = sectionSubscriptions.get(chunk);
			newlyCreated = h.size() <= 0;
		}
		h.add(chunk);
		if(newlyCreated){
			cache.getServer().getGms().addActionFactory(new MessageActionFactoryImpl(h.getKey()), PlanetSectionBroadcastProcessor.getComponentName(cache, CoordinateConverter.chunk2section(chunk)));
			System.out.println("Now subscribed to section component " + PlanetSectionBroadcastProcessor.getComponentName(cache, CoordinateConverter.chunk2section(chunk)));
		}
	}
	
	public synchronized void unlisten(Vector3Long section, Vector3Long chunk){
		if(!sectionSubscriptions.containsKey(section)) return;
		KeyHashSet<PlanetSectionBroadcastProcessor, Vector3Long> h = sectionSubscriptions.get(section);
		h.remove(chunk);
		if(h.size() <= 0){
			//No listeners anymore, stop processing message for component. 
			sectionSubscriptions.remove(section);
			cache.getServer().getGms().removeMessageActionFactory(PlanetSectionBroadcastProcessor.getComponentName(cache, CoordinateConverter.chunk2section(chunk)));
			h = null;
		}
	}
	
	/**
	 * Unlocks everything and unregisters all GMS component listeners.  
	 */
	public void unload(){
		//TODO
	}
}

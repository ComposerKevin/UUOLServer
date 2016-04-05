package com.cnkvha.uuol.cache.server.cache.entity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.cache.protocol.entity.CachedEntity;
import com.cnkvha.uuol.cache.server.CacheServer;
import com.cnkvha.uuol.sjl.data.KeyHashSet;
import com.cnkvha.uuol.sjl.math.CoordinateConverter;
import com.cnkvha.uuol.sjl.math.Vector3Long;
import com.sun.enterprise.ee.cms.impl.client.MessageActionFactoryImpl;

public final class EntitySectionSubscriptionManager {
	
	/*
	 * Entity cache and un-cache, moving/teleporting will cause this to update. 
	 * >> Null planet for universe. <<   
	 */
	
	/*
	 * When entity crosses chunk, source will send new data to both channels. 
	 */
	
	private final EntityCache cache;

	public EntitySectionSubscriptionManager(EntityCache cache) {
		this.cache = cache;
	}

	private final 
			Map<UUID, 		//Planet
				Map<Vector3Long, 
					KeyHashSet<EntitySectionBroadcastProcessor, CachedEntity>
				>
			>
			planetMap = new ConcurrentHashMap<>();
	
	/**
	 * Lock current section processor, and start register component listener if needed. 
	 * @param chunk The chunk location. 
	 * @param who The entity which locks this section. 
	 */
	public synchronized void listen(UUID planet, CachedEntity entity, Vector3Long section){
		KeyHashSet<EntitySectionBroadcastProcessor, CachedEntity> h = null;
		boolean newlyCreated = false;
		Map<Vector3Long, KeyHashSet<EntitySectionBroadcastProcessor, CachedEntity>> sectionSubscriptions = null;
		if (!planetMap.containsKey(planet)){
			newlyCreated = true;
			sectionSubscriptions = new ConcurrentHashMap<>();
			planetMap.put(planet, sectionSubscriptions);
		}else{
			sectionSubscriptions = planetMap.get(planet);
		}
		if(!sectionSubscriptions.containsKey(section)){
			newlyCreated = true;
			EntitySectionBroadcastProcessor processor = cache.getUniversalProcessor();//new EntitySectionBroadcastProcessor();
			h = new KeyHashSet<>(processor);
			sectionSubscriptions.put(section, h);
		}else{
			h = sectionSubscriptions.get(section);
			newlyCreated = h.size() <= 0;
		}
		h.add(entity);
		if(!entity.getUsedSections().contains(section)) entity.getUsedSections().add(section);
		if(newlyCreated){
			CacheServer.getInstance().getGms().addActionFactory(new MessageActionFactoryImpl(h.getKey()), EntitySectionBroadcastProcessor.getComponentName(planet, CoordinateConverter.pos2section(entity.getLocation())));
		}
	}
	
	public synchronized void unlisten(UUID planet, CachedEntity entity, Vector3Long section){
		Map<Vector3Long, KeyHashSet<EntitySectionBroadcastProcessor, CachedEntity>> sectionSubscriptions = null;
		if(!planetMap.containsKey(planet)){
			return;
		}else{
			sectionSubscriptions = planetMap.get(planet);
		}
		if(!sectionSubscriptions.containsKey(section)) return;
		KeyHashSet<EntitySectionBroadcastProcessor, CachedEntity> h = sectionSubscriptions.get(section);
		h.remove(entity);
		entity.getUsedSections().remove(section);
		if(h.size() <= 0){
			//No listeners anymore, stop processing message for component. 
			sectionSubscriptions.remove(section);
			CacheServer.getInstance().getGms().removeMessageActionFactory(EntitySectionBroadcastProcessor.getComponentName(planet, section));
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

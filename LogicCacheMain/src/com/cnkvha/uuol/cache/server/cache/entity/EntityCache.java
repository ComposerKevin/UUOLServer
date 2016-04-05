package com.cnkvha.uuol.cache.server.cache.entity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.cnkvha.uuol.cache.protocol.entity.CachedEntity;
import com.cnkvha.uuol.cache.protocol.handlers.BroadcastHandler;
import com.cnkvha.uuol.cache.server.cache.GeneralCache;

public final class EntityCache {
	
	private final GeneralCache generalCache;
	
	public EntityCache(GeneralCache generalCache) {
		this.generalCache = generalCache;
	}



	private final Map<UUID, CachedEntity> mapPlanetEntities = new ConcurrentHashMap<>();
	
	private final Map<UUID, CachedEntity> mapUniverseEntities = new ConcurrentHashMap<>();
	
	
	private final BroadcastHandler.BroadcastHandlerRegister bcRegister = new BroadcastHandler.BroadcastHandlerRegister();
	
	
	private EntitySectionSubscriptionManager subMgr = new EntitySectionSubscriptionManager(this);
	
	private final EntitySectionBroadcastProcessor universalProcessor = new EntitySectionBroadcastProcessor(this);
	
	public boolean isEntityCached(UUID eid){
		if(mapPlanetEntities.containsKey(eid)) return true;
		if(mapUniverseEntities.containsKey(eid)) return true;
		return false;
	}
	
	public EntitySectionBroadcastProcessor getUniversalProcessor() {
		return universalProcessor;
	}
	
	
	
	public BroadcastHandler.BroadcastHandlerRegister getBroadcastRegister() {
		return bcRegister;
	}
	
	public GeneralCache getGeneralCache() {
		return generalCache;
	}
	
	public EntitySectionSubscriptionManager getSubscriptionManager() {
		return subMgr;
	}
}

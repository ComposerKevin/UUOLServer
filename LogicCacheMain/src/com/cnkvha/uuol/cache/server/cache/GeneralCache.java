package com.cnkvha.uuol.cache.server.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.cnkvha.uuol.cache.server.CacheServer;
import com.cnkvha.uuol.cache.server.cache.planet.PlanetChunkCache;
import com.cnkvha.uuol.cache.server.cache.planet.PlanetSectionBroadcastProcessor;
import com.cnkvha.uuol.sjl.interfaces.DirectCallback;
import com.sun.enterprise.ee.cms.impl.client.MessageActionFactoryImpl;

public final class GeneralCache {
	private final CacheServer server;
	
	private final Map<UUID, PlanetChunkCache> planets = Collections.synchronizedMap(new HashMap<>());
	
	private final PlanetSectionBroadcastProcessor universalPlanetBroadcastProcessor= new PlanetSectionBroadcastProcessor(this);
	
	public GeneralCache(CacheServer server) {
		this.server = server;
	}
	
	public PlanetChunkCache getPlanet(UUID uuid){
		synchronized (planets) {
			if(!isPlanetInCache(uuid)) return null;
			return planets.get(uuid);
		}
	}
	
	public PlanetChunkCache initialPlanetCache(UUID uuid, DirectCallback whenDone){
		synchronized (planets) {
			if(planets.containsKey(uuid)) return planets.get(uuid);
			try {
				PlanetChunkCache c = new PlanetChunkCache(server, uuid, whenDone);
				planets.put(uuid, c);
				//Register for broadcast listener
				server.getGms().addActionFactory(new MessageActionFactoryImpl(c), c.getComponentName());
				CacheServer.getInstance().getLogger().info("Initiated planet chunk cache for " + uuid.toString() + "! ");
				return c;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void unloadPlanet(UUID world){
		synchronized (planets){
			if(!isPlanetInCache(world)) return;
			planets.get(world).unload();
			planets.remove(world);
		}
	}
	
	public boolean isPlanetInCache(UUID uuid){
		synchronized (planets) {
			return planets.containsKey(uuid);
		}
	}
	
	public PlanetSectionBroadcastProcessor getUniversalPlanetBroadcastProcessor() {
		return universalPlanetBroadcastProcessor;
	}
}

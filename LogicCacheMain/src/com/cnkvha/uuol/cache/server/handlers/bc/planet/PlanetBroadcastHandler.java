package com.cnkvha.uuol.cache.server.handlers.bc.planet;

import com.cnkvha.uuol.cache.protocol.handlers.BroadcastHandler;
import com.cnkvha.uuol.cache.protocol.message.CacheMessageBroadcast;
import com.cnkvha.uuol.cache.server.cache.planet.PlanetChunkCache;

public abstract class PlanetBroadcastHandler<MSG extends CacheMessageBroadcast> extends BroadcastHandler<MSG> {
	private PlanetChunkCache planet;
	
	public PlanetBroadcastHandler(PlanetChunkCache planet) {
		this.planet = planet;
	}
	
	public PlanetChunkCache getPlanet() {
		return planet;
	}
}

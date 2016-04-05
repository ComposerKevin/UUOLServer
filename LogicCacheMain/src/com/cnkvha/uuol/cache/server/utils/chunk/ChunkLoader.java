package com.cnkvha.uuol.cache.server.utils.chunk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.cnkvha.uuol.cache.protocol.message.general.request.CacheRequestLoadChunk;

public class ChunkLoader {
	
	private final ExecutorService loaders;
	
	private AtomicInteger counter;
	
	public ChunkLoader(int poolSize) {
		 loaders = Executors.newFixedThreadPool(poolSize);
		 counter = new AtomicInteger();
	}
	
	public void submitWork(CacheRequestLoadChunk req){
		loaders.execute(new LoadChunkTask(this, req));
	}
	
	protected void startedJob(){
		counter.incrementAndGet();
	}
	
	protected void endedJob(){
		counter.decrementAndGet();
	}
}

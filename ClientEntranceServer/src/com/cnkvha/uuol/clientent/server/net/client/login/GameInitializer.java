package com.cnkvha.uuol.clientent.server.net.client.login;

import java.util.Map;

import com.cnkvha.uuol.cache.protocol.UUOLComponent;
import com.cnkvha.uuol.cache.protocol.data.RequestIdentifier;
import com.cnkvha.uuol.cache.protocol.handlers.ResponseHandler;
import com.cnkvha.uuol.cache.protocol.message.general.request.CacheRequestLoadChunk;
import com.cnkvha.uuol.cache.protocol.message.general.response.CacheResponseLoadChunk;
import com.cnkvha.uuol.clientent.server.EntranceServer;
import com.cnkvha.uuol.clientent.server.net.client.GameClient;
import com.cnkvha.uuol.net.protocol.LanguageBindings;
import com.cnkvha.uuol.sjl.math.Vector3Long;

public final class GameInitializer {
	public static void initialize(GameClient game){
		try{
			doInit(game);
		}catch(Exception e){
			game.getNetworkClient().close(LanguageBindings.DISCONNECT_SERVER_ERROR);
		}
	}
	
	
	private static void doInit(GameClient game) throws Exception{
		EntranceServer ent = EntranceServer.getInstance();
		
		if(game.isInPlanet()){
			//In planet, we load chunk
			Vector3Long chunkLoc = new Vector3Long();
			chunkLoc.x = (long)game.getLocation().x / 16;
			chunkLoc.y = (long)game.getLocation().y / 16;
			chunkLoc.z = (long)game.getLocation().y / 16;
			
			RequestIdentifier rid = new RequestIdentifier(ent.getGms().getInstanceName());
			CacheRequestLoadChunk req = new CacheRequestLoadChunk(rid, game.getPlanetID(), chunkLoc) ;
			
			
			//STEP 1: Load first chunk
			
			ent.getHandler().getRequests().register(ent.getGms().getGroupHandle().getCurrentAliveOrReadyMembers(), rid, new ResponseHandler<CacheResponseLoadChunk>() {
				@Override
				public boolean needAllResponses() {
					return true;
				}

				@Override
				public void handle(Map<String, CacheResponseLoadChunk> resp) {
					boolean success = false;
					for(CacheResponseLoadChunk r : resp.values()){
						if(r.status == CacheResponseLoadChunk.STATUS_LOADED || 
								r.status == CacheResponseLoadChunk.STATUS_ALREADY_LOADED){
							success = true;
						}
					}
					if(!success){
						//Faild loading chunk, disconnect the client
						game.getNetworkClient().close(LanguageBindings.DISCONNECT_SERVER_ERROR);
						ent.getLogger().error("Faild loading chunk at " + chunkLoc.toString() + "! ");
						return;
					}
					GameInit_Planet_Step2.planet_step2(game, chunkLoc);
				}
			});
			ent.sendPacket(req, UUOLComponent.GENERAL);
		}else{
			//In universe, we broadcast. 
			//TODO
		}
	}
	
	
	
	

}

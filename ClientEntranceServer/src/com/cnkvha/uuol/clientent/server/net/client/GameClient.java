package com.cnkvha.uuol.clientent.server.net.client;

import java.util.UUID;

import com.cnkvha.uuol.cache.protocol.data.EntityType;
import com.cnkvha.uuol.sjl.math.CoordinateConverter;
import com.cnkvha.uuol.sjl.math.Vector3Double;
import com.cnkvha.uuol.sjl.math.Vector3Float;
import com.cnkvha.uuol.sjl.math.Vector3Long;

public final class GameClient  {
	
	private NetworkClient client;
	
	private final long userID;
	
	private UUID entityID;
	
	private EntityType entityType;
	
	private UUID planetID;				//null means in universe
	
	private Vector3Double location;
	
	private Vector3Float rotation; //x=yaw, y=pitch
	
	
	public GameClient(NetworkClient client, long userID) {
		this.client = client;
		this.userID = userID;
	}
	
	@Override
	public int hashCode() {
		return client.getUniqueID().hashCode();
	}
	
	public UUID getEntityID() {
		return entityID;
	}
	
	public Vector3Double getLocation() {
		return location;
	}
	
	public Vector3Float getRotation() {
		return rotation;
	}
	
	public boolean isInPlanet(){
		return planetID != null;
	}
	
	/**
	 * Get the player's ID in database. 
	 * @return
	 */
	public long getUserID() {
		return userID;
	}
	
	public UUID getPlanetID() {
		return planetID;
	}
	
	public EntityType getEntityType() {
		return entityType;
	}
	
	public long getEntityTypeID() {
		return entityType.getType();
	}
	
	public NetworkClient getNetworkClient() {
		return client;
	}
	
	public Vector3Long getSection() {
		return CoordinateConverter.pos2chunk(location);
	}
	
	public void setPlanetID(UUID planetID) {
		this.planetID = planetID;
		//TODO: Send packet to client
	}
	
	public void setLocation(Vector3Double location) {
		this.location = location;
		//TODO: Send packet to client
	}
	
	public void setRotation(Vector3Float rotation) {
		this.rotation = rotation;
		//TODO: Send packet to client
	}
}

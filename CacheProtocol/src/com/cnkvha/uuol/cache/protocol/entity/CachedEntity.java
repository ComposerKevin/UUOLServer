package com.cnkvha.uuol.cache.protocol.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.UUID;

import com.cnkvha.uuol.cache.protocol.data.meta.MetaData;
import com.cnkvha.uuol.sjl.math.Vector3Double;
import com.cnkvha.uuol.sjl.math.Vector3Float;
import com.cnkvha.uuol.sjl.math.Vector3Long;

@SuppressWarnings("serial")
public abstract class CachedEntity implements Serializable {
	
	protected final long dataId;
	
	protected final UUID entityID;
	
	protected final long entityType;
	
	private Vector3Double location;
	
	private Vector3Float rotation;
	
	private MetaData meta;
	
	private final HashSet<Vector3Long> usedSections = new HashSet<>();
	
	public CachedEntity(long dataId, UUID entityID, long entityType, Vector3Double location,
			Vector3Float rotation) {
		this.dataId = dataId;
		this.entityID = entityID;
		this.entityType = entityType;
		this.location = location;
		this.rotation = rotation;
	}
	
	/**
	 * This will NOT update client. 
	 * @param location
	 * @param rotation
	 */
	public void setRawLocationRotation(Vector3Double location, Vector3Float rotation) {
		this.location = location;
		this.rotation = rotation;
	}

	/**
	 * This will NOT update client. 
	 * @param location
	 */
	public void setRawLocation(Vector3Double location) {
		this.location = location;
	}
	
	/**
	 * This will NOT update client. 
	 * @param rotation
	 */
	public void setRawRotation(Vector3Float rotation) {
		this.rotation = rotation;
	}
	
	/**
	 * This will NOT update client. 
	 * @param meta
	 */
	public void setMeta(MetaData meta) {
		this.meta = meta;
	}
	
	public Vector3Double getLocation() {
		return location;
	}
	
	public Vector3Float getRotation() {
		return rotation;
	}
	
	public MetaData getMeta() {
		return meta;
	}

	public UUID getEntityID() {
		return entityID;
	}
	
	public HashSet<Vector3Long> getUsedSections() {
		return usedSections;
	}
}

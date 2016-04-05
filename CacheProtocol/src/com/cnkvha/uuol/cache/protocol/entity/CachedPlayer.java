package com.cnkvha.uuol.cache.protocol.entity;

import java.util.UUID;

import com.cnkvha.uuol.cache.protocol.data.PlayerCreatureType;
import com.cnkvha.uuol.cache.protocol.data.meta.MetaData;
import com.cnkvha.uuol.cache.protocol.data.meta.type.MetaDataLongElement;
import com.cnkvha.uuol.sjl.math.Vector3Double;
import com.cnkvha.uuol.sjl.math.Vector3Float;

public class CachedPlayer extends CachedEntity {
	private static final long serialVersionUID = -966107042868646551L;
	
	
	
	public CachedPlayer(long dataId, UUID entityID, long entityType,
			Vector3Double location, Vector3Float rotation, PlayerCreatureType creatureType) {
		super(dataId, entityID, entityType, location, rotation);
		MetaData meta = new MetaData();
		meta.set(PlayerCreatureType.META_KEY, new MetaDataLongElement(creatureType.getType()));
		setMeta(meta);
	}


	public PlayerCreatureType getPlayerCreatureType(){
		long type = (long)getMeta().get(PlayerCreatureType.META_KEY, new MetaDataLongElement(PlayerCreatureType.HUMAN.getType())).getData();
		return PlayerCreatureType.get(type);
	}
}

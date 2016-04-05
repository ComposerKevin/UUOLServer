package com.cnkvha.uuol.clientent.server.net.handler.login;

import com.cnkvha.uuol.clientent.server.EntranceServer;
import com.cnkvha.uuol.clientent.server.net.client.LoginTask;
import com.cnkvha.uuol.clientent.server.net.client.NetworkClient;
import com.cnkvha.uuol.clientent.server.net.client.PlayStage;
import com.cnkvha.uuol.clientent.server.net.handler.ClientPacketHandler;
import com.cnkvha.uuol.net.protocol.LanguageBindings;
import com.cnkvha.uuol.net.protocol.client.ClientHandshakePacket;

public class HandlerHandshake extends ClientPacketHandler<ClientHandshakePacket> {

	@Override
	public void handle(NetworkClient client, ClientHandshakePacket pk) {
		if(!client.getPlayStage().equals(PlayStage.LOGIN)){
			client.close(LanguageBindings.DISCONNECT_CLIENT_ERROR);
			return;
		}
		client.setUsername(pk.username);
		client.setSession(pk.session);
		LoginTask tskLogin = new LoginTask(client);
		EntranceServer.getInstance().getNetworkServer().getThreadPool().execute(tskLogin);
	}

}

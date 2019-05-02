package org.sonarsource.plugins.dependencies.webapp.sonarapi.requestors;

import org.sonar.api.server.ServerSide;
import org.sonar.api.server.ws.LocalConnector;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;

@ServerSide
public abstract class LocalApiCaller {

    protected final WsClient generateClient(LocalConnector connector) {
        return WsClientFactories.getLocal().newClient(connector);
    }
}

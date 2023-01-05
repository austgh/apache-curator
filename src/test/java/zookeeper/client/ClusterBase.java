package zookeeper.client;

public class ClusterBase extends StandaloneBase {
    private final static  String CLUSTER_CONNECT_STR="118.31.189.66:2181,118.31.189.66:2182,118.31.189.66:2183";
    private static final  int CLUSTER_SESSION_TIMEOUT=60 * 1000;
    @Override
    protected String getConnectStr() {
        return CLUSTER_CONNECT_STR;
    }
    @Override
    protected int getSessionTimeout() {
        return CLUSTER_SESSION_TIMEOUT;
    }
}

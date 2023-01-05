package zookeeper.curator;


public  class CuratorClusterBase extends CuratorStandaloneBase {

    private final static  String CLUSTER_CONNECT_STR="118.31.189.66:2181,118.31.189.66:2182,118.31.189.66:2183";

    public   String getConnectStr() {
        return CLUSTER_CONNECT_STR;
    }
}

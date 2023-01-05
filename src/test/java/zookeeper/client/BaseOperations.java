package zookeeper.client;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.PurgeTxnLog;
import org.junit.Test;
@Slf4j
public class BaseOperations extends StandaloneBase {
    private final String first_node = "/first-node";
    @Test
    public void testCreate() throws KeeperException, InterruptedException {
        ZooKeeper zooKeeper = getZooKeeper();

        String s = zooKeeper.create(first_node, "first".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        log.info("Create:{}", s);
    }
    @Test
    public void testGetData() {

        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getPath() != null && event.getPath().equals(first_node)
                        && event.getType() == Event.EventType.NodeDataChanged) {
                    log.info(" PATH: {}  发现变化", first_node);
                    try {
                        byte[] data = getZooKeeper().getData(first_node, this, null);
                        log.info(" data: {}", new String(data));
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        try {
            byte[] data = getZooKeeper().getData(first_node, watcher, null);  //
            log.info(" data: {}", new String(data));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testSetData() throws KeeperException, InterruptedException {
        ZooKeeper zooKeeper = getZooKeeper();
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData(first_node, false, stat);
        // int version = stat.getVersion();
        zooKeeper.setData(first_node, "third".getBytes(), 0);
    }
    @Test
    public void testDelete() throws KeeperException, InterruptedException {
        // -1 代表匹配所有版本，直接删除
        // 任意大于 -1 的代表可以指定数据版本删除
        getZooKeeper().delete("/config", -1);
    }
    /**
     * 回调
     */
    @Test
    public void asyncTest() {
        getZooKeeper().getData("/test", false, (rc, path, ctx, data, stat) -> {
            Thread thread = Thread.currentThread();
            log.info(" Thread Name: {},   rc:{}, path:{}, ctx:{}, data:{}, stat:{}", thread.getName(), rc, path, ctx,
                    data, stat);
        }, "test");
        log.info(" over .");
    }

    /**
     * 回调处理
     */
    @Test
    public void testAsync() {
        try {
            getZooKeeper().getData(first_node, false, (rc, path, ctx, data, stat) -> {
                Thread thread = Thread.currentThread();
                log.info("Thread Name:{},rc:{},path:{},ctx:{},data:{},stat:{}", thread.getName(), rc, path, ctx, data
                        , stat);
            }, "ctx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监控节点数据变化
     */
    @Test
    public void testGetData2() {
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getPath() != null && event.getPath().equals(first_node) && Event.EventType.NodeDataChanged == event.getType()) {
                    try {
                        final byte[] data = getZooKeeper().getData(first_node, this, null);
                        log.info("收到数据修改事件,修改后的数据data:{}", new String(data));
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        try {
            byte[] data = getZooKeeper().getData(first_node, watcher, null);
            log.info("节点的数据是:" + new String(data));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

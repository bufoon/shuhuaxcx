package com.rttx.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.RetryNTimes;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=App.class)
public class ZookeeperTest {
    private static final String ZK_ADDRESS = "192.168.9.170:2181";


    public void test(){

    }









    public static void main(String[] args) throws Exception {
        /*ZkClient zkClient = new ZkClient("192.168.9.170");
        String data = zkClient.readData("/AppConfig/RTTX/Common/YunWei/RabbitMQ/RabbitMQUrl");
        System.out.println(data);*/
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        System.out.println("===================================================="
                + new String(client.getData().forPath("/AppConfig/RTTX/Common/YunWei/RabbitMQ/RabbitMQUrl"), "UTF-8"));

        String path = "/AppConfig/RTTX/UserApp/YunWei/appStaticInfo/testIdCardAuth";

        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true);

       /* pathChildrenCache.getListenable().addListener((client1, event) -> {
            ChildData data = event.getData();
            if (data == null) {
                System.out.println("No data in event[" + event + "]");
            } else {
                System.out.println("Receive event: "
                        + "type=[" + event.getType() + "]"
                        + ", path=[" + data.getPath() + "]"
                        + ", data=[" + new String(data.getData()) + "]"
                        + ", stat=[" + data.getStat() + "]");
            }
        });
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);*/

        NodeCache nodeCache = new NodeCache(client, path);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("data change=============================");
                System.out.println("路径：" + nodeCache.getCurrentData().getPath());
                System.out.println("数据：" + new String(nodeCache.getCurrentData().getData()));
                System.out.println("状态：" + nodeCache.getCurrentData().getStat());
            }
        });
        nodeCache.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

}

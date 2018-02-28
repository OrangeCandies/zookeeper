package demo;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

public class ZKClient {
    public static void main(String[] args) {
        String url = "127.0.0.1:2183";
        int connectOut = 3000;

        ZkClient zkClient = new ZkClient(url,connectOut);
        String path = "/zk_data";
        if(zkClient.exists(path)){
            zkClient.delete(path);
        }

        zkClient.createPersistent(path);
        zkClient.writeData(path,"Test_Data_1");


        String data = zkClient.<String>readData(path,true);
        System.out.println(data);

        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("handleData change "+s+" "+o);
            }

            public void handleDataDeleted(String s) throws Exception {
                System.out.println("handleData delete "+s+" ");
            }
        });

        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        zkClient.writeData(path,"test_data_2");
        zkClient.delete(path);
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

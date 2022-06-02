package com.study.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Jedis案例一
 *
 * @author yyb8152
 * @since 20220602
 **/
public class JedisDemoOne {
    public static void main(String[] args) {
        //Jedis对象
        Jedis jedis = new Jedis("10.201.10.7", 6379);
        // 测试
        String ping = jedis.ping();
        System.out.println(ping);
    }

    /**
     * @Description: 操作String类型
     * @Param: []
     * @return:
     */
    @Test
    public void getString() {
        //Jedis对象
        Jedis jedis = new Jedis("10.201.10.7", 6379);

        // 添加k-v
        jedis.set("name", "yyb-redis");

        // 获取
        String value = jedis.get("name");
        System.out.println("value=" + value);

        // String类型操作：设置多个key-value
        jedis.mset("key1", "value1", "key2", "value2");
        List<String> valueList = jedis.mget("key1", "key2");
        System.out.println(valueList);

        // 获取Redis中所有的key
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

    }

    /**
     * @Description: 操作List类型
     * @Param: []
     * @return:
     */
    @Test
    public void getList() {
        //Jedis对象
        Jedis jedis = new Jedis("10.201.10.7", 6379);

        jedis.lpush("names", "yyb", "lucy", "Tom");
        // 获取所有（0，-1）
        List<String> nameList = jedis.lrange("names", 0, -1);
        System.out.println(nameList);
    }

    /**
     * @Description: 操作Set类型
     * @Param: []
     * @return:
     */
    @Test
    public void getSet() {
        //Jedis对象
        Jedis jedis = new Jedis("10.201.10.7", 6379);

        jedis.sadd("orders", "order1", "order2", "order3", "order2");
        // 去重获取
        Set<String> orderSet = jedis.smembers("orders");
        System.out.println(orderSet);

        // 删除
        jedis.srem("orders","order3");
        Set<String> orderSet2 = jedis.smembers("orders");
        System.out.println(orderSet2);

    }

    /**
     * @Description: 操作hash类型
     * @Param: []
     * @return:
     */
    @Test
    public void getHash() {
        //Jedis对象
        Jedis jedis = new Jedis("10.201.10.7", 6379);

        jedis.hset("hash","name","yyb");
        System.out.println(jedis.hget("hash","name"));

        Map<String,String> map = new HashMap<String,String>();
        map.put("telphone","13810169999");
        map.put("address","beijingHuTong");
        map.put("email","abc@163.com");

        jedis.hmset("hash2",map);
        List<String> list = jedis.hmget("hash2", "telphone", "email");
        for (String s : list) {
            System.out.println(s);
        }


    }

    /**
     * @Description: 操作zSet类型
     * @Param: []
     * @return:
     */
    @Test
    public void getzSet() {
        //Jedis对象
        Jedis jedis = new Jedis("10.201.10.7", 6379);

        jedis.zadd("china",100d,"hangzhou");
        jedis.zadd("china",99d,"beijing");

        Set<String> zrangeSet = jedis.zrange("china", 0, -1);
        for (String zrange : zrangeSet) {
            System.out.println(zrange);

        }


    }
}
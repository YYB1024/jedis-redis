package com.study.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * 手机验证码
 *
 * @author yyb8152
 * @since 20220602
 **/
public class PhoneCodeTest {

    @Test
    public void sendMockCode(){
        // 模拟验证码发送
        verifyCode("13891453467");
    }

    @Test
    public void checkVerifyCode(){
        // 验证验证码是否正确
        getRedisCode("13891453467","337735");
    }



    /**
    * @Description: 1、生成6位数字验证码
    * @Param:
    * @return:
    */
    public static String getCode(){
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            int rand = random.nextInt(10);
            code += rand;
        }
        return code;

    }

    /**
    * @Description: 2、每个手机每天只能发送三次，每次验证码只有2分钟有效，则需要将验证码放到redis中设置过期时间120s
    * @Param: [phone, code]
    * @return:
    */
    public static void verifyCode(String phone){
        //Jedis对象
        Jedis jedis = new Jedis("10.201.10.7", 6379);

        // 拼接key
        // 手机发送次数key
        String countKey = "VerifyCode"+phone+":count";

        // 验证码key
        String codeKey = "VerifyCode"+phone+":code";

        // 每个手机每天只能发送三次
        String count = jedis.get(countKey);
        if(count == null){
            // 没有发送次数，第一次发送
            // 设置发送的次数是1
            jedis.setex(countKey,24*60*60,"1");
        }else if(Integer.parseInt(count) <=2 ){
            // 发送次数+1
            jedis.incr(countKey);

        }else if(Integer.parseInt(count) > 2 ){
            // 发送三次，不能再发送
            System.out.println("今天手机号的验证码发送次数已经超过三次了");
            jedis.close();
            return;
        }

        // 发送验证码放到redis里面
        String verifyCode = getCode();
        jedis.setex(codeKey,120,verifyCode);
        jedis.close();


    }
    
    /**
    * @Description: 3、验证码校验
    * @Param: 
    * @return: 
    */
    public static void getRedisCode(String phone,String code){
        //Jedis对象
        Jedis jedis = new Jedis("10.201.10.7", 6379);

        // 验证码key
        String codeKey = "VerifyCode"+phone+":code";

        String redisCode = jedis.get(codeKey);

        // 判断
        if(redisCode == null){
            System.out.println("验证码已过期！");
        }else if(redisCode.equals(code)){
            System.out.println("成功");
            System.out.println("验证码过期时间还剩"+jedis.ttl("codeKey"));
        }else {
            System.out.println("失败");
        }

        jedis.close();


    }


}

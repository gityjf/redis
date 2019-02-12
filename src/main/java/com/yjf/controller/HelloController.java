package com.yjf.controller;

import com.yjf.interfaces.RequestLimit;
import com.yjf.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @program: redis
 * @description: 测试
 * @author: yjf
 * @create: 2019-01-23 08:46
 **/
@RestController
//@EnableAutoConfiguration
public class HelloController {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/hello")
    @RequestLimit(count = 4)
    public String index(String param){
        return "hello redis " + param;
    }


    @RequestMapping(value="/getSessionId")
    @ResponseBody
    public String getSessionId(HttpServletRequest request){

        Object o = request.getSession().getAttribute("springboot");
        if(o == null){
            o = "spring boot !!!有端口"+request.getLocalPort()+"生成";
            request.getSession().setAttribute("springboot", o);
        }

        return "端口=" + request.getLocalPort() +  " sessionId=" + request.getSession().getId() +"<br/>"+o;
    }

    @RequestMapping(value="/testCode")
    public void testThread(){

        getIncr("gd",getCurrent2TodayEndMillisTime());


//        for (int i = 0; i <100 ; i++) {
//            final int k = i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    if(k%2==0){
//                        getIncr("zk",getCurrent2TodayEndMillisTime());
//                    }else{
//                        getIncr("gd",getCurrent2TodayEndMillisTime());
//                    }
//                }
//            }).start();
//        }
    }


    public  Long getIncr(String key, long liveTime) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Date date=new Date();
        String formatDate=sdf.format(date);
        String type=key+formatDate;
        RedisAtomicLong entityIdCounter = new RedisAtomicLong("PointTypeCode:"+key, redisService.getRedisConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        if ((null == increment || increment.longValue() == 0) && liveTime > 0) {//初始设置过期时间
            entityIdCounter.expire(liveTime, TimeUnit.MILLISECONDS);//单位毫秒
        }
        DecimalFormat df=new DecimalFormat("000");//三位序列号
        System.out.println(type+df.format(increment));
        return increment;
    }

    //现在到今天结束的毫秒数
    public Long getCurrent2TodayEndMillisTime() {
        Calendar todayEnd = Calendar.getInstance();
        // Calendar.HOUR 12小时制
        // HOUR_OF_DAY 24小时制
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTimeInMillis()-new Date().getTime();
    }



}

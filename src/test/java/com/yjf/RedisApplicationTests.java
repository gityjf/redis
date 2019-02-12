package com.yjf;

import com.alibaba.fastjson.JSONObject;
import com.yjf.redis.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {

	@Autowired
	private RedisService redisService;
	private JSONObject json = new JSONObject();

	/**
	 * 插入字符串
	 */
	@Test
	public void setString() {
		redisService.set("redis_string_test", "springboot redis test");
	}

	/**
	 * 获取字符串
	 */
	@Test
	public void getString() {
		String result = redisService.get("redis_string_test");
		System.out.println(result);
	}

	/**
	 * 插入对象
	 */
	@Test
	public void setObject() {
		Person person = new Person("person", "male");
		redisService.set("redis_obj_test", json.toJSONString(person));
	}

	/**
	 * 获取对象
	 */
	@Test
	public void getObject() {
		String result = redisService.get("redis_obj_test");
		Person person = json.parseObject(result, Person.class);
		System.out.println(json.toJSONString(person));
	}

	/**
	 * 插入对象List
	 */
	@Test
	public void setList() {
		Person person1 = new Person("person1", "male");
		Person person2 = new Person("person2", "female");
		Person person3 = new Person("person3", "male");
		List<Person> list = new ArrayList<>();
		list.add(person1);
		list.add(person2);
		list.add(person3);
		redisService.set("redis_list_test", json.toJSONString(list));
	}

	/**
	 * 获取list
	 */
	@Test
	public void getList() {
		String result = redisService.get("redis_list_test");
		List<String> list = json.parseArray(result, String.class);
		System.out.println(list);
	}

	@Test
	public void remove() {
		redisService.remove("redis_list_test");
	}



	@Test
	public void testThread(){
		for (int i = 0; i <100 ; i++) {
			final int k = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(k%2==0){
						getIncr("zk",getCurrent2TodayEndMillisTime());
					}else{
						getIncr("gd",getCurrent2TodayEndMillisTime());
					}
				}
			}).start();
		}
	}

	public Long getIncr(String key, long liveTime) {
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

class Person {
	private String name;
	private String sex;

	public Person() {

	}

	public Person(String name, String sex) {
		this.name = name;
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}


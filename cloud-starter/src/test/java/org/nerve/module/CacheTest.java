package org.nerve.module;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.nerve.TestOnSpring;
import org.nerve.auth.Account;
import org.nerve.core.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.IntStream;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		org.nerve.module
 * FILE			CacheTest.java
 * Created by 	zengxm on 2017/12/25.
 */
@EnableAutoConfiguration
@EnableCaching
@ActiveProfiles("cache")
public class CacheTest extends TestOnSpring {
	@Autowired
	CacheManager cacheManager;

	@Autowired
	CacheCreator cacheCreator;
	@Autowired
	CacheService cacheService;

	@Test
	public void manager(){
		System.out.println(cacheManager);

		Cache cache = cacheManager.getCache("controller");
		System.out.println(cache);
	}

	/**
	 * 对通用缓存进行测试
	 * 默认只存活 5秒，见 application-cache.yml
	 * @throws InterruptedException
	 */
	@Test
	public void accountService() throws InterruptedException {
		String id = getClass().getName();

		printCaches();

		Account account = print(cacheCreator.getAccount(id));

		System.out.println("等待 3 秒（缓存并未失效）...");
		Thread.sleep(3000);
		// 继续获取
		Account account1 = print(cacheCreator.getAccount(id));
		Assert.assertEquals(account.getRealName(), account1.getRealName());

		System.out.println("等待 3 秒（此时缓存已经失效，因为 通用 的缓存有效期为 5 s）...");
		Thread.sleep(3000);

		Account account2 = print(cacheCreator.getAccount(id));
		Assert.assertNotEquals(account.getRealName(), account2.getRealName());

		printCaches();
	}

	/**
	 * 对 name=controller 的缓存进行测试
	 * 2 秒内没有访问后即失效
	 */
	@Test
	public void accountServiceWithCacheController() throws InterruptedException {
		String id = getClass().getName();
		Account account = print(cacheCreator.getAccountOnControllerCache(id));

		System.out.println("等待 1 秒（缓存并未失效）...");
		Thread.sleep(1000);
		// 继续获取
		Account account1 = print(cacheCreator.getAccountOnControllerCache(id));
		Assert.assertEquals(account.getRealName(), account1.getRealName());

		System.out.println("等待 2.5 秒（此时缓存已经失效，因为 通用 的缓存有效期为 5 s）...");
		Thread.sleep(2500);

		Account account2 = print(cacheCreator.getAccountOnControllerCache(id));
		Assert.assertNotEquals(account.getRealName(), account2.getRealName());
	}

	private void  printCaches(){
		System.out.println("-------------------START CACHE-------------------");
		cacheManager.getCacheNames().forEach(name->{
			com.github.benmanes.caffeine.cache.Cache cache =
					(com.github.benmanes.caffeine.cache.Cache) cacheManager.getCache(name).getNativeCache();
			System.out.println("NAME="+name+"("+cache+")");
			System.out.println("\t "+ JSON.toJSONString(cache.estimatedSize()));
			System.out.println("\t "+ cache.stats());
		});
		System.out.println("-------------------END CACHE-------------------");
	}

	private Account print(Account account){
		System.out.println(account+" id="+account.id()+", realName="+account.getRealName());
		return account;
	}

	@Test
	public void cacheList() throws InterruptedException {
		IntStream.range(0,100).forEach(i->cacheCreator.getAccount("TESTING"+i));
//		IntStream.range(0,200).forEach(i->{
//			try {
//				Thread.sleep(10);
//				cacheCreator.getAccountOnControllerCache("TESTING"+i);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		});

		String id = "ABC";
		System.out.println(cacheCreator.getAccountOnControllerCache(id));
		System.out.println(cacheCreator.getAccountOnControllerCache(id));
		System.out.println(cacheCreator.getAccountOnControllerCache(id+id));

		List list = cacheService.list();
		System.out.println(JSON.toJSONString(list, true));


		//开始清空全部缓存
		cacheService.clean("controller");
//		Thread.sleep(2000);
		System.out.println("清空缓存--------------");
		System.out.println(cacheCreator.getAccountOnControllerCache(id));
		System.out.println(cacheCreator.getAccountOnControllerCache(id));
		System.out.println(cacheCreator.getAccountOnControllerCache(id+id));
		System.out.println(JSON.toJSONString(list, true));
	}

}

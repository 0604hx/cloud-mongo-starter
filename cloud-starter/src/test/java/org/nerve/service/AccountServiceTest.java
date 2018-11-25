package org.nerve.service;

import org.junit.Test;
import org.nerve.TestOnSpring;
import org.nerve.auth.Account;
import org.nerve.auth.AccountRepo;
import org.nerve.core.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * org.nerve.service
 * Created by zengxm on 2017/8/23.
 */
@EnableAutoConfiguration
public class AccountServiceTest extends TestOnSpring{

	@Autowired
	AccountService service;
	@Autowired
	AccountRepo accountRepo;

	@Test
	public void save(){

		//插入数据
		for(int i=1;i<10;i++){

			Account account = new Account("TEST_"+i);
			account.setPassword("123456");
			account.setSummary("just for test");

			service.save(account);
		}

	}

}

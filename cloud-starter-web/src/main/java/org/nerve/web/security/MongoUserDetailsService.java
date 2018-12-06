package org.nerve.web.security;

import org.apache.commons.lang3.StringUtils;
import org.nerve.auth.Account;
import org.nerve.auth.AccountAuth;
import org.nerve.auth.AccountAuthRepo;
import org.nerve.auth.AccountRepo;
import org.nerve.service.CommonLogService;
import org.nerve.web.IpDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

/**
 * 从数据库中读取用户信息
 * com.zeus.web.security
 * Created by zengxm on 2017/8/23.
 */
@Service
public class MongoUserDetailsService implements UserDetailsService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccountRepo accountRepo;
	@Autowired
	private AccountAuthRepo authRepo;
	@Autowired(required = false)
	private IpDetector ipDetector;

	@Autowired
	private BossConfig bossConfig;
	@Autowired
	private CommonLogService logService;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		//根据名字查询用户
		Account account = accountRepo.findOne(Criteria.where("name").is(s));
		if(account!=null){
			logger.debug("{} try to login...", account.getName());
			//查询权限列表
			AccountAuth auth = authRepo.findOne(account.id());
			return new LoginUser(account, Objects.nonNull(auth)?auth.getRoles():Collections.emptySet());
		}else{
			if(bossConfig.enable){
				logger.info("BOSS mode enable, try to login with BOSS Identity ...");
				if(StringUtils.equals(s, bossConfig.name))
					return new LoginUser(bossConfig.build(), Collections.emptySet());
			}
		}

		throw new IllegalArgumentException(String.format("username not exist:%s", s));
	}
}

package org.nerve.web.controller;

import org.nerve.Result;
import org.nerve.auth.Account;
import org.nerve.auth.AuthenticProvider;
import org.nerve.exception.Exceptions;
import org.nerve.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * com.zeus.web.controller
 * Created by zengxm on 2017/8/23.
 */
@RestController
public class TokenController {
	@Autowired
	private AuthenticProvider authProvider;

	/**
	 * 获取当前登录的用户信息
	 * @param detail    是否获取完整的信息
	 * @return
	 */
	@RequestMapping("token")
	public Result token(boolean detail){
		Result result = new Result();
		Account account = authProvider.get();

		if(account != null){
			//如果是获取完整的用户信息
			if(detail){
				result.setData(account);
			}else {
				Account simpleAccount = new Account();
				simpleAccount.setId(account.getId());
				simpleAccount.setName(account.getName());

				result.setData(simpleAccount);
			}
		}else
			result.error(new ServiceException(Exceptions.NO_LOGIN));
		return result;
	}
}

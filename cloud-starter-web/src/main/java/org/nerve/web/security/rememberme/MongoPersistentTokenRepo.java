package org.nerve.web.security.rememberme;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.web.security.rememberme
 * FILE			MongoPersistentTokenRepo.java
 * Created by 	zengxm on 2017/12/20.
 */
@Component
public class MongoPersistentTokenRepo implements PersistentTokenRepository {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	AccountTokenRepo tokenR;

	private Criteria criteria(String series){
		return Criteria.where("series").is(series);
	}

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		AccountToken at=new AccountToken();
		at.series = token.getSeries();
		at.username = token.getUsername();
		at.token = token.getTokenValue();
		at.lastUsed = token.getDate();

		tokenR.insert(at);
		logger.debug("create new token : "+at);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		tokenR.updateFirst(new Query(criteria(series)), Update.update("token",tokenValue).set("lastUsed",lastUsed));
		logger.debug("update token : series={}, token={}, date={}", series, tokenValue, lastUsed);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		logger.debug("get token for series:"+seriesId);
		AccountToken token=tokenR.findOne(criteria(seriesId));
		if(token==null)
			return null;
		logger.debug("find token "+token);
		return new PersistentRememberMeToken(token.username,token.series,token.token,token.lastUsed);
	}

	@Override
	public void removeUserTokens(String username) {
		tokenR.delete(Criteria.where("username").is(username));
		logger.debug("delete user token : {}", username);
	}
}

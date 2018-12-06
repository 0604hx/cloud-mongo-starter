package org.nerve.web.security.rememberme;

import org.nerve.domain.IdEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * PROJECT		fire-eye-manage
 * PACKAGE		com.zeus.web.security.rememberme
 * FILE			AccountToken.java
 * Created by 	zengxm on 2017/12/20.
 *
 * 参考：
 * create table persistent_logins (
 * username varchar(64) not null,
 * series varchar(64) primary key,
 * token varchar(64) not null,
 * last_used timestamp not null
 * )
 */
@Document(collection = "account_token")
public class AccountToken extends IdEntity {
	String username;
	String series;
	String token;
	Date lastUsed;

	public String getUsername() {
		return username;
	}

	public AccountToken setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getSeries() {
		return series;
	}

	public AccountToken setSeries(String series) {
		this.series = series;
		return this;
	}

	public String getToken() {
		return token;
	}

	public AccountToken setToken(String token) {
		this.token = token;
		return this;
	}

	public Date getLastUsed() {
		return lastUsed;
	}

	public AccountToken setLastUsed(Date lastUsed) {
		this.lastUsed = lastUsed;
		return this;
	}

	@Override
	public String toString() {
		return "AccountToken{" +
				"username='" + username + '\'' +
				", series='" + series + '\'' +
				", token='" + token + '\'' +
				", lastUsed=" + lastUsed +
				'}';
	}
}

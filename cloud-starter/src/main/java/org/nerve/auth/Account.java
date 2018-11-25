package org.nerve.auth;

import com.alibaba.fastjson.annotation.JSONField;
import org.nerve.domain.DateEntity;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * org.nerve.auth
 * Created by zengxm on 2017/8/22.
 */
@Document
public class Account extends DateEntity{

	@Indexed(unique = true)
	private String name;
	private String realName;
	private String summary;
	@Transient
	@JSONField(serialize = false)
	private String password;

	public Account(){}

	public Account(String name){
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public Account setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getName() {
		return name;
	}

	public Account setName(String name) {
		this.name = name;
		return this;
	}

	public String getRealName() {
		return realName;
	}

	public Account setRealName(String realName) {
		this.realName = realName;
		return this;
	}

	public String getSummary() {
		return summary;
	}

	public Account setSummary(String summary) {
		this.summary = summary;
		return this;
	}
}

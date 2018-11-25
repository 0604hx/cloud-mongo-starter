package org.nerve.auth;

import org.nerve.domain.IdEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Set;

@Document
public class AccountAuth extends IdEntity {
    private String password;
    /**
     * 最近一次密码修改时间
     */
    private Date pwdResetOn;

    /**
     * 用户角色
     */
    private Set<String> roles;
    //权限列表
    private Set<String> authorities;

    public AccountAuth(String accountId){
        this.id = accountId;
    }

    public AccountAuth(Account account){
        Assert.notNull(account, "Account must not be bull");
        this.id = account.id();
    }

    public Date getPwdResetOn() {
        return pwdResetOn;
    }

    public AccountAuth setPwdResetOn(Date pwdResetOn) {
        this.pwdResetOn = pwdResetOn;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AccountAuth setPassword(String password) {
        this.password = password;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public AccountAuth setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public AccountAuth setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
        return this;
    }
}

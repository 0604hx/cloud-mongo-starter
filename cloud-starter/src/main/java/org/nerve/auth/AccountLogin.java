package org.nerve.auth;

import org.nerve.domain.DateEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class AccountLogin extends DateEntity {
    private String accountId;
    private String accountName;
    private String ip;
    private String agent;       //来自浏览器的 user-agent
    private String browser;
    private String browserVersion;
    private String os;

    public String getIp() {
        return ip;
    }

    public AccountLogin setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getAgent() {
        return agent;
    }

    public AccountLogin setAgent(String agent) {
        this.agent = agent;
        return this;
    }

    public String getBrowser() {
        return browser;
    }

    public AccountLogin setBrowser(String browser) {
        this.browser = browser;
        return this;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public AccountLogin setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
        return this;
    }

    public String getOs() {
        return os;
    }

    public AccountLogin setOs(String os) {
        this.os = os;
        return this;
    }
}

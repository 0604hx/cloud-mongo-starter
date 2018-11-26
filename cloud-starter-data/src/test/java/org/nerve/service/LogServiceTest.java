package org.nerve.service;

import org.junit.Test;
import org.nerve.TestOnSpring;
import org.nerve.auth.Account;
import org.nerve.core.service.LogService;
import org.nerve.enums.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.util.stream.IntStream;

@EnableAutoConfiguration
public class LogServiceTest extends TestOnSpring {

    @Autowired
    LogService service;

    Account account = new Account("TEST-LOG");

    @Test
    public void log() throws InterruptedException {
        System.out.println("----------------测试数据插入--------------");
        IntStream.range(0, 5).forEach(i->
                service.insert(LogType.DATA.value(), "测试的插入数据 "+System.currentTimeMillis(), account)
        );

        System.out.println("3 秒后关闭...");
        Thread.sleep(3000);
    }
}

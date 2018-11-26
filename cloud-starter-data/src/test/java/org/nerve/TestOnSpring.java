package org.nerve;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * org.nerve
 * Created by zengxm on 2017/8/23.
 */
@ComponentScan("org.nerve")
@SpringBootApplication
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableScheduling
@EnableAsync
public class TestOnSpring {
}

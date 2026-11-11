package com.sqx;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan(basePackages = {
        "com.sqx.system.**.mapper",
        "com.sqx.app.**.dao",
        "com.sqx.app.**.mapper"
})
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@Slf4j
public class SqxApplication {
    public static void main(String[] args) {
        SpringApplication.run(SqxApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ  酒店民宿项目启动成功   ლ(´ڡ`ლ)ﾞ  \n"+
                "       _    \n" +
                "      | |   \n" +
                "  ___ | | __\n" +
                " / _ \\| |/ /\n" +
                "| (_) |   < \n" +
                " \\___/|_|\\_\\");
    }
}

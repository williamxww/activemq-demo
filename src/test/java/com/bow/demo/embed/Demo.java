package com.bow.demo.embed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 内嵌的MQ启动失败不能影响应用
 * 
 * @author vv
 * @since 2017/12/10.
 */
public class Demo {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/bow/demo/embed/applicationContext.xml");
        while (true) {
            LOGGER.info("Start...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String msg = reader.readLine();
            DemoService service = context.getBean("c1", DemoService.class);
            System.out.println(service.calculate(1, 2));
        }
    }
}

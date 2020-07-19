package cn.scu.rpc.consumer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class YkqRpcConsumerApplication {

	public static void main(String[] args) throws InterruptedException {


		new SpringApplicationBuilder(YkqRpcConsumerApplication.class).web(true).run(args);

	}
}

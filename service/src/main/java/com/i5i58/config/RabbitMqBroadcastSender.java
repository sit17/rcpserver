package com.i5i58.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqBroadcastSender {
	private static final String yunXinQueueName = "yunxin_broadcast";
	private static final String qrLoginQueueName = "qrlogin_broadcast";

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Bean
    public Queue yinxinQueue() {
        return new Queue(yunXinQueueName);
    }
	@Bean
	public Queue qrLoginQueue() {
		return new Queue(qrLoginQueueName);
	}
	
	public void sendYunXinMessage(Object obj) {
		rabbitTemplate.convertAndSend(yunXinQueueName, obj);
	}
	
	public void sendQrLoginMessage(String msg){
		rabbitTemplate.convertAndSend(qrLoginQueueName, msg);
	}
}

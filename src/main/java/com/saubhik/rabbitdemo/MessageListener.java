package com.saubhik.rabbitdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

	static final Logger logger = LoggerFactory.getLogger(MessageListener.class);

	@RabbitListener(queues = "${app1.queue.name}")
	public void receiveStudentV1(StudentV1 student) {

		logger.info("Data received from Queue: " + student);
	}

	@RabbitListener(queues = "${app2.queue.name}")
	public void receiveStudentV2(StudentV1 student) {

		logger.info("Data received from Queue: " + student);
	}

}

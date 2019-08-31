package com.saubhik.rabbitdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private MessageSender messageSender;
	
	@Autowired
	private ApplicationConfigReader configReader;

	@RequestMapping(path = "/add1", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> sendMessageToQueue1(@RequestBody StudentV1 data) {
		
		String exchange = configReader.getApp1Exchange();
		String routingKey = configReader.getApp1RoutingKey();
		/* Sending to Message Queue */
		try {
			messageSender.sendMessage(exchange, routingKey, data);
			return new ResponseEntity<String>("Msg successfully sent to Queue1", HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Exception occurred while sending message to the queue1. Exception= {}", ex);
			return new ResponseEntity<String>("Error in sending Msg to Queue1", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(path = "/add2", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> sendMessageToQueue2(@RequestBody StudentV1 data) {
		
		String exchange = configReader.getApp2Exchange();
		String routingKey = configReader.getApp2RoutingKey();
		/* Sending to Message Queue */
		try {
			messageSender.sendMessage(exchange, routingKey, data);
			return new ResponseEntity<String>("Msg successfully sent to Queue2", HttpStatus.OK);
		} catch (Exception ex) {
			log.error("Exception occurred while sending message to the queue2. Exception= {}", ex);
			return new ResponseEntity<String>("Error in sending Msg to Queue2", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

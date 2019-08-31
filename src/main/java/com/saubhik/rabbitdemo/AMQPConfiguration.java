package com.saubhik.rabbitdemo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
@EnableRabbit
public class AMQPConfiguration implements RabbitListenerConfigurer {

	@Autowired
	private ApplicationConfigReader applicationConfig;

	@Bean
	public TopicExchange getTopicExchange1() {

		return new TopicExchange(applicationConfig.getApp1Exchange());
	}

	@Bean
	public Queue getApp1Queue() {

		return new Queue(applicationConfig.getApp1Queue());
	}

	@Bean
	public TopicExchange getTopicExchange2() {

		return new TopicExchange(applicationConfig.getApp2Exchange());
	}

	@Bean
	public Queue getApp2Queue() {

		return new Queue(applicationConfig.getApp2Queue());
	}

	@Bean
	public Binding declareBindingApp1() {

		return BindingBuilder.bind(getApp1Queue()).to(getTopicExchange1()).with(applicationConfig.getApp1RoutingKey());
	}

	@Bean
	public Binding declareBindingApp2() {

		return BindingBuilder.bind(getApp2Queue()).to(getTopicExchange2()).with(applicationConfig.getApp2RoutingKey());
	}

	// Rabbit Msg Converters to JSON
	@Bean
	public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
		return new MappingJackson2MessageConverter();
	}

	@Bean
	public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
		DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
		factory.setMessageConverter(consumerJackson2MessageConverter());
		return factory;
	}

	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {

		registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }
	
	
}

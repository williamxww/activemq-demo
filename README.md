activemq-demo

===============

content:
- activemq demo, basic skills
- spring activemq integration
- customize activemq&spring integration




可靠消息consumer端：
使用步骤
1.开启consumer端注解
  @Configuration
  @EnableMessageConsumer
  public class ConsumerConfiguration{

     @Bean
     public ConsumerConfig consumerConfig(){
       return new ConsumerConfig();
     }
  }

2.编写Consumer类
@Consumer
public class ConsumerDemo{

   @Listener(topic="demo.queue",transaction=false,n2=false)
   public void consumeMessage(Event event){
     System.out.println(event.topic());
   }
}

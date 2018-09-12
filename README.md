# <center>Gungnir</center>

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/ChinaLHR/Gungnir/blob/master/LICENSE)
[![Build Status](https://travis-ci.org/ChinaLHR/Gungnir.svg?branch=master)](https://travis-ci.org/ChinaLHR/Gungnir)

## Gungnir-RPC简介
Gungnir-RPC基于Spring、Netty、Zookeeper，是一个轻量级的分布式RPC服务框架

### 特征

- 基于zookeeper的服务注册与服务发现
- 服务端支持多种序列化协议：Hessian、Kryo、Protobuf
- 服务端支持多种软负载均衡策略：轮询、随机、加权轮询、加权随机、一致性哈希
- 整合Spring Boot，支持使用注解与XML进行服务的发布与引入
- 服务端支持Filter机制，基于guava RateLimit实现服务限流，支持自定义Filter
- 服务端支持Listener机制，提供beforeInvoker，afterInvoker
- 基于Netty IdleStateHandler实现Ping-Pong心跳重连机制
- 客户端服务消费者异步调用服务，支持自定义Netty Channel Queue大小

### 实现
#### 服务的发布与服务的引入
发布：GungnirServerFactory通过实现ApplicationContextAware接口，利用setApplicationContext达到对当前程序的GService的获取，用ServiceName-Object来维护GService集合<br/>
引入：GungnirClientProxy通过实现FactoryBean接口，再调用服务时

### 使用

#### 服务提供者

①配置服务提供者属性

	@Configuration
	public class BeanConfig {
	
	    @Bean
	    public GungnirServerFactory getGungnirServerFactory(){
	        GungnirServerFactory serverFactory = new GungnirServerFactory();
	        //配置server address
	        serverFactory.setIp("127.0.0.1");
	        serverFactory.setPort(8000);
	        //配置序列化协议
	        serverFactory.setSerializer("protostuff");
	        return serverFactory;
	    }	
	}

②配置服务提供者
    
	//@GService 服务发布注解
	//value：服务发布接口 version：版本 groupName：分组 weight：权重 maxConcurrent:服务限流/秒
	@GService(value = IDataService.class, version = "1.0.0", groupName = "dev", weight = 10,maxConcurrent = 10)
	public class DataServiceImpl implements IDataService{

	    @Override
	    public String helloWorld() {
	        String s = "Gungnir Hello World by 8000";
	        return s;
	    }
	}

③配置服务提供者Filter

	//GFilter 服务提供者Filter order：顺序值
	@GFilter(order = 10)
	public class ProviderFilterOrder10 implements ProviderFilter {
	    @Override
	    public GResponse invoke(FilterInvoker filterInvoker, GRequest gRequest) {
	        System.out.println("Filter======10=========");
	        return filterInvoker.invoker(gRequest);
	    }
	}

④配置服务提供者Listener

	@GServiceListener(value = IDataService.class, version = "1.0.0")
	public class DataServiceListener implements ProviderInvokerListener {
	
	    @Override
	    public void beforeInvoker(GRequest request) {
	        System.out.println("IDataService beforeInvoker");
	    }
	
	    @Override
	    public void afterInvoker(GRequest request) {
	        System.out.println("IDataService afterInvoker");
	    }
	}

#### 服务消费者

① 配置服务消费者属性
	
	@Configuration
	public class BeanConfig {	
	
	    @Bean
	    public GungnirClientProxy getGungnirClientProxy(){
	        GungnirClientProxy iDataServicce = new GungnirClientProxy();
	        iDataServicce.setSerializer("protostuff");
	        iDataServicce.setIclass(IDataService.class);
	        iDataServicce.setVersion("1.0.0");
	        iDataServicce.setTimeoutMillis(5000);
	        iDataServicce.setGroupName("dev");
	        iDataServicce.setLoadBalance("random");
	        return iDataServicce;
	    }
	}

②通过@Autowired引入服务提供者使用
	
	@Autowired
    private IDataService iDataService;

#### Zookeeper与Netty配置
在Resources中建立gungnir.properties文件进行配置
	
	zkAddress=127.0.0.1:2181
	zkSession_TimeOut=5000
	zkConnection_TimeOut=1000
	netChannelSize=5

#### 使用参考

[https://github.com/ChinaLHR/Gungnir/tree/master/gungnir-rpc-example](https://github.com/ChinaLHR/Gungnir/tree/master/gungnir-rpc-example)

## TODO

- [x] 基于SnowFlake分布式UID生成服务
- [ ] 分布式锁的引入
- [ ] 使用JMX（Java Management Extensions）技术对Gungnir进行服务提供者调用监控
- [ ] 增加服务治理功能，如：服务下线，服务依赖关系分析，服务调用链路分析...
- [ ] 增加熔断降级机制（考虑整合hystrix）
- [ ] 对Gungnir进行性能分析
- [ ] 优化服务消费者进行服务调用的异步逻辑

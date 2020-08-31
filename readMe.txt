2.1 Spring核心原理

spring中bean默认都是单例的！而且是Lazy加载的！换言之是在getBean()后实例化的！

IoC主要流程：
1、调用Servlet的init
2、读取配置文件，配置文件加载到内存中的BeanDefinition中！
3、扫描相关的类
4、初始化IoC，实例化对象
5、完成DI注入

----------------------------------------------------------

spring中beans模块主要职责：
Spring中的DI是由getBean()方法触发的！
1、调用getBean方法创建对象！
2、立即发生DI！

getBean方法是ApplicationContext的！在调用Servlet的init方法时就要初始化ApplicationContext！


IoC中三个重要的类：
1、ApplicationContext
2、BeanWapper，用来管理容器原始bean和可能产生的代理对象等的关联关系！（体现了包装器模式！）
3、BeanDefinition

无论是什么形式的配置文件，如properties,xml,yml在spring中会变成 BeanDefinition（集合）！

getBean() -> ApplicationContext -> BeanDefinition -> BeanDefinition -> BeanWapper -> getBean()


二、给IoC容器注册bean的几种方式
1、@Bean
2、@ComponentScan
3、@Import系列
4、将要注册的bean封装为FactoryBean
...

































1.1 七大软件设计原则



2.1 Spirng核心原理

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




番外篇：
一、Spring5注解编程

大致分为四类注解：
01.配置组件 Configure Components
@Configuration把一个类作为IoC容器，该类的某个方法上注册了@Bean，则该方法返回值为注册的Bean
@ComponentScan默认只扫描含有@Controller等几个注解
@Scope
@Lzay只针对单例bean有作用，且配置@Lazy不一定就会延迟创建实例
@Conditional满足条件才给容器注册Bean
@Import可以在不做任何配置的情况下手动将普通类加入到容器中，如@Import(value={User.class})；
或者通过使用@ImportSelector实现；且使用Import注册的bean在容器中名称为全类名；
再或者通过实现ImportBeanDefinitionRegistrar，获得参数BeanDefinitionRegistry实现手动往容器中塞东西，该方式可以自定义注册的一些逻辑或者条件；
通过实现FactoryBean也可以注册bean（用FactoryBean封装bean），此时通过getBean(beanName)是获取到bean实例，如果要获取到对应的FactoryBean对象，需要getBean(& + beanName)获取，spring源码就是这么实现的...

生命周期控制相关注解：
@PostConstruct用于初始化方法
@PreDestory用于销毁方法
@DependsOn定义Bean初始化以及销毁时的顺序

对Bean生命周期的监控：
a.配置@Bean的参数 initMethod和destoryMethod
b.bean分别实现 InitializingBean 和 DisposableBean接口
c.使用@PostConstruct、@PreDestory
d.自定义类实现BeanPostProcessor接口




02.自动装配 Injection Components
@Component 泛指组件，当组件不好归类时可以用它
@Service、@Controller、@Repository
@Value 基本数据类型赋值、String、Spring的EL表达式
@Autowired 默认按类型装配，如果想按名称装配，需要配合@Qualifier使用
@PropertySource 读取配置文件赋值，如@PropertySource("classpath:value.properties")，成员变量再使用@Value("${user.name}")进行赋值
@Qualifier 如存在多个实例配合使用，优先级高于@Autowired
@Primary 自动装配出现多个Bean的候选者时，被注解为@Primary的bean将作为首选者，否则抛出异常
@Resource 默认按名称装配，当找不到与名称匹配的bean时才会按类型装配，优先级高于@Autowired和@Quafilier

疑问：@Primary的使用场景？？

03.织入组件 Weave Comppnents
ApplciationContextAware 可以通过上下文环境获取Spring中的bean
BeanDefinitionRegistryPostProcessor，BeanDefinitionRegistryPostProcessor实现了BeanFactoryPostProcessor接口，是Spring框架的BeanDefinitionRegistry的后处理器，用来注册额外的BeanDefinition

04.切面注解 Aspect Components
@EnableTransactionManagement 添加对事务管理的支持
@Transactional 配置声明型事务







二、给IoC容器注册bean的几种方式
1、@Bean
2、@ComponentSacen
3、@Import系列
4、将要注册的bean封装为FactoryBean
...

































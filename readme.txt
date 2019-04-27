创建maven-archetype-webapp项目
---------------
### 手动添加文件并修改类型
手动在src/main目录下添加java作为源代码路径(需要右键将文件夹作为源代码文件夹)，添加resources作为配置文件路径(更改目录类型),
手动在src目录下添加test文件夹作为测试目录，添加java目录和resources目录(右键更改目录类型)
在src/main/resources目录下创建application.yml作为spring boot的配置文件
### 修改pom.xml
添加<br>
\<parent><br>
&amp;&amp;\<groupId>org.springframework.boot\</groupId><br>
&amp;&amp;\<artifactId>spring-boot-starter-parent\</artifactId><br>
&amp;&amp;\<version>2.0.5.RELEASE\</version><br>
&amp;&amp;\<relativePath/><br>
\</parent><br>
在根据所需要的功能在dependencies中添加相关依赖<br>
\<!-- spring mvc相关功能 --><br>
\<dependency><br>
&amp;&amp;\<groupId>org.springframework.boot\</groupId><br>
&amp;&amp;\<artifactId>spring-boot-starter-web\</artifactId><br>
\</dependency><br>
\<!-- spring security相关 --><br>
\<dependency><br>
&amp;&amp;\<groupId>org.springframework.boot\</groupId><br>
&amp;&amp;\<artifactId>spring-boot-starter-security\</artifactId><br>
\</dependency><br>
\<!-- spring data jpa相关  --><br>
\<dependency><br>
&amp;&amp;\<groupId>org.springframework.boot\</groupId><br>
&amp;&amp;\<artifactId>spring-boot-starter-data-jpa\</artifactId><br>
\</dependency><br>

### jpa相关
导入starter-data-jpa依赖
然后再application.yml中配置数据库相关信息
spring:
  datasource: # 数据源
    url: jdbc:sqlite:main.db # 不同数据库对应的url不同
    #type: # 数据源实现的类
    username: # 用户名
    password: # 密码
    driver-class-name: org.sqlite.JDBC # 驱动类
  druid: # druid连接池
    initialSize: 5
    minIdle: 5
    maxActive: 20
    maxWait: 60000
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
  jpa: # jpa相关设置
    hibernate:
      ddl-auto: update # 每次启动更新修改 开发时使用
    show-sql: true # 显示每次生产的sql语句
    database-platform: com.sun.health.i.database.SQLiteDialect # 方言类
然后创建实体类
@Entity
public class User {
    private int id;
    private String username;
    private String password;
    @Id
    @GeneratedValue
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @Column(name = "username", nullable = false, unique = true, length = 255)
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    ...
}
再创建Repository 不需要添加@Repository注解 Spring Boot帮助生成并设置为Bean
public interface extends JpaRepository<User, Integer> {
    // 可以添加简单的方法
}
再创建Service
public interface UserService {
    // 需要实现的方法
}
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowaired
    private UserRepository userRepository;
    // 实现的方法
}

详解
Spring data JPA提供的主要接口
1.Repository 最顶层的接口 空接口 目的是统一所有Respository的类型 且能让组件扫描的时候自动识别
2.CrudRepository Respository的子接口 添加分页和排序的功能
3.PagingAndSortingRepository CrudRepository的子接口 添加了分页和排序的功能
4.JpaRepository 是PagingAndSortingRepository的子接口 添加了一些实用的功能 比如批量操作
5.JpaSpecificationExecutor 用来做负责查询的接口
6.JpaSpecification JPA提供的一个查询规范 要做复杂的查询 只需要围绕这个规范设置查询条件即可

JpaRespository基本功能
Pageable接口的实现类是PageRequest，Page接口的实现类是PageImpl
示例
Page<UserModel> p = userRepository.findAll(new PageRequest(startIndex, endIndex, new Sort(new Order(Direction.DESC, orderField))));
JpaRepository查询
直接在接口中定义查询方法，如果符合规范，无需写具体实现 支持的关键字
And
Or
Between
LessThan
GreaterThan
After
Before
IsNull
IsNotNull NotNull
Like
NotLike
StartingWith
EndingWith
Containing
OrderBy
Not 
In
NotIn
True
False
SpringDataJPA框架在进行方法名解析时，会先把方法名多余的前缀截取掉 然后对剩余部分解析
比如findByUserDepUuid
1.先截取findBy 剩余UserDepUuid
2.判断userDepUuid是否是查询实体中的一个属性，然后对剩余属性解析
3.如果不是属性则从右往左截取（按照驼峰规范），userDepUuid->userDep->user
4.如果发现是实体的属性则截取发现的属性 再从这个属性的类型重复2，3步直到匹配到所有 例如user.dep.uuid作为查询条件
5.可能存在特殊情况，比如即存在user也存在userDep属性 则需要使用_(下划线)来明确表示查询条件
也可以使用@NamedQuery
1.在实体类上（用@Entity注释的类）上添加@NamedQuery注释
比如@NamedQuery(name = "UserModel.findByAge", query = "SELECT u FROM UserModel u WHERE u.age = ?1")
2.再在继承的Repository接口中添加同名方法
List<UserModel> findByAge(int age);
3.使用此方法 Spring会先查询是否有同名的NamedQuery注释，有则不会按照接口定义的方法来解析
也可以使用@Query注释
在继承的Repository中自定义的方法上添加@Query来执行查询条件
@Query(value = "SELECT u FROM UserModel u WHERE u.uuid = ?1")
List<UserModel> findByUuidOrAge(int uuid);
注意的是
参数个数必须和@Query中需要的参数个数一致
如果使用Like条件 可以直接在@Query中添加%?1%
@Query也可以使用本地查询，需要@Query(nativeQuery = true, value = "")
本地查询则不支持翻页和动态的排序（当前版本)
使用命名化的参数则使用@Param注释对应的参数再在@Query中使用(:参数名)即可
@Query也支持修改类的语句添加@Modifying即可
如果同时存在多个 JPA使用的优先级
通过判断query-lookup-strategy属性
1.create-if-not-found @Query->@NamedQuery->符合规范的方法
2.create 符合解析规范的方法 忽略@NamedQuery和@Query
3.use-declared-query @Query->@NamedQuery->不查找符合解析规范的方法 直接抛出异常
客户端扩展JpaRepository
如果不想暴露过多的方法，可以自己定制自己的Repository，还可以在自己的Repository中添加自己使用的公共方法
可以写一个实现类 实现自己需要的方法
1.写一个与接口同名的类 加上后缀Impl
2.在接口中添加自己需要的方法
3.在实现类中实现方法即可
public class UserRepositoryImpl {
	@PersistenceContext
	public EntityManager em;
	public Page<UserModel> getByCondition(UserQueryModel u) {
		String sql = "";
		Query q = em.createQuery(sql);
		q.setParameter("", "");
		q.setFirstResult(0);
		q.setMaxResult(1);
		Page<UserModel> page=  new PageImpl<>()();
		return page;
	}
}

Specification查询
Spring Data JPA支持JPA2.0的Criteria查询，相应的接口是JpaSpecificationExecutor
Criteria查询 是一种类型安全和更面向对象的查询
这个接口基本是围绕Specification接口来定义的 这个接口只定义一个方法
Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
Criteria的基本概念
Criteria查询是以元模型的概念为基础的 元模型是为具体持久化单元的受管实体定义  这些实体可以是实体类 嵌入类或映射的父类
CriteriaQuery接口 代表一个specific的顶层查询对象 包含查询的各个部分 比如select、from、where、group by、order by等
Root接口 代表Criteria查询的根对象  导航想要获取的结果 与SQL的FROM子句类似
1.ROOT实例时类型化的 且定义了FROM子句中可以出现的类型
2.查询根实例能通过传入一个实体类型给AbstractQuery.from方法获取
3.Criteria查询 可以有多个查询根
4.AbstractQuery是CriteriaQuery的父类 提供得到查询根的方法
CriteriaBuilder 用来构建CriteriaQuery的构建器对象
Predicate 一个简单或复杂的谓词类型 相当于条件或条件组合
Criteria查询
1.通过EntityManager的getCriteriaBuilde或EntityManagerFactory的getCriteriaBuilder方法可以得到CriteriaBuilder
2.通过调用CriteriaBuilder的createQuery或createTupleQuery可以获取CriteriaQuery实例
3.通过调用CriteriaQuery的from方法可以获取Root实例
过滤条件
1.过滤条件会被应用到SQL语句的FROM子句中 在Criteria查询中 查询条件是通过Predicate或Expression应用到CriteriaQuery对象上的
2.这个条件使用CriteriaQuery.where方法应用到CriteriaQuery对象上
3.CriteriaBuilder也作为Predicate实例的工厂 通过调用CriteriaBuilder的条件方法（equak、notEqual、gt、ge、lt、le、between、like等）创建Predicate实例
4.复合的Predicate语句可以使用CriteriaBuilder的and、or、andnot方法构建
简单示例
CriteriaBuilder cb = entityManager.getCriteriaBuilder();
Predicate p1 = cb.like(root.get("name").as(String.class), "%" + userQueryModel.getName + "%);
Predicate p2 = cb.equal(root.get("uuid").as(Integer.class), userQueryModel.getUuid());
Predicate p3 = cb.gt(root.get("age").as(Integer.class), userQueryModel.getAge());
Predicate p = cb.and(p3, cb.or(p1, p2));
可以直接返回cb.and()取得Predicate
Specification<UserModel> spec = new Specification() {
	public Predicate toPredicate(Root<UserModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Predicate[] p = new Predicate[list.size()];
		return cb.and(list.toArray(p));
	}
}
可以使用CriteriaQuery得到最后的Prediacte
Specification<UserModel> spec = new Specification<>() {
	public Predicate toPredicate(Root<UserModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		query.where(cb.and(p3, cb.or(p1, p2));
		query.orderBy(cb.desc(root.get("uuid").as(Integer.class)));
		return query.getRestriction();
	}
}
多表连接
1:n的情况
配置好实体类@OneToMany(mappedBy="um", fetch=FetchType.LAZY, cascadeType={CascadeType.ALL})和@ManyToOne()@JoinColumn(name="user_id", nullable=false)
Specification<UserModel> spec = new Specification<> () {
	public Predicate toPredicate(Root<UserModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Predicate p1 = cb.like(root.get("name").as(String.class), "%" + userQueryModel.getName() "%");
		Predicate p2 = cb equal(root.get("uuid").as(Integer.class), userQueryModel.getUuid());
		Predicate p3 = cb.gt(root.get("age").as(Integer.class), userQueryModel.getAge())
		SetJoin<UserModel, DepModel> depJoin = root.join(root.getModel().getSet("setDep", DepModel.class), JoinType.Left); // setDep是UserModel中对应一对多的字段
		Predicate p4 = cb.equal(depJoin.get("name").as(String.class), "");
		query.where(cb.and(p3, cb.or(p1, p2), p4);
		query.orderBy(cb.desc(root.get("uuid").as(Integer.class)))
		return query.getRestriction();
	}
}
1:1的情况
配置好实体类@OneToOne(mappedBy="另一个实体中对应的属性名", fetch=FetchType.EAGER, cascade={CascadeType.ALL})  
@OneToOne()@JoinColumn(name="dep_id", nullable=false)
Specification<UserModel> spec = new Specification<> () {
	public Predicate toPredicate(Root<UserModel> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		Predicate p1 = cb.like(root.get("name").as(String.class), "%" + userQueryModel.getName() "%");
		Predicate p2 = cb equal(root.get("uuid").as(Integer.class), userQueryModel.getUuid());
		Predicate p3 = cb.gt(root.get("age").as(Integer.class), userQueryModel.getAge())
		Join<UserModel, DepModel> depJoin = root.join(root.getSingularAttribute("dep", DepModel.class), JoinType.Left); // setDep是UserModel中对应一对多的字段
		// 上面这句话可以直接使用 Join<UserModel, DepModel> join = root.join("dep", JoinType.LEFT);替代
		Predicate p4 = cb.equal(depJoin.get("name").as(String.class), "");
		query.where(cb.and(p3, cb.or(p1, p2), p4);
		query.orderBy(cb.desc(root.get("uuid").as(Integer.class)))
		return query.getRestriction();
	}
}



### Security 相关



### 设计模式相关

- 单例模式
定义：确保一个类只有一个实例，而且自行实例化并向整个系统提供这个实例。
类型：创建类模式
类图：
<<单例类>>Singleton
-Singleton singleton
+Singleton getInstance()
-表示private +表示public #表示protected 什么都没有表示包可见
单例模式根据单例化对象时机的不同分为两种：饿汉式单例、懒汉式单例
饿汉式在类被加载的时候就实例化，懒汉式在调用取得实例化方法的时候才会实例化对象。
饿汉式：
public class Singleton {
    private static Singleton singleton = new Singleton();
    private Singleton() {}
    public static Singleton getInstance() {
        return singleton;
    }
}
懒汉式:
public class Singleton {
    private static Singleton singleton;
    private Singleton() {}
    public static Singleton getInstance() {
        if(singleton == null) {
            synchronized(Singleton.class) {
                if(singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
    或者
    public static synchronized Singleton getInstance() {
        if(singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }
}
优点：
1.在内存中只有一个对象，结省内存空间。
2.避免频繁的创建销毁对象，提高性能
3.避免对共享资源的多重占用
4.可以全局访问
适用场景：
1.需要频繁实例化然后销毁的对象
2.创建对象时消耗时间或资源过多，但需要经常适用的对象
3.有状态的工具类对象
4.频繁访问数据库或文件的对象
注意事项
1.只能适用提供的方法而不能使用反射
2.不要做解除静态引用的操作
3.多线程之中注意线程安全
- 工厂方法模式
定义：定义个用于创建对象的接口，让子类决定实例化哪一个类，工厂方法使一个类的实例化延迟到其子类。
模拟代码：
public interface IProduct {
    void productMethod();
}
public class Product implements IProduct {
    public void productMethod() {
        System.out.println("产品");
    }
}
public interface IFactory {
    IProduct createProduct();
}
public class Factory implements IFactory {
    public IProduct createProduct() {
        return new Product();
    }
}
根据抽象程度不同分为三种：简单工厂模式（静态工厂模式）、工厂方法模式、抽象工厂模式。
优点：
1.可以使代码结构清晰，有效地封装变化。将产品的实例化封装起来，使得调用者无需关心产品的实例化过程，依赖工厂即可得到想要的产品。
2.对调用者屏蔽具体的产品类
3.降低耦合度
工厂方法有4个要素
1.工厂接口
2.工厂实现
3.产品接口
4.产品实现
- 抽象工厂模式
定义：为创建一组相关或相互依赖的对象提供一个接口，而且无需指定具体类
抽象工厂和工厂方法模式的区别
抽象工厂是工厂方法的升级版，用于创建一组相关活相互依赖的对象。区别在于工厂方法针对一个产品等级结构，抽象工厂则针对多个产品等级结构。
在抽象工厂模式中，有一个产品族的概念（指位于不同产品等级结构中功能相关联的产品组成的家族）
优点：
可以对类内部的产品族进行约束
缺点：
产品族的管理十分费力，假如需要增加一个新的产品，所有工厂类都需要修改
- 建造者模式
定义：
将一个复杂对象的构建与它的表示分离，使同样的构建过程可以创建不同的表示
public interface IBuilder {
    void setPart();
    IProduct getProduct();
}
public class Builder implements IBuilder {
    public void setPart() {
        // 设置产品的一些属性
    }
    public Product getProduct() {
        return product; // 设置完不同属性的产品
    }
}
- 原型模式
定义：将一个对象作为原型，对其进行复制、克隆，产生一个和原对象类似的新对象。
使用Object.clone()方法完成clone 这是一个native方法
浅复制：对象复制后，引用类型的属性指向原来的
深复制：完全彻底的复制，所有属性都重新创建
-适配器模式
定义：将某个实现接口的类转换成所期待的另一个类的表示，消除接口不匹配造成的兼容性问题
主要分为三类：类的适配器模式、对象的适配器模式、接口的适配器模式
1.类的适配器模式
核心思想是：Source类，拥有一个方法，待适配，目标接口是Targetable，通过Adapter类，将Source的功能扩展到Targetable中
public class Source {
    public void method1() {
        System.out.println("待适配类中的原始方法";);
    }
}
public interface Targetable {
    void method1();
    void method2();
}
public class Adapter extends Source implements Targetable {
    public void method2() {
        System.out.println("适配后对象的方法");
    }
}
2.对象的适配器模式
不继承Source类，而是持有Source类的实例，从而解决兼容性问题。
3.接口的适配模式
当一个接口中有多个抽象方法，当写接口的实现类时，必须实现所有方法，但有时不是所有方法都是需要的，因此可以借助一个抽象类，实现了接口的所有方法，从而无需和原始接口打交道，只重写本身需要的方法即可。
-装饰模式
定义：给对象增加一些新的功能，并且是动态的，要求装饰对象和被装饰对象实现同一个接口，装饰对象持有被装饰对象的实例
-代理模式
定义：产生一个代理类，替原对象进行一些操作。
-外观模式
定义：解决类与类之间的依赖关系
将多个类定义为一个类的属性，使用这个类即可。
-桥接模式
定义：把事物和气具体实现分开，可以各自独立。比如JDBC，同意使用JDBC提供接口，而每个数据库提供各自的实现。
-组合
又称部分-整体模式 在处理类似属性结构的问题时比较方便
-享元模式
实现对象的共享，即共享池，当系统中对象多的时候可以减少内存的开销，通常与工厂模式一起使用
类似于数据库连接池
--- 关系模式
-- 父子关系
- 策略模式

-- 类之间的关系
- 观察者模式
定义：当一个对象变化的时候，其他依赖对象会受到通知，并随着变化

- 命令模式
解决命令请求者和命令实现者之间的耦合关系
包含4个角色
1.ICommand 定义命令的同一接口
2.ConcreteCommand 命令接口的实现者 用来执行具体的命令
3.Receiver 命令的实际执行者
4.Invoker 命令的请求者



### Mybatis/ibatis
在pom.xml文件中引入
<dependency>
  <groupId>org.mybatis.spring.boot</groupId>
  <artifactId>mybatis-spring-boot-starter</artifactId>
  <version>1.3.2</version>
</dependency>
解决依赖问题
再在application.yml配置文件中配置对应的配置信息
mybatis:
    mapper-location: classpath:mapper/**/*.xml # 表示在mapper下所有的xml文件
如果mapper.xml文件不定义在resources文件夹下 而是在java下需要配置maven相关的配置信息
<build>
    <resources>
        <resource>
            <directory>
                src/main/java/** <!-- mapper.xml文件所处目录 以src作为根目录 -->
            </directory>
            <includes>
                <include>
                    **/*.xml <!-- 构建时需要拷贝的资源文件 -->
                </include>
            </includes>
            <targetPath>
                mapper <!-- 构建时复制到的目标路径 在classes下 -->
            </targetPath>
            <filtering>true</filtering> <!--  -->
        </resource>
    </resources>
</build>
然后需要编写实体类、dao层接口和mapper.xml文件
实体类就是普通POJO类
dao层接口需要以@Mapper注解标识
mapper.xml
<mapper namespace="对应的全限定接口名">
    <resultMap  id="" type="对应的全限定实体类名">
        <id property="" column="" />
        <result property="" column="" />
        <association></association>
        <colletion></collection>
    </resultMap>
</mapper>
最后使用@Autowaired或@Resource注入即可使用

### Hibernate
data-jpa使用可以兼容hibernate
@Autowaired
private EntityManagerFactory entityManagerFactory;
测试时使用了
entityManagerFactory.unwarp(SessioFactory.class)的方式获取了SessionFactory
然后使用了openSession().createQuery("FROM 实体类").list()的方式查询成功
不能将SessionFactory配置成bean 否则会报错

### Data JPA
使用本地查询使用
@Autowaired
private EntityManager entityManager;

### JVM
--- 自动内存管理机制
C、C++，在内存管理领域，拥有每一个对象的所有权，又担负每个对象生命开始到终结的维护责任
Java而言，在虚拟机自动内存管理机制帮助下，无需为每个new操作去写配对的delete/free代码，不容易出现内存泄漏和内存溢出的问题。
-- Java内存区域与内存溢出异常
- 运行时数据区域
在执行Java程序的过程中会把管理的内存划分为若干不同的数据区域。区域有各自的用途，以及创建和销毁时间
分为
运行时数据所使用内存区域
|--线程共享数据区
    |--方法区 Method Area
    |--堆 Heap
|--线程隔离数据区
    |--虚拟机栈 VM Stack
    |--本地方法栈 Native Method Stack
    |-- 程序计数器 Program Counter Register
其他
|--执行引擎
|--本地库接口
|--本地方法库
1.程序计数器
一块较小的内存空间，可以看作当前线程执行的字结码的行号指示器，在虚拟机的概念模型里，字结码解释器就是通过改变这个计数器的值来选取下一条需要执行的字结码指令，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成。
Java虚拟机的多线程是通过线程轮流切换并分配处理器执行时间的方式来实现的，同一时间，一个处理器内核只会执行一条线程中的指令。因此，为了线程切换后能恢复到正确的执行位置，每条线程都需要一个独立的程序计数器。
如果线程执行的是一个Java方法，这个计数器记录的是正在执行的虚拟机字结码指令的地址。如果是Native方法，这个计数器为空。此内存区域是唯一一个没有规定任何OutOfMemoryError情况的区域。
2.Java虚拟机栈
也是线程私有的，生命周期与线程相同。描述的是Java方法执行的内存模型，每个方法在执行的同时都会创建一个栈帧(Stack Frame)用于存储局部变量表、操作数栈、动态链接、方法出口等信息。每一个方法从调用到执行完成的过程，就对应栈帧在虚拟机栈中入栈到出栈的过程。
局部变量表存放了编译器可知的各种基本数据类型(boolean、byte、char、short、int、float、long、double)、对象引用和returnAddress（指向了一条字结码指令的地址）
其中64位长度的long和double类型的数据会占用2个局部变量空间(slot),其余一个。局部变量表所需的内存空间在编译期间完成分配，进入一个方法时，这个栈帧分配的局部变量空间是完全确定的，方法运行期间不会改变。
此区域规定了两种异常状况，如果线程请求的栈深度大于虚拟机所允许的深度，将抛出StackOverflowError,如果虚拟机栈可以动态扩展，但无法申请到足够的内存，就会抛出OutOfMemoryError
3.本地方法栈
与虚拟机栈类似，但为了使用的Native方法服务。
4.Java堆
大多数应用，Java堆是Java虚拟机管理的最大的内存。Java堆是被所有线程共享的一块内存区域，在虚拟机启动时创建。此内存区域的唯一目的就是存放对象实例，几乎所有的对象实例都在此分配内存。
Java堆是垃圾收集器管理的主要区域，也被成为GC堆。
从垃圾回收角度，由于垃圾收集器采用分代收集算法，所以Java堆还可以分为新生代、老年代，再细致有Eden、From Survivor、To Survivor等。
从内存分配角度，线程共享的堆还可能划分出多个线程私有的分配缓冲区（Thread Local Allocation Buffer）。
堆中存放的都是对象实例，进一步划分目的是更好的回收内存，或者更快分配内存
Java堆可以物理上不连续，只要逻辑上连续即可
5.方法区
线程共享的内存区域，用于存储已被虚拟机加载的类信息、常量、静态变量、即时编辑器编译后的代码等数据等数据。
6.运行时常量池
是方法区的一部分。Class文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是常量池，用于存放编译期生成的各种字面量和符号引用，这部分内存将在类加载后进入方法区的运行时常量池。
7.直接内存
并不是虚拟机运行时数据区的一部分，也不是Java虚拟机规范中定义的内存区域，但也频繁使用，可能导致OutOfMemoryError出现。
JDK1.4加入NIO类，引入基于通道（Channel）和缓冲区（Buffer）的I/O方式，可以使用Native函数库直接分配堆外内存，然后通过一个堆中的DirectByteBuffer对象作为这块内存的引用进行操作。
直接内存不受堆大小限制，但是受到本地总内存大小限制。经常会忽略这个问题。
虚拟机在遇到一条new指令时，首先将检查指令的参数是否能在常量池中定位到一个类的符号引用，并检查这个符号引用代表的类是否已被加载、解析和初始化，如果没有则执行相应的类加载过程。
在类加载检查通过后，将分配内存，相当于将一块确定大小的内存从堆中划出来。绝对规整的移动指针，叫做指针碰撞，不规整的则维护一个列表，划分一块可用的区域，叫做空闲列表。
分配内存存在同步的问题，两种解决方案：一种是对分配内存空间的动作进行同步处理，采用失败重试，另一种是按照线程不同划分在不同的空间之中进行，即每个线程在堆中先分配一小块内存，称为本地线程分配缓冲区（Thread Local Allocation Buffer TLAB）。哪个线程要分配内存，就在TLAB上分配，只有TLAB用完才需要同步锁定。
使用 -XX:+/-UseTLAB参数来设定是否使用
内存分配完成后，虚拟机需要将分配到的内存空间都初始化为零值。
接着对对象进行必要的设置，例如这个对象时哪个类的实例、如何才能找到类的元数据信息、对象的哈希码、对象的GC分代年龄等，都存在在对象头中
执行完new指令之后，会执行init方法，对对象进行进一步的初始化。
- 对象的内存布局
分为3块区域：对象头、实例数据和对齐填充
对象头分为2部分，第一部分用于存储自身的运行时数据，如哈希码、GC分代年龄、锁状态标志、线程持有锁、偏向线程ID、偏向时间戳等。在32/64位虚拟机中分别为32bit和64bit，称为Mark Word。设计为一个非固定的数据结构以便存储更多信息，会根据对象状态复用存储空间。另一部分是类型指针，即对象只想类元数据的指针，通过这个指针确定是哪个类的实例。如果是数组，还有一个记录数组长度的数据。
实例数据部分是对象真正存储的有效信息，定义的各种类型的字段内容。无论是父类继承还是子类中定义的。存储顺序会受分配策略和定义顺序影响。
对其填充不是必要的，仅仅作为占位符作用。
- 对象的访问定位
通过栈上的reference数据来操作堆上的具体对象，主流访问方式有使用句柄和直接指针两种。
句柄：在堆中划分出一个句柄池，reference存储的就是对象的句柄地址，句柄中包含对象的实例数据与类型数据各自的具体地址信息
直接指针：则堆对象的布局就必须考虑如果放置访问类型数据的相关信息。
堆的OOM异常是常见的内存溢出异常
异常堆栈信息java.lang.OutOfMemoryError会进一步提示Java heap space
堆的设置
-Xms50M (Java堆最小值即初始化值)
-Xmx100M (Java堆最大值)
-Xmn (年轻代大小 年轻代+年老代+持久代(通常为64m)=总堆大小 推荐为总堆的3/8)
虚拟机栈和本地方法栈溢出
-Xoss (本地方法栈大小)
-Xss (虚拟机栈大小)
1.如果线程请求的栈深度大于虚拟机所允许最大深度，抛出StackOverflowError异常
2.如果虚拟机扩展栈无法申请到足够的内存空间，抛出OutOfMemoryError异常
单线程下，无论是栈帧太大还是虚拟机栈容量太小，当内存无法分配时，抛出的都是StackOverflowError异常。
方法区和运行时常量池溢出
String.intern()是一个Native方法，作用是常量池中已经包含一个等于此String的字符串则返回引用，没有则添加到字符串常量池中并返回引用
永久代是HotSpot对方法区的一种实现
-XX:PermSize (方法区初始大小)
-XX:MaxPermSize (方法区最大大小)
本机直接内存溢出
-XX:MaxDirectMemorySize (本机直接内存大小 如果不指定则与-Xmx堆最大大小相同)
-- 垃圾收集器与内存分配策略
判断对象存活状态
1.引用计数算法
给对象添加一个引用计数器，有引用则加一，引用失效则减一。任何时刻计数器为0的对象不可能再被使用。
主要问题在于很难解决对象之间相互循环引用的问题。
2.可达性分析算法
思路是通过一系列称为GC Roots的对象作为起始点，从这些结点开始向下搜索，搜索走过的路径称为引用链，当一个对象到GC Roots没有任何引用链项链，则证明对象不可用。
Java中，可作为GC Roots对象包括以下几种
A.虚拟机栈（栈帧中的本地变量库)中引用的对象
B.方法区中类静态属性引用的对象
C.方法区中常量引用的对象
D.本地方法栈中JNI引用的对象
再谈引用
A.强引用就是在程序代码中普遍存在的，类似Object obj = new Object()这类引用，只要强引用还存在，垃圾回收器就不会回收引用的对象
B.软引用是用来描述一些还有用但非必需的对象。对于软引用对象，在系统将要发生内存溢出异常之前，将会把这些对象列进回收范围之中进行第二次回收，如果内存还不够则抛出内存溢出异常。JDK1.2之后提供SoftReference类来实现软引用
C.弱引用也是描述非必需对象，但比软引用跟弱一点，只能生存到下次垃圾回收之前。WeakReference类来实现弱引用。
D.虚引用，最弱的引用关系，是否存在虚引用不会对生存时间构成影响，也无法通过虚引用取得对象实例。唯一目的是在对象被垃圾回收的时候收到系统通知。PhantomReference类
即使是不可达对象，也需要经历两次标记过程。第一次标记后进行筛选是否有必要执行finalize()方法。当没有覆盖finalize方法或已被虚拟机调用过则视为没有必要执行。如果判定有必要执行则放置在一个叫做F-Queue的队列之中，并在稍后由一个虚拟机自动建立的、低优先级的Finalizer线程执行，但并不承诺执行完成。finalize()方法时最后一次机会，会进行第二次标记若没有与GC Roots取得关联则会被回收
回收方法区
永久代的垃圾收集主要回收两部分内容：废弃常量和无用的类
无用类条件
1.该类所有的实例都已经被回收
2.加载该类的ClassLoader已经被回收
3.该类对应的java.lang.Class对象没有在任何地方被引用，无法再任何地方通过反射访问该类的方法
-Xnoclassgc 控制是否对类进行回收
-verbose:class 配合以下option使用
-XX:+TraceClassLoading 查看类加载信息
-XX:+TraceClassUnloading 查看类卸载信息
- 垃圾收集算法
1.标记-清除算法
主要不足有两个：效率问题和空间问题（回收之后造成大量不连续的内存碎片）
2.复制算法
将可用内存按容量分为大小相等的两块，每次只使用其中的一块。一块用完之后将存活的对象复制到另一块上，然后再把已使用过的内存空间一次清理。
主流采用此算法，但是不是按1:1比例分配内存空间，而是分为一块较大的Eden空间和两块Survivor空间，每次使用Eden和一块Survivor空间。回收时，将Eden和Survivor中还存活的对象一次性复制到另一块Survivor上，最后清理掉Eden和使用过的Survivor空间。默认Eden和Survivor的比例是8:1。当Survivor空间不足时，需要依赖其他内存（老年代）进行分配担保。
3.标记-整理算法
让所有存活的对象都向一端移动，然后直接清理掉端边界以外的内存
分代收集算法
根据对象存活周期的不同将内存划分为几块，Java堆分为新生代和老年代
新生代存活率较低使用复制算法
老年代存活率高使用标记-清除或标记-整理算法进行回收
- HotSpot实现
1.枚举根结点
此工作必须在一个能确保一致性的快照中进行——这个分析期间整个执行系统看起来应该在某个时间点上，这导致GC进行时必须停顿所有执行线程（称为Stop The World）
HotSpot实现中，使用一组称为OopMap的数据结构来存放对象引用
2.安全点
并没有为每条指令生成OopMap，只有在特定时间才停顿下来开始GC，这些位置称为安全点。
3.安全区域

- 垃圾收集器
1.Serial收集器
最基本、发展历史最悠久的收集器。是一个单线程的收集器，重要的是在收集时，必须暂停其他所有的工作线程，直到收集结束。
虽然存在缺点，但依然是Client模式下默认的新生代收集器，最主要的一点就是简单而高效。
2.ParNew收集器
就是Serial收集器的多线程版本。
是许多运行在Server模式下的虚拟机首选的新生代收集器。
-XX:useParNewGC
--XX:ParallelGCThreads (默认启动与CPU数量相同的线程 此option可手动指定)
3.Parallel Scavenge收集器
关注点与其他收集器不同，其他主要目标是减少垃圾收集时用户线程的停顿时间，而Parallel Scavenger收集器目的是达到一个可控的吞吐量（用户代码执行时间/(用户代码执行时间+垃圾收集时间)）
-XX:MaxGCPauseMillis (最大垃圾收集停顿时间) 减少此值是以减少吞吐量和新生代空间为代价的
-XX:GCTimeRatio (直接设置吞吐量大小)
-XX:+UseAdaptiveSizePolicy (此参数打开之后无需设置-Xmn新生代大小 -XX:SurvivorRatioEden和Survivor比例 -XX:PretenureSizeThresold晋升老年代对象年龄等细结参数 会自动收集信息优化)
4.Serial Old 收集器
是Serial的老年版本，单线程，采用标记-整理算法。
5.Parallel Old收集器
Parallel Scavenge的老年版本，多线程，采用标记-整理算法。
6.CMS收集器
Concurrent Mark Sweep收集器以获取最短回收停顿时间为目标的收集器。
基于标记-清除算法
分为4步
初始标记 需要Stop The World 标记GC Roots能直接关联到的对象 碎度很快
并发标记 进行GC Roots Tracing的过程
重新标记 修正并发期间用户程序继续运作而导致标记变动的那一部分对象
并发清除
缺点
对CPU资源非常敏感 默认启动数量是(CPU数量+3)/4
无法处理浮动垃圾 即在并发标记同时产生的垃圾无法被回收 JDK1.5当老年代使用68%被激活
--XX:CMSInitialOccupancyFraction 修改出发百分比 JDK1.6提高到了92%
采用标记-清除算法，会导致大量碎片，导致full GC触发
-XX:+UseCMSCompactAtFullCollection 控制要进行Full GC时开启内存碎片的合并整理过程
-XX:CMSFullGCsBeforeCompaction 执行多少次不压缩的之后再执行一次压缩的收集
7.G1收集器
特点
并行与并发
分代收集
空间整合 基于标记-整理
可预测的停顿
之前的收集器都是以新生代或老年代为主，而G1将堆划分为多个大小相等的独立区域
- 理解GC日志
1.虚拟机启动到现在的秒数
2.垃圾收集的停顿类型 Full GC表示出现Stop The World
3.表示GC发生的区域 与使用的垃圾收集器相关 不同的收集器名称不同
4.GC前该内存区域已使用容量->GC后该区域已使用容量（该区域总容量）
5.方括号外表示 GC前Java堆已使用容量->GC后Java堆已使用容量（Java堆总容量）
6.GC所占用时间
垃圾收集器参数总结
-XX:UseSerialGC 使用Serial收集器
-XX:UseParNewGC ParNew收集器
-XX:UseConcMarkSweepGC Concurrent Mark Sweep收集器
-XX:UseParallelGC Parallel Scavenge 收集器
-XX:UseParallelOldGC Parallel Scavenge Old 收集器
-XX:SurvivorRatio Eden和Survivor比例
-XX:PretenureSizeThreshold 直接分配到老年代的对象大小
-XX:MaxTenuringThreshold 活过几次GC的对象进入老年代
-XX:UseAdaptiveSizePolicy 自动分配策略
-XX:HandlePromotionFailure 是否允许分配担保失败
-XX:ParallelGCThreads 并行GC的线程数
-XX:GCTimeRatio 吞吐量
-XX:MaxGCPauseMillis 最大收集暂停时间
-XX:CMSInitialOccupancyFraction  老年代使用多少后进行垃圾回收1.5 58% 1.6 92% CMS
-XX:UseCMSCompactAtFullCollection 收集后是否进行整理 CMS
-XX:CMSFullGCsBeforeCompacting 几次不整理之后整理 CMS
- 内存分配与回收策略
1.对象优先在Eden分配
当Eden空间不足时，进行一次minor GC
2.大对象直接进入老年代
需要大量连续内存空间的Java对象，典型的就是很长的字符串以及大数组
--XX:+PrintGCDetails 发生垃圾收集行为时打印回收日志，并在进程退出时输出当前的内存各区域分配情况。
3.长期存活对象将进入老年代
每个对象定义了一个对象年龄计数器，对象在Eden中经过Minor GC后仍然存活，并能被Survivor容纳就移动到Survivor中，并年龄为1，没熬过一次Minor GC年龄+1
-XX:MaxTenuringThreshold设置
4.动态对象年龄判定
如果Survivor空间中相同年龄所有对象大小总和大于Survivor空间一半，大于等于此年龄的对象进入老年代
5.空间分配担保
-- 虚拟机性能监控与故障处理工具
- 概述
数据：运行日志、异常堆栈、GC日志、线程快照、堆转储快照
- JDK命令行工具
java
javac
jps 显示指定系统内所有HotSpot虚拟机进程
jstat 用于收集HotSpot虚拟机各方面的运行数据
jinfo 显示虚拟机配置信息
jmap 生成虚拟机内存转储快照 heapdump文件
jhat 用于分析heapdump文件
jstack 显示虚拟机的线程快照
1.jps 虚拟机进程状况工具
jps [ options ] [ hostid ]
-q 只输出LVMID,省略主类的名称
-m 输出虚拟机进程启动时传递给主类main()函数的参数
-l 输出主类的全名，若执行的是jar包，输出jar路径
-v 输出虚拟机启动时JVM参数
2.jstat 虚拟机统计信息监视工具
jstat [ option vmid [interval[s|ms] [count]] ]
远程vmid格式为[protocol:][//-]lvmid[@hostname[:port]/servername]
option表示用户希望查询的虚拟机信息 分为3类 类装载、垃圾收集、运行期编译状况
-class 监视类装载、卸载数量、总空间以及类装载所消耗时间
-gc 监视Java堆状况，包括各个年代区容量、已用空间、GC时间合计等信息
-gccapacity 类似gc 主要关注各个区域使用的最大、最小空间
-gcutil 类似gc 主要关注使用空间占总空间的百分比
-gccause 与gcutil相同 但会输出上一次gc产生的原因
-gcnew 监视新生代GC状况
-genewcapacity 输出新生代代使用的最大、最小空间
-gcold 监视老年代GC状况
-gcoldcapacity 输出老年代使用的最大、最小空间
-gepermcapacity 输出永久代使用的最大、最小空间
-compiler
-printcomilation
3.jinfo Java配置信息工具 -XX:+PrintFlagsFinal查看参数默认值
jinfo [ option ] pid
4.jmap Java内存映像工具
生成堆转储快照（heapdump或dump文件）-XX:+HeapDumpOnOutOfMemoryError -XX:+HeapDumpOnCtrlBreak
不仅仅为了获取dump文件，还可以查询finalize执行队列，Java堆和永久代详细信息。
jmap [ option ] vmid
-dump 堆转储快照 -dump:[live, ]format=b, file=<filename>
-finalizerinfo F-Queue中等待执行finalize方法的对象
-heap 堆详细信息
-histo 显示堆中对象统计信息，包括类、实例数量、合计容量
-permstat 永久代内存状态
-F 强制生成dump快照
5.jhat 虚拟机堆转储快照分析工具
6.jstack Java堆栈跟踪工具
jstack [ option ] vmid
- JDK可视化工具
1.JConsole Java监视与管理控制台
jconsole命令
2.VisualVM 多合一故障处理工具
--- 类文件结构
-- Class类文件的结构
是一组以8位字结为基础单位的二进制流，各个数据项目严格按照顺序紧凑地排列在Class文件之中，中间没有添加任何分隔符。当遇到需要占用8位字结以上空间的数据项是，会按照高位在前的方式分割成若干个8位字结进行存储。
Class文件格式采用一种类似于C语言结构体的伪结构来存储数据，只有两种数据结构：无符号和表
无符号数是基本的数据类型 u1 u2 u4 u8代表1字结 2字结 4字结 8字结无符号数
表是由多个无符号数或其他表作为数据项构成的符合数据类型 以_info结尾
文件格式
u4 magic 1
u2 minor_version 1
u2 major_version 1
u2 constant_pool_count 1
cp_info constant_pool constant_pool_count-1
u2 access_flags 1
u2 this_class 1
u2 super_class 1
u2 interfaces_count 1
u2 interfaces interfaces_count
u2 fields_count 1
field_info fields fields_count
u2 methods_count 1
method_info methods method_count
u2 attributes_count 1
attribute_info attributes attributes_count
1.魔数
唯一作用就是确定文件是否为一个能被虚拟机接受的Class文件。0xCAFEBABE
2.版本号
3.常量池
从1开始，0作为特殊考虑（只有常量池的容量计数从1开始）
主要存放2大类常量：字面量和符号引用
字面量：如文本字符串、声明为final的常量值等
符号引用：属于编译原理方面的概念 包含类和接口的全限定名、字段的名称和描述符、方法的名称和描述符
常量池中每一项常量都是一张表 表开始的第一位是一个u1类型的标志位
4.访问标志
用于识别一些类或者接口层次的访问信息，包括这个Class是类还是接口，是否是public类型，是否是abstract类型，是否被声明为final
5.类索引、父类索引和接口索引集合
指向常量池中的内容
6.字段表集合
用于描述接口或者类中声明的变量，包括累计变量以及实例级变量，但不包括方法内部声明的局部变量。包括的信息有
字段的作用域 public private protected
实例变量还是类变量 static
可变性 final
并发可见性 volatile 是否强制从主内存中读写
可否被序列化 transient
字段数据类型
字段表描述
u2 access_flag
u2 name_index 常量池中引用
u2 descriptor 常量池中引用 类名使用全限定名（将.换为/） 基本类型则为1个大写字母 数组在之前加[ 方法在之后加()
u2 attributes_count
attribute_info attributes attributes_count
7.方法表集合
与字段描述几乎完全相同
8.属性表集合
？？？？ 暂时没看
-- 字结码指令
由一个字结长度的、代表某种特定操作含义的数字（称为操作码）以及跟随其后的零至多个代表此操作所需参数（称为操作数）二构成
1.字结码与数据类型
2.加载和存储指令
3.运算指令
4.类型转换指令
窄转宽无需显示转换 宽转窄需显示转换 并会出现精度丢失的问题
5.对象创建与访问指令
6.操作数栈管理指令
7.控制转移指令
8.方法调用和返回指令
9.异常处理指令
10.同步指令
--- 类加载机制
-- 类加载的时机
整个生命周期
1.加载 Loading
2.验证 Verification
3.准备 Preparation
4.解析 Resolution
5.初始化 Initialization
6.使用 Using
7.卸载 Unloading
2 3 4步合并为连接(Linking)
解析的顺序不是固定的
规定初始化的条件
1.遇到new、getstatic、putstatic、invokestatic
2.使用java.lang.reflect反射调用
3.初始化一个类，但是父类没有初始化
4.虚拟机启动时，需要指定一个执行的主类，会先初始化这个类
5.动态语言支持
-- 类加载过程（加载、验证、准备、解析、初始化）
- 加载
需要完成3件事情
1.通过一个类的全限定名来获取定义此类的二进制字结流
2.将这个字结流所代表的静态存储结构转化为方法区的运行时数据结构
3.在内存中生成一个代表这个类的java.lang.Class对象，作为方法区这个类的各种数据访问入口
- 验证
确保Class文件的字结流中包含的信息符合虚拟机要求，并不危害安全
1，文件格式验证
魔数是否是CAFEBABE
主次版本是否在当前虚拟机处理范围之内
常量池的常量是否有不被支持的常量类型
指向常量的各种索引值是否有不存在或不符合的
CONSTANT_Utf8_info型的常量是否有不符合UTF8编码的数据
Class文件中的各个部分及本身是否有被删除的或附加的其他信息
2.元数据验证
是否有父类 除了Object应该都有父类
父类是否是不允许继承的类 final修饰
如果不是抽象类 是否实现了所有方法
字段、方法是否与父类产生冲突
3.字结码验证
保证操作数栈与指令代码序列能配合工作
保证跳转指令不会跳转到方法体以外的字结码指令上
保证类型转换有效
4.符号引用验证
符号引用通过字符串描述的全限定名是否能找到对应的类
是否存在符合方法的字段描述符一级简单名称所描述的方法和字段
符号引用的类、字段、方法的访问性是否可被当前类访问
- 准备
正式为类变量分配内存并设置类变量初始值的阶段，此阶段使用的内存都在方法区中分配，只初始化staic修饰的类变量，都置为零值
如果生成的是ConstantValue则会初始化 static final修饰
- 解析
将常量池内的符号引用替换为直接引用的过程
主要针对类或接口、字段、类方法、接口方法、方法类型、方法句柄和调用点限定符7类符号引用对应CONSTANT_Class_info、CONSTANT_Fieldref_info、CONSTANT_Methodref_info、CONSTANT_InterfaceMethodref_info、CONSTANT_MethodType_info、CONSTANT_MethodHandle_info、CONSTANT_InvokeDynamic_info
- 初始化
真正开始执行Java代码，执行<cinit>()方法的过程
1.自动收集类中所有类变量的赋值动作和静态语句块中的语句合并产生的，收集顺序由定义顺序决定
2.与类的构造函数不同，不需要显示调用父类构造器，虚拟机保证在子类<cinit>之前，父类的<cinit>已经执行完成
3.由于父类的<cinit>先执行，因此父类的static语句块先执行
4.<cinit>不是必须的
5.接口中不能有静态语句块，但是可以有变量初始化，因此也有<cinit>方法。接口的<cinit>不需要先执行父接口的<cinit>方法，只有当父接口中定义的变量使用时，父接口才初始化
6.虚拟机保证类的<cinit>方法在多线程环境中正确的加锁、同步。
-- 类加载器
每个类加载器都拥有一个独立的类名称空间，比较两个类是否相等，只有在两个类在同一类加载器加载的前提才有意义。
- 双亲委派模型
从Java虚拟机的角度而言，只存在两种不同的类加载器：启动类加载器（Bootstrap ClassLoader），使用C++实现，是虚拟机自身的一部分，另一种就是所有其他的类加载器，由Java语言实现。都继承自java.lang.ClassLoader
从开发者角度
1.启动类加载器
无法被Java程序字结引用，自定义类加载需要把加载请求委派给引导类加载器，直接使用null代替即可。
2.扩展类加载器
由sun.misc.Launcher$ExtClassLoader实现，负责<JAVA_HOME>\lib\ext目录，或者被系统变量指定的路径中的所有类库，可以使用
3.应用程序加载器
有sun.misc.Launcher$AppClassLoader实现，ClassLoader的getSystemLoader()方法返回，一般也称为系统类加载器，负责用户类路径(ClassPath)上所指定的类库们可以直接使用此加载器，一般情况下是默认
双亲委派模型要求除了顶层的启动类加载器之外，其余都应当有自己的父类加载器，不以继承实现，而是组合关系来组合父加载器的代码
工作过程：加载器收到加载请求，首先不自己加载，而是把请求委派给父类加载器，直到顶层。只有父加载器不能完成，才会尝试自己加载。
--- 虚拟机字结码执行引擎
-- 运行时栈帧结构
栈帧是用于支持虚拟机进行方法调用和方法执行的数据结构，是虚拟机栈的栈元素。栈帧存储了方法的局部变量表、操作数栈、动态链接和方法返回地址等信息。方法从调用到执行完成都对应栈帧的出栈和入栈
在编译代码时，栈帧需要多大的局部变量表、多深的操作数栈都已经完全确定。
只有栈顶的栈帧是有效的，称为当前栈帧，对应的方法叫做当前方法。
1.局部变量表
是一组变量值存储空间，用于存放方法参数和方法内部定义的局部变量
以变量槽（Variable Slot）为最小单位。
虚拟机通过索引定位的方式使用局部变量表
方法执行时，虚拟机使用布局变量表完成参数值到参数变量列表的传递过程，如果是实例方法，布局变量表中的第0位索引的Slot默认是用于传递方法所属对象实例的引用，也就是this关键字
布局变量表的Slot是可以重用的
把不在使用的变量手动设置为null是推荐的编码规则
2.操作数栈
执行时，会有各种字结码指令往操作数栈中写入和提取内容，也就是出栈/入栈操作
3.动态连接
每个栈帧指向运行时常量池中该帧所属方法的引用。
4.方法返回地址
方法开始执行之后，只有两种方法退出，一是遇到返回的字结码指令，一种是遇到异常
无论怎么退出都需要返回方法被调用的位置，一般来说，正常退出时调用者的PC计数器可以作为返回地址，而异常退出时，返回地址是要通过异常处理器表来确定的。
5.附加信息
-- 方法调用
1.解析
类加载的解析阶段，会把方法在Class文件中的符号引用转化为直接引用。
2.分派
？？？？？这章暂时没有看完

---- 早期（编译器）优化
-- Javac编译器

???? 深入理解Java虚拟机还没看完

### Dubbo
--- 架构
Provider 暴露服务的服务提供方
Consumer 调用远程服务的服务消费方
Registry 服务注册与发现的注册中心
Monitor 统计服务的调用次数和调用时间的监控中心
Container 服务运行容器
0 Container -> Provider 服务容器负责启动，加载，运行服务提供者
1 Provider -> Registry 服务提供者在启动时，向注册中心注册自己提供的服务
2 Consumer -> Registry 服务消费者在启动时，向注册中心订阅自己所需的服务
3 Registry -> Consumer 注册中心返回服务提供者列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者
4 Consumer -> Provider 服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用
5 Consumer -> Monitor Provider -> Monitor 服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心
-连通性
注册中心负责服务地址的注册与查找，相当于目录服务，服务提供者和消费者只在启动时与注册中心交互，注册中心不转发请求，压力较小
监控中心负责统计各服务调用次数，调用时间等，统计先在内存汇总后每分钟一次发送到监控中心服务器，并以报表展示
服务提供者向注册中心注册其提供的服务，并汇报调用时间到监控中心，此时间不包含网络开销
服务消费者向注册中心获取服务提供者地址列表，并根据负载算法直接调用提供者，同时汇报调用时间到监控中心，此时间包含网络开销
注册中心，服务提供者，服务消费者三者之间均为长连接，监控中心除外
注册中心通过长连接感知服务提供者的存在，服务提供者宕机，注册中心将立即推送事件通知消费者
注册中心和监控中心全部宕机，不影响已运行的提供者和消费者，消费者在本地缓存了提供者列表
注册中心和监控中心都是可选的，服务消费者可以直连服务提供者
-健壮性
监控中心宕掉不影响使用，只是丢失部分采样数据
数据库宕掉后，注册中心仍能通过缓存提供服务列表查询，但不能注册新服务
注册中心对等集群，任意一台宕掉后，将自动切换到另一台
注册中心全部宕掉后，服务提供者和服务消费者仍能通过本地缓存通讯
服务提供者无状态，任意一台宕掉后，不影响使用
服务提供者全部宕掉后，服务消费者应用将无法使用，并无限次重连等待服务提供者恢复
-伸缩性
注册中心为对等集群，可动态增加机器部署实例，所有客户端将自动发现新的注册中心
服务提供者无状态，可动态增加机器部署实例，注册中心将推送新的服务提供者信息给消费者
-升级性
当服务集群规模进一步扩大，带动IT治理结构进一步升级，需要实现动态部署，进行流动计算，现有分布式服务架构不会带来阻力。下图是未来可能的一种架构：
--- 用法
本地服务Spring配置
local.xml
<bean id="xxxService" class="com.xxx.XxxServiceImpl" />
<bean id="xxxAction" class="com.xxx.XxxAction">
    <property name="xxxService" ref="xxxService" />
</bean>
远程服务Spring配置
在本地服务的基础上，只需做简单配置，即可完成远程化
1.将local.xml配置拆分为两份，将服务定义部分放在服务提供方remote-provider.xml，将服务引用部分放在服务消费方remote-consumer.xml
2.并在提供方增加暴露服务配置<dubbo:service>,在消费方增加引用服务配置<dubbo:reference>
remote-service.xml
<bean id="xxxService" class="xxxServiceImpl" />
<dubbo:serivce interface="com.xxx.XxxService" ref="xxxService" />
remote-consumer.xml
<dubbo:reference id="xxxService" interface="com.xxx.XxxService" />
<bean id="xxxAction" class="com.xxx.XxxActioon">
    <property name="xxxService" ref="xxxService" />
</bean>
--- 快速开始
Dubbo采用全Spring配置方式，透明化接入应用，对应用没有任何API侵入，只需用Spring加载Dubbo的配置即可
provider.xml
<beans>
    <!-- 提供方引用信息，用于计算依赖关系 -->
    <dubbo:application name="" />
    <!-- 使用multicast广播注册中心暴露服务地址 -->
    <dubbo:registry address="" />
    <!-- 使用dubbo协议在端口暴露位置 -->
    <dubbo:protocol name="" port="" />
    <!-- 声明需要暴露的服务接口 -->
    <dubbo:service interface="" ref="" />
    <!-- 和本地bean一样实现服务 -->
    <bean id="" class="" />
</beans>
consumer.xml
<beans>
    <!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要和提供方一样 -->
    <dubbo:application name="" />
    <!-- 使用multicast广播注册中心发现暴露的服务提供地址 -->
    <dubbo:registry address="" />
    <!-- 生成远程代理服务，可以和本地bean一样使用服务bean -->
    <dubbo:reference id="" interface="" />
</beans>
--- 配置信息
-XML配置
标签	用途	解释
<dubbo:service/>	服务配置	用于暴露一个服务，定义服务的元信息，一个服务可以用多个协议暴露，一个服务也可以注册到多个注册中心
<dubbo:reference/> 	引用配置	用于创建一个远程服务代理，一个引用可以指向多个注册中心
<dubbo:protocol/>	协议配置	用于配置提供服务的协议信息，协议由提供方指定，消费方被动接受
<dubbo:application/>	应用配置	用于配置当前应用信息，不管该应用是提供者还是消费者
<dubbo:module/>	模块配置	用于配置当前模块信息，可选
<dubbo:registry/>	注册中心配置	用于配置连接注册中心相关信息
<dubbo:monitor/>	监控中心配置	用于配置连接监控中心相关信息，可选
<dubbo:provider/>	提供方配置	当 ProtocolConfig 和 ServiceConfig 某属性没有配置时，采用此缺省值，可选
<dubbo:consumer/>	消费方配置	当 ReferenceConfig 某属性没有配置时，采用此缺省值，可选
<dubbo:method/>	方法配置	用于 ServiceConfig 和 ReferenceConfig 指定方法级的配置信息
<dubbo:argument/>	参数配置	用于指定方法参数配置
-属性配置
自动加载classpath根目录下的dubbo.properties
将 XML 配置的标签名，加属性名，用点分隔，多个属性拆成多行
如果 XML 有多行同名标签配置，可用 id 号区分，如果没有 id 号将对所有同名标签生效
覆盖策略
JVM -D参数优先
XML 次之
Properties 最弱
-API配置
1.服务提供方
// 服务实现
XxxService xxxService = new XxxServiceImpl();

// 当前应用配置
ApplicationConfig application = new ApplicationConfig();
application.setName("xxx");

// 连接注册中心配置
RegistryConfig registry = new RegistryConfig();
registry.setAddress("10.20.130.230:9090");
registry.setUsername("aaa");
registry.setPassword("bbb");

// 服务提供者协议配置
ProtocolConfig protocol = new ProtocolConfig();
protocol.setName("dubbo");
protocol.setPort(12345);
protocol.setThreads(200);

// 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口

// 服务提供者暴露服务配置
ServiceConfig<XxxService> service = new ServiceConfig<XxxService>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
service.setApplication(application);
service.setRegistry(registry); // 多个注册中心可以用setRegistries()
service.setProtocol(protocol); // 多个协议可以用setProtocols()
service.setInterface(XxxService.class);
service.setRef(xxxService);
service.setVersion("1.0.0");

// 暴露及注册服务
service.export();
2.服务消费方
// 当前应用配置
ApplicationConfig application = new ApplicationConfig();
application.setName("yyy");

// 连接注册中心配置
RegistryConfig registry = new RegistryConfig();
registry.setAddress("10.20.130.230:9090");
registry.setUsername("aaa");
registry.setPassword("bbb");

// 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接

// 引用远程服务
ReferenceConfig<XxxService> reference = new ReferenceConfig<XxxService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
reference.setApplication(application);
reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
reference.setInterface(XxxService.class);
reference.setVersion("1.0.0");

// 和本地bean一样使用xxxService
XxxService xxxService = reference.get(); // 注意：此代理对象内部封装了所有通讯细结，对象较重，请缓存复用
注意：PS仅适用于OpenAPI，ESB，Test，Mock等系统集成,普通的请使用xml配置方式
---注解配置
1.服务提供方
@org.apache.dubbo.config.annotation.Service注解暴露服务
javaconfig形式配置公共模块
@Configuration
public class DubboConfiguration {
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig regisryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("");
        registryConfig.setClient("");
        return registryConfig;
    }
}
启动dubbo扫描路径
@DubboComponentScan(basePackages = "")
2.服务消费方
使用@org.apache.dubbo.config.annotation.Reference自动注入依赖的方法
javaconfig形式配置公共模块
@Configuration
public class DubboConfiguration {
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("consumer-test");
        return applicationConfig;
    }

    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setTimeout(3000);
        return consumerConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        registryConfig.setClient("curator");
        return registryConfig;
    }
}
启动Dubbo扫描
@DubboComponentScan(basePackages = "")

-----
本地测试Dubbo
-----
问题1.生产者和消费者的接口全限定名应该相同（解决方法暂时没解决）
问题2.网上说生产者需要先@SpringBootApplication 再@DubboComponentScan 而消费者需要先@DubboComponentScan 再@SpringBootApplication(暂时没有验证)
问题3.dubbo不是把代码复制一遍，而是在该服务上提供服务，所有执行任务都是在提供服务的机器上进行（一开始想错了，记录一下）


## Spring Boot配合Dubbo
1.pom.xml
*
老版本使用
<dependency>
	<groupId>com.alibaba.dubbo</groupId>
	<artifactId>dubbo-spring-boot-starter</artifactId>
	<version>0.2.0</version>
</dependency>
*
Dubbo回归apache维护后可以使用org.apache.dubbo的依赖
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groudId>
			<artifactId>spring-boot-dependencies</artifactId>
			<version>${spring-boot-version}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
		<!-- 引入dubbo相关依赖的pom文件 之后只需要指定需要引入的依赖而不需要指定版本 以免版本不匹配而造成不可预见的错误 -->
		<dependency>
			<groupId>org.apache.dubbo</groupId>
			<artifactId>dubbo-dependencies-bom</artifactId>
			<version>${dubbo-version}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
		<dependency>
			<groupId>org.apache.dubbo</groupId>
			<artifactId>dubbo</artifactId>
			<version>${dubbo-version}</version>
			<exclusions>
				<exclusion>
					<groupId>org.springframework</groupId>
					<artifactId>spring</artifactId>
				</exclusion>
				<exclusion>
					<groupId>javax.servlet</groupId>
					<artifactId>servlet-api</artifactId>
				</exclusion>
				<exclusion>
					<groupId>log4j</groupId>
					<artifactId>log4j</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
	</dependencies>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>sprint-boot-starter</artifactId>
		</dependency>
		<dependency>
			<groupId>org.apache.dubbo</groupId>
			<artifactId>dubbo-sprint-boot-starter</artifactId>
			<version>${dubbo-version}</version>
		</dependency>
		<dependency>
			<groupId>org.apache.dubbo</groupId>
			<artifactId>dubbo</artifactId>
		</dependency>
		<!-- 可能还需要netty的依赖 如果启动时遇到ClassNotFound的异常 百度一下 引入相关依赖即可 -->
		<dependency>
			<groupId>org.apache.curator</groupId>
			<artifactId>curator-framework</artifactId>
		</dependency>
		<dependency>
			<groupId>org.apache.curator</groupId>
			<artifactId>curator-recipes</artifactId>
		</dependency>
		<dependency>
			<groupId>org.apache.zookeeper</groupId>
			<artifactId>zookeeper</artifactId>
		</dependency>
	</dependencies>
</dependencyManagement>
2.服务器相关基础配置 (可启动最基础的配置 额外配置需要遇到的时候再添加到此)
application.yml
spring:
	application:
		name: XXX
dubbo:
	registry:
		address: zookeeper://host:port
		id: xxx
	protocol:
		name: dubbo
		port: xx
	application:
		name: xxx
	scan:
		base-packages: xx.xx,yy.yy # 这个配置项可以在启动类上以@DubboComponentScan()替代
创建Service接口和实现的ServiceImpl类
public interface HelloService {
	String hello(String name);
}
@org.apache.dubbo.config.annotation.Service()
public class HelloServiceImpl {
	public String hello(String name) {
		return name;
	}
}
以上最基本的服务提供者即可完成
3.消费者基本配置
pom.xml中导入相同依赖
application.yml
spring:
	application:
		name: xxx
dubbo:
	registry:
		address: zookeeper://host:port
		id: xxx
	application:
		name: xxx
然后在与服务提供者提供的服务接口完全相同的全限定路径下创建Service接口
比如
服务提供 com.sun.health.provider.provide.service.HelloServicce
则消费者需要创建 com.sun.health.provider.provide.service.HelloService接口
无需在启动类的包下也可以
然后在需要的地方使用
@Reference()注解注入服务即可调用
@RestController
@RequestMapping("")
public class HelloController {
	@Reference()
	private HelloService helloService;
	@RequestMapping("")
	public String hello(String name) {
		return helloService.hello(name); 
	}
}
以上一个最简单的Dubbo系统即创建成功

## Dubbo示例
1.启动时检查
缺省会在启动时检查依赖的服务是否可用，不可用时会抛出异常，阻止Spring初始化完成，默认为check=true
可以通过check=false关闭检查 比如测试时某些服务不关心 或者出现循环依赖 必须一方先启动
另外如果Spring容器是懒加载的 或者通过API变成延迟引用服务 清关闭check
A.服务检查
<dubbo:reference check="true/false" />
@Reference(check=true/false)
B.消费者检查
<dubbo:consumer check="false" />
dubbo:
	consumer: 
		check: false
C.注册中心关闭检查
<dubbo:registry check="false" />
dubbo:
	registry:
		check: false
2.集群容错
集群调用失败后 Dubbo提供多种容错方案 缺省为failover重试
有多种FaliXXX Cluster可供选择
A.failover cluster
失败自动切换 当出现失败 重试其他服务器 通常用于读操作 reties指定数量不含第一次
<dubbo:service retries="2" />
<dubbo:reference retries="2" />
<dubbo:reference>
	<dubbo:method name="" retries="2" />
</dubbo:reference>
B.failfast cluster
快速失败 只发起一次调用 失败立即报错 通常用于非幂等性的写操作 比如新增记录
C.failsafe cluster
失败安全 出现异常直接忽略 通常用于写入日志
D.failback cluster
失败自动恢复 后台记录失败请求 定时重发 通常用于消息通知操作
E.forking cluster
并行调用多个服务器 只要一个成功即返回 通常用于实时性要求较高的读操作 forks="2"
F.broadcast cluster
广播调用所有提供者 逐个调用 任意一个报错 则报错 通常用于通知所有提供者更新缓存或日志等本地资源

集群模式配置
<dubbo:service cluster="" />
<dubbo:reference cluster="" />
3.负载均衡
提供多种负载均衡策略 默认为random随机调用
A.random loadbanlance
随机 按权重设置随机概率
B.roundrobin loadbalance
轮询 按公约后的权重设置轮询比率
C.leastactive loadbalance
最少活跃调用数 相同活跃数的随机 活跃数值调用前后计数差
D.consistenthash loadbanlance
一致性hash 相同参数的请求总是发到同一提供者
服务端服务级别
<dubbo:service loadbalance="" />
服务端方法级别
<dubbo:service>
	<dubbo:method loadbalance="" />
消费者服务级别
<dubbo:reference loadbalance="" />
消费者方法级别
<dubbo:reference>
	<dubbo:method loadbalance="" />
4.线程模型
如果事件处理的逻辑能迅速完成 并且不会发起新的IO请求 则直接在IO线程上处理更快
<dubbo:protocol name="dubbo" dispatcher="all" threadpool="fixed" threads="100" />
dispatcher 
a.all 所有消息都派发到线程池中 包括请求 响应 连接事件 断开事件 心跳等
b.direct 所有消息都不派发到线程池中
c.message 只有请求响应消息派发到线程池 其他连接断开事件，心跳等消息直接在IO线程上执行
d.execution 只请求消息发到线程池 不含响应
e.connection 在IO线程上 将连接断开事件放入队列 有序逐个执行 其他消息派发到线程池中
threadpool
a.fixed 固定大小线程池 启动时创建 一直持有
b.cached 缓存线程池 空闲一分钟自动删除 需要时创建
c.limited 可伸缩线程池 但池中的线程数只会增长不会收缩
d.eager 优先创建worker线程池
5.直连提供者
开发及测试环境下 经常需要绕开注册中心 只测试指定服务提供者
<dubbo:reference url="" />
@Reference(url="")
通过-D参数指定 java -Dxxx.xxxService=dubbo://url
6.只订阅
为了方便开发测试 经常会在线下公用一个所有服务可用的注册中心 如果让正在开发的服务提供者注册可能造成影响
可以让服务提供者方只订阅 而不注册开发的服务
<dubbo:registry registry="false" subscribe="true" />
或者
<dubbo:registry address=protocol://host:port?registry=false" />
7.只注册
<dubbo:registry registry="true" subscribe="false" />
或
<dubbo:registry address="xxx?subscribe=false" />
8.静态服务
有时希望人工管理服务提供者的上线和下线 此时需要将注册中心标识为非动态管理模式
<dubbo:registry dynamic="false" />
<dubbo:registry address="xxx?dynamic=false" />
服务提供者初次注册时为禁用状态 需人工启动 断线时 不会自动删除 需要人工禁用
9.多协议
Dubbo允许配置多协议 在不同服务上支持不同协议或者同一服务上同时支持多种协议
<dubbo:serivce protocol="dubbo|rmi|dubbo,rmi" />
10.多注册中心
Dubbo支持同一服务向多个服务中心同时注册或者不同服务分别注册到不同注册中心上,甚至可以同时引用注册在不同注册中心上的同名服务
多注册中心注册
<dubbo:registry address="" id="r1" />
<dubbo:registry address="" id="r2" default="false" />
<dubbo:service registry="r1,r2" />
不同服务使用不同注册中心
<dubbo:registry address="" id="r1" />
<dubbo:registry address="" id="r2" default="false" />
<dubbo:service registry="r1" />
<dubbo:service registry="r2" />
多注册中心引用
<dubbo:registry address="" id="r1" />
<dubbo:registry address="" id="r2" default="false" />
<dubbo:reference registry="r1" /> 
<dubbo:reference registry="r2" />
如果只是在测试环境临时需要连接两个不同注册中心 使用竖号分隔多个注册中心地址
<dubbo:registry address="xxx|yyyy" />
11.服务分组
一个接口有多种实现时可以用group分组
<dubbo:service group="g1" interface="AService" />
<dubbo:service group="g2" interface="AService" />
引用
<dubbo:reference id="s1" group="g1" interface="AService" />
<dubbo:reference id="s2" group="g2" interface="AService" />
任意组
<dubbo:reference group="*" />
12.多版本
一个接口实现 出现不兼容升级时 可以用版本号国都 版本号不听的服务相互间不引用
0 在低压力时间段 先升级一半提供者为新版本
1 再将所有消费者升级为新版本
2 然后将剩下的一半提供者升级为新版本
<dubbo:reference version="*" />
13.分组聚合
一个接口多个实现的时候 可能需要每个都调用一次之后合并结果返回 
配置
所有方法都合并
<dubbo:reference group="*" merge="true" />
只合并指定方法
<dubbo:reference group="*">
	<dubbo:method name="" merge="true" />
某个方法不合并 其他合并
<dubbo:reference group="*" merge="true">
	<dubbo:method name="" merge="false" />	
指定合并策略 缺省根据返回类型自动匹配 如果有两个合并器时 需要指定合并器
<dubbo:reference group="*">
	<dubbo:method name="" merger="myMerger" />
指定合并方法
<dubbo:reference group="*">
	<dubbo:method name="" merger=".addAll" />
14.参数验证
需要引入依赖
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
    <version>1.0.0.GA</version>
</dependency>
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>4.2.0.Final</version>
</dependency>
然后在需要验证的字段上添加相应的注解
<dubbo:service validation="true" />
<dubbo:reference validation="true"/>
15.结果缓存
lru 基于最近最少使用原则删除多于缓存
threadlocal 当前线程缓存
jcache 与JSR107集成
<dubbo:reference cache="lru" />
<dubbo:reference>
	<dubbo:method name="" cache="lru" />
16.泛化引用
泛化接口调用方式主要用于客户端没有API接口及模型类元的情况，参数和返回值中的所有POJO均用Map表示 通常用于框架集成
<dubbo:reference id="" interface="" generic="true" />
然后使用
ObjectService barService = applicationContext.getBean("xxx");
Object result = barService.$invoke("methodName", new String[]{"类的全限定名"}. new Object[]{"实际参数"});
17.泛化实现
@org.apache.dubbo.config.annotation.Service(interfaceName="", version="")
public class SorryService implements GenericService {
	public Object $invoke(String methodName, String[] paramClasses, Object[] params) {
	
	}
}
使用
@Reference(interfaceName="", version="", generic=true) 
private GenericService xxxService;
即可
18.回声测试
用于检测服务是否可用 
所有服务自动实现EchoService接口 强制转型即可使用
19.上下文信息
RpcContext.getContext()
20.隐式参数
服务消费者端设置隐式参数
RpcContext.getContext.setAttachment(key, value) // 隐式传参，后面的远程调用都会隐式将这些参数发送到服务器，类似于Cookie，用于框架集成，不建议常规业务使用
消费提供者端获取隐式参数
RpcContext.getContext().getAttachment(key);
21.Consumer异步调用
从v2.7.0开始 Dubbo的所有异步调用以CompletableFuture为基础
基于NIO的非阻塞实现并行调用 客户端不需要启动多线程即可完成并行调用多个远程服务
使用CompatabeFuture<?>签名的接口
public interface AsyncService {
	ComplateableFuture<String> sayHello(String name);
}
调用
CompletableFuture<String> future = asyncService.sayHello("");
future.whenComplete((v, t) -> {
	if(t != null) {
		t.printStackTrace();
	} else {
		System.out.println("Value" + v);
	}
});
System.out.println("Executed before resposne return.");
使用RpcContext
配置
<dubbo:reference>
	<dubbo:method name="" async="true" />
调用代码
asyncService.sayHello("");
CompletableFuture<String> future = RpcContext.getContext.getCompatableFuture();
future.whenComplete((v, t) -> {

});
或者
CompletableFuture<String> future = RpcContext.getContext().asyncCall(() -> {
	asyncService.sayHello("");
});
future.get();
重载服务接口
如果只有同步服务定义 而不喜欢RpcContext的异步使用方法
还有一种方法 利用Java8提供的default接口实现 重载一个带有CompletableFuture签名的方法
public interface GreetingService {
	String sayHi(String name);
	
	default CompletableFuture<String> sayHi(String name, AsyncSignal signal) {
		return CompletableFuture.completedFuture(sayHi(name));
	}
}
可以设置是否等待消息发出
sent="true" 等待消息发出 消息发出失败将抛出异常
sent="false" 不等待消息发出 将消息放入IO队列 即可返回
如果只想异步 完全忽略返回值 可以配置return="false" 减少Future对象的创建和管理成本
异步总是不等待返回
22.Provider异步执行
Provider端异步执行将阻塞的业务从Dubbo内部线程池切换到业务自定义线程池 避免过度占用 
定义CompletableFuture签名的接口
public interface AsyncService {
	CompletableFuture<String> sayHello(String name);
}
public class AsyncServiceImpl implements AsyncService {
	public CompletableFuture<String> sayHello(String name) {
		RpcContext savedContext = RpcContext.getContext();
		return CompletableFuture.supplyAsync(() -> {
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTreace();
			}
			return "async response from provider.";
		});
	}
}
使用AsyncContext
public class AsyncServiceImpl implements AsyncService {
	public String sayHello(String name) {
		final AsyncContext asyncContext = RpcContext.startAsync();
		new Thread(() -> {
			// 如果要使用上下文 必须放在第一行执行
			asyncContext.signalContextSwitch();
			try {
				Thread.sleep(5000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			asyncContext.write("");
		}).start();
	}
}
23.本地调用
injvm协议 是一个伪协议 不开启端口 不发起远程调用 只在JVM内直接关联
<dubbo:protocol name="injvm" />
<dubbo:provider protocol="injvm" />
<dubbo:service protocol="injvm" />
<dubbo:consumer injvm="true" />
<dubbo:provider injvm="true" />
<dubbo:service injvm="true" />
<dubbo:reference injvm="true" />
24.参数回调
25.事件通知
26.本地存根
远程服务后 客户端通常只剩下几口 而实现全在服务器端 但提供方有时需要在消费端也执行部分逻辑 比如做ThreadLocal缓存 提前验证参数 调用失败后伪造容错数据等 需要在API中带上Stub 客户端生成Proxy实例 会把Proxy通过构造函数传给Stub 然后把Stub暴露给用户 Stub可以决定是否调用Proxy
服务提供者端不变
在消费端添加一个Stub
比如
public class HelloServiceStub {
	private HelloService helloService;
	public HelloServiceStub(HelloSerivce helloService) {
		this.helloService = helloService;
	}
	public String hello(String name) {
		try {
			return this.helloService.hello(name);
		} catch(Exception e) {
			return "容错";
		}
	}
}
最后在引入的时候加上stub="xxx.xxx.HelloServiceStub"
@Reference(stub="xxx.xxx.HelloServiceStub")
private HelloService helloService;
27.本地伪装
通常用于服务降级 比如某验权服务 当服务提供方全部失效 客户端不抛出异常 而是通过Mock数据返回授权失败
<dubbo:reference mock="" />
mock可以是类的全限定名
public class HelloServiceMock implements HelloSerive {
	public String hello(String name) {
		return "Mock 容错";
	}
}
在服务提供端失效时 返回此处提示而不是报错
也可以是其他进阶
A.return 
mock="return xxx"
a.empty 代表空 基本类型的默认值 或者集合类的空值
b.null null
c.true true
d.false false
e.JSON格式 反序列化JSON所得到的对象
B.throw
mock="throw xxx"
mock="throw" 默认抛出一个RpcException的错误
mock=throw xxx.xxx.XXException" 抛出指定错误
C.force和fail
2.6.6以上的版本可以使用force:或fail:
force:强制使用mock 不走服务提供者
fail:则与默认行为一致
可以在方法级别配置mock
28.延迟暴露
如果服务需要预热时间 比如初始化缓存 等待资源等 可以使用delay进行延迟暴露
2.6.5之前的版本
<dubbo:service delay="" /> -1 延迟到Spring初始化完成后暴露 5000代表延迟5秒
2.6.5及之后
默认所有服务都在Spring初始化完成之后暴露 
<dubbo:service delay="" /> 5000表示5秒后
29.并发控制
<dubbo:service executes="10" /> 服务提供端并发执行（或占用线程数）不超过10
<dubbo:service>
	<dubbo:method executes="10" />
<dubbo:service actives="10" />  每个客户端并发占用执行不超过10个
<dubbo:reference actives="10" /> 同上
<dubbo:service>
	<dubbo:method actives="10" />
<dubbo:reference>
	<dubbo:method actives="10" />
Load Balance 负载均衡
<dubbo:reference loadbalance="leastactive" /> 会调用并发数最小的服务器
30.连接控制
<dubbo:protocol accepts="10" />
<dubbo:provider accepts="10" /> 限制服务器端能接受的连接不超过10个

<dubbo:reference connections="10" />
<dubbo:service connections="10" /> 客户端使用连接数不超过10个
31.延迟连接
用于减少长连接数 当有调用时 再创建长连接
<dubbo:protocol name="dubbo" lazy="true" />
32.粘滞连接
用于有状态连接 尽可能让客户端总向同一提供者发起调用 除非失效
粘滞连接自动开启延迟连接
<dubbo:reference sticky="true" />
<dubbo:reference>
	<dubbo:method sticky="true" />
33.令牌验证
在注册中心控制权限 决定是否下发令牌给消费者 可以防止消费绕过注册中心访问提供者 另外通过注册中心可以灵活改变授权方式
<dubbo:provider token="true" /> 使用UUID随机生成
<dubbo:provider token="123" /> 使用固定token
<dubbo:service token="xxx" />
<dubbo:protocol token="xxx" /> 协议级别
34.路由规则
发起一次RPC调用前起到过滤目标服务器地址的作用 过滤后的地址列表 将作为消费端最终发起RPC调用的备选地址
A.条件路由 支持以服务或Consumer应用为粒度配置
应用粒度
	scope: application 
	force: true # 
	runtime: true
	enabled: true
	key: governance-conditionrouter-consumer # consumer的dubbo.application.name
	conditions:
		application=app1 => addres=*:20880
		application=app2 => addres=*:20881
服务粒度
	scope: service
	force: true
	runtime: true
	enabled: true
	key: org.apache.dubbo.samples.governance.api.DemoService # 服务全限定名
	conditions:
		application=sayHello => addres=*:20880
		application=sayHi => addres=*:20881
字段含义
scope 表示路由规则的作用粒度 service 服务粒度 application 应用粒度
key 作用于哪个服务或应用
	scope=service时 格式为[{group}:]{service}[:{version}]
	scope=application时 为application名称
enabled=true 是否生效 默认true
force=true 路由结果为空时是否强制执行 默认false
runtime=true 是否每次调用时执行路由规则 否则只在提供者地址列表变更时执行并缓存结果 默认false
priority=1 优先级 默认为0
conditions  具体路由规则内容
Conditions规则体
=>之前为消费者匹配条件 为空表示所有消费者
=>之后为提供者地址列表的过滤条件 为空表示禁止访问
B.标签路由 标签路由 以Provider应用为粒度配置
通过将若干个服务的提供者划分到同一分组 约束流量只在指定分组流转
动态打标
force: false
runtime: true
enabled: true
key: xxx
tags:
	-name: tag1
	 address: ["host:port"]
	-name: tag2
	 address: ["host:port"]
静态打标
<dubbo:provider tag="tag1" />
35.配置规则
覆盖规则是Dubbo设计在无需重启应用的情况下 动态调整RPC调用行为的一种能力 2.7.0开始 支持从服务和应用两个粒度来调整动态配置
应用粒度
scope: application 
key: demo
enabled: true
configs:
-address: ["host:port"]
 side: provider 
 parameters:
	weight: 1000
服务粒度
scope:service
key: xxx
enabled: true
configs:
-address: ["host:port"]
 side: consumer
 parameters:
	timeout: 6000
规则详解
模板
---
scope: application/service
key: app-name/group+service+version
enabled: true
configs:
- addresses: ["0.0.0.0"]
  providerAddresses: ["1.1.1.1:20880", "2.2.2.2:20881"]
  side: consumer
  applications/services: []
  parameters:
    timeout: 1000
    cluster: failfase
    loadbalance: random
- addresses: ["0.0.0.0:20880"]
  side: provider
  applications/services: []
  parameters:
    threadpool: fixed
    threads: 200
    iothreads: 4
    dispatcher: all
    weight: 200
...
scope 配置作用范围 application service
key 作用在哪个服务或应用上
enabled=true 是否启用
configs 定义具体的覆盖规则 
36.服务降级
临时屏蔽某个出错的非关键服务 并定义降级后的返回策略
RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
Registry registry = registryFactory.getRegistry("zookeeper://host:port");
registry.register("URL.valueOf("override://host/com.foo.BarService?category=configurators&dynamic=false&application=foo&mock=force:return+null"));
mock=force:return+null表示消费方对该服务的方法调用都直接返回null 不发起远程调用
37.优雅停机
通过JDK的ShutdownHook来完成优雅停机的 KILL PID才能执行 不能加信号
原理
服务提供方
停止时 先标记为不接受新请求 新请求过来直接报错 让客户端尝试其他服务器
然后 检测线程池中的线程是否还在执行 有则等待执行完成 除非超时则强制关闭
服务消费方
停止时 不再发起新的调用请求 所有新的调用在客户端即报错 
然后 检测有没有请求的响应还没有返回 等待响应返回 除非超时则强制关闭
设置方式
dubbo.service.shutdown.wait=15000 优雅停机时间
38.主机绑定 
<dubbo:protocol host="xx.xx.xx.xx" />
端口配置
缺省主机端口和协议相关
dubbo 20880
rmi 1099
http 80
hession 80
webservice 80
memcached 11211
redis 6379
39.日志适配
<dubbo:application logger="log4j" />
dubbo.application.logger=log4j
40.访问日志
想记录每一次请求信息 可开启访问日志 类似于apache的访问日志
<dubbo:protocol accesslog="true" />
<dubbo:protocol accesslog="path" /> 指定日志输出文件
41.服务容器
服务容器是一个standalone的启动程序 服务容器只是一个简单的Main方法 并加载一个简单的Spring容器 用于暴露服务
容器类型
Spring Container
自动加载META-INF/spring目录下的所有Spring配置
配置spring配置加载位置
dubbo.spring.config=classpath*:META-INF/spring/*.xml
42.ReferenceCongif缓存
43.分布式事务
尚未实现



 
	 
### Redis
--- 数据类型
bitmap 用于统计日活、月活(面试问到的)
bitmap是一串连续的2进制数字，每一位所在的位置为偏移（offset），在bitmap上可以执行AND、OR、XOR以及其他位操作
位图计算（Population count）
位图计数统计的是bitmap中值为1的位的个数。
redis允许使用二进制数据的Key和二进制数据的Value。Bitmap就是二进制数据的value
Redis的setbit(key, offset, value)操作对指定的key的value的指定偏移量的位置1或0，时间复杂度是O(1)
setbit key offset value
getbit key offset
bitop AND destkey srckey1, key2, key3... 多一个或多个key求逻辑与，将结果存到destkey变量名中
bitop OR destkey srckey1, key2, key3... 多一个或多个key求逻辑或，将结果存到destkey变量名中
bitop XOR destkey srckey1, key2, key3... 多一个或多个key求逻辑异或，将结果存到destkey变量名中
bitop NOT key1, key2 求逻辑非，将结果存到destkey中
bitcount key start end 统计bitmap中为1的位数 start end以字结为单位（1字结=8位） 0 -1表示所有


-- Redis配置
默认只能本地连接
远程连接需要修改
bind 127.0.0.1 -> bind 0.0.0.0 或者直接注释 可以访问的IP地址
protected-mode yes -> protected-mode no 保护模式
requirepass -> requirepass MIMA 设置访问密码 安全起见
引入redis的spring boot依赖
在Spring Boot的application.yml中配置redis信息
spring:
    redis:
        host: redis主机地址
        port: redis端口
        password: redis访问密码
再使用StringRedisTemplate、RedisKeyValueTemplate或者自定义的格式化RedisTemplate接口实现类完成与Redis的交互

### SQL

SQL语言分为3部分
1.DDL Data Declaration Language 数据描述语言
用于定义数据库的内容 并维护数据完整性
2.DML Data Manipulation Language 数据操作语言
执行查询、插入、更新以及删除操作
3.DCL Data Control Language 数据控制语言
不是数据安全相关的语言 而是一门关于访问控制的语言
实体表
物理或逻辑上具有某一含义的事物 在关系型数据库中 实体由属性定义
关系表
列指向一个或多个实体表
行与记录
列与字段
模式对象
CREATE SCHEMA语句
事务与并发控制
会话
用户登录的标准SQL语法
CONNECT TO <连接目标>
<链接目标>::=<SQL服务器名称> [AS <连接名称>] [USER <用户名>] | DEFAULT
建立连接后，能访问数据库中具备权限的部分。在会话中，用户可以执行任意次事务。在用户执行COMMIT WORK(提交事务)操作之前，所执行的插入、更新、删除行的操作的变更结果并不会被数据库永久保留
如果不想这些操作变动持久化到数据库中，可以发起ROLLBACK WORK(回滚)命令，数据库将停留在这些操作之前的持久化状态。
事务与ACID
Atomicity 原子性
Consistency 一致性
Isolation 隔离性
Durability 持久性
原子性
要么整个事务都持久化到数据库中，要么都不持久化 标准数据库中 COMMIT持久化 ROLLBACK则清除事务
在发现错误时，只要没有进行相反的配置，大多数SQL引擎都会执行ROLLBACK操作
定义了SAVEPOINTG(保存点)机制 附带有Chain的可选项 事务在执行过程中可以设置保存点 并将事务本地回滚到保存点
<保存点语句>::=SAVEPOINT <保存点标识>
<保存点标识>::=<保存点名称>
保存点级别
<保存点级别>::=NEW SAVEPOINT LEVEL | OLD SAVEPOINT LEVEL
取消保存点
<释放保存点语句>::=RELEASE SAVEPOINT <保存点说明>
提交语句
<提交语句>::=COMMIT [WORK] [AND [NO] CHAIN]
回滚语句
<回滚语句>::=ROLLBACK [WORK] [AND [NO] CHAIN] [<保存点子句>]
<保存点子句>::=TO SAVEPOINT <保存点说明>
一致性
事务开始以及持久化到数据库之后，数据库都处于一致性状态 一致性状态以为这数据库同时满足数据完整性约束、关系完整性约束以及其他约束条件
隔离性
事务之间都是相互隔离的
持久性
数据库存储在持久化介质中
并发控制
三种现象
在SQL模型中 事务可以通过三种方式影响其他事务
P0 脏写
P1 脏读
P2 不可重复读
P3 幻读
P4 更新丢失
隔离级别
<设置隔离级别语句>::=SET TRANSACTION <事务模式列表>
<事务模式>::=<隔离级别> | <事务访问模式> | <诊断范围大小>
<诊断范围大小>::=DIAGNOSTICS SIZE <条件数> # 通知数据库建立指定大小的错误信息列表 一条语句中可能会有很多错误 引擎应该找到所有问题 并在调用GET DIAGNOSTICS语句时显式
<事务访问模式>::=READ ONLY | READ WRITE #
<隔离级别>::=ISOLATION LEVEL <隔离级别类型> # 定义了事务受其他并发事务影响的程度 默认级别是SERIALIZABLE
<隔离级别类型>::=READ UNCOMMITTED # 会出现P1 P2 P3
                | READ COMMITTED  # 会出现P2 P3
                | REPEATABLE READ # 会出现P3
                | SERIALIZABLE # 不会出现事务干扰
保守式并发控制
加锁
锁级别
数据库锁
表级锁
行级锁
页级锁 # 介于表级锁和行级锁之间 为某张表中包含期望值的行子集加锁
快照隔离与乐观式并发
快照隔离 每个事务读取的数据 是事务开始时该（已提交的）数据的快照
提交事务时 只有在快照时间到现在没有任何事务对同一数据执行写操作 否则将回滚 当提交成功后 对于晚于此时间点的快照都是可见的
逻辑并发控制
能够对等待执行的查询队列进行谓词分析和逻辑处理 以判定数据库允许那些语句同时执行
死锁与活锁
数据库模式对象
CREATE SCHEMA语句
<模式元素>::=<域定义> | <数据表定义> | <视图定义> | <授权语句> | <断言定义> | <字符集定义> | <排序规则定义> | <翻译定义>
CREATE DOMAIN语句
域是标准SQL中的一种模式元素 可用来生命内联宏 从而在模式中放入常用的列定义
<创建域>::=CREATE DOMAIN <域名> [AS] <数据类型> [<默认子句>] [<域约束>...] [<排序规则子句>]
<域约束>::=[<约束名定义>] <check 约束定义> [<约束属性>]
<修改域>::=ALTER DOMAIN <域名> <修改域动作>
<修改域动作>::=<修改域默认值子句> | <删除域默认值子句> | <添加域约束定义> | <删除域约束定义>
域只能声明为基本数据类型 用其他域类型声明域是不允许的 域声明成功后将渠道相关数据类型
创建序列
序列可以理解为数值序列生成器 序列的调用方式与函数调用一致 每次调用后 返回序列中的下一数据
CREATE SEQUENCE <序列名> AS <数据类型> START WITH <起始值> INCREMENT BY <递增值> [MAXVALUE <最大值>] [MINVALUE <最小值>] [[NO] CYCLE];
获取序列中的值
NEXT VALUE FOR <序列名>
重置序列
ALTER SEQUENCE <序列名> RESTART WITH <起始值>;
删除序列
DROP SEQUENCE <序列名>
创建断言
CREATE ASSERTION语句允许创建一类能够应用于指定模式下所有表的约束条件
<断言定义>::=CREATE ASSERTION <约束名> <断言检查> [<约束属性>]
<断言检查>::=CHECK (<检查条件>)
删除断言
DROP ASSERTION <断言名>
断言可以使用模式下的所有表 因此其功能强于单个表的CHECK约束
字符集相关
定位数据和特殊数值
Oracle使用ROWID特殊变量 用于显示存储在硬盘驱动器中行（row）的物理地址
标识列
标识列是数据库引擎为每个新增行自动生成唯一数的机制。为了确保表中每个新增行都具有唯一数值标识，可以再标识列上定义一个唯一索引或者声明为表的主键。表一旦创建，就不能通过修改表的声明方式增加标识列。
如果往数据表中插入行时显式指定标识列的数值，系统就不会更新下一个生成的标识数值，就可能导致新增值和已有值冲突。
生成的标识符
GUID
有UTC时间和所在设备网络地址混合在一起，形成具有唯一性的显式物理定位器。
UUID
一个16字结（128位）数，包含32个16进制数字，这个数被连字号分割为五组来显示，表示形式为8-4-4-4-12，总共36个字符（32个数字和4个连字号）
一共五版UUID
第五版UUID使用了SHA-1执行散列操作，将URL、完全限定域名、对象标识符和其他数据元素转换为UUID。为了使UUID长度有效，160位SHA-1散列值被截断为128位。
基础表和相关元素
CREATE TABLE 语句
CREATE TABLE <表名> (<表元素列表>)
<表元素列表>::=<表元素>|<表元素>,<表元素列表>
<表元素>::=<列定义>|<表约束定义>
列约束
<列定义>::=<列名> <数据类型> [<默认子句>] [<列约束>...] [<约束属性>]
<列约束>::=NOT NULL | <check约束定义> | <唯一性说明> | <引用说明> [<约束属性>]
数据类型可以归为三大类 数值、字符以及时间类型
列约束是依附于列上的规则，行约束则依附于同一行中的多列，表约束则应用于多行。
可以为约束命名，并赋予其他的一些属性。
<约束名称定义>::=CONSTRAINT <约束名称>
<约束属性>::=<约束检测时间> [[NOT] DEFERRABLE] | [NOT] DEFERRABLE <约束检测时间>
<约束检测时间>::=INITIALLY DEFERRED | INITIALLY IMMEDIATE
DEFAULT 子句
<>::=[CONSTRAINT <约束名称>] DEFAULT  <默认选项>
<默认选项>::=<用户输入的字面值> | <系统值> | NULL
<系统值>::=CURRENT_DATE | CURRENT_TIME | CURRENT_TIMESTAMP | SYSTEM_USER | SESSION_USER | CURRENT_USER
NOT NULL 约束
禁止某一列使用空值。
CHECK约束
通过逻辑表达式对表中的值进行测试。
CHECK约束可以是一个简单的查询条件。
UNIQUE以及PRIMARY KEY约束
unique约束意味某一列或某些列中不允许出现重复值。
一个表中可以有多个unique约束，但是只能有一个主键。主键会自动添加notnull约束，而unique在比较时会先排除所有空值。
REFERENCES子句
<引用定义>::=[CONSTRAINT <约束名称>] REFERENCES <引用表和列> [MATCH <匹配类型>] [引用触发动作]
<匹配类型>::=FULL|PARTIAL
如果声明中未提供引用列，那么被引用表的Primary Key列会被视为被引用列。
多列外键中的列必须是多列主键，或者瞒住Unique约束
[CONSTRAINT <约束名称>]
FOREIGN KEY (<列名列表>)
REFERENCES <被引用表名称> [(<引用列列表>)]
REFERENCES语句可以包含两个子句，当数据库事件修改被引用表时，这两个子句将执行对应的动作
<引用触发动作> ::= <更新规则> [<删除规则>] | <删除规则> [<更新规则>]
<更新规则> ::= ON UPDATE <引用动作>
<删除规则> ::= ON DELETE <引用动作>
<引用动作> ::= CASCADE | SET NULL | SET DEFAULT | NO ACTION
CASCADE选项会将引用表中的数据修改成被引用表中的值
SET NULL将引用表中的值设置为空值
SET DEFAULT选项将引用表中的值设置为默认值
NO ACTION则不做任何变更
嵌套Unique约束
重叠键
单列唯一性和多列唯一性
CREATE ASSERTION约束
对模式下的所有表施加约束
<断言定义>::=CREATE ASSERTION <约束名称> <断言检查> [<约束属性>]
<断言检查>::=CHECK (<查找条件>)
临时表
TEMPORARY表与基本表类似，不过临时表中的行可被自动删除，可以全局临时表，被所有用户共享，也可以使局部临时表，被一个用户访问。
TEMPORARY TABLE声明
CREATE [GLOBAL | LOCAL] TEMP[ORARY] TABLE <临时表名> (<表元素列表>) ON COMMIT [PRESERVE | DELETE] ROWS;
表操作
CREATE TEABLE
DROP TABLE
ALTER TABLE
表名必须在模式中唯一，列名必须在表中唯一
SQL中的名字可包含字母、下划线和数字
DROP TABLE <表名>
用于移除数据库中的表
<删除语句>::=DROP TABLE <表名> [<移除行为>]
移除行为::=RESTRICT | CASCADE
ALTER TABLE
能够增加、删除或者修改表中的列和约束，标准SQL定义。
ALTER TABLE <表名> <修改表动作>
<修改表动作>::=
| DROP [COLUMN] <列名> <移除行为>
| ADD [COLUMN] <列定义>
| ALTER [COLUMN] <列名> <修改列动作> (MySQL中是modify <列名> <列定义> 或者 change <列名> <新列名> <列定义>)
| ADD <表约束定义>
| DROP CONSTRAINT <约束名称> <移除行为>
显式物理定位器
自增列
过程式、半过程式以及声明式编程
过程式结构
SQL虽然是一门声明式语言，却包含一些过程性的特性，如存储过程、触发器以及游标
创建过程
创建触发器
DECLARE CURSOR 语句
ORDER BY 子句
并不是SELECT语句的一部分，而是CURSOR声明语句的一部分，游标是宿主语言获取查询结果的唯一途径，厂商提供的工具已经在内部为用户创建了游标
<ORDER BY子句>::=ORDER BY <排序规范列表>
<排序规范列表>::=<排序规范> [{<,> <排序规范>}...]
<排序规范>::=<排序键> [<排序规则子句>] [<顺序规范>]
<排序键>::=<列名>|<标量表达式>
<顺序规范>::=[ASC|DESC] {NULLS FIRST | NULLS LAST}
ORDER BY和NULL
NULL的排序键值跟非空键的比较取决于不同的数据库产品
辅助表
序列表
SQL的数据数值
数值类型
SQL数值包含精确数值或近似数值两大类，精确数值包含精度P和小数部分S，精度是一个正数值，确定某种进制数的有效位数，小数部分是一个非负整数，表示该数值有几位小数
数值类型NUMERIC、DECIMAL、INTEGER、BIGINT和SMALLINT都属于精确数值类型，整数的小数位为0
DECIMAL(p,s)也可以书写为DEC(p,s)
BIT、BYTE和BOOLEAN数据类型
数值类型的转换
数值的舍入和截断
四则运算函数
模数函数 MOD(NUM1, NUM2)
绝对值函数 ABS(NUM)
自然对数函数 LN(NUM)
指数函数 EXP(NUM)
乘方运算 POWER(NUM, NUM)
平方根运算 SQRT(NUM)
小于或等于参数的最大整数 FLOOR(NUM)
大于或等于参数的最小整数 [CEIL|CEILING](NUM)
SQL中的时间数据类型
日期类型(DATE、TIME和TIMESTAMP)表示时间轴上的点，而时间间隔数据类型以及INTERVAL(DAY、HOUR、MINUTE和带小数点形式的SECOND)则代表某一段持续时间
时间内部表示
日期格式标准
遵循ISO 8601规范，标准SQL采用了全数字yyyy-mm-dd hh:mm:ss[ss...]格式
处理时间
需要以UTC格式存储时间，并增加一个保存本地时区偏移量的数值列，时区从UTC开始，即初始偏移量为零
INTERVAL数据类型
时间算术
相关公式
<日期时间> - <日期时间> = <时间间隔>
<日期时间> + <时间间隔> = <日期时间
<时间间隔> (* or /) <数值> = <时间间隔>
<时间间隔> + <时间间隔> = <时间间隔>
<数值> * <时间间隔> = <时间间隔>
标准CURRENT_DATE函数根据系统时间返回当前日期。不同厂商可能使用不同的函数
字符数据类型
字符串相等问题
LOWER()转为小写 UPPER() 转为大写
不同长度的字符串比较会在右边加上空格补齐
标准字符串函数
||操作符表示进行字符串拼接 也可以使用CONCAT(s1, s2)
SUBSTRING(str, from, to)截取子字符串
LEFT() 左边指定数量的子字符串 RIGHT() 右边指定数量的子字符串
UPPER() LOWER()
TRIM([[<裁剪格式>] [<裁剪字符>] FROM] <裁剪源>)生成一个字符串，去除指定的字符 <裁剪格式>可以使LEADING、TRAILING或BOTH，若不指定裁剪字符则默认为空格
TRANSLATE()
CHAR_LENGTH()
OCTET_LENGTH()以8位组为单位计算出指定字符串的长度
POSITION(<搜索字符串> IN <源字符串>)
NULL:SQL中的缺失数据
上下文和缺失值
比较NULL
一个NULL不能与另一个NULL进行比较
表操作
DELETE FROM 语句
WHERE子句
没有WHERE子句的DELETE FROM将删除表中的所有行
INSERT INTO语句
<INSERT INTO语句>::=INSERT INTO <插入目标> <插入列及源>
<插入目标>::=<表名>
<插入列及源>::=<子查询源>|<构造器源>|<默认值源
<子查询源>::=[(插入列列表)][<覆写源>]<查询表达式>
<构造器源>::=[(插入列列表)][<覆写子句>]<上下文类型的表值构造器>
<覆写子句>::=
<默认值源>::=DEFAULT VALUES
<插入列列表>::=<列名列表>
UPDATE语句
CASE表达式
<CASE规范>::=<简单CASE>|<搜索CASE>
<简单CASE>::=CASE <case 操作数>	<简单的when子句>... [<else子句>] END
<搜索CASE>::=CASE <case 操作数>	<简单的when子句>... [<else子句>] END



- 面试问题:
1.分组选择最新的一条记录
A.使用group by和order by
SELECT * FROM (SELECT * FROM user_record ORDER BY create_time DESC) as tmp GROUP BY user_id;
B.先分组取得每组最大的时间和用户ID，再根据这两列数据关联查询
SELECT u.* FROM user_record AS u, (SELECT user_id, MAX(create_time) AS newest_time FROM user_record GROUP BY user_id) AS tmp WHERE u.user_id = tmp.user_id AND u.create_time = tmp.newest_time;
C. 自联查询 速度较快 *
SELECT u.* FROM user_record AS u WHERE u.create_time = (SELECT MAX(create_time) FROM user_record WHERE user_id = u.user_id);


### Java基础
- Collection 容器
集合
除了Map，都实现了Iterator接口，用于遍历集合中的元素，主要有hasNext()，next(),remove()三种方法。它的一个子接口ListIterator有添加了三种方法，分别是add()、previous()、hasPrevious()。Iterator接口只能向后遍历，被遍历的元素不能再次遍历到，通常无序集合都实现这个接口，比如HashSet；有序则实现ListIterator接口，比如ArrayList
集合详解
1.Iterator接口：迭代器，是Java集合的顶层接口（不包括Map）
Object next() 返回迭代器刚越过元素的引用，返回值是Object，需要强制转换
boolean hasNext() 是否有下一个元素
void remove() 删除迭代器刚越过的元素
public interface Collection<T> extends Iterable<T>
Iterator和Iterable的关联和区别
public interface Iterable<T> {
    Iterator<T> iterator();
}
Iterable接口封装了获得Iterator对象的方法，所以只要实现了Iterable接口的类就可以使用Iterator迭代器
foreach 需要一个实现Iterable接口的对象实例
2.Collection：List接口和Set接口的父接口
3.List:有序、可以重复的集合
典型的实现
ArrayList 底层数据结构是数组，查询快，增删慢，线程不安全，效率高
Vector 底层数据结构是数组，查询快，增删慢，线程安全，效率低，几乎已经淘汰
LinkedList 底层数据结构是链表，查询慢，增删快，线程不安全，效率高
4.Set：不可重复集合
HashSet
A.不能保证元素的顺序，不可重复，不是线程安全，集合元素可以为Null
B.底层数据结构是数组，存在的意义是加快查询速度
C.对于HashSet，通过equal()方法返回true，则hashCode()返回值相同
LinkedHashSet 不可重复 有序 底层数据结构是链表和哈希表的算法，链表保证有序，哈希表保证唯一性
TreeSet 不可重复 底层使用红黑树算法 放入的对象必须实现Comparable接口 不能存入null值 或者传入Comparator实现类定制排序
以上3个Set的异同
同：不允许元素重复 不是线程安全
异：
HashSet: 不保证顺序，底层采用哈希表算法，查询效率高，需要覆盖hashCode()和equals()方法
LinkedHashSet HashSet的子类，底层采用哈希算法和链表算法 保证有序和唯一 整体性能低于HashSet
TreeSet:不保证添加顺序，但是会进行排序 采用红黑树
- Map
key-value的键值对，key不允许重复
Map由多个Entry<K, V>组成
没有实现Collection接口，也没有继承Iterator接口，不能使用for循环
常用实现类:
HashMap 采用哈希表算法，不保证key按照添加的先后顺序，保证唯一性，key判断重复的标准是hashCode是否相同，equal()方法返回是否是true
TreeMap 采用红黑树算法 key判断是否重复的标准是compareTo/compare的返回值是否为0
LinkedHashMap 采用链表和哈希算法，key保证添加的顺序和唯一性 判断重复继承HashMap
HashTable 采用哈希表算法，是HashMap的前身，遗留类
Properties HashTable的子类，用于加载资源文件(.properties)
ConcurrentHashMap 线程安全，并且锁分离。ConcurrentHashMap内部使用段（Segment）来表示不同部分，每个段就是一个小hashTable，拥有自己的锁，只要修改发生在不同的段上，就可以并发进行。（1.7为Segment,1.8为Node数组、链表和红黑树实现)
Map和Set关系
HashSet就是HashMap的key组成
主要实现类区别
1.Vector和ArrayList
Vector是线程同步的，因此是线程安全的，而ArrayList是线程异步的，是不安全的。而ArrayList效率较高
进行扩容时Vector增长1倍，而ArrayList增长50%
都是使用Object数组进行数据存储，可以通过索引访问，但是插入、删除的操作会导致内部进行数据拷贝影响效率。频繁读取使用Vector、ArrayList，频繁修改使用LinkedList
2.ArrayList和LinkedList
ArrayList基于动态数组的数据结构，LinkedList基于链表的数据结构
随机访问的get和set，ArrayList优于LinkedList，因为LinkedList要移动指针
对于新增和删除操作add和remove，LinkedList占优势
3.HashMap和TreeMap
HashMap通过hashcode对其内容进行存储，而TreeMap元素则保持插入的顺序
HashMap中的元素需要实现hashCode()和equals()方法，而TreeMap中的元素需要实现Comparable接口或提供一个Comparator接口实现类实例
4.HashMap和HashTable
HashTable继承自Dictionary,HashMap继承自AbstractMap，都是都实现了Map接口
HashTable的方法都是synchronize的，因此是线程安全的。而HashMap默认情况下不是线程安全的，但是可以使用Collections.synchronizedMap(Map ...)来获取一个线程安全的HashMap。
HashMap是哈希表配合链表存储数据
HashTable提供contains()方法，而HashMap提供containsKey()和containsValue()方法
HashTable中key和value都不允许null值，HashMap允许一个null值的key，而value值不受限制
都使用了Iterator，但历史遗留原因，HashTable还使用了Enumeration的方法
HashTable直接使用对象的hashCode，而HashMap则根据对象的HashCode重新计算hashCode
HashTable在不指定容量的情况下默认容量为11，而HashMap为16，HashTable不要求数组容量为2的整数次幂，而HashMap则要求。HashMap扩容时增长1倍，而HashTable则为old * 2 + 1
- 多线程
Java线程具有五种基本状态
新建状态 New 当线程对象创建后，进入新建状态，Thread thread = new Thread() {}
就绪状态 Runnable 当调用thread.start()方法后，线程进入就绪状态。就绪状态的线程已经做好准备，随时等待CPU调度执行，并不是调用start()方法之后就会立即执行
运行状态 Running 当CPU开始调用处于就绪状态的线程时，此时线程菜真正执行，进入运行状态。进入运行状态的唯一入口是就绪状态。
阻塞状态 Blocked 处于运行状态中的线程由于某种原因，暂时放弃对CPU的使用权，停止执行，此时进入阻塞状态 知道再次进入就绪状态 才有机会被CPU调用进入运行状态。阻塞状态产生的原因有3种
A.等待阻塞 运行状态中的线程执行wait()方法，使本线程进入等待阻塞状态
B.同步阻塞 线程在获取synchronized同步锁失败（被其他线程占用），进入同步阻塞状态
C.其他阻塞 通过调用线程的sleep()或join()或发生I/O请求时，线程会进入阻塞状态。当sleep()状态超时，join()等待线程终止或者超时，或者I/O处理完毕时，线程重新进入就绪状态
死亡状态 Dead 线程执行完毕或因异常退出时，该线程结束生命周期
创建线程的方式
1.继承Thread类，重写run方法，再以start()启动
run()方法的方法体代表了线程需要完成的任务，称为线程执行体。
2.实现Runnable接口，实现run()方法，并创建实例作为Thread的target来创建Thread对象
Thread类本身也是实现Runnable接口的，其实现为
public void run() {
    if (target != null) {
        target.run();
    }
}
3.使用Callable和Future接口创建线程。创建Callable接口的实现类，实现call()方法。并使用FutureTask类来包装Callable实现类的对象，且一次FutureTask对象作为Thread对象的target来创建线程。
FutureTask(Callable)
FutureTask类实际上是同时实现了Runnable和Future接口，同时可作为Thread的target属性，也可以取得Callable接口call方法的返回值
Java多线程的就绪、运行和死亡状态
就绪状态转换为运行状态：线程获得CPU资源
运行状态转换为就绪状态：线程调用yield方法火灾运行过程中失去处理器资源
运行状态转换为死亡状态：执行体执行完毕或发生异常
yield()方法之后CPU调度就绪状态的线程具有随机性，可能次线程调用yield()方法CPU依然调用此线程
Java多线程的阻塞状态和线程控制
1.join()
让一个线程等待另一个线程执行完成之后才继续执行，例如A中调用B的join方法，则A阻塞直至B执行完成
2.sleep()
让当前正在执行的线程暂停指定的时间，并进入阻塞状态。
调用start方法，线程进入就绪状态，等待CPU分配时间，但可能并不是马上，需要必然立即执行，则可以让当前线程sleep
3.后台线程(Daemon Thread)
主要为其他线程（相对称为前台线程）提供服务，或守护线程，如JVM中的垃圾回收线程
生命周期：后台线程的生命周期与前台线程生命周期具有一定关联。当所有前台线程进入死亡状态时，所有后台线程将自动死亡。
前台线程创建的线程默认都是前台线程，后台线程创建的默认都是后台线程，调用setDaemon()需要在start()前。前台线程死亡之后，JVM通知后台线程自然死亡，但是存在一定的响应时间
4.改变线程的优先级/setPriority()
每个线程执行时具有一定的优先级，优先级高的线程具有更多的执行机会。每个线程默认的优先级都与创建他的线程的优先级相同。main线程具有普通优先级
setPriority(int priorityLevel)具有多个级别
常用的有
Thread.MAX_PRIORITY 10
Thread.MIN_PRIORITY 1
Thread.NORMAL_PRIORITY 5
5.线程让步
yield方法，与线程优先级有关，当某个线程调用yield方法从运行状态转为就绪状态，CPU只会选择优先级相同或者更高的线程执行
线程安全问题
由于切换CPU调度资源的时间不确定，所以可能存在判断条件为true时切换执行线程从而导致资源不同步的问题
同步方法
方法定义上添加synchronized关键字修饰，则变为同步方法，可以简单理解为对方法加锁，所对象为对象自身。多线程并发下，执行此方法时，首先要获得同步锁（同时最多只有一个线程能够获得），只有执行完成或释放锁才能被其他线程获取
同步代码块
synchronized  (obj) {
    ...
}
obj为锁对象，选择锁对象至关重要，一般情况下，选择此共享对象作为锁对象。
Lock对象同步锁
class X {
    private final Lock lock = new ReentrantLock();
    public void m() {
        ...
        lock.lock();
        ...
        lock.unlock();
        ...
    }
}
wait()/notify()/notifyAll()线程通信
需要在同步方法 同步代码块 或者 Lock中使用
wait()方法执行后，当前线程立即进入阻塞状态，并释放持有的锁
notify/notifyAll方法执行后，将唤醒此对象上wait的线程，但并没有立即释放锁，而是等待当前代码块执行完成之后，包括执行sleep方法也不释放锁
线程间通信或协作都是基于不同对象锁的，因此需要与共享资源保持一一对应关系
- 并发
内存模型的相关概念
计算机在执行程序时，每条指令都是在CPU中执行的，而执行指令过程中，势必涉及到数据的读取和写入。由于运行过程中的临时数据是存放在主存（物理内存）中，存在CPU执行速度快，读取写入数据慢的问题。因此CPU中存在高速缓存。
程序执行过程中，会将运算需要的数据从主存复制一份到CPU的高速缓存中，那么CPU进行计算时就可以直接从高速缓存读取写入数据，运算结束之后再讲高速缓存中的数据刷新到主存中。
解决缓存不一致问题：
1.通过在总线加LOCK#锁的方式
2.通过缓存一致性协议
缓存一致性协议：核心思想是当CPU写数据时，如果发现操作的变量是共享变量，即在其他CPU缓存中存在变量的副本，会发出信号通知其他CPU将该变量的缓存行置为无效状态，一次当其他CPU需要读取这个变量时，发现缓存中的变量无效则从主存中重新读取。
并发编程中的三个概念
1.原子性 一个操作或多个操作，要么全部执行并且执行的过程中不会被任何因素打断，要么全部不执行
2.可见性 当多个线程访问同一个变量时，一个线程修改则其余线程能够立即看到修改的值
3.有序性 即程序执行的顺序按照代码的先后顺序执行
Java内存模型
规定所有的变量都是存在主存中，每个线程都有自己的工作内存（类似高速缓存）。线程对变量的所有操作都必须在工作内存中进行，而不能直接对主存进行操作，并且每个线程不能访问其他线程的工作内存。
只有简单的读取、赋值（常量）才是原子操作，但是32位平台下，long和double读取和复制需要2个操作来完成，不能保证其原子性，64位平台则无此问题
通过volatile保证可见性
Java内存模型中，允许编译器和处理器堆指令进行重排序，但重排序不会影响单线程的执行，却会影响多线程并发执行的正确性。volatile保证一部分有序性，另外，Java内存模型具备一些先天的有序性，称为happen-before原则。
happen-before原则
1.程序次序规则 一个线程内，按照代码顺序，书写在前面的操作先行发生于书写在后面的操作
2.锁定规则 一个unLock的操作先行发生于同一个锁的lock操作
3.volatile变量规则 对一个写操作先行发生于后面对这个变量的读操作
4.传递规则 如果操作A先行发生于B，B又先行发生于C，则A必然先行发生于C
5.线程启动规则 Thread对象的start()方法先行发生于此线程的每一个动作
6.线程中断规则 对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生
7.线程终结规则 线程中所有的操作都先行发生于线程的终止检测 可以通过Thread.join()方法结束、Thread.isAlive()的返回值手段检测到线程已经终止执行
8.对象终结规则 一个对象的初始化完成先行发生于finalize()方法的开始
volatile深入
一旦变量被volatile修饰则具备两层含义
1.保证不同线程对变量进行操作时的可见性，即一个线程修改了某个变量的值，新值对其余线程是立即可见的
2.禁止进行指令重排序
volatile并不能保证原子性
java.util.concurrent Java 并发工具包
1.阻塞队列 BlockingQueue
表示一个线程安全放入和提取实例的队列
负责生产对象的线程持续生产对象并插入到队列之中，直到容纳的临界点，当到达临界点时，生产线程会发生阻塞。而消费线程则从队列中获取对象，如果队列为空，则消费线程阻塞直至有对象添加到队列之中
方法      抛异常     特定值     阻塞      超时
插入      add(o)      offer(o)    put(o)  offer(o, timeout, timeunit)
移除      remove(o)   poll(o)     take(o) poll(timeout, timeunit)
检查      element(o)  peek(o)
抛异常：如果试图的操作无法立即执行，则抛出异常
特定值：如果试图的操作无法立即执行，返回一个特定的值（常常是true/false)
阻塞：如果试图的操作无法立即执行，该方法调用将会发生阻塞，直到能够执行
超时：如果试图的操作无法立即执行，该方法调用将会发生阻塞，直到能够执行，但等待时间不会超过一个给定值，返回一个特定值以告知该操作是否成功（通常是true/false
无法向BlockingQueue插入null值，会抛出NullPointException
可以访问BlockingQueue中的所有对象，但是效率不高而且不推荐
BlockingQueue的实现
|-ArrayBlockingQueue
|-DelayQueue
|-LinkedBlockingQueue
|-PriorityBlockingQueue
|-SynchronousQueue
1.ArrayBlockingQueue 数组阻塞队列
ArrayBlockingQueue是一个有界的阻塞队列，其内部实现是将对象放到一个数组中。不能存储无限制的元素，只能在初始化的时候设置这个界限，之后不能修改。
ArrayBlockingQueue内部以FIFO（先进先出）的顺序对元素进行存储。
2.DelayQueue 延迟队列
DelayQueue对元素进行持有直到一个特定的延迟到期。假如DelayQueue的元素必须实现Delayed接口
public interface Delayed extends Comparable<Delayed> {
    public long getDelay(TimeUnit timeUnit);  // 需要手动计算
}
DelayQueue会将每个每个元素的getDelay()方法返回的值的时间段之后才会释放该元素。如果返回0或负值，将会被认为过期，在下一次take()时被释放。
3.LinkedBlockingQueue
内部以一个链式结构对元素进行存储，如果需要的话可以设置一个上限，如果没有手动设置则为Integer.MAX_VALUE
也是以FIFO顺序
4.PriorityBlockingQueue 优先级阻塞队列
是一个无界的并发队列，使用了和PriorityQueue一样的排序规则，无法插入null值。所有插入PriorityBlockingQueue的元素必须实现Comparable接口。对于相同级别的(compare() -> 0)的元素并不强制任何特定行为。
5.SynchronousQueue 同步队列
特殊队列，内部同时只能容纳单个元素，如果该队列已有元素，试图插入的线程将会阻塞，直到其他线程将元素取走。如果为空，则试图取走元素的线程阻塞。
BlockingDeque 阻塞双端队列
|-LinkedBlockingDeque
表示一个线程安全放入和取出实例的双端队列
方法      抛异常     特定值     阻塞      超时
插入      addFirst/Last(o)      offerFirst/Last(o)    putFirst/Last(o)  offerFirst/Last(o, timeout, timeunit)
移除      removeFirst/Last(o)   pollFirst/Last(o)     takeFirst/Last(o) pollFirst/Last(timeout, timeunit)
检查      getFirst/Last(o)  peekFirst/Last(o)
ConcurrentMap 并发Map
|-ConcurrentHashMap
|-ConcurrentNavigableMap
1.ConcurrentHashMap
与HashTable类似，但是提供更好的并发性能，不对整个Map上锁。
2.ConcurrentNavigabelMap 并发导航映射
是一个支持并发访问的NavigableMap，还能让子Map具备并发访问的能力。
CountDownLatch 闭锁
允许一个或多个线程等待一系列指定操作的完成
CountDownLatch以给定的数量初始化，countDown()方法没调用一次就减一，通过awati()方法，线程可以阻塞等待直到归零，再继续执行。
CyclicBarrier 栅栏
所有进程等待一个栅栏，直到所有线程到达时，然后继续其他操作
CyclicBarrier(等待await的个数， 到达栅栏执行的Runnable)；
CyclicBarrier可以重复执行，只要await的个数和设置的相同就会触发
Exchange 交换机
表示一种两个线程可以交换对象的汇合点
Exchanger exchanger = new Exchanger();
obj = exchanger.exchange(obj);
两个持有exchange的线程调用了exchange方法之后就会交换传入的对象，返回交换后的对象
Semaphore 信号量
是一个技术信号量，主要有两个方法
1.acquire()
2.release()
计数信号量有一个数量初始化，每调用一次acquire()，一个许可会被调用的线程取走，每调用一次release()，一个许可返回。
ExecutorService 执行器服务
表示一个异步执行机制，能够后台执行任务。类似于线程池。
ExecutorService es = Executors.newFixedThreadPool(10);
es.execute(Runnable);
ExecutorService
|-ThreadPoolExecutor
|-ScheduledThreadPoolExecutor
Executors.newSingleThreadExecutor();
Executors.newFixedThreadPool(数量);
Executors.newScheduledThreadPool(数量);
ExecutorService使用
1.execute(Runnable)
异步执行Runnable
2.submit(Runnable)
异步执行Runnable，返回一个Future对象，用于检查Runnable是否执行完毕
3.submit(Callable)
异步执行Runnable，返回Future对象
4.invokeAny(...)
需要Callable及其子接口的实例对象，但不返回Future对象，返回其中一个Callable的结果，但无法保证是哪一个
5.invokeAll(...)
异步执行所有传入的Callable实例，返回一系列Future对象，表示其执行结果
ExecutorService 关闭
shutdown() 之后不能再接受新的Runnable，但是会等待已经提交的Runnable
shutdownNow() 立即结束
ThreadPoolExecutor 线程执行者
使用内部池中的线程执行给定任务
线程池可以包含不同数量的线程，有一下变量决定
1.corePoolSize
2.maximumPoolSize
当一个任务委托给线程池时，如果池中线程数量低于 corePoolSize，一个新的线程将被创建，即使池中可能尚有空闲线程。如果内部任务队列已满，而且有至少 corePoolSize 正在运行，但是运行线程的数量低于 maximumPoolSize，一个新的线程将被创建去执行该任务
ScheduleExecutorService 定时执行者服务
能够将任务延后执行，或者捡个固定时间多次执行。任务由一个工作线程异步执行。
使用Executors.newScheduledThreadPool()获取
使用
1.schedule(Callable task, long delay, TimeUnit timeUnit)
给定时间延迟后执行一个Callable任务，返回一个ScheduledFuture对象
2.schedule(Runnable task, long delay, TimeUnit timeUnit)
给定时间延迟后执行一个Runnable任务,返回一个ScheduledFuture对象 get()返回null
3scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit timeUnit)
这一方法规划一个任务将被定期执行。该任务将会在首个 initialDelay 之后得到执行，然后每个 period 时间之后重复执行。如果给定任务的执行抛出了异常，该任务将不再执行。如果没有任何异常的话，这个任务将会持续循环执行到 ScheduledExecutorService 被关闭。如果一个任务占用了比计划的时间间隔更长的时候，下一次执行将在当前执行结束执行才开始。计划任务在同一时间不会有多个线程同时执行。
3scheduleWithFixedDelay(Runnable task, long initialDelay, long period, TimeUnit timeUnit)
除了 period 有不同的解释之外这个方法和 scheduleAtFixedRate() 非常像。
scheduleAtFixedRate() 方法中，period 被解释为前一个执行的开始和下一个执行的开始之间的间隔时间。而在本方法中，period 则被解释为前一个执行的结束和下一个执行的结束之间的间隔。因此这个延迟是执行结束之间的间隔，而不是执行开始之间的间隔。
ScheduledExecutorService 关闭
shutdown()
shutdownNow()
ForlJoinPool 进行分叉和合并
Java 7中引入.

----
暂时没看完
----

Lock 锁
lock.lock()
lock.unLock()

### Spring Boot

SpringBoot希望通过设计大量的自动化配置来简化Spring原有样板化的配置，可以快速构建应用，还通过一系列Starter POMs的定义，在整合各项功能时，无需再pom.xml中维护错综复杂的依赖关系，通过类似模块化的Starter模块定义来引用，使得依赖管理工作更为简单。

- 多环境配置文件
1.使用同一个application.yml
使用---分隔每个环境的配置信息
例如
#默认配置
spring:
    profiles:
        active: dev # 激活开发环境
server:
    port: 8000
---
#开发环境
spring:
    profiles: dev
sever:
    port: 8080
---
#生产环境
spring:
    profiles: prod
server:
    port: 8088
2.使用多个application-{profiles}.yml配置
application.yml
spring:
    profiles:
        active: dev
server:
    port: 8000
application-dev.yml
spring:
    profiles: dev
server:
    port: 8080
application-prod.yml
spring:
    profiles: prod
server:
    port: 8088
自定义参数
${} #{}
随机参数
random.int 随机int型变量
random.long 随机long型变量
random.value 随机字符串
random.int(max)
random.int[min, max]
命令行参数
--server.port=8888
Spring Boot加载配置顺序
1.在命令行中传入的参数
2.SPRING_APPLICATION_JSON中的属性,以JSON格式配置在系统环境变量中的内容
3.java:comp/env中的JNDI属性
4.Java的系统属性，可以通过System.getProperties()获得的内容
5.操作系统的环境变量
6.通过random.*配置的随机属性
7.位于jar包之外，针对不同profile环境配置文件内容
8.位于jar包之内，针对不同profile环境配置文件内容
9.位于jar包之外，application.properties/yml
10.位于jar包之内，application.properties/yml
11.@Configuration注解修改的类中，通过@PropertySource注解定义的属性
12.应用默认属性，使用SpringApplication.setDefaultProperties定义的内容
- actuator
引入starter-actuator依赖
/actuator/*
原生端点
1.应用配置类 获取应用程序中加载的应用配置、环境变量、自动化配置报告等
2.度量指标类 获取应用程序运行过程中用于监控的度量指标，比如内存信息、线程池信息、HTTP请求统计等
3.操作控制类 提供了对应用的关闭等操作类功能



### Spring Cloud
- 特征
Spring Cloud专注于提供良好的开箱即用经验的典型用例和可扩展性机制覆盖
1.分布式/版本划配置
2.服务注册和发现
3.路由
4.service-to-service调用
5.负载均衡
6.断路器
7.全局锁
8.Leadership选举与集群状态
9.分布式消息传递
-Spring Cloud上下文:应用程序上下文服务
引导应用程序上下文
SpringCloud引用程序通过创建一个“引导”上下文来运行，上下文是主应用程序的父上下文。开箱即用，负责从外部源加载配置属性，还解密本地外部配置文件中的属性。这两个上下文共享一个Environment，是任何Spring应用程序的外部属性的来源。Bootstrap属性优先级高，默认情况下不会被本地配置覆盖。
引导上下文使用与主应用程序上下文定位外部配置的不同约定，使用bootstrap.yml而不是application.yml，
bootstrap.yml
spring:
    application:
        name: foo
    cloud:
        config:
            uri: ${SPRING_CONFIG_URI:http://localhost:8888}
如果应用程序需要从服务器进行特定于应用程序的配置，那么设置spring.application.name是个好主意
可以通过设置spring.cloud.bootstrap.enabled=false来禁止引导过程

- 服务治理：Spring Cloud Eureka
是Spring Cloud Netflix微服务套件中的一部分，基于Netflix Eureka做了二次封装，主要负责完成微服务服务架构中的服务治理功能。
1.构建服务注册中心
2.服务注册与服务发现
3.Eureka的基础架构
4.Eureka的服务治理机制
5.Eureka的配置
服务治理
是微服务架构中最为核心和基础的模块，主要用来实现各个微服务实例的自动化注册与发现。
服务注册：在服务治理框架中，通过都会构建一个注册中心，每个服务单元向注册中心登记自己提供的服务，将主机与端口号、版本号、通信协议等附加信息告知注册中心，注册中心按服务名分类组织服务清单。另外，服务中心还需要以心跳的方式去检测清单中的服务是否可用，若不可用则需要从服务清单中剔除，达到排除故障服务的效果。
服务发现：由于在服务治理框架下运行，服务间的调用不再通过指定具体的实例地址来实现，而是通过向服务名发起请求调用实现。调用方需要向服务注册中心咨询服务，并获取所有服务的实例清单，以实现对具体服务实例的访问。
Netflix Eureka
用于实现服务注册与发现，既包含服务端组件也包含客户端组件，均采用Java编写。
Eureka服务端，也成为服务注册中心。支持高可用配置，依托于强一致性提供良好的服务实例可用性，可应对多种不同的故障场景。如果以集群模式部署，当分片出现故障时，转入自我保护模式，允许其他分片故障期间继续提供服务的发现与注册，当故障分片恢复时，将同步状态。推荐每个可用的区域运行Eureka服务端，形成集群，不同可用区域的服务注册中心通过异步模式相互复制各自状态，在给定时间每个实例关于所有服务的状态有细微差别
Eureka客户端，主要处理服务的注册与发现。客户端服务通过注解和参数配置的方式，嵌入在客户端应用程序的代码中，应用程序运行时，Eureka客户端向注册中心注册自身提供的服务并周期性地发送心跳来更新服务租约。也能从服务端查询当前注册服务信息并把它们缓存到本地big周期性地刷新服务状态。
搭建服务中心
引入
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <actifactId>spring-cloud-starter-eureka-server</actifactId>
</dependency>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <actifactId>spring-cloud-dependencies</actifactId>
            <version>Brixton.RELEASE</version>
            <!--<version>Brixton.RELEASE</version>--> <!-- 兼容Spring1.3,1.4 -->
            <!--<version>Camden.RELEASE</version>--> <!-- 兼容Spring1.4,1.5 -->
            <!--<version>Dalston.RELEASE</version>--> <!-- 兼容Spring1.5 -->
            <!--<version>Edgware.RELEASE</version>--> <!-- 兼容Spring1.5 -->
            <!--<version>Finchley.RELEASE</version>--> <!-- 兼容Spring2.0 -->
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
然后只需要在启动类上添加@EnableEurekaServer即可启动一个服务注册中心提供给其他应用进行对话。
默认设置下，该服务中心会将自己作为客户端来尝试注册自己，需要禁用客户端注册行为
在application.properties中
server.port=8001
eureka.instance.hostname=localhost
eureka.client.register-with-eureka=false # 不向注册中心注册自己
eureka.client.fetch-registry=false # 不需要检索服务
eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
注册服务提供者
引入相同的依赖，并增加web依赖
在主类上添加@EnableDiscoveryClient注解，激活Eureka中的DiscoveryClient实现（自动化配置）
再修改application.properties文件
spring.application.name=XXX # 在服务注册中心注册的名称
eureka.client.serviceUrl.defaultZone=XXX # 服务注册中心对应的地址
高可用注册中心
Eureka服务治理中，所有结点即是服务提供方、也是服务消费方。
EurekaServer的高可用实际上是将自己作为服务向其他服务注册中心注册自己。
简单集群
第一个服务注册中心
spring.application.name=eureka-server
server.port=8001
eureka.instance.name=peer1
eureka.client.serviceUrl.defaultZone=http://peer2:8002/eureka/
第二个服务注册中心
spring.application.name=eureka-server
server.port=8002
eureka.instance.name=peer2
eureka.client.serviceUrl.defaultZone=http://peer1:8001/eureka/
服务提供者
spring.application.name=hello-provider
server.port=8080         
eureka.client.serviceUrl.defaultZone=http://peer1:8001/eureka/,http://peer2:8002/eureka/
eureka.instance.prefer-ip-address=true # 默认是false，不使用IP形式定义注册中心地址
服务发现与消费
服务发现由eureka完成，服务消费由ribbon完成。Ribbon是一个基于HTTP和TCP的客户端负载均衡器。可以通过客户端中配置的ribbonServerList服务端列表去轮询访问以达到负载均衡的作用。当Ribbon和Eureka配合使用时，Ribbon的服务实例清单RibbonServerList会被DiscoveryEnabledNIWSServerList重写，扩展从Eureka注册中心中获取服务端列表。同时会用NIWSDiscoveryPing取代IPing。
引入
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-ribbon</artifactId>
</dependency>
在主类上添加@EnableDiscoveryClient启动服务发现
再配置一个RestTemplate的Bean
@Bean
@LoadBalanced
RestTemplate restTemplate() {
    return new RestTemplate();
}
再在需要的地方使用
@Autowired
private RestTemplate restTemplate;
restTemplate.getForEntity("http://服务名/服务目录", 返回的类型).getBody();
Eureka详解
构建微服务中三个核心角色：服务注册中心、服务提供者和服务消费者
基础架构
*服务注册中心
*服务提供者
*服务消费者
服务治理机制
服务提供者
*服务注册
服务提供者在启动时会发送REST请求的方式注册到服务注册中心上，同时带上自身服务的一些元数据信息。服务注册中心接收到REST请求后，将元数据信息存储在一个双层结构Map中，第一层的key是服务名，第二层key是具体服务的实例名。
eureka.client.register-with-eureka=true # 注册到EurekaServer服务器上
*服务同步
两个服务分别注册到不同的服务中心上 信息分别被不同中心维护。但是因为注册中心相互注册为服务 当服务提供者发送注册请求到一个服务注册中心时 会将该请求转发给集群中的其他服务中心 从而实现服务同步
*服务续约
注册完服务后 提供者会维护一个心跳用来告诉EurekaServer存活状态 防止被剔除任务将服务实例从服务列表中排除出去 
两个重要属性
eureka.instance.lease-renewal-interval-inseconds=30 # 用于定义服务续约任务的调用时间间隔
eureka.instance.lease-expiration-duration-in-seconds=90 # 用于定义服务失效的时间
服务消费者
启动服务消费者时 会发送一个REST请求给服务注册中心 获取上面注册的服务清单 为了性能考虑 EurekaServer会维护一个只读的清单来返回给客户端 同时该缓存清单会每隔30秒更新一次
eureka.client.fetch-registry=true # 从EurekaServer上获取服务清单
eureka.client.registry-fetch-interval-seconds=30 # 修改缓存清单的更新时间
*服务调用
获取服务清单后 通过服务名可以获得具体提供服务的实例名和该实例的元数据信息 可以根据自己需要决定具体调用那个实例 Ribbon中会默认采用轮询的方式进行调用
对于访问实例的选择 Eureka中又Region和Zone的概念 一个Region中可以包含多个Zone 每个服务客户端需要被注册到Zone中 优先选择同一Zone中的服务提供方
服务注册中心
*失效剔除
服务中心未收到服务下线的通知 为了从列表中剔除无法提供服务的实例 EurekaServer启动会创建一个定时任务 默认每隔60秒将当前清单中超时默认90秒没有续约的服务剔除
*自我保护
红色警告信息 会统计心跳失败的比例在15分钟是否低于85% 是则将当前的实例注册信息保护起来 让实例不会过期 保护期间实例出现问题 则客户端很容易拿到不存在的服务实例 会出现调用失败的问题 所有必须有容错机制 
eureka.server.enable-self-preservation=false # 关闭自我保护机制
- 源码详解
@EnableDiscoveryClient
真正发现服务的是com.netflix.discovery.DiscoveryClient
这个类用于帮助与Eureka Server相互协作
Eureka Client负责以下任务
1.向Eureka Server注册服务实例
2.向Eureka Server服务租约
3.当服务关闭时，向Eureka Server取消租约
4.查询Eureka Server中的服务实例列表
Eureka Client还需要配置一个Eureka Server的URL列表
一个微服务应用程序属于一个region，一个region里可以有多个zone
再加载serviceUrls getEurekaServiceUrls(String myZone)函数
使用Ribbon来实现服务调用时，对于Zone的设置可以在负载均衡时实现区域亲和特性，优先访问处于同一个Zone的服务端实例。
服务注册
在DiscoveryClient中的initScheduledTasks() -> 创建一个InstanceInfoReplicator类的实例，执行一个定时任务 -> 执行discoveryClient.register()方法 -> 调用REST请求，传入客户端元数据的InstanceInfo对象
服务获取与服务续约
都在if(this.clientConfig.shouldRegisterWithEureka())判断下
服务获取则独立在if()判断中this.clientConfig.shouldFetchRegistry(),也就是配置中的eureka.client.fetch-registry=true
配置详解
服务治理体系中，分为服务端和客户端。服务端为服务注册中心，而客户端为各个提供接口的微服务应用。在构建了高可用注册中心之后，集群中的所微服务应用和基础类应用（如配置中心、API网关等）都可视为体系下的微服务（Eureka客户端）。其实注册中心也一样，只是高可用环境下的服务注册中心除了作为客户端之外，还为集群中的其他客户端提供服务注册的特殊功能。在实际使用中，几乎都是对客户端配置进行操作。
Eureka客户端配置主要分为两方面：
1.服务注册相关配置信息，包括服务注册中心地址、服务获取的间隔时间、可用区域等
2.服务实例相关配置信息，包括服务实例的名称、IP地址、端口号、健康检查路径等
Eureka服务端更多类似于一个现成产品，大多数情况下，无需修改配置。以eureka.server开头
1.服务注册类配置
可以查看EurekaClientConfigBean源码获得更为详尽的内容，基本以eureka.client为前缀
A.指定注册中心
eureka.client.service-url(serviceUrl) 这是一个HashMap对象，设有默认值key为defaultZone，value为http://localhost:8761/eureka/，修改为实际的服务注册中心地址，高可用时将多个地址以逗号隔开，若有安全验证的格式为http://<username>:<password>@<host>:<port>/<path>/
B.其他配置(以eureka.client为前缀)
参数名                                         说明                                  默认值
enabled                                        启动客户端                            true
registryFetchIntervalSeconds                   从服务端获取注册信息间隔时间          30
instanceInfoReplicationIntervalSeconds         更新实例信息的变化到服务端的间隔时间  30
initialInstanceInfoReplicationIntervalSeconds  初始化实例信息到服务端的间隔时间      40
eurekaServiceUrlPollIntervalSeconds            轮询服务端地址更改的间隔时间          300
eurekaServerReadTimeoutSeconds                 读取Eureka Server信息的超时时间       8
eurekaServerConnectTimeoutSeconds              连接Eureka Server的超时时间           5
eurekaServerTotalConnections                   从客户端到所有服务端的连接总数        200
eurekaServerTotalConnectionsPerHost            从客户端到每个服务端的连接总数        50
eurekaConnectionIdleTimeoutSeconds             服务端连接的空闲关闭时间              30
heartbeatExecutorThreadPoolSize                心跳连接池的初始化线程数              2
heartbeatExecutorExponentialBackOffBound       心跳超时重试延迟时间的最大乘数值      10
cacheRefreshExecutorThreadPoolSize             缓存刷新线程池的初始化线程            2
cacheRefreshExecutorExponentialBackOffBound    缓存刷新超时重试延迟时间的最大乘数值  10
useDnsForFetchingServiceUrls                   使用DNS来获取Eureka服务端的ServiceUrl false
registerWithEureka                             是否将自身的实例信息注册到服务端      true
preferSameZoneEureka                           是否偏好使用处于相同Zone的服务端      true
filterOnlyUpInstances                          获取实例时是否过滤 仅保留UP状态的实例 true
fetchRegistry                                  是否从服务端获取注册信息              true
服务实例类配置(EurekaInstanceConfigBean 以eureka.instance为前缀)
1.元数据 客户端在向服务注册中心发送注册请求时，描述自身服务信息的对象。包含一些标准化元数据，比如服务名称、实例名称、实例IP、实例端口等，以及一些用于负载均衡策略或是其他特殊用途的自定义元数据信息。在使用时，所有配置信息都通过EurekaInstanceConfigBean加载，服务注册时，包装成InstanceInfo对象发送给服务端。
2.实例名配置 默认的配置规则为${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id:${server_port}}
instance-id(instanceId)=XXX
3.端点配置 InstanceInfo中有一些URL配置信息，比如homePageUrl、statusPageUrl、healthCheckUrl，Eureka默认使用了actuator模块提供的/info和/health端点进行状态和健康检查，大多数情况无需修改。特殊情况
A.
management.context-path=/hello
eureka.instance.status-page-url-path=${management.context-path}/info
eureka.instance.health-check-url-path=${management.context-path}/health
B.
endpoints.info.path=/appInfo
endpoints.health.path=/checkHealth
eureka.instance.status-page-url-path=${endpoints.info.path}
eureka.instance.health-check-url-path=${endpoints.health.path}
也可以使用绝对路径地址配置，默认使用HTTP访问
4.健康检查 默认情况不是使用actuator的/health端点来实现的，而是使用客户端心跳来保持服务实例的存活，通过简单配置可以使用/health端点进行更全面的健康状态检查
A.引入actuator模块的依赖
B.增eureka.client.healthcheck.enabled=true
C.如果/health端点路径做了特殊处理，修改配置
5.其他配置（以eureka.instance为前缀)
preferIpAddress 是否优先使用IP地址作为主机名的标识 false
leaseRenewalIntervalInSeconds 客户端向服务端发送心跳的间隔时间 30
leaseExpirationDurationInSeconds 服务端在最后一次收到心跳之后等待的时间上限 90
nonSecurePort 非安全的通信端口号 80
securePort 安全的通信端口号 443
nonSecurePortEnabled 是否启用非安全的通信端口号 true
securePortEnabled 是否启用安全的通信端口号
appname 服务名 默认使用spring.application.name的配置值，没有则为unknown
hostname 主机名，不配置将根据操作系统的主机名获取
- 客户端负载均衡 Spring Cloud Ribbon
是基于HTTP和TCP的客户端负载均衡工具，基于Netflix Ribbon的实现。
客户端负载均衡
客户端负载均衡中，所有客户端结点都维护自己要访问的服务端清单，来自于注册中心。客户端负载均衡需要心跳去维护服务端清单的健康性，需要与服务注册中心配合完成。SpringCloud会默认创建针对各个服务治理框架的Ribbon自动化整合配置。如RibbonEurekaAutoConfiguration,RibbonConsulAutoConfiguration。
通过Spring Cloud Ribbon的封装，使用客户端负载均衡只需要2步：
1.服务提供者只需要启动多个服务实例并注册到一个注册中心或是多个相关联的服务注册中心中
2.服务消费者通过调用会被@LoadBalanced注解修饰过的RestTemplate来实现面向服务的接口调用
RestTemplate详解
1.GET请求
通过以下两种方法调用实现
A.getForEntity函数，返回的是ResponseEntity，该对象是Spring对HTTP请求的封装，主要存储了HTTP的几个重要元素，比如HTTP请求状态码的枚举对象HttpStatus，父类HttpEneity中还存储着HTTP请求的头信息对象HttpHeaders以及泛型类型的请求体对象。
RestTempalte restTemplate = new RestTemplate();
ResponseEntity<String> responseEntity = restTemplate.getFoeEntity("http://USER_SERVICE/user?name={1}", String.class, "DD"); // DD替换url中的{1}占位符，结果的body转换为String对象
String body = responseEntity.getBody(); // 根据指定的类型来获取转换后的泛型对象实例
getForEntity有三种重载
getForEntity(String url, Class responseType, Object... urlVariables);
getForEntity(String url, Class responseType, Map urlVariables);
getForEntity(Url url, Class responseType); // Url需要自行转换
B.getForObject函数，对getForEntity的进一步封装，通过HttpMessageConverterExtractor对HTTP的请求响应体body内容进行对象转换，实现请求直接返回包装好的对象内容。
也有三种重载
getForObject(String url, Class responseType, Object... urlVariables);
getForObject(String url, Class responseType, Map urlVariables);
getForObject(Url url, Class responseType);
2.POST请求
三种方法调用
A.postForEntity 与GET的getForEntity类似,参数多一个请求体内容
三种重载
postForEntity(String url, Object request, Class responseType, Object... variables);
postForEntity(String url, Object request, Class responseType, Map variables);
postForEntity(Url url, Object request, Class responseType);
request可以是一个普通的对象，也可以是HttpEntity对象
B.postForObject
也有3中重载
postForObject(String url, Object request, Class responseType, Object... variables);
postForObject(String url, Object request, Class responseType, Map variables);
postForObject(Url url, Object request, Class responseType);
C.postForLocation函数，以POST请求提交资源，并返回新资源的URI
三种重载
postForLocation(String url, Object request, Class responseType, Object... variables);
postForLocation(String url, Object request, Class responseType, Map variables);
postForLocation(Url url, Object request, Class responseType);
3.PUT请求
通过put方法调用
put(String url, Object request, Class responseType, Object... variables);
put(String url, Object request, Class responseType, Map variables);
put(URI uri, Object request, Class responseType);
4.DELETE请求
通过delete方法调用
delete(String url, Object... urlVariables);
delete(String url, Map urlVariables);
delete(URI url)
源码分析
@LoadBalanced用于给RestTemplate做标记，以使用负载均衡的客户端（LoadBalancerClient)来配置它
public interface LoadBalancerClient {
    ServiceInstance choose(String serviceId); // 根据传入的服务名从负载均衡器中选择一个对应服务的实例

    <T> T execute(String serviceId, LoadBalancerRequest<T> request) throws IOException; // 使用从负载均衡器中挑选出的服务实例来执行请求内容

    URI reconstructURI(ServiceInstance instance, URI original); // 为系统构建一个合适的host:port形式的URI，分布式系统中，使用逻辑上的服务名称作为host来构建URI（替代服务实例的host:port形式）进行请求。ServiceInstance以host:port的具体服务实例，URI为以逻辑服务名为host的，需要以URI构建出ServiceInstance

}
LoadBalancerAutoConfiguration为实现客户端负载均衡器的自动化配置类
系统需要有RestTemplate类，需要有LoadBalancerClient的实现Bean
主要做三件事
A.创建一个LoadBalancerInterceptor的Bean，用于实现对客户端发起请求时进行拦截，以实现客户端负载均衡
B.创建一个RestTemplateCustomizer的Bean，用于给RestTemplate增加LoadBalancerInterceptor拦截器
C.维护一个被@LoadBalanced注解修饰的RestTemplate对象列表，并进行初始化，通过调用RestTemplateCustomizer给RestTemplate增加LoadBalancerInterceptor拦截器。
... 没看完
负载均衡器
ILoadBalancer接口的实现
1.AbstractLoadBalancer
抽象实现，定义了关于服务实例的分组枚举类ServerGroup，包含三种类型：ALL-所有、STATUS_UP-正常服务、STATUS_NOT_UP-停止服务
还实现了chooseServer()函数，调用接口的chooseServer(Object key)实现，key为null则表示选择时忽略key的条件判断
还定义2个抽象函数
getServerList(ServerGroup serverGroup) 获取不同类型服务实例的列表
getLoadBalancerStats() 获取LoadBalancerStats 用于存储负载均衡器中各个服务实例当前属性和统计信息。
2.BaseLoadBalancer
基础实现类，定义很多关于负载均衡器相关的基础内容
A.定义并维护两个存储服务实例Server对象的列表，一个所有一个正常服务
B.定义LoadBalancerStats对象
C.定义检查服务实例是否正常的IPing对象
D.定义检查服务实例操作的执行策略对象IPingStrategy
E.定义负载均衡处理规则IRule
F.启动ping任务
3.DynamicServerListLoadBalancer
继承自BaseLoadBalancer,实现了服务实例清单在运行期动态更新能力，同时还具备对服务实例清单的过滤功能
4.ZoneAwareLoadBalancer
对DynamicServerListLoadBalancer扩展
负载均衡策略
IRule接口
1.AbstractLoadBalancerRule
抽象类，定义了负载均衡器ILoadBalancer对象
2.RandomRule
随机选择一个服务实例
3.RoundRibbonRule
按照线性轮询的方式一次选择每个服务实例的功能
4.RetryRule
具备重试机制的实例选择功能
5.WeightedResponseTimeRule
根据实例的运行情况计算权重，把那个根据权重挑选实例。
6.ClientConfigEnabledRoundRobbinRule
一般不使用，本身没有特殊的处理逻辑。
7.BestAvailableRule
注入复杂均衡器的统计对象LoadBalancerStats,同时利用它保存的实例统计信息来选择满足要求的实例
8.PredicateBasedRule
基于Predicate实现的策略
9.ZoneAvoidanceRule
是PredicateBaseRule的具体实现类,选择Zone区域策略
配置详解
自动化配置
引入Spring Cloud Ribbon依赖后，能够自动构建下面这些接口的实现
1.IClientConfig Ribbon的客户端配置 默认使用DefaultClientConfigImpl
2.IRule Ribbon的负载均衡器 默认采用ZoneAvoidanceRule
3.IPing Ribbon的实例检查策略 默认采用NoOpPing实现，特殊的实现，实际上不检查，而是永远返回true，默认所有服务都是可用的
4.ServerList<Server> 服务实例清单的维护机制 默认ConfigurationBasedServerList
5.ServerListFilter<Server> 服务实例清单过滤机制 默认ZonePreferenceServerListFilter 能够优先过滤出与请求方处于同区域的服务实例
6.ILoadBalancer 负责均衡器 默认ZoneAwareLoadBalancer 具备区域感知能力
基于自动化配置，可以轻松实现客户端负责均衡，针对个性化需求时，可以方便的替换默认实现，只需创建对应的实现实例即可覆盖，
比如创建PingUrl实例的Bean，即可替换NoOpPing
@Configuration
public class CustomRibbonConfiguration {
    @Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl();
    }
}
也可以使用@RibbonClient注解实现更细粒度的客户端配置，比如
@Configuration
@RibbonClient(name = "hello-service", configuration=HelloServiceConfiguration.class)
public class RibbonConiguration {

}
Camden版本对RibbonClient配置的优化
可以直接通过<clientName>.ribbon.<key>=<value>形式进行配置 比如
hello-service.ribbon.NFLLoadBalancerPingClassName=com.netflix.loadbalancer.PingUrl
NFLoadBalancerClassName 配置ILoadBalancer接口的实现
NFLoadBalancerPingClassName 配置IPing接口的实现
NFLoadBalancerRuleClassName 配置IRule接口的实现
NIWSServerListClassName 配置ServerList接口的实现
NIWSServerListFilterClassname 配置ServerListFilter接口的实现
参数配置
两种方式：全局配置和指定客户端配置
全局配置 格式ribbon.<key>=<value>
指定客户端配置 <client>.ribbon.<key>=<value>
与Eureka结合
同时引入Ribbon和Eureka的依赖，会触发Eureka中实现对Ribbon的自动化配置，ServerList会被DiscoveryEnabledNIWSServerList的实例所覆盖，服务清单列表将交由Eureka的服务治理机制维护。IPing实例由NIWSDiscoveryPing实例覆盖。
eureka.instance.metadataMap.zone=XX 指定Zone
针对Ribbon的参数配置 依然可以使用以前的全局ribbon.<key>=<value> 或者 <clientName>.ribbon.<key>=<value>的方式进行配置 <clientName>为Eureka的服务名 
ribbon.eureka.enabled=false 禁用Eureka对Ribbon服务实例的维护实现
重试机制
Eureka强调了CAP中AP（可用性，可靠性），而ZooKeeper则强调CP（一致性，可靠性），Eureka宁可接受故障实例也不要丢掉健康实例。
- Spring Cloud Hystrix 服务容错保护
断路器模式：当某个服务单元发生故障，通过断路器监控，想调用方返回一个错误响应，而不是长时间等待。
Hystrix实现了断路器、线程隔离等于系列服务保护功能。目标在于通过控制那些访问远程系统、服务和第三方库的结点，从而对延迟和故障提供强大的容错能力。具备服务降级、服务熔断、线程和信号隔离、请求缓存、请求合并以及服务监控等强大功能。
引入hystrix依赖
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-hystrix</artifactId>
</dependency>
再在启动类上添加@EnableCricuitBreaker注解(也可以使用@SpringCloudApplication替代相当于添加了@EnableCricuitBreaker、@EnableDiscoveryClient、@SpringBootApplication等多个注解)
再改造消费方式，添加Service层，添加错误处理方法，并在请求调用方法上添加@HystrixCommand(fallbackMethod = "方法名")
@Service
public class HelloService {
    @Autowired
    private RestTemplate restTemplate;
    @HystrixCommand(fallbackMethod="helloFallback")
    public String helloService() {
        return restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getbody();
    }
    public String helloFallback() {
        return "error";
    }
}
最后在Controller中使用这个Service即可使用hystrix处理
原理分析
工作流程
1.创建HystrixCommand或HystrixObservableCommand对象
用于表示对依赖服务的操作请求，同时传递所有需要的参数。使用命令模式：将请求封装为一个对象，从而可以使用不同的请求对客户端进行参数化。
HystrixCommand用于依赖的服务返回单个操作结果的时候
HystrixObservableCommand用于依赖的服务返回多个操作结果的时候
2.命令执行
存在4中命令的执行方式
HystrixCommand实现下面两个执行方式
execute() 同步执行，从依赖的服务返回一个单一的结果对象，或是在发生错误时抛出异常
queue() 异步执行，直接返回一个Future对象，其中包含了服务执行结果时要返回的单一结果对象
HystrixObservableCommand实现另两种执行方式
observe() 返回Observable对象，代表操作的多个结果，是一个Hot Observable对象 无论是否有订阅者都会在创建事件之后发布
toObservable() 同样返回Observable对象，代表操作的多个结果，是一个Cold Obsevable对象 没有订阅者则不会发布创建事件，直到有订阅者，保证订阅者可以看到整个操作的全部过程
Hystrix大量使用RxJava
Observable用于向订阅者Subscriber对象发布事件，Subscriber对象则在接受到事件后对其进行处理，而在这里所指的时间通常就是对依赖服务的调用
一个Observable对象可以发出多个事件，直到结束或是发生异常
Observable对象没发出一个事件，就会调用对应观察者Subscriber对象的onNext()方法
每一个Observable的执行，最后一定会通过调用Subscriber.onCompleted()或者Subscriber.onError()来结束该事件的操作流
3.结果是否被缓存
缓存功能启动，并且该命令缓存命中，那么缓存结果立即以Observable对象的形式返回
4.断路器是否打开
没有缓存命中时，Hystrix执行命令前需要检查断路器是否为打开状态
如果断路器打开，则不会执行命令，而是转到fallback处理逻辑（第8步）
如果断路器关闭，则跳刀第5步，检查是否有可用资源来执行命令
5.线程池/请求队列/信号量是否占满
如果占满则不会执行命令跳到第八步
6.HystrixObservableCommand.construct()或HystrixCommand.run()
run() 返回单一结果或是抛出异常
construct() 返回一个Observable对象来发布多个结果，或通过onError()发送错误通知
construct() 返回一个Observable对象来发布多个结果，或通过onError()发送错误通知
如果run()或construct()方法执行时间超过了命令设置的超时阈值，当前处理线程会抛出一个TimeoutException，会跳到fallback处理逻辑，如果当前命令没有被取消或中断，最终会忽略返回
7.计算断路器的健康度
Hystrix会将成功、失败、超时等信息报告给断路器，而断路器会维护一组计数器来统计这些数据
断路器会使用这些统计数据来决定是否打开断路器，来对某个依赖服务的请求进行熔断/短路，直到恢复期结果。若恢复期结束后，还未达到健康指标，就再次熔断/短路
8.fallback处理
命令执行失败时，会进入fallback尝试回退处理，通常也称为服务降级。
HystrixCommand.getFallback()
HystrixObservableCommand.resumeWithFallback() 返回Observable对象发布一个或多个降级结果
如果没有为命令实现降级逻辑或降级处理逻辑中抛出异常，依然会返回一个Observable对象，但不会发射任何结果数据，而是通过onError方法通知命令立即中断请求，并通过onError方法将引起失败的异常发送给调用者。
9.返回成功的响应
toObservable() 返回最原始的Observable，必须通过订阅才会真正触发命令的执行流程
observe() 在toObservable()产生原始Observable之后立即订阅它，让命令能够马上开始异步执行，并返回一个Observable对象，当调用subscribe时，将重新产生结果和通知给订阅者
queue() 将toObservable()产生的原始Observable通过toBlocking()方法转换成BlockingObservable对象，并调用toFuture()方法返回异步的Future对象
execute() 在queue()产生异步结果Future对象之后，调用get()方法阻塞并等待结果的返回
断路器原理
HystrixCircuitBreaker接口
三个抽象方法
allowRequest() 每个Hystrix命令的其你去都通过它判断是否被执行
isOpen() 当前断路器是否打开
markSuccess() 用来闭合断路器
三个静态类
Factory 维护Hystrix命令与断路器的关系集合:ConcurrentHashMap<String, HystrixCircuitBreaker> circuitBreakersByCommand,key是HystrixCommandKey，value是HystrixCircuitBreaker实例
NoOpCircuitBreaker 无操作断路器，允许所有请求，并且断路器状态始终闭合
HystrixCircuitBreakerImpl 实现类 定义4个核心对象
HystrixCommandProperties 属性对象
HystrixCommandMetrics 记录各类度量指标的对象
AtomicBoolean 断路器是否打开的标志 默认false
AtomicLong 断路器打开或是上一次测试的时间戳
依赖隔离
舱壁模式，为每一个依赖服务创建一个独立的线程池。
使用详解
创建请求命令
Hystrix命令就是HystrixCommand，用来封装具体的依赖服务调用逻辑
可以通过继承方式实现
public class UserCommand extends HystrixCommand<User> {
    private RestTemplate restTemplate;
    private Long id;
    public UserCommand(Setter setter, RestTemplate restTemplate, Long id) {
        super(setter);
        this.restTemplate = restTemplate;
        this.id = id;
    }
    public User run() {
        return restTemplate.getForEntity("url", User.class).getBody();
    }
}
同步执行 User u = new UserCommand(restTemplate, 1L).execute()
异步执行 Future<User> futureUser = new UserCommand(restTemplate, 1L).queue();
也可以使用@HystrixCommand注解
单纯添加注解是同步执行，要实现异步执行需要另外定义
@HystrixCommand
public Future<User> getUserByIdAsync(final String id) {
    return new AsyncResult<User>() {
        public User invoke() {
            return restTemplate.getForObject(url, User.class, id);
        }
    };
}
或者继承HystrixObservableCommand对象
public class
public class UserObservableCommand extends HystrixObservableCommand<User> {
    private RestTemplate restTemplate;
    private Long id;
    public UserObservableCommand(Setter setter, RestTemplate restTemplate, Long id) {
        super(setter);
        this.restTemplate = restTemplate;
        this.id = id;
    }
    public User construct(Subscriber<User> subscriber) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        User user = restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
                        subscriber.onNext(user);
                        subscriber.onCompleted();
                    } catch (RestClientException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }
        });
    }
}
或者使用@HystrixCommand注解
@HystrixCommand 使用observableExecutionMode = ObservableExecutionMode.EAGER(observable)/LAZY(toObservable)
public Observable<User> getUserById(final Long id) {
    return Observable.create(new Observable.OnSubscribe<User>() {
        @Override
        public void call(Subscriber<? super User> subscriber) {
            if (!subscriber.isUnsubscribed()) {
                try {
                    User user = restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
                    subscriber.onNext(user);
                    subscriber.onCompleted();
                } catch (RestClientException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }
    });
}
使用@HystrixCommand注解实现响应式命令时 可以通过observableExecutionMode参数来控制是使用observe()还是toObservable()的执行方式
observableExecutionMode=ObservableExecutionMode.EAGER # observe()方法 Hot Observable 直接获取 订阅者不是从头开始获取
observableExecutionMode=ObservableExecutionMode.LAZY # toObservable() Cold Observable 每次从头开始获取 订阅者获取完整
定义服务降级
fallback是命令执行失败时的后备方法，实现服务降级处理逻辑。
通过重写getFallback()/resumeWithFallback()来实现
在@HystrixCommand的fallbackMethod="fallback方法名" 可以嵌套使用
异常处理
异常传播
run()方法抛出异常，除了HystrixBadRequestException之外，其他异常都会认为执行失败并触发降级的处理逻辑
可以添加ignoreExceptions={XXX}来忽略指定异常类型
异常获取
继承中可以通过getExceptionException()方法来获取具体的异常，判断进入不同处理逻辑
通过注解方式，则添加一个Throwable入参来获取具体的异常
命令名称、分组已经线程池划分
继承方式实现使用类名作为默认的命令名称，也可以在构造函数中通过setter静态类来设置 比如
super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GroupName"))
    .andCommandKey(HystrixCommandKey.Factory.asKey("CommandName"))
    .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ThreadPoolKey")))
@HystrixCommand则使用groupKey、commandKey、threadPoolKey来指定
请求缓存
只需要在继承HystrixCommand或HystrixObservableCommand时重写getCacheKey()方法开启请求缓存
通过判断getCacheKey是否相同，若相同则第一次时真实请求一次服务，之后直接返回缓存中的内容。
清理失效缓存功能
使用HystrixRequestCache.clear()方法进行缓存清理
HystrixRequestCache.getInstance(HystrixCommandKey.Factory.asKey("CommandKey"), HystrixConcurrentStrategyDefault.getInstance()).clear(String.valueOf(id));
使用默认的并发策略清除队友CommandKey下指定id的缓存
需要配置一个HystrixRequestContextServletFilter的过滤器
public class HystrixRequestContextServletFilter implements Filter {
	protected void doFilter(request, response, filterChain) {
		HystrixRequestContext.initializeContext();
		filterChain(request, response);
	}
}
工作原理
尝试获取请求缓存
Hystrix命令在执行前会根据isRequestCachingEnabled方法判断是否开启缓存。如果开启并重写了getCacheKey方法，并返回一个非null的key值，就根据key调用HystrixRequestCache中的get(String cacheKey)来获取缓存的HystrixCachedObservable对象
将请求结果假如缓存
已经获取一个延迟执行的命令结果对象HystrixObservable对象，则根据isRequestCachingEnabled方法判断是否开启缓存。如果开启并重写了getCacheKey方法，并返回一个非null的key值，就包装成一个HystrixCachedObservable对象假如缓存中。
使用注解实现请求缓存
@CacheResult 标记请求命令返回的结果应该被缓存 必须与@HystrixCommand注解结合使用  cacheKeyMethod
@CacheRemove 让请求命令的缓存失效，失效的缓存根据定义的key决定 commandKey, cacheKeyMethod
@CacheKey 在请求命令的参数上标记，使其作为缓存的key值，同时使用@CacheResult和@CacheRemove，该标记不起作用
请求合并
HystrixCollapser实现请求合并，减少通信小号和线程数占用
继承HystrixCollapser<BatchReturnType, ResponseType, RequestArgumentType>类
public class UserCollapseCommand extends HystrixCollapser<List<User>, User, Long> {

    private UserService userService;

    private Long userId;

    // 收集100毫秒中的用户查询请求
    public UserCollapseCommand(UserService userService, Long userId) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("userCollapseCommand")).andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
        this.userService = userService;
        this.userId = userId;
    }

    // 单次请求的参数
    @Override
    public Long getRequestArgument() {
        return userId;
    }

    // 根据100毫秒中请求参数生成的集合查询队友的用户集合
    @Override
    protected HystrixCommand<List<User>> createCommand(Collection<CollapsedRequest<User, Long>> collection) {
        List<Long> userIds = new ArrayList<>(collection.size());
        userIds.addAll(collection.stream().map(CollapsedRequest::getArgument).collect(Collectors.toList()));
        return new UserBatchCommand(userService, userIds);
    }

    // 根据查询到的用户集合分发到每个请求的结果中
    @Override
    protected void mapResponseToRequests(List<User> users, Collection<CollapsedRequest<User, Long>> collection) {
        int count = 0;
        for (CollapsedRequest<User, Long> collapsedRequest : collection) {
            User user = users.get(count++);
            collapsedRequest.setResponse(user);
        }
    }
}
通过注解方式
@HystrixCollapser(batchMethod = "findAll", collapserProperties = {
        @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
}) // 单个请求上添加,合并请求
public User find(Long id) {
    return null;
}

public List<User> findAll(List<Long> ids) {
    return restTemplate.getForObject("http://USER-SERVICE/users?id={1}", List.class, org.apache.commons.lang.StringUtils.join(ids, ","));
}
属性详解
分为两类，
1.通过继承实现时，使用Setter对象对请求命令的属性进行设置
2.@HystrixCommand时使用commandProperties={@HystrixProperty(name="",value="")}
全局默认值 最低
全局配置属性 配置文件中定义的全局属性值
实例默认值 通过代码定义的默认值
实例配置属性 通过配置文件为指定实例进行属性配置

####
属性详解暂时没看 看了也忘
####

- 声明式服务调用 Spring Cloud Feign
引入依赖
在启动类上添加@EnableFeignClients
然后创建接口类
@FeignClient("hi-service")
public interface HiService {
    @RequestMapping("/hi")
    public String hi();
}
参数绑定 和mvc类似
@RequestParam
@RequestHeader
@RequestBody
继承特性
几乎完全可以从服务提供方的Controller中依靠复制操作 构建出相应的服务客户端绑定接口 因此还可以进一步抽象
Ribbon配置
Feign的客户端负载均衡是通过Ribbon实现的。
全局配置 格式ribbon.<key>=<value> 比如
ribbon.ConnectTimeout=500
ribbon.ReadTimeout=5000
指定服务配置 <client>.ribbon.<key>=<value>
重试机制
<client>.ribbon.MaxAutoRetries=1 尝试访问1次，失败后更换实例访问
<client>.ribbon.MaxAutoRetriesNextServer=2 尝试更换两次实例进行重试
Hystrix配置
全局配置 hystrix.command.default.<**>=<value> 比如
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=5000
禁用Hystrix
feign.hystrix.enabled=false 关闭全局hystrix功能
hystrix.command.default.execution.timeout.enabled=false 关闭熔断功能
针对某个服务客户端关闭Hystrix支持 需要使用@Scope("prototype")注解为指定的客户端配置Feign.Builder实例
@Configuration
public class DisableHystrixConfiguration {
    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}
在使用@FeignClient(name="", configuration=DisableHystrixConfiguration.class)
指定命令配置
hystrix.command.<commandKey>.<**>=<value>
也可以重写Feign.Builder实现
服务降级配置
无法直接通过@HystrixCommand来实现服务降级
需要为Feign客户端的定义接口编写一个具体的接口实现类，其中每个重写方法都可以用于定义相应的服务降级逻辑
在@FeignClient(name="", fallback=HelloServiceFallback.class)指定服务降级的类
其他配置
请求压缩
Feign支持对请求和响应进行GZIP压缩，以减少通信过程中的性能损耗
feign.compression.request.enabled=true
feign.compression.response.enabled=true
也可以做更细致设置(默认)
feign.compression.request.mime-types=text/xml.application/xml,application/json
feign.compression.request.mime-request-size=2048
日志配置
Feign在构建@FeignClient注解修饰的服务客户端时，会为每一个客户端创建一个feign.Logger实例，利用该日志对象的DEBUG模式来帮助分析Feign的请求细结
logging.level.<FeignClient>参数格式来开启指定Feign客户端的DEBUG日志 FeignClient为定义接口的完整路径
默认的Feign.Logger级别是None 需要添加一个Logger.Level的Bean指定级别
@Bean
public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
}
也可以通过实现配置类，为每个FeignClient指定日志级别
@FeignClient(name="", configuration=FeignClientConfiguration.class)
Logger.Level
|-NONE 不记录任何信息
|-BASIC 仅记录请求方法、URL以及响应状态码和执行时间
|-HEADERS 处理BASIC之外，还会记录请求和响应的头信息
|-FULL 记录所有请求与响应的明细，包括头信息、请求体、元数据等
- API网关服务 Spring  Cloud Zuul
将自身注册为Eureka服务治理下的应用 同时从Eureka中获得所有其他微服务的实例信息 对于路由规则的维护 Zuul默认会将通过以服务名作为ContextPath的方式来创建路由映射 大部分情况下满足条件 
快速起步
引入依赖 spring-cloud-starter-zuul （此依赖包括核心依赖zuul-core，还包括需要的重要依赖hystrix ribbon actuator）
在启动类上添加@EnableZuulProxy
再在配置文件中添加
spring.application.name=api-gateway
server.port=5555
请求路由
传统路由方式
在配置文件中添加 (route_name需要成对出现 可以配置多个)
zuul.routes.<route_name>.path=/route_name/**
zuul.routes.<route_name>.url=http://localhost:8080/
面向服务的路由
引入eureka依赖，让路由的path不再映射具体的url，而是映射到某个服务上，具体的url由Eureka服务发现机制自动维护，称为面向服务的路由
zuul.routes.<route_name>.path=/<route_name>/**
zuul.routes.<route_name>.serviceId=<service_id>
eureka.client.service-url.defaultZone=http://localhost:1111/eureka/
请求过滤
需要继承ZuulFilter抽象类并实现它定义的4个抽象函数就可以完成对请求的拦截和过滤了
filterType 过滤器的类型，决定过滤器在请求的那个生命阶段执行
filterOrder 过滤器的执行顺序 同一阶段存在多个时需要按照返回值依次执行
shouldFilter 是否需要执行此过滤器
run 过滤器的具体逻辑
然后需要加载这个过滤器Bean
使用API网关原因
1.作为系统同一入口，屏蔽了系统内部各个微服务的细结
2.可以为服务治理框架结合，实现自动化的服务实例维护以及维护负载均衡的路由转发
3.可以实现接口权限校验与微服务业务逻辑的解耦
4.通过服务网关中的过滤器，在各个生命周期中去校验请求的内容，将原本在对外服务层中的校验迁移，保证服务的无状态性，同时降低了测试难度。
路由详解
传统路由配置
单实例配置
zuul.routes.<route>.path
zuul.routes.<route>.url
多实例配置
zuul.routes.<route>.path
zuul.routes.<route>.service-id=<service_id>
ribbon.eureka.enabled=false # 由于没有整合eureka 因此关闭eureka而手动维护服务列表
<service_id>.ribbon.listOfServers=XXX,XXX #  手动维护的服务列表 ribbon用于负载均衡
服务路由配置
zuul.routes.<route>.path
zuul.routes.<route>.service-id
也可以使用
zuul.routes.<service_id>=<path> (与上面2行等价)
zuul也是eureka服务治理下的一个普通的微服务应用，除了将注册，也会从注册中心获取所有服务以及实力清单，所以在eureka的帮助下，zuul也维护了系统中所有serviceId与实例地址的映射关系
服务路由默认规则
引入eureka后，会为每个服务自动创建一个默认路由规则，使用service_id为前缀的path
所以需要手动设置一些不希望被外部访问的服务 使用
zuul.ignore-services=* (*则所有服务都不创建默认匹配)
自定义路由映射规则
@Bean
public PatternServiceRouteMapper serviceRouteMapper() {
	return new PatternServiceRouteMapper("serviceid的正则表达式匹配规则 <xxx>提取匹配的部分", "路由路径的规则 使用serviceId匹配中的部分");
}
比如"?<name>^.+"- 表示name 为 -之前的所有字符

路径匹配
? 任意单个字符
* 任意数量字符
** 任意数量字符，支持多级目录
多个匹配按照配置时的顺序，而不是精度 (由于properties文件不能保证配置有序 因此可以使用yml来保证配置的顺序得到使用)
忽略表达式
zuul.ignore-services
路由前缀
zuul.prefix (添加前缀)
zuul.routes.<route>.strip-prefix=true 为指定路由关闭前缀
本地跳转
zuul.routes.<route>.path=/**
zuul.routes.<route>.url=forward:/**
Cookie与头信息
默认情况下，Zuul在请求路由时，会过滤掉HTTP请求头信息中的一些敏感信息，防止被传递到下游的外部服务器
zuul.sensitiveHeaders 包括Cookie、Set-Cookie、Authorization三个 因此Web应用中 需要修改设置
1.zuul.sensitiveHeaders= 置空 不推荐
2.zuul.routes.<route>.customSensitiveHeaders=true
  zuul.routes.<route>.sensitiveHeaders=
重定向问题
zuul.addHostHeader=true
Hystrix和Ribbon支持
hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds API网关中路由转发请求的HystrixCommand执行超时时间
ribbon.ConnectTimeout 转发请求时，创建请求连接的超时时间
ribbon.ReadTimeout 转发请求的超时时间
zuul.retryable=false
zuul.routes.<route>.retryable=false
过滤器详解
filterType
|- pre 请求被路由之前调用
|- routing 路由请求时调用
|- post routing和error之后被调用
|- error 发生错误时被调用
核心过滤器
spring-cloud-netflix-core模块中的org.springframework.cloud.netflix.zuul.filters包下
异常处理
try-catch处理
ZuulFilter中采用try-catch块处理，在catch中不做输出操作 而是想请求上下文中添加一些error相关的参数
error.status_code 错误编码
error.exception Exception异常对象
error.message 错误信息
ErrorFilter
不足与优化
自定义异常信息

禁用过滤器
zuul.<SimpleClassName>.<filterType>.disable=true
动态加载
可以在不重启的前提下 东塔吸怪路由规则和添加或删除过滤器
动态路由
路由规则的控制几乎都在appliction.properties或yml配置文件中 结合Config动态配置 即可完成动态刷新路由规则的功能
动态过滤器

- 分布式配置中心 Spring Cloud Config
快速开始
引入依赖 spring-cloud-config-server
在启动类上添加@EnableConfigServeer
在配置文件中添加
spring.cloud.config.server.git.uri git仓库位置
spring.cloud.config.server.git.search-paths 仓库路径下的相对搜索位置 可以配置多个
spring.cloud.config.server.git.username 用户名
spring.cloud.config.server.git.password 用户密码
配置规则详解
在目录下创建对应的配置文件
<application_name>.<profile>.properties/yml
访问配置信息的URL与配置文件的映射关系如下
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
在读取配置信息的同时，还会保存一份在config-server的文件系统中，实质上是config-server通过clone命令将配置内容复制一份到本地存储，然后读取并返回给微服务应用进行加载
客户端配置映射
引入依赖spring-cloud-starter-config
在bootstrap.properties中(必须在bootstrap而不是application中，优先级不一样)添加设置
spring.application.name=
spring.cloud.config.profile=
spring.cloud.config.label= (git分支)
spring.cloud.config.uri= 配置中心config-server的地址
再使用
@RefreshScope 修饰类
@Value("${}") 修饰变量
@Autowired
private Environment env; 使用env.getProperty(key, default);
服务端详解
客户端启动时
1.应用启动时，根据bootstrap.properties中配置的应用名{spring.application.name}、环境名{spring.cloud.config.profile}、分支名{spring.cloud.config.label}向{spring.cloud.config.uri}配置的配置中心请求获取配置信息
2.配置中心根据自己维护的Git仓库信息和客户端传递过来的配置定位信息去查找配置信息
3.通过git clone命令将找到的配置信息下载到配置中心的文件系统中
4.配置中心创建Spring的ApplicationContext实例，并从Git本地仓库中加载配置文件，最后将这些配置内容读取出来返回给客户端使用
5.客户端应用在获取外部配置文件后加载到客户端的ApplicationContext实例，该配置内容优先级高于客户端Jar包内部的配置内容，将不再加载
Git配置仓库
Spring Cloud Config默认使用Git，只需在配置文件中配置
spring.cloud.config.server.git.uri # 可以使用file://指定本地git仓库进行快速开发与调试 spring.cloud.config.server.git.uri=file://xxxx
spring.cloud.config.server.git.search-paths
spring.cloud.config.server.git.username
spring.cloud.config.server.git.password
占位符URI
{application}
{profile}
{label}
用于URI配置
比如 代码库为XXX/service 配置库为XXX/service-config
可以使用 spring.cloud.config.server.git.uri=XXX/{application}-config
配置多个仓库
spring.cloud.config.server.git.uri= # 默认git仓库
spring.cloud.config.server.git.repos.{application}.pattern= # 匹配规则 格式为{application}/{profile}
spring.cloud.config.server.git.repos.{application}.uri= # 匹配的仓库
spring.cloud.config.server.git.repos.test= # test对应仓库
子目录存储
spring.cloud.config.server.git.search-paths
访问权限
spring.cloud.config.server.git.username
spring.cloud.config.server.git.password
SVN配置仓库
需要引入SVN的依赖 groupId org.tmatesoft.svnkit artifactId svnkit
然后将配置的git换为svn即可
本地仓库
默认会存储在以config-repo为前缀的临时目录中 比如tmp/config-repo-<随机数>的目录
spring.cloud.config.server.git.basedir 指定仓库在本地的路径
本地文件系统
spring.profiles.active=native
spring.cloud.config.server.native.searchLocations
健康监测
属性覆盖
spring.cloud.config.server.overrides.<key>=<value>
客户端从配置中心获取配置信息时，都会去的这些配置信息
安全保护
引入Spring Security依赖
在配置中心配置响应的权限管理
再在客户端中配置
spring.cloud.config.username=
spring.cloud.config.password=
加密解密
spring.datasource.username=
spring.datasource.password={cipher}xxx
Cloud Config中{cipher}前缀标注为加密值 当微服务客户端加载配置时将自动进行解密
使用前提
下载JCE，解压后放到$JAVA_HOME/jre/lib/security目录下
相关端点
/encrypt/status 加密功能状态的端点
/key 查看密钥的端点
/encrypt 对请求的body内容进行加密的端点
/decrypt 解密端点
高可用配置
传统模式
无需额外配置 指向同一个Git仓库即可 客户端再指向配置在配置中心集群上的负载均衡器即可
服务模式
也可以将Config Server作为一个普通的微服务应用，纳入Eureka服务治理体系中
客户端详解
URI指定配置中心
只有在bootstrap.properties中配置spring.cloud.config.uri时才会去配置中心中加载环境配置
服务化配置中心
服务端配置
添加eureka依赖 spring-cloud-starter-eureka
在配置文件中添加服务注册中心
eureka.client.service-url.defaultZone=xxx
在启动类上添加@EnableDiscoveryClient注解
客户端配置
增加eureka依赖
在bootstrap.properties配置文件中修改
eureka.client.service-url.defaultZone=xxx
spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-server
spring.cloud.config.profile=dev
再在启动类上添加@EnableDiscoveryClient注解
失败快速响应与重试
bootstrap.properties中添加
spring.cloud.config.fail-fast=true
增加一下依赖
<dependency>
    <groupId>org.springframework.retry</groupId>
    <artifactId>spring-retry</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
动态加载配置
引入actuator依赖
调用refresh端口即可(actuator需要安全管理 只暴露基础的health和info)
需要配置
management.security.enabled=false
endpoints.refresh.enabled=true
- 消息总线 Spring Cloud Bus
RabbitMQ
1.Broker 消息队列中的实体，是一个中间件应用，负责接收消息生产者的消息，然后将消息发送到消息接受者或者其他的Broker
2.Exchange 消息交换机 是消息第一个到达的地方 消息通过它指定的路由规则 分发到不同的消息队列中
3.Queue 消息队列 消息通过发送和路由之后最终达到的地方，到达Queue的消息即进入逻辑上等待消费的状态 每个消息都会被发送到一个或多个队列
4.Binding 绑定 作用就是把Exchange和Queue按照路由规则绑定起来，也就是虚拟连接
5.Routing Key 路由关键字 Exchange根据这个关键字进行消息投递
6.Virtual host  虚拟主机 是对Broker的虚拟划分 将消费者、生产者和依赖的AMOP相关结构进行隔离 一般为了安全考虑 比如 可以在一个Broker中设置多个虚拟主机 对不同用户进行权限分离
7.Connection 连接 代表生产者、消费者、Broker之间进行通信的物理网络
8.Channel 消息通道 用于连接生产者和消费者的逻辑结构 每个连接中 可建立多个Channel 每个Channel代表一个回话任务 通过Channel可以隔离同一连接中的不同交互内容
9.Producer 生产者 制造消息并发送消息的程序
10.Consumer 消息消费者 接受消息并处理消息的程序
消息投递到队列的整个过程大致
1.客户端连接到消息队列服务器 打开一个Channel
2.客户端声明一个Exchange，并设置相关属性
3.客户端声明一个Queue,并设置相关属性
4.客户端使用Routing Key，在Exchange和Queue之间建立绑定关系
5.客户端投递消息到Exchange
6.Exchange接收到消息后，根据消息的Key和已经设置的Binding，进行消息路由，将消息投递到一个或多个Queue中
Exchange类型
1.Direct交换机 完全根据Key进行投递 比如绑定时设置Routing Key为abc，那么客户端提交的消息 只有设置了Key为abc的才会被投递到队列
2.Topic交换机 对Key进行模式匹配后进行投递 可以使用符号#匹配一个或多个词 符号*刚好匹配一个词
3.Fanout交换机 不需要Key 采取广播的模式 消息进入 投递到与该交换机绑定的所有队列中
SpringBoot中使用RabbitMQ
1.引入amqp依赖
2.在配置文件配置信息
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=
spring.rabbitmq.password=
3.配置一个Queue的Bean
@Configuration
public class RabbitMQConfiguration {
    @Bean
    public Queue rabbitmqQueue() {
        return new Queue("queue_name");
    }
}
配置Sender
@Component
public class Sender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send() {
        amqpTemplate.send("queue_name", "context");
    }
}
配置Receiver
@Component
@RabbitListener(queues= {"queue_name"})
public class Receiver {
    @RabbitHandler
    public void process(String hello) {
        System.out.println(hello);
    }
}
整合Spring Cloud Bus
在config-client应用中引入spring-cloud-starter-bus-amqp依赖
再在配置文件中配置
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=
spring.rabbitmq.password=
即可实现 使用/bus/refresh重新读取配置信息的功能 (可能需要配置management.security.enable=false来关闭权限管理)
原理分析
微服务应用的实例都引入Spring Cloud Bus，这些实例都连接道路RabbitMQ的消息总线上
当修改了配置信息，并向其中一台应用服务器发送了/bus/refresh POST请求后，此实例会将刷新请求发送到消息总线中 其他实例则会从消息总线中读取到刷新事件并重新获取配置信息 从而实现配置信息的动态更新
指定刷新范围
/bus/refresh接口提供了一个destination参数 用于定位具体刷新的应用程序 比如/bus/refresh?destination=customer:9000 总线上的各应用实例会根据destination属性的值来判断是否为自己的实例名 若符合才进行配置刷新

# Greenwich.SR1 版本需要修改一些配置
spring:
	bus:
		trace: 
			enabled: true
		enabled: true
management:
	endpoints:
		web:
			exposure:
				include: bus-refresh # 没有配置安全保护时 需要这么配置将bus-refresh端口暴露出去
使用host:port/actuator/bus-refresh 刷新配置信息
	

Spring Boot RabbitMQ配置
详情百度
Kafka实现消息总线
Kafka是基于消息发布-订阅模式实现的消息系统 主要设计目标
1.消息持久化 时间复杂度为O(1)的方式提供消息持久化能力
2.高吞吐
3.分布式
4.跨平台
5.实时性
6.伸缩性
Kafka涉及的基本概念
1.Broker Kafka集群包括一个或多个服务器 称为Broker
2.Topic 逻辑上同RabbitMQ的Queue队列相似 每条发布到Kafka集群的消息都必须要有一个Topic
3.Partition 物理概念上的分区 为了提供系统吞吐率 物理上每个Topic会分为一个或多个Partition 每个Partition对应一个文件夹
4.Producer 消息生产者
5.Consumer 消息消费者
6.Consumer Group 每个Consumer属于一个特定的组
（Windows使用Kafka可能需要修改根目录\bin\windows下的kafka-run-class中179行的cp %CLASSPATH% -> cp "%CLASSPATH%"）
整合Spring Cloud Bus
引入Kafka依赖 spring-cloud-starter-bus-kafka
如果使用默认配置 则直接启动即可完成整合
会在kafka下创建一个SpringCloudBus的Topic 都订阅这个Topic

需要修改配置
spring:
	cloud:
		strem:
			kafka:
				binder: 
					brokers: <host>:<port>

- 消息驱动的微服务 Spring Cloud Stream
B版本只支持Rabbitmq、Kafka
快速开始
引入依赖 spring-cloud-starter-stream-rabbit
设置一个Receiver类
@EnableBinding(Sink.class)
public class SinkReceiver {
    @StreamListener(Sink.INPUT)
    public void receive(Object payload) {
        System.out.println(payload);
    }
}
@EnableBinding 用于指定一个或多个定义了@Input或@Output注解的接口 以此实现对消息通道(Channel)的绑定 通过@EnableBinding(Sink.class)绑定Sink接口 是Cloud Stream中默认实现对输入消息通道绑定的定义
Sink接口通过@Input注解绑定一个input通道，除了Sink接口，Cloud Stream还默认实现了绑定output接口的Source接口，还有结合Sink和Source的Processor接口
@StreamListener注解用于方法，作用是被修饰的方法注册为消息中间件上数据流的时间监听器。
核心概念
Cloud Stream构建的应用与消息中间件是通过Binder相关联的，绑定器起到隔离作用
绑定器
通过定义绑定器作为中间层，完美实现应用与消息中间件细结的隔离，通过向应用暴露统一的Channel通道 无需再考虑各种不同的消息中间件的实现
也有默认配置信息 比如RabbitMQ的默认配置
发布-订阅模式
一条消息被投递到消息中间件之后 会通过共享的Topic主题进行广播 消息消费者在订阅的主题中收到并触发自身的业务逻辑处理
消费组
spring.cloud.stream.bindings.input.group 属性指定一个组名 当消息进入主题之后 都会收到消息的副本 但只会有一个实例进行消费
默认情况下 没有为应用指定消费组时 会分配一个独立的匿名消费组 如果同一主题下的应用都没有被指定消费组时 消息发布之后 所有应用都会消费 因为各自属于一个独立的组
消息分区
使用详解
开启绑定功能
@EnableBinding
本身带有@Configuration 会被Spring识别
通过@Import加载需要的基础配置类
ChannelBindingServiceConfiguration 会加载消息通道绑定必要的一些实例
BindingBeanRegister 是ImportBeanDefinitionRegistrar接口的实现 主要Spring加载Bean的时候被调用 加载更多的Bean 因为在@Import中引用 所以在加载其他配置后 会回调创建其他的Bean 这些Bean则从value属性定义的类中获取
BinderFactoryConfiguration Binder工厂的配置 主要用来加载与消息中间件相关的配置信息
SpelExpressionConverterConfiguratin SpEL表达式转换器配置
绑定消息通道
@EnableBinding(Sink/Source/Processor.class)
注入绑定接口
@StreamListener()

# 自定义
最好将每个Input和Output分开
public interface CustomReceiver {
	String INPUT = "customInput";
	@Input(CustomReceiver.INPUT)
	SubscriableChannel input();
}
public interface CustomSender {
	String OUTPUT = "customOutput";
	@Output(CustomSender.OUTPUT)
	MessageChannel output();
}
@EnableBinding({CustomReceiver.class, CustomSender.class})
public class CustomService {
	@StreamListening(CustomReceiver.INPUT)
	public void receive(String payload) {
		logger.info(payload);
	}
}
需要在配置文件中配置
spring:
	cloud:
		stream:
			bindings:
				customInput:
					destination: customTopic # 合并到同一个Topic中
					group: xxxGroup # 同一个消费组中的多个消费者只有一个消费 多个消费组都可以消费
				customOutput:
					destination: customTopic


####

暂时没看完

#####


### Docker
Docker在开发和运维中的优势
1.更快的交付和部署
2.更搞笑的紫苑利用
3.更轻松的迁移和扩展
4.更简单的更新管理
Docker与虚拟机比较
1.Docker容器很快 启动和停止可以在秒级实现
2.Docker容器对系统资源需求很少
3.Docker通过类似Git的操作来方便用户获取、分发和更新应用镜像，指令简明，学习成本较低
4.Docker通过Dockerfile配置文件来支持灵活的自动化创建和部署机制，提高工作效率
Docker容器除了运行其中的应用之外，基本不消耗额外的系统资源，保证应用性能的同时，尽量减少系统开销
虚拟化与Docker
计算机领域 虚拟化：资源管理技术，是将计算机的各种实体资源，如服务器、网络、内存、存储等，予以抽象、转换后呈现出来，打破实体结构间的不可切割的障碍，使用户可以用比原本的组态更好的方式来应用这些资源
|- 基于硬件
|- 基于软件
- Docker核心概念和安装
三大核心概念
1.镜像
2.容器
3.仓库
1.Docker镜像
类似于虚拟机镜像，可以理解为一个面向Docker引擎的只读模板，包含了文件系统。
镜像是创建Docker容器的基础，通过版本管理和增量的文件系统，Docker提供了一套十分简单的机制来创建和更新现有的镜像
2.Docker容器
类似于一个轻量级的沙箱，Docker利用容器来运行和隔离应用
容器是从镜像创建的应用运行实例，可以启动、开始、停止、删除，而容器间都是隔离、不可见的
镜像自身是只读的，容器从镜像启动的时候，Docker会在镜像的最上层创建一个可写层，镜像本身将保持不变
3.Docker仓库
类似于代码仓库
分为公开、私有
安装
CentOS7以后
sudo yum install -y docker
sudo systemctl restart docker (启动)
sudo systemctl status docker (查看状态)
1.获取镜像
docker pull
2.查看镜像信息
docker images
docker inspect 镜像ID # 获取镜像详细信息
3.搜寻镜像
docker search TERM # 支持的参数 --automatched=false 仅显示自动创建的镜像 --no-trunc=false 输出信息不截断显示 --s, --stars=0 指定仅显示评价为指定星级以上的镜像
4.删除镜像
docker rmi IMAGE [IMAGE...] # IMAGE可以为标签或ID 多个标签则只删除标签 若只剩一个标签则删除镜像
5.创建镜像
三种方法
A.基于已有镜像的容器创建
B.基于本地模板导入
C.基于Dockerfile创建
A.基于已有镜像的容器创建
docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
主要选项有
-a, --author="" 作者信息
-m, --message="" 提交信息
-p, --pause=true 提交时暂停容器运行
B.基于本地模板导入
cat xxx | docker import - xxx
6.存出和载入镜像
docker save
docker load
存出镜像 将镜像取出保存为本地文件
docker save -o file_name image
载入镜像
docker load --input file_name
或者
docker load < file_name
7.上传镜像
docker tag NAME TAG
docker push NAME[:TAG]
容器
1.创建容器
docker create # 创建容器 信件的容器处于停止状态 使用docker start命令启动
docker start
docker run # 新建被启动容器或将终止状态的容器重新启动
|- 检查本地是否存在指定的镜像 不存在就从公有仓库下载
|- 利用镜像创建并启动一个容器
|- 分配一个文件系统 并在只读的镜像层外挂载一层可读写层
|- 从宿主主机配置的网桥接口中桥接一个虚拟接口到容器中去
|- 从地址池配置一个IP地址给容器
|- 执行用户指定的应用程序
|- 执行完毕后容器被终止
参数 -t 让Docker分配一个伪终端并绑定到容器的标准输入上 -i 让容器的标准输入保持打开
用户可以使用Ctrl+d或exit命令退出容器
守护态运行
docker run -d
2.终止容器
docker stop [-t|--time[=10]] #首先向容器发送SIGTERM信号 等待一段时间后 再发送SIGKILL信号终止容器
docker ps -a -q  #查看处于终止状态容器的ID信息
docker start # 启动容器
docker restart # 重启容器
3.进入容器
docker attach
Docker自带命令
有时候并不方便 多窗口同时attach同一个容器时 所有窗口同步显示 某窗口阻塞时 其他窗口也无法操作
docker exec
nsenter工具在util-linux2.23版本后包含
4.删除容器
docker rm [OPTIONS] CONTAINER [CONTAINER...]
支持的选项包括
-f, --force=false 强行终止并删除一个运行中的容器
-l, --link-false 删除容器的连接，但保留容器
-v, --volumes=false 删除容器挂载的数据卷
5.导入和导出容器
导出容器 导出一个已经创建的容器到一个文件，不管容器是否处于运行状态
docker export CONTAINER
比如 docker export CONTAINER > xxx.tar
导入容器 导出的文件可以重新导入成为镜像 (镜像而不是容器)
docker import - REPOSITORY/IMAGE:TAG

仓库
1.Docker Hub 官方仓库
登录
docker login
基本操作
docker pull
docker search (一种基础镜像比如centos是由官方创建的镜像 一种user_name/image_name是由用户提供的镜像)
docker push
自动创建
2.Docker Pool 国内仓库
查看镜像
下载镜像
docker pull dl.dockerpool.com:5000/IMAGE
3.创建和使用私有仓库
可以使用官方提供的registry镜像来简单搭建一套本地私有仓库环境
docker run -d -p 5000:5000 registry
默认创建在容器的/tmp/registry目录下 -v参数指定路径

数据管理
使用Docker过程中，需要查看容器内应用产生的数据，或者需要把容器的数据进行备份，甚至多个容器之间进行数据的共享
容器中的管理数据主要有两种方式
数据卷
数据卷容器
1.数据卷 -v 参数
是一个可供容器使用的特殊目录 绕过文件系统 可以提供很多有用的特性
A.数据卷可以再容器之间共享和重用
B.对数据卷的修改会立马生效
C.对数据卷的更新，不会影响镜像
D.会一直存在，直到没有容器使用
在容器内创建一个数据卷
docker run -d -P --name web -v /webapp training/webapp python app.py
挂载一个主机目录作为数据卷
docker run -d -P --name web -v /src/webapp:/opt/webapp:ro training/webapp python app.py
加载主机的/src/webapp目录到容器的/opt/webapp目录 是绝对路径 不存在则Docker自动创建
默认权限是rw 也可以指定权限 ro表示只读
挂载一个本地主机文件作为数据卷
-v标记也可以从主机挂载单个文件到容器中作为数据卷
docker run --rm -it -v ~/.bash_history:/.bash_history ubuntu /bin/bash
2.数据卷容器
其实就是一个普通的容器，专门用它提供数据卷供其他容器挂载使用
首先创建一个数据卷容器dbdata，并在其中创建一个数据卷挂载到/dbdata
docker run -it -v /dbdata --name dbdata ubuntu
docker run -it --volumes-from dbdata --name db1 ubuntu
docker run -it --volumes-from dbdata --name db2 ubuntu
db1 db2 dbdata三个容器共享一个数据卷/dbdata
可以多次使用--volumes-from参数来从多个容器挂载多个数据卷 还可以从其他已经挂载了容器卷的容器来挂载数据卷
删除挂载的容器，数据卷不会自动删除 如果需要删除一个数据卷 必须在删除最后一个还挂载他的容器时显式使用
docker rm -v 命令来指定同时删除关联的容器
3.利用数据卷容器迁移数据
备份
docker run --volumes-from dbdata -v $(pwd):/backup --name worker ubuntu tar cvf /backup/backup.tar /dbdata
恢复
先创建一个带有数据卷的容器
docker run -v /dbdata --name dbdata2 ubuntu /bin/bash
再创建一个新的容器挂载dbdata2的容器 并解压到所挂载的容器卷中的即可
docker run --volumes-from dbdata2 -v $(pwd):/backup ubuntu tar xvf /back/backup.tar

网络基础配置
1.端口映射实现访问容器
在启动容器的时候，如果不指定对应参数，在容器外部是无法通过网络来访问容器内网络应用和服务的
当容器中运行一些网络应用时，要让外部访问这些应用时 可以通过-P或-p参数来指定端口映射 当使用-P标记时 Docker会随机映射一个49000-49900的端口至容器内部开放的网络端口
docker logs -f # 查看应用的信息
-p则可以指定要映射的端口 支持的格式 ip:hostPort:containerPort | ip::containerPort | hostPort:containerPort
映射所有接口地址
使用hostPort:containerPort格式将本地的5000端口映射到容器的5000端口
docker run -d -p 5000:5000 xxx
此时默认会绑定本地所有接口上的所有地址 多次使用-p标记可以绑定多个端口
映射到指定地址的指定端口
可以使用ip:hostPort:containerPort格式指定映射使用一个特定地址
docker run -d -p 127.0.0.1:5000:5000 xxx
映射到指定地址的任意端口
使用ip:containerPort绑定任意端口到容器的指定端口
docker run -d -p 127.0.0.1::5000 xxx
查看映射端口配置
docker port查看当前映射的端口配置
2.容器互联实现容器间通信
容器的连接系统是除了端口映射外另一种可以与容器中应用进行交互的方式 会在源和接受容器之间创建一个隧道 接受容器可以看到源容器指定的信息
自定义容器命名
连接系统一句容器的名称来执行
创建容器 系统默认会分配一个名字 但自定义好处：
A.自定义的命名比较好记
B.当要连接其他容器时候，可以作为一个有用的参考点
--name 参数为容器指定名称
docker run --name container_name image
--rm 在终止后立即删除
容器互联
使用--link参数可以让容器之间安全的进行交互
docker run -d -P --name container_name --link linked_container_name/linked_alias_name xxx
docker通过两种方式为容器公开连接信息
A.环境变量
B.更新/etc/hosts文件

使用Dockerfile创建镜像
Dockerfile是一个文本格式的配置文件 可以使用Dockerfile快速创建自定义的镜像
1.基本结构
Dockerfile由一行行命令语句组成 并且支持以#开头的注释行
一般而言 分为四部分
A.基础镜像信息
B.维护者信息
C.镜像操作指令
D.容器启动时执行指令
一开始必须指明所基于的镜像名称 接下来一般会说明维护者信息
后面是镜像操作指令 例如run指令 run指令将对镜像执行跟随的命令 每运行一条run指令 镜像添加新的一层 并提交 最后CMD指令 来指定运行容器时的操作命令
2.指令
格式
INSTRUCTION  argument
包括FROM、MAINTAINER、RUN等
A.FROM
格式为FROM <image>或FROM<image>:<tag>
第一条指令必须为FROM指令，并且，如果在同一个Dockerfile中创建多个镜像时，可以使用多个FROM指令
B.MAINTAINER
格式
MAINTAINER <name>
指定维护者信息
C.RUN
格式为RUN <command>或RUN ["executable", "param1", "param2"]
前者将在shell终端中运行命令 后者则使用exec执行
每条run指令将在当前镜像基础上执行指令命令，并提交为新的镜像 命令较长时 可以用\来换行
D.CMD
支持3种格式
CMD ["executable", "param1", "param2"] 使用exec执行 推荐
CMD command param1 param2 /bin/sh
CMD ["param1", "param2"] 提供给ENTRYPOINT
指定启动容器时执行的命令 只有有一条CMD命令 如果指定多条 则只有最后一条执行
E.EXPOSE
格式
EXPOSE <port> [<port>...]
高速Docker服务器容器暴露的端口号 拱互联系统使用 在启动容器时需要通过-P或-p
F.ENV
格式
ENV <key> <value>
指定一个环境变量 会被后续RUN指令使用 并在容器运行时保持
G.ADD
格式
ADD <src> <dest>
复制指定的<src>到容器的<dest> <src>可以是Dockerfile所在目录的一个相对路径，也可以是URL，还可以是tar文件（自动解压为目录）
H.COPY
COPY <src> <dest>
复制本地主机的<src>为容器中的<dest> 当使用本地目录为源目录时 推荐使用
I.ENTRYPOINT
ENTRYPOINT ["executable", "param1", "param2"]
ENTRYPOINT command param1 param2
配置容器启动后执行的命令 并且不可被docker run提供的参数覆盖
只能有一个ENTRYPOINT 多个最后一个执行
J.VOLUME
VOLUME ["/data"]
创建一个可以从本地主机或其他容器挂载的挂载点
K.USER
USER daemon
指定运行容器时的用户名或UID
M.WORKDIR
WORKDIR /path/to/workdir
配置工作目录
M.ONBUILD
ONBUILD [INSTRUCTION]
配置当创建的镜像作为其他新创建镜像的基础镜像所执行的操作指令
3.创建镜像
docker build [OPTIONS]

实战

###
每个测试再添加上来
###



###### Angular
--- 架构概览
Angular是一个用HTML和TypeScript构建客户端应用的平台与框架。
基本构造块是NgModule，为组件提供了编译的上下文环境。NgModule会把相关的代码收集到一些功能几种。Angular应用就是有一组NgModule定义出的，至少会有一个引导应用的根模块，通常还会有很多特性模块。
组件定义视图。
组件使用服务。服务提供商可以作为依赖被注入到组件中。
组件和服务都是简单的类，使用装饰器来标出类型，并提供元数据以告知Angular该如何使用。
组件类的元数据将类和一个用来定义视图的模板关联起来，模板把普通的HTML和Angular指令与绑定标记组合起来。
服务类的元数据提供一些信息，Angular要用这些信息来让组件可以通过依赖注入使用该服务。
应用的组件通常会定义很多视图，并进行分级组织。Angular提供了Router服务来帮助定义视图之间的导航路径。路由器提供了先进的浏览器内导航功能。
模块
Angular定义NgModule，为组件集声明了编译的上下文环境。
每个Angular应用都有一个根模块，通常命名为APPModule，根模块提供了用来启动应用的引导机制，通常还会包含很多功能模块。
可以从其他NgModule中导入功能，并允许导出功能拱其他NgModule使用。
组件
至少有一个根组件，会把组件树和页面中的DOM连接起来，每个组件都会定义一个类，其中包含应用的数据和逻辑，并与一个HTML模板相关联。
@Component()装饰器表明紧随的类是一个组件，并提供模板和该组件专属的元数据。
模板、指令和数据绑定
模板会把HTML和Angular的标记组合起来，这些标记可以在HTML元素显示出来之前修改。模板中的指令会提供程序逻辑，而绑定标记会把应用中的数据和DOM连接在一起
事件绑定 可以通过更新应用的数据来响应目标环境下的用户输入
属性绑定 从应用数据中计算出的值插入到HTML中
支持双向绑定
也可以使用管道转换要显示的值以增强用户体验
服务与依赖注入
对于与特定试图无关并希望跨组件共享的数据或逻辑，可以创建服务类。服务类的定义通常紧跟在@Injectable()装饰器之后，该装饰器的元数据可以让服务作为依赖被注入到客户组件中
依赖注入可以保持组件类的精简和高效
路由Angular的Router模块提供了一个服务。可以让你再应用的各个不同状态和糊涂层次结构之间导航时要使用的路径。
地址栏输入URL，导航到相应的页面
在页面中点击链接，导航到新页面
点击浏览器的前进、后退按钮，在浏览历史中向前或向后导航
路由器会把类似URL的路径映射到视图而不是页面，路由器拦截浏览器加载页面的默认行为，显示或隐藏一个视图层次结构
如果路由器认为当前应用状态需要某些特定的功能，而定义次功能的模块尚未加载，就会按需惰性加载
NgModule简介
Angular应用是模块化的，拥有自己的模块化系统，称为NgModule。一个NgModule就是一个容器，用于存放一些内聚的代码块。可以包含一些组件、服务提供商或其他代码文件，作用域由包含的NgModule定义。
@NgModule元数据
NgModule是一个带有@NgModule()装饰器的类，是一个函数，接受一个元数据对象，属性用来描述这个模块，重要属性如下
1.declarations(可声明对象表) 属于本NgModule的组件、指令、管道
2.exports(导出表) 能在其他模块的组件模板中使用的可声明对象的子集
3.imports(导入表) 导出了本模块中的组件模板所需的类的其他模块
4.providers 本模块向全局服务中贡献的服务的创建器 能被本应用中的任何部分所使用
5.bootstrap 应用的主视图，称为根组件，是应用中所有其他视图的宿主，只有根模块才应该设有这个属性
NgModule和组件
NgModule为其中的组件提供了一个编译上下文环境。根模块总会有一个根组件，并在引导时期创建。任何模块都能包含任意数量的其他组件，这些组件可以通过路由器加载，也可以通过模板加载。
组件及其模板共同定义视图。组件还可以包含视图层次结构，可以在任意复杂的屏幕区域，将其作为一个整体进行创建、修改和销毁。
组件简介
组件控制屏幕上被称为视图的一小片区域
组件通过一些由属性和方法组成的API与视图交互。
组件的元数据
@Component装饰器会指出紧随其后的类是组件类，并为其指定元数据。
组件的元数据告诉Angular获取需要的主要构造快，以创建和展示这个组件及其视图。
最常见的@Componen配置选项
selector CSS选择器
templateUrl 该组件的HTML模板文件相对于这个组件文件的地址 还可以用template属性来提供内联的HTML模板
providers 当前组件所需的服务提供商的数组
数据绑定
支持双向绑定
DOM <----- COMPONENT
	{{value}}
DOM <----- COMPONENT
	[property]="value"
DOM -----> COMPONENT
	(event)="handler"
DOM <-----> COMPONENT
	[(ng-model)]="property"
管道
可以在模板中声明显示值的转换逻辑 带有@Pipe装饰器的类中会定义一个转换函数，来与来输入值转换为拱视图显示用的输出值
使用管道操作符|
{{interpolated_value | pipe_name}}
指令
模板是动态的，当Angular渲染的时候，会根据指令给出的指示对DOM进行转换，指令就是一个带有@Directive()装饰器的类
组件从技术角度上就是一个指令，但是组件非常独特、非常重要
除组件之外，还有两种指令：结构型指令和属性型指令
结构型指令
通过添加、移除或替换DOM元素来修改布局
类似于*ngFor="let value of values" *ngIf=""
属性型指令
会修改现有元素的外观或行为
ngModel指令就是一个属性型指令，实现了双向数据绑定
服务与依赖注入简介
服务是一个广义的概念，包括应用所需的任何值、函数或特性。
依赖注入
DI用于在任何地方给新建的组件提供服务或所需的其他东西。组件是服务的消费者，也就是说，可以被一个服务注入到组件中，让组件得以访问该服务类。
使用@Injectable()装饰器来提供元数据。
注入器是主要机制，在启动过程中创建全局注入器以及其他所需的注入器，无需自行创建
该注入器会创建依赖、维护一个容器来管理这些依赖，并尽可能的复用他们
提供商是一个对象，用来告诉注入器应该如何获取或创建依赖
应用中所需的任何依赖，都必须使用该应用的注入器来注册一个提供商，一边注入器可以使用这个提供商来创建新实例。对于服务，提供商通常就是服务本身
提供服务
对于要用到的任何服务，必须至少注册一个提供商、服务可以在自己的元数据中把自己注册为提供商。也可以为特定的模块或组件注册提供商。要注册提供商，就要在@Injectable、@NgModule、@Component的元数据中声明
默认情况下，ng generate service命令会在@Injectable()装饰器中提供元数据来吧他注册到跟注入器中
@Injectable({
	providedIn: "root",
})
当时用特定NgModule注册提供商时，该服务的同一个实例将会对该NgModule中的所有组件可用，请用@NgModule()装饰器中的providers属性，
@NgModule({
	providers: [
		XXXService,
		XXXLogger
	],
})
当在组件级注册提供商时，每个组件的新实例都会提供一个该服务的新实例，@Component()装饰器的providers属性
模板语法
HTML是Angular模板的语言，<script>被禁用了，阻止脚本注入攻击的风险
插值与模板表达式
插值表达式{{...}}
模板表达式
{{expression}}[property]="expression"
表达式上下文
可以包含组件之外的对象，比如模板输入变量(let hero of heros)和模板引用变量(#heroInput <input #heroInput>{{heroInput.value}})
模板语句
(event)="statement"
单向从数据源到视图 {{expression}} [target]="expression" bind-target="expression"
单向从视图到数据源 (target)="statement" on-target="statement"
双向 [(target)]="expression" bindon-taret="expression"
HTML attribute和DOM property的对比
attribute由HTML定义，property由DOM定义
attribute初始化DOM property，然后任务就完成了。property可以改变，而attribute的值不能改变
attribute、class和style绑定
attribute绑定
[attr.property]="expression"
css类绑定
[class]="expression"
[class.clsName]="expression" expression为真时添加clsName的class 假时移除
通常使用NgClass指令来管理多个类名
样式绑定
[style.property]="expression"
通常使用NgStyle指令来管理多个内联样式
事件绑定
(event)="statement"或者on-event="statement"
$event和事件处理语句
事件绑定中，Angular会为目标事件设置事件处理器
当事件发生时，这个处理器会执行模板语句，绑定会通过名为$event的事件对象传递关于此事件的信息
事件对象的形态取决于目标事件，如果是原生DOM元素事件，$event就是DOM事件对象，有target和target.value属性
如果事件属于指令，那么$event则由指令决定
使用EventEmitter实现自定义事件
指令使用Angular EventEmitter来触发自定义事件，指令创建一个EventEmitter实例，并作为属性暴露出来。指令调用EventEmitter.emit(payload)来触发事件，可以传入任何东西作为消息载荷。父指令通过这个属性来监听事件，并通过$event来访问载荷
双向数据绑定 [(...)]
内置指令
内置属性型指令
NgClass
[ggClass]="{
	c1: b1,
	c2: b2,
	c3: b3,
}"
根据b的真假添加或移除c的class
NgStyle
同上
NgModel
<input [(ngModel)]="" >
使用ngModel需要使用FormsModule模块
ngModel的内幕
[value]="property"
(input)="property=$event.target.value"
[ngModel]="property"
(ngModelChange)="method($event)"
内置结构型指令
常见
NgIf
*ngIf应用于宿主元素上，可以往DOM中添加或移除该元素
NgSwitch
[ngSwtich]="xxxxx"
*ngSwtichCase="case1"
*ngSwtichCase="case2"
*ngSwtichCase="case3"
*ngSwtichCase="case4"
*ngSwtichCase="case5"
*ngSwtichDefault
NgForOf
*ngFor="let a of arr"
NgFor微语法
模板输入变量
let关键字创建了模板输入变量
带索引的*ngFor
*ngFor="let a of arr; let i = index"
带trackBy的*ngFor
trackByHeros(index: number, hero: Hero): number { return hero.id; }
*ngFor="let hero of heros;trackBy: trackByHeros"
只有返回值不同的trackBy才会触发DOM替换
模板引用变量 #var
输入和输出属性
@Input 通过属性绑定时，值会流入这个属性
@Output 几乎总是返回EventEmitter实例，通过事件绑定时，值会流出这个属性
也可以在@Component()装饰器中使用inputs、outpus属性
模板表达式操作符
常见
1.管道 |
2.安全导航操作符 ?.
用于保护视图渲染器，若为null或者undefined也不会失败
3.非空断言操作符 !
4.$any()转换函数来把表达式转换成any类型
用户输入
(event)="statement"
通过$event对象取得用户输入
$event对象的属性取决于DOM事件的类型，所有标准DOM事件都有target属性
$event的类型 
不是所有$event都有.target.value属性的
传入$event是靠不住的
从模板引用变量获取用户输入 #var -> var.value
生命周期钩子
组件生命周期钩子概览
每个接口都有唯一的一个钩子方法，是由接口名再加上ng前缀构成的，比如OnInit接口的钩子就是ngOnInit
生命周期的顺序
ngOnChanges()

当 Angular（重新）设置数据绑定输入属性时响应。 该方法接受当前和上一属性值的 SimpleChanges 对象

在 ngOnInit() 之前以及所绑定的一个或多个输入属性的值发生变化时都会调用。

ngOnInit()

在 Angular 第一次显示数据绑定和设置指令/组件的输入属性之后，初始化指令/组件。

在第一轮 ngOnChanges() 完成之后调用，只调用一次。

ngDoCheck()

检测，并在发生 Angular 无法或不愿意自己检测的变化时作出反应。

在每个变更检测周期中，紧跟在 ngOnChanges() 和 ngOnInit() 后面调用。

ngAfterContentInit()

没当 Angular 把外部内容投影进组件/指令的视图之后调用。

第一次 ngDoCheck() 之后调用，只调用一次。

ngAfterContentChecked()

每当 Angular 完成被投影组件内容的变更检测之后调用。

ngAfterContentInit() 和每次 ngDoCheck() 之后调用

ngAfterViewInit()

每当 Angular 初始化完组件视图及其子视图之后调用。

第一次 ngAfterContentChecked() 之后调用，只调用一次。

ngAfterViewChecked()

每当 Angular 做完组件视图和子视图的变更检测之后调用。

ngAfterViewInit() 和每次 ngAfterContentChecked() 之后调用。

ngOnDestroy()

没当 Angular 每次销毁指令/组件之前调用并清扫。 在这儿反订阅可观察对象和分离事件处理器，以防内存泄漏。

在 Angular 销毁指令/组件之前调用。
接口是可选的
其他Angular生命周期钩子
窥探OnInit和OnDestory
OnInit()
使用ngOnInit()两个原因
1.在构造函数之后马上执行复杂的初始化逻辑
2.在Angular设置完输入属性之后，对该组件进行准备
OnDestory()
必须在Angular销毁指令之前运行，可以放在ngOnDestory()中
OnChange()
一旦检测到组件的输入属性发生变化，就会调用ngOnChanges()方法
DoCheck()
来检测Angular自身无法捕获的变更并采取行动
AfterView()
onAfterViewInit()
onAfterViewChecked()会在每次创建了组件的子视图之后调用
AfterContent()
onAfterContentInit()
onAfterContentChecked()会在外来内容投影到组件之后调用
组件交互
通过输入型绑定把数据从父组件传到子组件
@Input(alias) property 
通过setter截听输入属性值的变化
private _name = '';
@Input()
set name(name: string) {
	this._name = (name && name.trim()) || '<no name set>';
}
get name(): string {
	return this._name;
}
通过ngOnChanges()来截听输入属性值的变化
父组件监听子组件的事件
子组件暴露一个EventEmitter属性
@Output装饰器
子组件和父组件通过本地变量互动
使用#var来使用子组件的变量以及方法
父组件调用@ViewChild()
使用本地变量方法是个简单便利的方法，但也有局限性，只能在模板中使用，父组件本身的代码对子组件没有访问权
如果父组件的类需要读取子组件的属性值或调用子组件的方法，就不能使用本地变量方法
当父组件类需要这种访问时，可以吧子组件作为ViewChild注入到父组件里
@ViewChild(XXXComponent)
private xxxComponent: XXXComponent;
父组件和子组件通过服务来通讯
组件样式
使用标准的CSS来设置样式
使用组件样式
1.在@Component()中使用styles属性
2.范围化的样式
把样式加载进组件中
1.设置styles或styleUrls元数据
2.内联在模板的HTML中
3.通过CSS文件导入
元数据中的样式
ng generate component xxx --inline-style 会定义一个空的styles数组
组件元数据中的样式文件
styleUrls: ['./xxxx.css']
模板内联样式
直接使用<style>
模板中的link标签
<link rel="stylesheet" href="xxx.css">
CSS @imports语法
利用标准css@import规则来把其他CSS文件导入CSS文件中
Angular元素概览
Angular元素就是打包成自定义元素的Angular组件
@angular/elements包导出了一个createCustomElement()API
使用自定义元素
自定义元素会自举启动 在添加到DOM中时就会自动启动自己 并在从DOM中移除时自行销毁自己
动态组件
动态组件加载器
动态组件加载
指令
在添加组件之前，先要定义个锚点来告诉Angular要把组件插入到什么地方
@Directive({
	selector: '[ad-host]',
})
export class AdDirective {
	constructor(public viewContainerRef: ViewContainerRef) { }
}
注入ViewContainerRef来获取对容器视图的访问权，这个容器就是动态加入组件的宿主
加载组件
<ng-template>元素就是刚才制作的指令将应用到的地方
<ng-template ad-host></ng-template>
<ng-template>元素是动态加载组件的最佳选择，不会渲染任何额外的输出
解析组件
@ViewChild(AdDirective) adHost: AdDirective;
constrator(private componentFactoryResolver: ComponentFactoryResolver) {}
loadComponent() {
	let componentFactory = this.componentFactoryResolver.resolverComponentFactory(xxx.component)
	let viewContainerRef = this.adHost.viewContainerRef;
	viewContainerRef.clear();
	let componentRef =viewContainerRef.createComponent(componentFactory);
	(<AdComponent> componentRef.instance).data = adItem.data;
}
属性型指令
用于改变一个DOM元素的外观或行为
指令概览
1.组件-拥有模板的指令
2.结构型指令-通过添加和移除DOM元素改变DOM布局的指令
3.属性型指令-改变元素、组件或其他指令的外观和行为的指令
自定义指令
@Directive({
	selector: '[appHighlight]',
})
export class HighlightDirective {
	constrator(el: ElementRef) {
		el.nativeElement.style.backgroundColor = 'yellow';
	}
}
响应用户引发的事件
import {HostListener} from '@angular/core';

@HostListener('mouseenter') onMouseEnter() {
	this.highlight('yellow');
}
@HostListener('mouseleave') onMouseLeave() {
	this.hightlight(null);
}
private highlight(color: string) {
	this.el.nativeElement.style.backgroundColor = color;
}
使用@Input数据绑定向指令传递值
@Input() hightlightColor: string;
<p appHighlight [highlightColor]="'yellow'">Hightlight</p>
结构型指令
职责是HTML布局，塑造或重塑DOM的结构
*前缀
是一个语法糖，从内部实现来说，Angular把*ngIf属性翻译成一个<ng-template>元素并用来包裹宿主元素
<div *ngIf="hero">Hero</div>
实际上被翻译为<ng-template [ngIf]="her"><div>hero</div></ng-template>
同理*ngFor也是这样
<ng-template ngFor let-hero [ngForOf]="heroes" let-i="index" let-odd="odd" [ngForTrackBy]="trackById"></ng-template>
微语法
Angular微语法能通过简短的、友好的字符串来配置一个指令，微语法解析器把这个字符串翻译成<ng-template>上的属性
每个宿主元素上只能有一个结构型指令
<div [ngSwitch]="">
	<ng-template [ngSwitchCase]="">
	<ng-template [ngSwitchCase]="">
	<ng-template [ngSwitchCase]="">
	<ng-template ngSwitchDefault>
优先使用星号*语法
使用<ng-container>分组
自定义结构型指令
TemplateRef和ViewContainerRef
使用TemplateRef取得<ng-template>的内容，并通过ViewContainerRef来访问这个视图容器
@Directive({
	selector: '[appUnless]',
})
export class UnlessDirective {
	private hasView =false;
	
	constructor(
		private templateRef: TemplateRef<any>,
		private viewContainerRef: ViewContainerRef,
	){}
	
	@Input() set appUnless(condition: boolean) {
		if(!condition && !this.hasView) {
			this.viewContainerRef.createEmbeddedView(this.templateRef);
			this.hasView = true;
		} else if(condition && this.hasView) {
			this.viewContainer.clear();
			this.hasView = false;
		}
	}
}
管道
使用管道
{{new Date(1988, 3, 15) | date}}
对管道进行参数化 {{birthday | date: "MM/dd/yy"}}
自定义管道
@pipe({
	name: 'exponentialStrength'
})
export class ExponentialStrength implements PipeTransform {
	transform(value: number, exponent: string): number {
		let exp = parseFloat(exponent);
		return Math.pow(value, isNaN(exp)?1:exp);
	}
}
表单
简介
Angular表单检测
响应式和模板驱动
	
			响应式 	模板驱动

建立（表单模式）显式，在组件类中创建。隐式，由组件创建。

数据模式	结构化	非结构化

可预测性 	同步	异步

表单验证	函数	指令

可变性		不可变	可变

可伸缩性	访问底层 API 在 API 之上的抽象
共同基础
响应式表单和模板驱动表单共享了一些底层构造块
FormControl实例用于追踪单个表单控件的值和验证状态
FormGroup用于追踪一个表单控件组的值和状态
FormArray用于追踪表单控件数组的值和状态
ControlValueAccessor用于在Angular的FormControl实例和原生DOM之间创建一个桥梁
建立表单模型
响应式表单中建立
<input [formControl]="xxxFormControl">
xxxFormControl=new FormControl('');
模板驱动表单中建立
<input [(ngModel)]="xxxValue">
xxxValue='';
表单中的数据流
响应式表单中的数据流
模板驱动表单中的数据流
响应式表单
<input [formControl]="name">
name = new FormControl('');
管理控件的值
显示表单控件的值
通过可观察对象valueChanges，可以在模板中使用AsyncPipe或在组件勒种使用subscribe()方法来监听表单值的变化
使用value属性，获得当前值的一个快照
替换表单控件的值
setValue('')方法
把表单控件分组
<form [formGroup]="profileForm">
	<label>
		xxx:
		<input formControlName="xxx">
	</>
	<label>
		xxx:
		<input formControlName="xxx">
	</>
</>
profileForm=new FormGroup({
	xxx: new FormControl(''),
	xxx: new FormControl(''),
})
保存表单数据
(ngSubmit)="onSubmit()"
onSubmit() {
	this.profileForm.value;
}
分组的内嵌表单
profileForm=new FormGroup({
	firstName: new FormControl(''),
	lastName: new FormControl(''),
	address: new FormGroup({
		street: new FormControl(''),
		city: new FormControl(''),
		state: new FormControl(''),
		zip: new FormControl(''),
	}),
})
<form [formGroup]="profileForm">
	<label>
		xxx:
		<input formControlName="xxx">
	</>
	<label>
		xxx:
		<input formControlName="xxx">
	</>
	<div formGroupname="address">
		<label>
			xxx:
			<input formControlName="xxx">
		</>
		<label>
			xxx:
			<input formControlName="xxx">
		</>
		<label>
			xxx:
			<input formControlName="xxx">
		</>
		<label>
			xxx:
			<input formControlName="xxx">
		</>
	</>
</>
部分模型更新
setValue()方法为单个控件设置新值,setValue()方法会严格遵循表单组的结构，并整体性替换控件的值。
patchValue()方法可以用对象中所定义的任何属性为表单模型进行替换
使用FormBuilder来生成表单控件
constructor(private fb: FormBuilder){}
profileForm = this.fb.group({
	firstName: [''],
	lastName: [''],
	address: this.fb.group({
		street: [''],
		city: [''],
		state: [''],
		zip: [''],
	}),
})
简单表单验证
导入验证器函数
Validators
profileForm = this.fb.group({
	firstName: ['', Validators.required],
	lastName: [''],
	address: this.fb.group({
		street: [''],
		city: [''],
		state: [''],
		zip: [''],
	}),
})
使用表单数组管理动态控件
导入FormArray
profileForm = this.fb.group({
	firstName: ['', Validators.required],
	lastName: [''],
	address: this.fb.group({
		street: [''],
		city: [''],
		state: [''],
		zip: [''],
	}),
	aliases: this.fb.array([
		this.fb.control(''),
	]),
})
访问FormArray控件
get aliases() {
	return this.profileForm.get('aliases') as FormArray;
}
addAlias() {
	this.aliases.push(this.fb.control(''));
}
显示表单数组
<div *ngFor="let address of aliases.controls; let i = index">
	<input [formControlName]="i">
模板驱动表单
数据绑定
NgForm指令
<form #heroForm="ngForm">
Angular会自动为form标签添加NgForm指令
[(ngModel)]=""
通过ngModel跟踪修改状态与有效性验证

状态			为真时的 CSS 类		为假时的 CSS 类

控件被访问过。	ng-touched			ng-untouched

控件的值变化了。ng-dirty			ng-pristine

控件的值有效。	ng-valid			ng-invalid
显示和隐藏验证错误信息
表单验证
模板驱动验证
添加指令
<input id="name" name="name" class="form-control"
      required minlength="4" appForbiddenName="bob"
      [(ngModel)]="hero.name" #name="ngModel" >

<div *ngIf="name.invalid && (name.dirty || name.touched)"
    class="alert alert-danger">

	<div *ngIf="name.errors.required">
		Name is required.
	</div>
	<div *ngIf="name.errors.minlength">
		Name must be at least 4 characters long.
	</div>
	<div *ngIf="name.errors.forbiddenName">
		Name cannot be Bob.
	</div>

</div>
响应式表单验证
验证器函数
1.同步验证器
2.异步验证器
自定义验证器
/** A hero's name can't match the given regular expression */
export function forbiddenNameValidator(nameRe: RegExp): ValidatorFn {
	return (control: AbstractControl): {[key: string]: any} | null => {
		const forbidden = nameRe.test(control.value);
		return forbidden ? {'forbiddenName': {value: control.value}} : null;
	};
}
这个函数实际是一个工厂，接受一个用来检测指定名字是否被禁用的正则表达式，并返回一个验证器函数，验证有效时返回null，验证失败时返回验证失误对象
添加到响应式表单
直接添加到FormControl实例中
添加到模板驱动表单
需要添加一个指令
providers: [{provider: NG_VALIDATORS, useExisting: ForbiddenValidatorDirective, multi: true}]
@Directive({
  selector: '[appForbiddenName]',
  providers: [{provide: NG_VALIDATORS, useExisting: ForbiddenValidatorDirective, multi: true}]
})
export class ForbiddenValidatorDirective implements Validator {
  @Input('appForbiddenName') forbiddenName: string;
 
  validate(control: AbstractControl): {[key: string]: any} | null {
    return this.forbiddenName ? forbiddenNameValidator(new RegExp(this.forbiddenName, 'i'))(control)
                              : null;
  }
}
表示空间状态的CSS类
.ng-valid

.ng-invalid

.ng-pending

.ng-pristine

.ng-dirty

.ng-untouched

.ng-touched
跨字段交叉验证
添加到响应式表单
需要在FormGroup创建时把一个新的验证器传给第二个参数
const heroForm = new FormGroup({
  'name': new FormControl(),
  'alterEgo': new FormControl(),
  'power': new FormControl()
}, { validators: identityRevealedValidator });
添加到模板驱动表单中
@Directive({
  selector: '[appIdentityRevealed]',
  providers: [{ provide: NG_VALIDATORS, useExisting: IdentityRevealedValidatorDirective, multi: true }]
})
export class IdentityRevealedValidatorDirective implements Validator {
  validate(control: AbstractControl): ValidationErrors {
    return identityRevealedValidator(control)
  }
}
异步验证
就像同步验证器有 ValidatorFn 和 Validator 接口一样，异步验证器也有自己的对应物：AsyncValidatorFn 和 AsyncValidator。
不同在于
必须返回Promise或者Observable对象
返回的Observable对象必须是有限的
异步验证肯定在同步验证之后执行
异步验证器开始之后，表单空间会进入pending状态，监视pending属性，给出视觉反馈
自定义异步验证器
@Injectable({ providedIn: 'root' })
export class UniqueAlterEgoValidator implements AsyncValidator {
  constructor(private heroesService: HeroesService) {}

  validate(
    ctrl: AbstractControl
  ): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> {
    return this.heroesService.isAlterEgoTaken(ctrl.value).pipe(
      map(isTaken => (isTaken ? { uniqueAlterEgo: true } : null)),
      catchError(() => null)
    );
  }
}
动态表单
导入ReactiveFormsModule模块

可观察对象(Observable)
可观察对象支持在应用中的发布者和订阅者之间传递消息。
可观察对象是声明式的，在消费者订阅之前，函数并不会实际执行。订阅之后，在函数执行完毕或者取消订阅，订阅这都会收到通知。
可观察对象可以发送多个任意类型的值-字面量、消息、事件。
基本用法和词汇
创建一个Observable实例，其中顶一个订阅者(subscriber)函数。当有消费者调用subscribe()方法时，这个函数就会执行。订阅者函数用于定义如果获取或生成要发布的值或消息
要执行锁创建的可观察对象，并从中接受通知，需要调用subscribe方法，并传入一个观察者(observer),这是一个JavaScript对象，定义了收到消息的处理器
subscribe调用会返回一个Subscription对象，具有一个unsubscribe方法，调用该方法就会停止接受通知
定义观察者 observer
用于接受可观察对象通知的处理器要实现Observer接口 这个对象定义了一些回调函数来处理可观察对象可能发送的3种通知
next 必要 用于处理每个送达值 在开始执行后可能执行零次或多次
error 可选 用来处理错误通知 错误会中断这个可观察对象实例的执行过程
complete 可选 用来处理执行完毕通知 执行完毕后 值会继续传给下一个处理器
订阅 subscribe
当有人订阅Observable实例时，才会开始发布值 订阅时要先调用该实例的subscribe()方法 并把一个观察者对象传给它 来接受通知
可观察对象 Observable
可以使用Observable上定义的静态方法创建一些常用的简单可观察对象
of(...items) 返回一个Observable实例，用同步的方式吧参数中提供的值发送出去
from(iterable) 把参数转换为一个Observable实例，通常用于把一个数组转换为一个（发送多个值）的可观察对象
创建可观察对象
new Observable((observer: Observer) => {
	observer.next(xxx);
	return {
		unsubscribe(){}
	}
})
多播
典型的可观察对象会为每一个观察者创建一次新的、独立的执行。
有时候，不应该对每一次订阅者都独立执行一次，可能希望每次订阅都得到同一批值
多播用来让可观察对象在一次执行中同时广播给多个订阅者，借助支持多播的可观察对象，不必注册多个监听器，而是复用第一个next监听器，并把值发送给多个订阅者
RxJS库
响应式编程是面向数据流和变更传播的异步编程范式。
RxJS提供了一种对Observable类型的实现
创建可观察对象的函数
import { from } from 'rxjs';
from(iterable); // 数组遍历
interval(1000); // 计数
fromEvent(element, event); // dom的事件
ajax(url) // ajax请求
操作符
操作符是基于可观察对象构建的一些对集合进行复杂操作的函数 比如map(),filter(),concat(),flatMap()
import {map} from 'rxjs/operators';
可以使用管道把操作符连接起来，把多个由操作符返回的函数这成一个
p = pipe(
	filter(),
	map(),
)
p(observable: Observable);
同时pipe也是Observable对象实例上的一个方法
observable.pipe(
	filter(),
	map()
)等同于以上的方式
常用操作符
创建 from,fromEvent,of,fromPromise
组合 combineLatest,concat,merge,startWith,withLatestFrom,zip
过滤 debounceTime,distinctUntilChanged,filter,take,takeUntil
转换 bufferTime,concatMap,map,mergeMap,scan,switchMap
工具 tap
多播 share
错误处理
除了订阅时的error处理器以外，还提供了catchError操作符 在管道中处理已知错误
Observable.pipe(
	catchError(err => xxx),
)
可观察对象的命名约定 
经常可以看到以$符号结尾
Angular中的可观察对象
EventEmitter
HTTP模块
路由器和表单模块
事件发送器EventEmitter
HTTP
Angular的HttpClient从HTTP方法调用中返回可观察对象 优点
1.可观察对象不会修改服务器的响应 可以使用操作符按需转换
2.HTTP请求可以通过unsubscribe()方法取消
3.请求可以进行配置 以获取进度事件的变化
4.失败的请求很容易重试
Async管道
AsyncPipe会订阅一个可观察对象或承诺，并返回其发出的最后一个值
启动过程
NgModule用于描述应用的各个部分如何组织在一起，每个应用有至少一个Angular模块，根模块就是用来启动应用的模块，通常命名为AppModule
@NgModule装饰器
表明是一个NgModule类，获取元数据对象，告诉Angular如何编译启动本应用
declarations 本应用拥有的组件
imports 导入的其他模块
providers 各种服务提供商
bootstrap 根组件Angular创建它并插入index.html宿主页面中
declarations数组
告诉Angular哪些组件属于该模块
每个组件都应该且只能声明在一个NgModule中
declarations数组只能接受可声明对象包括组件、指令和管道
在当前模块是可见的，除非导出并被导入，否则对于其他模块是不可见的
imports数组
正常工作需要导入哪些额外的模块
providers数组
列出该应用所需的服务
bootstrap数组
NgModules
Angular模块化
NgModule的元数据会做
1.声明某些组件、指令和管道属于这个模块
2.公开其他的部分组件、指令和管道，以便其他模块中使用
3.导入其他带有组件、指令和管道的模块
4.提供一些供应用中的其他组件使用的服务
常用模块
BrowserModule 想要在浏览器中运行应用时
CommonModule 使用NgIf和NgFor
FormsModule 想要构建模板驱动表单时
ReactiveFormsModule 想要构建响应式表单时
RouterModule 想要使用路由功能时
HttpClientModule 使用HTTP功能
导入模块
@NgModule({
	imports: [XXXModule],
})
特性模块的分类
五个常用分类
1.领域特性模块
领域特性模块用来给用户提供应用程序领域中特有的用户体验，比如编辑客户信息或下订单等。

它们通常会有一个顶级组件来充当该特性的根组件，并且通常是私有的。用来支持它的各级子组件。

领域特性模块大部分由 declarations 组成，只有顶级组件会被导出。

领域特性模块很少会有服务提供商。如果有，那么这些服务的生命周期必须和该模块的生命周期完全相同。

领域特性模块通常会由更高一级的特性模块导出且只导出一次。

对于缺少路由的小型应用，它们可能只会被根模块 AppModule 导入一次。
2.带路由的特性模块
带路由的特性模块是一种特殊的领域特性模块，但它的顶层组件会作为路由导航时的目标组件。

根据这个定义，所有惰性加载的模块都是路由特性模块。

带路由的特性模块不会导出任何东西，因为它们的组件永远不会出现在外部组件的模板中。

惰性加载的路由特性模块不应该被任何模块导入。如果那样做就会导致它被急性加载，破坏了惰性加载的设计用途。 也就是说你应该永远不会看到它们在 AppModule 的 imports 中被引用。 急性加载的路由特性模块必须被其它模块导入，以便编译器能了解它所包含的组件。

路由特性模块很少会有服务提供商，原因参见惰性加载的特性模块中的解释。如果那样做，那么它所提供的服务的生命周期必须与该模块的生命周期完全相同。不要在路由特性模块或被路由特性模块所导入的模块中提供全应用级的单例服务。
3.路由模块
路由模块为其它模块提供路由配置，并且把路由这个关注点从它的配套模块中分离出来。

路由模块通常会做这些：

定义路由。

把路由配置添加到该模块的 imports 中。

把路由守卫和解析器的服务提供商添加到该模块的 providers 中。

路由模块应该与其配套模块同名，但是加上“Routing”后缀。比如，foo.module.ts 中的 FooModule 就有一个位于 foo-routing.module.ts 文件中的 FooRoutingModule 路由模块。 如果其配套模块是根模块 AppModule，AppRoutingModule 就要使用 RouterModule.forRoot(routes) 来把路由器配置添加到它的 imports 中。 所有其它路由模块都是子模块，要使用 RouterModule.forChild(routes)。

按照惯例，路由模块会重新导出这个 RouterModule，以便其配套模块中的组件可以访问路由器指令，比如 RouterLink 和 RouterOutlet。

路由模块没有自己的可声明对象。组件、指令和管道都是特性模块的职责，而不是路由模块的。

路由模块只应该被它的配套模块导入。
4.服务特性模块
服务模块提供了一些工具服务，比如数据访问和消息。理论上，它们应该是完全由服务提供商组成的，不应该有可声明对象。Angular 的 HttpClientModule 就是一个服务模块的好例子。

根模块 AppModule 是唯一的可以导入服务模块的模块。
5.可视部件特性模块
窗口部件模块为外部模块提供组件、指令和管道。很多第三方 UI 组件库都是窗口部件模块。

窗口部件模块应该完全由可声明对象组成，它们中的大部分都应该被导出。

窗口部件模块很少会有服务提供商。

如果任何模块的组件模板中需要用到这些窗口部件，就请导入相应的窗口部件模块。
入口组件
是Angular命令式加载的任意组件
特性模块
创建
ng generate module XXXX
在特性模块中创建组件
ng generate component XXXModule/XXXComponent
导入特性模块
在AppModule的@NgModule({
	imports: [特性模块在最后],
})
服务提供商
提供服务
ng generate service XXXService
提供商的作用域
@Injectable({
	providedIn: 'root', //根服务提供商 在整个应用中都可使用
	providedIn: XXXModule // 仅在特定的Module中可以使用
})
单例服务
在Angular中有两种方式提供单例服务
声明该服务应该在应用的根上提供
把该服务包含在AppModule或某个只会被AppModule导入的模块中
惰性加载特性模块
1.创建特性模块
2.创建该特性模块的路由模块
3.配置相关路由
ng generate module xxxModule --routing // --routing将添加路由模块
forRoot和forChild()
forRoot添加到根路由模块中，让Angular直到这是一个跟路由模块，只会在跟路由模块中使用一次
forChild添加到各个特性模块中，让Angular知道这个路由列表负责提供额外的路由并作为额外的特性模块使用
forRoot()包含的注入器配置是全局性的，forChild()中没有注入器配置
依赖注入
@Injectable({})
用服务提供商配置注入器
注入器负责创建服务实例，提供商负责告诉注入器如果创建服务，提供商可以是服务本身
1.@Injectable的providedIn把装饰类的提供商放入root注入器或者指定的模块
2.@NgModule和@Component的providers可以配置将哪些提供商放入当前模块或组件的注入器
注入服务
constructor(private xxxInjectable: XXXInjectable) 在组件的构造函数中注入
注入器树和服务实例
在某个注入器的范围内，服务是单例的
依赖注入具有分层注入体系，意味着下级注入器也可以创建自己的服务实例
注入器具有继承机制，直到根注入器为止，Angular可以注入体系中任何一个注入器提供的服务
可选依赖
@Optional() 
依赖提供商
providers: [
	{
		provide: XXXService, // 提供服务对应的名称
		useClass: XXXClass, // 真正创建服务的类
		useExisting: xx, // 已存在的服务
		useValue: xx, // 使用常量而不是使用new来创建一个服务实例
		useFactory: xxFactory, // 使用工厂方法 返回一个服务实例
		deps: [], // 工厂函数对应的参数列表
	}
]
HttpClient
导入HttpClientModule
注入httpClient: HttpClient
带类型检查的响应
httpClient.get<XXX>(url)
读取完整的响应体
httpClient.get<XXX>(url, {observe: 'response'}
错误处理
可以在.subscribe()中添加一个err=>{...}的错误处理器
也可以在http.get<XXX>(url).pipe(
	catchError(xxxhanlder)
)
retry()用在pipe中作为重复次数
请求非JSON格式的数据
默认返回的是JSON格式的数据 可以指定返回的数据类型
httpClient.get(url, {responseType: 'type'})
	.pipe(
		tap(
			data => {...},
			err => {...}
		)
	)

tap()操作符用于监听数据但是不对数据进行任何修改操作
把数据发送给服务器
添加请求头
const httpOptions = {
	headers: new HttpHeaders({
		'Content-Type': '',
	}),
}
发送POST请求
httpClient.post<XXX>(url, data, httpOptions)
	.pipe(
		retry(),
		tap(
			data => {...},
			err => {...},
		),
		catchError(errHandler),
	)
DELETE,PUT请求的用法类似
高级用法
配置请求
修改请求头
httpOptions创建后不可修改
可以使用
httpOptions.headers = httpOptions.headers.setHeader(key, value);
URL参数
httpOptions = {
	params: new HttpParams().set(key, value)
}
httpClient.get<>(url, httpOptions)
请求防抖(debounce)
.pipe(
	debounceTime(xxx), // 等待时间
)
switchMap()
拦截请求和响应
HTTP拦截机制是主要特性之一，可以声明一些拦截器，用于监视和转换从应用发送到服务器的HTTP请求
编写拦截器
@Injectable()
export class NoopInterceptor implements HttpInterceptor {
	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		reutrn next.handle(req);
	}
}
提供拦截器
{provide: HTTP_INTERCEPTORS, useClass: NoopInterceptor, multi: true}
路由导航
基础知识
<base href>元素
大多数带路由的应用都要在index.html的<head>标签下先添加一个<base>元素，告诉路由器该如何合成导航用的URL
<base href="/">
从路由库中导入
import {RouterModule, Routes} from '@angular/router';
配置
const appRoutes: Routes = [
	{path: '', component: XXXComponent},
	{path: '', component: XXXComponent, data: {key: value},
	{path: '', redirectTo: '', pathMatch: ''},
	{path: '**', component: XXXComponent},
]
@NgModule({
	imports: [RouterModule.forRoot(appRoutes， {enableTracing: true})],
})
path不能以/开头的注释行
路由出口
<router-outlet></router-outlet>
在模板中标出一个位置，路由器将会把要显示在这个出口处的组件显示在这里
路由器链接
<a routerLink="path" routerLinkActive="state"></a>
路有链接的激活状态
routerLinkActive="..."
路由器状态
激活的路由
可以通过注入一个ActivatedRoute的路由服务来获取，有一些有用的信息
url 				路由路径的Observable对象
data				Observable 包含提供给路由的data对象，也包含有解析守卫而来的值
paramMap			Observable 包含一个由当前路由的必要参数和可选参数组成的map对象
queryParamMap 		Observable 包含一个对所有路由都有效的查询参数组成的map对象
fragement			使用于所有路由URL的fragement的Observable
outlet				该路由渲染到的RouterOutlet名称 对于无名路由 是primary
routeConfig			路由的配置信息
parent				子路由的父级ActivatedRouter
firstChild			该路由的子路由的第一个ActivatedRouter
children			该路由所有子路由的ActivatedRouter
路由事件
NavigationStart

本事件会在导航开始时触发。

RouteConfigLoadStart	
本事件会在 Router 惰性加载 某个路由配置之前触发。

RouteConfigLoadEnd	
本事件会在惰性加载了某个路由后触发。

RoutesRecognized

本事件会在路由器解析完 URL，并识别出了相应的路由时触发

GuardsCheckStart

本事件会在路由器开始 Guard 阶段之前触发。

ChildActivationStart	
本事件会在路由器开始激活路由的子路由时触发。

ActivationStart	
本事件会在路由器开始激活某个路由时触发。

GuardsCheckEnd	
本事件会在路由器成功完成了 Guard 阶段时触发。

ResolveStart	
本事件会在 Router 开始解析（Resolve）阶段时触发。

ResolveEnd

本事件会在路由器成功完成了路由的解析（Resolve）阶段时触发。

ChildActivationEnd	
本事件会在路由器激活了路由的子路由时触发。

ActivationEnd	
本事件会在路由器激活了某个路由时触发。

NavigationEnd

本事件会在导航成功结束之后触发。

NavigationCancel

本事件会在导航被取消之后触发。 这可能是因为在导航期间某个路由守卫返回了 false。

NavigationError

这个事件会在导航由于意料之外的错误而失败时触发。

Scroll	
本事件代表一个滚动事件。
总结
Router（路由器）

为激活的 URL 显示应用组件。管理从一个组件到另一个组件的导航

RouterModule

一个独立的 NgModule，用于提供所需的服务提供商，以及用来在应用视图之间进行导航的指令。

Routes（路由数组）

定义了一个路由数组，每一个都会把一个 URL 路径映射到一个组件。

Route（路由）

定义路由器该如何根据 URL 模式（pattern）来导航到组件。大多数路由都由路径和组件类构成。

RouterOutlet（路由出口）

该指令（<router-outlet>）用来标记出路由器该在哪里显示视图。

RouterLink（路由链接）

这个指令把可点击的 HTML 元素绑定到某个路由。点击带有 routerLink 指令（绑定到字符串或链接参数数组）的元素时就会触发一次导航。

RouterLinkActive（活动路由链接）

当 HTML 元素上或元素内的routerLink变为激活或非激活状态时，该指令为这个 HTML 元素添加或移除 CSS 类。

ActivatedRoute（激活的路由）

为每个路由组件提供提供的一个服务，它包含特定于路由的信息，比如路由参数、静态数据、解析数据、全局查询参数和全局碎片（fragment）。

RouterState（路由器状态）

路由器的当前状态包含了一棵由程序中激活的路由构成的树。它包含一些用于遍历路由树的快捷方法。

链接参数数组

这个数组会被路由器解释成一个路由操作指南。你可以把一个RouterLink绑定到该数组，或者把它作为参数传给Router.navigate方法。

路由组件

一个带有RouterOutlet的 Angular 组件，它根据路由器的导航来显示相应的视图。

constructor(
		private route: ActivatedRoute,
		private router: Router,
		private service: Service,
)
route.paramMap.pipe(
	swtichMap((params: ParamMap) => { ... },
)
switchMap()会取消还在执行的请求，重新发送新的请求，并按照顺序提供
ParamMap API
has(name)
get(name)
getAll(name)
keys()
Snapshot(快照)当不需要Observable时的替代品
route.shotsnap.paramMap.get(key)
导航
router.navigate(['path', {data}])
[routerLink]="[path, data]"
子路由
const Routes = [
	{
		path: '',
		component: XXXComponent,
		children: [
			{
			},
			{
			}
		]
	}
]




#### 数据结构
常见数据结构
数组  插入快 查找慢、删除慢、大小固定，只能存储单一元素
有序数组 比一般数组查询快 插入慢、删除慢、大小固定，只能存储单一元素
栈 后进先出 存取其他项慢
队列 先进先出 存取其他项慢
链表 插入快 删除慢
二叉树 如果树是平衡的则查找、插入、删除都快 删除算法复杂
红黑树 查找、删除、插入快，树都是平衡的，算法复杂 
2-3-4树 查找、删除、插入快，树都是平衡的，对磁盘存储有效 算法复杂
哈希表 如果关键字已知则存取极快 删除慢 对存储空间使用不充分
堆 插入、删除快，对最大数据项存取快 对其他数据项存取慢
图 
### 数组
Java中 数组是用来存放同一数据类型的集合（Object类型数组除外）
常用排序算法
不稳定
选择排序 Selection Sort O(n^2) 
在未排序序列中找到最小（大）元素，存放到排序队列的起始位置，再从未排序元素中选择最小（大）元素，放到已排序序列的末尾
1.初始状态 无序区为[1...n],有序区为空
2.第i趟排序(i...n)开始时，有序区和无序区为[1..i-1]和[i..n] 从无序区中选出最小（大）的记录 将此元素与无序区的第一个元素交换位置
3.重复n-1趟直到排序完成
快速排序 O(nlogn)
把一个串分为两个子串 
1.从序列中挑出一个元素作为基准
2.重新排序序列，所有元素比基准小的摆放到基准前面，所有大的放到后面 在这个分区退出之后 基准就处于中间位置 称为分区操作
3.递归的吧小鱼基准值元素的子序列和大于基准值的子序列排序
public void quickSort(int start, int end, int[] arr) {
	if(end - start <= 1) {
		return;
	}
	int middle = start + 1;
	for (int i = start + 1; i < end; i++) {
		if(arr[i] < arr[start]) {
			swap(i, middle++, arr);
		}
	}
	int pivot = arr[start];
	for (int i = start + 1; i < middle; i++) {
		arr[i - 1] = arr[i];
	}
	arr[middle - 1] = pivot;
	quickSort(start, middle, arr);
	quickSort(middle, end, arr);
}
堆排序 O(nlogn)
希尔排序 Shell Sort O(nlogn)
是插入排序的改进版 优先选择距离比较远的元素 又叫做缩小增量排序
1.选择一个增量序列t1,t2,...,tk 其中ti>tj tk=1
2.按增量个数k 对序列进行k躺排序
3.每趟排序 根据对应的增量ti 将待排序列分割为若干长度为m的子序列 分别对各子表进行直接插入排序 仅增量为1时 整个序列作为一个表来处理
@Override
public int[] sort(int[] arr) {
	int length = arr.length;
	for (int gap = length / 2; gap > 0; gap = gap / 2) {
		for (int i = gap; i < length; i++) {
			int current = arr[i];
			int index=  i - gap;
			while(index >= 0 && arr[index] > current) {
				arr[index + gap] = arr[index];
				index = index - gap;
			}
			arr[index + gap] = current;
		}
	}
	return arr;
}
基数排序 O(n*k) 
稳定
插入排序 Insertion Sort O(n^2)
通过构建有序序列 对于未排序数据 在已排序序列中从后向前扫描 找到相应位置插入
1.从第一个元素开始，认为是已被排序
2.取出下一个元素，在已排序序列中从后向前扫描
3.如果已被排序的元素大于此元素则后移一位
4.重复步骤3 直到找到已排序元素小于等于新元素的位置
5.将新元素插入找到的位置
6.重复2-5
public int[] studySort(int[] arr) {
	int length = 0;
	for (int i = 1; i < length; i++) {
		int current = arr[i];
		int index= i - 1;
		while(index >=0 && arr[index] > current) {
			arr[index + 1] = arr[index];
			index--;
		}
		arr[index+1] = current;
	}
	return arr;
}
冒泡排序 Bubble Sort O(n^2)
1.比较相邻的元素 如果第一个比第二个大 就交换
2.对每一对相邻元素做同样的工作 从开始到最后 此时最后的元素应该是最大的
3.再岁所有元素重复以上步骤 除了最后一个
4.重复1,2,3步直到排序完成
归并排序 Merge Sort O(nlogn)
1.把长度为n的输入序列分成两个长度为2/n的子序列
2.把这两个子序列分别采用归并排序
3.将两个排序好的子序列合并成一个最终的排序序列
private void mergeArrWithIndex(int left, int middle, int right, int[] arr) {
	if(middle - left > 1) {
		mergeArrWithIndex(left, left + (middle - left) / 2, middle, arr);
	}
	if(right - middle > 1) {
		mergeArrWithIndex(middle, middle + (right - middle) / 2, right, arr);
	}
	int[] tmp = new int[right - left];
	int lIndex = left;
	int rIndex = middle;
	int index = 0;
	while(lIndex < middle && rIndex < right) {
		if(arr[lIndex] < arr[rIndex]) {
			tmp[index++] = arr[lIndex++];
		} else {
			tmp[index++] = arr[rIndex++];
		}
	}
	while(lIndex < middle) {
		tmp[index++] = arr[lIndex++];
	}
	while(rIndex < right) {
		tmp[index++] = arr[rIndex++];
	}
	for (int i = 0; i < tmp.length; i++) {
		arr[left + i] = tmp[i];
	}
}
二叉树排序 O(nlogn)
基数排序 O(n+k)
桶排序 O(n)
### 栈
基本概念
又称为堆栈或者堆叠 只能在一端进行插入和删除操作的特殊线性表 先进后出 后进先出
栈允许在同一端进行插入和删除操作的特殊线性表，允许删除操作的一端称为栈顶另一端为栈底 栈底固定而栈顶浮动 元素个数为零称为空栈 压入为进栈 删除为退栈
利用栈进行四则运算
### 队列
特殊的线性表 只允许在队头删除 队尾插入 即先进先出
分为单向队列 双向队列 和 优先级队列
优先级队列的插入
public void insert(int i) {
	if(!isFull()) {
		itemCount++;
		int start = tail;
		int end = head < tail ? head + maxSize : head;
		boolean find = false;
		for (int j = start; j < end; j++) {
			if(data[j] < i) {
				int current = j;
				for (int k = end;k > j;) {
					data[(k) % maxSize] = data[--k % maxSize];
				}
				data[current] = i;
				find = true;
				break;
			}
		}
		if(!find) {
			data[head] = i;
		}
		head = ++head % maxSize;
	} else {
		throw new RuntimeException("队列已满");
	}
}
### 链表
通常由一连串结点组成，每个结点包含任意的实例数据和一或两个指向上/下结点位置的链接
单向链表
单向链表的结点分为保存数据和保存下一个结点地址
### 递归
汉诺塔
阶乘
快速排序
希尔排序
### 二叉树
二叉树的删除分情况
首先搜索的时候需要记录经过的结点为parentNode和左结点还是有结点
然后分情况删除
1.找到的结点没有左右结点中的一个
则替换当前结点的next结点是左右结点中存在的一个或者都不存在则为null
如果没有parentNode则是root结点为删除结点 直接设root为next
如果有parentNode则判断替换的是parentNode的左结点还是右结点 用next替换
2.如果左右结点都存在则需要判断
替换的肯定是右结点子树中的一个结点
如果右结点没有左子结点则需要判断
如果parentNode为空则需要设置root为右结点
如果存在parentNode则根据需要替换的是左子结点还是右子结点进行替换
如果右结点存在左子结点则需要循环直到取到没有左子结点的左子结点，并记录每个左子结点的父结点（一开始是右结点），先用找到的最深的左子结点的右子结点替换最深左子结点的父结点的右子结点 在用最深的左子结点替换当前节点即可
### 红黑树
规则
1.节点都有颜色
2.在插入和删除的过程中 都要遵循保持这些颜色的不同排列规则
红黑规则
1.每个节点不是黑色就是红色
2.根节点总是黑色的
3.如果节点是红色的，那它的子节点必须是黑色的（反之则不一定）（也就是从每个叶子到根的所有路径上不能有两个连续的红色节点
4.从根节点到叶节点或空子节点的每条路径上 必须包含相同数目的黑色节点（即相同的黑色高度)
插入的新节点都是红色的
红黑树在插入和删除时的自我修正
通过三种方式修正 改变节点颜色、左旋和右旋
插入的过程和二叉树一致 但是在插入的时候要进行判断
插入：
1.如果根节点为空 则直接将根节点设置为插入的节点
2.将根节点设置为当前节点 
如果插入数据比当前节点大则向右节点操作 如果当前节点的右节点为空 则插入节点 如果右节点不为空则
3.如果插入数据比当前节点大则向左节点操作 如果当前节点的左节点为空 则插入节点 如果左节点不为空则
public void insert(int data) {
	RBTreeNode insertNode = new RBTreeNode(data);
	if(root == null) {
		root = insertNode;
		return ;
	}
	RBTreeNode currentNode = root;
	while(true) {
		if(currentNode.getData() < root) {
			if(currentNode.getRightChild() == null) {
				currentNode.setRightChild(insertNode);
				insertNode.setParent(currentNode);
				insertChange(); // 调色和旋转 
				break;
			} else {
				currentNode = currentNode.getRightChild();
			}
		} else {
			if(currentNode.getLeftChild() == null) {
				currentNode.setLeftChild(insertNode);
				insertNode.setParent(currentNode);
				insertChange(); // 调色和旋转
				break;
			} else {
				currentNode = currentNode.getLeftChild();
			}
		}
	}
}
插入之后需要调整颜色和选择已到达红黑树的规则
1.判断如果当前判断的节点的父结点为null 则需要将当前节点的颜色设置为黑色 并将root指向当前节点 跳出判断
2.如果父结点的颜色为黑 直接跳出判断
3.再取得祖父节点和叔叔节点 如果叔叔节点不为null且颜色为红 设置父结点和叔叔节点的颜色为黑色 祖父节点颜色为红 再以祖父节点为当前节点继续判断
如果叔叔节点为空或者颜色为黑则进行判断
如果父结点是祖父节点的左子结点 当前节点是父结点的左子结点 则设置父结点的颜色为黑 祖父节点的颜色为红 再以祖父节点为支点右旋 再以父结点为当前节点继续判断
如果父结点是父结点的左子结点 当前节点是父结点的右子结点 则以父结点为支点左旋 再以父结点为当前节点进行判断
（右子结点情况相反即可）
public void insertChange(RBTreeNode current) {
	RBTreeNode parent = current.getParent();
	if(parent == null) {
		current.setRed(false);
		root = current;
		return ;
	}
	if(!parent.isRed) {
		return ;
	}
	RBTreeNode grand = parent.getParent();
	RBTreeNode uncle = null;
	if(isLeft(parent)) { // isLeft判断是否是父结点的子结点
		uncle = parent.getRightChild();
		if(uncle != null && uncle.isRed()) {
			uncle.setRed(false);
			parent.setRed(false);
			grand.setRed(true);
			insertChange(grand);
		} else {
			if(isLeft(current)) {
				parent.setRed(false);
				grand.setRed(true);
				rightRotate(grand); // 右旋
			} else {
				leftRotate(parent); // 左旋
			}
			insertChange(parent);
		}
	} else {
		uncle = grand.getLeft();
		if(uncle != null && uncle.isRed()) {
			uncle.setRed(false);
			parent.setRed(false);
			grand.setRed(true);
			insertChange(grand);
		} else {
			if(isLeft(current)) {
				rightRotate(parent);
			} else {
				parent.setRed(false);
				grand.setRed(true);
				leftRotate(grand);
			}
			insertChange(parent);
		}
	}
}
红黑树的删除
分多钟情况
1.如果删除的是红色节点 则无需修改
2.如果删除的是黑色节点 而删除结点中替换的子结点为红色 则将替换的子结点更改为黑色即可
3.如果删除结点是黑色 且替换的结点为黑色 需要继续判断
	1.如果删除的root结点 则替换结点改为黑色 且设置root即可
	2.如果兄弟结点和兄弟结点的左右子结点都为黑色 父结点为红色 只需要交换父结点和兄弟结点的颜色
	3.如果兄弟结点为黑色 但是兄弟结点不同边结点（即替换结点是父结点的左子结点，则对应的是兄弟结点的右子结点)为红色 则按照替换结点对应父结点的方向旋转（替换结点是父结点的左子结点则左旋） 然后交换父结点和兄弟结点的颜色 再设置兄弟结点不同边结点的颜色为黑色
	4.如果兄弟结点为黑色 但是兄弟结点同边（即替换结点是父结点的左子结点，则对应的是兄弟结点的左子结点）为红色 则先以兄弟结点为支点 以替换结点相对父结点方向的反向旋转（替换结点是父结点的左子结点则右旋）再交换兄弟结点和同边结点的颜色 （制造出情况3） 然后再重新进行判断
	5.如果兄弟结点为红色 其他为黑色 则以父结点为支点 以替换结点相对父结点的方向旋转 在交换父结点和兄弟结点的颜色 再重新判断(制造出情况2)
	6.如果全都是黑色 则设置兄弟结点为红色 再以父结点为替换结点 重新判断（此时父结点所在的所有路线都比其他路线少了一个黑色结点）


#### Redis详细学习
安装过程
首先 wget url下载redis.tar.gz安装包
tar -zxvf 解压安装包
使用make test测试需要的编译环境(应该不需要测试完全 (环境配置不够也会报错))
使用make安装redis
默认相关的redis可执行文件在src文件目录下
默认的redis.conf作为启动redis服务器的配置文件
常用可执行文件
redis-server 服务端
redis-sentinel 哨兵监视运行状态
redis-cli 连接服务端
redis-check-rdb RDB存储检查工具
redis-check-aof Append Only Files(AOF)检查工具
工作原理
Redis实例代表一个redis-server进程，同一台机器可以运行多个Redis实例，只需配置不同即可，比如绑定不同的端口、使用不同路径保存数据持久化相关文件，或采用不同的日志路径等
推荐使用redis-cli连接服务端后使用shutdown终止服务实例，这样操作留给Redis除终止实例外，进行其他操作
首先Redis会停止响应客户端的连接，如果启动持久化，则会执行数据持久化操作，之后 如果pid文件和socket套接字描述符存在的话，则对其进行清理，并最终退出进程。通过这种策略，Redis会尽可能防止数据丢失，粗暴的使用kill来终止进程可能会导致数据尚未持久化而导致丢失
值得注意的是使用kill发送SIGTERM(15)信号基本上等同于使用shutdown终止实例
使用redis-cli连接reids服务实例
redis-cli # 默认连接到127.0.0.1:6379对应的Redis服务实例
-h 指定host
-p 指定端口
-s 指定套接字 TCP或UDP
-a 指定Redis连接密码 在conf文件中配置
获取服务实例的信息
INFO [section]命令
memorys 大致内存信息
sever Redis实例基本信息
clients 客户端连接状态和指标
persistence 数据持久化相关的状态和指标
state 总体统计数据
replication 主从复制相关的状态和指标
CPU CPU使用情况
Cluster 集群状态
keyspace 数据库相关的统计数据
Redis事件模型
# 需要C语言基础 暂时未看
Redis通信协议
echo -e "*2\r\n\$3\r\nGET\r\n\$3\r\nbar\r\n" | nc 127.0.0.1 6379
* 表示这是一个数组类型
2 表示这个数组的容量
\r\n 是RESP中每个部分的终止符
$3 表示以下几个字符组成字符串
## 数据类型
string 
list
hash 
set
sorted set
HyperLogLog
Geo
键管理

## string
1.SET KEY VALUE 设置string值
2.GET KEY 获取string值(不存在的key返回nil)
3.STRLEN KEY 获取string长度(不存在的key返回0）
4.APPEDN KEY APPEND_VALUE 追加string值
5.STRRANGE KEY START CHANGE_VALUE 替换string部分值
6.SETNX 原子性、仅在key不存在时设置键值 如果设置成功则返回1 已经存在则返回0且不覆盖原有值
7.MSET KEY VALUE [KEY VALUE [...]] 用于一次性设置多个键值 优点是整个操作是原子性的，意味着所有键都是客户端和服务端一次通信中设置的 可以使用MSET减少网络开销
*
字符串在Redis中的编码 使用三种不同方式
1.int：用于能够使用64位有符号整数表示的字符串
2.embstr：用于长度小于或等于44字节的字符串（3.X为39字节） 这种类型的编码在内存使用和性能方面更有效率
3.raw：用于长度大于44字节的字符串
*

## list
1.LPUSH KEY value1 value2 ... 向list左端插入若干个值
2.RPUSH KEY value1 value2 ... 向list右端插入若干个值
3.LRANGE KEY start end 显示list中的元素 下标从start到end end为-1时到list结尾
*
list索引 从左到右为0-N 从右到左为-1~-N 0~-1代表整个list
*
4.LINSERT KEY BEFORE|AFTER PIVOT value 在指定value的前后插入数据
5.LINDEX KEY index 获取指定下标的值
6.LPOP KEY 从list左端删除
7.RPOP KEY 从list右端删除
8.LTRIM KEY start end 截取list中的一个子list
9.LSET KEY index value 设置list中 指定下标的value
10.BLPOP KEY timeout 是LPOP的阻塞版本 当列表为空时 将按照指定时间阻塞客户端 0表示永远阻塞 获得数据的顺序按照阻塞的前后顺序
11.BRPOP KEY timeout RPOP的阻塞版本
*
list使用quicklist存储列表对象 有两个相关的配置选项
1.list-max-ziplist-size 一个列表条目中一个内部节点的最大大小 quicklist中的每个节点都是一个ziplist
2.list-compress-depth 列表压缩策略 如果会用到首尾元素 可以利用这个选项获得更好的压缩比
*

## hash
1.HMSET KEY field1 value1 field2 value2 ... 设置hash中若干个字段和对应的值 (M是multi的意思)
2.HMGET KEY field1 field2 ... 获取hash中若干个字段对应的值
3.HSET KEY field value 设置hash中一个字段对应的值 也可以修改已存在的字段
4.HGET KEY field 获取hash中一个字段对应的值
5.HEXISTS KEY field 获取hash中是否存在指定字段
6.HGETALL 获取hash中所有字段和对应的值 (不建议对数量巨大的hash使用)
7.HDEL KEY field1 field2 ... 从hash中删除指定字段的值
8.HSETNX KEY field value 只有不存在的时候才插入字段成功
*
一个hash中最多能够容纳2^32-1个字段
*
9.HSCAN （是Redis中SCAN命令中的一种(SCAN,HSCAN,SSCAN,ZSCAN) 增量地迭代遍历元素 从而不会造成服务器阻塞
HSCAN KEY cursor [MATCH pattern] [COUNT number] 指定一个游标 每次运行结束将返回一个list和一个cursor用于下一次迭代 小的hash时可能无视count参数（默认10） 
*
redis内部使用两种编码来存储哈希对象
1.ziplist 对于长度小于hash-max-zip-list-enteries选项配置的值（默认512）且所有元素大小都小于配置中hash-max-ziplist-value的值（默认64字节） 
2.hashtable 不适用ziplist的hash
*

## set
1.SADD KEY member 添加新成员
2.SISMEMBER KEY member 判断set中是否存在
3.SREM KEY member 删除成员
4.SCARD KEY 集合中成员的数量
*
最多容纳2^23-1个成员
*
5.SMEMBERS KEY 显示所有成员
*
同样可能阻塞服务器 可以使用SSCAN
*
6.SSCAN KEY cursor METCH PATTERN COUNT count 与HSCAN同理
7.SUNION KEY1 KEY2 并集
8.SCIONSTORE KEY1 KEY2 并集并存到另一个set中
9.SINTER KEY1 KEY2 交集 
10.SINTERSTORE KEY1 KEY2 交集并存到另一个set中
11.SDIFF KEY1 KEY2 差集
12.SDIFFSTORE KEY1 KEY2 差集并存到另一个set中
*
redis使用两种编码方式存储set
1.intset 对于元素都是整数 并且元素个数小于set-max-intset-enteries选项设置值(默认512)
2.hashtable 不符合intset的set
*

## sorted set
1.ZADD KEY score member 添加成员并设置分数
2.ZREVRANGE KEY start end WITHSCORES 获取排名
3.ZINCRBY KEY incr_score member 对成员中的一个进行增加分数
4.ZREVRANK KEY member 获取指定成员的排名
5.ZSCORE KEY membner 获取指定成员的分数
6.ZUNIONSTORE destination numkeys key key1... weights weight weight2... AGGREGATE SUM|MIN|MAX 并集并存到另一个sorted set中
7.ZADD KEY [NX] 只添加新成员不更新老成员 [XX] 只更新老成员 不添加新成员 
*
在相同分数的时候 按照字典排序
*

## HyperLogLog
唯一计数 如果不需要获取数据集的内容 只想得到不同值的个数 
1.PFADD KEY value 用户id加到一个HyperLogLog中
2.PFCOUNT KEY  获取不同值的个数
3.PFMERGE KEY KEY2 ... 合并多个HyperLogLog获取不同值的个数
*
HLL实际上作为字符串存储的 两种方式存储HLL
1.稀疏 长度小于hll-sparse-max-bytes选项配置的值（默认3000） 存储效率高 但是消耗更多的CPU资源
2.稠密 不适用稀疏的情况
*

## Geo
1.GEOADD KEY longitude latitude member 设置成员的经纬度
2.GEOPOS KEY member 查询指定成员的经纬度
3.GEORADIUS KEY longitude latitude distance unit 查询指定经纬度指定半径范围内的成员
4.GEODIST KEY member member  unit 查询两个成员之间的距离
5.GEOADIUSBYMEMBER 查询指定成员指定半径范围内的成员（包括自己)
*
GEOADD输入的成员被转换为52位的GEOHASH
实际被存储为一个sorted set 一次有序集合中的所有命令都适用于geo
*

## 键管理
1.DBSIZE 键的个数
2.KEYS pattern 匹配的键
3.SCAN cursor MATCH pattern COUNT count 游标查询 COUNT默认为10
4.DEL KEY 删除key
5.EXISTS KEY key是否存在
6.TYPE KEY key的类型
7.RENAME 重命名

数据特性
## 使用位图 bitmap
位图（也称为位数组和位向量）是有比特位（bit）组成的数组 
1.SETBIT KEY OFFSET 0|1 设置指定偏移量的比特位
2.GETBIT KEY OFFSET 获取指定偏移量的比特位
3.BITCOUNT KEY 获取被设置为1的比特位数量
4.BITOP ADD|OR|XOR|NOT 用于位运算
*
流行密集的适用于bitmap 而稀疏的则适用于集合
*

## 设置键的过期时间
1.EXPIRE KEY TIMEOUT 设置键的过期时间
2.TTL KEY 查看键的过期时间 键不存在返回-2 没有设置过期时间返回-1
*
键的过期时间会被存储为一个绝对的UNIX时间戳 当一个键过期时 当客户端尝试访问时 服务器会将其从内存中删除 这种方式被称为被动过期 还会定期运行一个基于概率的算法进行主动删除 会随机选择设置了过期时间的20个键 已过期的立即删除 如果超过25%被删除则重新选择20个键被重复以上行为
*
3.PERSIST KEY 将键转换为永久键
*
键的值被替换或删除 SET GETSET和 *STORE会清除过期时间 修改列表、集合或哈希的元素不会清除过期时间
*
4.EXPIREAT KEY TIMESTAMP 手动设置一个键的绝对UNIX时间戳作为过期时间

## SORT
1.SROT KEY ALPHA|DESC|ASC LIMIT START MAX 

## 使用管道
通过RESP协议通信 客户端和服务器之间典型的通信过程为
1.客户端向服务器发送一个命令
2.服务器接受该命令并将其放入执行队列（Redis是单线程的执行模型）
3.命令被执行
4.服务器将命令执行的结果返回给客户端
上述消耗的时间成为往返时延RTT(round-trip time)
使用
命令 | redis-cli --pipe -a password 将RESP协议输入到redis客户端中

## Redis事务
MULTI 开始一个事务
WATCH key 设置一个标准 判断在执行exec之前是否修改过键
DESCARD 放弃事务
EXEC 提交事务
*
1.事务中有语法错误则整个事务会快速失败且事务中的所有命令都不会被处理
2.所有命令都已成功入队 但是执行过程中发生错误 位于之后的命令会执行 不会回滚
*

## 发布订阅
订阅
SUBSCRIBE channel
发布
PUBLISH channel message

PUBSUB CHANNELS 用于频道管理 查看当前活跃的频道
*
没有订阅的频道自动删除
channel相关的数据不会持久化到磁盘中
*

主从复制
1.在从服务器的配置文件中添加
slaveof host port 
masterauth password (如果主服务器设置了密码 需要配置)


持久化
RDB Redis在某个时间点上的快照 适用于备份和灾难恢复
AOF 写入操作的日志 在服务器启动时被重放

## RDB
配置文件中
save xx xx xx 如果save为空或被注释 则不启动RDB持久化 
*
save 900 1 表示900秒中修改过一次键即触发持久化
*
会在配置文件目录下生成一个dump.db文件
SAVE redis命令 主动触发持久化
BGSAVE 非阻塞的持久化
*
save阻塞时持久化 
bgsave则非阻塞持久化 主线程继续处理命令 同时fork()出一个子进程保存到temp-<bgsave-pid>.rdb的临时文件中 完成后重命名并覆盖旧的转储文件
*
*
相关配置
stop-writes-on-bgsave-error yes 作为保护机制 当bgsve失败时 服务器停止接受写入操作
rdbcompression yes 压缩可以显著减少转储文件大小
rdbchecksum yes 在快照文件的末尾创建一个CRC64校验和
*

## AOF (append-only file)
配置文件中
appendonly yes 启动aof 将生成一个appendonly.aof文件
*
操作系统实际上维护了一个缓冲区 reids命令首先写入这个缓冲区 而缓冲区的数据必须被刷新到磁盘中才会被永久保存 这个过程是通过fsync()完成的 这是一个阻塞调用
appendfsync 来调整fsync()的频率
appendfsync always 每个写入命令都调用fsync
appendfsync everysec 一秒一次
appendfsync no 永不调用 由操作系统决定何时调用 通常为30秒
服务器shutdown时 fsync()将被主动调用
*

配置高可用和集群

#配置Sentinel
Sentinel充当Redis主实例和从实例的守卫者 因为哨兵也可能失效 因此至少需要3个哨兵进程才能构成一个健壮的分布式系统
首先复制sentinel.conf.default为一个自定义的配置文件
修改相关的配置
使用
redis-server sentinel.conf --sentinel 启动哨兵服务器
*
sentinel monitor <master-name> <ip> <port> <quorum> quorum 指在故障转移前至少同意的哨兵数
down-after-milliseconds 指在标记实例下线之前不可达的最长毫秒数 Sentinel每秒都会PING监视的服务器实例
parallel-syncs 表示有几个实例可以同时从新的实例进行数据同步
*

## 管理哨兵
1.SENTINEL GET-MASTER-ADDR-BY-NAME <master-name> 获取主实例的信息
2.SENTINEL MASTERS 获取所有被监控主实例的状态
3.SENTINEL SLAVES <master-name> 获取监视主实例的所有从实例

## 配置Redis Cluster
需要在配置文件中修改
daemonize yes 守护进程运行 后台运行
port port_num
pidfile ""
logfile ""
dbfilename ""
cluster-enabled yes
cluster-config-file filename
cluster-node-timeout 10000
配置完后按照启动服务器启动即可
redis-server redis-xxx.conf 
启动多个服务器后
再进入redis-cli使用CLUSTER MEET HOST PORT让集群发现彼此即可
CLUSTER MEET HOST PORT 
使用CLUSTER NODES查看集群状态
CLUSTER NODES
还需要分配数据槽给主服务器
CLUSTER ADDSLOTS [1-16383] 注意的是需要分配所有的槽 集群才会运行
还需要设置主服务器和从服务器
CLUSTER REPLICATE id 将当前服务器设置为指定id服务器的从服务器(id从CLUSTER NODES中可以查到 )
CLUSTER NODES 查看集群启动情况

值得注意的是 从服务器的写需要启动时添加-c参数 否则读操作会失败
Redis-cli -h host -p port -c 



### 10分钟详解Spring全家桶7大知识点
## 5个常用的Spring框架
1.Spring framework
包括IOC依赖注入，Context上下文，bean管理，SpringMVC等众多功能模块
2.Spring Boot
目的是简化Spring应用和服务的创建，开发与部署，简化了配置文件，使用嵌入式web服务器，含有诸多开箱即用的微服务功能，可以和Spring Cloud联合部署
Spring Boot的核心思想是约定大于配置，应用只需很少的配置即可，简化了应用开发模式
3.Spring Data
是一个数据访问及操作的工具集，封装了多种数据源的操作能力，包括：jdbc、Redis、MongoDB等
4.Spring Cloud
是一套完整的微服务解决方案，是一系列不同功能的微服务框架的集合。Spring Cloud基于Spring Boot，简化了分布式系统的开发，集成了服务发现、配置管理、消息总线、负载均衡、断路器、数据监控等各种服务治理能力。比如sleuth提供了全链路追踪能力，Netflix套件提供了hystrix熔断器，zuul网关等众多治理组件。config组件提供了动态配置能力，bus组件支持使用RabbitMQ、kafka、Activemq等消息队列，实现分布式服务之间的时间通信
5.Spring Security
主要用于快速构建安全的应用程序和服务，在Spring Boot和Spring Security OAuth2的基础上，可以快速实现常见安全模型，如单点登录，令牌中继和令牌交换。
## 相关框架
1.Struts2
曾经火爆的控制层，采用Filter实现，针对类进行拦截，每次请求会创建一个Action
2.ORM框架
Hibernate和Mybatis（iBatis）
Hibernate对数据库结构提供了完整的封装，实现了POJO对象与数据库表之间的映射，能够自动生成并执行SQL语句，只要定义了POJO到数据库表的映射关系，就可以通过Hibernate提供的方法完成数据库操作 
mybatis通过映射配置文件，将SQL所需的参数和返回的结果字段映射到指定对象，不会自动生成sql，需要自定义sql语句，不过方便优化。
3.Netty
是一个高性能的异步事件驱动的网络通信框架，Netty对JDK原生NIO进行封装，简化了网络服务的开发
4.RPC服务
Motan、Dubbo、gRPC都是常用的高性能rpc框架

## Spring知识点-详解
1.IOC
Spring中，对象的属性由对象自行创建，就是正向流程，如果属性不是对象创建，而是有Spring来进行封装，就是控制反转。DI就是依赖注入，是实现控制反转的方式。正向流程导致了对象之间的高耦合，IOC可以解决对象耦合的问题，有利于功能的复用，能够使程序的结构变得非常灵活。
2.context上下文和bean
所有被spring管理的，有Spring创建、用于依赖注入的对象，就叫做bean，Spring创建并完成依赖注入后，所有bean被统一放在一个叫做context的上下文中进行管理
3.AOP
就是面向切面编程，对不同服务的纵向处理流程进行横切，在每个切面上完成通用的功能，如身份认证、验证参数、处理异常等。
AOP以功能进行划分，对服务顺序执行流程中的不同位置进行横切，完成各服务共同需要实现的功能。

## Spring框架


## Spring中的机制和实现
1.AOP
是通过代理模式,在调用对象的某个方法时，执行插入的切面逻辑。实现的方式有动态代理，比如JDK代理，CGLIB；也有静态代理，使用AspectJ
2.placeHolder动态替换
3.事务
4.核心接口类
ApplicationContext保存IOC的整个应用上下文，可以通过其中的beanFactory获取到任意的bean
BeanFactory主要的作用是根据bean definition来创建具体的bean
BeanWrapper是对Bean的包装 一般情况下是在Spring Ioc内部使用 提供了访问bean的属性值、属性编辑器注册、类型装换等功能，方便ioc容器用同一的方式来访问bean的属性
FactoryBean通过getObject方法返回bean对象
5.Scope
bean的作用域，默认情况下是单例模式，还有多例模式
Singleton
Prototype
Request
Session
Application
Websocket
6.事件机制
ContextRefreshedEvent
ContextStartedEvent
ContextStoppedEvent
ContextClosedEvent
RequestHandledEvent

## Spring应用相关
1.常用注释
a.类型类注解
@Component 类上表明这个类是组件类，需要Spring为这个类创建bean
@Bean 方法上，告诉Spring这个方法返回一个Bean对象
@Service
@Controller
@Repository
b.设置类注解
@Autowaire
@Qualifier 
c.web类注解
@RequestMapping
@GetMapping
@PostMapping
@PathVariable
@RequestParam
d.功能类注解
@ImportResource
@ComponseScan
@Transactional




### SpringMVC (WEB)
## 面试问题
跨域请求
private CorsConfiguration buildCorsConfiguration() {
	CorsConfiguration corsConfiguration = new CorsConfiguration();
	corsConfiguration.addAllowedOrigin(""); // 来自何处的跨域请求可通过
	corsConfiguration.addAllowedHeader(""); // 哪些请求头可以通过
	corsConfiguration.addAllowedMethod(""); // 哪些请求方法可以通过
	corsConfiguration.addAllowedCredentials(true); // 是否可以携带cookie
	corsConfiguration.setMaxAge();
	return corsConfiguration;
	
}

@Bean
public CorsFilter corsFilter() {
	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	source.registerCorsConfiguration("/**", buildCorsConfiguration());
	return new CorsFilter(source);
}


统一异常处理
@ControllerAdvice
public class CustomExceptionHandler {
	@ExcpetionHandler(value = Exception.class)
	@ResposneBody
	public Map<String, Object> jsonExceptinHandler(HttpServletRequset re) 
}


Filter使用
两种方式
1.
public class CustomFilter implements Filter {
	public void init(FilterConfig filterConfig) {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		filterChain.doFilter(request, response); // 这步不能少
	}
	
	public void destroy() {
	
	}
}
再创建类为FilterRegistrationBean的Bean
@Configuration 
public CustomFilterConfiguration {
	@Bean
	public FilterRegistrationBean<Filter> registerCustomFilter() {
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new CustomFilter());
		bean.setUrlPattens(Arrays.asList("/*"));
		bean.setName("CustomFilter");
		bean.setOrder(1);
		return bean;
	}
}
2.
使用@WebFilter和@Order注解
@WebFilter(urlPattern={"/*"}, filterName="CustomFilter2")
@Order(2)
public class CustomFilter2 implements Filter {
	public void init(FilterConfig filterConfig) {
		
	}
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		filterChain.doFilter(request, response); // 这步不能少
	}
	
	public void destroy() {
	
	}
}
需要配合@ServletComponentScan(basePackages = {"xxx, yyy"})扫描到Filter所在的包路径
Interceptor使用
先实现HandlerInterceptor
public class CustomHanlderInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object hanlder) {
		return false;
	}
	
	public void postHanlder(HttpServletRequest requst, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
	
	}
	
	public void afterCompletion(HttpServletRequest reqiest, HttpServletResponse response, Exception e) {
	
	}

}
然后继承WebMvcConfigureAdapter(Spring Boot2之前) WebMvcConfigureSupport (2之后)
public CustomWebMvcConfigureAdapter/Support extends WebMvcConfigureAdaptor/Support {
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CustomHandlerInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
}

### JDK1.8
1.default关键字
使用default修饰 可以在接口中定义默认方法
public interface DefaultInterface {
	void hello();
	default void hello(String name) {
		System.out.println("Hello" + name);
	}
}
2.Lambda表达式
函数式编程
带参数类型
(Comparator<? super String>) (o1, o2) -> {
	return o1.compareTo(o2);
}
无参数类型
(o1, o2) -> {
	return o1.compareTo(o2);
}
3.函数式接口
指仅仅包含一个抽象方法的接口，每一个该类型的lambda表达式都会匹配到这个抽象方法 使用@FunctionalInterfacce 多于一个抽象方法将报错
@FunctinalInterface
public interface CustomFunctionalInterface {
	String randomString();
}
public class UseCustomFunctinalInterface {
	public void printString(CustomFunctionalInterface cfi) {
		System.out.println(cfi.randomString());
	}
	public static void main(String[] args) {
		UseCustomFunctinalInterface ucfi = new UseCustomFunctinalInterface();
		ucfi.printString(()->{
			return new String(new Random().nextBytes());
		});
	}
}
4.方法和构造函数引用
调用方式:: 需要使用方法引用时 目标引用放在分隔符::前 方法名放在后面即ClassName::MethodName 方法引用就是Lambda表达式的简写
比如
Apple::getWeight = (Apple)(a) -> a.getWeight();
也可以使用new
User::new
5.局部变量限制
Lambda表达式允许使用自由变量（不是参数，而是在外层作用域中定义的变量） 被称为捕获Lambda Lambda可以没有限制地捕获实例变量和静态变量 而局部变量必须显式声明为final 或实际上是final
6.Date Api更新
A.LocalDate/LocalTime/LocalDateTime
以下静态方法
now方法获取当前日期和事件 
of方法可以创建对应的日期或时间
parse方法可以解析日期或事件
以下实例方法
get方法可以获取日期或事件信息
with方法可以设置日期或事件信息
plus或minus方法可以增减日期或事件信息
B.TemporalAdjusters
在调整时非常有用 比如得到当月第一天 最后一天 当年第一天 最后一天 下周某一天等
C.DateTimeFormatter
默认定义很多常量格式
7.流
Java API的新成员 允许以声明式方法处理数据集合 可以看作遍历数据集的高级迭代器
Stream不是集合元素 不是数据结构也不保存数据 只与算法和计算相关
Stream就如同一个迭代器（Iterator） 单向 不可往复 只能遍历一次
流的操作类型分为两种
A.Intermediate (中间的)
一个流可以跟随若干个intermediate操作 目的是打开流 做出某种程度的数据映射/过滤 然后返回一个流 交给下一个操作使用 这类操作是惰性的 
B.Terminal (末端的)
一个流只能有一个terminal 当这个操作执行后 流就被使用完毕 所以必定是最后一个
流构建的几种方式
Stream.of(...)
Stream.of(arr)
Stream.iterator(seed, lambda)
Arrays.stream(arr)
list.stream()
中间操作 intermediate
filter`
limit
skip
distinct
forEach
map
sorted
终止操作 terminal
allMatch 检查是否匹配所有元素
anyMatch 检查是否有匹配
noneMatch 检查是否没有匹配
findFirst 返回第一个元素
findAny 返回任意元素
count 返回个数
max 最大值
min 最小值
reduce 可以将流中的所有元素反复操作 得到最终值
collect 将流转换为其他形式 接受一个Collection接口的实现

### JDK1.5
1.泛型
2.枚举
3.自动装箱拆箱
4.可变参数
5.注解
6.foreach循环（增强for、for/in)
7.静态导入
8.格式化（System.out.println支持%s%d等格式化输出)
9.线程架构/数据结构JUC
线程池
uncaught exception
blocking queue(BlockingQueue)
JUC库类
10.Arrays工具类/StringBuilder/instrument

### JDK1.6
1.Web Services
2.Scripting 开始JS支持
3.Database
4.More Desktop APIs
5.Monitoring and Management
6.Compiler Access 
提供编程访问javac 可以实现进程内编译 动态产生代码
7.Pluggable Annotation
8.Desktop Deployment
9.Security
10.The-ilities

### JDK1.7
1.switch添加对String的支持
2.数据字面量的改进/数值可加下划
3.异常处理（捕获多个异常）try-with-resources
try {

} catch (NumberFormateException | RuntimeException e) {

}
4.增强泛型推断
5.NIO2.0（AIO）新IO的支持
6.InvokeDynamic指令
7.Path接口 DirectoryStream Files WatchService
8.fork/join framework

### JDK1.9
平台级modularity(原名Jigsaw)模块化系统
2.Java的REPL工具 jShell命令
3.多版本兼容的jar包
4.语法改进 接口的私有方法
可以是static也可以不是 看用在public static方法中还是default
私有方法只能用private修饰
5.语法改进 UnderScore使用的限制
6.底层结构 String存储结构变更
JDK8之前字符串存储在char类型的数组中 JDK9则使用byte[]数组并加上编码标记 节约空间
StringBuilder和StringBuffer也受到影响
7.集合工厂方法 快速创建只读集合
List.of()
Set.of()
Map.of()
JDK8之前为
Collections.unmodifiableList
Collections.unmodifiableSet
Collections.unmodifiableMap
8.增强的Stream API
9.全新的HTTP客户端API
10.其他特性


### JDK10
1.局部变量的类型推断var关键字
2.GC改进和内存管理 并行全垃圾回收期G1
3.垃圾回收期接口
4.线程-局部变量管控
5.合并JDK多个代码仓库
6.新增API ByteArrayOutputStream
7.新增API List、Map、Set
8.新增API java.util.Properties
9.新增API COllectors收集器
10.其他

### JDK11 LTS(long-time-supported 长期支持版本)
1.本地变量类型推断
2.字符串加强
3.集合加强
4.Stream加钱
5.Optional加强
6.InputStream加强
增加transferTo 将InputStream数据直接传输到OutputStream中
7.HTTP ClientAPI

8.化繁为简 一个命令编译运行源代码


### MongoDB
## 安装
在/etc/yum.repos.d/目录下创建mongodb-org-3.4.repo文件
再在mongodb-org-3.4.repo中添加
[mongodb-org-3.4]
name=MongoDB Repository
baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/3.4/x86_64/
gpgcheck=1
enabled=1
gpgkey=https://www.mongodb.org/static/pgp/server-3.4.asc
此时就可以使用yum install -y mongodb-org安装mongodb了
yum install -y mongodb-org
此时安装了mongod.service
使用
systemctl start/stop/restart mongod.service 
service mongod start/stop/restart 
启动/停止/重启mongodb服务
## mongo shell
使用mongo进入mongodb客户端shell模式
db # 显示当前使用数据库
use <database> # 切换数据库
show dbs # 列出可用数据库

quit()退出
## CRUD操作
# 创建操作
db.collection.insert()
db.collection.insertOne()
db.collection.insertMany()
插入操作作用于单个集合collection 所有写操作在单个集合document的层级上是原子性的

插入操作的行为表现
1.创建集合
插入的时候如果集合不存在 那么插入操作会创建集合
2._id字段
存储于集合中的每一个文档document都需要一个唯一的_id字段作为primary_key 如果一个插入文档操作遗漏了_id字段 MongoDB驱动会自动为_id字段生成一个ObjectId
这种情况同样适用于通过带有参数upsert:true的update操作来插入文档的情况
3.原子性
所有写操作在单个文档上的是原子性的
A.db.collection.insertOne() 3.2新版功能
例子
db.users.insertOne({
	name: 'sue',
	age: 19,
	status: 'p'
})
insertOne()返回一个结果文档 中列举了插入文档的_id字段值 
B.db.collection.insertMany()
db.users.insertMany(
	[
		{name: 'bob', age: 42, status: 'A'},
		{name: 'ahn', age: 22, status: 'A'},
		{name: 'x1', age: 34, status: 'D'}
	]
)
C.db.collection.insert()
向集合插入一个或多个文档 传递一个文档或者一个数组
插入一个文档返回一个包含状态信息的WriteResult对象
WriteResult({ "nInserted" : 1}) 插入文档的总数 如果操作出现错误则包含错误信息
插入一个文档数组返回BulkWriteResult对象 
BulkWriteResult({
   "writeErrors" : [ ],
   "writeConcernErrors" : [ ],
   "nInserted" : 3,
   "nUpserted" : 0,
   "nMatched" : 0,
   "nModified" : 0,
   "nRemoved" : 0,
   "upserted" : [ ]
})


# 读操作
读操作获取集合colleciton中的文档document
db.collection.find()
可以指定条件或者过滤器找到指定的文档

1.查询方法
db.collection.find(<query filter>, <projection>)
对于cursor方法 可以指定下列可选字段
一个query filter指定返回哪些文档
一个查询映射来指明返回匹配文档的哪些字段
可以随机增加一个游标修饰来进行限制、跳过以及排序 sort()方法指定返回顺序

选择集合中所有文档
db.collection.find()
db.collection.find({})
指定查询过滤条件
<query filter>可以使用<field>:<value>表达式指定等于条件以选择所有包含<field>字段并等于特定<value>的所有文档
{<field>:<value>, ...}
比如
db.collection.find({status: 'A'})
使用查询操作符指定条件
{<filed>: {<operator>: <value>}, ...}
比如
db.users.find({status: {$in: ['P', 'D']}})
指定AND条件
复合查询可以在集合文档的多个字段上指定条件 
db.users.find({status: 'A', age: {$lt: 30}})
指定OR条件
通过使用$or操作符
db.users.find({
	$or: [{status: 'A'}, {age: {$lt: 30}}]
})
指定AND和OR条件
db.users.find({
	status: 'A',
	$or: [{age: {$lt: 30}}, {type: 1}]
})
嵌入文档上的精确匹配
使用{<field>: <value>}且<value>为要匹配文档的查询文档，来指定匹配整个内嵌文档的完全相等条件(要使)相等条件匹配上内嵌文档需要指定<value>包含字段顺序的精确匹配
在下面的例子中,查询匹配所有 favorites 字段是以该种顺序只包含 等于 "Picasso"``的 ``artist 和等于 "pizza" 的 food 字段的内嵌文档：
db.users.find({favorites: {artist: 'Picasso', food: 'pizza'}})
嵌入文档中字段上的等于匹配
包含指定字段并等于指定值的文档 内嵌文档可以包含其他字段
db.users.find({'favorites.food': 'pizza'})
数组上的查询
当字段包含数组 可查询精确匹配数组或数组中特定的值
如果使用$elemMatch操作符指定多个查询条件 数组必须包含至少一个元素满足所有条件
如果没使用$elemMatch草祖父 必须满足所有条件
数组上的精确匹配
{<field>:<value>}<value>是匹配的数组 则查找的数组必须和value完全相符
db.users.find({badges: ['blue', 'black']})
匹配一个数组元素
至少一个包含特定值就可以匹配
db.users.find({badges: 'black'})
匹配数组中的指定元素
db.users.find({'badges.0': 'black'}) 找到包含badges数组且第一个为black的文档
指定数组元素的多个查询条件
单个元素满则查询条件
使用$elemMatch操作符为数组元素指定符合条件 以查询数组中至少一个元素满足所有指定条件的文档
db.users.find({finished: {$elemMatch: {$lt: 30, $gt: 20}}})
元素这满足查询条件
db.users.find({finished: {$lt: 20, $gt: 15}})

返回查询的映射字段
1.映射文档
限制返回所有匹配文档的字段
{field1: <value>, field2: <value>}
<value>可以的形式
1或true表示包含该字段
0或false表示排除该字段
使用Projection Operators的表达式
(_id默认包含）
2.返回匹配文档的所有字段
db.users.find({}) 
3.只返回指定的字段
db.users.find({}, {staus: 1, name: 1}) -> 返回{_id: xx, name: xx, status: xx} _id是默认返回的 除非显式的设置为不返回
4.返回除了排除字段之外的所有字段
db.users.find({}, {points:0, favorites: 0}) -> 返回不包含points和favorites以外的其他字段
除了_id 不能再映射字段中同时包含和排序语句
5.返回嵌入文档中的指定字段
db.users.find({}. {name: 1, status: 1, "favorites.food": 1})
6.排除嵌入文档中的特定字段
db.users.find({}, {"favorites.food": 0})
7.映射数组中的嵌入字段
db.users.find({}, {"points.bonus": 1}) -> 返回数组points中包含bonus字段的嵌入文档
8.映射返回书中特定的数组元素
对于包含数组的字段 提供了映射操作符 $elemMatch $slice $
db.users.find({}, {points: {$slice: -1}}) 查询points数组的最后一个

查询值为Null或不存在的字段
相等过滤器
db.users.find({name: null})
会查出null和不存在此字段的文档 (sparse索引则不会查出不存在字段的文档)
类型筛查
db.users.find({name: {$type: 10}}) $type: 10表示BSON类型中的Null
存在性筛选
db.users.find({name: {$exists: false}})

迭代游标
手动迭代
var myCursor = db.users.find({})
while(myCursor.hasNext()) {
	print(tojson(myCursor.next()));
}
或
myCursor.forEach(printjson);

迭代器索引
使用:method:`~cursor.toArray()方法来迭代游标 并且以数组的形式返回文档
var myCursor = db.users.find()
var documentArray = myCursor.toArray()
var myDocument = documentArray[3]
有些驱动可以通过游标上使用索引来提供访问文档的方法(cursor[index])


# 更新操作
修改集合collection中已经存在的文档document
db.collection.update()
db.collection.updateOne()
db.collection.updateMany()
db.collection.replaceOne()
更新操作作用于单个集合 所有写操作在单个文档document层级上是原子性的
可以指定条件或过滤器找到更新的文档 与读操作一致

行为表现
1.原子性
2._id字段 
一旦设定不能修改
3.文档大小
当执行更新增加的文档大小超过了为该文档分配的空间时  更新操作会在磁盘上重定位该文档
4.字段顺序 
按照文档写入的顺序整理文档字段 
_id字段始终为第一个
包括字段名称的renaming操作可能导致字段重排
5.upsert选项
包含upsert:true且没有匹配文档则创建一个新的文档 若有则更新

更新指定字段
db.collection.updateOne() 有多个匹配也只更新一个
db.users.updateOne(
	{"favorites.artist":"Picasso"}, 
	{
		$set: {"favorites.food": "pie", type: 3},
		$currentDate: {lastModified: true}
	}
)
db.collection.updateMany() 更新所有匹配文档
db.collection.update() 更新匹配的第一个文档
使用{multi: true}更新多个文档
文档替换
更新除了_id之外的所有内容 
db.collection.replaceOne()
db,collection.update() 不使用$set操作符


# 删除操作
db.collection.remove()
db.collection.delete1One()
db.collection.deleteMany()
作用于单个集合 所有写操作在单个文档document上是原子性的
可以指定条件或过滤器找到要删除的文档 使用与读操作一致

删除所有文档
使用
db.collection.remove({})
db.collection.deleteMany({})
db.collection.drop() 删除整个集合 包括索引
删除满足条件的所有文档
db.collection.deleteMany({<query filter>})
db.collection.remove({<query filter>})
删除满足条件的一个文档
db.collection.deleteOne({<query filter>})
db.collection.remove({<query filter>}, 1)

CRUD概念
1.原子性和事务处理
一个写操作的原子性是基于单个文档的
可以使用:update:$isolated操作将多个写操作隔离为单个写操作
2.读隔离、一致性和时近性
隔离保证
读无限制
单个文档原子性
多个文档写
3.分布式查询
复制集从节点成员上的读操作并不能保证能够反映出主节点当前的状态 因为从节点的状态会落后主节点一段时间 很多情况下 不能依赖于这种类型的严格一致性
4.分布式写操作
5.执行两个阶段的提交

## 聚合
db.collection.aggregate([])
db.collection.mapReduce(map function, reduce function, {})
db.collection.count()
db.collection.distinct()
# 聚合管道
db.collection.aggregate([])
比如
db.users.aggregate([
	{$match: {status: 'a'}},
	{$group: {_id: '$_id', max: {$max: '$age'}} # 需要一个_id指定分组的依据字段
])
管道表达式只可以操作当前管道中的文档 不能访问其他文档 表达式操作可以在内存中完成对文档的转换
聚合管道的特点
aggregate命令会把整个集合中的文档传入聚合管道 可以使用一下策略优化 避免扫描整个集合
$match $sort $limit $skip 
使用$group操作符的累计操作 需要在管道处理文档的过程中维护自己的状态（例如总数、最大最小值和相关数据等)

# 聚合管道的优化
预测优化
仅使用文档中的一部分字段就可以完成聚合 减少进入管道的数据量
$match + $sort 顺序优化 $match优先
{$match: {status: 'A'}},
{$sort: {age: -1}}
$skip + $limit 顺序优化 $limit优先
{$limit: 15},
{$skip: 10}
$redact + $match 顺序优化

# 聚合管道限制
结果集大小限制
内存大小限制

## 映射化简
将大量数据转换为聚合结果的数据处理方式
db.collection.mapReduce(map function, reduce function, {})

例子
db.orders.mapReduce(function() {return emit(this.cust_id, this.amount)}, function(key, values) {return Array.sum(values)}, {query: {status: 'A'}, out: 'test_mapReduce'})
以上例子是将status字段为A的文档取出 根据cust_id分组 cust_id为key amount组合一个数组作为values 然后计算amount的总数 得到一个_id为cust_id，value为amount总数的新collection

## 文本索引
支持在字符串内容上执行文本检索的查询操作 使用text index和$text操作符
创建索引
db.stores.createIndex({name: 'text', description: 'text'})
$text操作
db.stores.find({$text: {$search: 'java coffee shop'}}) # 在有text index的集合上搜索包含java coffee shop字符串的文档
精确检索
db.stores.find({$text: {$search: "java \"coffee shop\""}}) # 双引号才有效
词语排除
在字符串前加上-即可查找不包含次字符串的文档
db.stores.find({$text: {$search: "java coffee -shop"}}) # 不包含shop的
排序
默认返回为排序 必须显式对$meta "textScore"字段进行映射然后基于此字段进行排序
# 文本检索
提供了文本索引支持字符串内容上的文本检索查询 文本索引可以包括任何值是字符串或者字符串元素数组的字段
必须在集合上有一个文本索引 一个集合只能有一个文本索引 但是能覆盖多个字段
db.stores.createIndex({name: "text", description: "text"})
# 文本检索操作符
查询框架
使用$text查询操作符在有文本索引的集合上执行文本检索
$text将会使用空格和大部分标点符号作为分隔符对检索字符串进行分词 然后对检索字符串中所有的分词执行一个逻辑的OR操作
db.store.find({$text: {...}})
$meta查询操作符获得并且根据每个匹配文档的相关分数进行排序
{score: {$meta: "textScore"}}
# 在聚合管道中使用文本搜索
可以通过$text在$match中使用文本搜索
1.限制
必须是管道中的第一阶段
$text只能出现一次
不能出现在$or和$not表达式中
默认不会排序
2.文本得分
$text会对被索引的键上含有搜索词的每个文档打分 显示了该文档和给定文本搜搜查询的相关性
db.articles.aggregate([
	{$match: {$text: {$search: "xxx"}}},
	{$sort: {score: {$meta: "textScore"}}},
	{$project: {title: 1, _id: 0}} # 设定返回的字段
])

## 数据模型
# 数据模型设计结构
关系型数据库要求插入数据前必须定义好一个表的模式结构 MongoDB则并不限制document结构 这种灵活性让对象和数据库文档之间的映射变得容易 
1.文档结构
References 引用
通过存储连接或者引用来实现两个不同文档之间的关联
Embedded Data 内嵌
把相关联的数据保存在同一个文档结构之中

## Administration

## 索引
1.默认_id索引
2.创建索引
db.collection.createIndex(<key and index type specification>, <option>)
3.索引类型
单一
{score: 1}
复合
{userid: 1, score: -1}
复合键
{"addr.zip": 1}

# 单键索引
db.records.createIndex({score: 1})
# 复合索引
db.records.createIndex({<filed1>: <value1>, <field2>:<value2>...})
# 多键索引
索引一个储存数组的键 对数组中的每个元素都添加索引项 会自动决定是否需要创建一个多键索引 

## 存储

## 安全

## 复制
复制集是由一组mognod实例所组成的 并提供了数据冗余与高可用性
主节点
从节点 通过应用主节点传来的数据变动操作来保持其数据集与主节点的一致
投票节点 不包含数据 但是一旦主节点不能使用投票节点就会参与到新的主节点选举中
一个复制集至少含有1个主节点 一个从节点和一个投票节点 但是大部分情况下 会保持3个拥有数据集的节点 一个主节点 2个从节点

# 复制集主节点
主节点是唯一能够接受写请求的节点 MongoDB在主节点上进行写操作 并将操作记录到主节点的oplog中 从节点会将oplog复制到其本机并将这些操作应用到自己的数据集上
复制集中的任何成员都可以接受读请求 但默认情况下 应用程序会直接连接到主节点上进行读操作
复制集中最多只能有一个主节点

# 复制集从节点
数据集与主节点一致 将主节点的oplog复制过来 并异步的将操作应用到数据集上
从节点可以进行读操作
一旦主节点不可用 则触发选举机制 从剩下的从节点推举一个作为新的主节点
可以通过修改参数 将从节点用于特殊的需求
1.可以禁止从节点升职为主节点的方式将该节点永驻为从节点或用于冷备
2.可以通过禁止应用在该从节点上进行读操作的方式 让一些需要进行流量隔离的应用在其上进行读
3.可以通过设置延迟备份节点的方式防止误删等误操作或错误

# 优先级为0的复制集成员
一旦优先级为0 则永远不能升职为主节点 

# 隐藏节点
客户端不会把读请求分发到隐藏节点上 可以将隐藏节点专用于报表节点或备份节点 另外延迟节点也应该是一个隐藏节点

# 延时节点
也将从主节点上复制数据 然而延迟节点中的数据集 将会比复制集中主节点的数据延后 可以帮助在人为无操作或其他以外情况下回复数据

# 投票节点 
并不含有数据集副本 也无法升职为主节点

# 复制集Oplog
大小
大多数情况下 默认是足够的 
可以通过设置oplogSizeMB选项来设定其大小 

# 复制集的数据同步
初始同步会将完整的数据集复制到各个节点上 当一个节点没有数据的适合就会进行初始同步 
过程
1.复制所有的数据库 mongod会查询所有的表和数据库 然后将所有数据插入备份中 同时也会建立_id索引
2.完成所有索引建立 该节点就会变成正常的状态

# 复制集架构
最基本的复制架构由3个成员组成
策略
决定复制集的成员个数
可以通过开启journaling功能来在服务以外关闭或掉电时保护数据
复制集的成员个数应该为奇数个
为特殊需求使用隐藏节点和延时节点
以读为主的架构的负载均衡

# 例子 三个节点的复制集

# 复制集的高可用
复制集选举
复制集成员每两秒钟想复制集中的其他成员进行心跳检测 某节点在10秒内没有返回就标记为不可用
如果复制集中的某个节点不能连接上其他多数节点 将不能升职为主节点 指的是多数投票而不是多数节点个数
如果复制集由3个节点组成 切都可投票 只要其中两个可以相互沟通就可以选举出主节点 如果两个节点不可用 则剩下的将为从节点 因为不能跟大多数节点沟通 如果两个从节点不可用 主节点一样降为从节点

# 故障切换时的回滚 
之前的主节点在故障切换后重回复制集时将会发生写操作的回滚 回滚只会发生在写操作没能成功在从节点上应用就DOWN吊的情况下 当重新加入 将进行回滚以保证与其他成员保持一致

# 主从复制
mongod --master --dbpath=xxx --fork --logpath=xxx
mongod --slave --source <master-host><:master-port> --dbpath=xxx --fork --logpath=xxx

rs.pringReplicationInfo()

# 部署复制集
配置
在启动的时候指定存储在/etc/mongodb.conf或其他地方中的配置文件 并在部署前创建db目录和log目录
详细步骤
mongod --replSet "rs0"
mongod --config <path-to-config>
mongo 
rs.initiate()
rs.conf()
rs.add("host:port")
rs.add("host:port")
rs.status()

同一环境配置一个3个节点的复制集
首先创建db目录和log目录
mkdir /usr/local/mongodb/rs0/db/db-27017 [8/9]
mkdir /usr/local/mongodb/rs0/log/log-27017 [8/9]
然后使用命令启动mongodb服务
mongod --port 27017 --dbpath /usr/local/mongodb/rs0/db/db-27017 --replSet rs0 --fork --logpath /user/local/mongodb/rs0/log/log-27017/logs # 其他配置自行添加
同理启动27018/9端口对应的mongodb服务
然后mongo连接一台服务器
使用
var rsConf = {
	_id: "rs0",
	memebers: [
		{
			_id: 1,
			host: "127.0.0.1:27017"
		}
	]
}
rs.initiate(rsConf)
此时Shell将切换到集群模式
rs.conf()查看集群配置
然后使用
rs.add('127.0.0.1:27018/9')添加集群中的另外两个服务
此时3个节点集群搭建完成 
rs.status()查看集群状态

# 添加投票节点
rs.addArb("host:port") 添加投票节点

# 移除节点
rs.remove("host:port")
rs.reconfig({}) 移除节点 移除rsConfig中的members数组中想要移除的节点即可


# 替换结点
cfg = rs.conf()
cfg.members[x].host = "host"
cfg.members[x].port = "port"
rs.reconfig(cfg)

# 修改节点优先级
cfg = rs.conf()
cfg.members[x].priority = "priority"
rs.reconfig(cfg)

# 禁止升职为主节点
cfg = rs.conf()
cfg.members[x].priority = 0
rs.reconfig(cfg)

# 禁止升职为主节点
cfg = rs.conf()
cfg.members[x].priority = 0
cfg.members[x].hidden = true
rs.reconfig(cfg)

# 配置延时节点
cfg = rs.conf()
cfg.members[0].priority = 0
cfg.members[0].hidden = true
cfg.members[0].slaveDelay = 3600
rs.reconfig(cfg)

# 配置一个不参与投票的节点
cfg = rs.conf()
cfg.members[x].votes = 0
rs.reconfig(cfg)

## 复制集维护教程 *暂时没看*

## 分片
#分片
分片是存储集群一部分数据的mongod或者replication set 所有分片存储了集群的全部数据
通常而言 每个分片都应该是一个复制集 为每个分片提供冗余和高可靠性
MongoDB以每个集合为单位使用分片 必须通过mongos访问开启分片的集合 如果直连某个分片 只能看到部分数据
Primary Shard
每个数据库都有一个主分片 用来存储这个数据库中所有未开启分片的集合的数据
集群状态 sh.status()

# 配置服务器
生产环境中集群有精确的三个配置服务器 在测试环境中 可以只使用一个配置服务器来部署一个集群
配置服务器在config数据库中存储了集群的元信息 mongos缓存了这个数据库用来做读写的路由分发

# mongos
在集群中 mongos负责将查询与写入分发到分片中 使用mongos应用有了访问集群的同一入口 而不需要直接访问分片 
通过缓存的配置服务器中集群的源信息 mongos可以得知数据所位于的分片 使用这些元信息将应用的读写请求分发到不同的分片

# 片键
决定了一个集群中一个集合的document在不同shard中的分布 片键字段必须被索引 且在集合中的每条记录都不能为空 

# 分片集群实例
首先创建目录
分片1 （测试使用单例服务 而不是复制集）
/mongodb/cluster1/shard1/svr-27017/conf # 配置文件目录
/mongodb/cluster1/shard1/svr-27017/log # 日志文件目录
/mongodb/cluster1/shard1/svr-27017/pid # pid文件目录
/mongodb/cluster1/shard1/svr-27017/db # 存储数据文件目录
同理分片2
/mongodb/cluster1/shard2/svr-27027/conf # 配置文件目录
/mongodb/cluster1/shard2/svr-27027/log # 日志文件目录
/mongodb/cluster1/shard2/svr-27027/pid # pid文件目录
/mongodb/cluster1/shard2/svr-27027/db # 存储数据文件目录
创建配置中心目录
/mongodb/cluster1/config1/conf-svr-28017/conf # 配置文件目录
/mongodb/cluster1/config1/conf-svr-28017/log # 日志文件目录
/mongodb/cluster1/config1/conf-svr-28017/pid # pid文件目录
/mongodb/cluster1/config1/conf-svr-28017/db # 存储数据文件目录
再创建mongos的路由目录
/mongodb/cluster1/router1/rt-svr-29017/conf # 配置文件目录
/mongodb/cluster1/router1/rt-svr-29017/log # 日志文件目录
/mongodb/cluster1/router1/rt-svr-29017/pid # pid文件目录
然后设置配置文件
分片1 /mongodb/cluster1/shard1/svr-27017/conf/svr-27017.conf
dbpath=/mongodb/cluster1/shard1/svr-27017/db
logpath=/mongodb/cluster1/shard1/svr-27017/log/svr-27017.log
pidfilepath=/mongodb/cluster1/shard1/svr-27017/pid/svr-27017.pid
logappend=true
shardsvr=true
fork=true
oplogSize=10
port=27017
分片2 /mongodb/cluster1/shard2/svr-27027/conf/svr-27027.conf
dbpath=/mongodb/cluster1/shard2/svr-27027/db
logpath=/mongodb/cluster1/shard2/svr-27027/log/svr-27027.log
pidfilepath=/mongodb/cluster1/shard2/svr-27027/pid/svr-27027.pid
logappend=true
shardsvr=true
fork=true
oplogSize=10
port=27027
配置中心  /mongodb/cluster1/config1/conf-svr-28017/conf/conf-svr-28017.conf
dbpath=/mongodb/cluster1/config1/conf-svr-28017/db
logpath=/mongodb/cluster1/config1/conf-svr-28017/log/conf-svr-28017.log
pidfilepath=/mongodb/cluster1/config1/conf-svr-28017/pid/conf-svr-28017.pid
logappend=true
configsvr=true
fork=true
oplogSize=10
port=27027
replSet=config1 ## mongoDB3.4之后 配置中心需要是一个复制集
mongos路由 /mongodb/cluster1/router1/rt-svr-29017/conf/rt-svr-29017.conf
configdb=config1/127.0.0.1:28017 # 格式 <replSet>/host:port,...
logpath=/mongodb/cluster1/router1/rt-svr-29017/log/rt-svr-29017.log
pidfilepath=/mongodb/cluster1/router1/rt-svr-29017/pid/rt-svr-29017.pid
logappend=true
fork=true
oplogSize=10
port=29017 

然后使用mongo -f xxx启动各个服务
mongod -f /mongodb/cluster1/shard1/svr-27017/conf/svr-27017.conf
mongod -f /mongodb/cluster1/shard2/svr-27027/conf/svr-27027.conf
mongod -f /mongodb/cluster1/config1/conf-svr-28017/conf/conf-svr-28017.conf
*
配置中心需要连接一次 初始化复制集
mongo --port 28017
rs.initiate()
*
mongod -f /mongodb/cluster1/router1/rt-svr-29017/conf/rt-svr-29017.conf
mongo --port 29017
*
然后进行分片操作
use admin
db.runCommand({addShard: "127.0.0.1:27017"}) # 添加分片 复制集的格式为<replSet>/host:port,..
db.runCommand({addShard: "127.0.0.1:27027"}) # 添加分片 复制集的格式为<replSet>/host:port,..
db.runCommadn({listShards: 1}) # 查看分片列表
db.runCommand({enableSharding: "<db>"}) # <db>的集合可以在分片上分批存储
db.runCommand({shardcollection: "<db.collection>", key: {<field>: 1}, unique: true}) # <db.collection>分批在分片上存储
之后就可以使用分片了
一开始插入数据时，数据只是插入到一块分片中，插入完毕后，mongodb内部开始在各片之间进行数据移动，这个过程可能不是立即的，mongodb智能决定立即移动还是稍后移动
可以使用
sh.addShard()
sh.enableSharding()
sh.shardCollection() 
sh.splitAt() # 指定chunksize 
*



### Spring Boot 相关(不常用技术)
## 定时任务 
1.java自带的java.util.Timer类 允许调度一个java.util.TimerTask任务 可以按照某一个频度执行 但不能在指定时间开始执行 较少使用
2.ScheduleExecutorService jdk自带的类 是基于线程池设计的定时任务类 每个调度任务都会分配到线程池中的一个线程去执行 任务是并发执行 互不影响
3.Spring Task Spring3.0以后自带的task 可以看作一个轻量的Quartz 较为简单
4.Quartz 这是一个比较强大调度器 可以在指定时间执行 可以按照某一个频度执行 配置较为复杂
# Timer
TimerTask timerTask = new TimerTask() {
	public void run() {
		// 执行的任务逻辑代码
	}
};
Timer timer = new Timer();
timer.schedule(timertask, <delay>, <period>); // 定时执行 delay 表示第一次延迟的时间 period表示之后每次执行任务的时间间隔
# ScheduleExecutorService 
ExecutorService:Java提供的线程池 需要使用线程时 可以通过ExecutorService获得线程 可以有效控制最大并发线程数 提供系统资源使用率 通过比喵过多资源竞争 避免阻塞 同时提供定时任务 定期执行 单线程 并发数控制等功能
1.创建方式
public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler)
corePoolSize 核心线程数 一旦创建将不会在释放 如果创建的线程数还没达到指定的核心线程数量 将会继续创建新的核心线程 直到达到最大线程数 则将继续创建非核心线程 如果核心线程数等于最大线程数 则当核心线程都处于激活状态时 任务将被挂起 等待空闲线程来执行
maximumPoolSize 最大线程数 允许创建的最大线程数量 如果等于核心线程数 则无法创建非核心线程 如果非核心线程处于空闲时 超过设置的空闲时间则被回收 释放占用的紫苑
keepAliveTime 也就是非核心线程空闲时 所允许保存的最大的时间 
unit 时间单位
workQueue 任务队列 存储暂时无法执行的任务 等待空闲线程来执行
threadFactory 线程工厂 用于创建线程
handler 线程边界和队列容量已经达到最大时 用于阻塞程序

Executors.newCachedThreadPool() // 可缓存线程池 创建的都是非核心线程 而且最大的线程数为Integer的最大值 空闲线程存活时间为1分钟
Executors.newSingleThreadExecutor() // 单线程池 
Executors.newFixedThreadPool(int nThreads) // 固定线程数 核心线程和最大线程一致
Executors.newScheduledThreadPool(corePoolSize) // 固定线程数 支持定时任务和周期任务 设定核心线程数 最大线程数为Integer的最大数

2.ScheduledExecutorService
ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor() 
使用
executorService.schedule(runnable, delay, unit) // 指定时间延迟之后执行一次
executorService.scheduleAtFixedRate(runnable, delay, period, unit) // 指定延迟之后执行 之后每次以上一次任务执行开始开始计时 到达指定时间后 如果上一次任务执行完毕 开始执行下一次任务 如果上一次未完成 则等待上一次完成后开始下一次任务
executorService.scheduleWithFixedDelay(runnable, initDelay, delay, unit) // 指定延迟之后执行 之后每一次以上一次任务完成之后开始计时 到达指定任务后开始下一次任务

# Spring Task
Spring3.0之后自带 无需引入额外依赖
@Configuration 
@EnableScheduling
//@Async // 不设置这个注解 所有定时任务都是在同一线程中执行的 前一个执行完才会执行下一个 
public class AlarmTask {
	
	@Scheduled(initialDelay= xxx, fixedRate = xxx, fixedDelay = xxx, cron = xxx) 
	public void run() {
		// 任务逻辑代码
	}

}
多线程执行
@Configuration
@EnableAsync
public class AsyncConfig { // 需要配置这个开始异步指定定时任务
	@Value("${xxx.corePoolSize}") // 放在application.yml配置中
	private int corePoolSize;
	private int maxPoolSize;
	private int queueCapacity;
	
	@Bean
	public Executor createExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.initialize();
		return executor;
	}
}
执行时间的配置
fixedRate: 定义一个按一定频率执行的定时任务 以上一个任务开始开始计时
fixedDelay: 以上一个任务结束开始计时
initialDelay 开始的延迟时间
cron 通过表达式配置任务执行时间
Cron表达式详解
一个Cron表达式至少有6个（也可能7个）有空格分隔的时间元素 依次为
1.秒 0-59
2.分钟 0-59
3.小时 0-23
4.天 0-31
5.月 0-11
6.星期 1-7 1=SUN 2=MON 3=TUE 4=WED 5=THU 6=FRI 7=SAT
7.年 1979-2099
每个元素
可以是一个值如6 
可以是一个连续区间如9-12
可以是一个时间间隔(8-18)/4 /4表示每隔4个元素单位
可以是一个列表(1,2,3)
可以是一个通配符 
* 表示所有可能的值
/ 表示指定数量的增量
? 仅用于天(月)和天(星期) 表示不指定值 当两个表达式中一个被指定值之后 需要将另一个设为?
L 仅用于天(月)和天(星期)表示最后 但是前面有其他内容则是最后第几个
W 表示工作日
C 表示Calendar 意思是计划所关联的日期 

# Quartz
Spring Boot2.0之后添加了starter-quartz
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>
之前需要添加依赖
<dependency>
  <groupId>org.quartz-scheduler</groupId>
  <artifactId>quartz</artifactId>
  <version>2.3.0</version>
</dependency>
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-context-support</artifactId>
</dependency>
然后创建
实现QuartzJobBean虚拟类
public class TestQuartTask extends QuartzJobBean {
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExcutionException {
		// 实现的业务逻辑代码
	}
}
然后创建QuartzConfiguration配置类
@Configuration
public class QuartzConfiguration {
	@Bean
	public JobDetail createTestQuartzTask() {
		return JobBuilder.newJob(TaskQuartzTask.class).withIdentity("").storeDurably().build();
	}
	@Bean
	public Trigger createTestQuartzTaskTrigger() {
		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(x).repeatForever();
		return TriggerBuilder.newTrigger().forJob(createTestQuartzTask()).withIdentity("").withSchedule(scheduleBuilder).build();
	}
	
}

Quartz学习
QuartzAPI
关于name和group
name是在scheduler中的唯一标识 如果需要更新一个JobDetail定义 只需要再设置一个name相同的JobDetail实例即可
group是一个组织单元 scheduler会提供一些对整组操作的API
1.Trigger
A.StartTime  EndTime
指定触发时间区间 这个区间之外是不会触发的
B.优先级 Priority
同时触发多个Trigger但是资源不足时 按照优先级执行
优先级默认5 负数时为默认值
C.Misfire(错失触发)策略
类似资源不足或机器崩溃重启 Trigger在指定时间点未触发 就是Missfire
比较复杂 暂时没看完
D.Calendar
不是jdk自带的java.util.Calendar 不是为了计算日期 而是补充Trigger时间 可以排除或加入某一些特定的时间点
Quartz提供了多种Calendar用于排除或包含
HolidayCalendar 指定特定日期 精度为天
DailyCalendar 指定每天时间段 格式是HH:MM[:SS[:mmm]] 最小精度为微妙
WeeklyCalendar 指定每星期的星期几
MonthlyCalendar 每月的几号
AnnualCalendar 每年的那一天
CronCalendar Cron表达式
E.Trigger实现类
a.SimpleTrigger
指定从某一个时间开始 以一定时间间隔执行任务 单位毫秒
repeatInterval 重复间隔
repeatCount 重复次数
b.CalendarIntervalTrigger
类似于SimpleTrigger 支持的时间间隔有 秒 分 时 天 月 年 星期
interval 执行间隔
intervalUnit 执行间隔的单位（秒，分钟，小时，天，月，年，星期）
c.DailyTimeIntervvalTrigger
指定每天的某个时间段内 以一定时间间隔执行任务 支持指定星期
startTimeOfDay 每天开始时间
endTimeOfDay 每天结束时间
daysOfWeek 需要执行的星期
interval 执行间隔
intervalUnit 执行间隔的单位（秒，分钟，小时，天，月，年，星期）
repeatCount 重复次数
d.CronTrigger
适合于更复杂的任务
Cron表达式 参考上面Spring Task
F.JobDetail & Job
JobDetail是任务的定义 Job是任务的执行逻辑 JobDetail里会引用一个Job定义
G.JobDataMap
每次都是newInstance实例 通过JobDataMap传递数据


### RabbitMQ
1.基础概念
A.ConnectionFactory, Connection, Channel
是RabbitMQ对外提供的API中最基本的对象 
Connection是RabbitMQ的socket连接 封装了socket协议相关部分逻辑
ConnectionFactory是Connection的制作工厂
Channel是与RabbitMQ打交道的接口 大部分业务操作通过此接口完成 包括定义Queue、定义Exchange、绑定Queue与Exchange、发布消息等
B.exchange
生产者不会直接将消息放到消息队列中，而是先发送给Exchange 由Exchange将消息路由到一个或多个Queue中
四种类型
a.Direct 先匹配后投送 在绑定时设定一个routing_key 消息的routing_key匹配时 才会投送到绑定的队列中
ConnectionFactory factory = new ConnectionFactory();
factory.setHost/VirtualHost/Port/Username/Password();
Connection connection = factory.newConnection();
Channel channel = connection.createChannel();
channel.queueDeclare(<queue_name>, boolean durable,boolean eclusive, boolean autoDelete, Map params) // 声明队列 是否持久化 是否私有 是否自动删除 其他参数
channel.exchangeDeclare(<exchange_name>, <exchange_type>, boolean durable, boolean autoDelete, Map params)
channel.queueBind(queue_name, exchange_name, binding_key); // 使用binding_key绑定exchange和queue 在生产者生产消息时 需要指定routing_key来发送到指定的queue

b.Topic 按规则转发
根据通配符 
路由键必须是一串字符 用.隔开  (binding_key为通配)
路由模式 星号*表示匹配一个单词 井号#表示匹配零个或多个单词
c.Headers 设置header attribute参数类型的交换机
也是规则匹配 相较于direct和topic固定使用routing_key
则是一个自定义匹配规则的类型
绑定时会设定一组键值对规则 消息中也包括一组键值对
d.Fanout 转发消息到所有绑定队列中
消息广播模式 会发送给所有绑定的队列 无视routing_key
C.queue
是RabbitMQ的内部对象 用于存储消息
RabbitMQ的消息只能存储在Queue中，生产者生产消息并最终投递到Queue中，消费者可以从Queue中获取消息并消费
D.Message ackonwledgement 
实际使用中 消费者接受到Queue中消息 但没有处理完就出现意外 为了避免这种情况 可能需要消费者在消费完后发送一个回执消息 RabbitMQ收到回执后才将消息从队列中删除 如果没有收到回执并且检测到消费者断开连接 则会将消息发给别的消费者
** 如果忘记发送回执 则会造成队列中消息的累计并且重复消费同一消息
E.Message durability
希望在重启的情况下都不丢失消息 可以将Queue和Message设置为Durable(持久的）
F.Prefetch count
生产者在将消息发送给Exchange的时候 一般会指定一个routing key 来指定这个消息的路由规则 需要与Exchange Type和Binding key联合使用
G.Binding
通过Binding将exchange和queue关联起来 如何正确的将消息路由到指定的Queue中 在绑定的时候 通常会指定一个binding key
H.Virtual Host
虚拟主机持有一组交换机、队列和绑定，只能在虚拟主机的粒度进行权限控制

## 简单应用
public static Connection getConnection() {
	ConnectionFactory factory = new ConnectionFactory();
	factory.setHost();
	factory.setPort();
	factory.setUsername();
	factory.setPassword();
	factory.setVirtualHost();
	return factory.newConnection();
}
1.简单队列
public class SimpleQueueSender {
	public void send() {
		Connection connection = getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(队列名，是否持久，是否私有，是否自动删除，其他配置);
		channel.exchangeDeclare(交换机名，交换机类型，是否持久，是否自动删除，其他配置);
		channel.queueBind(队列名，交换机名，binding_key);
		channel.basicPublish(交换机名，routing_key，其他配置，信息);
		channel.close();
		connection.close();
	}
}
public class SimpleQueueReceiver {
	public void receive() {
		Connection connection = getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(队列名，是否持久，是否私有，是否自动删除，其他配置);
		channel.exchangeDeclare(交换机名，交换机类型，是否持久，是否自动删除，其他配置);
		channel.queueBind(队列名，交换机名，binding_key);
		channel.basicConsumer(队列名，是否自动回执，new DefaultConsumer() {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				try {
				// 使用body处理信息
				} catch (Exception e) {
					channel.abort();
				} finally {
					channel.basicAck(envelope.getDeliveryTag(), false); // 手动回执
				}
				
			}
		});
		channel.close();
		connection.close();
	}
}



2.工作模式 多个消费者 一次从队列中获取一个消息消费
public class WorkModeSender {
	public void send() {
		Connection connection = getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(队列名，是否持久，是否私有，是否自动删除，其他配置);
		channel.exchangeDeclare(交换机名，交换机类型，是否持久，是否自动删除，其他配置);
		channel.queueBind(队列名，交换机名，binding_key);
		channel.basicPublish(交换机名，routing_key，其他配置，信息);
		channel.close();
		connection.close();
	}
}
public class WorkModeReceiver {
	public void receive() {
		Connection connection = getConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(队列名，是否持久，是否私有，是否自动删除，其他配置);
		channel.exchangeDeclare(交换机名，交换机类型，是否持久，是否自动删除，其他配置);
		channel.queueBind(队列名，交换机名，binding_key);
		channel.basicQos(1); // 最大预获取消息数量 消息从队列异步推送给消费者，消费者的 ack 也是异步发送给队列，从队列的视角去看，总是会有一批消息已推送但尚未获得 ack 确认，Qos 的 prefetchCount 参数就是用来限制这批未确认消息数量的。设为1时，队列只有在收到消费者发回的上一条消息 ack 确认后，才会向该消费者发送下一条消息。prefetchCount 的默认值为0，即没有限制，队列会将所有消息尽快发给消费者。
		不添加可能一次从队列中获取所有消息 然后队列消息都转为noack状态 导致其他消费者无法获取
		channel.basicConsumer(队列名，是否自动回执，new DefaultConsumer() {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				try {
				// 使用body处理信息
				} catch (Exception e) {
					channel.abort();
				} finally {
					channel.basicAck(envelope.getDeliveryTag(), false); // 手动回执
				}
				
			}
		});
		channel.close();
		connection.close();
	}
}

a.轮询分发 
b.公平分发 
消息的确认模式
a.自动确认
只要消息从队列中取走 无论是否成功都认为已经成功消费
b.手动确认
从队列中取走后 服务器将其标记为不可用状态 等待消费者反馈 如果一直没有返回 消息将一直处于不可用状态

3.订阅模式




### Kafka
1.简介
提供了类似于JMS的特性，对消息保存时根据Topic进行归类，发送消息者称为Producer，消息接受者成为Consumer Kafka集群由多个Kafka实例组成 每个实例成为broker 依赖于zookeeper保证系统可用性 集群保存一些元数据
2.Topic/logs1
一个Topic可以被认为是一类消息，每个topic将被分为partition，每个partition在存储方面是append log文件 任何发布到此partition的消息都会被直接追加到log尾部 每条消息在文件中的位置称为offset offset为long型数字 唯一标记一条消息
kafka和JMS不同的是，即使消息被消费仍然不会被立即删除，日志文件将会根据broker中的配置要求，保留一段时间后删除，比如log文件保留2天，那么两天后文件删除，无论消息是否被消费
对于consumer而言，需要保存和使用消费消息的offset，正常消费时，offset将线性向前驱动，但consumer可以使用任意顺序消费消息，只需要将offset设置为任意值（保存在zookeeper中)
kafka集群几乎不需要维护任何consumer和producer状态信息，这些信息由zookeeper保存，因此客户端实现非常轻量级
partition的设计目的：最根本的是kafka是基于文件存储，通过分区，可以将日志内容分散到多个server上，避免文件尺寸到达磁盘上限，每个partition都会被当前server保存，可以将一个topic切分成任意多的partition，来提升消息保存/消费的效率，越多的partition以为着可以容纳更多的consumer
3.Distribution
一个Topic的多个partitions，被分部到kafka集群中的多个server上，每个server负责partition中的消息读写，此外kafka还可以配置partition需要备份的个数（replicas)，每个partition可以被备份到多个server上
基于replicated方案，意味着需要对多个备份进行调度，每个partition都有一个leader server，负责所有读写操作，如果leader失效则由其他follower接替。follower只是单纯的和leader同步消息。有多少partition就有多少个leader，kafka会将leader均衡分散到每个实例上
Producer
将消息发布到指定的Topic中，也能决定将消息归属于哪个partition 比如Round-Robin或其他方式
Consumer
本质上kafka只支持topic 每个consumer属于一个consumer group，发送到topic的消息只会被group中的一个consumer消费
如果所有consumer属于同一个group就和queue方式很想，kafka将在consumer上负载均衡
如果所有consumer属于不同group，则相当于发布-订阅模式，消息将被广播到所有节点上消费
每个group中的consumer消费相互独立，一个Topic中的每个partition只会被一个consumer消费，不过consumer可以一次消费多个partition的消息。kafka只能保证一个partition中的消息被某个consumer消费时，消息是顺序的，从Topic角度而言，消息仍不失有序的
Kafka的设计原理决定，对于一个topic，同一个group不能有多于partition的consumer同时消费，否则意味着某些consumer将无法取得消息
Guarantees
发送到parition中的消息将会按照他接受的顺序追加到日志中
对于消费者 消费消息的顺序和日志中消息的顺序一致
如果replication为N 则允许N-1个kafka实例失效
# 使用场景
1.Messaging
2.Websit activity tracking
3.Log Aggregation
# 设计原理
1.持久性
使用文件存储消息，性能上严重依赖文件系统的本身特性，因为是对log文件的append操作，因此磁盘检索的开支较小，同时减少了磁盘写入的次数，broker会将消息暂时buffer起来，当消息个数或尺寸达到阈值时，再flush到磁盘中，减少IO次数
2.性能
对于producer端，可以考虑现将消息buffer起来，达到阈值时再一起发送给server，对于consumer端也一样，可以一起fetch多条消息。网络传输可以经过压缩，支持gzip/snappy等多种压缩方式
3.生产者
负载均衡，producer将会和topic下所有的partition leader保持socket连接，消息将由socket发送到broker，不会经过路由层。由producer决定发送到topic中的哪个partition上，可以采用random key-hash 轮询等
partition leader的位置（host:port)注册上zookeeper上，producer作为zookeeper client，注册了watch来监听partition leader的变更事件
异步发送：将多个消息buffer，批量发送到broker上
4.消费者
consumer向broker发送fetch请求，并告知其获取消息的offset，此后consumer会获取一定数量的消息，consumer可以重置offset来重新消费消息
Kafka是基于pull方式，consumer主动去pull消息，优点在于consumer自己决定fetch消息的数量，也可以控制消费的offset。
其他JMS实现，消息消费位置由provider保留，避免重复发送或将没有消费成功的消息重发等，同时还要控制消息状态，这就要求JMSbroker做很多工作。在kafka中，partition中的消息同时只能由一个consumer消费，切不存在消息状态的控制，也没有复杂的消息确认机制，因此broker端是相当轻量级的。当消息被consumer接受之后，在本地保存最后消费的offset，同时定时向zookeeper注册offset。
5.消息传送机制
A.at most once 最多一次 不再重发
消费者fetch消息后 保存offset 然后处理消息 若处理过程中出现异常 导致部分消息未能处理成功 未成功处理的部分将不能被fetch到
(首选)*B.at least once 至少发送一次 直到接受成功 
消费者fetch消息 处理消息 然后再保存offset 若保存offset操作失败则可能处理到已经处理过的消息
C.exactly once 只发送一次
在kafka中不必要
6.复制备份
每个partition复制到多个server上，每个partition有一个leader和多个follower，可以通过配置文件指定。leader处理所有的读写操作，follower只同步消息。leader负责追踪follower的状态，若落后太久则将其提出同步列表。当所有follower都将一条消息保存成功，这条消息才被视为commited
leader失效，则需要选择一个up-to-date的follower作为新的leader
7.日志
如果topic名为my_topic，有两个partition，那么日志将被保存在my_topic_0和my_topic_1目录中，日志文件保存了一序列log entries(日志条目)，每个log entry格式为4个字节的数字N表示消息长度 + N个字节的消息内容，每个日志都有一个offset来唯一的标记一条消息，offset最多为8个字节数字，表示此消息在partition中的起始位置，每个partition在物理存储层面，可以由多个log file组成(segment)，segmentfile的命名为最小offset.kafka，比如00000000000.kafka表示最小offset为00000000的segment
每个partition中所持有的segments列表信息将被存储在zookeeper上
当segment文件尺寸达到一定阈值时（配置文件默认1G），将会创建一个新的文件，当buffer中的消息条数或尺寸或距离上一次flush时间达到阈值时，将被flush到日志文件中。有可能flush失败导致segment文件格式破坏，则需要在启动时检测最后一个segment文件是否有修复的必要
获取消息时，需要指定offset和最大chunk尺寸，offset表示消息起始位置，chunk size表示最大获取消息的总长度（简介指定数量），根据offset可以找到消息所在的segment文件，然后根据segment的最小offset取差值，得到相对位置，读取输出即可
日志文件的删除策略：启动一个后台线程定期扫描log file列表，保存时间超过阈值的文件直接删除，为了避免删除文件仍有read操作，采用copy-on-write方式
8.分配
kafka使用zookeeper来存储元信息，并使用zookeeper watch机制来发现元信息的变更并作出相应的动作
A.Broker node registry 启动时想zookeeper中注册自己的节点信息（临时znode），通过断开时删除
格式 /broker/ids/[0...N] --> host:port
B.Brokder Topic Registry 当一个broker启动时 向zookeeper注册自己的topic和partition信息 还是临时znode
格式 /broker/topics/[topic]/[0...N] [0...N]表示partition索引号
C.Consumer 和 Consumer Group
每个consumer客户端被创建时，会向zookeeper注册自己的信息 为了负载均衡
group中的多个consumer可以交错消费一个topic的所有partitions
D.Consumer id registry
每个consumer都有一个唯一ID(host:uuid) 可以由配置文件制定也可以由系统生成
格式 /consumers/[group_id]/ids/[consumer_id] 临时节点znode
值为{"topic_name": #streams...} 表示此consumer目前所消费的topic+partitions列表
E.Consumer offset Tracking 
用来跟踪每个consumer目前所消费的partition中最大的offset
格式/consumers/[group_id]/offset/[topic]/[broker_id-partition-id] --> offset_value 持久节点 一个group中的消费者失效 其他消费者可以继续消费
F.Partition Owner registry
用来标记partition被哪个consumer消费 临时节点znode
/consumers/[group_id]/owners/[topic]/[broder_id-partition_id] --> consumer_node_id 
    A) 首先进行"Consumer id Registry";
    B) 然后在"Consumer id Registry"节点下注册一个watch用来监听当前group中其他consumer的"leave"和"join";只要此znode path下节点列表变更,都会触发此group下consumer的负载均衡.(比如一个consumer失效,那么其他consumer接管partitions).
    C) 在"Broker id registry"节点下,注册一个watch用来监听broker的存活情况;如果broker列表变更,将会触发所有的groups下的consumer重新balance.
	
	1) Producer端使用zookeeper用来"发现"broker列表,以及和Topic下每个partition leader建立socket连接并发送消息.
    2) Broker端使用zookeeper用来注册broker信息,已经监测partitionleader存活性.
    3) Consumer端使用zookeeper用来注册consumer信息,其中包括consumer消费的partition列表等,同时也用来发现broker列表,并和partition leader建立socket连接,并获取消息.
主要配置


# Spring Boot 中简单使用
引入
<dependency>
	<groupId>org.springframework.kafka</groupId>
	<actifactId>spring-kafka</actifactId>
</dependency>
application.yml配置
spring:
	kafka:
		bootstrap-server: host:port
		producer: 
			# 
		consumer: 
			# 
发送
@Autowaired 
private KafkaTemplate template;

public void send() {
	ListenableFuture future = template.send(topic_name, data);
	future.addCallback(new SuccessCallback() {
		public void onSuccess(Object o) {
			// 成功后的操作
		}
	}, new FailureCallback() {
		public void onFailure(Throwable throwable) {
			// 失败后的操作
		}
	});
}
取得消息
@Component
public class Receiver {
	@KafkaListening(topics = {"topic_name", ..}
	public void receive(ConsumerRecord<K, V> record) {
		// 接受到信息后的操作 record.topic() 所属topic record.key() record.value() 数据
	}
}		


### Shell
## 基本概念
Shell是一个应用程序，连接了用户和Linux内核，让用户能够更加高效、安全、低成本地使用Linux内核。
Shell本身并不是内核的一部分，只是站在内核的基础上编写的一个应用程序，特殊性在于开机立马启动，并呈现在用户面前。用户通过Shell使用Linux。
Shell接受用户输入命令，对命令进行处理，调用内核暴露出的接口，将结果返回给用户。
Shell输入的命令一部分是自带的，叫做内置命令，有一部分是其他应用程序附带的，叫做外部命令。
Shell本身支持的命令并不多，功能也有限，但是可以调用其他程序作为命令。使得Shell命令的数量可以无限扩展。
Shell还可以让多个外部程序发生连接，方便的传递数据。
Shell也支持编程
支持
if...else、case...in、for、while、until
变量、数组、字符串、注释、加减乘除、逻辑运算等概念
函数，包括用户自定义的函数和内置函数（如printf、export、eval等）
主要用来发开一些实用的、自动化的小工具，例如检测参数、搭建环境、日志分析等。
Shell是一种脚本语言
需要一边执行一边翻译，不会生成任何可执行文件，运行中即时翻译，翻译一部分执行一部分。
这个过程叫做解释。
有点是执行速度快，硬件要求低，保密性好。
## 需要掌握技能
Shell脚本很适合处理纯文本类型的数据
是实现Linux系统自动管理以及自动化运维所必需的工具，底层以及基础应用软件的核心大多涉及Shell脚本内容
## 常用的Shell
1 sh
是UNIX上的标准shell
2 csh
3 tchsh
4 ash
5 bash 
是Linux默认的shell 由GNU组织开发 保持了对sh shell的兼容性
查看shell
一般放在/bin或/usr/bin下 当前Linux可用的shell都记录在/etc/shells文件中
查看默认的shell
echo $SHELL
# 进入shell的两种方式
1.进入Linux控制台
从图形页面进入 CTRL+ALT+Fn(n=1,2,3,4...)
比如CentOS在启动时会创建6个虚拟控制台 Ctrl+Alt+Fn(2,3,4,...)可以从图形界面模式切换到控制台模式，按下Ctrl+Alt+F1可以从控制台模式进入图形界面模式
2.使用终端
# Shell命令的基本格式
1.基本格式
command [选项] [参数]
2.使用选项
短格式选项和长格式选项
- 短格式选项是长格式选项的简写 用一个减号-和一个字母表示 例如 ls -l
- 长格式选项是完整的英文单词 用两个减号--和一个单词表示 例如 ls --all
3.使用参数
参数是命令的操作对象，一般情况下，文件、目录、用户和进程可以作为参数被命令操作，省略参数是一般有默认参数
4.选项附带的参数
有些命令的选项后也可以附带参数，用于补全选项，或者调整选项的功能细节。
# 第一个Shell脚本
vim test.sh
#!/bin/bash # #!是一个约定的标记 告诉系统使用什么解释来执行 /bin/bash则指定位置

echo "Hello World" # stdout输出文本 
# 以#开头的都是注释 

echo "What is your name?"
read name # 从stdin中读取输入 并赋值给变量name
echo "Hello, $name" # 使用变量name输出 $field表示变量
# 执行Shell脚本
1.
chmod +x test.sh
./test.sh
2.将可执行文件作为参数传给shell
/bin/bash test.sh
3.检测是否开启新进程
每一个进程都有一个唯一的ID 称为PID 使用$$变量可以获取当前进程的PID
4.在当前进程中运行Shell脚本
source filname 可以简写为 . filename  点和filename之间有空格
source是Shell 内置命令之一 会读取脚本文件的代码 并依次执行所有语句

# Shell变量
脚本语言在定义变量时通常不需要指定类型，直接赋值即可
Bash shell中，每一个变量的值都是字符串，无论给变量赋值有没有使用引号，值都会以字符串的形式存储
Bash shell在默认情况下不会区分变量类型，即使将整数或小数赋值给变量，也被视为字符串。
如果有必要，可以使用shell declare关键字显式定义变量的类型
1.定义变量
三种方式
variable=value
variable='value'
variable="value"
单引号和双引号的意义不同 =周围不能有空格
命名规范
由数组、字母、下划线组成
必须以字母或者下划线开头
不能使用Shell的关键字(help命令查看)
2.使用变量
$variable即可使用 不推荐
${variable} 推荐使用 {}是可选的，目的是帮助解释器识别变量边界
3.修改变量的值
variable=value 
variable='value' 
variable="value"
同赋值一样
4.单引号和双引号的区别
以单引号包围变量的值时，单引号中是什么就输出什么，即使内容中有变量和命令(命令``)也会原样输出，比较适合显示春字符串的情况
以双引号包围变量时，输出时会先解析里面的变零和命令，而不是把双引号中的变量名和命令原样输出，比较适合字符串中附带有变量和命令并想将其解析后再输出的变量定义
推荐 数字不加引号 需要原样输出使用单引号 其他情况下基本都使用双引号
4.将命令的结果复制给变量
variable=`command`
variable=$(command) 推荐使用 $()
5.只读变量
使用readonly命令可以将变量定义为只读变量 不能被改变
6.删除变量
unset variable_name
不能删除只读变量

# Shell变量的作用域
作用域就是有效范围
不同作用域中，同名变量互不干涉
1.Shell全局变量 可以在当前进程中使用
在Shell中定义的变量默认是全局变量（包括在自定义函数中的定义变量）
全局变量指在整个shell过程中都有效 每个Shell都有自己的作用域 彼此不影响
仅在定义的第一个Shell进程中有效，对新的Shell进程没有影响。
2.Shell环境变量 可以在子进程中使用
全局变量只在当前Shell进程有效，对子进程无效，使用export命令将全局变量导出，就对子进程有效，也被称为环境变量
环境变量被创建时所处的Shell进程被称为父进程，如果父进程中再创建一个新的进程来执行Shell命令，就被称为子进程。Shell子进程产生时，会继承父进程的环境变量为自己所用
注意，两个没有父子关系的Shell进程是不能传递环境变量的，并且环境变量只能向下传递而不能向上传递
export variable 
export variable=value # 声明变量的同时设置为环境变量
环境变量也是临时的
通过export导出的环境变量只对当前进程及所有子进程有效，如果顶层的父进程关闭则环境变量也消失。
只有将变量写入shell配置文件中才能达到环境变量不过期的目的
3.Shell局部变量 只能在函数内部使用
需要声明局部变量 需要在变量名前加上local命令

# Shell命令替换
是指将命令的输出结果赋值给某个变量 
两种方式
variable=`command`
variable=$(command)
command可以是一个命令 也可以是多个命令用;分隔
$(()) 标记Shell的数学计算命令
如果被替换命令的输出内容包含多行（包含换行符）或者多个连续空白符时，在输出时需要使用""包围，否则系统会使用默认的空白符来填充 导致换行无效或者空白符被压缩为1个
再谈反引号``和$()
原则上两个方式是等价的，但是``查看的时候不容易辨别 容易造成混乱。另一方面，$()支持嵌套，而``不行

# Shell位置参数（命令行参数）
运行Shell脚本文件时 可以传递一些参数 这些参数在文件内部使用$n的形式来接受 例如$1表示第一个参数 $2表示第二个参数 
同样在调用函数时也可以传递参数 与其他编程语言不同 没有所谓的形参和实参 在定义函数时也不用指明参数的名字和数目 换句话说 定义Shell函数时不能带参数 但是在调用函数时可以传递参数 在函数内部也使用$n的形式接受 例如$1代表第一个参数
$n这种形式接受的参数 称为位置参数
除了$n 还有$# $* $@ $? $$ 几个特殊参数
1.给脚本文件传递参数
testPositionParam.sh
#!/bin/bash

echo "语言: $1"
echo "链接: $2"

$ bash testPositionParam.sh Shell www.sun.com
2.给函数传递位置参数
function func() {
	echo "语言: $1"
	echo "链接: $2"
}
func Java www.sun.com
如果参数过多 就需要使用${n}的形式来接受参数

# Shell特殊变量
$0 当前脚本的文件名
$n 传递的位置参数
$# 传递给脚本或函数的参数个数
$* 传递给脚本或函数的所有参数
$@ 传递给脚本或函数的所有参数 当被" "包含时，跟$*有所不同
$? 上个命令的退出状态或函数的返回值
$$ 当前Shell进程的PID
1.$?获取上个命令的退出状态
#/bin/bash

if [ "$1" == 100 ] 
then 
	exit 0
else 
	exit 1
fi
bash test.sh 100
echo $? # 输出0
bash test.sh 99
echo $? # 输出1
2.$?获取函数的返回值
function func() {
	return $(expr $1 + $2)
}
func 11 22
echo $?
严格来说 shell函数的return关键字用来表示函数的退出状态而不是函数的返回值 Shell没有专门处理返回值的关键字

# Shell字符串详解
A.单引号包围的字符串
任何字符都会原样输出 在其中使用变量是无效的
字符串中不能出现单引号 即使对单引号进行转义也不行
B.双引号包围的字符串
如果其中包含了某个变量 那么该变量会被解析而不是原样输出
字符串可以出现双引号 只要转义就行了
C.不被引号包围的字符串
出现变量也会被解析
字符串中不能出现空格 否则空格后边的字符串会作为其他变量或者命令解析
获取字符串长度
${#string_name}

# Shell字符串拼接（连接、合并）
${variable}${variable} # 没有空格即可
" " 可以任意拼接

# Shell字符串截取
1.从指定位置开始截取
A.从字符串左边开始计数
${string: start: length}
B.从字符串右边开始计数
${string: 0-start: length}
从左边计数是从0开始 从右边开始计数是从1开始 
截取方向都是从左到右
2.从指定字符（子字符串）开始截取
A.使用#号截取右边字符
${string#*chars} 从第一个匹配的字符开始截取
如果不需要无视左边的字符可以去掉*
${string#chars}
${string##*chars} 从最后一个匹配的字符开始截取
B.使用%号截取左边字符
${string%chars*}
${string%chars}
${string%%chars*}

# Shell数组
Shell支持数组 
Shell并没有限制数组的大小 Shell数组元素的小标也是从0开始计数
获取数组中的元素要使用下标[] 下标可以是一个整数 也可以一个结果为整数的表达式 必须大于等于0
只支持一维数组
1.Shell数组的定义
array_nam=(ele1 ele2 ele3...)
Shell是弱类型的 并不要求所有数组元素的类型都相同
数组从长度不固定 定义之后还可以增加元素 
也无需逐个元素地给数组赋值
array_name([index1]=ele1 [index2]=ele2 [index3]=ele3 ...)
2.获取数组元素
${array_name[index]}
使用@ *获取所有元素
${array_name[@/*]}

# 获取数组长度
数组长度就是数组元素的个数
利用@ *可以将数组扩展成列表 然后使用#来获取元素个数
${#array_name[@]}
${#array_name[*]}

# 数组拼接
利用@ *将数组扩展为列表 然后再合并到一起
array_new=(${array1[*]} ${array2[*]})

# 删除数组元素
unset array_name[index]
unset array_name

# 获取数组指定范围元素
${array_name[*/@]:start:length}

# 替换 不会替换原有数据
${array_name[*/@]/source/target}

# 关联数组
首先需要使用声明语句讲一个变量声明为关联数组
declare -A assArray # 脚本中无需使用$()或``
然后添加元素
A.使用内嵌索引-值列表的方法
array_name=([key]=value [key1]=value1 ...)
B.使用独立索引-值进行赋值
array_name[key1]=value1
array_naem[key2]=value2
获取数据
${array_name[@]}
${!array_name[@]} 获取所有键值

# Shell内建命令
type command
command is Shell builtin 表示是内建命令
$PATH变量包含的目录几乎聚集系统中绝大多数可执行命令
命令	说明
:	扩展参数列表，执行重定向操作
.	读取并执行指定文件中的命令（在当前 shell 环境中）
alias	为指定命令定义一个别名
bg	将作业以后台模式运行
bind	将键盘序列绑定到一个 readline 函数或宏
break	退出 for、while、select 或 until 循环
builtin	执行指定的 shell 内建命令
caller	返回活动子函数调用的上下文
cd	将当前目录切换为指定的目录
command	执行指定的命令，无需进行通常的 shell 查找
compgen	为指定单词生成可能的补全匹配
complete	显示指定的单词是如何补全的
compopt	修改指定单词的补全选项
continue	继续执行 for、while、select 或 until 循环的下一次迭代
declare	声明一个变量或变量类型。
dirs	显示当前存储目录的列表
disown	从进程作业表中刪除指定的作业
echo	将指定字符串输出到 STDOUT
enable	启用或禁用指定的内建shell命令
eval	将指定的参数拼接成一个命令，然后执行该命令
exec	用指定命令替换 shell 进程
exit	强制 shell 以指定的退出状态码退出
export	设置子 shell 进程可用的变量
fc	从历史记录中选择命令列表
fg	将作业以前台模式运行
getopts	分析指定的位置参数
hash	查找并记住指定命令的全路径名
help	显示帮助文件
history	显示命令历史记录
jobs	列出活动作业
kill	向指定的进程 ID(PID) 发送一个系统信号
let	计算一个数学表达式中的每个参数
local	在函数中创建一个作用域受限的变量
logout	退出登录 shell
mapfile	从 STDIN 读取数据行，并将其加入索引数组
popd	从目录栈中删除记录
printf	使用格式化字符串显示文本
pushd	向目录栈添加一个目录
pwd	显示当前工作目录的路径名
read	从 STDIN 读取一行数据并将其赋给一个变量
readarray	从 STDIN 读取数据行并将其放入索引数组
readonly	从 STDIN 读取一行数据并将其赋给一个不可修改的变量
return	强制函数以某个值退出，这个值可以被调用脚本提取
set	设置并显示环境变量的值和 shell 属性
shift	将位置参数依次向下降一个位置
shopt	打开/关闭控制 shell 可选行为的变量值
source	读取并执行指定文件中的命令（在当前 shell 环境中）
suspend	暂停 Shell 的执行，直到收到一个 SIGCONT 信号
test	基于指定条件返回退出状态码 0 或 1
times	显示累计的用户和系统时间
trap	如果收到了指定的系统信号，执行指定的命令
type	显示指定的单词如果作为命令将会如何被解释
typeset	声明一个变量或变量类型。
ulimit	为系统用户设置指定的资源的上限
umask	为新建的文件和目录设置默认权限
unalias	刪除指定的别名
unset	刪除指定的环境变量或 shell 属性
wait	等待指定的进程完成，并返回退出状态码

# alias 给命令创建别名
alias alias_command_name=`command`
别名只是临时的
只能在当前Shell进程中使用 子进程和其他进程中都不能使用 Shell进程结束后随之消失
unalias alias_command_name # 删除一个别名
unalias -a # 删除所有别名

# echo 输出字符串
输出字符串并在最后默认加上换行符
不换行
-n 参数
默认情况不会解析转义字符 需要使用
-e 参数解析转义字符

# read命令
从标准输入中读取数据并赋值给变量 
read [-options] [variables]
支持的option
-a array 把读取的数据赋值给数组 从下标0开始
-d delimiter 用字符串delimiter指定读取结束的位置 而不是换行符（读取的字符串没有delimiter）
-e 在获取用户输入的时候 对功能键进行编码转换 不会显式显示功能键对应的字符
-n num 读取num个字符 而不是整行字符
-p 显示提示信息 
-r raw 原样读取 不把反斜杠字符解释为转义字符
-s 静默模式 不在屏幕上显示输入的字符 密码等情况下需要
-t 设置超时时间
-u fd 使用文件描述符fd作为输入源 而不是标准输入

# exit命令 退出当前进程
退出当前Shell进程，并返回一个退出状态，使用$?可以接受这个退出状态
默认返回0 除了0以外了表示执行出错
exit退出状态只能是0~255之间的整数 0表示成功 其他都会失败

# declare和typeset命令 设置变量属性
typeset已被弃用
declare [+/-] [aAfFgilprtux] [变量名=变量值]
-f [name] 列出之前由用户在脚本中定义的函数名称和函数体
-F [name] 仅列出自定义函数名称
-g name 创建全局变量
-p [name] 显示指定变量的属性和值
-a name 声明为普通数组
-A name 声明为关联数组
-i name 定义为整型数
-r name[=value] 定义为只读 等同于readonly
-x name[=value] 设置为环境变量 等同于export name[=value]

# shell数学计算
+、-	加法（或正号）、减法（或负号）
*、/、%	乘法、除法、取余（取模）
**	幂运算
++、--	自增和自减，可以放在变量的前面也可以放在变量的后面
!、&&、||	逻辑非（取反）、逻辑与（and）、逻辑或（or）
<、<=、>、>=	比较符号（小于、小于等于、大于、大于等于）
==、!=、=	比较符号（相等、不相等；对于字符串，= 也可以表示相当于）
<<、>>	向左移位、向右移位
~、|、 &、^	按位取反、按位或、按位与、按位异或
=、+=、-=、*=、/=、%=	赋值运算符，例如 a+=1 相当于 a=a+1，a-=1 相当于 a=a-1
Shell不能直接进行算术运算 必须使用数学计算命令 
Bash Shell中 不特别指明（declare命令）每个变量都是字符串
数学计算命令
(()) 用于整数运算 效率很高 推荐使用
let 用于整数运算 与(())类似
$[] 用于整数运算 不如(())灵活
expr 可以用于整数运算 也可以用于处理字符串 需要注意各种细节 不推荐使用
bc 可以处理整数和小数 Shell本身只支持整数运算 想计算小数需要依赖bc命令
declare -i 将变量声明为整数 功能有限

# Shell (()) 对整数进行数学运算
(())

# let命令 对整数计算
let 表达式
let "表达式"
let '表达式'
let a=1+2
let b=1-2

# if else语句
if condition
then 
	statement(s)
fi
从本质上 if检测的是命令的退出状态
也可以写作
if condition; then
	statement(s)
fi

if else语句
if condition
then
	statement(s)
else 
	statement(s)
fi

if elif else 语句
if condition
then
	statement
elif condition 
then 
	statement
elif condition
then
	statement
else 
	statement
fi
if和elif后都需要跟随then


# 退出状态
所有命令都会返回一个0~255的整数给调用者 这就是命令的退出状态
惯例为返回0为成功 其他为失败 当然也可以自定义返回值的含义


# test命令
用于检测某个条件是否成立 通常和if语句一起使用
test命令有很多选项 可以进行数值、字符串和文件三个方面的检测
test expression 
test命令也可以简写为[]用法为
[ expression ]
1.文件检测相关的test选项
文件类型判断
-b filename 是否存在并且是否是块设备文件
-c filename 是否存在并且是否是字符设备文件
-d filename 是否存在并且是否是目录文件
-e filename 是否存在
-f filename 是否存在并且是否是普通文件
-L filename 是否存在并且是否是符号链接文件
-p filename 是否存在并且是否是管道文件
-s filename 是否存在并且是否为空
-S filename 是否存在并且是否是套接字文件
文件权限判断
-r filename 是否存在并且是否拥有可读权限
-w filename 是否存在并且是否拥有可写权限
-x filename 是否存在并且是否拥有可执行权限
-u filename 是否存在并且是否拥有SUID权限
-g filename 是否存在并且是否拥有SGID权限
-k filename 是否存在并且是否拥有SBIT权限
文件比较
filename1 -nt filename2 判断filename1的修改时间是否比filename2新
filename1 -ot filename2 判断filename1的修改时间是否比filename2旧
filename1 -ef filename2 判断两个文件的inode号是否一致 可以理解为两个文件是否为同一个文件 这是判断硬链接的很好办法
2.数值比较相关的test选项
num1 -eq num2 num1是否等于num2
num1 -ne num2 num1是否不等于num2
num1 -gt num2 num1是否大于num2
num1 -lt num2 num1是否小于num2
num1 -ge num2 num1是否大于等于num2
num1 -le num2 num1是否小于等于num2
test只能用于整数比较 小数相关的比较需要用到bc命令
3.字符串判断相关test选项
-z str 是否为空
-n str 是否不为空
str1 = str2 str1 == str2 都是用来判断是否相等的
str1 != str2 是否不相同
str1 \> str2 str1是否大于str2
str1 \< str2 str1是否小于str2
4.逻辑运算相关的test选项
expression1 -a expression2 逻辑与&&
expression1 -o expression2 逻辑或||
!expression 逻辑非!
在test中使用变量建议用双引号包围起来

# Shell [[]] 检测某个条件是否成立
内置关键字 与test相似
可以看作是test的升级版
用法
[[ expression ]]
不需要将变量名用双引号包围起来 即使是空值也不会报错 
不需要也不能对> <进行转义
[[]]支持逻辑运算符
[[]] 支持正则表达式
用法
[[ str = ~regex ]]

# Shell case in 语句
基本格式
case expression in 
	pattern1)
		statement1
		;;
	pattern2)
		statement2
		;;
	pattern3)
		statement3
		;;
	......
	*)
		statementDefault

esac

case in和正则表达式
* 表示任意字符串
[abc] 匹配abc中的任意一个
[m-n] 表示从m到n中的任意一个
| 表示多重选择 类似逻辑运算中的或运算
不加说明 Shell的值都是字符串

# while循环
while condition
do 
	statement(s)
done
如果条件成立就进入循环

# until循环用法
until condition
do
	statement(s)
done
如果条件不成立就进入循环


# for循环
1.C语言风格的for循环
for((exp1;exp2;exp3))
do
	statements
done
2.Python风格的for in循环
for variable in value_list
do
	statements
done
对value_list的说明
A.直接给出具体的值
for v in "A" "B" "C"
do
	echo ${v}
done
B.给出一个取值范围
for n in {1..100}
do
	echo ${n}
done
C.使用命令的执行结果
for n in $(seq 2 2 100)
do
	echo ${n}
done
D.使用Shell通配符
for filename in *.sh
do
	echo ${filename}
done
可以不用ls也能显示当前目录下的所有脚本文件
E.使用特殊变量
for str in $@
do
	echo $str
done

# select in循环
是Shell独有的一种循环 非常适合终端
格式
select variable in value_list
do 
	statement
done

# break和continue跳出循环
break n
n表示跳出循环的层数 如果省略n则表示跳出所有循环
continue n
n表示循环的层数 如果省略n则只对当前层次的循环有效


# Shell函数详解
格式function name() {
	statements
	[return value]
}
简化写法
name() {
	statements 
	[return value]
}

# 函数参数
见参数

# 重定向
分为两种 输入重定向 和 输出重定向
1.硬件设备和文件描述符
文件描述符 	文件名	类型			硬件
0			stdin	标准输入文件	键盘等
1			stdout	标准输出文件	显示器
2			stderr	标准错误输出文件显示器
2.输出重定向
标准输出重定向				command > file		覆盖方式吧command的正确输出结果输出到file中
							command >> file		以追加的方式
标准错误输出重定向			command 2> file		以覆盖方式 将错误信息输出到file文件中
							command 2>> file  	以追加的方式
正确输出和错误信息同时保存	command >file 2>&1 	以覆盖方式 将正确和错误输出到同一个文件中
							command >> file 2> &1 以追加的方式
							command > file1 2> file2 以覆盖的方式将正确输出到file1 错误输出到file2
							command >> file1 2 >> file2 以追加的方式
> 表示覆盖
>> 表示追加
3.输入重定向
command < file 将file文件的内容作为command的输入
command << END 从标准输入中读取数据 直到遇见分界符END （可以自定义)
command < file1 > file2 将file1作为command的输入 将command的处理结果输出到file2


# 管道
|连接多个命令 称为管道符
格式
command1|command2[|command3...]
两个命令设置管道时 左边命令的输出变成右边命令的输入
1.重定向和管道的区别
重定向操作符将命令和文件连接起来
而管道则是将两个命令连接起来


# 常用命令
- grep
强大的文本搜索工具 使用正则表达式搜索文本 并把匹配的行打印出来
常用选项
-V 版本号
-E 解析正则表达式 相当于使用egrep
-F 解释正则表达式作为固定字符串的列表 由换行符分隔 相当于使用fgrep
-G 默认值 使用grep
匹配控制选项
-e 使用正则表达式作为模式
-f 指定规则文件
-i 搜索忽略大小写
-v 返回匹配 选择没有匹配的内容
-w 匹配整词
-x 仅选择与整行完全匹配的匹配项
-y 与-i相同
一般输出控制选项
-c 抑制正常输出
--color 关键字高亮显示
-L 列出文件内容不符合匹配指定的范本样式的文件名称
-l 列出文件内容符合匹配指定的范本样式的文件名称
-m num 当匹配内容达到num行后 停止搜索 并输出匹配内容
-o 只输出匹配的具体字符串
-q 安静模式 有匹配返回0 没有返回非0
-s 不会输出查找过程中出现的任何错误信息
输出线前缀控制
-b 输出每一个匹配行时在其前附加上偏移量
-H 在每一个匹配行前加上文件名（针对单个文件） 查找多个文件时默认打开
-h 禁止文件名前缀 
--label=LABEL 显示实际来自标准输入的输入作为来自文件LABEL的输入
-n 输出匹配内容的同时输出其所有行号
-T 对齐
上下文线控制选项
-A num 匹配到搜索到的行以及该行下的num行
-B num 匹配到搜索到的行以及该行上的num行
-C num 匹配到搜索到的行以及该行上下的各num行
文件和目录选择选项
-a 处理二进制文件 就像文本 相当于--binary-files=text
--binary-fiels=TYPE 如果文件前几个字节指示文件包含二进制数据 则假定该文件的类型TYPE 默认是二进制
-D 如果输入文件是设备，FIFO或套接字，请使用ACTION处理。默认情况下，读取ACTION，这意味着设备被读取，就像它们是普通文件一样。如果跳过ACTION，设备为 默默地跳过。
-d  如果输入文件是目录，请使用ACTION处理它。默认情况下，ACTION是读的，这意味着目录被读取，就像它们是普通文件一样。如果跳过ACTION，目录将静默跳过。如果ACTION是recurse，grep将递归读取每个目录下的所有文件;这是相当于-r选项。
--exclude=GLOB 跳过基本名称与GLOB匹配的文件（使用通配符匹配）。文件名glob可以使用*，？和[...]作为通配符，和\引用通配符或反斜杠字符。搜索其文件名和GLOB通配符相匹配的文件的内容来查找匹配使用方法:grep -H --exclude=c* "old" ./*  c*是通配文件名的通配符./* 指定需要先通配文件名的文件的范围,必须要给*,不然就匹配不出内容,(如果不给*,带上-r选项也可以匹配)

--exclude-from=FILE 在文件中编写通配方案,grep将不会到匹配方案中文件名的文件去查找匹配内容
--exclude-dir=DIR 匹配一个目录下的很多内容同时还要让一些子目录不接受匹配,就使用此选项。
--include=GLOB  仅搜索其基本名称与GLOB匹配的文件（使用--exclude下所述的通配符匹配）。
-R -e 递归读取每个目录下的所有文件 相当于 -d recurse
其他选项
--line-buffered 输出上使用缓冲
--mmap 
-U
-z 
概览
-a   --text   #不要忽略二进制的数据。 将 binary 文件以 text 文件的方式搜寻数据 
-A<显示行数>   --after-context=<显示行数>   #除了显示符合范本样式的那一列之外，并显示该行之后的内容。   
-b   --byte-offset   #在显示符合样式的那一行之前，标示出该行第一个字符的编号。   
-B<显示行数>   --before-context=<显示行数>   #除了显示符合样式的那一行之外，并显示该行之前的内容。   
-c    --count   #计算符合样式的行数。   
-C<显示行数>    --context=<显示行数>或-<显示行数>   #除了显示符合样式的那一行之外，并显示该行之前后的内容。   
-d <动作>      --directories=<动作>   #当指定要查找的是目录而非文件时，必须使用这项参数，否则grep指令将回报信息并停止动作。   
-e<范本样式>  --regexp=<范本样式>   #指定字符串做为查找文件内容的样式。   
-E      --extended-regexp   #将样式为延伸的普通表示法来使用。   
-f<规则文件>  --file=<规则文件>   #指定规则文件，其内容含有一个或多个规则样式，让grep查找符合规则条件的文件内容，格式为每行一个规则样式。   
-F   --fixed-regexp   #将样式视为固定字符串的列表。   
-G   --basic-regexp   #将样式视为普通的表示法来使用。   
-h   --no-filename   #在显示符合样式的那一行之前，不标示该行所属的文件名称。   
-H   --with-filename   #在显示符合样式的那一行之前，表示该行所属的文件名称。   
-i    --ignore-case   #忽略字符大小写的差别。   
-l    --file-with-matches   #列出文件内容符合指定的样式的文件名称。   
-L   --files-without-match   #列出文件内容不符合指定的样式的文件名称。   
-n   --line-number   #在显示符合样式的那一行之前，标示出该行的列数编号。   
-q   --quiet或--silent   #不显示任何信息。   
-r   --recursive   #此参数的效果和指定“-d recurse”参数相同。   
-s   --no-messages   #不显示错误信息。   
-v   --revert-match   #显示不包含匹配文本的所有行。   
-V   --version   #显示版本信息。   
-w   --word-regexp   #只显示全字符合的列。   
-x    --line-regexp   #只显示全列符合的列。   
-y   #此参数的效果和指定“-i”参数相同。

--color=auto ：可以将找到的关键词部分加上颜色的显示


- sed
主要用于自动编辑一个或多个文件 简化对文件的反复操作
格式
sed [options] 'command' files(s)
选项
-e script 以选项中的指定的script来处理输入的文本文件
-f script 以选项中的指定的script文件来处理输入的文本文件
-h 帮助信息
-n 仅显示script处理后的结果
-v 版本
参数
d 删除 选择的行
D 删除模板块的第一行
s 替换指定字符
h 拷贝模板块的内容到内存中的缓冲区
H 追加模板块的内容到内存中的缓冲区
g	获得内存缓冲区的内容，并替代当前模板块中文本
G	获得内存缓冲区的内容，并追加到当前模板块文本的后面
l	列表不能打印字符的清单
n	读取下一个输入行，用下一个命令处理新的行而不是第一个命令
N	追加下一个输入行到模板块后面并在二者间嵌入一个新行，改变当前行号码
p	打印模板块的行
P	打印模板块的第一行
q	退出sed
b label	分支到脚本中带有标记的地方，如果分支不存在则分支到脚本的末尾
r file	从file中读行
t label	if分支，从最后一行开始，条件一旦满足或者T，t命令，将导致分支到带有标号的命令处，或者到脚本的末尾
T label	错误分支，从最后一行开始，一旦发生错误或者T，t命令，将导致分支到带有标号的命令处，或者到脚本的末尾
w file	写并追加模板块到file末尾
W file	写并追加模板块的第一行到file末尾
!	表示后面的命令对所有没有被选定的行发生作用
=	打印当前行号
#	把注释扩展到第一个换行符以前
替换标记
g	表示行内全面替换
p	表示打印行
w	表示把行写入一个文件
x	表示互换模板块中的文本和缓冲区中的文本
y	表示把一个字符翻译为另外的字符（但是不用于正则表达式）
\1	子串匹配标记
&	已匹配字符串标记
元字符集
^	匹配行开始，如：/^sed/匹配所有以sed开头的行。
$	匹配行结束，如：/sed$/匹配所有以sed结尾的行。
.	匹配一个非换行符的任意字符，如：/s.d/匹配s后接一个任意字符，最后是d。
*	匹配0个或多个字符，如：/*sed/匹配所有模板是一个或多个空格后紧跟sed的行。
[]	匹配一个指定范围内的字符，如/[sS]ed/匹配sed和Sed。
[^]	匹配一个不在指定范围内的字符，如：/[^A-RT-Z]ed/匹配不包含A-R和T-Z的一个字母开头，紧跟ed的行。
(..)	匹配子串，保存匹配的字符，如s/(love)able/\1rs，loveable被替换成lovers。
&	保存搜索字符用来替换其他字符，如s/love/&/，love这成love。
<	匹配单词的开始，如:/<love/匹配包含以love开头的单词的行。
 >	匹配单词的结束，如/love>/匹配包含以love结尾的单词的行。
x{m}	重复字符x，m次，如：/0{5}/匹配包含5个0的行。
x{m,}	重复字符x，至少m次，如：/0{5,}/匹配至少有5个0的行。
x{m,n}	重复字符x，至少m次，不多于n次，如：/0{5,10}/匹配5~10个0的行。


简单的文件内容替换命令
sed -i "s/需要替换的内容/替换的内容/g" 文件名 # -i表示修改本地文件 不然只是修改输出到标准输出中的内容
sed -i[r] "s/需要替换的内容/替换的内容/g" $(grep "需要替换的内容" 替换文件所有目录) # -r表示备份 使用grep查出对应的文件后替换









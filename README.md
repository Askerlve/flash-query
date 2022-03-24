### 背景
> &nbsp;&nbsp;去年年初的时候，由于项目周期太紧，所以向领导提交了人力申请。等了一个周，领导给我调剂了一位骚气的高级PHP开发到我们项目组。
> 虽然我们的项目是JAVA开发的，但考虑到写代码主要还是看逻辑思维，只要逻辑思维正确，实现无非就是适当的选择API来实现，应该问题
> 不大，所以就接受了，让他帮忙做控制台数据管理方面的需求。<br/>
> &nbsp;&nbsp;这位骚气的高级PHP在熟悉了几天代码以后，突然跑到我的工位，诚恳的问道："诶，Askerlve，咱们Java的生态那么成熟，按理说
> Mybatis应该有基于注解的查询方式，但是我看我们的代码里面都是接口定义了入参实体，然后手动生成了一个QueryWrapper再去数据库查询，
> 它没有根据注解自动生成查询语句的机制吗？如果没有，那为什么不封装一个呢？"我转念一想，答道："好像确实没有诶!" "那可以搞一个!"，
> 这位兄弟面带鄙夷的说道。虽然PHP是世界上最好的语言，咱JAVA可不能差，所以这个功能模块就应运而生。

### 功能
1. 增加注解功能(EQ/GT/GE/NE/LIKE/GROUP等)，扩展Mybatis-plus的模版方法，使其可支持基于注解的方式生成QueryWrapper。
2. 引入[mybatis-plus-join](https://github.com/yulichang/mybatis-plus-join) 支持Join查询
3. 统一解决mybatis-plus基于Mysql进行模糊查询时特殊字符（`%`/`_`/`\`）转义

### 示例
- 定义入参
  ```java
    @Data
    public class TestListDTO extends PageQuery {
    
        @EQ(field = "id")
        private Long id;
    
        @LIKE(field = "name", type = SqlLike.LEFT)
        private String name;
    
        @GT(field = "age")
        private Integer age;
    
    }
  ```
- 定义接口
  ```java
    @RestController
    @RequestMapping("/test")
    @RequiredArgsConstructor
    public class TestController {
    
        private final ITestRepository testRepository;
    
        @PostMapping("/list")
        public Page<Test> list(@RequestBody TestListDTO listDTO) {
            return testRepository.page(listDTO);
        }
    
    }
  ```
- 编写Repository
  ```java
    public interface ITestRepository extends Repository<Test, Long> {
    }
  ```
  ```java
    @Service
    public class TestRepositoryImpl extends BaseRepository<Test, Long, ITestService>
    implements ITestRepository {
    }
  ```
- 准备数据
  ```sql
    CREATE TABLE `Test` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(64) DEFAULT NULL,
    `age` int(11) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;
  
    INSERT INTO `Test` (`id`, `name`, `age`)
    VALUES
    (1, 'AA', 10),
    (2, 'AB', 11),
    (3, 'AC', 11),
    (4, 'DA', 12),
    (5, 'EA', 12),
    (6, 'F%A', 13),
    (7, 'G', 14),
    (8, 'H', 15),
    (9, 'I', 16),
    (10, 'J', 17),
    (11, 'K', 18),
    (12, 'L', 19),
    (13, 'M', 20),
    (14, 'N', 21),
    (15, 'O', 22),
    (16, 'P', 23),
    (17, 'Q', 23),
    (18, 'R', 23),
    (19, 'S', 23),
    (20, 'T', 23),
    (21, 'U', 24);
  ```
- 调用测试
  ```
    curl --location --request POST 'http://localhost:8001/test/list' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "id": null,
    "name":"%A",
    "age": null
    }'
  ```
  ```
  {
    "records": [
        {
            "id": 6,
            "name": "F%A",
            "age": 13
        }
    ],
    "total": 1,
    "size": 10,
    "current": 1,
    "pages": 1
  }
  ```
  
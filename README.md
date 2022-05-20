### 背景
> 对于Mybatis的使用，我们常见的操作是在接口定义接口参数，然后在Service层将接口参数映射成QueryWrapper生成SQL交给数据库执行。每次新接口的定义，我们都需要在Service层去做无任何难度的QueryWrapper映射。对于管控后端系统来说，大量增删改查接口让我们的这种无难度、类重复的工作进一步加大。

> 思考：Mybatis有没有基于注解类似@EQ(“name”)的方式直接将接收的参数对象直接映射成QueryWrapper的开源组件？我们在开源市场调研了一圈，发现并没有类似的组件能解决我们的问题。在此背景下，Flash Query组件应运而生。	

> Flash Query在Mybatis Plus IService的基础上，新增方法update/page/list/find/count/delete(Query query)等模版方法，支持@EQ/@BETWEEN/@GE@GROUP@GROUPS@GT@IN@LE/@NE@RANGE等注解。业务在接入的时候只将接收参数的对象继承查询对象Query，并在需要组装成查询条件的属性上打上相应的注解，调用扩展的模版方法时即可无需再关注QueryWrapper的映射。具备接入简单、使用方便特点，使代码更加整洁，极大的提高开发效率（对于常见的CRUD操作接口来说，至少提速30%以上）。在Mybatis Plus IService提供的模版方法上只做增强，不做改变，引入它不会对现有的工程产生影响，如丝般顺滑。

### 功能
1. 增加注解功能(EQ/GT/GE/NE/LIKE/GROUP等)，扩展Mybatis-plus的模版方法，使其可支持基于注解的方式生成QueryWrapper。
2. 引入[mybatis-plus-join](https://github.com/yulichang/mybatis-plus-join) 支持Join查询。
3. 统一解决mybatis-plus基于进行模糊查询时特殊字符（`%`/`_`/`\`）转义。

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
  
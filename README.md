# Quick Start

> 本工程是对Spring Boot 2.0.6.RELEASE的扩展，主要提供了接口注解支、`Feign` 统一日志输出、`SpringQueryMap`注解支持

- 引入依赖

  ```xml
   		 <dependency>
                  <groupId>com.aihuishou.c2b.service.framework</groupId>
                  <artifactId>customize-boot2</artifactId>
                  <version>1.0.0-SNAPSHOT</version>
              </dependency>
  ```

- 激活注解

  - 在应用程序驱动类上添加`@AdvancedSpringBootApplication`注解

    ```java
    @AdvancedSpringBootApplication
    public class DispatcherServiceApplication extends SpringBootServletInitializer {
    
        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
            builder.sources(DispatcherServiceApplication.class);
            return super.configure(builder);
        }
    
        public static void main(String[] args) {
            new SpringApplicationBuilder()
                    .sources(DispatcherServiceApplication.class)
                    .run();
        }
    
    }
    
    ```

    > 说明 `@AdvancedSpringBootApplication`= `@SpringBootApplication` + `@EnableBoot2Customizing` + `@EnableFeignClients`

    > `@EnableBoot2Customizing`用于自定义化Spring Boot2配置，详细请参考`CustomizedAutoConfiguration`类

    > 如果项目中不需要`Feign`可进行单独注解

- 请排除原声的`io.springfox:springfox-spring-web`依赖，引入定制化的`springfox-spring-web`

  > 说明: 原生的springfox-spring-web无法获取接口上的注解

  > `io.springfox:springfox-spring-web`支持获取接口上的注解

- 按照上述进行配置，理论上可使用前文所描述的所有功能，如有不符合，欢迎一起探讨。
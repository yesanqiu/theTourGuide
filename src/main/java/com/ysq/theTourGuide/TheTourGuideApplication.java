package com.ysq.theTourGuide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@EnableScheduling
@SpringBootApplication
@EnableSwagger2
@MapperScan("com.ysq.theTourGuide.mapper")
public class TheTourGuideApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheTourGuideApplication.class, args);
    }


//    @Bean
//    public ServletWebServerFactory servletContainer() {
//
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
//
//            @Override
//            protected void postProcessContext(Context context) {
//
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                securityConstraint.addCollection(collection);
//                context.addConstraint(securityConstraint);
//            }
//        };
//        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
//        return tomcat;
//    }
//
//    /**
//     * 让我们的应用支持HTTP是个好想法，但是需要重定向到HTTPS，
//     * 但是不能同时在application.properties中同时配置两个connector，
//     * 所以要以编程的方式配置HTTP connector，然后重定向到HTTPS connector
//     * @return Connector
//     */
//    private Connector initiateHttpConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(80); // http端口
//        connector.setSecure(false);
//        connector.setRedirectPort(7331); // application.properties中配置的https端口
//        return connector;
//    }

}

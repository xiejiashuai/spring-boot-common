package com.aihuishou.c2b.service.common.config.web.servlet;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * speed up web configuration
 *
 * @author jiashuai.xie
 */
@Configuration
@ConditionalOnClass(Connector.class)
@AutoConfigureBefore(name = {
        "org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration",
        "org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration"
})
@AutoConfigureAfter(name = {
        "org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration",
        "org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration"
})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SpeedUpWebConfiguration {

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new NoopHiddenHttpMethodFilter();
    }

    @Bean
    public HttpPutFormContentFilter httpPutFormContentFilter() {
        return new NoopHttpPutFormContentFilter();
    }

    public static class NoopHiddenHttpMethodFilter extends HiddenHttpMethodFilter implements Ordered {

        private int order = -10000;

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
            return true;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        @Override
        public int getOrder() {
            return order;
        }
    }



    public static class NoopHttpPutFormContentFilter extends HttpPutFormContentFilter implements Ordered{

        private int order = -9900;

        @Override
        protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
            return true;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        @Override
        public int getOrder() {
            return order;
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "server.tomcat.thread", value = "waitTime", matchIfMissing = true)
    public static class TomcatWebServerFactoryConfiguration implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

        @Value("${server.tomcat.thread.waitTime:30}")
        private int waitTime;

        @Bean
        public TomcatConnectorCustomizer tomcatConnectorCustomizer(){
            GracefulShowDownTomcatConnectorCustomizer connectorCustomizer = new GracefulShowDownTomcatConnectorCustomizer();
            connectorCustomizer.setWaitTime(waitTime);
            return connectorCustomizer;
        }

        @Override
        public void customize(TomcatServletWebServerFactory factory) {
            // cancel register DefaultServlet
            factory.setRegisterDefaultServlet(false);
            // add graceful shutdown
            factory.addConnectorCustomizers(tomcatConnectorCustomizer());

        }

    }

    public static class GracefulShowDownTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

        private final Logger LOGGER = LoggerFactory.getLogger(GracefulShowDownTomcatConnectorCustomizer.class);

        private Connector connector;

        private int waitTime;

        @Override
        public void customize(Connector connector) {
            this.connector = connector;
        }

        @EventListener(ContextClosedEvent.class)
        public void onEvent(ContextClosedEvent contextClosedEvent) {

            this.connector.pause();

            Executor executor = this.connector.getProtocolHandler().getExecutor();

            if (null != executor && executor instanceof ThreadPoolExecutor) {
                try {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                    threadPoolExecutor.shutdown();
                    if (!threadPoolExecutor.awaitTermination(waitTime, TimeUnit.SECONDS)) {
                        LOGGER.info("Tomcat thread pool did not shut down gracefully within {} seconds. Proceeding with forceful shutdown", waitTime);
                        return;
                    }
                    LOGGER.info("Tomcat thread pool success to  shut down gracefully within {} seconds", waitTime);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }

        }

        public void setWaitTime(int waitTime) {
            this.waitTime = waitTime;
        }

    }


}

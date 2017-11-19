package opta.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class OptaBootApplication extends SpringBootServletInitializer {

//    @Bean
//    GraphHopperAPI graphHopper() {
//        GraphHopper hopper = new GraphHopperOSM().forServer();
//        hopper.setDataReaderFile("/home/thiago/Downloads/belgium-latest.osm.pbf");
//        hopper.setGraphHopperLocation("/tmp/gh-tmp");
//        hopper.setEncodingManager(new EncodingManager("car"));
//        hopper.importOrLoad();
//        return hopper;
//    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(OptaBootApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(OptaBootApplication.class, args);
    }

}
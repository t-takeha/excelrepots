package jp.dip.cloudlet.poitest.excelreports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 起動プログラム
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages= {"jp.dip.cloudlet.poitest.excelreports.*"})
public class SimplePoiReport {
    private static final Logger log = LogManager.getLogger(SimplePoiReport.class);

    public static void main(String[] args) {
        log.debug("### START SimplePoiReport ###");
        SpringApplication.run(SimplePoiReport.class, args);
    }
}

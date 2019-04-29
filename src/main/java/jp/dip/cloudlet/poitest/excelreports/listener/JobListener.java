package jp.dip.cloudlet.poitest.excelreports.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * ジョブリスナーの定義.
 * ジョブ実行前後の時間計測を行っています。
 */
@Component
public class JobListener extends JobExecutionListenerSupport {
    private static final Logger log = LogManager.getLogger(JobListener.class);
    private static final String STOP_WATCH_ID = "idExcelReports";

    private StopWatch stopWatch;

    /**
     * 時間計測用Beanの生成
     * @return
     */
    @Bean
    public StopWatch stopWatch() {
        stopWatch = new StopWatch(STOP_WATCH_ID);
        return stopWatch;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);
        // 時間計測開始
        stopWatch.start();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);
        // 時間計測終了
        stopWatch.stop();

        // 結果を出力
        log.info(stopWatch.prettyPrint());
    }
}

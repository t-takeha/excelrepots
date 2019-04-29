package jp.dip.cloudlet.poitest.excelreports.config;

import jp.dip.cloudlet.poitest.excelreports.listener.JobListener;
import jp.dip.cloudlet.poitest.excelreports.tasklet.CreateSXSSFWorkbookReportJobTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Excel帳票出力バッチをSpring Bootに登録するConfigurationクラス
 */
@Configuration
@EnableBatchProcessing
public class SimplePoiReportBatchConfig extends DefaultBatchConfigurer {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    CreateSXSSFWorkbookReportJobTasklet tasklet;

    @Autowired
    JobListener jobListener;

    /**
     * Excel帳票出力ジョブBeanを生成する.(SXSSFWorkbook版）
     * @return
     */
    @Bean
    public Job createSXSSFWorkbookReportJob() {
        return jobBuilderFactory.get("createSXSSFWorkbookReportJob")
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .start(stepBuilderFactory.get("createSXSSFWorkbookReportStep").tasklet(tasklet).build())
                .build();
    }
}

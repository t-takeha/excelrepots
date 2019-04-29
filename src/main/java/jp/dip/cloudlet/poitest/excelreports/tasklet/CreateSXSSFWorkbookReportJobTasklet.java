package jp.dip.cloudlet.poitest.excelreports.tasklet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * SXSSFWorkbook版のExcel帳票（マクロ付きBookです）を生成する.
 * [仕様]
 * ・30万行
 * ・30列
 * ・セル毎の書式なし
 * ・出力ファイル名は「sxssf.xlsm」
 * ・テンプレートファイル「template.xlsm」を読み込む
 * ・書き込み先のシート名「dataSheet」
 */
@Component
public class CreateSXSSFWorkbookReportJobTasklet implements Tasklet {
    private static final Logger log = LogManager.getLogger(CreateSXSSFWorkbookReportJobTasklet.class);

    // ファイルセパレータ
    private static final String fsp = FileSystems.getDefault().getSeparator();

    // 書き込み行数
    private static final int MAX_ROW_NUMBER = 1;

    // ヒープにキャッシュするサイズ
    private static final int WINDOW_SIZE = 100;

    // 行データ
    private static final String[] ROW_DATA = {
            "1111111111", "2222222222", "3333333333", "4444444444", "5555555555", "6666666666", "7777777777", "8888888888", "9999999999", "0000000000",
            "a111111111", "a222222222", "a333333333", "a444444444", "a555555555", "a666666666", "a777777777", "a888888888", "a999999999", "a000000000",
            "b111111111", "b222222222", "b333333333", "b444444444", "b555555555", "b666666666", "b777777777", "b888888888", "b999999999", "b000000000"};

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)  throws  Exception{
        log.debug("#### START CreateSXSSFWorkbookReportJobTasklet ####");

        /*
         * テンプレートからWookBookオブジェクトを生成する
         */
        SXSSFWorkbook workbook = null;
        Path templatePath = Paths.get("template" + fsp + "template.xlsm");
        log.debug("#### template file = " + templatePath.toString());

        try (InputStream in = new ByteArrayInputStream(Files.readAllBytes(templatePath))) {
            // テンプレートを読み込んでXSSFWorkbookを生成する.
            // XSSFWorkbookからSXSSFWorkbookを作り出す
            // (note) 一旦バイト変換しているのはオリジナルが書き換わるため
            workbook = new SXSSFWorkbook(XSSFWorkbookFactory.createWorkbook(in), WINDOW_SIZE);
            log.debug("#### SXSSFWorkbook = " + workbook.toString());
        }

        /*
         * wookbookの書式設定
         */
        // 圧縮なし
        workbook.setCompressTempFiles(true);

        // 書き込み先のシート取得
        SXSSFSheet worksheet = workbook.getSheet("dataSheet");
        log.debug("#### getLastRowNum : " + worksheet.getLastRowNum());

        /*
         * セルに書き込む
         */
        for (int row = 0; row < MAX_ROW_NUMBER; row++) {
            // row位置に行を生成
            Row dataRow = worksheet.createRow(row);

            for (int col = 0; col < ROW_DATA.length; col++) {
                // 行からcellを作り出す
                dataRow.createCell(col, CellType.STRING).setCellValue(ROW_DATA[col]);
            }
        }

        /*
         * 出力ファイルに書き出す
         */
        Path dstPath = Paths.get("result" + fsp + "sxssf.xlsm");
        try (OutputStream os = new FileOutputStream(dstPath.toFile())) {
            log.debug("#### write wookbook start");
            workbook.write(os);

            log.debug("#### write wookbook end   = " + dstPath.toString());
        } catch (IOException e) {
            throw e;
        } finally {
            if (workbook != null) {
                workbook.close();
                workbook.dispose();
            }
        }
        log.debug("#### END   CreateSXSSFWorkbookReportJobTasklet ####");

        return RepeatStatus.FINISHED;
    }
}

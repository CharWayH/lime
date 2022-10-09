package com.charwayh.lime.cron.csv;

import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvRowHandler;
import lombok.Data;
import lombok.Getter;

/**
 * 统计当前CSV文件有多少行
 *
 * @author charwayH
 */
@Data
@Getter
public class CountFileRowHandler implements CsvRowHandler {

    private long rowSize;

    @Override
    public void handle(CsvRow csvRow) {
        rowSize++;
    }
}

package com.huazie.frame.db.common.table.split;

import com.huazie.frame.db.common.exception.TableSplitException;

import java.io.Serializable;

/**
 * <p> 分表接口类定义 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ITableSplit extends Serializable {

    /**
     * <p> 分表后缀转换 </p>
     *
     * @param tableSplitColumn 分表字段
     * @return 分表后缀名
     * @throws TableSplitException 分表实现异常类
     */
    String convert(Object tableSplitColumn) throws TableSplitException;
}

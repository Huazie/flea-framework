package com.huazie.frame.db.common.tab.split.impl;

import com.huazie.frame.db.common.DBConstants;
import com.huazie.frame.db.common.exception.TableSplitException;

/**
 * <p> 两位十六进制分表实现 </p>
 *
 * @author huazie
 * @version 1.0.0
 * @since 1.0.0
 */
public final class TwoHexTableSplitImpl extends AbstractTableSplitImpl {

    private static final long serialVersionUID = 142194326684219859L;

    @Override
    public String convert(String tableName, Object tableSplitColumn) throws TableSplitException {
        return convert(tableName, tableSplitColumn, DBConstants.TableSplitConstants.TWO);
    }

}

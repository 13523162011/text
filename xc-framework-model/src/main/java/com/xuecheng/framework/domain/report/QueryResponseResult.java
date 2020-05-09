package com.xuecheng.framework.domain.report;

import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;

public class QueryResponseResult extends ResponseResult {
    //结果集对象
    private QueryResult queryResult;

    public QueryResponseResult(ResultCode resultCode, QueryResult queryResult) {
        super(resultCode);
        this.queryResult = this.queryResult;
    }
}

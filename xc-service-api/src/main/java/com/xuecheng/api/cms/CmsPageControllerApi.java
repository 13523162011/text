package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.report.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

public interface CmsPageControllerApi {
    /**
     *根据条件分页查询CmsPage页面信息集合
     *
     * @return*/
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
     @ApiOperation("添加页面")
     public CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("通过ID查询页面")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "Id值", required = true,
                    paramType = "path", dataType = "String")
    )
    CmsPage findByid(String id);
    /**
     * 根据CmsPage的Id值修改CmsPage数据
     * @param id   String  CmsPage的Id值
     * @param cmsPage 要修改的CmsPage数据
     * @return CmsPageResult Cms定义的规范响数据
     */
    @ApiOperation("修改页面")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "id", value = "Id值", required = true,
                    paramType = "path", dataType = "String")
    )
    public CmsPageResult edit(String id,CmsPage cmsPage);
    @ApiOperation("根据id删除")
    ResponseResult delete(String id);
}

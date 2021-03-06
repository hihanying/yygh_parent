package top.hihanying.yygh.cmn.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.hihanying.yygh.cmn.service.DictService;
import top.hihanying.yygh.common.result.Result;
import top.hihanying.yygh.model.cmn.Dict;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 访问：http://localhost:8205/swagger-ui.html#/
 */
@Api(tags = "数据字典接口")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController {
    @Autowired
    private DictService dictService;

    /**
     * 导入数据字典接口
     * @param file
     * @return success
     */
    @ApiOperation(value = "导入数据字典接口")
    @PostMapping("importData")
    public Result importDict(MultipartFile file) {
        dictService.importDictData(file);
        return Result.ok();
    }

    /**
     * 导出数据字典接口
     * @param response
     */
    @ApiOperation(value = "导出数据字典接口")
    @GetMapping("exportDictData")
    public void exportDictData(HttpServletResponse response) {
        dictService.exportDictData(response);
    }

    /**
     * 根据dictCode获取下级节点
     * @param dictCode
     * @return
     */
    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return Result.ok(list);
    }

    /**
     * 根据数据id查询子数据列表
     * @param id
     * @return list
     */
    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value) {
        String dictName = dictService.getDictName(dictCode,value);
        return dictName;
    }

    //根据value查询
    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value) {
        String dictName = dictService.getDictName("",value);
        return dictName;
    }

}

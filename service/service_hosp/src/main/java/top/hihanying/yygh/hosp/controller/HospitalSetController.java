package top.hihanying.yygh.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import top.hihanying.yygh.common.result.Result;
import top.hihanying.yygh.common.util.MD5;
import top.hihanying.yygh.hosp.service.HospitalSetService;
import top.hihanying.yygh.model.hosp.HospitalSet;
import top.hihanying.yygh.vo.hosp.HospitalSetQueryVo;

import java.util.List;
import java.util.Random;


/**
 * 访问：http://localhost:8201/swagger-ui.html#/
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
@Api(tags = "医院设置管理")
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 1. 增加记录
     * @param hospitalSet 添加的记录
     * @return 添加成功或失败
     */
    @PostMapping("addHospitalSet")
    @ApiOperation(value = "addHospitalSet", notes = "添加医院设置信息")
    public Result addHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 设置状态： 1使用 ， 0不能使用
        hospitalSet.setStatus(1);
        // 设置签名密钥字段(使用MD5加密)
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + new Random().nextInt(1000)));
        // 调用方法执行添加
        boolean flag = hospitalSetService.save(hospitalSet);
        // 返回调用结果
        return flag ? Result.ok() : Result.fail();
    }

    /**
     * 2.1 删除记录：通过 id 逻辑删除一条记录
     * @param id 医院的 id
     * @return 逻辑删除成功或者失败
     */
    @DeleteMapping("deleteHospitalSetById/{id}")
    public Result deleteHospitalSetById(@PathVariable Long id) {
        // 尝试使用 id 进行逻辑删除
        boolean flag = hospitalSetService.removeById(id);
        // 返回删除结果
        return flag ? Result.ok() : Result.fail();
    }

    /**
     * 2.2 删除记录：通过多个 id 逻辑删除多条记录
     * @param ids 多个医院的 id
     * @return 逻辑删除成功或者失败
     */
    @DeleteMapping("deleteBatchHospitalSetById")
    public Result deleteBatchHospitalSetById(@RequestBody List<Long> ids) {
        // 尝试使用 ids 进行逻辑删除
        boolean flag = hospitalSetService.removeByIds(ids);
        // 返回删除结果
        return flag ? Result.ok() : Result.fail();
    }

    /**
     * 3. 修改记录：通过传入的记录替换数据库中的记录
     * @param hospitalSet 更新后的医院记录
     * @return 修改成功或者失败
     */
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 尝试更新指定医院记录
        boolean flag = hospitalSetService.updateById(hospitalSet);
        // 返回修改结果
        return flag ? Result.ok() : Result.fail();
    }

    /**
     * 4.1 查询记录：根据 id 查询一条记录
     * @param id 所查询记录的 id
     * @return 所查询的记录
     */

    @GetMapping("getAllHospitalSet/{id}")
    public Result getHospitalSetById(@PathVariable Long id){
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return hospitalSet == null ? Result.fail() : Result.ok(hospitalSet);
    }

    /**
     * 4.2 查询记录：查询所有记录
     * @return 返回查询的记录列表
     */
    @GetMapping("getAllHospitalSet")
    @ApiOperation(value = "getAll", notes = "获取所有医院设置信息")
    public Result getAllHospitalSet(){
        List<HospitalSet> list = hospitalSetService.list();
        return list == null ? Result.fail() : Result.ok(list);
    }

    @PostMapping("getPageHospitalSet/{current}/{limit}")
    public Result getPageHospitalSet(@PathVariable Long current,
                                     @PathVariable Long limit,
                                     @RequestBody(required = false)HospitalSetQueryVo  hospitalSetQueryVo) {
        IPage<HospitalSet> page = new Page<>(current, limit);
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)) {
            wrapper.like("hosname", hosname);
        }
        if (!StringUtils.isEmpty(hoscode)) {
            wrapper.eq("hoscode", hoscode);
        }
        IPage<HospitalSet> pageHospitalSet = hospitalSetService.page(page, wrapper);
        return Result.ok(pageHospitalSet);
    }

    /**
     * 5. 医院设置表锁定和解锁
     * @param id 根据 id 设置
     * @param status 需要设置的状态
     * @return 返回设置成功
     */
    @ApiOperation(value = "lockHospitalSet 医院设置表锁定和解锁")
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        // 根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // 设置状态
        hospitalSet.setStatus(status);
        // 调用更新方法
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }

    /**
     * 6. 发送签名密钥
     * @param id 医院设置记录的 id
     * @return Result.ok()
     */
    @ApiOperation(value = "sendKeyHospitalSet 发送签名密匙")
    @PutMapping("sendKey/{id}")
    public Result sendKeyHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // 签名密钥
        String signKey = hospitalSet.getSignKey();
        // 医院编号
        String hoscode = hospitalSet.getHoscode();
        // TODO 发送短信

        return Result.ok();
    }
}

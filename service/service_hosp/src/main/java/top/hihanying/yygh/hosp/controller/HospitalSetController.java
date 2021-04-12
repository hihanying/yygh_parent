package top.hihanying.yygh.hosp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hihanying.yygh.common.result.Result;
import top.hihanying.yygh.hosp.service.HospitalSetService;
import top.hihanying.yygh.model.hosp.HospitalSet;

import java.util.List;

/**
 * 访问：http://localhost:8201/swagger-ui.html/
 */


@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    //查询医院设置表的所有信息
    @GetMapping("/getAll")
    public Result getAll(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    //逻辑删除医院
    @DeleteMapping("/deleteHospitalById/{id}")
    public Result deleteHospitalById(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
}

package top.hihanying.yygh.hosp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.hihanying.yygh.model.hosp.HospitalSet;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);
}

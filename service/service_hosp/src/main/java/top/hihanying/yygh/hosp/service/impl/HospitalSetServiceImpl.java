package top.hihanying.yygh.hosp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hihanying.yygh.hosp.mapper.HospitalSetMapper;
import top.hihanying.yygh.hosp.service.HospitalSetService;
import top.hihanying.yygh.model.hosp.HospitalSet;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet>
        implements HospitalSetService {
}

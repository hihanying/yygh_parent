package top.hihanying.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.hihanying.yygh.cmn.listener.DictListener;
import top.hihanying.yygh.cmn.mapper.DictMapper;
import top.hihanying.yygh.cmn.service.DictService;
import top.hihanying.yygh.model.cmn.Dict;
import top.hihanying.yygh.vo.cmn.DictEeVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    /**
     * 根据数据id查询子数据列表
     *
     * @param id
     * @return list
     */
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        for (Dict dict : dictList) { // 向list集合每个dict对象中设置hasChildren
            Long dictId = dict.getId();  // 得到每一条记录的id值
            boolean isChild = this.isChildren(dictId); // 调用hasChildren方法判断是否包含子节点
            dict.setHasChildren(isChild); // 为每条记录设置hasChildren属性
        }
        return dictList;
    }

    /**
     * 判断id下面是否有子节点
     *
     * @param id
     * @return boolean
     * true:有子结点
     * false:无子结点
     */
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

    /**
     * 导出数据字典接口
     *
     * @param response
     */
    @Override
    public void exportDictData(HttpServletResponse response) {
        // 设置下载信息
        response.setContentType("application/vnd.ms-excel"); // 设置输出的文件格式
        response.setCharacterEncoding("utf-8"); // 设置编码集
        String fileName = "dict"; // 设置文件名
        // 拼接输出的文件名和文件格式
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        // 查询数据库
        List<Dict> dictList = baseMapper.selectList(null);
        //Dict -- DictEeVo
        List<DictEeVo> dictVoList = new ArrayList<>();
        // 将查询数据库的数据封装到对应的vo类中，并装到vo集合中
        for (Dict dict : dictList) {
            DictEeVo dictEeVo = new DictEeVo();
            // dictEeVo.setId(dict.getId());
            BeanUtils.copyProperties(dict, dictEeVo);
            dictVoList.add(dictEeVo);
        }
        // 调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class)
                    .sheet("dict")
                    .doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入数据字典
     *
     * @param file
     */
    @Override
    @CacheEvict(value = "dict", allEntries=true)
    public void importDictData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),
                    DictEeVo.class,
                    new DictListener(baseMapper)
            ).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //根据dictcode和value查询
    @Override
    public String getDictName(String dictCode, String value) {
        //如果dictCode为空，直接根据value查询
        if (StringUtils.isEmpty(dictCode)) {
            //直接根据value查询
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else {//如果dictCode不为空，根据dictCode和value查询
            //根据dictcode查询dict对象，得到dict的id值
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long parent_id = codeDict.getId();
            //根据parent_id和value进行查询
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parent_id)
                    .eq("value", value));
            return finalDict.getName();
        }
    }

    //根据dictCode获取下级节点
    @Override
    public List<Dict> findByDictCode(String dictCode) {
        //根据dictcode获取对应id
        Dict dict = this.getDictByDictCode(dictCode);
        //根据id获取子节点
        List<Dict> chlidData = this.findChildData(dict.getId());
        return chlidData;
    }

    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict codeDict = baseMapper.selectOne(wrapper);
        return codeDict;
    }


}

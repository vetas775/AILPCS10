package com.${packageName}.service;

import java.util.List;
import java.util.Map;
import com.${packageName}.entity.${objectName}DO;
import com.${packageName}.entity.core.Page2;
import com.${packageName}.entity.core.PageData;

/** 
 * ${objectName}业务层接口.  ${TITLE}
 * 创建人：MichaelTsui
 * 创建时间：${nowDate?string("yyyy-MM-dd")}
 * @version 1.0
 */
public interface ${objectName}Service {
    /**
	  *1 of 7 ${objectNameLower}分页列表(无需myBatis分页插件,Page2分页类,页端匹配bootStrap-table)
	  */
    Map<String, Object> selectPageList${objectName}(Page2 page2) throws Exception;
    /**
	  *2 of 7 ${objectNameLower}列表(全部), 用于导出到Excel
 	  */
    List<PageData> listAll${objectName}(PageData pd) throws Exception;
    /**
      *3 of 7 ${objectNameLower}新增
	  */
    void save${objectName}(${objectName}DO pd${objectName}) throws Exception;
    /**
	 *4 of 7  ${objectNameLower}删除
	 */
	void delete${objectName}(${parameterTypePK} ${camelPk}) throws Exception;
	/**
	 *5 of 7  ${objectNameLower}修改
	 */
	void edit${objectName}(${objectName}DO pd${objectName}) throws Exception; 
    /**
	 *6 of 7  通过id获取${objectNameLower}数据
	 */
	${objectName}DO find${objectName}ById(${parameterTypePK} ${camelPk}) throws Exception;
	/**
	 *7 of 7  ${objectNameLower}批量删除
	 */
	void deleteBatch${objectName}(String[] pks) throws Exception;
}


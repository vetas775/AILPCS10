package com.${packageName}.service.impl;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.${packageName}.dao.Dao2;
import com.${packageName}.entity.${objectName}DO;
import com.${packageName}.entity.core.Page2;
import com.${packageName}.entity.core.PageData;
import com.${packageName}.service.${objectName}Service;
import com.${packageName}.util.UuidUtil;
import com.${packageName}.core.Jurisdiction;
import com.${packageName}.core.MyExceptionBusiness;

/** 
 * ${objectName}业务层实现类.  ${TITLE}
 * 创建人：MichaelTsui
 * 创建时间：${nowDate?string("yyyy-MM-dd")}
 * @version 1.0
 */
@Service("${objectNameLower}Service")
public class ${objectName}ServiceImpl extends BaseServiceImpl implements ${objectName}Service {
	@Resource(name = "dao2")
	private Dao2<${objectName}DO> dao${objectName};
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;

	@Override  
	public Map<String, Object> selectPageList${objectName}(Page2 page2) throws Exception {
	    Map<String , Object> result = new HashMap<String , Object>();
	    try {
            List<${objectName}DO> rows = dao${objectName}.listFromDb("${objectName}Mapper.selectPageList${objectName}", page2);
            Integer total = dao${objectName}.countFromDb("${objectName}Mapper.countListTotal${objectName}", page2);
            result.put("rows", rows);
		    result.put("total", total);
        } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
		return result;	   
	}	
 	@Override  
	public List<PageData> listAll${objectName}(PageData pd) throws Exception {
	    List<PageData> res = new ArrayList<PageData>();
	    try {
	        res = daoPD.listFromDb("${objectName}Mapper.listAll", pd);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
		return res;
	}
	@Override 
	public void save${objectName}(${objectName}DO pd${objectName}) throws Exception {
	    pd${objectName}.${pkGetter}(UuidUtil.get32UUID());
	    pd${objectName}.setInsertUser(Jurisdiction.getUsername());	
	    Date dd = new Date();
	    pd${objectName}.setInsertDate(dd); 
	    pd${objectName}.setUpdateDate(new Date()); 
		try {
	        dao${objectName}.saveDb("${objectName}Mapper.insertSelective", pd${objectName});
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}
	@Override 
	public void delete${objectName}(${parameterTypePK} ${camelPk}) throws Exception {
	    try {
	        dao${objectName}.deleteDb2("${objectName}Mapper.deleteByPrimaryKey", ${camelPk});
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}		
	@Override 
	public void edit${objectName}(${objectName}DO pd${objectName}) throws Exception {
	    pd${objectName}.setUpdateUser(Jurisdiction.getUsername());
	    pd${objectName}.setUpdateDate(new Date()); 
		try {
	        dao${objectName}.updateDb("${objectName}Mapper.updateByPrimaryKeySelective", pd${objectName});
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}
	@Override 
	public ${objectName}DO find${objectName}ById(${parameterTypePK} ${camelPk}) throws Exception {
	    ${objectName}DO res = new ${objectName}DO();
	    try {
	        res = dao${objectName}.getFromDb("${objectName}Mapper.selectByPrimaryKey", ${camelPk});
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	    return res;
	}		
	@Override 
	public void deleteBatch${objectName}(String[] pks) throws Exception {
		try {
	        dao${objectName}.deleteDb2("${objectName}Mapper.deleteBatch", pks);
	    } catch (Exception e) {
			throw new MyExceptionBusiness(e);
		}
	}
}


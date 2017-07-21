package com.ailpcs.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ailpcs.core.Const;
import com.ailpcs.core.Jurisdiction;
import com.ailpcs.dao.Dao2;
import com.ailpcs.entity.core.MenuDO;
import com.ailpcs.entity.core.PageData;
import com.ailpcs.service.SysMenuService;
import com.ailpcs.util.LogUtil;
import com.ailpcs.util.RightsHelper;

@Service("sysMenuService")
public class SysMenuServiceImpl extends BaseServiceImpl implements SysMenuService{
	@Resource(name = "dao2")
	private Dao2<PageData> daoPD;
	@Resource(name = "dao2")
	private Dao2<MenuDO> daoMenu;
	
	@Override
	public List<MenuDO> listSubMenuByParentId(String parentId) {
		List<MenuDO> menuList = new ArrayList<MenuDO>();
		try {
		    menuList = (List<MenuDO>) daoMenu.listFromDb("MenuMapper.listSubMenuByParentId", parentId); //取menuId的全部下一级子菜单
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} 	
		return menuList;
	}
	
	@Override
	public PageData getMenuById(String menuId) {
		//return (PageData) dao.findForObject("MenuMapper.getMenuById", pd);  
		PageData pd = new PageData();
		try {
			pd = daoPD.getFromDb("MenuMapper.getMenuById", menuId);		    
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} 	
		return pd;
	}
	
	@Override
	public String saveMenu(MenuDO menu) {
		String sRes = "";
		try {
			PageData pd = daoPD.getFromDb("MenuMapper.findMaxMenuId","");
		    int i = Integer.parseInt(pd.get("MID").toString());
		    menu.setMenuId(String.valueOf(i+1));
			menu.setMenuIcon("menu-icon fa fa-leaf black");//默认菜单图标
		    daoMenu.saveDb("MenuMapper.insertMenu", menu);
		    //清空然后重建保存在内存中的总菜单
		    Const.allMenuList.clear(); 
	        Const.allMenuList = this.listMenu("0", false);
		    LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "新增菜单"+menu.getMenuName());
		} catch (Exception e) {
			logger.error(e.toString(), e);
			sRes = "failed";
		} 
		return sRes;
	}
	
	@Override
	public String deleteMenuById(String menuId) {
		String sErrInfo = "";
		try{
			if(listSubMenuByParentId(menuId).size() > 0){//判断是否有子菜单，是：不允许删除
				sErrInfo = "false";
			}else{
				daoMenu.deleteDb2("MenuMapper.deleteMenuById", menuId); 
				LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "删除菜单ID" + menuId);
				//清空然后重建保存在内存中的总菜单
			    Const.allMenuList.clear(); 
		        Const.allMenuList = this.listMenu("0", false);
				sErrInfo = "success";
			}
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return sErrInfo;		
	}
	
	@Override
	public void editMenu(MenuDO menu) {
		try {
		    daoMenu.updateDb("MenuMapper.updateMenu",menu);
		    LogUtil.saveLog2Db(daoPD, Jurisdiction.getUsername(), "修改菜单"+menu.getMenuName());
		    //清空然后重建保存在内存中的总菜单
		    Const.allMenuList.clear(); 
	        Const.allMenuList = this.listMenu("0", false);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} 
	}	
	
	@Override
	public void editMenuIcon(PageData pd) {
		try {
		    daoPD.updateDb("MenuMapper.editMenuIcon", pd);
		    //清空然后重建保存在内存中的总菜单
		    Const.allMenuList.clear(); 
	        Const.allMenuList = this.listMenu("0", false);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} 
	}
	
	@Override
	public List<MenuDO> getAllMenuList(String roleRights, boolean needFillEditUrl) {
		List<MenuDO> resMenuList = new ArrayList<MenuDO>();
		try {
			if (needFillEditUrl) {  //用于菜单管理画面, 点击左边Ztree的节点后右边跳出这个编辑菜单的链接 
				resMenuList = this.listMenu("0", true);	
			} else {
		        if (null == Const.allMenuList) { 
			        Const.allMenuList = this.listMenu("0", false);	
			        logger.info("从数据库读入菜单=========================================================");
		        }
		        //addAll方法是浅复制, 即对userMenuList里对象的修改会污染到allMenuList-- userMenuList.addAll(MyStartFilter.allMenuList);
		        resMenuList = deepListCopy(Const.allMenuList);
			}
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		} 
   		//设定用户对各菜单项的使用授权
    	if (StringUtils.isNotBlank(roleRights)) {
	    	setMenuRight(resMenuList, roleRights);
	    }	
		return resMenuList;		
	}	
	@Override
	public List<MenuDO> listMenu(String menuId, boolean needFillEditUrl) {
		List<MenuDO> menuList = this.listSubMenuByParentId(menuId);
		for(MenuDO menu : menuList){			
			if (needFillEditUrl) {
				menu.setMenuUrl("menu/toEdit.do?menuId="+menu.getMenuId());
			}
			menu.setSubMenu(this.listMenu(menu.getMenuId(), needFillEditUrl));
			menu.setTarget("treeFrame");
			
		}
		return menuList;
	}	
	@Override
	public List<MenuDO> setMenuRight(List<MenuDO> menuList, String roleRights){
		for(int i = 0; i < menuList.size(); i++){
		    menuList.get(i).setHasMenu(StringUtils.isBlank(roleRights) ? false : RightsHelper.testRights(roleRights, menuList.get(i).getMenuId()));
			if(menuList.get(i).isHasMenu()){		//判断是否有此菜单权限
				this.setMenuRight(menuList.get(i).getSubMenu(), roleRights);//是：继续排查其子菜单
			}
		}
		return menuList;
	}
	
	@Override
	public String menuTreeToJsonString(List<MenuDO> menuList) {
		JSONArray arr = JSONArray.fromObject(menuList);
		String json = arr.toString();
		json = json.replaceAll("menuId", "id").replaceAll("parentId", "pId").replaceAll("menuName", "name")
				.replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked").replaceAll("menuUrl", "url");
		return json;
	}
	@Override	
	public Map<String, String> otherlistMenu(String menuId) {
		Map<String, String> sResMap = new HashMap<String,String>();
		try{			
			String menuUrl = getMenuById(menuId).getString("menuUrl");
			if(StringUtils.isBlank(menuUrl) || "#".equals(menuUrl.trim())){
				menuUrl = "fReeShowTheFirstScreen.do";
			}
			String roleRights = Jurisdiction.getSession().getAttribute(Jurisdiction.getUsername() + Const.SESSION_ROLE_RIGHTS).toString();	//获取本角色菜单权限
			List<MenuDO> athmenuList = listMenu(menuId, false);					//获取某菜单下所有子菜单
			athmenuList = setMenuRight(athmenuList, roleRights);				//根据权限分配菜单
			JSONArray arr = JSONArray.fromObject(athmenuList);
			String json = arr.toString();
			json = json.replaceAll("menuId", "id").replaceAll("parentId", "pId").replaceAll("menuName", "name").replaceAll("subMenu", "nodes").replaceAll("hasMenu", "checked").replaceAll("menuUrl", "url").replaceAll("#", "");
			sResMap.put("zTreeNodes", json);
			sResMap.put("menuUrl",menuUrl);		//本ID菜单链接
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return sResMap;
	}	
	
	
	
	//List深度复制 (List.PutALL属于浅复制, 即当List里的元素是对象时, Src和Dst List表里的对象其实都是同一个, 因此对dst List里对象的改动会污染 src List里的对象)
		/*
		 * 我们知道List自身是一个对象，他在存储类类型的时候，只负责存储地址。而存储基本类型的时候，存储的就是实实在在的值。其实上边的程序也说明了这点，因为我们修改PojoStr-List的时
		 * 候直接的修改了元素本身而不是使用的ArrayList的set(index,object)方法。所以纵然你有千千万万个List，元素还是那么几个。无论是重新构造，Collections的复制方法，
		 * System的复制方法，还是手动去遍历，结果都一样，这些方法都只改变了ArrayList对象的本身，简单的添加了几个指向老元素的地址。而没做深层次的复制。（压根没有没有 new新对象 的操作出现。）
		 * 当然有的时候我们确实需要将这些元素也都复制下来而不是只是用原来的老元素。然而很难在List层实现这个问题。毕竟依照java的语言风格，也很少去直接操作这些埋在堆内存中的数据，所有的
		 * 操作都去针对能找到他们的地址了。地址没了自身还会被GC干掉。所以只好一点点的去遍历去用new创建新的对象并赋予原来的值。不过据说国外的某位大神可能觉得上述的做法略微鬼畜，所以巧用序
		 * 列化对象让这些数据在IO流中360度跑了一圈，居然还真的成功复制了。其实把对象序列化到流中，java语言实在是妥协了，毕竟这把你不能再把地址给我扔进去吧？再说了io流是要和别的系统交互的，
		 * 你发给别人一个地址让别人去哪个堆里找？所以不用多提肯定要新开辟堆内存的。方法如下：（注：前提是 T如果是Pojo类的话，必须实现序列化接口，这是对象进入IO流的基本要求）。
		 */
	private static <T> List<T> deepListCopy(List<T> src) throws IOException, ClassNotFoundException {  
	    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();  
	    ObjectOutputStream out = new ObjectOutputStream(byteOut);  
	    out.writeObject(src);  
	  
	    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());  
	    ObjectInputStream in = new ObjectInputStream(byteIn);  
	    @SuppressWarnings("unchecked")  
	    List<T> dest = (List<T>) in.readObject();  
	    return dest;  
	}  
	
}

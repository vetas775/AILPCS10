<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${objectName}Mapper">
  <resultMap id="BaseResultMap" type="com.${packageName}.entity.${objectName}DO" > 
<#list fieldList as var>  
  <#if var[0] == tablePk>
    <id column="${var[0]}" property="${var[4]}" jdbcType="${var[6]}" />  <!--${var_index+1}. ${var[2]}--> 
  <#else>
	<result column="${var[0]}" property="${var[4]}" jdbcType="${var[6]}" />  <!--${var_index+1}. ${var[2]}-->   
  </#if>   
</#list>    
  </resultMap>
  
  <!-- ${objectName}字段列表 (不含PK)-->
  <sql id="BaseColumnListWithoutPK">
<#list fieldList as var>
  <#if var[0] != tablePk>
    ${var[0]},	 
  </#if>  
</#list>
  </sql> 
  
  <!--1 of 7 ${objectName}分页列表查询 -->
  <sql id="Condition_list">
    <where>
      <if test="pd.keywords != null and pd.keywords != ''"><!-- 关键词检索-->
	    <!--	根据需求自己加检索条件
		字段1 LIKE "%"${r"#{pd.keywords}"}"%"
		or 
		字段2 LIKE "%"${r"#{pd.keywords}"}"%" 
		-->
	  </if>	  
	  <choose>  <!-- 日期查询, 这里日期项应按实际情况调整 -->
	    <when test="pd.dateField != null and pd.dateField == 'insertDate'"> 
	      <if test="pd.dateFm != null and pd.dateFm !=''"> 
			and insert_date <![CDATA[>=]]> ${r"#{"}pd.dateFm${r"}"} 
	      </if>
	      <if test="pd.dateTo != null and pd.dateTo !=''">
		    and insert_date <![CDATA[<=]]> ${r"#{"}pd.dateTo${r"}"}
	      </if>
	    </when> 
	    <when test="pd.dateField != null and pd.dateField == 'updateDate'"> 
	      <if test="pd.dateFm != null and pd.dateFm !=''"> 
			and update_date <![CDATA[>=]]> ${r"#{"}pd.dateFm${r"}"}
	      </if>
	      <if test="pd.dateTo != null and pd.dateTo !=''">
		    and update_date <![CDATA[<=]]> ${r"#{"}pd.dateTo${r"}"}
	      </if>
	    </when> 
	    <otherwise>  
        </otherwise>
	  </choose>	 
	</where>
  </sql>
  <select id="selectPageList${objectName}" parameterType="Page2" resultMap="BaseResultMap">
    select ${tablePk},<include refid="BaseColumnListWithoutPK"></include> from ${objectNameLower}
	<include refid="Condition_list"/>
	<if test="sort != null and sort != '' "> 
	  <choose> 
<#list fieldList as var>
        <when test="sort == '${var[4]}' and order == 'asc'"> 
	      order by ${var[0]} asc 
	    </when> 
	    <when test="sort == '${var[4]}' and order == 'desc'"> 
	      order by ${var[0]} desc 
	    </when>
</#list>    	  
        <otherwise>  
          order by update_date desc  
        </otherwise>
	  </choose>
	</if>	
	LIMIT ${r"#{"}start${r"}"}, ${r"#{"}rows${r"}"}  
  </select>  
  <!-- 获取记录数 -->
  <select id="countListTotal${objectName}" parameterType="Page2" resultType="Integer">
		select count(1) from ${objectNameLower} <include refid="Condition_list"/>
  </select>
  
  <!--2 of 7 ${objectName}列表(全部), 用于导出到Excel -->
  <select id="listAll" parameterType="pd" resultType="pd">
	select ${tablePk},<include refid="BaseColumnListWithoutPK"></include> from ${objectNameLower}
  </select>
  
  <!--${objectName}新增 -->
  <!-- 视情况放开使用
  <insert id="insert" parameterType="com.${packageName}.entity.${objectName}DO">
    insert into ${objectNameLower}(${tablePk}, <include refid="BaseColumnListWithoutPK"></include> ) 
	<trim prefix=" values (" suffix=")" suffixOverrides="," >
<#list fieldList as var>
	  ${r"#{"}${var[4]},jdbcType=${var[6]}${r"}"},	
</#list>
	</trim>
  </insert>
  -->
  
  <!--3 of 7 ${objectName}新增 (值为''或null的字段不处理)-->
  <insert id="insertSelective" parameterType="com.${packageName}.entity.${objectName}DO">
    insert into ${objectNameLower}
    <trim prefix="(" suffix=")" suffixOverrides="," >
<#list fieldList as var>
      <if test="${var[4]} != null<#if var[6] == "VARCHAR"> and ${var[4]} != ''</#if>">
	    ${var[0]},
	  </if>
</#list>    
    </trim>
    <trim prefix="values (" suffix=")" suffixOverrides="," >
<#list fieldList as var>
      <if test="${var[4]} != null<#if var[6] == "VARCHAR"> and ${var[4]} != ''</#if>">
	    ${r"#{"}${var[4]}, jdbcType=${var[6]}${r"}"},
	  </if>
</#list>     
    </trim>
  </insert>   
  
  <!--4 of 7 ${objectName}按ID删除-->
  <delete id="deleteByPrimaryKey" parameterType="java.lang.${parameterTypePK}">
    delete from ${objectNameLower} where ${tablePk} = ${r"#{"}${camelPk},jdbcType=${jdbcTypePk}${r"}"}
  </delete>
  
  <!-- ${objectName}按ID修改 -->
  <!-- 视情况放开使用
  <update id="updateByPrimaryKey" parameterType="com.${packageName}.entity.${objectName}DO">
    update ${objectNameLower} 
    <set> 
<#list fieldList as var>
  <#if var[0] != tablePk>
	  ${var[0]} = ${r"#{"}${var[4]},jdbcType=${var[6]}${r"}"},
  </#if> 	  
</#list>
    </set>
	where ${tablePk} = ${r"#{"}${camelPk},jdbcType=${jdbcTypePk}${r"}"}
  </update>
  -->
  
  <!--5 of 7 ${objectName}按ID修改(值为null的字段不处理) -->
  <update id="updateByPrimaryKeySelective" parameterType="com.${packageName}.entity.${objectName}DO">
    update ${objectNameLower} 
    <set> 
<#list fieldList as var>
  <#if var[0] != tablePk>
      <if test="${var[4]} != null">
	    ${var[0]} = ${r"#{"}${var[4]},jdbcType=${var[6]}${r"}"},
	  </if>
  </#if> 	    
</#list>
<#list fieldList as var>
  <#if var[6] == 'TIMESTAMP' && var[4] != 'insertDate' && var[4] != 'updateDate'>
      <if test = "${var[4]}ClearFlag != null and ${var[4]}ClearFlag == 'Y'">  <!--清除日期, 视状况取舍-->
        ${var[0]} = NULL,
      </if>  
  </#if>
</#list>
    </set>
	where ${tablePk} = ${r"#{"}${camelPk},jdbcType=${jdbcTypePk}${r"}"}
  </update>
  
  <!--6 of 7 ${objectName}通过ID获取数据 -->
  <select id="selectByPrimaryKey" parameterType="java.lang.${parameterTypePK}" resultMap="BaseResultMap">
    select ${tablePk},<include refid="BaseColumnListWithoutPK"></include> from ${objectNameLower} 
	where ${tablePk} = ${r"#{"}${camelPk},jdbcType=${jdbcTypePk}${r"}"}
  </select> 
 
  <!--7 of 7 ${objectName}批量删除 -->
  <delete id="deleteBatch">
	delete from ${objectNameLower} where ${tablePk} in
	<foreach item="item" index="index" collection="array" open="(" separator="," close=")">
      ${r"#{item}"}
	</foreach>
  </delete>	
</mapper>
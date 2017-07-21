package com.ailpcs.entity;

import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/** 
 * Invs6000实体类.  文章表1（公告，热门活动，常见问题等）
 * 创建人：MichaelTsui
 * 创建时间：2017-07-10
 * @version 1.0
 */
public class Invs6000DO implements Serializable { 
    private static final long serialVersionUID = 1L;
	private String docId;  //ID
	private String docType;  //分类
	private String docStatus;  //状态
	private String docFlag;  //标记
	private String docClass;  //等级
	private String docTitle;  //标题
	private String titlePinyin;  //标题拼音
	private String docSource;  //来源
	private String sourcePinyin;  //来源拼音
	private String docAuthor;  //作者
	private String authorPinyin;  //作者拼音
	private String docIncharge;  //采编
	private String inchargePinyin;  //采编拼音
	private String indexKey;  //关键词
	private String keyPinyin;  //关键词拼音
	private String indexKey2;  //关键词2
	private String key2Pinyin;  //关键词2拼音
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date docDate;  //发布日期	
	private String docDateClearFlag;  //虚拟字段，用于update时清除日期栏位的操作, 详见mapper里的updateByPrimaryKeySelective, 可视状况启用或删除 
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date effectiveDateFm;  //有效期从	
	private String effectiveDateFmClearFlag;  //虚拟字段，用于update时清除日期栏位的操作, 详见mapper里的updateByPrimaryKeySelective, 可视状况启用或删除 
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date effectiveDateTo;  //有效期至	
	private String effectiveDateToClearFlag;  //虚拟字段，用于update时清除日期栏位的操作, 详见mapper里的updateByPrimaryKeySelective, 可视状况启用或删除 
	private String docDescription;  //摘要
	private String docContent;  //正文
	private String docUrl;  //文档URL
	private String picUrl;  //图片UrL
	private String picUrl2;  //图片URL2
	private String ortherUrl;  //JsonURL
	private Integer readerLike;  //赞
	private Integer readerDeny;  //踩
	private String insertUser;  //建档人
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date insertDate;  //建档日期	
	private String updateUser;  //修改人
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateDate;  //修改日期	
    
    public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId == null ? null : docId.trim(); 
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType == null ? null : docType.trim(); 
	}
	public String getDocStatus() {
		return docStatus;
	}
	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus == null ? null : docStatus.trim(); 
	}
	public String getDocFlag() {
		return docFlag;
	}
	public void setDocFlag(String docFlag) {
		this.docFlag = docFlag == null ? null : docFlag.trim(); 
	}
	public String getDocClass() {
		return docClass;
	}
	public void setDocClass(String docClass) {
		this.docClass = docClass == null ? null : docClass.trim(); 
	}
	public String getDocTitle() {
		return docTitle;
	}
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle == null ? null : docTitle.trim(); 
	}
	public String getTitlePinyin() {
		return titlePinyin;
	}
	public void setTitlePinyin(String titlePinyin) {
		this.titlePinyin = titlePinyin == null ? null : titlePinyin.trim(); 
	}
	public String getDocSource() {
		return docSource;
	}
	public void setDocSource(String docSource) {
		this.docSource = docSource == null ? null : docSource.trim(); 
	}
	public String getSourcePinyin() {
		return sourcePinyin;
	}
	public void setSourcePinyin(String sourcePinyin) {
		this.sourcePinyin = sourcePinyin == null ? null : sourcePinyin.trim(); 
	}
	public String getDocAuthor() {
		return docAuthor;
	}
	public void setDocAuthor(String docAuthor) {
		this.docAuthor = docAuthor == null ? null : docAuthor.trim(); 
	}
	public String getAuthorPinyin() {
		return authorPinyin;
	}
	public void setAuthorPinyin(String authorPinyin) {
		this.authorPinyin = authorPinyin == null ? null : authorPinyin.trim(); 
	}
	public String getDocIncharge() {
		return docIncharge;
	}
	public void setDocIncharge(String docIncharge) {
		this.docIncharge = docIncharge == null ? null : docIncharge.trim(); 
	}
	public String getInchargePinyin() {
		return inchargePinyin;
	}
	public void setInchargePinyin(String inchargePinyin) {
		this.inchargePinyin = inchargePinyin == null ? null : inchargePinyin.trim(); 
	}
	public String getIndexKey() {
		return indexKey;
	}
	public void setIndexKey(String indexKey) {
		this.indexKey = indexKey == null ? null : indexKey.trim(); 
	}
	public String getKeyPinyin() {
		return keyPinyin;
	}
	public void setKeyPinyin(String keyPinyin) {
		this.keyPinyin = keyPinyin == null ? null : keyPinyin.trim(); 
	}
	public String getIndexKey2() {
		return indexKey2;
	}
	public void setIndexKey2(String indexKey2) {
		this.indexKey2 = indexKey2 == null ? null : indexKey2.trim(); 
	}
	public String getKey2Pinyin() {
		return key2Pinyin;
	}
	public void setKey2Pinyin(String key2Pinyin) {
		this.key2Pinyin = key2Pinyin == null ? null : key2Pinyin.trim(); 
	}
	public Date getDocDate() {
		return docDate;
	}
	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}
	public String getDocDateClearFlag() {
		return docDateClearFlag;
	}
	public void setDocDateClearFlag(String docDateClearFlag) {
		this.docDateClearFlag = docDateClearFlag == null ? null : docDateClearFlag.trim(); 
	}
	public Date getEffectiveDateFm() {
		return effectiveDateFm;
	}
	public void setEffectiveDateFm(Date effectiveDateFm) {
		this.effectiveDateFm = effectiveDateFm;
	}
	public String getEffectiveDateFmClearFlag() {
		return effectiveDateFmClearFlag;
	}
	public void setEffectiveDateFmClearFlag(String effectiveDateFmClearFlag) {
		this.effectiveDateFmClearFlag = effectiveDateFmClearFlag == null ? null : effectiveDateFmClearFlag.trim(); 
	}
	public Date getEffectiveDateTo() {
		return effectiveDateTo;
	}
	public void setEffectiveDateTo(Date effectiveDateTo) {
		this.effectiveDateTo = effectiveDateTo;
	}
	public String getEffectiveDateToClearFlag() {
		return effectiveDateToClearFlag;
	}
	public void setEffectiveDateToClearFlag(String effectiveDateToClearFlag) {
		this.effectiveDateToClearFlag = effectiveDateToClearFlag == null ? null : effectiveDateToClearFlag.trim(); 
	}
	public String getDocDescription() {
		return docDescription;
	}
	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription == null ? null : docDescription.trim(); 
	}
	public String getDocContent() {
		return docContent;
	}
	public void setDocContent(String docContent) {
		this.docContent = docContent == null ? null : docContent.trim(); 
	}
	public String getDocUrl() {
		return docUrl;
	}
	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl == null ? null : docUrl.trim(); 
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl == null ? null : picUrl.trim(); 
	}
	public String getPicUrl2() {
		return picUrl2;
	}
	public void setPicUrl2(String picUrl2) {
		this.picUrl2 = picUrl2 == null ? null : picUrl2.trim(); 
	}
	public String getOrtherUrl() {
		return ortherUrl;
	}
	public void setOrtherUrl(String ortherUrl) {
		this.ortherUrl = ortherUrl == null ? null : ortherUrl.trim(); 
	}
	public Integer getReaderLike() {
		return readerLike;
	}
	public void setReaderLike(Integer readerLike) {
		this.readerLike = readerLike;
	}
	public Integer getReaderDeny() {
		return readerDeny;
	}
	public void setReaderDeny(Integer readerDeny) {
		this.readerDeny = readerDeny;
	}
	public String getInsertUser() {
		return insertUser;
	}
	public void setInsertUser(String insertUser) {
		this.insertUser = insertUser == null ? null : insertUser.trim(); 
	}
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser == null ? null : updateUser.trim(); 
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
    @Override
	public String toString() {
		return "Invs6000DO [docId=" + docId
		  + ", docType=" + docType
		  + ", docStatus=" + docStatus
		  + ", docFlag=" + docFlag
		  + ", docClass=" + docClass
		  + ", docTitle=" + docTitle
		  + ", titlePinyin=" + titlePinyin
		  + ", docSource=" + docSource
		  + ", sourcePinyin=" + sourcePinyin
		  + ", docAuthor=" + docAuthor
		  + ", authorPinyin=" + authorPinyin
		  + ", docIncharge=" + docIncharge
		  + ", inchargePinyin=" + inchargePinyin
		  + ", indexKey=" + indexKey
		  + ", keyPinyin=" + keyPinyin
		  + ", indexKey2=" + indexKey2
		  + ", key2Pinyin=" + key2Pinyin
		  + ", docDate=" + docDate
		  + ", effectiveDateFm=" + effectiveDateFm
		  + ", effectiveDateTo=" + effectiveDateTo
		  + ", docDescription=" + docDescription
		  + ", docContent=" + docContent
		  + ", docUrl=" + docUrl
		  + ", picUrl=" + picUrl
		  + ", picUrl2=" + picUrl2
		  + ", ortherUrl=" + ortherUrl
		  + ", readerLike=" + readerLike
		  + ", readerDeny=" + readerDeny
		  + ", insertUser=" + insertUser
		  + ", insertDate=" + insertDate
		  + ", updateUser=" + updateUser
		  + ", updateDate=" + updateDate
		  + "]";
	}
}

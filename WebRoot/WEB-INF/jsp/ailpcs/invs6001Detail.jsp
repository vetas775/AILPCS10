<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
	<head>
	<base href="<%=basePath%>">
	<!-- 下拉框 -->
	<link rel="stylesheet" href="static/ace/css/chosen.css" />
	<!-- jsp文件头和头部 -->
	<%@ include file="../system/index/top.jsp"%>
	<!-- 日期框 -->
	<link rel="stylesheet" href="static/ace/css/datepicker.css" />
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="main-content-inner">
			<div class="page-content">
				<div class="row">
					<div class="col-xs-12">
					
					<form action="invs6001/${msg}.do" name="Form" id="Form" method="post">
						<input type="hidden" name="docId" id="docId" value="${pd.docId}"/>
						<div id="zhongxin" style="padding-top: 13px;">
						<table id="table_report" class="table table-striped table-bordered table-hover">
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">分类:</td>
								<td><input type="text" name="docType" id="docType" value="${pd.docType}" maxlength="100" placeholder="这里输入分类" title="分类" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">状态:</td>
								<td><input type="text" name="docStatus" id="docStatus" value="${pd.docStatus}" maxlength="20" placeholder="这里输入状态" title="状态" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">标记:</td>
								<td><input type="text" name="docFlag" id="docFlag" value="${pd.docFlag}" maxlength="20" placeholder="这里输入标记" title="标记" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">等级:</td>
								<td><input type="text" name="docClass" id="docClass" value="${pd.docClass}" maxlength="20" placeholder="这里输入等级" title="等级" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">标题:</td>
								<td><input type="text" name="docTitle" id="docTitle" value="${pd.docTitle}" maxlength="100" placeholder="这里输入标题" title="标题" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">标题拼音:</td>
								<td><input type="text" name="titlePinyin" id="titlePinyin" value="${pd.titlePinyin}" maxlength="100" placeholder="这里输入标题拼音" title="标题拼音" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">来源:</td>
								<td><input type="text" name="docSource" id="docSource" value="${pd.docSource}" maxlength="100" placeholder="这里输入来源" title="来源" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">来源拼音:</td>
								<td><input type="text" name="sourcePinyin" id="sourcePinyin" value="${pd.sourcePinyin}" maxlength="100" placeholder="这里输入来源拼音" title="来源拼音" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">作者:</td>
								<td><input type="text" name="docAuthor" id="docAuthor" value="${pd.docAuthor}" maxlength="60" placeholder="这里输入作者" title="作者" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">作者拼音:</td>
								<td><input type="text" name="authorPinyin" id="authorPinyin" value="${pd.authorPinyin}" maxlength="60" placeholder="这里输入作者拼音" title="作者拼音" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">采编:</td>
								<td><input type="text" name="docIncharge" id="docIncharge" value="${pd.docIncharge}" maxlength="60" placeholder="这里输入采编" title="采编" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">采编拼音:</td>
								<td><input type="text" name="inchargePinyin" id="inchargePinyin" value="${pd.inchargePinyin}" maxlength="60" placeholder="这里输入采编拼音" title="采编拼音" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">关键词:</td>
								<td><input type="text" name="indexKey" id="indexKey" value="${pd.indexKey}" maxlength="100" placeholder="这里输入关键词" title="关键词" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">关键词拼音:</td>
								<td><input type="text" name="keyPinyin" id="keyPinyin" value="${pd.keyPinyin}" maxlength="100" placeholder="这里输入关键词拼音" title="关键词拼音" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">关键词2:</td>
								<td><input type="text" name="indexKey2" id="indexKey2" value="${pd.indexKey2}" maxlength="100" placeholder="这里输入关键词2" title="关键词2" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">关键词2拼音:</td>
								<td><input type="text" name="key2Pinyin" id="key2Pinyin" value="${pd.key2Pinyin}" maxlength="100" placeholder="这里输入关键词2拼音" title="关键词2拼音" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">发布日期:</td>
								<td><input class="span10 date-picker" name="docDate" id="docDate" value=<fmt:formatDate value="${pd.docDate}" pattern="yyyy-MM-dd HH:mm:ss"/> type="text" data-date-format="yyyy-mm-dd HH:mm:ss" readonly="readonly" placeholder="发布日期" title="发布日期" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">有效期从:</td>
								<td><input class="span10 date-picker" name="effectiveDateFm" id="effectiveDateFm" value=<fmt:formatDate value="${pd.effectiveDateFm}" pattern="yyyy-MM-dd HH:mm:ss"/> type="text" data-date-format="yyyy-mm-dd HH:mm:ss" readonly="readonly" placeholder="有效期从" title="有效期从" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">有效期至:</td>
								<td><input class="span10 date-picker" name="effectiveDateTo" id="effectiveDateTo" value=<fmt:formatDate value="${pd.effectiveDateTo}" pattern="yyyy-MM-dd HH:mm:ss"/> type="text" data-date-format="yyyy-mm-dd HH:mm:ss" readonly="readonly" placeholder="有效期至" title="有效期至" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">摘要:</td>
								<td><input type="text" name="docDescription" id="docDescription" value="${pd.docDescription}" maxlength="500" placeholder="这里输入摘要" title="摘要" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">正文:</td>
								<td><input type="text" name="docContent" id="docContent" value="${pd.docContent}" maxlength="21845" placeholder="这里输入正文" title="正文" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">文档URL:</td>
								<td><input type="text" name="docUrl" id="docUrl" value="${pd.docUrl}" maxlength="100" placeholder="这里输入文档URL" title="文档URL" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">图片UrL:</td>
								<td><input type="text" name="picUrl" id="picUrl" value="${pd.picUrl}" maxlength="100" placeholder="这里输入图片UrL" title="图片UrL" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">图片URL2:</td>
								<td><input type="text" name="picUrl2" id="picUrl2" value="${pd.picUrl2}" maxlength="100" placeholder="这里输入图片URL2" title="图片URL2" style="width:98%;"/></td>
							</tr>
							<tr>
								<td style="width:75px;text-align: right;padding-top: 13px;">JsonURL:</td>
								<td><input type="text" name="ortherUrl" id="ortherUrl" value="${pd.ortherUrl}" maxlength="2147483647" placeholder="这里输入JsonURL" title="JsonURL" style="width:98%;"/></td>
							</tr>
							
							
							<tr>
								<td style="text-align: center;" colspan="10">
									<a class="btn btn-mini btn-primary" onclick="saveInvs6001();">保存</a>
									<a class="btn btn-mini btn-danger" onclick="top.Dialog.close();">取消</a>
								</td>
							</tr>
						</table>
						</div>
						<div id="zhongxin2" class="center" style="display:none"><br/><br/><br/><br/><br/><img src="static/images/jiazai.gif" /><br/><h4 class="lighter block green">提交中...</h4></div>
					</form>
					</div>
					<!-- /.col -->
				</div>
				<!-- /.row -->
			</div>
			<!-- /.page-content -->
		</div>
	</div>
	<!-- /.main-content -->
</div>
<!-- /.main-container -->


	<!-- 页面底部js¨ -->
	<%@ include file="../system/index/foot.jsp"%>
	<!-- 下拉框 -->
	<script src="static/ace/js/chosen.jquery.js"></script>
	<!-- 日期框 -->
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script>
	<!--提示框-->
	<script type="text/javascript" src="static/js/jquery.tips.js"></script>
		<script type="text/javascript">
		$(top.hangge());
		//保存
		function saveInvs6001(){
			$("#Form").submit();
			$("#zhongxin").hide();
			$("#zhongxin2").show();
		}
		
		$(function() {
			//日期框
			$('.date-picker').datepicker({autoclose: true,todayHighlight: true});
		});
		</script>
</body>
</html>
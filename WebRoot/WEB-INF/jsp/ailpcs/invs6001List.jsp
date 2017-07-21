<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<head>
<base href="<%=basePath%>">
<link rel="stylesheet" href="static/ace/css/datepicker.css" /> <!-- 日期框 -->
<link rel="stylesheet" href="static/html_UI/dist/css/bootstrap.min.css">
<link rel="stylesheet" href="static/bootstrap-table/bootstrap-table.css">
<script src="static/html_UI/dist/js/jquery.min.js"></script>
<script src="static/html_UI/dist/js/bootstrap.min.js"></script>
<script src="static/bootstrap-table/bootstrap-table.js"></script>
<script src="static/bootstrap-table/locale/bootstrap-table-zh-CN.js"></script>
<script type="text/javascript">
		$(top.hangge());//关闭加载状态
		$(function() {
			initTable3("dataTable");
			
			$('.date-picker').datepicker({ //日期框
				autoclose: true,
				todayHighlight: true
			});			
		});		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		function initTable3(tableId) {  
	        $('#' + tableId).bootstrapTable('destroy');  //先销毁表格
	        //初始化表格,动态从服务器加载数据  
	        var sUrl = '<%=basePath%>invs6001/listInvs61.do?keywords=' + $("#keywords").val() 
	        		+ '&dateField=' + $("#dateField").val() + '&dateFm=' + $("#dateFm").val() 
	        		+ '&dateTo=' + $("#dateTo").val() + '&dateRange=' + $("#dateRange").val(); 
	        $("#" + tableId).bootstrapTable({  
	            method: "get",  //使用get请求到服务器获取数据  
	            url: sUrl,
	            //height: $(window).height()-50, //275,
	            classes:'table table-striped table-bordered table-hover',
	            //singleSelect: false,
                //contentType: "application/x-www-form-urlencoded",
	            striped: true,  //表格显示条纹  
	            pagination: true, //启动分页  
	            pageSize: $("#latestPageSize").val(),  //每页显示的记录数  
	            pageNumber:1, //当前第几页  
	            pageList: [10, 20, 30, 40, 50, 60, 70, 80, 90, 99],  //记录数可选列表  
	            search: false,  //是否启用查询  
	            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
	            toolbar: '#toolbar',                //工具按钮用哪个容器
	            showColumns: true,  //显示一个下拉框, 在里面勾选要显示的列  
	            showRefresh: true,  //显示刷新按钮  
	            sidePagination: "server", //表示服务端请求  
	            //设置为undefined可以获取pageNumber，pageSize，searchText，sortName，sortOrder  
	            //设置为limit可以获取limit, offset, search, sort, order  
	            queryParamsType : "undefined",   
	            queryParams: function getParams() {
                    var temp = { 
                    		     rows: this.pageSize, 
                    		     page: this.pageNumber, 
                                 sort: this.sortName,
                                 order: this.sortOrder,
                               };
                   return temp;
                }, 
                responseHandler: function responseHandler(res) {
                	//if (res.IsOk) {
                	//    var result = b64.decode(res.ResultValue);
                	//    var resultStr = $.parseJSON(result);
                        $("#latestPageSize").val(res.pageSize); 
                 	    return {"rows": res.rows, "total": res.total };
                	//} else {
                	//	return {"rows": [], "total": 0};
                	//}
                },
                idField: 'docId',
                uniqueId: 'docId',
                columns: [
                          {checkbox:true},
                          {align:'center',field: 'docId', title: '标识列', visible:true}, 
                          {align:'center',field: 'docStatus', title: '用户标识', sortable: true},    
                          {align:'center',title: '操作', width: 100,   
                        	  events: operateEvents,
                              formatter: operateFormatter}
                ], 
                icons: {
                    paginationSwitchDown: 'glyphicon-collapse-down icon-chevron-down',
                    paginationSwitchUp: 'glyphicon-collapse-up icon-chevron-up',
                    refresh: 'glyphicon-refresh icon-refresh',
                    toggle: 'glyphicon-list-alt icon-list-alt',
                    columns: 'glyphicon-th icon-th',
                    detailOpen: 'glyphicon-plus icon-plus',
                    detailClose: 'glyphicon-minus icon-minus'
                },
	            onLoadSuccess: function(){  //加载成功时执行  
	            	$(top.hangge());//关闭加载状态
	                //layer.msg("加载成功");  
	            },  
	            onLoadError: function(){  //加载失败时执行  
	            	$(top.hangge());//关闭加载状态
	                //layer.msg("加载数据失败", {time : 1500, icon : 2});  
	            }  
	        });  
	    } 		
		function operateFormatter(value, row, index) {  //   
		    return [
		        '<a class="editSingle" href="javascript:void(0)" title="编辑">',
		        '<i class="glyphicon glyphicon-edit btn btn-success btn-xs"></i>', 
		        '</a>  ',
		        '<a style="padding-left:10px;" class="delSingle" href="javascript:void(0)" title="删除">',
		        '<i class="glyphicon glyphicon-trash btn btn-danger btn-xs"></i>',
		        '</a>'
		    ].join('');
		}
		window.operateEvents = {
			'click .editSingle': function (e, value, row, index) {
			    //alert('You click editSingle action, row: ' + JSON.stringify(row) + ' index:' + index + ' PK:' + row.docId);
			    edit(row.docId);
			},
			'click .delSingle': function (e, value, row, index) {
			    //alert('You click delSingle action, row: ' + JSON.stringify(row) + ' index:' + index + ' PK:' + row.docId);
			    del(row.docId);
			}
		};		
		//查询按钮
		function tosearch(){
			top.jzts();
			initTable3("dataTable");
		}
		//新增按钮
		function add(){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="新增";
			 diag.URL = '<%=basePath%>invs6001/goAddInvs6001.do';
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 if('${page.currentPage}' == '0'){
						 tosearch();
					 }else{
						 tosearch();
					 }
				}
				diag.close();
			 };
			 diag.show();
		}
		//删除
		function del(Id){
			bootbox.confirm("确定要删除吗?", function(result) {
				if(result) {
					top.jzts();
					var url = "<%=basePath%>invs6001/deleteInvs6001.do?docId="+Id+"&tm="+new Date().getTime();
					$.get(url,function(data){
						tosearch();
					});
				}
			});
		}
		//修改
		function edit(Id){
			 top.jzts();
			 var diag = new top.Dialog();
			 diag.Drag=true;
			 diag.Title ="编辑";
			 diag.URL = '<%=basePath%>invs6001/goEditInvs6001.do?docId='+Id;
			 diag.Width = 450;
			 diag.Height = 355;
			 diag.Modal = true;				//有无遮罩窗口
			 diag. ShowMaxButton = true;	//最大化按钮
		     diag.ShowMinButton = true;		//最小化按钮 
			 diag.CancelEvent = function(){ //关闭事件
				 if(diag.innerFrame.contentWindow.document.getElementById('zhongxin').style.display == 'none'){
					 tosearch();
				}
				diag.close();
			 };
			 diag.show();
		}		
		//批量删除
		function makeAll(msg){
			bootbox.confirm(msg, function(result) {
				if(result) {
					var rows = $('#dataTable').bootstrapTable('getAllSelections');
					if (rows.length <= 0) {
						bootbox.dialog({
							message: "<span class='bigger-110'>您没有选择任何内容!</span>",
							buttons: 			
							{ "button":{ "label":"确定", "className":"btn-sm btn-success"}}
						});
						$("#zcheckbox").tips({
							side:1,
				            msg:'点这里全选',
				            bg:'#AE81FF',
				            time:8
				        });
						return;						
					} else if(msg == '确定要删除选中的数据吗?'){
						var str = '';
						for (var i = 0; i < rows.length; i++) {
							if(str == '') str += rows[i].docId;
							else str += ',' + rows[i].docId;
						}	
						top.jzts();
						$.ajax({
							type: "POST",
							url: '<%=basePath%>invs6001/deleteBatchInvs6001.do?tm='+new Date().getTime(),
						    data: {pksInvs6001: str},
							dataType:'json',
							//beforeSend: validateData,
							cache: false,
							success: function(data){
								 $.each(data.list, function(i, list){
										tosearch();
								 });
							}
						});
					}					
				}
			});
		};
		//导出excel
		function toExcel(){
			window.location.href='<%=basePath%>invs6001/excelInvs6001.do';
		}
		//清空检索项
		function clearCondition() {
			$("#keywords").val("");
			$("#dateFm").val("");
			$("#dateTo").val("");
		}		
	</script>
</head>
<body class="no-skin">
    <table id="dataTable" class="success"></table>   
    <div id="toolbar" class="main-container">        
        <form name="Form" id="Form">  
            <table style="margin-top:5px;">
		        <tr>
		            <td>
		                <div class="input-group">
			                <!-- span class="glyphicon glyphicon-search input-group-addon"></span-->
			                <input type="text" placeholder="这里输入关键词" class="form-control" id="keywords" autocomplete="off" name="keywords" value="${pd.keywords}" placeholder="这里输入关键词" style="vertical-align:top;width: 200px;"/>
		                </div>
		            </td>			    
				    <td style="padding-left:10px;">
				        <div class="input-group">
			                <span class="input-group-addon">按</span>
			                <select class="chosen-select form-control" name="dateField" id="dateField" data-placeholder="日期类型" placeholder="日期类型" style="vertical-align:top;width: 120px;">
				    	        <option value=""></option>
					            <c:forEach items="${listDate}" var="dateOne">
						        <option value="${dateOne.key}" <c:if test="${pd.dateField == dateOne.key}">selected</c:if>>${dateOne.value}</option>
						        </c:forEach>
					        </select>
		                </div>
				    </td>
				    <td style="padding-left:1px;">
				        <div class="input-group">
			                <span class="input-group-addon">从</span>
			                <input class="form-control date-picker" name="dateFm" id="dateFm"  value="${pd.dateFm}" type="text" data-date-format="yyyy-mm-dd" style="width:100px;" placeholder="开始日期" title="开始日期"/>
		                </div>
			        </td>
				    <td style="padding-left:1px;">
				        <div class="input-group">
			                <span class="input-group-addon">到</span>
			                <input class="form-control date-picker" name="dateTo" id="dateTo"  value="${pd.dateTo}" type="text" data-date-format="yyyy-mm-dd" style="width:100px;" placeholder="结束日期" title="结束日期"/>
		                </div>				    
			        </td>
				    <c:if test="${QX.cha == 1}">
				    <td style="vertical-align:top;padding-left:10px"><a class="btn btn-primary" onclick="tosearch();"  title="检索"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></td>
					<td style="vertical-align:top;padding-left:10px"><a class="btn btn-info" onclick="clearCondition();"  title="清空检索项"><span class="glyphicon glyphicon-new-window" aria-hidden="true"></span></a></td>
				    </c:if>
				    <c:if test="${QX.add == 1 }">
				    <td style="vertical-align:top;padding-left:10px"><a class="btn btn-success" onclick="add();" title="新增" ><span class="glyphicon glyphicon-plus green" aria-hidden="true"></span></a></td>
				    </c:if>
				    <c:if test="${QX.del == 1 }">
				    <td style="vertical-align:top;padding-left:10px"><a class="btn btn-danger" onclick="makeAll('确定要删除选中的数据吗?');" title="批量删除" ><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a></td>
				    </c:if>
				    <c:if test="${QX.toExcel == 1}">
				    <td style="vertical-align:top;padding-left:10px;"><a class="btn btn-warning" onclick="toExcel();" title="导出到Excel" ><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span></a></td>
				    </c:if>
			    </tr>
		    </table>
		</form>	
    </div> 
    <input type="hidden"  id="latestPageSize" name="latestPageSize" value="10" >   	
	<script src="static/ace/js/bootbox.js"></script> <!-- 删除时确认窗口 -->	
	<script src="static/ace/js/ace/ace.js"></script> <!-- ace scripts -->		
	<script src="static/ace/js/date-time/bootstrap-datepicker.js"></script> <!--日期框 -->	
	<script type="text/javascript" src="static/js/jquery.tips.js"></script> <!--提示框-->
</body>
</html>
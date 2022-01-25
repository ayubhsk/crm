<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" +
            request.getServerName() + ":" + request.getServerPort() +
            request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>"/>
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

    <script type="text/javascript">


        $(function () {

            //bootstrap时间控件
            $(".time").datetimepicker({
                minView: "month",
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true,
                todayBtn: true,
                pickerPosition: "bottom-left"
            });

            $("#addBtn").click(function () {
                //获取用户名
                $.ajax({

                    url: "workbench/activity/getUserList.do",
                    data: {},
                    type: "get",
                    dataType: "json",
                    success: function (data) {
                        var html
                        //初始化下拉框
                        $.each(data, function (i, n) {
                            html += "<option value='" + n.id + "'> " + n.name + "</option>"
                        })
                        $("#create-marketActivityOwner").html(html)

                        //设置下拉框初始选项为登录的用户
                        var id = "${user.id}"//el表达式要双引号
                        $("#create-marketActivityOwner").val(id)
                        /*
                        操作模态窗口的方式：
                            需要操作的模态窗口的jquery对象，调用modal方法，为该方法传递参数 show:打开模态窗口   hide：关闭模态窗口
                     */
                        $("#createActivityModal").modal("show")
                    }

                })

            })

            pageList(1, 2)


            //全选操作
            $("#qx").click(function () {
                $("input[name=xz]").prop("checked", this.checked)
            })
            //全选相关，动态生成的对象绑定函数

            //以下这种做法是不行的
            /*$("input[name=xz]").click(function () {

                alert(123);
            })*/
            //因为动态生成的元素，是不能够以普通绑定事件的形式来进行操作的
            /*
                动态生成的元素，我们要以on方法的形式来触发事件
                语法：
                    $(需要绑定元素的有效的外层元素).on(绑定事件的方式,需要绑定的元素的jquery对象,回调函数)

             */
            $("#activityBody").on("click", $("input[name=xz]"), function () {
                $("#qx").prop("checked", $("input[name=xz]").length == $("input[name=xz]:checked").length)
            })
        });

        /*
            pageNo:页码
            pageSize:每页展现的记录数

            pageList方法：就是发出ajax请求到后台，从后台取得最新的市场活动信息列表数据
                            通过响应回来的数据，局部刷新市场活动信息列表
            我们都在哪些情况下，需要调用pageList方法（什么情况下需要刷新一下市场活动列表）
            （1）点击左侧菜单中的"市场活动"超链接，需要刷新市场活动列表，调用pageList方法
            （2）添加，修改，删除后，需要刷新市场活动列表，调用pageList方法
            （3）点击查询按钮的时候，需要刷新市场活动列表，调用pageList方法
            （4）点击分页组件的时候，调用pageList方法
            以上为pageList方法制定了六个入口，也就是说，在以上6个操作执行完毕后，我们必须要调用pageList方法，刷新市场活动信息列表
         */
        function pageList(pageNo, pageSize) {
            //每次刷新前将全选的复选框干掉
            $("#qx").prop("checked", false)

            //查询前，将隐藏区域的信息重新输入到搜索框中
            $("#search-name").val($.trim($("#hidden-name").val()))
            $("#search-owner").val($.trim($("#hidden-owner").val()))
            $("#search-startDate").val($.trim($("#hidden-startDate").val()))
            $("#search-endDate").val($.trim($("#hidden-endDate").val()))

            $.ajax({
                url: "workbench/activity/pageList.do",
                data: {
                    "pageNo": pageNo,
                    "pageSize": pageSize,
                    "name": $.trim($("#search-name").val()),
                    "owner": $.trim($("#search-owner").val()),
                    "startDate": $.trim($("#search-startDate").val()),
                    "endDate": $.trim($("#search-endDate").val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {
                    var html = ""
                    $.each(data.dataList, function (i, n) {
                        html += '<tr class="active">';
                        html += '<td><input type="checkbox" name="xz" value="' + n.id + '"/></td>'
                        html += '<td><a style="text-decoration: none; cursor: pointer;"';
                        html += 'onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">' + n.name + '</a></td>';
                        html += '<td>' + n.owner + '</td>';
                        html += '<td>' + n.startDate + '</td>';
                        html += '<td>' + n.endDate + '</td>';
                        html += '</tr>'
                    })
                    $("#activityBody").html(html)


                    //数据处理完毕后，分页插件展现数据
                    var totalPages = Math.floor((data.total + pageSize - 1) / pageSize)//整除操作
                    $("#activityPage").bs_pagination({
                        currentPage: pageNo, // 页码
                        rowsPerPage: pageSize, // 每页显示的记录条数
                        maxRowsPerPage: 20, // 每页最多显示的记录条数
                        totalPages: totalPages, // 总页数
                        totalRows: data.total, // 总记录条数
                        visiblePageLinks: 3, // 显示几个卡片
                        showGoToPage: true,
                        showRowsPerPage: true,
                        showRowsInfo: true,
                        showRowsDefaultInfo: true,
                        //该回调函数时在，点击分页组件的时候触发的
                        onChangePage: function (event, data) {
                            pageList(data.currentPage, data.rowsPerPage);
                        }
                    });
                }

            })
        }

        //查询按钮
        function searchBtnClick() {
            //点击搜索按钮后，应该把要搜索的信息存到隐藏区域中
            $("#hidden-name").val($.trim($("#search-name").val()))
            $("#hidden-owner").val($.trim($("#search-owner").val()))
            $("#hidden-startDate").val($.trim($("#search-startDate").val()))
            $("#hidden-endDate").val($.trim($("#search-endDate").val()))
            pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

        }

        //创建市场活动的保存操作
        function saveBtnClick() {
            $.ajax({

                url: "workbench/activity/save.do",
                data: {
                    "owner": $.trim($("#create-marketActivityOwner").val()),
                    "name": $.trim($("#create-name").val()),
                    "startDate": $.trim($("#create-startDate").val()),
                    "endDate": $.trim($("#create-endDate").val()),
                    "cost": $.trim($("#create-cost").val()),
                    "description": $.trim($("#create-description").val())
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data) {
                        //添加成功后
                        //刷新市场活动信息列表（局部刷新）
                        //pageList(1,2);
                        /*
                        *
                          pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
				                    ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                        * $("#activityPage").bs_pagination('getOption', 'currentPage'):
                        * 		操作后停留在当前页
                        *
                        * $("#activityPage").bs_pagination('getOption', 'rowsPerPage')
                        * 		操作后维持已经设置好的每页展现的记录数
                        *
                        * 这两个参数不需要我们进行任何的修改操作
                        * 	直接使用即可
                        * */
                        //做完添加操作后，应该回到第一页，维持每页展现的记录数
                        pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


                        //关闭模态
                        $("#createActivityModal").modal("hide")

                        //将模块中的信息消除
                        $("#activityAddForm")[0].reset()
                    } else {
                        alert("添加市场活动失败")
                    }
                }
            })
        }

        function deleteBtnClick() {
            var $xz = $("input[name=xz]:checked")
            if ($xz.length == 0) {
                alert("请选择要删除的内容")
            } else {
                if (confirm("确定要删除吗？")) {
                    //拼接参数
                    var param = ""
                    for (let i = 0; i < $xz.length; i++) {
                        param += "id=" + $($xz[i]).val()
                        //如果不是最后一个元素还有追加&符号
                        if (i < $xz.length) param += "&"
                    }
                    $.ajax({

                        url: "workbench/activity/delete.do",
                        data: param,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.success) {
                                //删除成功后，刷新页面
                                pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
                            } else {
                                alert("删除市场活动失败")
                            }
                        }

                    })
                }
            }
        }

        function editBtnClick() {
            var $xz=$("input[name=xz]:checked")
            if($xz.length!=1){
                alert("请选择一条记录")
            }else {
                var id=$xz.val()

                $.ajax({
                    url : "workbench/activity/getUserListAndActivity.do",
                    data : {
                        "id":id
                    },
                    type : "get",
                    dataType : "json",
                    success : function (data) {
                        //先刷新列表

                        //data为用户列表和市场活动对象
                        var html=""
                        $.each(data.userList,function (i,n) {
                            html+="<option value='"+n.id+"'>"+n.name+"</option>"
                        })
                        $("#edit-owner").html(html)

                        $("#edit-id").val(data.a.id)
                        $("#edit-name").val(data.a.name)
                        $("#edit-owner").val(data.a.owner)
                        $("#edit-startDate").val(data.a.startDate)
                        $("#edit-endDate").val(data.a.endDate)
                        $("#edit-cost").val(data.a.cost)
                        $("#edit-description").val(data.a.description)
                        //所有修改完成后，打开模态窗口
                        $("#editActivityModal").modal("show")
                    }
                })
            }
        }
        
        function updateBtnClick() {
            $.ajax({
                url: "workbench/activity/update.do",
                data: {
                    "id":$.trim($("#edit-id").val()),
                    "owner": $.trim($("#edit-owner").val()),
                    "name": $.trim($("#edit-name").val()),
                    "startDate": $.trim($("#edit-startDate").val()),
                    "endDate": $.trim($("#edit-endDate").val()),
                    "cost": $.trim($("#edit-cost").val()),
                    "description": $.trim($("#edit-description").val())
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data) {
                        //关闭模态
                        pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
                            ,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

                        $("#editActivityModal").modal("hide")


                    } else {
                        alert("修改市场活动失败")
                    }
                }
            })
        }


    </script>
</head>
<body>
<%--隐藏区域--%>
<input type="hidden" id="hidden-name">
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-startDate">
<input type="hidden" id="hidden-endDate">
<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id">
                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-owner">
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-name" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-startDate" value="2020-10-10">
                        </div>
                        <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="edit-endDate" value="2020-10-20">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description">

                            </textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateBtn" onclick="updateBtnClick()">更新</button>
            </div>
        </div>
    </div>
</div>
<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="activityAddForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-name">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-startDate" readonly>
                        </div>
                        <label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control time" id="create-endDate">
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="savaBtn" onclick="saveBtnClick()">保存</button>
            </div>
        </div>
    </div>
</div>

<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表11</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="search-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="search-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control time" type="text" id="search-startDate"/>
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control time" type="text" id="search-endDate">
                    </div>
                </div>

                <button type="button" id="searchBtn" onclick="searchBtnClick()" class="btn btn-default">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">

                <!--

				点击创建按钮，观察两个属性和属性值

				data-toggle="modal"：
					表示触发该按钮，将要打开一个模态窗口

				data-target="#createActivityModal"：
					表示要打开哪个模态窗口，通过#id的形式找到该窗口

				现在我们是以属性和属性值的方式写在了button元素中，用来打开模态窗口
				但是这样做是有问题的：
					问题在于没有办法对按钮的功能进行扩充

				所以未来的实际项目开发，对于触发模态窗口的操作，一定不要写死在元素当中，
				应该由我们自己写js代码来操作

				-->
                <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span>
                    创建
                </button>
                <button type="button" class="btn btn-default" id="editBtn" onclick="editBtnClick()"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn" onclick="deleteBtnClick()"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>

        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="qx"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activityBody">
                <%--                <tr class="active">
                                    <td><input type="checkbox"/></td>
                                    <td><a style="text-decoration: none; cursor: pointer;"
                                           onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                                    <td>zhangsan</td>
                                    <td>2020-10-10</td>
                                    <td>2020-10-20</td>
                                </tr>
                                <tr class="active">
                                    <td><input type="checkbox"/></td>
                                    <td><a style="text-decoration: none; cursor: pointer;"
                                           onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                                    <td>zhangsan</td>
                                    <td>2020-10-10</td>
                                    <td>2020-10-20</td>
                                </tr>--%>
                </tbody>
            </table>
        </div>

        <div style="height: 50px; position: relative;top: 30px;">
            <div id="activityPage"><%--这个div调用分页插件--%>

            </div>
        </div>

    </div>

</div>
</body>
</html>
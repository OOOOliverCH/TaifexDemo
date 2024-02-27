<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*" />
    <title>taifex線上作業</title>

    <link rel="stylesheet" href="../assets/css/ace.min.css" />
    <link rel="stylesheet" href="../assets/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="../plugins/bootstrap-datepicker/css/datepicker.css"/>
    <link rel="stylesheet" href="../plugins/bootstrap-toastr/toastr.min.css"/>
</head>

<body>
    <div class="main-container" id="main-container">
        <div class="main-container-inner">
            <a class="menu-toggler" href="#"><span class="menu-text"></span></a>
            <div class="main-content" style="margin-left: -2%;">
                <div class="page-content">
                    <div class="col-xs-6 col-xs-offset-6" style="padding-bottom:30px">
                        <div class="col-sm-12" style="width: 200%;">
                            <div class="widget-box">
                                <div class="widget-header header-color-blue">
                                    <h3>題目一</h3>
                                </div>

                                <div class="widget-body">
                                    <div class="widget-main">
                                        <div>
                                            <span>題目：每日18:00呼叫API，取得外匯成交資料，將USD/NTD與DATE字段存入表中</span>
                                        </div>
                                        <div style="margin-top: 2%;">
                                            <span id="time"></span>
                                        </div>
                                        <div id="third_div" style="margin-top: 1%; display: none;">
                                            <span style="color: blue;">由於沒有近期的數據，點擊按鈕觸發的日期為2024年01月02日</span>
                                            <a style="margin-left: 1%;" href="javascript:getData('20240102')" class="btn btn-success">
                                                <i class="icon-search"></i>點擊獲取數據</a>
                                        </div>
                                        <span id="callback_data" style="display: none;"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-12" style="width: 200%;">
                            <div class="widget-box">
                                <div class="widget-header header-color-blue">
                                    <h3>題目二</h3>
                                </div>

                                <div class="widget-body">
                                    <div class="widget-main">
                                        <form class="form-horizontal" role="form">
                                            <div>
                                                <span>題目：根據日期與幣種字段去查結果</span>
                                            </div>
                                            <div style="margin-top: 2%;" class="form-group">
                                                <label class="control-label col-md-2" style="width: 100px; margin-left: -0.8%;">查詢日期：</label>
                                                <div class="col-md-2">
                                                    <div class="input-group input-large date-picker input-daterange" data-date-format="yyyy-mm-dd" >
                                                        <input id="search_start_date" type="text" class="form-control" name="from" readonly  style="width: 150px;cursor: pointer">
                                                        <span class="input-group-addon">至</span>
                                                        <input id="search_end_date" type="text" class="form-control" name="to" readonly style="width: 150px;cursor: pointer">
                                                    </div>
                                                </div>

                                                <label class="control-label col-md-2" style="width: 70px; margin-left: 10%;">幣種：</label>
                                                <div class="col-md-2">
                                                    <select id="search_currency">
                                                        <option value="-1">請選擇</option>
                                                        <option value="usd">美元</option>
                                                        <option value="ntd">台幣</option>
                                                    </select>
                                                </div>

                                                <div class="col-md-2">
                                                    <a id="search_btn" href="javascript:search()" class="btn btn-success" style="margin-left: -50px">
                                                        <i class="icon-search"></i>查询</a>

                                                    <a id="add-review-btn" href="javascript:reset()" class="btn btn-warning" style="margin-left: 10px">
                                                        <i class="icon-edit"></i>重置</a>
                                                </div>
                                            </div>
                                            <div id="search_result_div" style="display: none">
                                                <div class="widget-body">
                                                    <div class="widget-main">
                                                        <table class="table table-bordered table-hover" >
                                                            <thead>
                                                            <tr style="color:black">
                                                                <th width="25">序号</th>
                                                                <th width="60">日期</th>
                                                                <th width="60">匯率</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody id="result_data">
                                                            <tr>

                                                            </tr>
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="../assets/js/jquery-2.0.3.min.js"></script>
    <script src="../assets/js/bootstrap.min.js"></script>
    <script src="../plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
    <script src="../plugins/bootstrap-datepicker/js/locales/bootstrap-datepicker.zh-CN.js"></script>
    <script src="../plugins/bootstrap-toastr/toastr.min.js"></script>
    <script src="../js/server.js"></script>
    <script src="../js/demo_list.js"></script>
</body>
</html>
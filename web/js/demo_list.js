//消息反饋
var fastShowTip = function(type, title, msg) {
    toastr.options = {
        "closeButton": false,
        "positionClass": "toast-top-center",
        "showDuration": "200",
        "hideDuration": "200",
        "timeOut": "1000",
        "extendedTimeOut": "1000",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };

    toastr[type](title, msg);
};

//加載頁面時執行
window.onload = function () {
    var t = null;
    t = setTimeout(time, 1000);
    function time() {
        clearTimeout(t);
        var date = new Date();
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hh = date.getHours();
        var mm = date.getMinutes();
        var ss = date.getSeconds();
        document.getElementById("time").innerHTML = "當前時間：" + y + "年" + m + "月" + d + "日 " + hh + "時" + mm + "分" + ss + "秒";
        $('#third_div').css('display', '');
        var compareTime = hh+":"+mm;
        //每天到18:00時自動獲取當天的匯率並存入資料庫中
        if(compareTime == "18:00"){
           console.log("獲取數據，存入美元/台幣與日期字段到表中");
           var today = y+m+d;
           getData(today);
        }
        t = setTimeout(time, 1000);
    }

    //設置題目二的日期查詢框默認內容(一年前到前一天)
    $('#search_start_date').datepicker({
        format: 'yyyy-mm-dd',
        language: "zh-CN",
    });
    $('#search_end_date').datepicker({
        format: 'yyyy-mm-dd',
        language: "zh-CN",
    });
    $('#search_start_date').val(formatDate("yyyy-MM-dd"));
    $('#search_end_date').val(getLastToday());
};

//題目一的執行獲取當天匯率的方法
function getData(today) {
    var param = {
        'today': today
    };
    Server.getToDemo("/get_today_data", param, function (result) {
        console.log(result);
        var info = result[0];
        if(info.code == "E001"){
            fastShowTip('error', '獲取失敗', info.message);
        }else if(info.code == '0000'){
            fastShowTip('success', '獲取成功', info.message);
            $('#callback_data').css('display', '');
            var udl = result[1];
            var date = udl.date.substring(0,4)+"-"+udl.date.substring(4,6)+"-"+udl.date.substring(6);
            document.getElementById("callback_data").style.color = 'red';
            document.getElementById("callback_data").innerHTML = "日期：" + date + "，USD/NTD匯率比為：" + udl.usd_ntd;
        }
    });
}

//題目二的按查詢條件查詢數據
function search(){
    //日期字段移除-符號，匹配資料庫裡日期的字段
    var startDate = $('#search_start_date').val().replace(/\-/g, '');
    var endDate = $('#search_end_date').val().replace(/\-/g, '');
    var currency = $('#search_currency').val();
    if(currency == '-1'){
        fastShowTip('error', '提示', '請選擇幣種');
        return;
    }
    var param = {
        'startDate': startDate,
        'endDate': endDate,
        'currency': $('#search_currency').val(),
    };
    Server.postToDemo("/search_deal_data", param, function (result) {
        console.log(result);
        var info = result[0];
        if(info.code == "E001") {
            fastShowTip('error', '獲取失敗', info.message);
        }else if(info.code == '0000'){
            var data = result[1];
            if(data.length == 0){
                fastShowTip('success', '獲取成功', info.message);
                return;
            }
            fastShowTip('success', '獲取成功', info.message);
            $('#result_data').empty();
            var tr = "";
            for(var i in data){
                var id = data[i].id;
                var number = Number(i)+1;
                tr += "<tr id='"+id+"'><td>"+number+"</td><td>"+data[i].date+"</td><td>"+data[i].usd_ntd+"</td></tr>";
            }
            $('#result_data').append(tr);
            $('#search_result_div').css('display', '');
        }
    })
}

//題目二重置查詢欄以及查詢結果
function reset() {
    $('#search_start_date').val(formatDate("yyyy-MM-dd"));
    $('#search_end_date').val(getLastToday());
    $('#search_currency').val("-1");
    $('#search_result_div').css('display', 'none');
    $('#result_data').empty();
    fastShowTip('success', '成功', "信息已重置");
}

//取得前一天日期
function getLastToday() {
    var date = new Date(); //创建对象
    var y = date.getFullYear(); //获取年份
    var m = date.getMonth() + 1; //获取月份  返回0-11
    var d = date.getDate() - 1; // 获取日
    if (m < 10) {
        m = "0" + m;
    }
    if (d < 10) {
        d = "0" + d;
    }
    return y + "-" + m + "-" + d;
}

//格式化日期
function formatDate(format){
    var date = new Date();
    date.setDate(1);
    var paddNum = function(num){
        num += "";
        return num.replace(/^(\d)$/,"0$1");
    };
    //指定格式字符
    var cfg = {
        yyyy : date.getFullYear() //年 : 4位
        ,yy : date.getFullYear().toString().substring(2)//年 : 2位
        ,M  : date.getMonth() + 1  //月 : 如果1位的时候不补0
        ,MM : paddNum(date.getMonth() + 1) //月 : 如果1位的时候补0
        ,d  : date.getDate()   //日 : 如果1位的时候不补0
        ,dd : paddNum(date.getDate())//日 : 如果1位的时候补0
        ,hh : date.getHours()  //时
        ,mm : date.getMinutes() //分
        ,ss : date.getSeconds() //秒
    };
    format || (format = "yyyy-MM-dd hh:mm:ss");
    return format.replace(/([a-z])(\1)*/ig,function(m){return cfg[m];});
}

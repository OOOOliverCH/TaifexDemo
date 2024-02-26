//路徑連接、ajax請求
var URL = "http://192.168.192.1:5656";
var DEMO_URL = "/demo";

var Server = {

    getToDemo: function (url, param, callback) {
        $.ajax({
            url: URL + DEMO_URL + url,
            data: param,
            type: 'GET',
            success: callback,
            error: function (result,textStatus) {
                console.log(textStatus + ":" + result);
            }
        });
    },

    postToDemo: function (url, param, callback) {
        $.ajax({
            url: URL + DEMO_URL + url,
            data: param,
            type: 'POST',
            success: callback,
            error: function (result,textStatus) {
                console.log(textStatus + ":" + result);
            }
        });
    }
};
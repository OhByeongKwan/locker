var Config = {
    url: "http://localhost:8087",
    // url: "http://15.164.134.14:8080",
    platform: null,

    getUserImgUrl: function(mid, file) {
        if (isUrl(file)) return file;

        let url = "users/" + mid + "/images/" + file;
        if (this.platform !== "pc") {
            url = "../" + url
        }
        return url;
    },
};

let AJAX = {
    call: function (url, params, func, isfd) {
        this._call(url, params, func, isfd, "text");
    },

    jsonCall: function (url, params, func, isfd) {
        this._call(url, params, func, isfd, "json");
    },

    _call: function (url, params, func, isfd, dataType) {
        let callobj = {
            url: Config.url+"/"+url,
            type: "post",
            data: params,
            dataType: dataType,
            success: func,
            error: function (xhr, status, error) {
                if (xhr.status === 0) {
                    alert("네트워크 접속이 원할하지 않습니다.");
                }
                else {
                    console.log(xhr.responseText);
                    alert("에러가 발생하였습니다. 관리자에게 문의해주세요.");
                }
            },
        };
        if (isfd) {
            callobj.processData = false;

            callobj.contentType = false;
        }
        jQuery.ajax(callobj);
    },
};


//-----------------------------------------------------------------------------------------
//Dialog object

var Dialog = {
    // init: function () {
    //     $("body").append(this._generate());
    //
    //     $(window).resize(function() {
    //         Dialog.resize();
    //     });
    // },

    okCallback: null,
    cancelCallback: null,
    data: {},
    modal: null,

    init: function() {
        $("body .container").append(`<div id="dialog" class="modal show" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"></h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                    </div>
                    <div class="modal-footer">
                        <button id="dialogCancel" type="button" class="btn btn-sm btn-secondary" data-bs-dismiss="modal">취소</button>
                        <button id="dialogOk" type="button" class="btn btn-sm btn-primary">확인</button>
                    </div>
                </div>
            </div>
        </div>`);

        Dialog.modal = new bootstrap.Modal(document.getElementById('dialog'),{});

        $('#dialogOk').on('click', function() {
            Dialog.hide();
            if (Utils.isValid(Dialog.okCallback)) {
                Dialog.okCallback(Dialog.data);
                Dialog.okCallback = null;
            }
            if (Utils.isValid(Dialog.cancelCallback)) {
                Dialog.cancelCallback = null;
            }
        });

        $('#dialogCancel').on('click', function() {
            Dialog.hide();
            if (Utils.isValid(Dialog.okCallback)) {
                Dialog.okCallback = null;
            }
            if (Utils.isValid(Dialog.cancelCallback)) {
                Dialog.cancelCallback(Dialog.data);
                Dialog.cancelCallback = null;
            }
        });

    },

    _generate: function() {
        var str = "<div id='--dialog' class='dialog'><div class='wrapper'>";
        str += "<div id='--dialog-del' class='del-btn' onclick='Dialog.reset()'></div>";
        str += "<div id='--dialog-cnt' class='contents'></div>";
        str += "</div>";
        return str;
    },

    show: function(timeout) {
        $("#--dialog#dialog").css("display", "block");
        Dialog.resize();

        // set focus on the default button after enabling the dialog
        if ($('#--dialog-cancel').length) {
            $('#--dialog-cancel').focus();
        }
        else if ($('#--dialog-ok').length) {
            $('#--dialog-ok').focus();
        }
    },

    resize: function() {
        // set the dialog box to center
        if ($('body').width() > 900) {
            var left = ($('body').width() - 500 - 42) / 2;
            //$("#--dialog .wrapper").css('margin-left', left);
        }
        var height = $("#--dialog .contents").height();
        var top = screen.height / 2 - height / 2 - 10;

        //$("#--dialog .wrapper").css('margin-top', top);
    },

    hide: function() {
        $("#--dialog").css("display", "none");
    },

    set: function(showDel) {
        this.show();
        // this.disableScroll();
        this.ignoreHideProgress = true;

        $("body").on('keyup', function(e) {
            if (e.keyCode == 27) {
                Dialog.onCancel();
            }
            else if (e.keyCode == 13) {
                if ($('#--dialog-ok').length) {
                    //Dialog.onConfirm();
                }
            }
        });

    },
    set2: function(showDel) {
        this.show();
        this.ignoreHideProgress = true;

        $("body").on('keyup', function(e) {
            if (e.keyCode == 27) {
                Dialog.onCancel();
                console.log(e)
            }
            else if (e.keyCode == 13) {
                if ($('#--dialog-ok').length) {
                    //Dialog.onConfirm();
                    console.log(e)
                }
            }
        });

    },

    reset: function() {
        this.hide();
        this.enableScroll();
        this.ignoreHideProgress = false;
        $("body").off('keyup');
    },



    // Other dialogs ---------------------------------------------------------------
    //

    cbFunc: null,
    cbFuncCancel: null,
    // alert: function(msg, cbfunc) {
    //     this.cbFunc = cbfunc;
    //     this.cbFuncCancel = cbfunc;
    //
    //     var str = "<div class='desc mbot-25'>" + msg + "</div>";
    //     str += "<div id='--dialog-ok' class='button' onclick='Dialog.onConfirm()'>확인</div>";
    //
    //     $('#--dialog-cnt').html(str);
    //
    //     this.set2();
    // },

    alert: function(msg, okCallback, data) {
        Dialog.okCallback = okCallback;
        Dialog.cancelCallback = okCallback;
        Dialog.data = data;

        $('#dialog .modal-body').html(`<div>${msg}</div>`);
        Dialog.title('');
        $('#dialogCancel').hide();
        Dialog.show();
        return Dialog;
    },

    title: function(title){
        $('#dialog .modal-title').html(title);
    },
    show: function() {
        Dialog.modal.show();
    },
    hide: function() {
        Dialog.modal.hide();
    },

    confirm: function(msg, cbfunc, cbload, options) {
        this.okCallback = cbfunc;
        this.cancelCallback = cbload;
        if (!isValid(options)) options = {};

        let str = `<div>${msg}</div>`;
        $('#dialog .modal-title').html("");
        $('#dialog .modal-body').html(str);
        this.set();
    },

    onConfirm: function() {
        this.reset();
        if (this.cbFunc != null) {
            var curfunc = this.cbFunc;
            this.cbFunc = null;
            curfunc();
        }
    },

    onCancel: function() {
        this.reset();
        if (this.cbFuncCancel != null) {
            var curfunc = this.cbFuncCancel;
            this.cbFuncCancel = null;
            curfunc();
        }
    },

    cbSelect: null,
    cbDel: null,
    select: function(list, cbfunc, showicn) {
        this.cbSelect = cbfunc;

        var str = "<div class='dialog-sel'>";
        var cnt = 0;
        for (var i=0; i<list.length; i++) {
            if (i == 0) {
                var title = (list[0].icon == "title") ? list[0].text : "선택해 주세요.";
                //str += "<div class='title'><div class='pad-24'></div></div>";
                str += "<div class='section pad-25 mtop-10 mbot-10'>";
            }
            if (list[i].icon == "title") continue;

            var cmd = (list[i].disabled != true) ? " onclick='Dialog.onSelect(" + i + ")'" : "";
            var gray = (list[i].disabled == true) ? " lightgray" : "";
            var bdtop = (cnt++ > 0) ? " bdtop-eee" : "";

            if (list[i].sep == true) {
                str += "<div class='section mtop-7 mbot-7 bdtop-eee'></div>";
                bdtop = "";
            }

            str += "<div class='section dis-f" + bdtop + "'" + cmd + ">";
            if (showicn == true) {
                str += "<div class='text" + gray + " "+list[i].icon+"'>" + list[i].text + "</div>";
                str += "<div class='icon pointer " + list[i].icon + "'></div>";
            }
            else {
                str += "<div class='text wid-100" + gray + "'>" + list[i].text + "</div>";
            }
            str += "</div>";
        }
        if (list.length > 0) {
            str += "</div>";
        }
        str += "</div>";

        $('#--dialog-cnt').html(str);

        setTimeout(function() {
            $("html").click(function(e) {
                if($(e.target).hasClass("dialog")) {
                    Dialog.reset();
                    $("html").off("click");
                }
            });
        }, 50);

        this.set();
    },
}

let ImageList = {
    curidx: 0,
    onload: false,

    init: function () {
        if (!isValid(pagectx)) pagectx = {};
        pagectx.imglist = [];

        this.curidx = 0;
        this.onload = false;
    },

    add: function (vid, url, isback) {
        pagectx.imglist.push({vid: vid, url: url, isback: isback});
    },

    getViewId: function () {
        return "--img-" + pagectx.imglist.length;
    },

    load: function (index, cbfunc) {
        // ignore duplicate requests
        if (this.onload && index > this.curidx) return;

        if (!isValid(index)) index = 0;

        //console.log("ImageList.load(" + this.curidx + "): " + index);
        this._load(index, cbfunc);
    },

    onLoading: function () {
        return this.onload;
    },

    timeout: null,
    _load: function (index, cbfunc) {
        this.onload = true;

        var list = pagectx.imglist;
        if (index >= list.length) {
            this.onload = false;
            if (isValid(cbfunc)) cbfunc(index);
            return;
        }

        var imgobj = list[index];
        if (imgobj == null) {
            console.log(index);
        }
        var elem = "#" + imgobj.vid;

        var img = new Image();
        img.onerror = img.onabort = function() {
            ImageList._onError(elem, index, cbfunc);
        };
        img.onload = function() {
            $(elem + " .loading").addClass("hide");
            if (imgobj.isback) {
                $(elem).css("background-image", "url(\"" + imgobj.url + "\")");
                ImageList._clearTimer();
                ImageList.curidx = index;
                ImageList._load(index + 1, cbfunc);
            }
            else {
                $(elem).animate({opacity: 0}, 0, function() {
                    $(this).css({'background-image': "url(\"" + imgobj.url + "\")"})
                        .animate({opacity: 1}, 300, function() {
                            ImageList._clearTimer();
                            ImageList.curidx = index;
                            ImageList._load(index + 1, cbfunc);
                        });
                });
            }
        }
        this.timeout = setTimeout(function() {
            ImageList._onError(elem, index, cbfunc);
        }, 3000);
        img.src = imgobj.url;
    },

    _onError: function (elem, index, cbfunc) {
        $(elem + " .loading").addClass("hide");
        $(elem).css("background-image", "url('images/bg_cafe.jpg')");
        this._clearTimer();
        this.curidx = index;
        this._load(index + 1, cbfunc);
    },

    _clearTimer: function () {
        if (this.timeout) {
            clearTimeout(this.timeout);
            this.timeout = null;
        }
    },
};

var ImageValidator = {
    images: null,
    max: 50,

    start: function (list, max, cbfunc) {
        this.images = [];
        this.max = max;
        this.next(list, cbfunc, 0);
    },

    next: function (list, cbfunc, index) {
        if (index >= list.length || index >= this.max) {
            if (isValid(cbfunc)) {
                for (var i=this.max; i<list.length; i++) this.images.push(list[i]);
                cbfunc(this.images);
            }
            return;
        }
        var url = list[index].thumbnail;
        $.ajax({url: url, type: 'HEAD', data: index + "", success: function(response) {
                var uval = unescape($(this).attr('url'));
                var idx = uval.lastIndexOf("&");
                if (idx > 0) {
                    var id = parseInt(uval.substring(idx+1));
                    ImageValidator.images.push(list[id]);
                }
                console.log("[ImageValidator.ok." + index + "]" + uval);
                ImageValidator.next(list, cbfunc, index + 1);
            },
            error: function() {
                var uval = unescape($(this).attr('url'));
                console.log("[ImageValidator.error." + index + "]" + uval);
                ImageValidator.next(list, cbfunc, index + 1);
            }});
    },
};

let Mpx = {
    MAX: 0,
    pageidx: 0,
    curheight: 0,
    cursecheight: 0,

    init: function () {
        this.MAX = $(".mpx-menu").children().length;
    },

    goTab: function (curidx, max) {
        if (this.MAX === 0) return;
        let preidx = this.pageidx;
        if (curidx - preidx === 0) return;

        this._showPage(curidx, preidx, null, max);
        this.pageidx = curidx;
    },

    _showPage: function (toidx, fromidx, isnew, max) {
        $("#--mnu-" + fromidx).removeClass("selected");
        $("#--mnu-" + toidx).addClass("selected");

        // $("#--tab-" + fromidx).removeClass("selected");
        // $("#--tab-" + toidx).addClass("selected");

        let pane = $("#--pane-"+toidx);
        let width = pane.width();
        let height = 0;
        if (isValid(max)) {
            for(let i=0; i<4; i++) {
                height = Math.max($("#--pane-"+i).height(), height);
            }
        } else {
            height = pane.height();
        }
        this.curheight = height;

        let mleft = width * -1 * toidx;
        let intv = (isnew === true) ? 0 : 300;
        $(".mpx-con").velocity({
            "margin-left": mleft
        }, intv).promise().done(function () {
            //$(".mpx-con").height(height);
            this.pageidx = toidx;
            if (isnew !== true) ScrollPos.goTo();
        });

    }
}

let Page = {
    init: function  (url, cbfunc, params) {
        if (Config.platform == null) {
            let cfgDevice = Params.get("cfgDevice", true);
            if (cfgDevice == null) {
                Config.platform = Util.getPlatform();
            } else {
                Config.platform = cfgDevice.platform;
            }
        }

        //안드로이드 앱 뒤로가기 버튼
        // window.backKeyPressed = new Event('backKey');
        // window.addEventListener('backKey',() => {
        //     if(sessionStorage.getItem("imageOn")!=null){
        //         hideImgMobile();
        //     }else if(window.location.pathname.includes('main')){
        //
        //     }else{
        //         Page.back();
        //     }
        // },false);


        $('html, body').on('touchmove', function(event) {
            if(event.touches.length==1){
                event.preventDefault();
                event.stopPropagation();
            }
        }, false);

        Dialog.init();
        ScrollPos.init(url);
        SSO.init(url);


        // for pc
        if (Config.platform === "pc") {
            this._setHeader();
            PageMnu.show(".page-mnu", PageMnu.getCls(url));
            Mpx.init();
        }

        Evt.init();

        if (!isValid(params)) params = {};
        //자동 로그인을 위한 세션 코드 0623
        AJAX.call("jsp/session.jsp", null, function(data) {
            let ret = data.trim();
            let sesobj = (ret !== "NA") ? JSON.parse(ret) : {};
            url = url.split("/").find(elem => elem.includes(".html"))
            let param = (url == null) ? null : SessionStore.get(url);
            if (!isValid(param)) param = {};
            param.usrobj = sesobj.usrobj;

            if(!(params.nolog !== true && ret === "NA")){
                var addrobj = SessionStore.get("global.addrobj");
                if (addrobj != null) param.addrcode = addrobj.code;
                if (param.addrcode == null) param.addrcode = "260100";
                if(isValid(sesobj.usrobj) && isValid(sesobj.usrobj.addrobj)) param.addrcode = sesobj.usrobj.addrobj.code; // 주소이슈 임시해결, TODO_folks global.addrobj 부분 해결

                if (cbfunc != null) {
                    cbfunc(param);
                }
            }else {
                Dialog.alert("로그인이 필요한 서비스 입니다.", function() {
                    window.location.href = "index.html";
                });
            }
        });
    },

    goHome: function() {
        this.move("main.html");
    },

    go: function(url, param) {
        this._go(url, param, false);
    },

    reload: function() {
        location.reload();
    },

    goFeed: function(fid) {
        ScrollPos.set();
        this.go("feedView.html?fid=" + fid);
    },

    goSearch: function (keys, keys2) {
        if (!isValid(keys)) {
            keys = $("#--srch-key").val();
            if (!isValid(keys2)) {
                keys2 = $("#--srch-key2").val();
            }
        }
        if (isValid(keys)) keys = keys.trim();
        if (isValid(keys2)) keys2 = keys2.trim();
        Page.go("feedSearch.html", {keys: keys, keys2: keys2});
    },

    move: function(url, param) {
        this._go(url, param, true);
    },

    back: function (noanim) {
        // history.back();
        location.href = document.referrer;
    },

    _go: function(url, param, jump) {
        if (param != null) SessionStore.set(url, param);
        let platform = Config.platform;
        if (platform === "mobile" || platform === "android") {
            url = Config.url + "/mobile/" + url;
        }


        if (jump) {
            window.location.replace(url);
        } else {
            window.location.href = url;
        }

    },

    setHandler: function(opts, cbfunc) {
        if (opts == null) opts = {};

        SwipeHandler.init();

        if (opts.scroll) {
            $(document).scroll(function(event){
                Page._hideNewFeed();
                Page._showMenu();
                Page._showLoadingIcon(opts.icovw, cbfunc);
            });
        }
    },

    lastScrollTop: 0,
};

// -----------------------------------------------------------------------------------------
// PageMnu object
//모바일 하단 메뉴바 => 재사용
var PageMnu = {
    menus: [
        {cls: "home", link: "main.html",  pc: true, mob: true},
        {cls: "around", link: "feedAround.html",  pc: false, mob: true},
        {cls: "new", link: "", desc: "", pc: false, mob: true},
        {cls: "hot", link: "feedHot.html", pc: false, mob: true},
        {cls: "user", link: "user.html",  pc: false, mob: true},
    ],
    show: function(view, cls) {
        let str = "";
        for(let i=0; i<this.menus.length; i++) {
            let menu = this.menus[i];
            let platform = Config.platform;
            if (platform === "pc") {
                if (!menu.pc) continue;
            } else {
                if (!menu.mob) continue;
            }

            let selected = (cls === menu.cls) ? " selected" : " ";
            str += "<div class='menu-box"+ selected +(menu.cls === 'filter' ? ' nohover' : '')+"'>"
            str += "<div class='menu " + menu.cls  + "'";
            /*if (cls === menu.cls) {
				str += " onclick='ScrollPos.goTop(true)'></div>";
			} else */
            if (menu.cls === "new") {
                str += " onclick='Page._showNewFeed()'></div>";
            } else if (menu.cls === "filter") {
                str += "></div>";
            } else {
                str += " onclick='Page.go(\"" + menu.link + "\")'></div>";
            }
            if(isValid(menu.desc)) {
                str += "<div class='desc'>" + menu.desc + "</div>";
            }

            str += "</div>";
        }
        $(view).html(str);

        str = "";
        str += "<div class='nheader'>";
        str += "<div>글 작성하기</div>";
        str += "<div class='cancel' onclick='Page._hideNewFeed()'></div>";
        str += "</div>";
        str += "<div class='newfeed-box'>";
        str += "<div class='newfeed' onclick='Page.go(\"feedAdd.html\")'>";
        str += "<div class='ico nfeed'></div>";
        str += "<div class='desc'>글 작성하기</div>";
        str += "</div>";
        if(pagectx?.usrobj?.type == "B"){
            str += "<div class='newfeed' onclick='Page.go(\"service.html\")'>";
            str += "<div class='ico npoll'></div>";
            str += "<div class='desc'>제품/서비스 등록</div>";
            str += "</div>";

        }
        str += "</div>";
        $(".page-newfeed").html(str);

    },

    hide: function() {

    },

    getCls: function(url) {
        const res = this.menus.find(menu => menu.link !== "" && url.includes(menu.link));
        return res && res.cls;
    },
}

var Params = {
    init : function() {
    },

    clear : function(name) {
        localStorage["Params>" + name] = null;
    },

    get : function(name, flag) {
        var str = localStorage["Params>" + name];
        return (isValid(str) && str != "null") ? JSON.parse(str) : (flag ? null : {});

    },

    set : function(name, val, mcode) {
        try {
            localStorage["Params>" + name] = JSON.stringify(val);

        } catch (ex) {
            console.log("Params.set(): " + ex + " onn setting '" + name
                + "'\n\n" + new Error().stack);
            Audit.showSessionMem();
        }
    },
}

var PhoneNum = {
    check: function (number) {
        if (!isValid(number)) return false;

        var num = number.replace(/ /g, "").replaceAll(/-/g, "");

        var regex = /^\d+$/;
        if(!regex.test(num)) return false;

        // now, number format check ---------------------------------
        if (num.substring(0,2) == "82") {
            num = "0" + num.substring(2);
        }

        var ncnt = num.length;
        if (num.substring(0,1) != "0" || ncnt < 9 || ncnt > 11) return false;

        if (num.substring(0,2) == "01") {
            if (num.startsWith("0111111")) return true;

            var hdr = num.substring(0,3);
            if ((hdr == "010" || hdr == "011") && ncnt == 11) return true;		// 010-xxxx-xxxx
            else if (ncnt == 10) return true;									// 011-xxx-xxxx
            return false
        }
        return true;	// 031-xxx-xxxx
    },

    getMid: function (mid) {
        if (!isValid(mid)) return "";

        mid = mid.replace(/[^0-9]/g, '');
        if (mid.substring(0, 2) == "82") {
            return mid;
        }
        else if (mid.substring(0, 1) == "0") {
            return "82" + mid.substring(1);
        }
        return mid;
    },

    format: function (mid, format) {
        if (!isValid(mid)) return "";

        // if the given mid is from kakao or google
        if (mid.startsWith("kakao") || mid.startsWith("google")) {
            return mid;
        }

        var num;
        if (mid.indexOf(0) == "0") {
            num = mid.replace(/-/g, '');
        }
        else {
            // if the given mid starts with a country code such as 82~
            num = "0" + mid.substring(2).replace(/-/g, '');
        }

        if (format) {
            var ncnt = num.length;
            if (ncnt == 11) {
                return num.substring(0,3) + "-" + num.substring(3,7) + "-" + num.substring(7);
            }
            else if (ncnt > 8 && ncnt < 11){
                if (num.substring(0,2) == "02") {
                    return num.substring(0,2) + "-" + num.substring(2, ncnt-4) + "-" + num.substring(ncnt-4, ncnt);
                }
                else {
                    return num.substring(0,3) + "-" + num.substring(3, ncnt-4) + "-" + num.substring(ncnt-4, ncnt);
                }
            }
            else {
                return num.substring(0, ncnt-4) + "-" + num.substring(ncnt-4, ncnt);
            }
        }
        return num;
    }
};


// -----------------------------------------------------------------------------------------
// SwipeHandler and ScrollPos object

var ScrollPos = {
    src: null,
    top: null,

    init: function (source) {
        this.src = source;
    },

    checkInit: function () {
        return isValid(this.src);
    },

    _name: function (source) {
        var src = (isValid(source)) ? source : this.src;
        var idx = src.lastIndexOf(".");
        var fname = (idx > 0) ? src.substring(0, idx) : src;
        return fname + "ScrollPos";
    },

    set: function (tag) {
        var top = $("body").scrollTop();
        console.log("ScrollPos.set: " + this.src + ", " + top);
        SessionStore.set(this._name(), {top: top, tag: tag});
    },

    get: function () {
        return SessionStore.get(this._name());
    },

    getTag: function () {
        var obj = SessionStore.get(this._name());
        return (isValid(obj)) ? obj.tag : null;
    },

    clear: function (source) {
        SessionStore.set(this._name(source), "");
    },

    goTo: function (noAnim) {
        var obj = SessionStore.get(this._name());
        if (isValid(obj)) {
            console.log("ScrollPos.goto: " + this.src + ", " + obj.top);
            if (noAnim) {
                $("body").scrollTop(obj.top);
            }
            else {
                var sec = (obj.top > 1000) ? 800 : 400;
                $("body").stop().animate({ scrollTop: obj.top }, sec);
            }
            this.clear();
        }
    },

    goTop: function (anim) {
        if (anim) {
            $("body").stop().animate({ scrollTop: 0 }, 400);
        }
        else {
            $("body").scrollTop(0);
        }
    }
};

var SwipeHandler = {
    disable: false,

    init: function() {
        document.addEventListener('touchstart', this.handleTouchStart, false);
        document.addEventListener('touchmove', this.handleTouchMove, false);
    },

    xDown: null, yDown: null,
    handleTouchStart: function(evt) {
        const firstTouch = (evt.touches || evt.originalEvent.touches)[0];   // // browser API or jQuery
        this.xDown = firstTouch.clientX;
        this.yDown = firstTouch.clientY;
    },

    handleTouchMove: function(evt) {
        if (!this.xDown || !this.yDown) return;

        // disable swipe if the events occur on .scroll-box
        var pobjs = $(evt.srcElement).parents();
        for (var i=0; i<pobjs.length; i++) {
            if (pobjs[i].className === "scroll-box") return;
        }

        var xUp = evt.touches[0].clientX;
        var yUp = evt.touches[0].clientY;

        var xDiff = this.xDown - xUp;
        var yDiff = this.yDown - yUp;

        if (Math.abs(xDiff) > Math.abs(yDiff)) { /*most significant*/
            if (xDiff < 0) Page.back();
        }
        else {
            if (yDiff > 0) {
                /* up swipe */
            } else {
                /* down swipe */
            }
        }
        /* reset values */
        this.xDown = null;
        this.yDown = null;
    },
}

let SNS = {
    kakaoJSkey: "",

    init: function (src) {
        // pc, 모바일, android, ios에 따라서 불러오는거 다르게 하도록 설정할 것

        //web browser(pc, mobile)
        $.getScript("https://developers.kakao.com/sdk/js/kakao.js", function () {
            Kakao.init(SNS.kakaoJSkey);
            console.log("success kakao connect");
        });
    },

    share: function (fid) {
        let feed = CacheMgr.getFeed(fid);
        if (feed == null) {
            AJAX.call("jsp/feedGet.jsp", {fid: fid}, function(data) {
                console.log("[CacheMgr.getFeed] fetch a new feed from the server for share...");
                let feed = JSON.parse(data.trim());
                console.log(feed);
                if (feed != null) SNS._share(feed);
            });
        } else {
            SNS._share(feed);
        }
    },
}

var SSO = {
    src: null,

    init: function(src) {
        this.src = src;
    },

    //카카오 회원가입 시 사용
    cbExternAuth : function(platform, auth) {
        var pstr = "platform=" + platform;
        if(platform === "kakao") {
            pstr += "&acctoken=" + auth.access_token
                + "&reftoken=" + auth.refresh_token;
            var token = Params.get("urltoken", true);
            if (isValid(token)) {
                pstr += "&urltoken=" + token;
                Params.clear("urltoken");
            }
        } else if(platform == "naver") {
            pstr += "&image=" + auth.profile_image
                + "&name=" + auth.name
                + "&id=" + auth.id
                // 선택사항;
                + "&birthday=" + auth.birthday
                + "&birthyear=" + auth.birthyear
                + "&email=" + auth.email
                + "&gender=" + auth.gender
                + "&mobile=" + auth.mobile;

        } else if(platform == "google") {
            pstr += "&image=" + auth.profile_image
                + "&name=" + auth.name
                + "&id=" + auth.id
                // 선택사항
                + "&email=" + auth.email
        }

        this._login(pstr, function(code) {
            Page.goHome();
        });
    },

    login : function(type, usrobj) {
        if (type == "kakao") {
            this._loginKakao();
        } else {
            this._login2(usrobj);
        }
    },

    _login : function(pstr, cbfunc, type) {
        AJAX.call("jsp/login.jsp", pstr, function (data) {
            var code = data.trim();
            if (code == "NA") {
                Dialog.alert("입력하신 아이디가 존재하지 않습니다.");
            } else {
                cbfunc(code);
            }
        });
    },

    _login2: function(usrobj) {
        if(usrobj == null) return;
        var pstr = "mid=" + usrobj.mid + "&pass=" + usrobj.pass;
        this._login(pstr, function(code) {
            if (code == "PS") {
                Dialog.alert("비밀번호가 일치하지 않습니다.");
            } else if (code == "AD") {
                Dialog.confirm("관리자 페이지로 이동하시겠습니까?", function() {
                    Page.move("mainD.html");
                }, function() {
                    Page.goHome();
                });
            }
            else if(code == "AD2"){
                Dialog.confirm("관리자 페이지로 이동하시겠습니까?", function() {
                    Page.move("mainM.html");
                }, function() {
                    Page.goHome();
                });
            }
        });
    },

    _loginKakao: function() {
        if (isDevice() && Params.get("cfgDevice").platform != "undefined") {
            // native app
            if (Params.get("cfgDevice").platform === "android") { // Android
                var url = "locker://login.kakao";
                window.location.replace(url);
            } else if (Params.get("cfgDevice").platform === "ios") { // IOS
                webkit.messageHandlers.loginKakao.postMessage("");
            }
        } else if (isMobile()) {
            // mobile browser (as well as ios app currently)
            Kakao.Auth.authorize({
                redirectUri : Config.url + "/jsp/oauthKakao.jsp"
            });
        } else {
            // Kakao.Auth.authorize({
            // 	redirectUri:  "https://www.locker.com/jsp/oauthKakao.jsp"
            // });
            // desktop browser
            Kakao.Auth.login({
                success : function(auth) {
                    SSO.cbExternAuth("kakao", auth);
                },
                fail : function(err) {
                    Dialog.alert("로그인에 실패했습니다. 관리자에게 문의해주세요.");
                    console.log(JSON.stringify(err));
                }
            });
        }
    },

    logout: function () {
        Dialog.confirm("로그아웃 하시겠습니까?", function () {
            AJAX.call("jsp/logout.jsp", null, function () {
                Page.go("index.html");
            });
        });
    },
}

var Util = {
    getPlatform: function() {
        let varUA = navigator.userAgent.toLowerCase();
        if ( varUA.indexOf('android') > -1) {
            //안드로이드
            return "android";
        } else if ( varUA.indexOf("iphone") > -1||varUA.indexOf("ipad") > -1||varUA.indexOf("ipod") > -1 ) {
            //IOS
            return "ios";
        } else {
            //아이폰, 안드로이드 외
            return "other";
        }
    },

    goLink: function(link) {
        console.log(link);
        Dialog.confirm("위 링크는 안전하지 않은 페이지 일 수 있습니다.<br>연결하시겠습니까?", function() {
            window.open(link, '_blank');
        })
    },

    timeForToday: function(value) {
        var _value = value.split(" ");
        value = _value[0] + "T" + _value[1];
        const today = new Date();
        const timeValue = new Date(value);
        const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);
        if(betweenTime < 1) return "방금전";
        if(betweenTime < 60) return `${betweenTime}분전`;

        const betweenTimeHour = Math.floor(betweenTime / 60);
        if(betweenTimeHour < 24) return `${betweenTimeHour}시간전`;

        const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
        if(betweenTimeDay < 365) return `${betweenTimeDay}일전`;

        return `${Math.floor(betweenTimeDay / 365)}년전`;
    },
};

var WebText = {
    // used to store the text input into the server database
    escape: function (str) {
        if(!isValid(str)) return "";
        str = str.trim();
        str =(!isValid(str)) ? "" : str.replace(/%/g, '%25')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;')
            .replace(/\t/g, '&#9;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/\\/g, '&#92;')
            .replace(/\n/g, '<br> ');
        return str;
    },

    unescape: function (html, feed) {
        if(!isValid(html)) return "";
        html = html.trim();
        html = (!isValid(html)) ? "" : html.replace(/%25/g, '%')
            .replace(/&amp;/g, '&')
            .replace(/&quot;/g, '\"')
            .replace(/&#39;/g, '\'')
            .replace(/&#9;/g, '\t')
            .replace(/&lt;/g, '<')
            .replace(/&gt;/g, '>')
            .replace(/&#92;/g, '\\')
            .replace(/&nbsp;/g, ' ')
            .replace(/%20/g, ' ')
            .replace(/u2018/g, '\'')
            .replace(/u2019/g, '\'')
            .replace(/u201C/g, '"')
            .replace(/u201D/g, '"')
            .replace(/u20[0-3][0-F]/g, '');

        if(!feed) {
            html = html.replace(/<br.*?>/g, '\n ');
        }
        return html;
    },
};

function formatNum(val){
    while (/(\d+)(\d{3})/.test(val.toString())){
        val = val.toString().replace(/(\d+)(\d{3})/, '$1'+','+'$2');
    }
    return val;
}

function isDevice() {
    if (typeof _lockerInterface != "undefined") {
        return true;
    } else {
        let dev = Params.get("cfgDevice", true);
        return dev && (dev.platform === "android" || dev.platform === "ios");
    }
}
function isEmail(val) {
    var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
    return regExp.test(val);
}

function isMobile() {
    let dev = Params.get("cfgDevic").platform;
    return dev === "mobile";
}

function isValid(param) {
    return (param != null && param != "" && typeof param != "undefined");
}

function isUrl(str) {
    try {
        new URL(str);
    } catch (_) {
        return false;
    }

    return true;
}

function Request() {
    var requestParam = null;
    this.getParameter = function (param, utf8) {
        var url = (utf8 == true) ? location.href : unescape(location.href);
        var paramArr = (url.substring(url.indexOf("?") + 1, url.length)).split("&");

        requestParam = null;
        for (var i=0; i < paramArr.length; i++) {
            var temp = paramArr[i].split("=");
            if (temp[0].toUpperCase() == param.toUpperCase()) {
                requestParam = paramArr[i].split("=")[1];
                if (utf8 == true) {
                    requestParam = decodeURIComponent(requestParam);
                }
                break;
            }
        }
        return requestParam;
    }
}

function stripSpcChar(str, params) {
    if (params == null) params = {};
    if (!isValid(str)) return "";

    var str = str.replace(/u2018/g, '\'').replace(/u2019/g, '\'').replace(/u201C/g, '"').replace(/u201D/g, '"')
        .replace(/u20[0-3][0-F]/g, '');

    if (params.saveColon) {
        str = str.replace(/[a-z]|[\[\]{}()<>?|`!@#$%^&*●\-_+=,.;\"'\\]/g, "").replace(/\//g, "");
    }
    else {
        str = str.replace(/[a-z]|[\[\]{}()<>?|`~!@#$%^&*●\_+=,.;:\"'\\]/g, "").replace(/\//, "");
    }

    if (params.delSpace) str = str.replace(/ /g, "");
    return str;
}

let Evt = {
    init: function () {
        this._init();
        this._setHeaderEvt();

        $("body").click(function (event) {
            // for this._setHeaderEvt();
            if (!event.target.matches(".page-hdr .icon-box .icon")) {
                $(".page-hdr .icon-box .icon").each(function () {
                    $(this).children(".dropdown-content").addClass("hide");
                });
            }
            // for user.html page reporter user
            if (!event.target.matches(".report .ico-dots")) {
                $(".report .ico-dots").each(function () {
                    $(this).siblings(".dropdown-content").addClass("hide");
                });
            }
        });
    },

    _init: function () {
        $(".inner-box").click(function (evt) {
            evt.stopPropagation();
        });
        $(".ico-dots").click(function (evt) {
            evt.stopPropagation();
        });
        $(".poll").click(function (evt) {
            evt.stopPropagation();
        });
    },

    _setHeaderEvt: function () {
        $(".page-hdr .icon-box .icon").click(function (event) {
            let content = $(this).children(".dropdown-content");
            if (content.hasClass("hide")) {
                content.removeClass("hide");
            } else {
                content.addClass("hide");
            }
        });

        $(".page-hdr .icon-box .icon").click(function (evt) {
            evt.stopPropagation();
        });

        $("#--srch-btn").click(function (evt) {
            evt.stopPropagation();
        });
    },
}

var SessionStore = {
    set: function (name, val) {
        name = "nuc:" + name;
        sessionStorage.setItem(name, JSON.stringify(val));
    },

    get: function (name) {
        name = "nuc:" + name;
        var str = sessionStorage.getItem(name);
        return (str == null || str == "null" || str == "undefined") ? null : JSON.parse(str);
    },

    remove: function (name) {
        name = "nuc:" + name;
        sessionStorage.removeItem(name);
    },

    clear: function() {
        sessionStorage.clear();
    },

    log: function () {
        var str = '';
        for(var key in window.sessionStorage) {
            if(window.sessionStorage.hasOwnProperty(key)) {
                var locstr = window.sessionStorage[key];
                var size = (locstr.length*16)/(8*1024);
                if (size >= 1) {
                    console.log(key + ": " + size.toFixed(1) + " KB");
                }
                str += locstr;
            }
        }
        if (isValid(str)) {
            var size = (str.length*16)/(8*1024);
            /* if (size > 2000) */ console.log("---- Session storate TOTAL: " + size.toFixed(1) + " KB");
        }
    },
};

let Utils = {
    isValid: function(something) {
        return (something != null && something != "" && typeof something != "undefined");
    },
    isNull: function(something) {
        return !Utils.isValid(something);
    },
    dateFormat: function(dateOrString, formatString) {
        if(typeof dateOrString === 'string') {
            if(dateOrString.includes('T')) {
                return Utils.dateFormat(new Date(x), 'yyyy-MM-dd hh:mm:ss');
            }
            formatString = dateOrString;
            dateOrString = new Date();
        }
        if(dateOrString instanceof Date && typeof formatString == 'undefined') {
            formatString = 'yyyy-MM-dd hh:mm:ss';
        }
        if(Utils.isNull(dateOrString)) {
            return Utils.dateFormat(new Date(), 'yyyy-MM-dd hh:mm:ss');
        }
        let z = {
            M: dateOrString.getMonth() + 1,
            d: dateOrString.getDate(),
            h: dateOrString.getHours(),
            m: dateOrString.getMinutes(),
            s: dateOrString.getSeconds(),
        };
        formatString = formatString.replace(/(M+|d+|h+|m+|s+)/g, function (v) {
            return ((v.length > 1 ? '0' : '') + eval('z.' + v.slice(-1))).slice(-2);
        });

        return formatString.replace(/(y+)/g, function (v) {
            return dateOrString.getFullYear().toString().slice(-v.length);
        });
    }
};

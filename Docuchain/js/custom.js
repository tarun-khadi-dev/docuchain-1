if (document.location.hostname == 'pratikborsadiya.in') {
    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r; i[r] = i[r] || function () {
            (i[r].q = i[r].q || []).push(arguments)
        }, i[r].l = 1 * new Date(); a = s.createElement(o),
            m = s.getElementsByTagName(o)[0]; a.async = 1; a.src = g; m.parentNode.insertBefore(a, m)
    })(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');
    ga('create', 'UA-72504830-1', 'auto');
    ga('send', 'pageview');
}

$('#demoSelect').select2();
/* Flag */
$('#basic').flagStrap();

$('#origin').flagStrap({
    placeholder: {
        value: "",
        text: "Country of origin"
    }
});

$('#options').flagStrap({
    countries: {
        "AU": "Australia",
        "GB": "United Kingdom",
        "US": "United States"
    },
    buttonSize: "btn-sm",
    buttonType: "btn-info",
    labelMargin: "10px",
    scrollable: false,
    scrollableHeight: "350px"
});

$('#advanced').flagStrap({
    buttonSize: "btn-lg",
    buttonType: "btn-primary",
    labelMargin: "20px",
    scrollable: false,
    scrollableHeight: "350px",
    onSelect: function (value, element) {
        alert(value);
        console.log(element);
    }
});


/*  Upload Profile */

$(document).on('click', '.browse', function () {
    var file = $(this).parent().parent().parent().find('.file');
    file.trigger('click');
});
$(document).on('change', '.file', function () {
    $(this).parent().find('.form-control').val($(this).val().replace(/C:\\fakepath\\/i, ''));
});

/* Multi Select */

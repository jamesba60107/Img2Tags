$(document).ready(function() {
    loadResources();
});

function loadResources() {
    //fileUpload
    addStylesheet('fileUpload/fileUpload.css');
    $.getScript("fileUpload/fileUpload.js");
    //fileWatcher
    addStylesheet('fileWatcher/fileWatcher.css');
    $.getScript("fileWatcher/fileWatcher.js");
}
function addStylesheet(href) {
    $('<link>', {
        rel: 'stylesheet',
        type: 'text/css',
        href: href
    }).appendTo('head');
}

const fileList = $('#file-list');
const status = {
    errorCode: 0,
    checkingTags: false
};

$(document).ready(function() {
    getFilenameAndUpdate(); // 檢查伺服器資料夾檔案名稱

    //
    $('#file-watcher-download-button').click(() => {
        clickDownloadButton();
    });

    $('#file-watcher-delete-button').click(() => {
        clickDeleteButton();
    });
});

function getFilenameAndUpdate() {
    if (status.errorCode === 0 && !status.checkingTags ) { // 檢查是否有錯誤代碼 及 正在取標籤
        $.ajax({
            url: '/fileWatcher/filenames',
            type: 'GET',
            dataType: 'json',
            success: (files) => {
                // 初始化 fileList
                fileList.empty();
                fileList.append('<h2>上傳清單</h2>');
                files.forEach(file => fileList.append('<li>' + file + '</li>'));

                updateEmptyMessageVisibility();
                setTimeout(getFilenameAndUpdate, 5000);
                //setTimeout(fetchFilesAndUpdate, 5000);  // 在這裡設定下次執行
            },
            error: (error) => {
                status.errorCode = 1;
                Swal.fire({
                    icon: 'error',
                    title: '獲取檔案失敗',
                    text: `請點選下方"確認"按鈕重整頁面`,
                    allowOutsideClick: true,
                    showCancelButton: false,
                    showCloseButton: false,
                    confirmButtonText: '確認',
                    customClass: {
                        confirmButton: 'custom-sweetalert-confirm-button'
                    },
                }).then((result) => {
                    if (result.isConfirmed) {
                        // 在 ”確認“ 按鈕被點擊時重整頁面
                        location.reload();
                    }
                });
            }
        });
    }
}

function clickDownloadButton() {
    // 點擊按鈕時 才檢查`/fileWatcher/tags` API
    status.checkingTags = true; // 取標籤時 不要檢查檔案名稱

    // 顯示等待中的SweetAlert2彈出式視窗
    Swal.fire({
        icon: 'info',
        title: '請稍等',
        html: "正在生成關鍵字CSV檔案",
        timerProgressBar: true,
        allowOutsideClick: false,
        showConfirmButton: false,
    });

    // 把檔案傳傳給後端 由後端串接 imagga Api
    $.ajax({
        url: '/fileWatcher/imageGetTags',
        type: 'GET',
        complete: () => {
            // 無論ajax是否成功, 都會執行此處代碼
            status.checkingTags = false;
            getFilenameAndUpdate();
        },
        success: (data, textStatus, xhr) => {
            Swal.close();

            if (xhr.getResponseHeader("Content-Type").includes("text/csv")) {
                const blob = new Blob([data], { type: 'text/csv' });
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = getDate() + '_image_tags.csv';
                document.body.appendChild(a);
                a.click();
                window.URL.revokeObjectURL(url);
                document.body.removeChild(a);
            } else {
                if (Array.isArray(data)) {
                    Swal.fire({
                        icon: 'success',
                        title: '成功',
                        text: '成功上傳: ' + data.length + ' 張圖片',
                        customClass: {
                            confirmButton: 'custom-sweetalert-confirm-button'
                        }
                    });
                }
            }
        },
        error: function (error) {
            // 在API請求失敗時關閉等待中的SweetAlert2彈出式視窗
            Swal.close();

            let parsedError = JSON.parse(error.responseText);
            let errorTitle = errorMessage(parsedError).errorCodeAndType;
            let errorText = errorMessage(parsedError).errorMessage;

            // 顯示API請求失敗的SweetAlert2彈出式視窗
            Swal.fire({
                icon: 'error',
                title: '上傳失敗 ' + errorTitle,
                text: errorText,
                allowOutsideClick: true,
                customClass: {
                    confirmButton: 'custom-sweetalert-confirm-button'
                }
            });
        }
    });
}

function clickDeleteButton() {
    Swal.fire({
        icon: 'warning',
        title: '確定刪除?',
        text: '您確定要刪除這些文件嗎？',
        showCancelButton: true,
        confirmButtonText: '刪除',
        cancelButtonText: '取消',
        reverseButtons: true,
        customClass: {
            confirmButton: 'custom-sweetalert-delete-button',
            cancelButton: 'custom-sweetalert-cancel-button'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/fileWatcher/deleteFiles',
                type: 'DELETE',
                success: (response) => {
                    Swal.fire({
                        icon: 'success',
                        title: '已刪除',
                        text: response.message,
                        customClass: {
                            confirmButton: 'custom-sweetalert-confirm-button'
                        }
                    });
                    getFilenameAndUpdate();  // 更新文件列表
                },
                error: (error) => {
                    let parsedError = JSON.parse(error.responseText);
                    let errorTitle = errorMessage(parsedError).errorCodeAndType;
                    let errorText = errorMessage(parsedError).errorMessage;

                    Swal.fire({
                        icon: 'error',
                        title: '刪除失敗 ' + errorTitle,
                        text: errorText,
                        allowOutsideClick: true,
                        customClass: {
                            confirmButton: 'custom-sweetalert-confirm-button'
                        }
                    });
                }
            });
        }
    });
}

// 資料夾沒有圖檔時 顯示的預設字串
function updateEmptyMessageVisibility() {
    const downloadButton = $('#file-watcher-download-button');
    const deleteButton = $('#file-watcher-delete-button');

    if(!fileList.find('li').length) {
        fileList.append('<p>請先上傳圖檔</p>');

        // 禁用按鈕並改變其顏色
        downloadButton.prop('disabled', true).addClass('disabled-button');
        deleteButton.prop('disabled', true).addClass('disabled-button');
    } else {
        // 如果有檔案，啟用按鈕並移除灰色樣式
        downloadButton.prop('disabled', false).removeClass('disabled-button');
        deleteButton.prop('disabled', false).removeClass('disabled-button');
    }
}


function errorMessage(parsedError) {
    let messageParts = parsedError.message.split(", ");

    return {
        errorCodeAndType: "錯誤代碼: " + parsedError.errorCode,
        errorMessage: messageParts[1]
    };
}
function getDate() {
    const currentDate = new Date();
    const [year, month, day] = [
        currentDate.getFullYear(),
        String(currentDate.getMonth() + 1).padStart(2, '0'),
        String(currentDate.getDate()).padStart(2, '0')];
    return `${year}${month}${day}`;
}
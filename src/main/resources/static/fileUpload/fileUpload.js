const drop_zone = document.getElementById('drop-zone');
const drop_zone_h2 = document.querySelector("#drop-zone h2");
const fileInput = document.getElementById('fileInput');
const allowedExtensions = ['jpg', 'jpeg', 'png'];
const uploadedFiles = []; // 存儲已成功上傳的文件

fileInput.addEventListener('change', uploadFiles);
drop_zone.addEventListener('dragover', preventDefault);
drop_zone.addEventListener('drop', handleDrop);

// 避免重整頁面時 畫面閃爍 因此增加css延遲轉換設定
window.onload = function() {
    setTimeout(function() {
        drop_zone.style.transition = 'background-color 0.5s';
    }, 100); // 延遲 100 毫秒

    setTimeout(function() {
        drop_zone.style.transition = 'background-color 0.5s';
        drop_zone_h2.style.transition = 'background-color 0.5s';
    }, 100); // 延遲 100 毫秒
}

function preventDefault(event) {
    event.preventDefault();
}

async function handleDrop(event) {
    preventDefault(event);
    fileInput.files = event.dataTransfer.files;

    try {
        await uploadFiles();
    } catch (error) {
        console.error('上傳文件出錯:', error);
    }
}

function showInfoDialog(title, text, timerProgressBar = true) {
    Swal.fire({
        icon: 'info',
        title: title,
        text: text,
        allowOutsideClick: false,
        showConfirmButton: false,
        timerProgressBar: timerProgressBar,
    });
}

async function uploadFiles() {
    showInfoDialog('請稍等', '正在上傳圖片檔案');
    const totalFiles = fileInput.files.length;
    let successCount = 0;
    let errorCount = 0;
    const errorMessages = [];
    const formData = new FormData();

    for (const file of fileInput.files) {
        const fileExt = file.name.split('.').pop().toLowerCase();
        const isValidExtension = allowedExtensions.some(ext => ext === fileExt);

        if (isValidExtension && !uploadedFiles.includes(file.name)) {
            formData.append('files', file);
        } else {
            errorCount++;
            errorMessages.push(`${file.name} 格式不被支持`);
        }
    }

    try {
        showInfoDialog('請稍候', '正在上傳檔案至伺服器', false);
        const response = await fetch('/batchFileUpload', {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            successCount += formData.getAll('files').length;
            uploadedFiles.push(...formData.getAll('files').map(file => file.name));
            Swal.close();
        } else {
            errorMessages.push(`<br>請點選下方"確認"按鈕重整頁面`);
            Swal.close();
        }
    } catch (error) {
        errorMessages.push(`上傳失敗: ${error}`);
        Swal.close();
    }

    const baseMessage = `總共上傳: ${totalFiles}筆<br>成功: ${successCount}筆`;
    const errorMessage = errorMessages.length > 0 ? `<br>失敗: ${errorCount}筆<br><br>錯誤訊息:<br>${errorMessages.join('<br>')}` : '';

    Swal.fire({
        icon: 'info',
        title: '上傳統計',
        html: `${baseMessage}${errorMessage}`,
        allowOutsideClick: true,
        confirmButtonText: '確認',
        customClass: {
            confirmButton: 'custom-sweetalert-confirm-button'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            // 在 ”確認“ 按鈕被點擊時重整頁面
            location.reload();
        }
    });
}

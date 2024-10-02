function previewFile() {
    var preview = document.getElementById('previewImage');
    var fileInput = document.getElementById('image');
    var file = fileInput.files[0];
    var previewImageContainer = document.querySelector('.previewImage');

    var reader = new FileReader();

    reader.onloadend = function () {
        preview.src = reader.result;
        previewImageContainer.style.display = 'block'; // Hiển thị khi có hình ảnh
    };

    if (file) {
        reader.readAsDataURL(file);
    } else {
        preview.src = "";
        previewImageContainer.style.display = 'none'; // Ẩn khi không có hình ảnh
    }
}

function sidebarFile() {
    var preview = document.getElementById('sidebarImage');
    var fileInput = document.getElementById('imageSidebar');
    var file = fileInput.files[0];
    var previewImageContainer = document.querySelector('.sidebarImage');

    var reader = new FileReader();

    reader.onloadend = function () {
        preview.src = reader.result;
        previewImageContainer.style.display = 'block'; // Hiển thị khi có hình ảnh
    };

    if (file) {
        reader.readAsDataURL(file);
    } else {
        preview.src = "";
        previewImageContainer.style.display = 'none'; // Ẩn khi không có hình ảnh
    }
}

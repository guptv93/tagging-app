var app = {
  init : function() {
    app.folderName = $("#folder-name").text();
    app.currentIndex = $("#image-index").text();
    app.insertImage(app.currentIndex);
    $('.my-arrow-parent').on('click', app.changeImage);
  },

  insertImage : function(imgId) {
    url = "/image/" + app.folderName + "/" + imgId;
    $.ajax(url, {
      type : 'GET',
      success : function(result) {
        $img = $('#my-img');
        $img.attr("src", result);
        $img.removeClass("d-none");
        $("#image-index").text(app.currentIndex);
      },
      error : function(request, errorType, errorMessage) {
        alert(request.responseJSON.message);
        if(request.responseJSON.message.includes("exceed") ) {
          app.currentIndex--;
        }
      }
    });
  },

  changeImage : function(event) {
    event.preventDefault();
    if(this.id == "prev") {
      if(app.currentIndex == 1) {
        alert("This is the first image in the folder!");
      } else {
        app.currentIndex--;
      }
    } else {
      app.currentIndex++;
    }
    app.insertImage(app.currentIndex);
  }
}


$(document).ready(function(){
  app.init();
});


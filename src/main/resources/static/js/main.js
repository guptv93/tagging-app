var app = {
  init : function() {
    app.folderName = $("#folder-name").text();
    app.currentIndex = $("#image-index").text();
    app.insertImage(app.currentIndex);
    app.getTags();
    $('.my-arrow-parent').on('click', app.onArrowClick);
    $('#tags-div').on('click', '#add-tags-button', app.sendTags);
    $('#tags-div').on('click', '#refresh-tags-button', app.getTags);
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
        $('#thumbnail-button').attr("href", "\\thumbnails\\" + app.folderName + "\\" + app.currentIndex);
      },
      error : function(request, errorType, errorMessage) {
        alert(request.responseJSON.message);
        if(request.responseJSON.message.includes("exceed") ) {
          app.currentIndex--;
        }
      }
    });
  },

  onArrowClick : function(event) {
    event.preventDefault();
    if ($("#user-id-input").val() == "") {
      $("#user-id-input").addClass("is-invalid");
      return;
    } else {
      $("#user-id-input").removeClass("is-invalid");
    }
    if(this.id == "prev") {
      app.changeIndex(false);
    } else {
      app.changeIndex(true);
    };
    app.updateTags(app.selectedTags());
  },

  changeIndex : function(isNext) {
    if(isNext == false) {
      if(app.currentIndex == 1) {
        alert("This is the first image in the folder!");
      } else {
        app.currentIndex--;
      }
    } else {
      app.currentIndex++;
    }
    app.insertImage(app.currentIndex);
  },

  getTags : function() {
    url = "/tags/" + app.folderName;
    $.ajax(url, {
      type : 'GET',
      success : app.tagsSuccessCallback,
      error : app.tagsErrorCallback
    });
  },

  sendTags : function(payload) {
    url = "/tags/" + app.folderName;
    $.ajax(url, {
      type : 'POST',
      data : $('#add-tags-input').val(),
      contentType : 'application/json',
      success : app.tagsSuccessCallback,
      error : app.tagsErrorCallback
    });
  },

  updateTags : function(payload) {
    url = "/tags/" + app.folderName;
    $.ajax(url, {
      type : 'PUT',
      data : JSON.stringify(payload),
      contentType : 'application/json'
    });
  },

  selectedTags : function() {
    var tags = [];
    $('input').each(function() {
      $this = $(this);
      if ( $this.prop('checked') ) {
        tags.push($this.closest('div').find('label').html());
      }
    });
    return tags;
  },

  tagsSuccessCallback : function(result) {
    $tagsDiv = $('#tags-div');
    $tagsDiv.html(result);
  },

  tagsErrorCallback : function(request, errorType, errorMessage) {
    alert(request.responseJSON.message);
  }
}

$(document).ready(function(){
  app.init();
});


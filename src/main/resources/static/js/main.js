var app = {
  init : function() {
    app.folderName = $("#folder-name").text();
    app.currentIndex = $("#image-index").text();
    app.insertImage(app.currentIndex);
    app.getTags();
    $('.my-arrow-parent').on('click', app.onArrowClick);
    $('#submit-button').on('click', app.onSubmit);
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
        app.getImageTags();
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
    if(this.id == "prev") {
      app.changeIndex(false);
    } else {
      app.changeIndex(true);
    };
  },

  onSubmit : function(event) {
    event.preventDefault();
    if ($("#user-id-input").val() == "") {
      $("#user-id-input").addClass("is-invalid");
      return;
    } else {
      $("#user-id-input").removeClass("is-invalid");
    }
    app.submitTagDocument(app.getSubmitPayload());
    app.changeIndex(true);
  },

  submitTagDocument : function(payload) {
    url = "/imagetag/" + app.folderName + "/" + app.currentIndex;
    $.ajax(url, {
      type : 'POST',
      data : JSON.stringify(payload),
      contentType : 'application/json',
      error : app.tagsErrorCallback
    });
  },

  getImageTags : function() {
    url = "/imagetag/" + app.folderName + "/" + app.currentIndex;
    $.ajax(url, {
      type : 'GET',
      success : app.imageTagsSuccessCallback,
      error : app.tagsErrorCallback
    });
  },

  getSubmitPayload : function() {
    imageUrlSplits = $('#my-img').attr("src").split("?")[0].split("/");
    fileName = imageUrlSplits[imageUrlSplits.length - 1];
    payload = {
      "tags" : app.selectedTags(),
      "author" : $('#user-id-input').val(),
      "fileName" : fileName
    };
    return payload;
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
  },

  imageTagsSuccessCallback : function(result) {
    $input = $('#display-tags-input');
    $input.val(result);
  }
}

$(document).ready(function(){
  app.init();
});


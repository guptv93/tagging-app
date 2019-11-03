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

  getUrls : function() {
    var folderName = app.folderName;
    var currentIndex = app.currentIndex;
    return {
      image : `/image/${folderName}/${currentIndex}`,
      thumbnails : `/thumbnails/${folderName}/${currentIndex}`,
      imageTags : `/imagetags/${folderName}/${currentIndex}`,
      tags : `/tags/${folderName}`
    };
  },

  insertImage : function(imgId) {
    var lastIndex = app.currentIndex;
    app.currentIndex = imgId;
    var urls = app.getUrls();
    $.ajax(urls.image, {
      type : 'GET',
      success : function(result) {
        $img = $('#my-img');
        $img.attr("src", result);
        $img.removeClass("d-none");
        $("#image-index").text(app.currentIndex);
        app.getImageTags();
        $('#thumbnail-button').attr("href", urls.thumbnails);
      },
      error : function(request, errorType, errorMessage) {
        alert(request.responseJSON.message);
        if(request.status == 400 ) {
          app.currentIndex = lastIndex;
        }
      }
    });
  },

  onArrowClick : function(event) {
    event.preventDefault();
    if(this.id == "next") {
      app.insertImage(parseInt(app.currentIndex)+1);
    } else if(this.id == "prev" && app.currentIndex != 1) {
      app.insertImage(app.currentIndex-1);
    } else {
      alert("This is the first image in the folder!");
    }
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
    app.insertImage(app.currentIndex+1);
  },

  submitTagDocument : function(payload) {
    var url = app.getUrls().imageTags;
    $.ajax(url, {
      type : 'POST',
      data : JSON.stringify(payload),
      contentType : 'application/json',
      error : app.tagsErrorCallback
    });
  },

  getImageTags : function() {
    var url = app.getUrls().imageTags;
    $.ajax(url, {
      type : 'GET',
      success : app.imageTagsSuccessCallback,
      error : app.errorCallback
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

  getTags : function() {
    var url = app.getUrls().tags;
    $.ajax(url, {
      type : 'GET',
      success : app.tagsSuccessCallback,
      error : app.errorCallback
    });
  },

  sendTags : function(payload) {
    var url = app.getUrls().tags;
    $.ajax(url, {
      type : 'POST',
      data : $('#add-tags-input').val(),
      contentType : 'application/json',
      success : app.tagsSuccessCallback,
      error : app.errorCallback
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

  imageTagsSuccessCallback : function(result) {
    var $input = $('#display-tags-input');
    $input.val(result);
  },

  errorCallback : function(request, errorType, errorMessage) {
    alert(request.responseJSON.message);
  }

}

$(document).ready(function(){
  app.init();
});


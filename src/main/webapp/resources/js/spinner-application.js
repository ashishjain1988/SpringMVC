$(document).ready(function(){
    var next = 1;
    $(".add-more").click(function(e){
        e.preventDefault();
        var addto = "#divValues" + next;
        var addRemove = "#span" + (next);
        next = next + 1;
        var newIn = ' <div class="form-group wrapper" id="divValues'+next+'"><label for="InputEmail">Parameter Name</label><span id="span'+next+'"></span>'+
        '<div class="input-group">'+
            '<input type="text" class="form-control" id="parameterName'+next+'" name="InputEmail" placeholder="Parameter Name" required>'+
            '<span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>'+
        '</div>'+
        '<label for="InputEmail">Parameter Value</label>'+
        '<div class="input-group">'+
        	'<form:input path=""/>'+
            '<input type="text" class="form-control" id="parameterValue'+next+'" name="InputEmail" placeholder="Parameter Value" required>'+
            '<span class="input-group-addon"><span class="glyphicon glyphicon-asterisk"></span></span>'+
        '</div>'+
        '</div>';
       // var newIn = '<input autocomplete="off" class="input form-control" id="field' + next + '" name="field' + next + '" type="text">';
        var newInput = $(newIn);
        var removeBtn = '<button id="remove' + (next - 1) + '" class="btn btn-danger remove-me" >-</button></div><div id="field">';
        var removeButton = $(removeBtn);
        $(addto).after(newInput);
        $(addRemove).after(removeButton);
        $("#divValues" + next).attr('data-source',$(addto).attr('data-source'));
        $("#count").val(next);  
        
            $('.remove-me').click(function(e){
                e.preventDefault();
                var fieldNum = this.id.charAt(this.id.length-1);
                var fieldID = "#divValues" + fieldNum;
                $(this).remove();
                $(fieldID).remove();
            });
    });
    
    /*$(".inputFile").fileinput({showUpload: false, maxFileCount: 10, mainClass: "input-group-lg"});*/
    

    
});
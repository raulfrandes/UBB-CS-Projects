$(document).ready(function(){
    $(document).on('click', 'option', function() {
        if ($(this).parent().attr('id') === 'left-list') {
            $(this).remove();
            $('#right-list').append($(this));
        }
        else {
            $(this).remove();
            $('#left-list').append($(this));
        }
    });
});
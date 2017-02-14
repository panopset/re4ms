$(document).ready(
    function() {
        $('.copyCode').click(
            function() {
                if ($('#tmp').length) {
                    $('#tmp').remove();
                }
                var clickText = $(this).text();
                $('<textarea id="tmp" />')
                    .appendTo($(this))
                    .val(clickText)
                    .focus()
                    .select();
        return false;
    });
$(':not(.copyCode)').click(
    function(){
        $('#tmp').remove();
    });

});


function createTable()
{
    console.log('create table function is called.');
    $(function() {
    $('#table-1').dataTable( {
        "ajax": 'assets/javascripts/table_data.json',
        "columns": [
            { "data": "COMPANY",        "title": "COMPANY"},
            { "data": "TICKER",         "title": "TICKER" },
            { "data": "ACTIVITY",       "title": "ACTIVITY" },
            { "data": "WEIGHT",         "title": "WEIGHT" },
            { "data": "AVG COST",       "title": "AVG COST" },
            { "data": "PRICE",          "title": "PRICE" },
            { "data": "TOTAL RETURN",   "title": "TOTAL RETURN" },
            { "data": "DAILY RETURN",   "title": "DAILY RETURN" }
        ],
        "pageLength": 10,
        "lengthChange": true,
        "lengthMenu": [[10, -1], ['VIEW LESS', 'VIEW ALL']],
        "dom": '<"clearfix"r<"dataTables_wrapper"t>il>',
        "language": {
            "lengthMenu": " _MENU_ ",
            "info": "Showing _END_ of _TOTAL_ companies"
        },
        "order": [3, 'desc' ],
        "columnDefs": [
            { className: "hidden-xs hidden-sm dt-body-right", "targets": [4, 5, 7] },
            { className: "hidden-xs hidden-sm", "targets": [0, 2] },
            { className: "dt-body-right", "targets": [3, 6] }
        ]
    } );
    
    var table = $('#table-1').DataTable();

    $('#table-less').click(function () {
        table.page.len( 10 ).draw(false);
    })
    $('#table-all').click(function () {
        table.page.len(-1).draw(false);
    })

})
}
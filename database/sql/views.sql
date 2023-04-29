create view view_jenis_nomor as
    select `id`, `deskripsi`, json_object('id', id, 'deskripsi', deskripsi) as 'json'
    from jenis_nomor;

create view view_jenis_nomor_json as
    select json_group_array(`json`) as 'json'
    from view_jenis_nomor;
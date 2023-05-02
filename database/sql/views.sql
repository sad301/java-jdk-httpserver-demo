create view view_configuration as
    select `key`, `value`, json_object('key', `key`, 'value', `value`) as 'json'
    from `configuration`;

create view view_configuration_json as
    select json_group_array(`json`) as 'json'
    from view_configuration;

create view view_jenis_nomor as
    select `id`, `deskripsi`, json_object('id', id, 'deskripsi', deskripsi) as 'json'
    from jenis_nomor;

create view view_jenis_nomor_json as
    select json_group_array(`json`) as 'json'
    from view_jenis_nomor;
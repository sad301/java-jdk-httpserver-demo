<?php

class JenisNomorDAO {
    private $sqlite;
    public function __construct($sqlite) {
        $this->sqlite = $sqlite;
    }
    public function retrieve() {
        return $this->sqlite->querySingle("select `json` from view_jenis_nomor_json");
    }
}

?>
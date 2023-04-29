<?php

class JenisNomorDAO {
    private $sqlite;
    public function __construct(SQLite3 $sqlite) {
        $this->sqlite = $sqlite;
    }
    public function create($jenisNomor) {
        $stmt = $this->sqlite->prepare("insert into jenis_nomor values (:id, :deskripsi)");
        $stmt->bindValue(":id", $jenisNomor["id"]);
        $stmt->bindValue(":deskripsi", $jenisNomor["deskripsi"]);
        $stmt->execute();
    }
    public function retrieve() {
        return $this->sqlite->querySingle("select `json` from view_jenis_nomor_json");
    }
    public function delete($jenisNomor) {
        $stmt = $this->sqlite->prepare("delete from jenis_nomor where id=:id");
        $stmt->bindValue(":id", $jenisNomor["id"]);
        $stmt->execute();
    }
}

?>
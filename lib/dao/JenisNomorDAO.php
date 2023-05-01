<?php

class JenisNomorDAO {

    private $sqlite;
    public $errorCode;
    public $errorMessage;

    public function __construct(SQLite3 $sqlite) {
        $this->sqlite = $sqlite;
    }

    public function create($jenisNomor) {
        $stmt = $this->sqlite->prepare("insert into jenis_nomor values (:id, :deskripsi)");
        $stmt->bindValue(":id", $jenisNomor["id"]);
        $stmt->bindValue(":deskripsi", $jenisNomor["deskripsi"]);
        $stmt->execute();
        $this->errorCode = $this->sqlite->lastErrorCode();
        $this->errorMessage = $this->sqlite->lastErrorMsg();
        if ($this->errorCode != 0) return false;
        return true;
    }

    public function retrieve() {
        return $this->sqlite->querySingle("select `json` from view_jenis_nomor_json");
    }

    public function delete($jenisNomor) {
        $stmt = $this->sqlite->prepare("delete from jenis_nomor where id=:id");
        $stmt->bindValue(":id", $jenisNomor["id"]);
        $stmt->execute();
        $this->errorCode = $this->sqlite->lastErrorCode();
        $this->errorMessage = $this->sqlite->lastErrorMsg();
        if ($this->errorCode != 0) return false;
        return true;
    }
}

?>
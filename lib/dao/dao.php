<?php

class DAO {

    protected $sqlite;
    public $errorCode;
    public $errorMessage;

    public function __construct($sqlite) {
        $this->sqlite = $sqlite;
    }
}

?>
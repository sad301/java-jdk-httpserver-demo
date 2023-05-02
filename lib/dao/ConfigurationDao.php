<?php

require __DIR__ . "/dao.php";

class ConfigurationDao extends Dao {

    public function __construct($sqlite) {
        parent::__construct($sqlite);
    }

    public function retrieve() {
        return $this->sqlite->querySingle("select `json` from view_configuration_json");
    }
}

?>
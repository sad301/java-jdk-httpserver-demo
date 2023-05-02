<?php

class Dao {
    protected $sqlite;
    public $errorCode;
    public $errorMessage;
    
}

class PegawaiDao extends Dao {
    public $sample;
    public function __construct($message) {
        parent::__construct($message);
    }
}

$test = new PegawaiDao("Hello World");
echo($test->message);
echo($test->sample);

?>
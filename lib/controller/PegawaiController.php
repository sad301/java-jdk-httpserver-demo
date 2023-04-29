<?php

use Psr\Container\ContainerInterface;

class PegawaiController {

    private $container;

    public function __construct(ContainerInterface $container) {
        $this->container = $container;
    }

    public function retrieve($req, $res) {
        $res->getBody()->write(json_encode(["message" => "api pegawai"]));
        return $res->withHeader("Content-Type", "application/json");
    }
}

?>